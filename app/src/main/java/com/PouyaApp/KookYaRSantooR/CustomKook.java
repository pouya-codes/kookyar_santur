package com.PouyaApp.KookYaRSantooR;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
//import androidx.appcompat.app.ActionBarActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.PouyaApp.KookYaRSantooR.R.drawable;
import com.PouyaApp.KookYaRSantooR.R.id;
import com.google.android.material.button.MaterialButton;

public class CustomKook extends AppCompatActivity implements View.OnClickListener,
        OnItemSelectedListener {
    private MaterialButton[] buttons = new MaterialButton[9];
    private int[] buttonsId = {id.button_1, id.button_2, id.button_3,
            id.button_4, id.button_5, id.button_6, id.button_7, id.button_8,
            id.button_9};
    private MaterialButton ok, reset;
    private RadioButton rd1, rd2, rd3;
    public String fonts = "BZar.ttf";
    private double note = 0;
    private LinearLayout wire;
    private Spinner noteSpineer, posisionSpinner;
    private int position, wireSelected, positiontone, notetone;
    private RadioButton bemol, koron, sori, diyez, bekar;
    private int[] wireId = {drawable.santur_1, drawable.santur_2,
            drawable.santur_3, drawable.santur_4, drawable.santur_5,
            drawable.santur_6, drawable.santur_7, drawable.santur_8,
            drawable.santur_9, drawable.santur_10, drawable.santur_11,
            drawable.santur_12, drawable.santur_13, drawable.santur_14,
            drawable.santur_15, drawable.santur_16, drawable.santur_17,
            drawable.santur_18, drawable.santur_19, drawable.santur_20,
            drawable.santur_21, drawable.santur_22, drawable.santur_23,
            drawable.santur_24, drawable.santur_25, drawable.santur_26,
            drawable.santur_27};
    double[] kookMiditone = {48, 53, 55, 56.5, 58, 60, 61.5, 63, 65, 63.5, 65,
            67, 68.5, 70, 72, 74, 75, 77, 75.5, 77, 79, 80, 82, 84, 86, 87, 89};
    boolean enabled = false;
    double[] kookMiditoneRef = {48, 53, 55, 56.5, 58, 60, 61.5, 63, 65, 63.5, 65,
            67, 68.5, 70, 72, 74, 75, 77, 75.5, 77, 79, 80, 82, 84, 86, 87, 89};
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_kook);
        initGui();

    }

    private void initGui() {
        // TODO Auto-generated method stub
//		Typeface face = Typeface.createFromAsset(getAssets(), "font/" + fonts
//				+ "");
        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = (MaterialButton) findViewById(buttonsId[i]);
            buttons[i].setOnClickListener(this);
            //		buttons[i].getTextView().setTypeface(face);
            buttons[i].setBackgroundColor(getResources().getColor(R.color.white));
        }
        ok = (MaterialButton) findViewById(id.button_ok);
        ok.setOnClickListener(this);
        //	ok.getTextView().setTypeface(face);

        reset = (MaterialButton) findViewById(id.button_reset);
        reset.setOnClickListener(this);
        //	reset.getTextView().setTypeface(face);

        rd1 = (RadioButton) findViewById(id.zardRB);
        //	rd1.setTypeface(face);
        rd1.setOnClickListener(this);

        rd2 = (RadioButton) findViewById(id.sefidRB);
        //	rd2.setTypeface(face);
        rd2.setOnClickListener(this);

        rd3 = (RadioButton) findViewById(id.poshtRB);
//		rd3.setTypeface(face);
        rd3.setOnClickListener(this);

        wire = (LinearLayout) findViewById(id.layout_sim);

        posisionSpinner = (Spinner) findViewById(R.id.posison_spinner);

        ArrayAdapter<CharSequence> positionAdapter = ArrayAdapter
                .createFromResource(this, R.array.posision_array,
                        android.R.layout.simple_spinner_item);

        positionAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        posisionSpinner.setAdapter(positionAdapter);
        posisionSpinner.setOnItemSelectedListener(this);

        noteSpineer = (Spinner) findViewById(R.id.note_spinner);
        ArrayAdapter<CharSequence> noteAdapter = ArrayAdapter
                .createFromResource(this, R.array.note_array,
                        android.R.layout.simple_spinner_item);

        noteAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        noteSpineer.setAdapter(noteAdapter);
        noteSpineer.setOnItemSelectedListener(this);

        bemol = (RadioButton) findViewById(R.id.bemol_radioButton);
        bemol.setOnClickListener(this);
