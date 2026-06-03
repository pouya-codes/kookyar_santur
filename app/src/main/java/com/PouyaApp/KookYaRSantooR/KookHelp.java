package com.PouyaApp.KookYaRSantooR;

import androidx.appcompat.app.AppCompatActivity;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import com.google.android.material.button.MaterialButton;
import com.PouyaApp.KookYaRSantooR.R;

public class KookHelp extends AppCompatActivity {
	public String fonts = "BNazanin.ttf";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_kook);
		Typeface face = Typeface.createFromAsset(getAssets(), "font/" + fonts
				+ "");
		
		TextView[] textViews = {(TextView) findViewById(R.id.kookHelp1) ,(TextView) findViewById(R.id.kookHelp2) ,(TextView) findViewById(R.id.kookHelp3) ,(TextView) findViewById(R.id.kookHelp4) ,(TextView) findViewById(R.id.kookHelp5) ,(TextView) findViewById(R.id.kookHelp6) ,(TextView) findViewById(R.id.kookHelp7) ,(TextView) findViewById(R.id.kookHelp8) ,(TextView) findViewById(R.id.kookHelp9) ,(TextView) findViewById(R.id.kookHelp10) ,(TextView) findViewById(R.id.kookHelp11) ,(TextView) findViewById(R.id.kookHelp12) ,(TextView) findViewById(R.id.kookHelp13) } ;

		// Determine the appropriate text color based on current theme
		int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
		int textColor;
		if (nightModeFlags == Configuration.UI_MODE_NIGHT_NO) {
			// Light mode - use dark text
			textColor = Color.parseColor("#212121");
		} else {
			// Dark mode - use light text
			textColor = Color.parseColor("#E8E8E8");
		}

		for (int i = 0; i < textViews.length; i++) {
			textViews[i].setTypeface(face);
			textViews[i].setTextColor(textColor);
			String str_tuner = (String) textViews[i].getText().toString();
			textViews[i].setText(PersianReshape.reshape(str_tuner));
		}
        MaterialButton button = (MaterialButton) findViewById(R.id.button_return);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
 		//
	}


}
