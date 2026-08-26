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
 * The twenty pieces of "بیست قطعه برای سنتور".
 *
 * Picking a piece re-tunes the running tuner for it.
 */
public class Meshkatian extends AppCompatActivity {

    private static final double[] SHUR_SEGAH = {48, 53, 55, 56.5, 58, 60, 61.5, 63, 65,
            63.5, 65, 67, 68.5, 70, 72, 73.5, 75, 77, 75.5, 77, 79, 80, 82,
            84, 85.5, 87, 89};
    private static final double[] SHUR = {48, 53, 55, 56.5, 58, 60, 61.5, 63, 65, 63.5,
            65, 67, 68.5, 70, 72, 74, 75, 77, 75.5, 77, 79, 80, 82, 84, 86,
            87, 89};
    private static final double[] SHUR_SOL = {48, 53, 55, 56.5, 58, 60, 62, 63, 65, 63.5,
            65, 67, 68.5, 70, 72, 74, 75, 77, 75.5, 77, 79, 80, 82, 84, 86,
            87, 89};
    private static final double[] SHUR_RE = {50, 53, 55, 56.5, 58, 60, 62, 63, 65, 63.5,
            65, 67, 68.5, 70, 72, 74, 75, 77, 75.5, 77, 79, 80, 82, 84, 85.5,
            87, 89};
    private static final double[] DELANGIZAN = {48, 53, 55, 57, 58.5, 60, 62, 63, 65,
            63.5, 65, 67, 69, 70, 72, 74, 75, 77, 75.5, 77, 79, 80.50, 82, 84, 86,
            87, 89};
    private static final double[] QAMANGIZ = {50, 53, 55, 57, 58.5, 60, 62, 63, 65,
            63.5, 65, 67, 69, 70, 72, 74, 75, 77, 75.5, 77, 79, 80.50, 82, 84, 86,
            87, 89};
    private static final double[] SARKESH = {48, 53, 55, 56.5, 58, 60, 62, 63, 65,
            63.5, 65, 67, 69, 70, 72, 74, 75, 77, 75.5, 77, 79, 80, 82, 84, 86,
            87, 89};
    private static final double[] HOMAYUN = {48, 53, 55, 56.5, 59, 60, 62, 63, 65,
            64, 65, 67, 68.5, 71, 72, 74, 75, 77, 75.5, 77, 79, 80, 82, 84, 86,
            87, 89};
    private static final double[] MAHUR = {48, 53, 55, 57, 58, 60, 62, 63.5, 65,
            64, 65, 67, 69, 71, 72, 74, 76, 77, 76, 77, 79, 81, 82, 84, 86,
            88, 89};
    private static final double[] CHAHARGAH = {48, 53, 55, 56.5, 59, 60, 61.5, 64, 65,
            64, 65, 67, 68.5, 71, 72, 73.5, 76, 77, 75.5, 78, 79, 80.5, 83,
            84, 86, 88, 89};

    /** One piece: its title and the tuning the santur needs for it. */
    private static final class Piece {
        final int titleRes;
        final double[] tuning;

        Piece(int titleRes, double[] tuning) {
            this.titleRes = titleRes;
            this.tuning = tuning;
        }
    }

    private static final Piece[] PIECES = {
            new Piece(R.string.pieces1, SHUR_SEGAH),
            new Piece(R.string.pieces2, SHUR_SEGAH),
            new Piece(R.string.pieces3, SHUR_SEGAH),
            new Piece(R.string.pieces4, SHUR),
            new Piece(R.string.pieces5, SHUR),
            new Piece(R.string.pieces6, SHUR),
            new Piece(R.string.pieces7, SHUR),
            new Piece(R.string.pieces8, SHUR),
            new Piece(R.string.pieces9, SHUR),
            new Piece(R.string.pieces10, SHUR_SOL),
            new Piece(R.string.pieces11, SHUR),
            new Piece(R.string.pieces12, SHUR),
            new Piece(R.string.pieces13, DELANGIZAN),
            new Piece(R.string.pieces14, SHUR_RE),
            new Piece(R.string.pieces15, QAMANGIZ),
            new Piece(R.string.pieces16, SARKESH),
            new Piece(R.string.pieces17, HOMAYUN),
            new Piece(R.string.pieces18, HOMAYUN),
            new Piece(R.string.pieces19, MAHUR),
            new Piece(R.string.pieces20, CHAHARGAH),
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), true);
        setContentView(R.layout.activity_pick_list);

        PickList list = new PickList(this, "BZar.ttf");
        for (int i = 0; i < PIECES.length; i++) {
            final Piece piece = PIECES[i];
            list.addCard(getString(piece.titleRes),
                    i % 2 == 0 ? R.color.pickPiece : R.color.pickPieceAlt,
                    com.kookyar.common.R.drawable.ic_kookyar_text,
                    () -> openPiece(piece));
        }
        initFeatureBar();
    }

    private void openPiece(Piece piece) {
        Bundle selection = new Bundle();
        selection.putString("title", getString(piece.titleRes));
        selection.putDoubleArray("custom", piece.tuning.clone());
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
                // Highlighted because a piece is part of the book shelf, but still
                // tappable so the shelf itself stays reachable.
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
