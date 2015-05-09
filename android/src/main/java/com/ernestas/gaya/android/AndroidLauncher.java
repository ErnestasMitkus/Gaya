package com.ernestas.gaya.android;

import android.os.Bundle;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.ernestas.gaya.GayaEntry;
import com.ernestas.gaya.Util.Settings.Settings;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

        Settings.getInstance().setFrameRate(60);


//        config.useGL30 = true; // Use GL30 for performance improvement


        //https://code.google.com/p/libgdx-users/wiki/PreventSleepAndDim
		initialize(new GayaEntry(GayaEntry.ANDROID), config);
	}
}
