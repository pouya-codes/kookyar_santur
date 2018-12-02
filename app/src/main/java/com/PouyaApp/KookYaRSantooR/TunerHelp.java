package com.PouyaApp.KookYaRSantooR;
import com.PouyaApp.KookYaRSantooR.R;


import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
//import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
        Toolbar toolbar =(Toolbar) findViewById(R.id.toolbar) ;
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}
}
