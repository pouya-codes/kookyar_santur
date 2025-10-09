package com.PouyaApp.KookYaRSantooR;

import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

public class Content extends AppCompatActivity {
    public String fonts = "BNazanin.ttf";
    private TextView help;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        
        // Setup toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        Bundle data = getIntent().getExtras();
        int content = data.getInt("content");
        help = (TextView) findViewById(R.id.tv_content);
        if (content == 1)
            help.setText(getText(R.string.text1));
        if (content == 2)
            help.setText(getText(R.string.text2));
        if (content == 3)
            help.setText(getText(R.string.text3));

        Typeface face = Typeface.createFromAsset(getAssets(), "font/" + fonts
                + "");

        // help.setText(Html.fromHtml(getString(R.string.text1)));
        help.setTypeface(face);
        
        // Ensure text color is visible in both light and dark modes
        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_NO) {
            // Light mode - use dark text
            help.setTextColor(Color.parseColor("#212121"));
        } else {
            // Dark mode - use light text
            help.setTextColor(Color.parseColor("#E8E8E8"));
        }

    }
}
