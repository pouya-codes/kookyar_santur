package com.PouyaApp.KookYaRSantooR;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.PouyaApp.KookYaRSantooR.R.id;

public class KookType extends AppCompatActivity implements View.OnClickListener {
    private Button ketab, dastgah;
    public String fonts = "BZar.ttf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kook_type);
        ketab = (Button) findViewById(id.button_ketab);

        Typeface face = Typeface.createFromAsset(getAssets(), "font/" + fonts
                + "");
        ketab.setTypeface(face);
        String b_ketab = (String) ketab.getText().toString();
        ketab.setText(PersianReshape.reshape(b_ketab));
        ketab.setOnClickListener(this);

        dastgah = (Button) findViewById(id.button_dastgah);
        dastgah.setTypeface(face);
        String b_dastgah = (String) dastgah.getText();
        dastgah.setText(PersianReshape.reshape(b_dastgah));
        dastgah.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent activity;
        int viewId = v.getId();
        if (viewId == id.button_dastgah) {
            activity = new Intent(KookType.this, SanturMenu.class);
            startActivity(activity);
        } else if (viewId == id.button_ketab) {
            activity = new Intent(KookType.this, Books.class);
            startActivity(activity);
        }
    }
}
