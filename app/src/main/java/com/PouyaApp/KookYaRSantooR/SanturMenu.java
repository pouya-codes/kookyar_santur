package com.PouyaApp.KookYaRSantooR;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.PouyaApp.KookYaRSantooR.R;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class SanturMenu extends Activity implements View.OnClickListener {
    private Button navaTuning, shurTuning, shurTuningPavar, esfehanTuning, mahurTuning, segahTuning, chahargahTuning, homayunTuning, shurReTuning, esfehanFaTuning,homayounChapTuning ,mahurChapTuning ,segahChapTuning,chahargahChapTuning, faMinorTuning, doMajorTuning, doMinorTuning, customTuning;
    private Bundle data;
    public String fonts = "BNazanin.ttf";
    private SharedPreferences prefs;
    private boolean tunerType ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        super.onCreate(savedInstanceState);
        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        setContentView(R.layout.activity_santur_menu);
        setFace();


    }

    private void setFace() {
        // TODO Auto-generated method stub
        Typeface face = Typeface.createFromAsset(getAssets(), "font/" + fonts
                + "");
        shurTuning = (Button) findViewById(R.id.shur_Tuning);
        shurTuning.setOnClickListener(this);
        shurTuning.setTypeface(face);

        shurTuningPavar = (Button) findViewById(R.id.shur_Tuning_payvar);
        shurTuningPavar.setOnClickListener(this);
        shurTuningPavar.setTypeface(face);

        esfehanTuning = (Button) findViewById(R.id.esfehan_Tuning);
        esfehanTuning.setOnClickListener(this);
        esfehanTuning.setTypeface(face);

        mahurTuning = (Button) findViewById(R.id.mahur_Tuning);
        mahurTuning.setOnClickListener(this);
        mahurTuning.setTypeface(face);


        segahTuning = (Button) findViewById(R.id.segah_Tuning);
        segahTuning.setOnClickListener(this);
        segahTuning.setTypeface(face);

        chahargahTuning = (Button) findViewById(R.id.chargah_Tuning);
        chahargahTuning.setOnClickListener(this);
        chahargahTuning.setTypeface(face);


        homayunTuning = (Button) findViewById(R.id.Homayun_Tuning);
        homayunTuning.setOnClickListener(this);
        homayunTuning.setTypeface(face);


        shurReTuning = (Button) findViewById(R.id.shorRe_Tuning);
        shurReTuning.setOnClickListener(this);
        shurReTuning.setTypeface(face);


        esfehanFaTuning = (Button) findViewById(R.id.esfehanFa_Tuning);
        esfehanFaTuning.setOnClickListener(this);
        esfehanFaTuning.setTypeface(face);


        faMinorTuning = (Button) findViewById(R.id.faMinor_Tuning);
        faMinorTuning.setOnClickListener(this);
        faMinorTuning.setTypeface(face);


        doMajorTuning = (Button) findViewById(R.id.doMajor_Tuning);
        doMajorTuning.setOnClickListener(this);
        doMajorTuning.setTypeface(face);


        doMinorTuning = (Button) findViewById(R.id.doMinor_Tuning);
        doMinorTuning.setOnClickListener(this);
        doMinorTuning.setTypeface(face);


        customTuning = (Button) findViewById(R.id.custom_Tuning);
        customTuning.setOnClickListener(this);
        customTuning.setTypeface(face);


        navaTuning = (Button) findViewById(R.id.nava_Tuning);
        navaTuning.setOnClickListener(this);
        navaTuning.setTypeface(face);

        chahargahChapTuning = (Button) findViewById(R.id.chahargahChap_Tuning);
        chahargahChapTuning.setOnClickListener(this);
        chahargahChapTuning.setTypeface(face);

        segahChapTuning = (Button) findViewById(R.id.segahChap_Tuning);
        segahChapTuning.setOnClickListener(this);
        segahChapTuning.setTypeface(face);

        homayounChapTuning = (Button) findViewById(R.id.homayounChap_Tuning);
        homayounChapTuning.setOnClickListener(this);
        homayounChapTuning.setTypeface(face);

        mahurChapTuning = (Button) findViewById(R.id.mahurChap_Tuning);
        mahurChapTuning.setOnClickListener(this);
        mahurChapTuning.setTypeface(face);

        TextView tv_rastkook = (TextView) findViewById(R.id.tv_rastKook) ;
        tv_rastkook.setTypeface(face);

        TextView tv_chap = (TextView) findViewById(R.id.tv_chapKook) ;
        tv_chap.setTypeface(face);

        TextView tv_talfiqi = (TextView) findViewById(R.id.tv_talifiqiKook) ;
        tv_talfiqi.setTypeface(face);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        int viewId = v.getId();
        if (viewId == R.id.shur_Tuning) {
            data = new Bundle();
            data.putInt("kuk", 1);
            launchActivity();
        } else if (viewId == R.id.shur_Tuning_payvar) {
            data = new Bundle();
            data.putInt("kuk", 12);
            launchActivity();
        } else if (viewId == R.id.Homayun_Tuning) {
            data = new Bundle();
            data.putInt("kuk", 6);
            launchActivity();
        } else if (viewId == R.id.esfehan_Tuning) {
            data = new Bundle();
            data.putInt("kuk", 2);
            launchActivity();
        } else if (viewId == R.id.mahur_Tuning) {
            data = new Bundle();
            data.putInt("kuk", 3);
            launchActivity();
        } else if (viewId == R.id.segah_Tuning) {
            data = new Bundle();
            data.putInt("kuk", 4);
            launchActivity();
        } else if (viewId == R.id.chargah_Tuning) {
            data = new Bundle();
            data.putInt("kuk", 5);
            launchActivity();
        } else if (viewId == R.id.shorRe_Tuning) {
            data = new Bundle();
            data.putInt("kuk", 7);
            launchActivity();
        } else if (viewId == R.id.esfehanFa_Tuning) {
            data = new Bundle();
            data.putInt("kuk", 8);
            launchActivity();
        } else if (viewId == R.id.faMinor_Tuning) {
            data = new Bundle();
            data.putInt("kuk", 9);
            launchActivity();
        } else if (viewId == R.id.doMajor_Tuning) {
            data = new Bundle();
            data.putInt("kuk", 10);
            launchActivity();
        } else if (viewId == R.id.doMinor_Tuning) {
            data = new Bundle();
            data.putInt("kuk", 11);
            launchActivity();
        } else if (viewId == R.id.custom_Tuning) {
            Intent activity;
            activity = new Intent(SanturMenu.this, CustomKook.class);
            startActivity(activity);
        } else if (viewId == R.id.nava_Tuning) {
            data = new Bundle();
            data.putInt("kuk", 13);
            launchActivity();
        } else if (viewId == R.id.homayounChap_Tuning) {
            data = new Bundle();
            data.putInt("kuk", 14);
            launchActivity();
        } else if (viewId == R.id.mahurChap_Tuning) {
            data = new Bundle();
            data.putInt("kuk", 15);
            launchActivity();
        } else if (viewId == R.id.segahChap_Tuning) {
            data = new Bundle();
            data.putInt("kuk", 16);
            launchActivity();
        } else if (viewId == R.id.chahargahChap_Tuning) {
            data = new Bundle();
            data.putInt("kuk", 17);
            launchActivity();
        }
    }

    private void launchActivity() {
        tunerType  = prefs.getBoolean("tunerChange",
                false);
        Intent activity;
        if(!tunerType)activity = new Intent(SanturMenu.this, SanturTuner.class);
        else {
            activity = new Intent(SanturMenu.this, SanturTuner.class);
        }
        activity.putExtras(data);
        startActivity(activity);
    }

    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }


}
