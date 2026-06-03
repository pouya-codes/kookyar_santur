package com.PouyaApp.KookYaRSantooR;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import android.view.Menu;
import android.widget.TextView;

public class StartScreen extends AppCompatActivity {

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable launchRunnable = () -> {
        Intent menu = new Intent(StartScreen.this, MainMenu.class);
        startActivity(menu);
        finish();
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
        
        // Set version information automatically
        setVersionInfo();
        
        String model = Build.MODEL;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();

        // Legacy device-specific tuner workaround
        editor.putBoolean("tunerChange", false);
        editor.apply();

        // Launch main menu after splash delay
        handler.postDelayed(launchRunnable, 3000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(launchRunnable);
    }
    
    private void setVersionInfo() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String versionName = packageInfo.versionName;
            long versionCode = Build.VERSION.SDK_INT >= Build.VERSION_CODES.P 
                    ? packageInfo.getLongVersionCode() 
                    : packageInfo.versionCode;
            
            TextView versionNameTextView = findViewById(R.id.version_name);
            TextView versionCodeTextView = findViewById(R.id.version_code);
            
            if (versionNameTextView != null) {
                versionNameTextView.setText(versionName);
            }
            if (versionCodeTextView != null) {
                versionCodeTextView.setText(String.valueOf(versionCode));
            }
        } catch (PackageManager.NameNotFoundException e) {
            // Fallback values
            TextView versionNameTextView = findViewById(R.id.version_name);
            TextView versionCodeTextView = findViewById(R.id.version_code);
            if (versionNameTextView != null) versionNameTextView.setText("4.1");
            if (versionCodeTextView != null) versionCodeTextView.setText("47");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}
