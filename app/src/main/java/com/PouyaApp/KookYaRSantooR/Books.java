package com.PouyaApp.KookYaRSantooR;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowCompat;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.PouyaApp.KookYaRSantooR.R.id;
import com.google.android.material.card.MaterialCardView;
import com.kookyar.common.FeatureBar;
import com.kookyar.common.MetronomeLauncher;
import com.kookyar.common.RadifLauncher;

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

        initFeatureBar();
    }

    /** Flat switcher: the same bar as the tuner, so no screen is a dead end. */
    private void initFeatureBar() {
        FeatureBar featureBar = findViewById(R.id.feature_bar);
        featureBar.setAccentColor(ContextCompat.getColor(this, R.color.accentColor));
        featureBar.setInactiveColor(ContextCompat.getColor(this, R.color.onSurfaceVariant));
        featureBar.setItems(
                FeatureBar.Item.of(
                        getString(com.kookyar.common.R.string.kookyar_feature_tuner),
                        com.kookyar.common.R.drawable.ic_kookyar_tuner,
                        () -> SanturTuner.bringToFront(this)),
                FeatureBar.Item.of(
                        getString(com.kookyar.common.R.string.kookyar_feature_kooks),
                        com.kookyar.common.R.drawable.ic_kookyar_kook_list,
                        () -> {
                            startActivity(new Intent(this, SanturMenu.class));
                            finish();
                        }),
                FeatureBar.Item.current(
                        getString(com.kookyar.common.R.string.kookyar_feature_books),
                        com.kookyar.common.R.drawable.ic_kookyar_text),
                FeatureBar.Item.of(
                        getString(com.kookyar.common.R.string.kookyar_feature_metronome),
                        com.kookyar.common.R.drawable.ic_kookyar_metronome,
                        () -> MetronomeLauncher.open(this)),
                FeatureBar.Item.of(
                        getString(com.kookyar.common.R.string.kookyar_feature_radif),
                        com.kookyar.common.R.drawable.ic_kookyar_radif,
                        () -> RadifLauncher.open(this)),
                FeatureBar.Item.of(
                        getString(com.kookyar.common.R.string.kookyar_feature_settings),
                        com.kookyar.common.R.drawable.ic_kookyar_settings,
                        () -> startActivity(new Intent(this, Prefs.class))));
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
        Bundle selection = new Bundle();
        selection.putInt("book", bookNumber);
        SanturTuner.openWith(this, selection);
    }

}
