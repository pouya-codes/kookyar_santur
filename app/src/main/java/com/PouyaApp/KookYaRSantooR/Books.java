package com.PouyaApp.KookYaRSantooR;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.PouyaApp.KookYaRSantooR.R.id;
import com.google.android.material.card.MaterialCardView;

public class Books extends AppCompatActivity {

    public String fonts = "BNazanin.ttf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), true);
        setContentView(R.layout.activity_books);

        Typeface face = Typeface.createFromAsset(getAssets(), "font/" + fonts);

        setTypeface(face, id.TextView_dastor);
        setTypeface(face, id.TextView_shive);
        setTypeface(face, id.TextView_ebtedaei);
        setTypeface(face, id.tvMeshkatian);
        setTypeface(face, id.TextView_golnar);
        setTypeface(face, id.TextView_yadegaran_esf);
        setTypeface(face, id.TextView_yadegaran_hom);
        setTypeface(face, id.TextView_hazarDastan_3gah);
        setTypeface(face, id.TextView_hazardastan_4gah);
        setTypeface(face, id.TextView_sepideh);

        setupTunerBook(id.book_dastur, 1);
        setupTunerBook(id.books_shive, 2);
        setupTunerBook(id.book_golnar, 3);
        setupTunerBook(id.book_yadegaran, 4);
        setupTunerBook(id.book_yadegaran_homayun, 5);
        setupTunerBook(id.book_dastan, 6);
        setupTunerBook(id.book_dastan_4gah, 7);
        setupTunerBook(id.book_sepideh, 8);
        setupActivityBook(id.book_meshkatian, Meshkatian.class);
        setupActivityBook(id.book_dore, DoreyeEbtedaE.class);
    }

    private void setTypeface(Typeface face, int textViewId) {
        TextView tv = findViewById(textViewId);
        tv.setTypeface(face);
    }

    private void setupTunerBook(int cardId, int bookNumber) {
        MaterialCardView card = findViewById(cardId);
        disableChildClicks(card);
        card.setOnClickListener(v -> openTunerWithBook(bookNumber));
    }

    private void setupActivityBook(int cardId, Class<?> activityClass) {
        MaterialCardView card = findViewById(cardId);
        disableChildClicks(card);
        card.setOnClickListener(v ->
                startActivity(new Intent(Books.this, activityClass)));
    }

    private void disableChildClicks(ViewGroup parent) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            child.setClickable(false);
            child.setFocusable(false);
            if (child instanceof ViewGroup) {
                disableChildClicks((ViewGroup) child);
            }
        }
    }

    private void openTunerWithBook(int bookNumber) {
        Intent activity = new Intent(Books.this, SanturTuner.class);
        activity.putExtra("book", bookNumber);
        startActivity(activity);
    }

}
