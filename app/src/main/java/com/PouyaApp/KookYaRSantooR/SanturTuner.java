package com.PouyaApp.KookYaRSantooR;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import androidx.core.content.ContextCompat;
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
import android.widget.TableLayout;
import android.widget.TableRow;
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

public class SanturTuner extends AppCompatActivity implements OnClickListener {
    private volatile boolean threadRuned = false;
    private volatile boolean threadRuned2 = false;
    private static final String TAG = "KookYaR";
    public static boolean mi;
    final private int highSensitive = 55;
    final private int midSensitive = 50;
    final private int lowSensitive = 45;
    private Bundle data;
    private final Handler pitchHandler = new Handler(Looper.getMainLooper());
    private Runnable pitchResetRunnable;
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
    private TableLayout tabelLayout;
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
        // TODO Auto-generated method stub
        boolean changed = false;
        for (int i = 0; i < MiditoneOld.length; i++) {
            if ((Miditone[i] != MiditoneOld[i])) {
                changed = true;

            }

        }


        if (changed) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                    SanturTuner.this);
            alertDialog.setTitle("ذخیره کوک انتخاب شده به عنوان کوک فعلی سنتور");
            alertDialog
                    .setMessage("با انتخاب کوک انتخاب شده به عنوان کوک فعلی سنتور، در صورتی که کوک دیگری را انتخاب نمایید، سیم هایی که نیازمند تغییر کوک می باشند به رنگ زرد نمایش داده می شوند");
            alertDialog.setIcon(android.R.drawable.ic_menu_save);
            alertDialog.setPositiveButton("بله",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences.Editor editor = prefs.edit();
                            for (int j = 0; j < Miditone.length; j++) {
                                editor.putFloat(j + "", (float) Miditone[j]);
                            }

                            editor.apply();
                            SanturTuner.super.onBackPressed();
                        }
                    });
            alertDialog.setNegativeButton("خیر",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            SanturTuner.super.onBackPressed();
                        }
                    });
            alertDialog.show();

        } else super.onBackPressed();


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
        // TODO Auto-generated method stub
        int itemId = item.getItemId();
        if (itemId == R.id.help_tuner) {
            Intent i = new Intent(SanturTuner.this, TunerHelp.class);
            startActivity(i);
        } else if (itemId == R.id.bazgasht) {
            onBackPressed();
        } else if (itemId == R.id.settings_menu) {
            Intent j = new Intent(SanturTuner.this, Prefs.class);
            startActivity(j);
        } else if (itemId == id.change_tempe) {
            Intent k = new Intent(SanturTuner.this, CustomeTempeament.class);
            startActivity(k);
        }
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuner);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        data = getIntent().getExtras();
        kuk = data.getInt("kuk");
        book = data.getInt("book");
        String title = data.getString("title");
        if (title != null)
            setTitle(title.substring(3));
        Miditone = data.getDoubleArray("custom");
        prefsListnner();

        initGui();
        bindService(new Intent(this, CustomPdService.class), pdConnection,
                BIND_AUTO_CREATE);
        for (int i = 0; i < 9; i++) {

            buttons[i].setBackgroundColor(Color.GRAY);
        }
    }

    private void prefsListnner() {
        // TODO Auto-generated method stub
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(this);

        listener = new OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs,
                                                  String key) {
                Boolean change = prefs.getBoolean("mi", false);

                laFrequens = Integer.parseInt(prefs.getString("ref", "440"));
                pitchView.setAFrequnse(laFrequens);

                if (prefs.getString("type", "سل کوک").compareTo("سل کوک") == 0) {
                    santurType = 0;
                    pitchView.setSanturType("سل کوک");
                } else if (prefs.getString("type", "سل کوک")
                        .compareTo("لا کوک") == 0) {
                    santurType = 2;
                    pitchView.setSanturType("لا کوک");
                } else if (prefs.getString("type", "سل کوک")
                        .compareTo("سی کوک") == 0) {
                    santurType = 4;
                    pitchView.setSanturType("سی کوک");
                } else if (prefs.getString("type", "سل کوک")
                        .compareTo("دو کوک") == 0) {
                    santurType = 5;
                    pitchView.setSanturType("سی کوک");
                } else if (prefs.getString("type", "سل کوک")
                        .compareTo("می کوک") == 0) {
                    santurType = -2;
                    pitchView.setSanturType("می کوک");
                } else if (prefs.getString("type", "سل کوک")
                        .compareTo("فا کوک") == 0) {
                    santurType = -1;
                    pitchView.setSanturType("فا کوک");
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
                    sensitiveLevel = lowSensitive;

                } else if (prefs.getString("sensitive", "2").compareTo("1") == 0) {
                    sensitiveLevel = highSensitive;
                } else {
                    sensitiveLevel = midSensitive;
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
        pitchHandler.removeCallbacksAndMessages(null);
        unbindService(pdConnection);
    }

    private void initGui() {
        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        for (int j = 0; j < tempeament.length; j++) {
            tempeament[j] = prefs.getInt(j + "Tempeament", 0);
        }

        TableRow tuning_ = (TableRow) findViewById(id.tuning);
        tuning_.setOnClickListener(this);

        toolbar = (Toolbar) findViewById(id.app_bar);
        setSupportActionBar(toolbar);

        tabelLayout = (TableLayout) findViewById(id.tableLayout_main);
        tabelLayout.setOnClickListener(this);
        //
        double[] shurMiditone = {48, 53, 55, 56.5, 58, 60, 61.5, 63, 65, 63.5,
                65, 67, 68.5, 70, 72, 74, 75, 77, 75.5, 77, 79, 80, 82, 84, 86,
                87, 89};
        double[] navaMiditone = {48, 53, 55, 56.5, 58, 60, 62, 63, 65, 63.5,
                65, 67, 68.5, 70, 72, 73.5, 75, 77, 75.5, 77, 79, 80, 82, 84, 85,
                87, 89};

//         double[] shurMiditone = { 48, 52.98, 55.02, 56.52, 57.96, 60, 61.48,
//         62.94, 65.98, 63.54,
//         64.98, 67.02, 68.52, 69.96, 72, 74.04, 74.94, 76.98, 75.54, 76.98,
//         79.02, 79.94, 81.96, 84, 86.04,
//         86.94, 88.98 };
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
            buttons[i].setBackgroundColor(Color.GRAY);
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
        if (mi)
            kharakDo.setBackgroundResource(R.drawable.mi);
        else
            kharakDo.setBackgroundResource(R.drawable.doo);
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
            pitchView.setKookType(0);

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

        String noteName = data.getString("noteName");
        if (noteName != null){
            this.setTitle(noteName);
        }



        if (prefs.getString("type", "سل کوک").compareTo("سل کوک") == 0) {
            santurType = 0;
            pitchView.setSanturType("سل کوک");
        } else if (prefs.getString("type", "سل کوک").compareTo("لا کوک") == 0) {
            santurType = 2;
            pitchView.setSanturType("لا کوک");
        } else if (prefs.getString("type", "سل کوک").compareTo("سی کوک") == 0) {
            santurType = 4;
            pitchView.setSanturType("سی کوک");
        } else if (prefs.getString("type", "سل کوک").compareTo("دو کوک") == 0) {
            santurType = 5;
            pitchView.setSanturType("سی کوک");
        } else if (prefs.getString("type", "سل کوک").compareTo("می کوک") == 0) {
            santurType = -2;
            pitchView.setSanturType("می کوک");
        } else if (prefs.getString("type", "سل کوک")
                .compareTo("فا کوک") == 0) {
            santurType = -1;
            pitchView.setSanturType("فا کوک");
        }

        if (prefs.getString("sensitive", "2").compareTo("3") == 0) {
            sensitiveLevel = lowSensitive;

        } else if (prefs.getString("sensitive", "2").compareTo("1") == 0) {
            sensitiveLevel = highSensitive;
        } else {
            sensitiveLevel = midSensitive;
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

    }

    private void initPd() throws IOException {
        AudioParameters.init(this);
        int sampleRate = AudioParameters.suggestSampleRate();
        pdService.initAudio(sampleRate, 1, 2, 20.0f);
        start();

        dispatcher = new PdUiDispatcher();
        PdBase.setReceiver(dispatcher);
        dispatcher.addListener("pitch", new PdListener.Adapter() {

            @Override
            public void receiveFloat(String source, final float x) {
//                Toast.makeText(pdService, ""+pitchView.getCurrentPitch()+","+pressure+","+pitchView.getCenterPitch(), Toast.LENGTH_SHORT).show();

                if (pressure >= sensitiveLevel) {
                    // Cancel any pending pitch reset
                    if (pitchResetRunnable != null) {
                        pitchHandler.removeCallbacks(pitchResetRunnable);
                    }
                    float pitch = (float) Math.round(x * 1000) / 1000;
                    pitchView.setCurrentPitch(pitch);
                    if (tuning.isChecked()) {

                        for (int i = 0; i < Miditone.length; i++) {
                            if (((Miditone[i] + santurType+((float) (tempeament[(int) (Miditone[i] * 2) % 24])/100)) - 0.4) <= pitch
                                    && pitch <= ((Miditone[i] + pitchChange+santurType+((float) (tempeament[(int) (Miditone[i] * 2) % 24])/100)) + 0.4)) {
                                tuning(i);
                            }
                        }
                    }

                    // Schedule pitch reset after silence
                    pitchResetRunnable = () -> pitchView.setCurrentPitch(12);
                    pitchHandler.postDelayed(pitchResetRunnable, 2500);
                }
//                else {
//                    if (t == null) {
//                        t = new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//
//                                try {
//                                    Thread.sleep(300);
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
//                                pitchView.setCurrentPitch(12);
//                            }
//                        });
//                        t.start();
//                    }
//                    if (!t.isAlive() && pitchView.getCenterPitch() != 12) {
//                        t = new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//
//                                try {
//                                    Thread.sleep(200);
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
//                                pitchView.setCurrentPitch(12);
//                            }
//                        });
//                        t.start();
//                    }
//
//
//                }
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

    private void triggerNote(float triggeredNote) {

        pitchView.setKookPitch(setKookPitch);
        float realMidi = midiToRealMidi((triggeredNote + (float) (tempeament[(int) (triggeredNote * 2) % 24])/100 + santurType+pitchChange));
        pitchView.setCenterPitch(realMidi);
        pitchView.setMidiRef((triggeredNote + santurType+pitchChange));
        PdBase.sendFloat("midinote", realMidi);
        PdBase.sendBang("trigger");

    }

    private void triggerNote2(float triggeredNote) {

        pitchView.setKookPitch(setKookPitch);
        float realMidi = midiToRealMidi((triggeredNote + (float) (tempeament[(int) (triggeredNote * 2) % 24])/100 + santurType+pitchChange));
        pitchView.setCenterPitch(realMidi);
        pitchView.setMidiRef((triggeredNote + santurType+pitchChange));
        PdBase.sendFloat("midinote", realMidi);

    }

    private void tuning(int miditone) {

        for (int i = 0; i < buttons.length; i++) {

            buttons[i].setBackgroundColor(Color.WHITE);
            buttons[i].setEnabled(true);
        }

        if (miditone <= 8) {
            for (int i = 0; i < 9; i++) {
                if (Miditone[i] != MiditoneOld[i] && MiditoneOld[i] != 0)
                    buttons[i].setBackgroundColor(Color.YELLOW);
            }
            rd1.setChecked(true);
            rd2.setChecked(false);
            rd3.setChecked(false);
            kukposition = 1;
            if (miditone == 0) {
                setKookPitch = 1;
                triggerNote2((float) Miditone[0]);
                pitchLabel.setText(PersianReshape
                        .reshape("خرک اول سیم های زرد"));
                wire.setBackgroundResource(R.drawable.santur_1);
                buttons[0].setBackgroundColor(getResources().getColor(R.color.blue));
            } else if (miditone == 1) {
                setKookPitch = 2;
                triggerNote2((float) Miditone[1]);
                pitchLabel.setText(PersianReshape
                        .reshape("خرک دوم سیم های زرد"));
                wire.setBackgroundResource(R.drawable.santur_2);
                buttons[1].setBackgroundColor(getResources().getColor(R.color.blue));
            } else if (miditone == 2) {
                setKookPitch = 3;
                triggerNote2((float) Miditone[2]);
                pitchLabel.setText(PersianReshape
                        .reshape("خرک سوم سیم های زرد"));
                wire.setBackgroundResource(R.drawable.santur_3);
                buttons[2].setBackgroundColor(getResources().getColor(R.color.blue));
            } else if (miditone == 3) {
                setKookPitch = 4;
                triggerNote2((float) Miditone[3]);
                pitchLabel.setText(PersianReshape
                        .reshape("خرک چهارم سیم های زرد"));
                wire.setBackgroundResource(R.drawable.santur_4);
                buttons[3].setBackgroundColor(getResources().getColor(R.color.blue));
            } else if (miditone == 4) {
                setKookPitch = 5;
                triggerNote2((float) Miditone[4]);
                pitchLabel.setText(PersianReshape
                        .reshape("خرک پنجم سیم های زرد"));
                wire.setBackgroundResource(R.drawable.santur_5);
                buttons[4].setBackgroundColor(getResources().getColor(R.color.blue));
            } else if (miditone == 5) {
                setKookPitch = 6;
                triggerNote2((float) Miditone[5]);
                pitchLabel.setText(PersianReshape
                        .reshape("خرک ششم سیم های زرد"));
                wire.setBackgroundResource(R.drawable.santur_6);
                buttons[5].setBackgroundColor(getResources().getColor(R.color.blue));
            } else if (miditone == 6) {
                setKookPitch = 7;
                triggerNote2((float) Miditone[6]);
                pitchLabel.setText(PersianReshape
                        .reshape("خرک هفتم سیم های زرد"));
                wire.setBackgroundResource(R.drawable.santur_7);
                buttons[6].setBackgroundColor(getResources().getColor(R.color.blue));
            } else if (miditone == 7) {
                setKookPitch = 8;
                triggerNote2((float) Miditone[7]);
                pitchLabel.setText(PersianReshape
                        .reshape("خرک هشتم سیم های زرد"));
                wire.setBackgroundResource(R.drawable.santur_8);
                buttons[7].setBackgroundColor(getResources().getColor(R.color.blue));
            } else if (miditone == 8) {
                setKookPitch = 9;
                triggerNote2((float) Miditone[8]);
                pitchLabel.setText(PersianReshape
                        .reshape("خرک نهم سیم های زرد"));
                wire.setBackgroundResource(R.drawable.santur_9);
                buttons[8].setBackgroundColor(getResources().getColor(R.color.blue));
            }
        } else if (miditone <= 17 && miditone >= 9) {
            for (int i = 9; i < 18; i++) {
                if (Miditone[i] != MiditoneOld[i] && MiditoneOld[i] != 0)
                    buttons[i % 9].setBackgroundColor(Color.YELLOW);
            }
            rd1.setChecked(false);
            rd2.setChecked(true);
            rd3.setChecked(false);
            kukposition = 2;
            if (miditone == 9) {
                setKookPitch = 10;
                triggerNote2((float) Miditone[9]);
                pitchLabel.setText(PersianReshape
                        .reshape("خرک اول سیم های سفید"));
                wire.setBackgroundResource(R.drawable.santur_10);
                buttons[0].setBackgroundColor(getResources().getColor(R.color.blue));
            } else if (miditone == 10) {
                setKookPitch = 11;
                triggerNote2((float) Miditone[10]);
                pitchLabel.setText(PersianReshape
                        .reshape("خرک دوم سیم های سفید"));
                wire.setBackgroundResource(R.drawable.santur_11);
                buttons[1].setBackgroundColor(getResources().getColor(R.color.blue));
            } else if (miditone == 11) {
                setKookPitch = 12;
                triggerNote2((float) Miditone[11]);
                pitchLabel.setText(PersianReshape
                        .reshape("خرک سوم سیم های سفید"));
                wire.setBackgroundResource(R.drawable.santur_12);
                buttons[2].setBackgroundColor(getResources().getColor(R.color.blue));
            } else if (miditone == 12) {
                setKookPitch = 13;
                triggerNote2((float) Miditone[12]);
                pitchLabel.setText(PersianReshape
                        .reshape("خرک چهارم سیم های سفید"));
                wire.setBackgroundResource(R.drawable.santur_13);
                buttons[3].setBackgroundColor(getResources().getColor(R.color.blue));
            } else if (miditone == 13) {
                setKookPitch = 14;
                triggerNote2((float) Miditone[13]);
                pitchLabel.setText(PersianReshape
                        .reshape("خرک پنجم سیم های سفید"));
                wire.setBackgroundResource(R.drawable.santur_14);
                buttons[4].setBackgroundColor(getResources().getColor(R.color.blue));
            } else if (miditone == 14) {
                setKookPitch = 15;
                triggerNote2((float) Miditone[14]);
                pitchLabel.setText(PersianReshape
                        .reshape("خرک ششم سیم های سفید"));
                wire.setBackgroundResource(R.drawable.santur_15);
                buttons[5].setBackgroundColor(getResources().getColor(R.color.blue));
            } else if (miditone == 15) {
                setKookPitch = 16;
                triggerNote2((float) Miditone[15]);
                pitchLabel.setText(PersianReshape
                        .reshape("خرک هفتم سیم های سفید"));
                wire.setBackgroundResource(R.drawable.santur_16);
                buttons[6].setBackgroundColor(getResources().getColor(R.color.blue));
            } else if (miditone == 16) {
                setKookPitch = 17;
                triggerNote2((float) Miditone[16]);
                pitchLabel.setText(PersianReshape
                        .reshape("خرک هشتم سیم های سفید"));
                wire.setBackgroundResource(R.drawable.santur_17);
                buttons[7].setBackgroundColor(getResources().getColor(R.color.blue));
            } else if (miditone == 17) {
                setKookPitch = 18;
                triggerNote2((float) Miditone[17]);
                pitchLabel.setText(PersianReshape
                        .reshape("خرک نهم سیم های سفید"));
                wire.setBackgroundResource(R.drawable.santur_18);
                buttons[8].setBackgroundColor(getResources().getColor(R.color.blue));
            }
        } else {
            for (int i = 18; i < 27; i++) {
                if (Miditone[i] != MiditoneOld[i] && MiditoneOld[i] != 0)
                    buttons[i % 9].setBackgroundColor(Color.YELLOW);
            }
            rd1.setChecked(false);
            rd2.setChecked(false);
            rd3.setChecked(true);
            kukposition = 3;
            if (miditone == 18) {
                setKookPitch = 19;
                triggerNote2((float) Miditone[18]);
                pitchLabel.setText(PersianReshape
                        .reshape("خرک اول سیم های پشت خرک"));
                wire.setBackgroundResource(R.drawable.santur_19);
                buttons[0].setBackgroundColor(getResources().getColor(R.color.blue));
            } else if (miditone == 19) {
                setKookPitch = 20;
                triggerNote2((float) Miditone[19]);
                pitchLabel.setText(PersianReshape
                        .reshape("خرک دوم سیم های پشت خرک"));
                wire.setBackgroundResource(R.drawable.santur_20);
                buttons[1].setBackgroundColor(getResources().getColor(R.color.blue));
            } else if (miditone == 20) {
                setKookPitch = 21;
                triggerNote2((float) Miditone[20]);
                pitchLabel.setText(PersianReshape
                        .reshape("خرک سوم سیم های پشت خرک"));
                wire.setBackgroundResource(R.drawable.santur_21);
                buttons[2].setBackgroundColor(getResources().getColor(R.color.blue));
            } else if (miditone == 21) {
                setKookPitch = 22;
                triggerNote2((float) Miditone[21]);
                pitchLabel.setText(PersianReshape
                        .reshape("خرک چهارم سیم های پشت خرک"));
                wire.setBackgroundResource(R.drawable.santur_22);
                buttons[3].setBackgroundColor(getResources().getColor(R.color.blue));
            } else if (miditone == 22) {
                setKookPitch = 23;
                triggerNote2((float) Miditone[22]);
                pitchLabel.setText(PersianReshape
                        .reshape("خرک پنجم سیم های پشت خرک"));
                wire.setBackgroundResource(R.drawable.santur_23);
                buttons[4].setBackgroundColor(getResources().getColor(R.color.blue));
            } else if (miditone == 23) {
                setKookPitch = 24;
                triggerNote2((float) Miditone[23]);
                pitchLabel.setText(PersianReshape
                        .reshape("خرک ششم سیم های پشت خرک"));
                wire.setBackgroundResource(R.drawable.santur_24);
                buttons[5].setBackgroundColor(getResources().getColor(R.color.blue));
            } else if (miditone == 24) {
                setKookPitch = 25;
                triggerNote2((float) Miditone[24]);
                pitchLabel.setText(PersianReshape
                        .reshape("خرک هفتم سیم های پشت خرک"));
                wire.setBackgroundResource(R.drawable.santur_25);
                buttons[6].setBackgroundColor(getResources().getColor(R.color.blue));
            } else if (miditone == 25) {
                setKookPitch = 26;
                triggerNote2((float) Miditone[25]);
                pitchLabel.setText(PersianReshape
                        .reshape("خرک هشتم سیم های پشت خرک"));
                wire.setBackgroundResource(R.drawable.santur_26);
                buttons[7].setBackgroundColor(getResources().getColor(R.color.blue));
            } else if (miditone == 26) {
                setKookPitch = 27;
                triggerNote2((float) Miditone[26]);
                pitchLabel.setText(PersianReshape
                        .reshape("خرک نهم سیم های پشت خرک"));
                wire.setBackgroundResource(R.drawable.santur_27);
                buttons[8].setBackgroundColor(getResources().getColor(R.color.blue));
            }
        }

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
        wire.setBackgroundResource(R.drawable.santur);
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setEnabled(true);
            buttons[i].setBackgroundColor(Color.WHITE);

        }
        pitchView.setCenterPitch(0);
        pitchLabel.setText(PersianReshape
                .reshape("خرک مورد نظر برای کوک شدن را انتخاب نمایید"));


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
            for (int i = 0; i < buttons.length; i++) {
                buttons[i].setBackgroundColor(Color.WHITE);
            }

            if (kukposition == 1) {


                for (int i = 0; i < 9; i++) {
                    if (Miditone[i] != MiditoneOld[i] && MiditoneOld[i] != 0)
                        buttons[i].setBackgroundColor(Color.YELLOW);
                }

                int viewId = v.getId();
                if (viewId == R.id.button_1) {
                    setKookPitch = 1;
                    triggerNote((float) Miditone[0]);
                    pitchLabel.setText(PersianReshape
                            .reshape("خرک اول سیم های زرد"));
                    wire.setBackgroundResource(R.drawable.santur_1);
                    buttons[0].setBackgroundColor(getResources().getColor(R.color.blue));
                } else if (viewId == R.id.button_2) {
                    setKookPitch = 2;
                    triggerNote((float) Miditone[1]);
                    pitchLabel.setText(PersianReshape
                            .reshape("خرک دوم سیم های زرد"));
                    wire.setBackgroundResource(R.drawable.santur_2);
                    buttons[1].setBackgroundColor(getResources().getColor(R.color.blue));
                } else if (viewId == R.id.button_3) {
                    setKookPitch = 3;
                    triggerNote((float) Miditone[2]);
                    pitchLabel.setText(PersianReshape
                            .reshape("خرک سوم سیم های زرد"));
                    wire.setBackgroundResource(R.drawable.santur_3);
                    buttons[2].setBackgroundColor(getResources().getColor(R.color.blue));
                } else if (viewId == R.id.button_4) {
                    setKookPitch = 4;
                    triggerNote((float) Miditone[3]);
                    pitchLabel.setText(PersianReshape
                            .reshape("خرک چهارم سیم های زرد"));
                    wire.setBackgroundResource(R.drawable.santur_4);
                    buttons[3].setBackgroundColor(getResources().getColor(R.color.blue));
                } else if (viewId == R.id.button_5) {
                    setKookPitch = 5;
                    triggerNote((float) Miditone[4]);
                    pitchLabel.setText(PersianReshape
                            .reshape("خرک پنجم سیم های زرد"));
                    wire.setBackgroundResource(R.drawable.santur_5);
                    buttons[4].setBackgroundColor(getResources().getColor(R.color.blue));
                } else if (viewId == R.id.button_6) {
                    setKookPitch = 6;
                    triggerNote((float) Miditone[5]);
                    pitchLabel.setText(PersianReshape
                            .reshape("خرک ششم سیم های زرد"));
                    wire.setBackgroundResource(R.drawable.santur_6);
                    buttons[5].setBackgroundColor(getResources().getColor(R.color.blue));
                } else if (viewId == R.id.button_7) {
                    setKookPitch = 7;
                    triggerNote((float) Miditone[6]);
                    pitchLabel.setText(PersianReshape
                            .reshape("خرک هفتم سیم های زرد"));
                    wire.setBackgroundResource(R.drawable.santur_7);
                    buttons[6].setBackgroundColor(getResources().getColor(R.color.blue));
                } else if (viewId == R.id.button_8) {
                    setKookPitch = 8;
                    triggerNote((float) Miditone[7]);
                    pitchLabel.setText(PersianReshape
                            .reshape("خرک هشتم سیم های زرد"));
                    wire.setBackgroundResource(R.drawable.santur_8);
                    buttons[7].setBackgroundColor(getResources().getColor(R.color.blue));
                } else if (viewId == R.id.button_9) {
                    setKookPitch = 9;
                    triggerNote((float) Miditone[8]);
                    pitchLabel.setText(PersianReshape
                            .reshape("خرک نهم سیم های زرد"));
                    wire.setBackgroundResource(R.drawable.santur_9);
                    buttons[8].setBackgroundColor(getResources().getColor(R.color.blue));
                }
            } else if (kukposition == 2) {

                for (int i = 9; i < 18; i++) {
                    if (Miditone[i] != MiditoneOld[i] && MiditoneOld[i] != 0)
                        buttons[(i - 9)].setBackgroundColor(Color.YELLOW);
                }

                int viewId = v.getId();
                if (viewId == R.id.button_1) {
                    setKookPitch = 10;
                    triggerNote((float) Miditone[9]);
                    pitchLabel.setText(PersianReshape
                            .reshape("خرک اول سیم های سفید"));
                    wire.setBackgroundResource(R.drawable.santur_10);
                    buttons[0].setBackgroundColor(getResources().getColor(R.color.blue));
                } else if (viewId == R.id.button_2) {
                    setKookPitch = 11;
                    triggerNote((float) Miditone[10]);
                    pitchLabel.setText(PersianReshape
                            .reshape("خرک دوم سیم های سفید"));
                    wire.setBackgroundResource(R.drawable.santur_11);
                    buttons[1].setBackgroundColor(getResources().getColor(R.color.blue));
                } else if (viewId == R.id.button_3) {
                    setKookPitch = 12;
                    triggerNote((float) Miditone[11]);
                    pitchLabel.setText(PersianReshape
                            .reshape("خرک سوم سیم های سفید"));
                    wire.setBackgroundResource(R.drawable.santur_12);
                    buttons[2].setBackgroundColor(getResources().getColor(R.color.blue));
                } else if (viewId == R.id.button_4) {
                    setKookPitch = 13;
                    triggerNote((float) Miditone[12]);
                    pitchLabel.setText(PersianReshape
                            .reshape("خرک چهارم سیم های سفید"));
                    wire.setBackgroundResource(R.drawable.santur_13);
                    buttons[3].setBackgroundColor(getResources().getColor(R.color.blue));
                } else if (viewId == R.id.button_5) {
                    setKookPitch = 14;
                    triggerNote((float) Miditone[13]);
                    pitchLabel.setText(PersianReshape
                            .reshape("خرک پنجم سیم های سفید"));
                    wire.setBackgroundResource(R.drawable.santur_14);
                    buttons[4].setBackgroundColor(getResources().getColor(R.color.blue));
                } else if (viewId == R.id.button_6) {
                    setKookPitch = 15;
                    triggerNote((float) Miditone[14]);
                    pitchLabel.setText(PersianReshape
                            .reshape("خرک ششم سیم های سفید"));
                    wire.setBackgroundResource(R.drawable.santur_15);
                    buttons[5].setBackgroundColor(getResources().getColor(R.color.blue));
                } else if (viewId == R.id.button_7) {
                    setKookPitch = 16;
                    triggerNote((float) Miditone[15]);
                    pitchLabel.setText(PersianReshape
                            .reshape("خرک هفتم سیم های سفید"));
                    wire.setBackgroundResource(R.drawable.santur_16);
                    buttons[6].setBackgroundColor(getResources().getColor(R.color.blue));
                } else if (viewId == R.id.button_8) {
                    setKookPitch = 17;
                    triggerNote((float) Miditone[16]);
                    pitchLabel.setText(PersianReshape
                            .reshape("خرک هشتم سیم های سفید"));
                    wire.setBackgroundResource(R.drawable.santur_17);
                    buttons[7].setBackgroundColor(getResources().getColor(R.color.blue));
                } else if (viewId == R.id.button_9) {
                    setKookPitch = 18;
                    triggerNote((float) Miditone[17]);
                    pitchLabel.setText(PersianReshape
                            .reshape("خرک نهم سیم های سفید"));
                    wire.setBackgroundResource(R.drawable.santur_18);
                    buttons[8].setBackgroundColor(getResources().getColor(R.color.blue));
                }
            } else if (kukposition == 3) {

                for (int i = 18; i < 27; i++) {
                    if (Miditone[i] != MiditoneOld[i] && MiditoneOld[i] != 0)
                        buttons[(i - 18)].setBackgroundColor(Color.YELLOW);
                }
                int viewId = v.getId();
                if (viewId == R.id.button_1) {
                    setKookPitch = 19;
                    triggerNote((float) Miditone[18]);
                    pitchLabel.setText(PersianReshape
                            .reshape("خرک اول سیم های پشت خرک"));
                    wire.setBackgroundResource(R.drawable.santur_19);
                    buttons[0].setBackgroundColor(getResources().getColor(R.color.blue));
                } else if (viewId == R.id.button_2) {
                    setKookPitch = 20;
                    triggerNote((float) Miditone[19]);
                    pitchLabel.setText(PersianReshape
                            .reshape("خرک دوم سیم های پشت خرک"));
                    wire.setBackgroundResource(R.drawable.santur_20);
                    buttons[1].setBackgroundColor(getResources().getColor(R.color.blue));
                } else if (viewId == R.id.button_3) {
                    setKookPitch = 21;
                    triggerNote((float) Miditone[20]);
                    pitchLabel.setText(PersianReshape
                            .reshape("خرک سوم سیم های پشت خرک"));
                    wire.setBackgroundResource(R.drawable.santur_21);
                    buttons[2].setBackgroundColor(getResources().getColor(R.color.blue));
                } else if (viewId == R.id.button_4) {
                    setKookPitch = 22;
                    triggerNote((float) Miditone[21]);
                    pitchLabel.setText(PersianReshape
                            .reshape("خرک چهارم سیم های پشت خرک"));
                    wire.setBackgroundResource(R.drawable.santur_22);
                    buttons[3].setBackgroundColor(getResources().getColor(R.color.blue));
                } else if (viewId == R.id.button_5) {
                    setKookPitch = 23;
                    triggerNote((float) Miditone[22]);
                    pitchLabel.setText(PersianReshape
                            .reshape("خرک پنجم سیم های پشت خرک"));
                    wire.setBackgroundResource(R.drawable.santur_23);
                    buttons[4].setBackgroundColor(getResources().getColor(R.color.blue));
                } else if (viewId == R.id.button_6) {
                    setKookPitch = 24;
                    triggerNote((float) Miditone[23]);
                    pitchLabel.setText(PersianReshape
                            .reshape("خرک ششم سیم های پشت خرک"));
                    wire.setBackgroundResource(R.drawable.santur_24);
                    buttons[5].setBackgroundColor(getResources().getColor(R.color.blue));
                } else if (viewId == R.id.button_7) {
                    setKookPitch = 25;
                    triggerNote((float) Miditone[24]);
                    pitchLabel.setText(PersianReshape
                            .reshape("خرک هفتم سیم های پشت خرک"));
                    wire.setBackgroundResource(R.drawable.santur_25);
                    buttons[6].setBackgroundColor(getResources().getColor(R.color.blue));
                } else if (viewId == R.id.button_8) {
                    setKookPitch = 26;
                    triggerNote((float) Miditone[25]);
                    pitchLabel.setText(PersianReshape
                            .reshape("خرک هشتم سیم های پشت خرک"));
                    wire.setBackgroundResource(R.drawable.santur_26);
                    buttons[7].setBackgroundColor(getResources().getColor(R.color.blue));
                } else if (viewId == R.id.button_9) {
                    setKookPitch = 27;
                    triggerNote((float) Miditone[26]);
                    pitchLabel.setText(PersianReshape
                            .reshape("خرک نهم سیم های پشت خرک"));
                    wire.setBackgroundResource(R.drawable.santur_27);
                    buttons[8].setBackgroundColor(getResources().getColor(R.color.blue));
                }
            }

            if (v.getId() == id.zardRB || v.getId() == id.sefidRB
                    || v.getId() == id.poshtRB) {

                wire.setBackgroundResource(R.drawable.santur);
                for (int i = 0; i < buttons.length; i++) {
                    buttons[i].setEnabled(true);
                    buttons[i].setBackgroundColor(Color.WHITE);

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
                            buttons[i].setBackgroundColor(Color.YELLOW);
                    }
                } else if (viewId == R.id.sefidRB) {
                    positonShow.setBackgroundResource(R.drawable.sefidd);
                    YoYo.with(Techniques.Landing).duration(700).playOn(findViewById(id.layout_positon));
                    kukposition = 2;
                    rd1.setChecked(false);
                    rd3.setChecked(false);
                    for (int i = 9; i < 18; i++) {
                        if (Miditone[i] != MiditoneOld[i] && MiditoneOld[i] != 0)
                            buttons[i % 9].setBackgroundColor(Color.YELLOW);
                    }
                } else if (viewId == R.id.poshtRB) {
                    positonShow.setBackgroundResource(R.drawable.posht);
                    YoYo.with(Techniques.Landing).duration(700).playOn(findViewById(id.layout_positon));
                    kukposition = 3;
                    rd1.setChecked(false);
                    rd2.setChecked(false);
                    for (int i = 18; i < 27; i++) {
                        if (Miditone[i] != MiditoneOld[i] && MiditoneOld[i] != 0)
                            buttons[i % 9].setBackgroundColor(Color.YELLOW);
                    }
                }

            } else {
                positonShow.setBackgroundResource(R.drawable.empty);
            }
        }
    }


}
