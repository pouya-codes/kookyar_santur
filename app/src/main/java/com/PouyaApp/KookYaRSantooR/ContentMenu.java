package com.PouyaApp.KookYaRSantooR;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.PouyaApp.KookYaRSantooR.R.id;

public class ContentMenu extends Activity implements View.OnClickListener {
    private Button content1, content2, content3;
    private Bundle data;
    public String fonts = "BZar.ttf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_menu);
        setFace();
    }

    private void setFace() {
        // TODO Auto-generated method stub
        Typeface face = Typeface.createFromAsset(getAssets(), "font/" + fonts
                + "");
        content1 = (Button) findViewById(id.content1);
        content1.setTypeface(face);
        content1.setOnClickListener(this);
        content2 = (Button) findViewById(id.content2);
        content2.setTypeface(face);
        content2.setOnClickListener(this);
        content3 = (Button) findViewById(id.content3);
        content3.setTypeface(face);
        content3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        Intent activity;
        int viewId = v.getId();
        if (viewId == id.content1) {
            data = new Bundle();
            data.putInt("content", 1);
            activity = new Intent(ContentMenu.this, Content.class);
            activity.putExtras(data);
            startActivity(activity);
        } else if (viewId == id.content2) {
            data = new Bundle();
            data.putInt("content", 2);
            activity = new Intent(ContentMenu.this, Content.class);
            activity.putExtras(data);
            startActivity(activity);
        } else if (viewId == id.content3) {
            data = new Bundle();
            data.putInt("content", 3);
            activity = new Intent(ContentMenu.this, Content.class);
            activity.putExtras(data);
            startActivity(activity);
        }
    }
}
