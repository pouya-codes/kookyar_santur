package com.PouyaApp.KookYaRSantooR;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
import android.view.Menu;
import android.widget.TextView;

public class StartScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
        
        // Set version information automatically
        setVersionInfo();
        
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
    
    /**
     * Automatically set version information from build.gradle
     */
    private void setVersionInfo() {
        try {
            PackageManager packageManager = getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            
            // Get version name and code
            String versionName = packageInfo.versionName;
            long versionCode;
            
            // Handle different API levels for getLongVersionCode
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                versionCode = packageInfo.getLongVersionCode();
            } else {
                versionCode = packageInfo.versionCode;
            }
            
            // Update UI elements
            TextView versionNameTextView = findViewById(R.id.version_name);
            TextView versionCodeTextView = findViewById(R.id.version_code);
            
            if (versionNameTextView != null) {
                versionNameTextView.setText(versionName);
            }
            
            if (versionCodeTextView != null) {
                versionCodeTextView.setText(String.valueOf(versionCode));
            }
            
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            // Fallback to default values if unable to get version info
            TextView versionNameTextView = findViewById(R.id.version_name);
            TextView versionCodeTextView = findViewById(R.id.version_code);
            
            if (versionNameTextView != null) {
                versionNameTextView.setText("4.0");
            }
            
            if (versionCodeTextView != null) {
                versionCodeTextView.setText("40");
            }
        }
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
