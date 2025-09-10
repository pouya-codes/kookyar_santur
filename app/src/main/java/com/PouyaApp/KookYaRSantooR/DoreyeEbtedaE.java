package com.PouyaApp.KookYaRSantooR;



import com.PouyaApp.KookYaRSantooR.R.id ;
import com.PouyaApp.KookYaRSantooR.R;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

public class DoreyeEbtedaE extends Activity implements View.OnClickListener{
	private Button[] buttons = new Button[10] ;
	private int[] buttonsId = {id.ebtedaei1 , id.ebtedaei2 , id.ebtedaei3 , id.ebtedaei4 , id.ebtedaei5 , id.ebtedaei6 , id.ebtedaei7 , id.ebtedaei8 , id.ebtedaei9 , id.ebtedaei10 }; 
	private int[] buttonsText = {R.string.ebtedaei1 , R.string.ebtedaei2 , R.string.ebtedaei3 , R.string.ebtedaei4 , R.string.ebtedaei5 , R.string.ebtedaei6 , R.string.ebtedaei7 , R.string.ebtedaei8 , R.string.ebtedaei9 , R.string.ebtedaei10 };
	public String fonts = "BZar.ttf";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_doreye_ebtedaei);
			setFace();

	}

	private void setFace() {
		// TODO Auto-generated method stub
		Typeface face = Typeface.createFromAsset(getAssets(), "font/" + fonts
				+ "");
		for (int i = 0; i < buttons.length; i++) {
			buttons[i] =(Button) findViewById(buttonsId[i]) ;
			buttons[i].setText(getString(buttonsText[i]));
			buttons[i].setTypeface(face);
			buttons[i].setOnClickListener(this);
		}
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int i = v.getId() ;
		Bundle data = new Bundle();
		double[] kookMiditone ={} ;
		if(i==id.ebtedaei1 ||i==id.ebtedaei2||i==id.ebtedaei3||i==id.ebtedaei4||i==id.ebtedaei5) {
			kookMiditone= new double[] { 51.5, 53, 55, 56.5, 58, 60, 61.5, 63, 65, 63.5,
					65, 67, 68.5, 70, 72, 74, 75, 77, 75.5, 77, 79, 80, 82, 84, 86,
					87, 89 };
		}
		else if(i==id.ebtedaei6) {
			kookMiditone= new double[] { 51.5, 53, 55, 56.5, 58, 60, 61.5, 63, 65,
					63.5, 65, 67, 68.5, 70, 72, 73.5, 75, 77, 75.5, 77, 79, 80, 82,
					84, 85.5, 87, 89 };
		}
		else if(i==id.ebtedaei7 ) {
			kookMiditone= new double[] { 52, 53, 55, 56.5, 59, 60, 62, 63, 65,
					63.5, 65, 67, 68.5, 71, 72, 74, 75, 77, 75.5, 77, 79, 80, 82,
					84, 86, 87, 89 };
		}
		else if(i==id.ebtedaei8 ) {
			kookMiditone= new double[]  { 52, 53, 55, 56.5, 59, 60, 62, 63, 65,
					64, 65, 67, 68.5, 71, 72, 74, 75, 77, 75.5, 77, 79, 80, 82,
					84, 86, 87, 89 };
		}
		else if(i==id.ebtedaei9 ) { 
			kookMiditone= new double[] { 52, 53, 55, 56.5, 59, 60, 61.5, 63.5,
					66, 64, 65, 67, 68.5, 71, 72, 73.5, 76, 77, 76, 77, 79, 80, 83,
					84, 85.5, 88, 89 };
		}
		else if(i==id.ebtedaei10 ) {
			kookMiditone= new double[] { 52, 53, 55, 56.5, 58, 60, 62, 63, 65, 64,
					65, 67, 69, 70, 72, 73.5, 75, 77, 76, 77, 79, 80, 82, 84, 86,
					87, 89 };
		}
		
		
		Button t = (Button) findViewById(i) ;
		String title = (String) t.getText() ;
			
		data.putString("title",title) ;
		data.putDoubleArray("custom", kookMiditone);


		Intent activity;
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		boolean tunerType  = prefs.getBoolean("tunerChange",
				false);
		if(!tunerType)activity = new Intent(DoreyeEbtedaE.this, SanturTuner.class);
		else activity = new Intent(DoreyeEbtedaE.this, SanturTuner2.class);
		activity.putExtras(data);
		startActivity(activity);
		

	}
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish() ;
	}
	

}
