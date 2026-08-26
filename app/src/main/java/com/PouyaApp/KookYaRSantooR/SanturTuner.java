package com.PouyaApp.KookYaRSantooR;

import com.kookyar.common.FeatureBar;
import com.kookyar.common.MetronomeLauncher;
import com.kookyar.common.RadifLauncher;
import com.kookyar.common.PersianReshape;
import com.kookyar.common.PitchView;
import com.kookyar.common.TunerPitchCoordinator;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import androidx.activity.OnBackPressedCallback;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowCompat;
import androidx.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.PouyaApp.KookYaRSantooR.R.id;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import android.widget.Button;

import org.puredata.android.io.AudioParameters;
import org.puredata.android.service.PdService;
import org.puredata.android.utils.PdUiDispatcher;
import org.puredata.core.PdBase;
import org.puredata.core.PdListener;
import org.puredata.core.utils.IoUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SanturTuner extends AppCompatActivity implements OnClickListener {
    private volatile boolean threadRuned = false;
    private volatile boolean threadRuned2 = false;
    private static final String TAG = "KookYaR";
    private static final int PERMISSION_REQUEST_RECORD_AUDIO = 1001;
    private static final String LAST_KUK_KEY = "lastKuk";
    /** The keys owned by the settings screen. */
    private static final Set<String> SETTINGS_KEYS = new HashSet<>(
            Arrays.asList("mi", "ref", "type", "sensitive", "changepitch"));
    public static boolean mi;
    final private int highSensitive = 55;
    final private int midSensitive = 50;
    final private int lowSensitive = 45;
    private Bundle data;
    private final TunerPitchCoordinator pitchCoordinator = new TunerPitchCoordinator();
    int setKookPitch = 0;
    OnSharedPreferenceChangeListener listener;
    private Toolbar toolbar;
    private PdUiDispatcher dispatcher;
    private PdService pdService = null;
    private CheckBox tuning;
    private LinearLayout wire;
    private LinearLayout kharakDo;
    private LinearLayout kharakMi;
    private LinearLayout kharakLa;
    private LinearLayout kharakSi;
    private LinearLayout positonShow;
    private LinearLayout tabelLayout;
    private Button[] buttons = new Button[9];
    private int[] buttonsId = {id.button_1, id.button_2, id.button_3,
            id.button_4, id.button_5, id.button_6, id.button_7, id.button_8,
            id.button_9};
    private int kuk, book;
    private TextView pitchLabel;
    private PitchView pitchView;
    private int kukposition = 0;
    private int santurType, pressure;
    private RadioButton rd1, rd2, rd3;
    private double[] Miditone = new double[27];
    private double[] MiditoneOld = new double[27];
    private int laFrequens = 440;
    private int sensitiveLevel = midSensitive;
    private float pitchChange =0;
    private int[] tempeament = new int[24];
    private SharedPreferences prefs;
    private int selectedStringIndex = -1;
    private int lastTunedIndex = -1;

    private static final int[] STRING_DRAWABLES = {
            R.drawable.santur_1, R.drawable.santur_2, R.drawable.santur_3,
            R.drawable.santur_4, R.drawable.santur_5, R.drawable.santur_6,
            R.drawable.santur_7, R.drawable.santur_8, R.drawable.santur_9,
            R.drawable.santur_10, R.drawable.santur_11, R.drawable.santur_12,
            R.drawable.santur_13, R.drawable.santur_14, R.drawable.santur_15,
            R.drawable.santur_16, R.drawable.santur_17, R.drawable.santur_18,
            R.drawable.santur_19, R.drawable.santur_20, R.drawable.santur_21,
            R.drawable.santur_22, R.drawable.santur_23, R.drawable.santur_24,
            R.drawable.santur_25, R.drawable.santur_26, R.drawable.santur_27
    };

    private static final String[] STRING_ORDINALS = {
            "اول", "دوم", "سوم", "چهارم", "پنجم", "ششم", "هفتم", "هشتم", "نهم"
    };

    private final ServiceConnection pdConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            pdService = ((PdService.PdBinder) service).getService();
            try {
                initPd();
                loadPatch();

            } catch (IOException e) {
                Toast errorToast = Toast
                        .makeText(
                                getApplicationContext(),
                                "اشکال در برقراری ارتباط با رابط ورودی/خروجی.لطفا برنامه هایی که از  واسط میکروفن یا بلندگو استفاده می کنند را ببندید و مجددا وارد شوید. ",
                                Toast.LENGTH_LONG);
                errorToast.show();
                Log.e(TAG, e.toString());

                finish();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };


    @Override
    public void onBackPressed() {
        handleBackWithSavePrompt();
    }

    private void handleBackWithSavePrompt() {
        if (Miditone == null) {
            finishTuner();
            return;
        }

        boolean changed = false;
        for (int i = 0; i < MiditoneOld.length; i++) {
            if (Miditone[i] != MiditoneOld[i]) {
                changed = true;
                break;
            }
        }

        if (!changed) {
            finishTuner();
            return;
        }

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SanturTuner.this);
        alertDialog.setTitle("ذخیره کوک انتخاب شده به عنوان کوک فعلی سنتور");
        alertDialog.setMessage(
                "با انتخاب کوک انتخاب شده به عنوان کوک فعلی سنتور، در صورتی که کوک دیگری را انتخاب نمایید، سیم هایی که نیازمند تغییر کوک می باشند به رنگ زرد نمایش داده می شوند");
        alertDialog.setIcon(android.R.drawable.ic_menu_save);
        alertDialog.setPositiveButton("بله", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences.Editor editor = prefs.edit();
                for (int j = 0; j < Miditone.length; j++) {
                    editor.putFloat(j + "", (float) Miditone[j]);
                }
                editor.apply();
                finishTuner();
            }
        });
        alertDialog.setNegativeButton("خیر", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finishTuner();
            }
        });
        alertDialog.show();
    }

    private void finishTuner() {
        finish();
    }

    private void registerBackPressedHandler() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                handleBackWithSavePrompt();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu);
        MenuInflater blowUp = getMenuInflater();
        blowUp.inflate(R.menu.santur_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.help_tuner) {
            startActivity(new Intent(SanturTuner.this, TunerHelp.class));
        } else if (itemId == id.change_tempe) {
            startActivity(new Intent(SanturTuner.this, CustomeTempeament.class));
        } else if (itemId == R.id.kook_help_menu) {
            startActivity(new Intent(SanturTuner.this, KookHelp.class));
        } else if (itemId == R.id.content_menu) {
            startActivity(new Intent(SanturTuner.this, ContentMenu.class));
        }
        return true;
    }

    /** Flat switcher: every feature is one tap from the tuner. */
    private void initFeatureBar() {
        FeatureBar featureBar = findViewById(R.id.feature_bar);
        featureBar.setAccentColor(ContextCompat.getColor(this, R.color.accentColor));
        featureBar.setInactiveColor(ContextCompat.getColor(this, R.color.onSurfaceVariant));
        featureBar.setItems(
                FeatureBar.Item.current(
                        getString(com.kookyar.common.R.string.kookyar_feature_tuner),
                        com.kookyar.common.R.drawable.ic_kookyar_tuner),
                FeatureBar.Item.of(
                        getString(com.kookyar.common.R.string.kookyar_feature_kooks),
                        com.kookyar.common.R.drawable.ic_kookyar_kook_list,
                        () -> startActivity(new Intent(this, SanturMenu.class))),
                FeatureBar.Item.of(
                        getString(com.kookyar.common.R.string.kookyar_feature_books),
                        com.kookyar.common.R.drawable.ic_kookyar_text,
                        () -> startActivity(new Intent(this, Books.class))),
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), true);
        setContentView(R.layout.activity_tuner);
        registerBackPressedHandler();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            extras = new Bundle();
        }
        data = extras;
        kuk = data.getInt("kuk", 0);
        book = data.getInt("book", 0);
        Miditone = data.getDoubleArray("custom");
        // The tuner is the launcher screen, so a direct start has no kook to
        // load: fall back to the last one the user picked.
        SharedPreferences startPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (kuk == 0 && book == 0 && Miditone == null) {
            kuk = startPrefs.getInt(LAST_KUK_KEY, 1);
        } else if (kuk > 0) {
            startPrefs.edit().putInt(LAST_KUK_KEY, kuk).apply();
        }
        prefsListnner();

        initGui();
        if (book > 0) {
            prepareTunerFromBook();
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    PERMISSION_REQUEST_RECORD_AUDIO);
        } else {
            initAudio();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_RECORD_AUDIO) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initAudio();
            } else {
                Toast.makeText(this, "دسترسی به میکروفون برای کار کردن کوک یار ضروری است", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    private void initAudio() {
        bindService(new Intent(this, PdService.class), pdConnection, BIND_AUTO_CREATE);
    }

    private void prepareTunerFromBook() {
        rd1.setChecked(true);
        rd2.setChecked(false);
        rd3.setChecked(false);
        positonShow.setBackgroundResource(R.drawable.zard);
        kukposition = 1;

        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setEnabled(true);
            styleStringButtonDefault(buttons[i]);
            if (Miditone[i] != MiditoneOld[i] && MiditoneOld[i] != 0) {
                styleStringButtonNeedsTune(buttons[i]);
            }
        }

        int firstString = 0;
        for (int i = 0; i < 9; i++) {
            if (Miditone[i] != MiditoneOld[i] && MiditoneOld[i] != 0) {
                firstString = i;
                break;
            }
        }
        final int stringToSelect = firstString;
        buttons[stringToSelect].post(() -> buttons[stringToSelect].performClick());
    }

    private void prefsListnner() {
        // TODO Auto-generated method stub
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(this);

        listener = new OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs,
                                                  String key) {
                // Only a change on the settings screen should retune. The tuner
                // also writes the saved tuning, the temperament and the last
                // kook to the same file, and those must not be picked up here.
                if (key != null && !SETTINGS_KEYS.contains(key)) {
                    return;
                }
                Boolean change = prefs.getBoolean("mi", false);

                laFrequens = Integer.parseInt(prefs.getString("ref", "440"));
                pitchView.setAFrequnse(laFrequens);

                if (prefs.getString("type", "سل کوک").compareTo("سل کوک") == 0) {
                    santurType = 0;
                    pitchView.setSetarType("سل کوک");
                } else if (prefs.getString("type", "سل کوک")
                        .compareTo("لا کوک") == 0) {
                    santurType = 2;
                    pitchView.setSetarType("لا کوک");
                } else if (prefs.getString("type", "سل کوک")
                        .compareTo("سی کوک") == 0) {
                    santurType = 4;
                    pitchView.setSetarType("سی کوک");
                } else if (prefs.getString("type", "سل کوک")
                        .compareTo("دو کوک") == 0) {
                    santurType = 5;
                    pitchView.setSetarType("سی کوک");
                } else if (prefs.getString("type", "سل کوک")
                        .compareTo("می کوک") == 0) {
                    santurType = -2;
                    pitchView.setSetarType("می کوک");
                } else if (prefs.getString("type", "سل کوک")
                        .compareTo("فا کوک") == 0) {
                    santurType = -1;
                    pitchView.setSetarType("فا کوک");
                }

                if (change) {
                    kharakDo.setBackgroundResource(R.drawable.mi);
                    if (kuk == 1) {

                        Miditone[0] = 51.5;
                    } else if (kuk == 2) {

                        Miditone[0] = 51.5;
                    } else if (kuk == 3) {

                        Miditone[0] = 52;
                    } else if (kuk == 4) {

                        Miditone[0] = 51.5;
                    } else if (kuk == 5) {

                        Miditone[0] = 51.5;
                    } else if (kuk == 6) {

                        Miditone[0] = 52;
                    } else if (kuk == 7) {

                        Miditone[0] = 51.5;
                    } else if (kuk == 8) {

                        Miditone[0] = 52;
                    } else if (kuk == 9) {

                        Miditone[0] = 52;
                    } else if (kuk == 10) {

                        Miditone[0] = 52;
                    } else if (kuk == 11) {

                        Miditone[0] = 52;
                    } else if (kuk == 14) {

                        Miditone[0] = 52;
                    } else if (kuk == 15) {

                        Miditone[0] = 52;
                    } else if (kuk == 16) {

                        Miditone[0] = 51.5;
                    } else if (kuk == 17) {

                        Miditone[0] = 51.5;
                    }
                } else {
                    if (mi) {

                        kharakDo.setBackgroundResource(R.drawable.doo);

                    }
                    if (kuk == 7)
                        Miditone[0] = 50;
                    else {

                        Miditone[0] = 48;
                    }

                }

                if (prefs.getString("sensitive", "2").compareTo("3") == 0) {
                    sensitiveLevel = highSensitive;
                } else if (prefs.getString("sensitive", "2").compareTo("1") == 0) {
                    sensitiveLevel = midSensitive;
                } else {
                    sensitiveLevel = lowSensitive;
                }
                if(prefs.getString("changepitch","1").compareTo("1")==0) {
                    pitchChange = 0 ;
                }
                else if(prefs.getString("changepitch","2").compareTo("2")==0) {
                    pitchChange =(float) -0.5 ;
                }
                else if(prefs.getString("changepitch","3").compareTo("3")==0) {
                    pitchChange = -1 ;
                }
                else if(prefs.getString("changepitch","4").compareTo("4")==0) {
                    pitchChange = -2 ;
                }

            }
        };

        prefs.registerOnSharedPreferenceChangeListener(listener);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        pitchCoordinator.destroy();
        try {
            unbindService(pdConnection);
        } catch (Exception e) {
            // Service may not have been bound if permission was denied
        }
    }

    private void initGui() {
        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        for (int j = 0; j < tempeament.length; j++) {
            tempeament[j] = prefs.getInt(j + "Tempeament", 0);
        }

        LinearLayout tuning_ = (LinearLayout) findViewById(id.tuning);
        tuning_.setOnClickListener(this);

        toolbar = (Toolbar) findViewById(id.app_bar);
        setSupportActionBar(toolbar);

        tabelLayout = (LinearLayout) findViewById(id.tableLayout_main);
        tabelLayout.setOnClickListener(this);
//		Typeface face = Typeface.createFromAsset(getAssets(), "font/" + fonts
//				+ "");

        // ivSantur = (ImageView) findViewById(R.id.imageView1);
        wire = (LinearLayout) findViewById(id.layout_sim);
        kharakDo = (LinearLayout) findViewById(id.layout_kharakDo);
        kharakLa = (LinearLayout) findViewById(id.layout_kharakla);
        kharakMi = (LinearLayout) findViewById(id.layout_kharakMi);
        kharakSi = (LinearLayout) findViewById(id.layout_kharakSi);
        positonShow = (LinearLayout) findViewById(id.layout_positon);
        kharakLa.setBackgroundResource(R.drawable.la);
        kharakMi.setBackgroundResource(R.drawable.mii);
        kharakSi.setBackgroundResource(R.drawable.si);
        tuning = (CheckBox) findViewById(id.checkBox_tuning);


        //	tuning.setTypeface(face);
        tuning.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                lastTunedIndex = -1;
                if (tuning.isChecked()) {
                    positonShow.setBackgroundResource(R.drawable.empty);
                    Toast.makeText(
                            getApplicationContext(),
                            "توجه : در صورتی که اولین بار از سنتور را در دستگاه "
                                    + getTitle()
                                    + " کوک می کنید، بهتر است سیم ها را به صورت دستی انتخاب کنید",
                            Toast.LENGTH_LONG).show();
                }
            }
        });


        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = (Button) findViewById(buttonsId[i]);
            buttons[i].setOnClickListener(this);
            styleStringButtonDisabled(buttons[i]);
            buttons[i].setEnabled(false);
        }

        // ------------------------------------------------
        pitchLabel = (TextView) findViewById(R.id.pitch_label);
        pitchView = (PitchView) findViewById(R.id.pitch_view);

        rd1 = (RadioButton) findViewById(R.id.zardRB);

        String str_rb1 = (String) rd1.getText().toString();
        rd1.setText(PersianReshape.reshape(str_rb1));
        rd1.setOnClickListener(this);
        rd2 = (RadioButton) findViewById(R.id.sefidRB);

        String str_rb2 = (String) rd2.getText().toString();
        rd2.setText(PersianReshape.reshape(str_rb2));
        rd2.setOnClickListener(this);
        rd3 = (RadioButton) findViewById(R.id.poshtRB);

        String str_rb3 = (String) rd3.getText().toString();
        rd3.setText(PersianReshape.reshape(str_rb3));
        rd3.setOnClickListener(this);

        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        laFrequens = Integer.parseInt(prefs.getString("ref", "440"));
        pitchView.setAFrequnse(laFrequens);

        for (int j = 0; j < MiditoneOld.length; j++) {
            MiditoneOld[j] = (double) prefs.getFloat(j + "",
                    0);
        }

        mi = prefs.getBoolean("mi", false);
        applyKookSelection();

        if (prefs.getString("type", "سل کوک").compareTo("سل کوک") == 0) {
            santurType = 0;
            pitchView.setSetarType("سل کوک");
        } else if (prefs.getString("type", "سل کوک").compareTo("لا کوک") == 0) {
            santurType = 2;
            pitchView.setSetarType("لا کوک");
        } else if (prefs.getString("type", "سل کوک").compareTo("سی کوک") == 0) {
            santurType = 4;
            pitchView.setSetarType("سی کوک");
        } else if (prefs.getString("type", "سل کوک").compareTo("دو کوک") == 0) {
            santurType = 5;
            pitchView.setSetarType("سی کوک");
        } else if (prefs.getString("type", "سل کوک").compareTo("می کوک") == 0) {
            santurType = -2;
            pitchView.setSetarType("می کوک");
        } else if (prefs.getString("type", "سل کوک")
                .compareTo("فا کوک") == 0) {
            santurType = -1;
            pitchView.setSetarType("فا کوک");
        }

        if (prefs.getString("sensitive", "2").compareTo("3") == 0) {
            sensitiveLevel = highSensitive;
        } else if (prefs.getString("sensitive", "2").compareTo("1") == 0) {
            sensitiveLevel = midSensitive;
        } else {
            sensitiveLevel = lowSensitive;
        }

        pitchLabel.setText(PersianReshape
                .reshape("سیم های مورد نظر جهت کوک کردن را انتخاب کنید"));
        if(prefs.getString("changepitch","1").compareTo("1")==0) {
            pitchChange = 0 ;
        }
        else if(prefs.getString("changepitch","2").compareTo("2")==0) {
            pitchChange =(float) -0.5 ;
        }
        else if(prefs.getString("changepitch","3").compareTo("3")==0) {
            pitchChange = -1 ;
        }
        else if(prefs.getString("changepitch","4").compareTo("4")==0) {
            pitchChange = -2 ;
        }

        initFeatureBar();
    }

    /**
     * Re-tunes the running tuner for a kook, book or custom tuning.
     *
     * The tuner is the launcher screen and runs as a single task, so the pickers
     * hand their choice to the instance that is already on screen instead of
     * stacking a second tuner on top of it.
     */
    static void openWith(Activity source, Bundle selection) {
        Intent intent = new Intent(source, SanturTuner.class);
        intent.putExtras(selection);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        source.startActivity(intent);
        source.finish();
    }

    /**
     * Returns to the tuner and leaves its kook alone.
     *
     * Used by the feature bar of the picker screens, which may sit more than one
     * level above the tuner.
     */
    static void bringToFront(Activity source) {
        Intent intent = new Intent(source, SanturTuner.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        source.startActivity(intent);
        source.finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle extras = intent.getExtras();
        if (extras == null) {
            return;
        }
        if (extras.getInt("kuk", 0) == 0 && extras.getInt("book", 0) == 0
                && extras.getDoubleArray("custom") == null) {
            // Plain re-entry, e.g. the feature bar or the service notification.
            return;
        }
        setIntent(intent);
        data = extras;
        kuk = extras.getInt("kuk", 0);
        book = extras.getInt("book", 0);
        Miditone = extras.getDoubleArray("custom");

        applyKookSelection();
        clearStringSelection();
        if (book > 0) {
            prepareTunerFromBook();
        }
        if (kuk > 0) {
            prefs.edit().putInt(LAST_KUK_KEY, kuk).apply();
        }
    }

    /**
     * Loads the tuning table for the current kuk/book into {@link #Miditone}
     * and titles the screen after it.
     *
     * The bridge artwork is reset first because a previous selection may have
     * swapped it for a variant.
     */
    private void applyKookSelection() {
        kharakDo.setBackgroundResource(mi ? R.drawable.mi : R.drawable.doo);
        kharakLa.setBackgroundResource(R.drawable.la);
        kharakMi.setBackgroundResource(R.drawable.mii);
        kharakSi.setBackgroundResource(R.drawable.si);
        pitchView.setKookType(kuk == 12 ? 0 : 1);

        double[] shurMiditone = {48, 53, 55, 56.5, 58, 60, 61.5, 63, 65, 63.5,
                65, 67, 68.5, 70, 72, 74, 75, 77, 75.5, 77, 79, 80, 82, 84, 86,
                87, 89};
        double[] navaMiditone = {48, 53, 55, 56.5, 58, 60, 62, 63, 65, 63.5,
                65, 67, 68.5, 70, 72, 73.5, 75, 77, 75.5, 77, 79, 80, 82, 84, 85,
                87, 89};
        double[] shurMiditonePayvar = {51.80, 53.28, 55.32, 56.73, 58.28,
                60.38, 61.86, 63.25, 65.34, 63.80, 65.28, 67.32, 68.73, 70.28,
                72.38, 74.34, 75.25, 77.34, 75.80, 77.28, 79.32, 80.20, 82.28,
                84.38, 86.34, 87.25, 89.34};
        double[] esfehanMiditone = {48, 53, 55, 56.5, 59, 60, 62, 63, 65,
                63.5, 65, 67, 68.5, 71, 72, 74, 75, 77, 75.5, 77, 79, 80, 83,
                84, 86, 87, 89};
        double[] mahurMiditone = {48, 53, 55, 56.5, 58, 60, 62, 63, 65, 64,
                65, 67, 69, 70, 72, 73.5, 75, 77, 76, 77, 79, 80, 82, 84, 86,
                87, 89};
        double[] segahMiditone = {48, 53, 55, 56.5, 58, 60, 61.5, 63, 65,
                63.5, 65, 67, 68.5, 70, 72, 73.5, 75, 77, 75.5, 77, 79, 80, 82,
                84, 85.5, 87, 89};
        double[] chahargahMiditone = {48, 53, 55, 56.5, 59, 60, 61.5, 63.5,
                66, 64, 65, 67, 68.5, 71, 72, 73.5, 76, 77, 76, 77, 79, 80, 83,
                84, 85.5, 88, 89};
        double[] homayunMiditone = {48, 53, 55, 56.5, 59, 60, 62, 63, 65, 64,
                65, 67, 68.5, 71, 72, 74, 75, 77, 75.5, 77, 79, 80, 82, 84, 86,
                87, 89};
        double[] shourReMiditone = {50, 53, 55, 57, 58, 60, 62, 63, 65, 63.5,
                65, 67, 69, 70, 72, 74, 75, 77, 76, 77, 79, 80, 81.5, 84, 86,
                87, 89};
        double[] esfehanFaMiditone = {48, 53, 55, 56.5, 58, 60, 61.5, 63, 65,
                64, 65, 67, 68, 70, 72, 73, 75, 77, 76, 77, 79, 80, 82, 84, 85,
                87, 89};
        double[] faMinorMiditone = {48, 53, 55, 56, 58, 60, 61, 63, 65, 64,
                65, 67, 68, 70, 72, 73, 75, 77, 76, 77, 79, 80, 82, 84, 85, 87,
                89};
        double[] doMajorMiditone = {48, 53, 55, 57, 59, 60, 62, 64, 65, 64,
                65, 67, 69, 71, 72, 74, 76, 77, 76, 77, 79, 81, 83, 84, 86, 88,
                89};
        double[] doMinorMiditone = {48, 53, 55, 56, 58, 60, 62, 63, 65, 64,
                65, 67, 68, 70, 72, 74, 75, 77, 76, 77, 79, 80, 82, 84, 86, 87,
                89};
        double[] segahMiKoron = {48, 53, 55, 56.5, 58, 60, 62, 63, 65,
                63.5, 65, 67, 68.5, 70.50, 72, 74, 75, 77,
                75.5, 77, 79, 80, 82.50, 84, 86, 87, 89};
        double[] homayunDo = {48, 53, 55, 56.5, 58, 60, 61.5, 63, 65,
                64, 65, 67, 68, 70, 72, 73.50, 75, 77,
                76, 77, 79, 80, 82, 84, 85.50, 87, 89};
        double[] chahargaheSol = {48, 54, 55, 56.5, 59, 60, 62, 63, 65
                , 63.5, 66, 67, 68.5, 71, 72, 74, 75, 77,
                75.5, 78, 79, 81, 82.5, 85, 86, 87.5, 90};
        double[] MahurDo = {48, 53, 55, 57, 59, 60, 62, 63.5, 65,
                64, 65, 67, 68.5, 70, 72, 74, 75, 77,
                76, 77, 79, 80.50, 83, 84, 86, 87, 89};

        if (kuk == 1 || book == 1 || book == 2 || book == 3) {
            Miditone = shurMiditone;
            if (kuk == 1)
                setTitle(getResources().getString(R.string.shur));
            if (book == 1)
                setTitle("کتاب دستور سنتور");
            if (book == 2)
                setTitle("کتاب شیوه سنتور نوازی");
            if (book == 3)
                setTitle("کتاب گلنار");
            if (mi || book == 1)
                Miditone[0] = 51.5;
        } else if (kuk == 2 || book == 4) {
            if (kuk == 2)
                setTitle(getResources().getString(R.string.esfehan));
            if (book == 4)
                setTitle("کتاب یادگاران(اصفهان)");
            Miditone = esfehanMiditone;
            if (mi)
                Miditone[0] = 51.5;
        } else if (kuk == 3 || book == 8) {
            if (kuk == 3)
                setTitle(getResources().getString(R.string.mahur));
            if (book == 8) setTitle("کتاب سپیده");
            Miditone = mahurMiditone;
            kharakLa.setBackgroundResource(R.drawable.la2);
            if (mi)
                Miditone[0] = 52;
        } else if (kuk == 4 || book == 6) {
            if (kuk == 4)
                setTitle(getResources().getString(R.string.segah));
            if (book == 6)
                setTitle("کتاب هزاردستان(سه گاه)");
            Miditone = segahMiditone;
            if (mi)
                Miditone[0] = 51.5;
        } else if (kuk == 5 || book == 7) {
            if (kuk == 5)
                setTitle(getResources().getString(R.string.chahargah));
            if (book == 7)
                setTitle("کتاب هزاردستان(چهارگاه)");
            Miditone = chahargahMiditone;
            if (mi)
                Miditone[0] = 51.5;
        } else if (kuk == 6 || book == 5) {
            if (kuk == 6)
                setTitle(getResources().getString(R.string.homayun));
            if (book == 5)
                setTitle("کتاب یادگاران(همایون)");
            Miditone = homayunMiditone;
            kharakMi.setBackgroundResource(R.drawable.mii2);
            kharakSi.setBackgroundResource(R.drawable.sii);
            if (mi)
                Miditone[0] = 52;
        } else if (kuk == 7) {
            setTitle(getResources().getString(R.string.shorRe));
            Miditone = shourReMiditone;
            kharakLa.setBackgroundResource(R.drawable.la2);
            if (mi)
                Miditone[0] = 51.5;
        } else if (kuk == 8) {
            setTitle(getResources().getString(R.string.esfehanFa));
            Miditone = esfehanFaMiditone;
            kharakLa.setBackgroundResource(R.drawable.la1);
            if (mi)
                Miditone[0] = 52;
        } else if (kuk == 9) {
            setTitle(getResources().getString(R.string.faMinor));
            Miditone = faMinorMiditone;
            kharakLa.setBackgroundResource(R.drawable.la1);
            if (mi)
                Miditone[0] = 52;
        } else if (kuk == 10) {
            setTitle(getResources().getString(R.string.doMajor));
            Miditone = doMajorMiditone;
            kharakLa.setBackgroundResource(R.drawable.la1);
            if (mi)
                Miditone[0] = 52;
        } else if (kuk == 11) {
            setTitle(getResources().getString(R.string.doMinor));
            Miditone = doMinorMiditone;
            kharakLa.setBackgroundResource(R.drawable.la1);
            if (mi)
                Miditone[0] = 52;
        } else if (kuk == 12) {
            setTitle(getResources().getString(R.string.shurPayver));
            Miditone = shurMiditonePayvar;
        } else if (kuk == 13) {
            setTitle(getResources().getString(R.string.nava));
            Miditone = navaMiditone;
            if (mi)
                Miditone[0] = 51.5;
        } else if (kuk == 14) {
            setTitle(getResources().getString(R.string.HomayunChap));
            Miditone = homayunDo;
            if (mi)
                Miditone[0] = 52;
        } else if (kuk == 15) {
            setTitle(getResources().getString(R.string.MahurChap));
            Miditone = MahurDo;
            if (mi)
                Miditone[0] = 52;
        } else if (kuk == 16) {
            setTitle(getResources().getString(R.string.segahChap));
            Miditone = segahMiKoron;
            if (mi)
                Miditone[0] = 51.5;
        } else if (kuk == 17) {
            setTitle(getResources().getString(R.string.chahargahChap));
            Miditone = chahargaheSol;
            if (mi)
                Miditone[0] = 51.5;
        }

        String pieceTitle = data.getString("title");
        if (pieceTitle != null) {
            setTitle(pieceTitle.length() > 3 ? pieceTitle.substring(3) : pieceTitle);
        }
        String noteName = data.getString("noteName");
        if (noteName != null) {
            setTitle(noteName);
        }
    }

    /** Drops the current string pick so a new kook starts from the santur view. */
    private void clearStringSelection() {
        selectedStringIndex = -1;
        lastTunedIndex = -1;
        setKookPitch = 0;
        kukposition = 0;
        tuning.setChecked(false);
        rd1.setChecked(false);
        rd2.setChecked(false);
        rd3.setChecked(false);
        positonShow.setBackgroundResource(R.drawable.empty);
        wire.setBackgroundResource(R.drawable.santur);
        for (Button button : buttons) {
            button.setEnabled(true);
            styleStringButtonDefault(button);
        }
        pitchView.setKookPitch(0);
        pitchView.setCenterPitch(0);
        pitchView.setCurrentPitch(12);
        pitchLabel.setText(PersianReshape
                .reshape("خرک مورد نظر برای کوک شدن را انتخاب نمایید"));
    }

    private void initPd() throws IOException {
        AudioParameters.init(this);
        int sampleRate = AudioParameters.suggestSampleRate();
        pdService.initAudio(sampleRate, 1, 2, 20.0f);
        start();
        pitchCoordinator.setAudioStarter(() -> {
            if (pdService != null && !pdService.isRunning()) {
                pdService.startAudio();
            }
        });

        dispatcher = new PdUiDispatcher();
        PdBase.setReceiver(dispatcher);
        dispatcher.addListener("pitch", new PdListener.Adapter() {
            @Override
            public void receiveFloat(String source, final float x) {
                if (pressure >= sensitiveLevel && pitchView.getCenterPitch() > 0) {
                    final float pitch = (float) Math.round(x * 1000) / 1000;
                    pitchCoordinator.onPitchInput(
                            () -> {
                                pitchView.setCurrentPitch(pitch);
                                if (tuning.isChecked()) {
                                    for (int i = 0; i < Miditone.length; i++) {
                                        double target = tuningTarget((float) Miditone[i]);
                                        if (pitch >= target - 0.4 && pitch <= target + 0.4) {
                                            autoTuneString(i);
                                            break;
                                        }
                                    }
                                }
                            },
                            () -> pitchView.setCurrentPitch(12));
                }
            }
        });

        dispatcher.addListener("pressure", new PdListener.Adapter() {
            @Override
            public void receiveFloat(String source, final float z) {
                pressure = (int) z;
            }
        });

    }

    private void start() {
        if (!pdService.isRunning()) {
            // Use startAudio without notification to avoid PendingIntent issues on Android S+
            pdService.startAudio();
        }
    }

    private void loadPatch() throws IOException {
        File dir = getFilesDir();
        IoUtils.extractZipResource(getResources().openRawResource(R.raw.temp),
                dir, true);
        File patchFile = new File(dir, "path");
        PdBase.openPatch(patchFile.getAbsolutePath());
    }

    private float displayMidi(float baseNote) {
        return baseNote + santurType + pitchChange;
    }

    private double tuningTarget(float baseNote) {
        return audioMidi(baseNote);
    }

    private float audioMidi(float baseNote) {
        return displayMidi(baseNote)
                + (float) tempeament[(int) (baseNote * 2) % 24] / 100f;
    }

    private void triggerNote(float baseNote) {
        float noteForRef = displayMidi(baseNote);
        float noteForAudio = audioMidi(baseNote);
        float realMidi = midiToRealMidi(noteForAudio);
        pitchCoordinator.invalidatePitchReset();
        pitchView.setKookPitch(setKookPitch);
        pitchView.setCenterPitch(realMidi);
        pitchView.setCurrentPitch(12);
        pitchView.setMidiRef(noteForRef);
        final float midiForPd = realMidi;
        pitchCoordinator.scheduleTrigger(() -> TunerPitchCoordinator.runPdSend(() -> {
            PdBase.sendFloat("midinote", midiForPd);
            PdBase.sendBang("trigger");
        }));
    }

    private void triggerNote2(float baseNote) {
        float noteForRef = displayMidi(baseNote);
        float noteForAudio = audioMidi(baseNote);
        float realMidi = midiToRealMidi(noteForAudio);
        pitchCoordinator.invalidatePitchReset();
        pitchView.setKookPitch(setKookPitch);
        pitchView.setCenterPitch(realMidi);
        pitchView.setMidiRef(noteForRef);
    }

    private String labelForString(int index) {
        String row;
        if (index < 9) {
            row = "سیم های زرد";
        } else if (index < 18) {
            row = "سیم های سفید";
        } else {
            row = "سیم های پشت خرک";
        }
        return "خرک " + STRING_ORDINALS[index % 9] + " " + row;
    }

    private void markNeedsTuneForRow(int row) {
        int start = row * 9;
        for (int i = start; i < start + 9; i++) {
            if (Miditone[i] != MiditoneOld[i] && MiditoneOld[i] != 0) {
                styleStringButtonNeedsTune(buttons[i - start]);
            }
        }
    }

    private void applyRowForIndex(int index) {
        int row = index / 9;
        kukposition = row + 1;
        rd1.setChecked(row == 0);
        rd2.setChecked(row == 1);
        rd3.setChecked(row == 2);
        if (row == 0) {
            positonShow.setBackgroundResource(R.drawable.zard);
        } else if (row == 1) {
            positonShow.setBackgroundResource(R.drawable.sefidd);
        } else {
            positonShow.setBackgroundResource(R.drawable.posht);
        }
    }

    private int buttonOffsetForId(int viewId) {
        if (viewId == R.id.button_1) return 0;
        if (viewId == R.id.button_2) return 1;
        if (viewId == R.id.button_3) return 2;
        if (viewId == R.id.button_4) return 3;
        if (viewId == R.id.button_5) return 4;
        if (viewId == R.id.button_6) return 5;
        if (viewId == R.id.button_7) return 6;
        if (viewId == R.id.button_8) return 7;
        if (viewId == R.id.button_9) return 8;
        return -1;
    }

    private void selectString(int index) {
        selectedStringIndex = index;
        lastTunedIndex = -1;
        int buttonIndex = index % 9;

        for (int i = 0; i < buttons.length; i++) {
            styleStringButtonDefault(buttons[i]);
        }
        if (kukposition > 0) {
            markNeedsTuneForRow(kukposition - 1);
        }

        setKookPitch = index + 1;
        triggerNote((float) Miditone[index]);
        pitchLabel.setText(PersianReshape.reshape(labelForString(index)));
        wire.setBackgroundResource(STRING_DRAWABLES[index]);
        styleStringButtonActive(buttons[buttonIndex]);
        YoYo.with(Techniques.Landing).duration(700).playOn(wire);
    }

    private void autoTuneString(int index) {
        if (index == lastTunedIndex) {
            return;
        }
        lastTunedIndex = index;
        selectedStringIndex = index;
        int buttonIndex = index % 9;
        int row = index / 9;

        for (int i = 0; i < buttons.length; i++) {
            styleStringButtonDefault(buttons[i]);
            buttons[i].setEnabled(true);
        }
        markNeedsTuneForRow(row);
        applyRowForIndex(index);

        setKookPitch = index + 1;
        triggerNote2((float) Miditone[index]);
        pitchLabel.setText(PersianReshape.reshape(labelForString(index)));
        wire.setBackgroundResource(STRING_DRAWABLES[index]);
        styleStringButtonActive(buttons[buttonIndex]);
    }

    private void tuning(int miditone) {
        autoTuneString(miditone);
    }

    private float midiToRealMidi(float midi) {
        double frequens = laFrequens * Math.pow(2, ((midi - 69) / 12));
        float RealMidi = 69 + 12 * (float) Math.log(frequens / 440.0f)
                / (float) Math.log(2.0);
        RealMidi = (float) Math.round(RealMidi * 1000) / 1000;
        return (float) RealMidi;
    }

    @Override
    protected void onPause() {
        super.onPause();
        pitchView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        pitchView.resume();
        for (int j = 0; j < tempeament.length; j++) {
            tempeament[j] = prefs.getInt(j + "Tempeament", 0);
        }
        if (kukposition == 0) {
            wire.setBackgroundResource(R.drawable.santur);
            for (int i = 0; i < buttons.length; i++) {
                buttons[i].setEnabled(true);
                styleStringButtonDefault(buttons[i]);
            }
            pitchView.setCenterPitch(0);
            pitchLabel.setText(PersianReshape
                    .reshape("خرک مورد نظر برای کوک شدن را انتخاب نمایید"));
        }
    }

    @Override
    public void onClick(View v) {




        if (id.tuning != v.getId() && kukposition == 0 && v.getId() != id.zardRB && v.getId() != id.sefidRB && v.getId() != id.poshtRB) {
            YoYo.with(Techniques.Pulse).duration(700).playOn(findViewById(id.zardRB));
            YoYo.with(Techniques.Pulse).duration(700).playOn(findViewById(id.sefidRB));
            YoYo.with(Techniques.Pulse).duration(700).playOn(findViewById(id.poshtRB));
            YoYo.with(Techniques.RubberBand).duration(700).playOn(findViewById(id.pitch_label));
        }
        if (v.getId() == id.tableLayout_main && kukposition != 0 && pitchView.getCenterPitch() == 0) {
            YoYo.with(Techniques.Pulse).duration(700).playOn(findViewById(id.button_1));
            YoYo.with(Techniques.Pulse).duration(700).playOn(findViewById(id.button_2));
            YoYo.with(Techniques.Pulse).duration(700).playOn(findViewById(id.button_3));
            YoYo.with(Techniques.Pulse).duration(700).playOn(findViewById(id.button_4));
            YoYo.with(Techniques.Pulse).duration(700).playOn(findViewById(id.button_5));
            YoYo.with(Techniques.Pulse).duration(700).playOn(findViewById(id.button_6));
            YoYo.with(Techniques.Pulse).duration(700).playOn(findViewById(id.button_7));
            YoYo.with(Techniques.Pulse).duration(700).playOn(findViewById(id.button_8));
            YoYo.with(Techniques.Pulse).duration(700).playOn(findViewById(id.button_9));
            YoYo.with(Techniques.RubberBand).duration(700).playOn(findViewById(id.pitch_label));
        }

        if (id.tuning == v.getId()) {
            tuning.setChecked(!tuning.isChecked());
            lastTunedIndex = -1;
            if (tuning.isChecked()) {
                positonShow.setBackgroundResource(R.drawable.empty);
                Toast.makeText(
                        getApplicationContext(),
                        "توجه : در صورتی که اولین بار از سنتور را در دستگاه "
                                + getTitle()
                                + " کوک می کنید، بهتر است سیم ها را به صورت دستی انتخاب کنید",
                        Toast.LENGTH_LONG).show();
            }


        }
        if (v.getId() != id.tableLayout_main) {
            int buttonOffset = buttonOffsetForId(v.getId());
            if (buttonOffset >= 0 && kukposition > 0) {
                selectString((kukposition - 1) * 9 + buttonOffset);
            }

            if (v.getId() == id.zardRB || v.getId() == id.sefidRB
                    || v.getId() == id.poshtRB) {

                selectedStringIndex = -1;
                lastTunedIndex = -1;
                wire.setBackgroundResource(R.drawable.santur);
                for (int i = 0; i < buttons.length; i++) {
                    buttons[i].setEnabled(true);
                    styleStringButtonDefault(buttons[i]);

                }
                pitchView.setCenterPitch(0);
                pitchLabel.setText(PersianReshape
                        .reshape("خرک مورد نظر برای کوک شدن را انتخاب نمایید"));
                int viewId = v.getId();
                if (viewId == R.id.zardRB) {
                    positonShow.setBackgroundResource(R.drawable.zard);
                    YoYo.with(Techniques.Landing).duration(700).playOn(findViewById(id.layout_positon));
                    kukposition = 1;
                    rd2.setChecked(false);
                    rd3.setChecked(false);
                    for (int i = 0; i < 9; i++) {
                        if (Miditone[i] != MiditoneOld[i] && MiditoneOld[i] != 0)
                            styleStringButtonNeedsTune(buttons[i]);
                    }
                } else if (viewId == R.id.sefidRB) {
                    positonShow.setBackgroundResource(R.drawable.sefidd);
                    YoYo.with(Techniques.Landing).duration(700).playOn(findViewById(id.layout_positon));
                    kukposition = 2;
                    rd1.setChecked(false);
                    rd3.setChecked(false);
                    for (int i = 9; i < 18; i++) {
                        if (Miditone[i] != MiditoneOld[i] && MiditoneOld[i] != 0)
                            styleStringButtonNeedsTune(buttons[i % 9]);
                    }
                } else if (viewId == R.id.poshtRB) {
                    positonShow.setBackgroundResource(R.drawable.posht);
                    YoYo.with(Techniques.Landing).duration(700).playOn(findViewById(id.layout_positon));
                    kukposition = 3;
                    rd1.setChecked(false);
                    rd2.setChecked(false);
                    for (int i = 18; i < 27; i++) {
                        if (Miditone[i] != MiditoneOld[i] && MiditoneOld[i] != 0)
                            styleStringButtonNeedsTune(buttons[i % 9]);
                    }
                }

            } else {
                positonShow.setBackgroundResource(R.drawable.empty);
            }
        }
    }

    private int color(int resId) {
        return ContextCompat.getColor(this, resId);
    }

    private void styleStringButtonDefault(Button button) {
        button.setBackgroundTintList(ColorStateList.valueOf(color(R.color.stringDefault)));
        button.setTextColor(color(R.color.onSurface));
    }

    private void styleStringButtonDisabled(Button button) {
        button.setBackgroundTintList(ColorStateList.valueOf(color(R.color.stringDisabled)));
        button.setTextColor(color(R.color.onSurfaceVariant));
    }

    private void styleStringButtonNeedsTune(Button button) {
        button.setBackgroundTintList(ColorStateList.valueOf(color(R.color.stringNeedsTune)));
        button.setTextColor(color(R.color.black));
    }

    private void styleStringButtonActive(Button button) {
        button.setBackgroundTintList(ColorStateList.valueOf(color(R.color.stringActive)));
        button.setTextColor(color(R.color.white));
    }


}
