package com.PouyaApp.KookYaRSantooR;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowCompat;
import androidx.preference.PreferenceManager;

import com.PouyaApp.KookYaRSantooR.R.drawable;
import com.PouyaApp.KookYaRSantooR.R.id;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class CustomKook extends AppCompatActivity implements View.OnClickListener,
        OnItemSelectedListener {

    private final MaterialButton[] buttons = new MaterialButton[9];
    private final int[] buttonsId = {id.button_1, id.button_2, id.button_3,
            id.button_4, id.button_5, id.button_6, id.button_7, id.button_8,
            id.button_9};
    private MaterialButton ok;
    private MaterialButton reset;
    private MaterialButton saveKook;
    private MaterialButton loadKook;
    private MaterialButton deleteKook;
    private RadioButton rd1;
    private RadioButton rd2;
    private RadioButton rd3;
    private double note = 0;
    private LinearLayout wire;
    private Spinner noteSpineer;
    private Spinner posisionSpinner;
    private Spinner savedKookSpinner;
    private TextInputEditText kookNameInput;
    private int position;
    private int wireSelected;
    private int positiontone;
    private int notetone;
    private RadioButton bemol;
    private RadioButton koron;
    private RadioButton sori;
    private RadioButton diyez;
    private RadioButton bekar;
    private final int[] wireId = {drawable.santur_1, drawable.santur_2,
            drawable.santur_3, drawable.santur_4, drawable.santur_5,
            drawable.santur_6, drawable.santur_7, drawable.santur_8,
            drawable.santur_9, drawable.santur_10, drawable.santur_11,
            drawable.santur_12, drawable.santur_13, drawable.santur_14,
            drawable.santur_15, drawable.santur_16, drawable.santur_17,
            drawable.santur_18, drawable.santur_19, drawable.santur_20,
            drawable.santur_21, drawable.santur_22, drawable.santur_23,
            drawable.santur_24, drawable.santur_25, drawable.santur_26,
            drawable.santur_27};
    private final double[] kookMiditone = {48, 53, 55, 56.5, 58, 60, 61.5, 63, 65, 63.5, 65,
            67, 68.5, 70, 72, 74, 75, 77, 75.5, 77, 79, 80, 82, 84, 86, 87, 89};
    private final double[] kookMiditoneRef = {48, 53, 55, 56.5, 58, 60, 61.5, 63, 65, 63.5, 65,
            67, 68.5, 70, 72, 74, 75, 77, 75.5, 77, 79, 80, 82, 84, 86, 87, 89};
    private boolean enabled = false;
    private SharedPreferences prefs;
    private List<SavedCustomKook.Entry> savedEntries = new ArrayList<>();
    private ArrayAdapter<String> savedKookAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), true);
        setContentView(R.layout.activity_custom_kook);
        setupToolbar();
        initGui();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.custom);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void initGui() {
        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = findViewById(buttonsId[i]);
            buttons[i].setOnClickListener(this);
            styleStringButtonDisabled(buttons[i]);
        }

        ok = findViewById(id.button_ok);
        ok.setOnClickListener(this);

        reset = findViewById(id.button_reset);
        reset.setOnClickListener(this);

        saveKook = findViewById(R.id.button_save_kook);
        saveKook.setOnClickListener(this);

        loadKook = findViewById(R.id.button_load_kook);
        loadKook.setOnClickListener(this);

        deleteKook = findViewById(R.id.button_delete_kook);
        deleteKook.setOnClickListener(this);

        kookNameInput = findViewById(R.id.edit_kook_name);

        rd1 = findViewById(id.zardRB);
        rd1.setOnClickListener(this);

        rd2 = findViewById(id.sefidRB);
        rd2.setOnClickListener(this);

        rd3 = findViewById(id.poshtRB);
        rd3.setOnClickListener(this);

        wire = findViewById(id.layout_sim);

        posisionSpinner = findViewById(R.id.posison_spinner);
        ArrayAdapter<CharSequence> positionAdapter = ArrayAdapter.createFromResource(
                this, R.array.posision_array, R.layout.spinner_item_dark);
        positionAdapter.setDropDownViewResource(R.layout.spinner_item_dark);
        posisionSpinner.setAdapter(positionAdapter);
        posisionSpinner.setOnItemSelectedListener(this);

        noteSpineer = findViewById(R.id.note_spinner);
        ArrayAdapter<CharSequence> noteAdapter = ArrayAdapter.createFromResource(
                this, R.array.note_array, R.layout.spinner_item_dark);
        noteAdapter.setDropDownViewResource(R.layout.spinner_item_dark);
        noteSpineer.setAdapter(noteAdapter);
        noteSpineer.setOnItemSelectedListener(this);

        bemol = findViewById(R.id.bemol_radioButton);
        bemol.setOnClickListener(this);

        koron = findViewById(R.id.koron_radioButton);
        koron.setOnClickListener(this);

        bekar = findViewById(R.id.bekar_radioButton);
        bekar.setOnClickListener(this);

        sori = findViewById(R.id.sori_radioButton);
        sori.setOnClickListener(this);

        diyez = findViewById(R.id.diyez_radioButton);
        diyez.setOnClickListener(this);

        savedKookSpinner = findViewById(R.id.saved_kook_spinner);
        savedKookAdapter = new ArrayAdapter<>(this, R.layout.spinner_item_dark, new ArrayList<>());
        savedKookAdapter.setDropDownViewResource(R.layout.spinner_item_dark);
        savedKookSpinner.setAdapter(savedKookAdapter);

        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        refreshSavedKookSpinner();
    }

    private void refreshSavedKookSpinner() {
        savedEntries = SavedCustomKook.loadAll(this);
        List<String> names = new ArrayList<>();
        if (savedEntries.isEmpty()) {
            names.add(getString(R.string.no_saved_kooks));
            loadKook.setEnabled(false);
            deleteKook.setEnabled(false);
        } else {
            for (SavedCustomKook.Entry entry : savedEntries) {
                names.add(entry.name);
            }
            loadKook.setEnabled(true);
            deleteKook.setEnabled(true);
        }
        savedKookAdapter.clear();
        savedKookAdapter.addAll(names);
        savedKookAdapter.notifyDataSetChanged();
        savedKookSpinner.setSelection(0);
    }

    private void applyMiditones(double[] miditones) {
        System.arraycopy(miditones, 0, kookMiditone, 0, kookMiditone.length);
        enabled = false;
        wire.setBackgroundResource(R.drawable.santur2);
        for (MaterialButton button : buttons) {
            button.setEnabled(false);
            styleStringButtonDisabled(button);
        }
        rd1.setChecked(false);
        rd2.setChecked(false);
        rd3.setChecked(false);
    }

    private void saveCurrentKook() {
        CharSequence name = kookNameInput.getText();
        if (name == null || name.toString().trim().isEmpty()) {
            Toast.makeText(this, R.string.kook_save_failed, Toast.LENGTH_SHORT).show();
            return;
        }
        if (SavedCustomKook.save(this, name.toString().trim(), kookMiditone)) {
            Toast.makeText(this, R.string.kook_saved, Toast.LENGTH_SHORT).show();
            refreshSavedKookSpinner();
        } else {
            Toast.makeText(this, R.string.kook_save_failed, Toast.LENGTH_SHORT).show();
        }
    }

    private void loadSelectedKook() {
        if (savedEntries.isEmpty()) {
            Toast.makeText(this, R.string.select_saved_kook, Toast.LENGTH_SHORT).show();
            return;
        }
        int index = savedKookSpinner.getSelectedItemPosition();
        if (index < 0 || index >= savedEntries.size()) {
            Toast.makeText(this, R.string.select_saved_kook, Toast.LENGTH_SHORT).show();
            return;
        }
        SavedCustomKook.Entry entry = savedEntries.get(index);
        applyMiditones(entry.miditones);
        kookNameInput.setText(entry.name);
        Toast.makeText(this, entry.name, Toast.LENGTH_SHORT).show();
    }

    private void deleteSelectedKook() {
        if (savedEntries.isEmpty()) {
            Toast.makeText(this, R.string.select_saved_kook, Toast.LENGTH_SHORT).show();
            return;
        }
        int index = savedKookSpinner.getSelectedItemPosition();
        if (index < 0 || index >= savedEntries.size()) {
            Toast.makeText(this, R.string.select_saved_kook, Toast.LENGTH_SHORT).show();
            return;
        }
        SavedCustomKook.delete(this, index);
        refreshSavedKookSpinner();
        Toast.makeText(this, R.string.kook_deleted, Toast.LENGTH_SHORT).show();
    }

    private void setSpinner(int i) {
        int spinnerPosition = (int) ((kookMiditone[i] / 12) - 4);
        posisionSpinner.setSelection(spinnerPosition);

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
        if (v.getId() == id.zardRB || v.getId() == id.sefidRB
                || v.getId() == id.poshtRB) {
            wire.setBackgroundResource(R.drawable.santur2);
            for (int i = 0; i < buttons.length; i++) {
                buttons[i].setEnabled(true);
                styleStringButtonDefault(buttons[i]);
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
                    styleStringButtonActive(buttons[i]);
                    setSpinner(wireSelected);
                } else {
                    styleStringButtonDefault(buttons[i]);
                }
            }
        }

        int radioId = v.getId();
        if (radioId == R.id.bemol_radioButton) {
            note = -1;
            sendnote();
            koron.setChecked(false);
            bekar.setChecked(false);
            sori.setChecked(false);
            diyez.setChecked(false);
        } else if (radioId == R.id.koron_radioButton) {
            note = -0.5;
            sendnote();
            bemol.setChecked(false);
            bekar.setChecked(false);
            sori.setChecked(false);
            diyez.setChecked(false);
        } else if (radioId == R.id.bekar_radioButton) {
            note = 0;
            sendnote();
            bemol.setChecked(false);
            koron.setChecked(false);
            sori.setChecked(false);
            diyez.setChecked(false);
        } else if (radioId == R.id.sori_radioButton) {
            note = 0.5;
            sendnote();
            bemol.setChecked(false);
            koron.setChecked(false);
            bekar.setChecked(false);
            diyez.setChecked(false);
        } else if (radioId == R.id.diyez_radioButton) {
            note = 1;
            sendnote();
            bemol.setChecked(false);
            koron.setChecked(false);
            bekar.setChecked(false);
            sori.setChecked(false);
        }

        if (v.getId() == id.button_ok) {
            Bundle data = new Bundle();
            data.putDoubleArray("custom", kookMiditone);
            data.putString("noteName", getString(R.string.custom));
            SanturTuner.openWith(this, data);
        }

        if (v.getId() == id.button_reset) {
            kookMiditone[wireSelected] = kookMiditoneRef[wireSelected];
            setSpinner(wireSelected);
        }

        if (v.getId() == R.id.button_save_kook) {
            saveCurrentKook();
        } else if (v.getId() == R.id.button_load_kook) {
            loadSelectedKook();
        } else if (v.getId() == R.id.button_delete_kook) {
            deleteSelectedKook();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        for (int j = 0; j < kookMiditone.length; j++) {
            kookMiditone[j] = prefs.getFloat(j + "", (float) kookMiditone[j]);
        }
        refreshSavedKookSpinner();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = prefs.edit();
        for (int j = 0; j < kookMiditone.length; j++) {
            editor.putFloat(j + "", (float) kookMiditone[j]);
        }
        editor.apply();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View arg1, int position,
                               long arg3) {
        Spinner spinner = (Spinner) parent;
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
                default:
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
                default:
                    break;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
    }

    private void sendnote() {
        double miditone = (positiontone * 12) + (notetone + note);
        if (enabled) {
            kookMiditone[wireSelected] = miditone;
        }
    }

    private int color(int resId) {
        return ContextCompat.getColor(this, resId);
    }

    private void styleStringButtonDefault(MaterialButton button) {
        button.setBackgroundTintList(ColorStateList.valueOf(color(R.color.stringDefault)));
        button.setTextColor(color(R.color.onSurface));
    }

    private void styleStringButtonDisabled(MaterialButton button) {
        button.setBackgroundTintList(ColorStateList.valueOf(color(R.color.stringDisabled)));
        button.setTextColor(color(R.color.onSurfaceVariant));
    }

    private void styleStringButtonActive(MaterialButton button) {
        button.setBackgroundTintList(ColorStateList.valueOf(color(R.color.stringActive)));
        button.setTextColor(color(R.color.white));
    }
}
