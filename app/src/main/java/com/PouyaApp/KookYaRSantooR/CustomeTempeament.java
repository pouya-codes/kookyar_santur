package com.PouyaApp.KookYaRSantooR;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.Preference;
import androidx.preference.PreferenceManager;
import androidx.core.app.NavUtils;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;



public class CustomeTempeament extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {
    private static final String TAG = "Kookyar";
    private int[] slidersid = {R.id.slider_pitch_1, R.id.slider_pitch_2, R.id.slider_pitch_3, R.id.slider_pitch_4, R.id.slider_pitch_5, R.id.slider_pitch_6, R.id.slider_pitch_7, R.id.slider_pitch_8, R.id.slider_pitch_9, R.id.slider_pitch_10, R.id.slider_pitch_11, R.id.slider_pitch_12, R.id.slider_pitch_13, R.id.slider_pitch_14, R.id.slider_pitch_15, R.id.slider_pitch_16, R.id.slider_pitch_17, R.id.slider_pitch_18, R.id.slider_pitch_19, R.id.slider_pitch_20, R.id.slider_pitch_21, R.id.slider_pitch_22, R.id.slider_pitch_23, R.id.slider_pitch_24};
    private SeekBar[] sliders = new SeekBar[24];
    private int[] textsid = {R.id.textview_pitch_1, R.id.textview_pitch_2, R.id.textview_pitch_3, R.id.textview_pitch_4, R.id.textview_pitch_5, R.id.textview_pitch_6, R.id.textview_pitch_7, R.id.textview_pitch_8, R.id.textview_pitch_9, R.id.textview_pitch_10, R.id.textview_pitch_11, R.id.textview_pitch_12, R.id.textview_pitch_13, R.id.textview_pitch_14, R.id.textview_pitch_15, R.id.textview_pitch_16, R.id.textview_pitch_17, R.id.textview_pitch_18, R.id.textview_pitch_19, R.id.textview_pitch_20, R.id.textview_pitch_21, R.id.textview_pitch_22, R.id.textview_pitch_23, R.id.textview_pitch_24};
    private TextView[] texts = new TextView[24];
    private int[] checkboxsid = {R.id.checkbox_pitch_1, R.id.checkbox_pitch_2, R.id.checkbox_pitch_3, R.id.checkbox_pitch_4, R.id.checkbox_pitch_5, R.id.checkbox_pitch_6, R.id.checkbox_pitch_7, R.id.checkbox_pitch_8, R.id.checkbox_pitch_9, R.id.checkbox_pitch_10, R.id.checkbox_pitch_11, R.id.checkbox_pitch_12, R.id.checkbox_pitch_13, R.id.checkbox_pitch_14, R.id.checkbox_pitch_15, R.id.checkbox_pitch_16, R.id.checkbox_pitch_17, R.id.checkbox_pitch_18, R.id.checkbox_pitch_19, R.id.checkbox_pitch_20, R.id.checkbox_pitch_21, R.id.checkbox_pitch_22, R.id.checkbox_pitch_23, R.id.checkbox_pitch_24};
    private CheckBox[] checkboxs = new CheckBox[24];
    private SharedPreferences sharedPreferences ;
    private int[] tempeament = new int[24] ;

    private int id = 0;
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu);
        MenuInflater blowUp = getMenuInflater();
        blowUp.inflate(R.menu.tempeament_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        // TODO Auto-generated method stub
        int itemId = item.getItemId();
        if (itemId == R.id.restore) {
            for(int i =0 ;i<sliders.length;i++) {
                sliders[i].setProgress(50);
            }
        } else if (itemId == R.id.bazgasht_temp) {
            onBackPressed();
        } else if (itemId == R.id.save) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                    CustomeTempeament.this);
            alertDialog.setTitle("اعمال تغییرات");
            alertDialog
                    .setMessage("آیا تغییرات داده شده ذخیره شود ؟");
            alertDialog.setIcon(android.R.drawable.ic_menu_save);
            alertDialog.setPositiveButton("بله",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            saveTempeament();

                        }
                    });
            alertDialog.setNegativeButton("خیر",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            alertDialog.show();
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custome_tempeament);

        initGUI();
        initSeekBars();



    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                CustomeTempeament.this);
        alertDialog.setTitle("اعمال تغییرات");
        alertDialog
                .setMessage("آیا تغییرات داده شده ذخیره شود ؟");
        alertDialog.setIcon(android.R.drawable.ic_menu_save);
        alertDialog.setPositiveButton("بله",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        saveTempeament();
                        CustomeTempeament.super.onBackPressed();
                    }
                });
        alertDialog.setNegativeButton("خیر",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        CustomeTempeament.super.onBackPressed();
                    }
                });
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                CustomeTempeament.super.onBackPressed();
            }
        });
        alertDialog.show();
    }

    private void saveTempeament() {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        for (int j = 0; j < sliders.length; j++) {
            if (checkboxs[j].isChecked()) editor.putInt(j + "Tempeament", tempeament[j]);
            else editor.putInt(j + "Tempeament", 0 );
        }

        editor.commit();

    }

    private void initSeekBars() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this) ;
        for (int j = 0; j < sliders.length; j++) {
            tempeament[j] = sharedPreferences.getInt(j+"Tempeament", 0) ;
            texts[j].setText(tempeament[j] + " سنت");
            sliders[j].setProgress(tempeament[j]+50);
            if( tempeament[j]!=0 ) checkboxs[j].setEnabled(true);
        }




    }

    private void initGUI() {

        for (int i = 0; i < 24; i++) {
            sliders[i] = (SeekBar) findViewById(slidersid[i]);
            sliders[i].setOnSeekBarChangeListener(this);
            texts[i] = (TextView) findViewById(textsid[i]);
            checkboxs[i] = (CheckBox) findViewById(checkboxsid[i]);
            sliders[i].setMax(99);

        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        for (int i = 0;i<slidersid.length;i++) {
            if(seekBar.getId() == slidersid[i]) {
                if(progress==50){
                    checkboxs[i].setChecked(false);
                }
                else checkboxs[i].setChecked(true);
                texts[i].setText(PersianReshape.reshape(progress-50+"") + " سنت");
                tempeament[i]=progress-50 ;

            }
        }

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
