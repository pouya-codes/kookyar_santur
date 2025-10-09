package com.PouyaApp.KookYaRSantooR;
import com.PouyaApp.KookYaRSantooR.R;


import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.core.app.NavUtils;
//import androidx.appcompat.app.ActionBarActivity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class TunerHelp extends AppCompatActivity {

	public String fonts="BNazanin.ttf";



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
		setContentView(R.layout.activity_tuner_help);
		Typeface face = Typeface.createFromAsset(getAssets(), "font/"+fonts+"");
		TextView help = (TextView) findViewById(R.id.tunerHelp) ;
		help.setTypeface(face);
		String str_tv = (String) help.getText().toString();
		help.setText(PersianReshape.reshape(str_tv));
		
		// Ensure text color is visible in both light and dark modes
		int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
		if (nightModeFlags == Configuration.UI_MODE_NIGHT_NO) {
			// Light mode - use dark text
			help.setTextColor(Color.parseColor("#212121"));
		} else {
			// Dark mode - use light text
			help.setTextColor(Color.parseColor("#E8E8E8"));
		}
		
        Toolbar toolbar =(Toolbar) findViewById(R.id.toolbar) ;
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}
}
