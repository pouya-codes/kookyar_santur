package com.PouyaApp.KookYaRSantooR;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;

public class StartScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
        String model = Build.MODEL;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor editor = prefs.edit();

        if (model.compareToIgnoreCase("HUAWEI P6-U06") == 0|| model.compareToIgnoreCase("S5201") == 0 || model.compareToIgnoreCase("HUAWEI Y520-U22")==0 || model.compareToIgnoreCase("HUAWEI Y360-U61") == 0  || model.compareToIgnoreCase("HTC Desire 616 dual sim") == 0 || model.compareToIgnoreCase("HTC Desire 816 dual sim") == 0 || model.contains("GT7582") || model.compareToIgnoreCase("Lenovo A606") == 0 || model.compareToIgnoreCase("HTC Desire 816G dual sim") == 0 || model.compareToIgnoreCase("HUAWEI G750-U10") == 0 || model.compareToIgnoreCase("V8") == 0|| model.compareToIgnoreCase("Lenovo S650") == 0|| model.compareToIgnoreCase("Vsun H9") == 0 || model.compareToIgnoreCase("LEAGOO_Lead5") == 0 || model.compareToIgnoreCase("HTC One_E8 dual sim") == 0 )   {

            editor.putBoolean("tunerChange", true);

        }
        else {
            editor.putBoolean("tunerChange", false);
        }
        editor.commit();
        Thread timer = new Thread() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {

                    sleep(3000);

                } catch (Exception e) {

                    e.printStackTrace();
                } finally {

                    Intent Menu = new Intent(StartScreen.this, MainMenu.class);
                    startActivity(Menu);

                }
            }


        };
        timer.start();
    }

    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }

}
