package com.PouyaApp.KookYaRSantooR;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

public class Content extends Activity {
    public String fonts = "BNazanin.ttf";
    private TextView help;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        Bundle data = getIntent().getExtras();
        int content = data.getInt("content");
        help = (TextView) findViewById(R.id.tv_content);
        if (content == 1)
            help.setText(getText(R.string.text1));
        if (content == 2)
            help.setText(getText(R.string.text2));
        if (content == 3)
            help.setText(getText(R.string.text3));

        Typeface face = Typeface.createFromAsset(getAssets(), "font/" + fonts
                + "");

        // help.setText(Html.fromHtml(getString(R.string.text1)));
        help.setTypeface(face);

    }
}
