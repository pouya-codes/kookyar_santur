package com.PouyaApp.KookYaRSantooR;
import com.PouyaApp.KookYaRSantooR.R;


import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class Prefs extends PreferenceActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.perfs);

//        getListView().setBackgroundColor(Color.GRAY);
//
//        getListView().setCacheColorHint(Color.YELLOW);

    }

	
	

}
