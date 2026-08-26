package com.PouyaApp.KookYaRSantooR;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowCompat;

import com.kookyar.common.FeatureBar;
import com.kookyar.common.MetronomeLauncher;
import com.kookyar.common.RadifLauncher;

/**
 * The dastgah list of "دوره ابتدایی سنتور".
 *
 * Picking one re-tunes the running tuner for it.
 */
public class DoreyeEbtedaE extends AppCompatActivity {

    private static final double[] SHUR = {51.5, 53, 55, 56.5, 58, 60, 61.5, 63, 65, 63.5,
            65, 67, 68.5, 70, 72, 74, 75, 77, 75.5, 77, 79, 80, 82, 84, 86,
            87, 89};
    private static final double[] SEGAH = {51.5, 53, 55, 56.5, 58, 60, 61.5, 63, 65,
            63.5, 65, 67, 68.5, 70, 72, 73.5, 75, 77, 75.5, 77, 79, 80, 82,
            84, 85.5, 87, 89};
    private static final double[] ESFEHAN = {52, 53, 55, 56.5, 59, 60, 62, 63, 65,
            63.5, 65, 67, 68.5, 71, 72, 74, 75, 77, 75.5, 77, 79, 80, 82,
            84, 86, 87, 89};
    private static final double[] HOMAYUN = {52, 53, 55, 56.5, 59, 60, 62, 63, 65,
            64, 65, 67, 68.5, 71, 72, 74, 75, 77, 75.5, 77, 79, 80, 82,
            84, 86, 87, 89};
    private static final double[] CHAHARGAH = {52, 53, 55, 56.5, 59, 60, 61.5, 63.5,
            66, 64, 65, 67, 68.5, 71, 72, 73.5, 76, 77, 76, 77, 79, 80, 83,
            84, 85.5, 88, 89};
    private static final double[] MAHUR = {52, 53, 55, 56.5, 58, 60, 62, 63, 65, 64,
            65, 67, 69, 70, 72, 73.5, 75, 77, 76, 77, 79, 80, 82, 84, 86,
            87, 89};

    /** One dastgah: its title and the tuning the santur needs for it. */
    private static final class Dastgah {
        final int titleRes;
        final double[] tuning;

        Dastgah(int titleRes, double[] tuning) {
            this.titleRes = titleRes;
            this.tuning = tuning;
        }
    }

    private static final Dastgah[] DASTGAHS = {
            new Dastgah(R.string.ebtedaei1, SHUR),
            new Dastgah(R.string.ebtedaei2, SHUR),
            new Dastgah(R.string.ebtedaei3, SHUR),
            new Dastgah(R.string.ebtedaei4, SHUR),
            new Dastgah(R.string.ebtedaei5, SHUR),
            new Dastgah(R.string.ebtedaei6, SEGAH),
            new Dastgah(R.string.ebtedaei7, ESFEHAN),
            new Dastgah(R.string.ebtedaei8, HOMAYUN),
            new Dastgah(R.string.ebtedaei9, CHAHARGAH),
            new Dastgah(R.string.ebtedaei10, MAHUR),
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), true);
        setContentView(R.layout.activity_pick_list);

        PickList list = new PickList(this, "BZar.ttf");
        for (int i = 0; i < DASTGAHS.length; i++) {
            final Dastgah dastgah = DASTGAHS[i];
            list.addCard(getString(dastgah.titleRes),
                    i % 2 == 0 ? R.color.pickPiece : R.color.pickPieceAlt,
                    com.kookyar.common.R.drawable.ic_kookyar_text,
                    () -> openDastgah(dastgah));
        }
        initFeatureBar();
    }

    private void openDastgah(Dastgah dastgah) {
        Bundle selection = new Bundle();
        selection.putString("title", getString(dastgah.titleRes));
        selection.putDoubleArray("custom", dastgah.tuning.clone());
        SanturTuner.openWith(this, selection);
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
                // Highlighted because a dastgah is part of the book shelf, but
                // still tappable so the shelf itself stays reachable.
                new FeatureBar.Item(
                        getString(com.kookyar.common.R.string.kookyar_feature_books),
                        com.kookyar.common.R.drawable.ic_kookyar_text,
                        true,
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