//		bemol.setTypeface(face);
        // ------------------------------------------------------------------
        koron = (RadioButton) findViewById(R.id.koron_radioButton);
        koron.setOnClickListener(this);
        //	koron.setTypeface(face);
        // ------------------------------------------------------------------
        bekar = (RadioButton) findViewById(R.id.bekar_radioButton);
        bekar.setOnClickListener(this);
        //	bekar.setTypeface(face);
        // ------------------------------------------------------------------
        sori = (RadioButton) findViewById(R.id.sori_radioButton);
        sori.setOnClickListener(this);
        //	sori.setTypeface(face);
        // ------------------------------------------------------------------
        diyez = (RadioButton) findViewById(R.id.diyez_radioButton);
        diyez.setOnClickListener(this);
        //	diyez.setTypeface(face);

        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

    }

    private void setSpinner(int i) {
        // TODO Auto-generated method stub
        int position = (int) ((kookMiditone[i] / 12) - 4);
        posisionSpinner.setSelection(position);

        double noteName = kookMiditone[i] % 12;

        if (noteName == 0) {
            noteSpineer.setSelection(0);
            setRadio(0);
        } else if (noteName == 0.50) {
            noteSpineer.setSelection(0);
            setRadio(1);
        } else if (noteName == 1) {
            noteSpineer.setSelection(1);
            setRadio(-2);
        } else if (noteName == 1.5) {
            noteSpineer.setSelection(1);
            setRadio(-1);
        } else if (noteName == 2) {
            noteSpineer.setSelection(1);
            setRadio(0);
        } else if (noteName == 2.5) {
            noteSpineer.setSelection(1);
            setRadio(1);
        } else if (noteName == 3) {
            noteSpineer.setSelection(2);
            setRadio(-2);
        } else if (noteName == 3.5) {
            noteSpineer.setSelection(2);
            setRadio(-1);
        } else if (noteName == 4) {
            noteSpineer.setSelection(2);
            setRadio(0);
        } else if (noteName == 4.5) {
            noteSpineer.setSelection(2);
            setRadio(1);
        } else if (noteName == 5) {
            noteSpineer.setSelection(3);
            setRadio(0);
        } else if (noteName == 5.5) {
            noteSpineer.setSelection(3);
            setRadio(1);
        } else if (noteName == 6) {
            noteSpineer.setSelection(4);
            setRadio(-2);
        } else if (noteName == 6.5) {
            noteSpineer.setSelection(4);
            setRadio(-1);
        } else if (noteName == 7) {
            noteSpineer.setSelection(4);
            setRadio(0);
        } else if (noteName == 7.5) {
            noteSpineer.setSelection(4);
            setRadio(1);
        } else if (noteName == 8) {
            noteSpineer.setSelection(5);
            setRadio(-2);
        } else if (noteName == 8.5) {
            noteSpineer.setSelection(5);
            setRadio(-1);
        } else if (noteName == 9) {
            noteSpineer.setSelection(5);
            setRadio(0);
        } else if (noteName == 9.5) {
            noteSpineer.setSelection(5);
            setRadio(1);
        } else if (noteName == 10) {
            noteSpineer.setSelection(6);
            setRadio(-2);
        } else if (noteName == 10.5) {
            noteSpineer.setSelection(6);
            setRadio(-1);
        } else if (noteName == 11) {
            noteSpineer.setSelection(6);
            setRadio(0);
        } else if (noteName == 11.5) {
            noteSpineer.setSelection(6);
            setRadio(1);
        }

    }

    private void setRadio(int i) {
        // TODO Auto-generated method stub
        if (i == -2) {

            note = -1;
            sendnote();
            bemol.setChecked(true);
            koron.setChecked(false);
            bekar.setChecked(false);
            sori.setChecked(false);
            diyez.setChecked(false);
        }
        if (i == -1) {

            note = -0.5;
            sendnote();
            bemol.setChecked(false);
            koron.setChecked(true);
            bekar.setChecked(false);
            sori.setChecked(false);
            diyez.setChecked(false);
        }
        if (i == 0) {

            note = 0;
            sendnote();
            bemol.setChecked(false);
            koron.setChecked(false);
            bekar.setChecked(true);
            sori.setChecked(false);
            diyez.setChecked(false);
        }
        if (i == 1) {

            note = 0.5;
            sendnote();
            bemol.setChecked(false);
            koron.setChecked(false);
            bekar.setChecked(false);
            sori.setChecked(true);
            diyez.setChecked(false);
        }

        if (i == 2) {

            note = 1;
            sendnote();
            bemol.setChecked(false);
            koron.setChecked(false);
            bekar.setChecked(false);
            sori.setChecked(false);
            diyez.setChecked(true);
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        if (v.getId() == id.zardRB || v.getId() == id.sefidRB
                || v.getId() == id.poshtRB) {
            wire.setBackgroundResource(R.drawable.santur2);
            for (int i = 0; i < buttons.length; i++) {
                buttons[i].setEnabled(true);
                buttons[i].setBackgroundColor(getResources().getColor(R.color.white));
            }
            int viewId = v.getId();
            if (viewId == R.id.zardRB) {
                position = 0;
                rd2.setChecked(false);
                rd3.setChecked(false);
            } else if (viewId == R.id.sefidRB) {
                position = 1;
                rd1.setChecked(false);
                rd3.setChecked(false);
            } else if (viewId == R.id.poshtRB) {
                position = 2;
                rd1.setChecked(false);
                rd2.setChecked(false);
            }

        }
        if (v.getId() == id.button_1 || v.getId() == id.button_2
                || v.getId() == id.button_3 || v.getId() == id.button_4
                || v.getId() == id.button_5 || v.getId() == id.button_6
                || v.getId() == id.button_7 || v.getId() == id.button_8
                || v.getId() == id.button_9) {
            enabled = true;
            for (int i = 0; i < buttonsId.length; i++) {
                if (v.getId() == buttonsId[i]) {
                    wireSelected = (position * 9) + i;
                    wire.setBackgroundResource(wireId[wireSelected]);
                    buttons[i].setBackgroundColor(getResources().getColor(R.color.blue));
                    setSpinner(wireSelected);
                } else {
                    buttons[i].setBackgroundColor(getResources().getColor(R.color.white));
                }
            }
        }

        int radioId = v.getId();
        if (radioId == R.id.bemol_radioButton) {
            note = -1;
            sendnote();
            // bemol.setChecked(false);
            koron.setChecked(false);
            bekar.setChecked(false);
            sori.setChecked(false);
            diyez.setChecked(false);
        } else if (radioId == R.id.koron_radioButton) {
            note = -0.5;
            sendnote();
            bemol.setChecked(false);
            // koron.setChecked(false);
            bekar.setChecked(false);
            sori.setChecked(false);
            diyez.setChecked(false);
        } else if (radioId == R.id.bekar_radioButton) {
            note = 0;
            sendnote();
            bemol.setChecked(false);
            koron.setChecked(false);
            // bekar.setChecked(false);
            sori.setChecked(false);
            diyez.setChecked(false);
        } else if (radioId == R.id.sori_radioButton) {
            note = 0.5;
            sendnote();
            bemol.setChecked(false);
            koron.setChecked(false);
            bekar.setChecked(false);
            // sori.setChecked(false);
            diyez.setChecked(false);
        } else if (radioId == R.id.diyez_radioButton) {
            note = 1;
            sendnote();
            bemol.setChecked(false);
            koron.setChecked(false);
            bekar.setChecked(false);
            sori.setChecked(false);
            // diyez.setChecked(false);
        }
        if (v.getId() == id.button_ok) {
            SharedPreferences prefs;
            prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            boolean tunerType = prefs.getBoolean("tunerChange",
                    false);
            Bundle data = new Bundle();
            data.putDoubleArray("custom", kookMiditone);
            Intent activity;
            if (!tunerType) activity = new Intent(CustomKook.this, SanturTuner.class);
            else {
                activity = new Intent(CustomKook.this, SanturTuner2.class);
            }
            activity.putExtras(data);
            startActivity(activity);

        }
        if (v.getId() == id.button_reset) {

            kookMiditone[wireSelected] = kookMiditoneRef[wireSelected];
            setSpinner(wireSelected);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        for (int j = 0; j < kookMiditone.length; j++) {
            kookMiditone[j] = (double) prefs.getFloat(j + "", (float) kookMiditone[j]);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = prefs.edit();
        for (int j = 0; j < kookMiditone.length; j++) {
            editor.putFloat(j + "", (float) kookMiditone[j]);
        }

        editor.commit();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View arg1, int position,
                               long arg3) {
        // TODO Auto-generated method stub
        Spinner spinner = (Spinner) parent;
        // TODO Auto-generated method stub
        if (spinner.getId() == R.id.posison_spinner) {
            switch (position) {

                case 0:
                    positiontone = 4;
                    sendnote();
                    break;

                case 1:
                    positiontone = 5;
                    sendnote();
                    break;
                case 2:
                    positiontone = 6;
                    sendnote();
                    break;
                case 3:
                    positiontone = 7;
                    sendnote();
                    break;
            }
        } else if (spinner.getId() == R.id.note_spinner) {

            switch (position) {

                case 0:
                    diyez.setEnabled(true);
                    bemol.setEnabled(false);
                    notetone = 0;
                    sendnote();
                    break;

                case 1:
                    bemol.setEnabled(true);
                    diyez.setEnabled(true);
                    notetone = 2;
                    sendnote();
                    break;
                case 2:
                    diyez.setEnabled(true);
                    notetone = 4;
                    diyez.setEnabled(false);
                    sendnote();
                    break;
                case 3:
                    diyez.setEnabled(true);
                    notetone = 5;
                    bemol.setEnabled(false);
                    sendnote();
                    break;
                case 4:
                    notetone = 7;
                    bemol.setEnabled(true);
                    diyez.setEnabled(true);
                    sendnote();
                    break;
                case 5:
                    bemol.setEnabled(true);
                    diyez.setEnabled(true);
                    notetone = 9;
                    sendnote();
                    break;
                case 6:
                    bemol.setEnabled(true);
                    diyez.setEnabled(false);
                    notetone = 11;
                    sendnote();
                    break;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

    private void sendnote() {
        // TODO Auto-generated method stub
        double miditone = (positiontone * 12) + (notetone + note);
        if (enabled)
            kookMiditone[wireSelected] = miditone;

    }

}