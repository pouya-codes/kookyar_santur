package com.PouyaApp.KookYaRSantooR;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowCompat;

import com.kookyar.common.FeatureBar;
import com.kookyar.common.MetronomeLauncher;
import com.kookyar.common.RadifLauncher;

/** The reading list that sits behind the "متن‌ها" menu entry. */
public class ContentMenu extends AppCompatActivity {

    private static final int[] TITLES = {
            R.string.content_1, R.string.content_2, R.string.content_3
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), true);
        setContentView(R.layout.activity_pick_list);

        PickList list = new PickList(this, "BZar.ttf");
        for (int i = 0; i < TITLES.length; i++) {
            final int content = i + 1;
            list.addCard(getString(TITLES[i]),
                    i % 2 == 0 ? R.color.pickPiece : R.color.pickPieceAlt,
                    com.kookyar.common.R.drawable.ic_kookyar_text,
                    () -> openContent(content));
        }
        initFeatureBar();
    }

    private void openContent(int content) {
        Intent activity = new Intent(this, Content.class);
        activity.putExtra("content", content);
        startActivity(activity);
    }

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
                FeatureBar.Item.of(
                        getString(com.kookyar.common.R.string.kookyar_feature_books),
                        com.kookyar.common.R.drawable.ic_kookyar_text,
                        () -> {
                            startActivity(new Intent(this, Books.class));
                            finish();
                        }),
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
}
