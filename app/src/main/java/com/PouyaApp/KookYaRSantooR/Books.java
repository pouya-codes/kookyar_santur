package com.PouyaApp.KookYaRSantooR;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

import com.PouyaApp.KookYaRSantooR.R.id;
import com.gc.materialdesign.views.LayoutRipple;

public class Books extends Activity implements View.OnClickListener {

    public String fonts = "BNazanin.ttf";
    private TextView tvEbtedaei, tvMeshkatian, tvDastur, tvGolnar, tvShive, tvYadegaran_hom, tvYadegaran_esf, tvHazarDastan_3gah, tvHazarDastan_4gah, tvSepideh;
    private LayoutRipple imEbtedaei, imMeshkatian, imDastur, imShive, imGolnar, imYadegaran_hom, imYadegeran_esf, imHazardastan_3gah, imHazarDastan_4gah, im_sepideh;
    private Bundle data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);

        Typeface face = Typeface.createFromAsset(getAssets(), "font/" + fonts
                + "");

        tvDastur = (TextView) findViewById(id.TextView_dastor);
        tvDastur.setTypeface(face);
        tvDastur.setOnClickListener(this);


        tvShive = (TextView) findViewById(id.TextView_shive);
        tvShive.setTypeface(face);
        tvShive.setOnClickListener(this);


        tvEbtedaei = (TextView) findViewById(id.TextView_ebtedaei);
        tvEbtedaei.setTypeface(face);
        tvEbtedaei.setOnClickListener(this);

        tvMeshkatian = (TextView) findViewById(id.tvMeshkatian);
        tvMeshkatian.setTypeface(face);
        tvMeshkatian.setOnClickListener(this);

        tvGolnar = (TextView) findViewById(id.TextView_golnar);
        tvGolnar.setTypeface(face);
        tvGolnar.setOnClickListener(this);

        tvYadegaran_esf = (TextView) findViewById(id.TextView_yadegaran_esf);
        tvYadegaran_esf.setTypeface(face);
        tvYadegaran_esf.setOnClickListener(this);


        tvYadegaran_hom = (TextView) findViewById(id.TextView_yadegaran_hom);
        tvYadegaran_hom.setTypeface(face);
        tvYadegaran_hom.setOnClickListener(this);

        tvHazarDastan_3gah = (TextView) findViewById(id.TextView_hazarDastan_3gah);
        tvHazarDastan_3gah.setTypeface(face);
        tvHazarDastan_3gah.setOnClickListener(this);

        tvHazarDastan_4gah = (TextView) findViewById(id.TextView_hazardastan_4gah);
        tvHazarDastan_4gah.setTypeface(face);
        tvHazarDastan_4gah.setOnClickListener(this);

        tvSepideh = (TextView) findViewById(id.TextView_sepideh);
        tvSepideh.setTypeface(face);
        tvSepideh.setOnClickListener(this);

        imDastur = (LayoutRipple) findViewById(id.book_dastur);
        imDastur.setOnClickListener(this);
        imShive = (LayoutRipple) findViewById(id.books_shive);
        imShive.setOnClickListener(this);
        imEbtedaei = (LayoutRipple) findViewById(id.book_dore);
        imEbtedaei.setOnClickListener(this);
        imMeshkatian = (LayoutRipple) findViewById(id.book_meshkatian);
        imMeshkatian.setOnClickListener(this);
        imGolnar = (LayoutRipple) findViewById(id.book_golnar);
        imGolnar.setOnClickListener(this);
        imYadegaran_hom = (LayoutRipple) findViewById(id.book_yadegaran_homayun);
        imYadegaran_hom.setOnClickListener(this);
        imYadegeran_esf = (LayoutRipple) findViewById(id.book_yadegaran);
        imYadegeran_esf.setOnClickListener(this);
        imHazardastan_3gah = (LayoutRipple) findViewById(id.book_dastan);
        imHazardastan_3gah.setOnClickListener(this);
        imHazarDastan_4gah = (LayoutRipple) findViewById(id.book_dastan_4gah);
        imHazarDastan_4gah.setOnClickListener(this);
        im_sepideh = (LayoutRipple) findViewById(id.book_sepideh);
        im_sepideh.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        Intent activity;
        switch (v.getId()) {

            case id.book_dastur:
                data = new Bundle();
                data.putInt("book", 1);
                luanchActivity();

                break;

            case id.books_shive:
                data = new Bundle();
                data.putInt("book", 2);
                luanchActivity();
                break;

            case id.book_golnar:
                data = new Bundle();
                data.putInt("book", 3);
                luanchActivity();
                break;

            case id.book_yadegaran:
                data = new Bundle();
                data.putInt("book", 4);
                luanchActivity();
                break;

            case id.book_yadegaran_homayun:
                data = new Bundle();
                data.putInt("book", 5);
                luanchActivity();
                break;

            case id.book_dastan:
                data = new Bundle();
                data.putInt("book", 6);
                luanchActivity();
                break;

            case id.book_dastan_4gah:
                data = new Bundle();
                data.putInt("book", 7);
                luanchActivity();
                break;

            case id.book_meshkatian:

                activity = new Intent(Books.this, Meshkatian.class);
                startActivity(activity);
                break;

            case id.book_dore:

                activity = new Intent(Books.this, DoreyeEbtedaE.class);
                startActivity(activity);
                break;
            case id.book_sepideh:
                data = new Bundle();
                data.putInt("book", 8);
                luanchActivity() ;
                break;

        }

    }

    private void luanchActivity() {
        Intent activity;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        boolean tunerType  = prefs.getBoolean("tunerChange",
                false);
        if(!tunerType)activity = new Intent(Books.this, SanturTuner.class);
        else activity = new Intent(Books.this, SanturTuner2.class);
        activity.putExtras(data);
        startActivity(activity);
    }

    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }

}
