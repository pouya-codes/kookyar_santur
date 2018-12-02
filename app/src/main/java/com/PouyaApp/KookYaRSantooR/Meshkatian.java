package com.PouyaApp.KookYaRSantooR;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

import com.PouyaApp.KookYaRSantooR.R.id;

public class Meshkatian extends Activity implements View.OnClickListener {
    private Button[] buttons = new Button[20];
    private int[] buttonsId = {id.pieces1, id.pieces2, id.pieces3, id.pieces4, id.pieces5, id.pieces6, id.pieces7, id.pieces8, id.pieces9, id.pieces10, id.pieces11, id.pieces12, id.pieces13, id.pieces14, id.pieces15, id.pieces16, id.pieces17, id.pieces18, id.pieces19, id.pieces20};
    private int[] buttonsText = {R.string.pieces1, R.string.pieces2, R.string.pieces3, R.string.pieces4, R.string.pieces5, R.string.pieces6, R.string.pieces7, R.string.pieces8, R.string.pieces9, R.string.pieces10, R.string.pieces11, R.string.pieces12, R.string.pieces13, R.string.pieces14, R.string.pieces15, R.string.pieces16, R.string.pieces17, R.string.pieces18, R.string.pieces19, R.string.pieces20};
    public String fonts = "BZar.ttf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meshkatian);
        setFace();

    }

    private void setFace() {
        // TODO Auto-generated method stub
        Typeface face = Typeface.createFromAsset(getAssets(), "font/" + fonts
                + "");
        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = (Button) findViewById(buttonsId[i]);
            buttons[i].setText(getString(buttonsText[i]));
            buttons[i].setTypeface(face);
            buttons[i].setOnClickListener(this);
        }

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        int i = v.getId();
        Bundle data = new Bundle();
        double[] kookMiditone = {};
        if (i == id.pieces1 || i == id.pieces2 || i == id.pieces3) {
            kookMiditone = new double[]{48, 53, 55, 56.5, 58, 60, 61.5, 63, 65,
                    63.5, 65, 67, 68.5, 70, 72, 73.5, 75, 77, 75.5, 77, 79, 80, 82,
                    84, 85.5, 87, 89};
        } else if (i == id.pieces4 || i == id.pieces5 || i == id.pieces6 || i == id.pieces7 || i == id.pieces8 || i == id.pieces9 || i == id.pieces11 || i == id.pieces12) {
            kookMiditone = new double[]{48, 53, 55, 56.5, 58, 60, 61.5, 63, 65, 63.5,
                    65, 67, 68.5, 70, 72, 74, 75, 77, 75.5, 77, 79, 80, 82, 84, 86,
                    87, 89};
        } else if (i == id.pieces10) {
            kookMiditone = new double[]{48, 53, 55, 56.5, 58, 60, 62, 63, 65, 63.5,
                    65, 67, 68.5, 70, 72, 74, 75, 77, 75.5, 77, 79, 80, 82, 84, 86,
                    87, 89};
        } else if (i == id.pieces14) {
            kookMiditone = new double[]{50, 53, 55, 56.5, 58, 60, 62, 63, 65, 63.5,
                    65, 67, 68.5, 70, 72, 74, 75, 77, 75.5, 77, 79, 80, 82, 84, 85.5,
                    87, 89};
        } else if (i == id.pieces13) { //delangizan
            kookMiditone = new double[]{48, 53, 55, 57, 58.5, 60, 62, 63, 65, 63.5,
                    65, 67, 69, 70, 72, 74, 75, 77, 75.5, 77, 79, 80.50, 82, 84, 86,
                    87, 89};
        } else if (i == id.pieces15) {//qamangiz
            kookMiditone = new double[]{50, 53, 55, 57, 58.5, 60, 62, 63, 65, 63.5,
                    65, 67, 69, 70, 72, 74, 75, 77, 75.5, 77, 79, 80.50, 82, 84, 86,
                    87, 89};
        } else if (i == id.pieces16) {//sarkesh
            kookMiditone = new double[]{48, 53, 55, 56.5, 58, 60, 62, 63, 65, 63.5,
                    65, 67, 69, 70, 72, 74, 75, 77, 75.5, 77, 79, 80, 82, 84, 86,
                    87, 89};
        } else if (i == id.pieces17 || i == id.pieces18) {
            kookMiditone = new double[]{48, 53, 55, 56.5, 59, 60, 62, 63, 65, 64,
                    65, 67, 68.5, 71, 72, 74, 75, 77, 75.5, 77, 79, 80, 82, 84, 86,
                    87, 89};
        } else if (i == id.pieces19) {
            kookMiditone = new double[]{48, 53, 55, 57, 58, 60, 62, 63.5, 65, 64,
                    65, 67, 69, 71, 72, 74, 76, 77, 76, 77, 79, 81, 82, 84, 86,
                    88, 89};
        } else if (i == id.pieces20) {
            kookMiditone = new double[]{48, 53, 55, 56.5, 59, 60, 61.5, 64, 65,
                    64, 65, 67, 68.5, 71, 72, 73.5, 76, 77, 75.5, 78, 79, 80.5, 83,
                    84, 86, 88, 89};
        }

        Button t = (Button) findViewById(i);
        String title = (String) t.getText();

        data.putString("title", title);
        data.putDoubleArray("custom", kookMiditone);

        Intent activity;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean tunerType = prefs.getBoolean("tunerChange",
                false);
        if (!tunerType) activity = new Intent(Meshkatian.this, SanturTuner.class);
        else activity = new Intent(Meshkatian.this, SanturTuner2.class);
        activity.putExtras(data);
        startActivity(activity);


    }

    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }


}
