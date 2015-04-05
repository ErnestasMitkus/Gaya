package com.ernestas.gaya.Ships.Arsenal;

import com.ernestas.gaya.Ships.Arsenal.Bullets.Bullet;
import com.ernestas.gaya.Ships.Arsenal.Bullets.SimpleBullet;
import com.ernestas.gaya.Ships.PlayerShip;
import com.ernestas.gaya.Ships.Ship;
import com.ernestas.gaya.Util.Vectors.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class Arsenal {

    public enum BulletType {
        singleBullet, doubleBullet, tripleBullet, pentaBullet
    };

    private Ship ship;
    private boolean isEnemyShip;
    private int damage;

    BulletType bulletType;

    public Arsenal(Ship ship) {
        this(ship, 1);
    }

    public Arsenal(Ship ship, int damage) {
        this.ship = ship;
        this.damage = damage;
        bulletType = BulletType.singleBullet;
        isEnemyShip = !(ship instanceof PlayerShip);
    }

    public void resetBulletType() {
        bulletType = BulletType.singleBullet;
    }

    public void upgradeBulletType() {
        BulletType newType;
        switch(bulletType) {
            case singleBullet:
                newType = BulletType.doubleBullet;
                break;
            case doubleBullet:
                newType = BulletType.tripleBullet;
                break;
            case tripleBullet:
            case pentaBullet:
            default:
                newType = BulletType.pentaBullet;
                break;
        }

        bulletType = newType;
    }

    public void setBulletType(BulletType newType) {
        bulletType = newType;
    }

    public void shoot() {
        List<Bullet> bulletList = new ArrayList<Bullet>();

        switch(bulletType) {
            case singleBullet:
                handleSingleBulletShot(bulletList);
                break;
            case doubleBullet:
                handleDoubleBulletShot(bulletList);
                break;
            case tripleBullet:
                handleTripleBulletShot(bulletList);
                break;
            case pentaBullet:
                handlePentaBulletShot(bulletList);
                break;
            default:
                break;
        }

        if (damage != 1) {
            setDamageToAllBullets(bulletList, damage);
        }
        ship.getLevel().addBullets(bulletList);
    }

    private void setDamageToAllBullets(List<Bullet> bullets, int damage) {
        for (int i = 0; i < bullets.size(); ++i) {
            bullets.get(i).setDamage(damage);
        }
    }

    private void handleSingleBulletShot(List<Bullet> bullets) {
        if (!isEnemyShip) {
            bullets.add(new SimpleBullet(ship, new Vector2f(ship.getBounds().getX(), ship.getBounds().getY()), new Vector2f(0, 1)));
        } else {
            bullets.add(new SimpleBullet(ship, new Vector2f(ship.getBounds().getX(), ship.getBounds().getY()), new Vector2f(0, -1)));
        }
    }

    private void handleDoubleBulletShot(List<Bullet> bullets) {
        bullets.add(new SimpleBullet(ship, new Vector2f(ship.getBounds().getX() - 10, ship.getBounds().getY()), new Vector2f(0, 1)));
        bullets.add(new SimpleBullet(ship, new Vector2f(ship.getBounds().getX() + 10, ship.getBounds().getY()), new Vector2f(0, 1)));
    }

    private void handleTripleBulletShot(List<Bullet> bullets) {
        bullets.add(new SimpleBullet(ship, new Vector2f(ship.getBounds().getX(), ship.getBounds().getY()), new Vector2f(0, 1))) ;
        bullets.add(new SimpleBullet(ship, new Vector2f(ship.getBounds().getX(), ship.getBounds().getY()), new Vector2f(-0.35f, 1)));
        bullets.add(new SimpleBullet(ship, new Vector2f(ship.getBounds().getX(), ship.getBounds().getY()), new Vector2f(0.35f, 1)));
    }

    private void handlePentaBulletShot(List<Bullet> bullets) {
        bullets.add(new SimpleBullet(ship, new Vector2f(ship.getBounds().getX(), ship.getBounds().getY()), new Vector2f(0, 1))) ;
        bullets.add(new SimpleBullet(ship, new Vector2f(ship.getBounds().getX(), ship.getBounds().getY()), new Vector2f(-0.25f, 1)));
        bullets.add(new SimpleBullet(ship, new Vector2f(ship.getBounds().getX(), ship.getBounds().getY()), new Vector2f(0.25f, 1)));

        bullets.add(new SimpleBullet(ship, new Vector2f(ship.getBounds().getX() - 20, ship.getBounds().getY() - 15), new Vector2f(0, 1)));
        bullets.add(new SimpleBullet(ship, new Vector2f(ship.getBounds().getX() + 20, ship.getBounds().getY() - 15), new Vector2f(0, 1)));
    }

}
