package com.ernestas.gaya.Validator;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.ernestas.gaya.AI.AI;
import com.ernestas.gaya.AI.FloatAI;
import com.ernestas.gaya.Exceptions.GayaException;
import com.ernestas.gaya.Gameplay.Scenario;
import com.ernestas.gaya.Gameplay.Wave;
import com.ernestas.gaya.ResourceLoaders.ResourceLoader;
import com.ernestas.gaya.Ships.EnemyShip;
import com.ernestas.gaya.Util.IntWrapper;
import com.ernestas.gaya.Util.Settings.GameSettings;
import com.ernestas.gaya.Util.Settings.Settings;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class JSONToScenarioConverter {

    private static class EnemyStruct {
        public String name = "";
        public int health = 0;
        public float speed = 0;
        public AI ai = null;
        public String spriteName = "enemyShip32";
        public Sprite sprite;

        public boolean isValid() {
            if (name == null) {
                return false;
            }
            if (health <= 0) {
                return false;
            }
            if (speed <= 0) {
                return false;
            }
            if (!spriteName.equalsIgnoreCase("greenShip")) {
                return false;
            }

            return true;
        }
    }

    private JSONToScenarioConverter() {}

    public static Scenario convertFromFile(String path) throws GayaException {
        String str = new Validator(path).fileToString();

        return convert(str);
    }

    public static Scenario convert(String str) {
        Scenario scenario = new Scenario();
        Map<String, EnemyStruct> map = new HashMap<String, EnemyStruct>();

        try {
            JSONObject obj = new JSONObject(str);

            parseName(scenario, obj);
            parseAuthor(scenario, obj);
            parseEnemies(map, obj);
            parseWaves(scenario, map, obj);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return scenario;
    }

    private static void parseName(Scenario scenario, JSONObject obj) throws JSONException {
        scenario.setName(obj.getString("name"));
    }

    private static void parseAuthor(Scenario scenario, JSONObject obj) throws JSONException {
        scenario.setAuthor(obj.getString("author"));
    }

    private static void parseEnemies(Map<String, EnemyStruct> map, JSONObject obj) throws JSONException {
        JSONArray enemyArray = obj.getJSONArray("enemies");
        for (int i = 0; i < enemyArray.length(); ++i) {
            EnemyStruct enemy = parseEnemy(enemyArray.getJSONObject(i));
            if (enemy.isValid()) {
                map.put(enemy.name, enemy);
            }
        }
    }

    private static EnemyStruct parseEnemy(JSONObject obj) throws JSONException {
        EnemyStruct struct = new EnemyStruct();

        struct.name = obj.getString("name");
        struct.health = obj.getInt("health");
        struct.speed = (float) obj.getInt("speed");
        struct.ai = parseAI(obj.getString("ai"));
        struct.spriteName = obj.getString("sprite");

        if (GameSettings.getInstance().getResourceLoader() != null)
            struct.sprite = GameSettings.getInstance().getResourceLoader().getResource(ResourceLoader.resourceIdFromName(struct.spriteName));

        return struct;
    }

    public static AI parseAI(String aiString) {
        if (aiString.equalsIgnoreCase("float")) {
            return new FloatAI();
        }

        return null;
    }

    private static void parseWaves(Scenario scenario, Map<String, EnemyStruct> map, JSONObject obj) throws JSONException {
        JSONArray wavesArray = obj.getJSONArray("waves");
        for (int i = 0; i < wavesArray.length(); ++i) {
            Wave wave = parseWave(map, wavesArray.getJSONObject(i));
            scenario.addWave(wave);
        }
    }

    // Cannot be tested because of sprite loading
    private static Wave parseWave(Map<String, EnemyStruct> map, JSONObject obj) throws JSONException {
        Wave.Builder waveBuilder = new Wave.Builder();

        waveBuilder.withId(obj.getInt("id"));

        JSONArray spawnArray = obj.getJSONArray("spawn");
        for (int i = 0; i < spawnArray.length(); ++i) {
            Wave.EnemyWithOffset enemy = new Wave.EnemyWithOffset(0, 0, null);
            JSONArray spawnEvent = spawnArray.getJSONArray(i);



            if (!mapContainsKey(map, spawnEvent.getString(0))) {
                throw new JSONException("Undefined enemy: \"" + spawnEvent.getString(0) + "\".");
            } else {
                EnemyStruct enemyStruct = map.get(spawnEvent.getString(0));
                float offsetX = evaluateExpression(spawnEvent.getString(1), map);
                float offsetY = evaluateExpression(spawnEvent.getString(2), map);

                if (offsetY < 0) {
                    throw new JSONException("offsetY is below 0");
                }
                if (offsetX > Settings.getInstance().getWidth()) {
                    throw new JSONException("offsetX is out of screen bounds(" + Settings.getInstance().getWidth() + ")");
                }
                if (offsetX < 0) {
                    throw new JSONException("offsetX cannot be below 0");
                }

                // got offsets and enemyStruct
                enemy.offsetX = offsetX;
                enemy.offsetY = offsetY;

                enemy.ship = new EnemyShip.Builder()
                                .withSpeed(enemyStruct.speed)
                                .withHealth(enemyStruct.health)
                                .withAI(enemyStruct.ai.clone())
                                .withSprite(
                                    GameSettings.getInstance().getResourceLoader().getResource(
                                        ResourceLoader.resourceIdFromName(enemyStruct.spriteName)
                                    )
                                )
                                .build();
            }
            waveBuilder.withEnemy(enemy);
        }


        return waveBuilder.build();
    }

    private static boolean mapContainsKey(Map<String, EnemyStruct> map, String s) {
        for (String key : map.keySet()) {
            if (key.compareTo(s) == 0) {
                return true;
            }
        }

        return false;
    }

    public static float evaluateExpression(String expression, Map<String, EnemyStruct> map) throws JSONException {
        float result = 0f;

        int right = Settings.getInstance().getWidth();
        int middle = right / 2;

        // Replace keywords: [MIDDLE, RIGHT]
        expression = expression.replaceAll("MIDDLE", middle + "");
        expression = expression.replaceAll("RIGHT", right + "");

        // Replace @ENEMY
        if (map != null) {
            for (EnemyStruct enemyStruct : map.values()) {
                expression = expression.replaceAll("@" + enemyStruct.name,
                                                        32 * Settings.getInstance().getScale() + "");
                                                            // TODO: Fix to be a correct value
            }
        }

        // priority 1: *
        for (int i = 0; i < expression.length(); ++i) {
            char c = expression.charAt(i);

            if (c == '*') {
                IntWrapper startIndex = new IntWrapper();
                IntWrapper endIndex = new IntWrapper();
                float firstNr = getNumberFromIndex(expression, i, startIndex, true);
                float secondNr = getNumberFromIndex(expression, i, endIndex, false);

                float answer = firstNr * secondNr;
                String equation = expression.substring(startIndex.integer, endIndex.integer + 1);


                expression = expression.replace(equation, answer + "");
                i = 0;
            }
        }

        // priority 2: + -
        for (int i = 0; i < expression.length(); ++i) {
            char c = expression.charAt(i);

            if (c == '+' || c == '-') {
                IntWrapper startIndex = new IntWrapper();
                IntWrapper endIndex = new IntWrapper();
                float firstNr = getNumberFromIndex(expression, i, startIndex, true);
                float secondNr = getNumberFromIndex(expression, i, endIndex, false);

                float answer = (c == '+' ?
                                    firstNr + secondNr :
                                    firstNr - secondNr);

                String equation = expression.substring(startIndex.integer, endIndex.integer + 1);


                expression = expression.replace(equation, answer + "");
                i = 0;
            }
        }

        try {
            result = Float.parseFloat(expression);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Made to count only with 1 digit after decimal. If the floating number is on the right side of the index, then it will
     * return unwanted result
     * */
    public static float getNumberFromIndex(String line, int index, IntWrapper finishIndex, boolean goLeft) throws JSONException {
        float result = 0;
        boolean found = false;
        boolean dotAlreadyFound = false;
        int multiplier = 1;

        while (true) {
            if (goLeft) {
                index--;
                if (index < 0) {
                    break;
                }
            } else {
                index++;
                if (index > line.length() - 1) {
                    break;
                }
            }

            // found space?
            if (line.charAt(index) == ' ' || line.charAt(index) == '\t') {
                if (found) {
                    break;
                }
                continue;
            }

            // found number?
            if (line.charAt(index) >= '0' && line.charAt(index) <= '9') {
                int digit = Integer.parseInt(line.charAt(index) + "");
                found = true;
                if (goLeft) {
                    result = digit * multiplier + result;
                    multiplier *= 10;
                } else {
                    if (dotAlreadyFound) {
                        multiplier *= 10;
                        result = result + (digit * 1f) / multiplier;
                    } else {
                        result = result * 10 + digit;
                    }
                }
            }

            // found dot(floating number)
            if (line.charAt(index) == '.') {
                if (dotAlreadyFound) {
                    throw new JSONException("Double floating point in expression");
                } else {
                    dotAlreadyFound = true;
                    if (goLeft) {
                        while (result > 1) {
                            result /= 10;
                        }
                    }
                    multiplier = 1;
                }
            }

        }

        if (!found) {
            throw new JSONException("Bad expression. Left number from the action is not found.");
        }
        if (finishIndex != null) {
            finishIndex.integer = index + (goLeft ? 1 : -1);
        }
        return result;
    }
}
