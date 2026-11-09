package com.PouyaApp.KookYaRSantooR;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowCompat;

import com.kookyar.common.FeatureBar;
import com.kookyar.common.MetronomeLauncher;
import com.kookyar.common.RadifLauncher;

import java.util.List;

/**
 * Kook picker.
 *
 * Picking a kook re-tunes the running tuner rather than opening a second one.
 */
public class SanturMenu extends AppCompatActivity {

    /** One kook entry: its display name and the id the tuner expects. */
    private static final class Kook {
        final int titleRes;
        final int kuk;

        Kook(int titleRes, int kuk) {
            this.titleRes = titleRes;
            this.kuk = kuk;
        }
    }

    private static final Kook[] RAST_KOOKS = {
            new Kook(R.string.shur, 1),
            new Kook(R.string.shurPayver, 12),
            new Kook(R.string.homayun, 6),
            new Kook(R.string.esfehan, 2),
            new Kook(R.string.mahur, 3),
            new Kook(R.string.segah, 4),
            new Kook(R.string.nava, 13),
            new Kook(R.string.chahargah, 5),
    };

    private static final Kook[] CHAP_KOOKS = {
            new Kook(R.string.shorRe, 7),
            new Kook(R.string.HomayunChap, 14),
            new Kook(R.string.esfehanFa, 8),
            new Kook(R.string.MahurChap, 15),
            new Kook(R.string.segahChap, 16),
            new Kook(R.string.chahargahChap, 17),
    };

    private static final Kook[] TALFIQI_KOOKS = {
            new Kook(R.string.faMinor, 9),
            new Kook(R.string.doMinor, 11),
            new Kook(R.string.doMajor, 10),
    };

    private PickList list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), true);
        setContentView(R.layout.activity_pick_list);
        list = new PickList(this, "BNazanin.ttf");
        buildCards();
        initFeatureBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // A kook saved in the custom editor should show up without a restart.
        buildCards();
    }

    private void buildCards() {
        list.clear();

        list.addHeader(getString(R.string.header_rast));
        addKooks(RAST_KOOKS, R.color.pickRast);

        list.addHeader(getString(R.string.header_chap));
        addKooks(CHAP_KOOKS, R.color.pickChap);

        list.addHeader(getString(R.string.header_talfiqi));
        addKooks(TALFIQI_KOOKS, R.color.pickTalfiqi);

        list.addHeader(getString(R.string.header_custom));
        list.addCard(getString(R.string.custom), getString(R.string.custom_kook_hint),
                R.color.pickCustom, com.kookyar.common.R.drawable.ic_kookyar_settings,
                () -> startActivity(new Intent(this, CustomKook.class)));

        List<SavedCustomKook.Entry> saved = SavedCustomKook.loadAll(this);
        if (!saved.isEmpty()) {
            list.addHeader(getString(R.string.saved_kooks_menu_title));
            for (SavedCustomKook.Entry entry : saved) {
                list.addCard(entry.name, R.color.pickSaved,
                        com.kookyar.common.R.drawable.ic_kookyar_kook_list,
                        () -> openSavedKook(entry));
            }
        }
    }

    private void addKooks(Kook[] kooks, int color) {
        for (Kook kook : kooks) {
            list.addCard(getString(kook.titleRes), color,
                    com.kookyar.common.R.drawable.ic_kookyar_kook_list,
                    () -> openKook(kook.kuk));
        }
    }

    private void openKook(int kuk) {
        Bundle selection = new Bundle();
        selection.putInt("kuk", kuk);
        SanturTuner.openWith(this, selection);
    }

    private void openSavedKook(SavedCustomKook.Entry entry) {
        Bundle selection = new Bundle();
        selection.putDoubleArray("custom", entry.miditones);
        selection.putString("noteName", entry.name);
        SanturTuner.openWith(this, selection);
    }

    /** Same flat switcher as the tuner, so features stay one tap apart. */
    private void initFeatureBar() {
        FeatureBar featureBar = findViewById(R.id.feature_bar);
        featureBar.setAccentColor(ContextCompat.getColor(this, R.color.accentColor));
        featureBar.setInactiveColor(ContextCompat.getColor(this, R.color.onSurfaceVariant));
        featureBar.setItems(
                FeatureBar.Item.of(
                        getString(com.kookyar.common.R.string.kookyar_feature_tuner),
                        com.kookyar.common.R.drawable.ic_kookyar_tuner,
                        () -> SanturTuner.bringToFront(this)),
                FeatureBar.Item.current(
                        getString(com.kookyar.common.R.string.kookyar_feature_kooks),
                        com.kookyar.common.R.drawable.ic_kookyar_kook_list),
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
