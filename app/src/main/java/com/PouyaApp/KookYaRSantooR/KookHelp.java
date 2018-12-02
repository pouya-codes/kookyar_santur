package com.PouyaApp.KookYaRSantooR;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.PouyaApp.KookYaRSantooR.R;
import com.gc.materialdesign.views.Button;

public class KookHelp extends Activity {
	public String fonts = "BNazanin.ttf";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_kook);
		Typeface face = Typeface.createFromAsset(getAssets(), "font/" + fonts
				+ "");
		
		TextView[] textViews = {(TextView) findViewById(R.id.kookHelp1) ,(TextView) findViewById(R.id.kookHelp2) ,(TextView) findViewById(R.id.kookHelp3) ,(TextView) findViewById(R.id.kookHelp4) ,(TextView) findViewById(R.id.kookHelp5) ,(TextView) findViewById(R.id.kookHelp6) ,(TextView) findViewById(R.id.kookHelp7) ,(TextView) findViewById(R.id.kookHelp8) ,(TextView) findViewById(R.id.kookHelp9) ,(TextView) findViewById(R.id.kookHelp10) ,(TextView) findViewById(R.id.kookHelp11) ,(TextView) findViewById(R.id.kookHelp12) ,(TextView) findViewById(R.id.kookHelp13) } ;

		for (int i = 0; i < textViews.length; i++) {
			textViews[i].setTypeface(face);
			String str_tuner = (String) textViews[i].getText().toString();
			textViews[i].setText(PersianReshape.reshape(str_tuner));
		}
        Button button = (Button) findViewById(R.id.button_return) ;

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
 		//
	}


}
