package com.PouyaApp.KookYaRSantooR;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.PouyaApp.KookYaRSantooR.R.id;

public class MainMenu extends Activity implements View.OnClickListener {
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_PHONE_STATE };

    Button tunerB, exitB, mailB, kookB, metroB, textB, shareB;


    Toast exitToast;
    SharedPreferences mPrefs;
    final String welcomeScreenShownPref = "welcomeScreenShown";
    final String welcomeScreenShownPrefV17 = "welcomeScreenShownV32";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        setFace();


        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        Boolean welcomeScreenShown = mPrefs.getBoolean(welcomeScreenShownPref,
                false);

        if (!welcomeScreenShown) {

            String title = getResources().getString(R.string.show);

            String payamHead = getResources().getString(R.string.payamHead);
            String payam = getResources().getString(R.string.payam);


            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setTitle(title)
                    .setMessage(payamHead + "\n" + payam)
                    .setPositiveButton(R.string.edame,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss();
                                }
                            }).show();


            SharedPreferences.Editor editor = mPrefs.edit();
            editor.putBoolean(welcomeScreenShownPref, true);
            editor.commit();
        }

        Boolean welcomeScreenShownV13 = mPrefs.getBoolean(welcomeScreenShownPrefV17,
                false);

        if (!welcomeScreenShownV13) {

            String title = getResources().getString(R.string.changeV14);


            String payam = getResources().getString(R.string.changeV14Text);
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setTitle(title)
                    .setMessage(payam)
                    .setPositiveButton(R.string.edame,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss();
                                }
                            }).show();

            SharedPreferences.Editor editor = mPrefs.edit();
            editor.putBoolean(welcomeScreenShownPrefV17, true);
            editor.commit();
        }



    }

    private boolean hasPermissions(String... permissions) {

        if (permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;

    }

    protected void setFace() {


        tunerB = (Button) findViewById(R.id.TunerButton);
        tunerB.setOnClickListener(this);

        exitB = (Button) findViewById(R.id.exitButton);
        exitB.setOnClickListener(this);

        kookB = (Button) findViewById(R.id.button_kookHelp);
        kookB.setOnClickListener(this);


        mailB = (Button) findViewById(R.id.mail);
        mailB.setOnClickListener(this);

        metroB = (Button) findViewById(id.metro);
        metroB.setOnClickListener(this);

        textB = (Button) findViewById(id.text);
        textB.setOnClickListener(this);

        shareB = (Button) findViewById(id.share);
        shareB.setOnClickListener(this);


    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        Intent activity;

        switch (arg0.getId()) {

            case R.id.TunerButton: {
                if(!hasPermissions(PERMISSIONS)){
                    ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
                    if(hasPermissions(PERMISSIONS)){
                        activity = new Intent(MainMenu.this, KookType.class);
                        startActivity(activity);
                    }
                    else Toast.makeText(this, "کوک یار جهت کوک کردن ساز شما نیازمند دسترسی به میکروفن گوشی است", Toast.LENGTH_LONG).show();
                }
                else {
                    activity = new Intent(MainMenu.this, KookType.class);
                    startActivity(activity);
                }

                break;
            }
            case R.id.text: {


                activity = new Intent(MainMenu.this, ContentMenu.class);

                startActivity(activity);
                break;
            }
            case R.id.share: {

                try {
                    PackageManager a = getPackageManager();
                    activity = new Intent(getPackageManager().getLaunchIntentForPackage("com.PouyaApp.NotkadehSantour"));
                    startActivity(activity);
                } catch (Exception e) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                            MainMenu.this);
                    alertDialog.setTitle("دریافت نت کده سنتور");
                    alertDialog.setMessage("برای دریافت رایگان نت کده سنتور دکمه ی زیر را لمس نمایید");
                    alertDialog.setIcon(R.drawable.kookyar);
                    alertDialog.setNegativeButton("دریافت نت کده سنتور",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent("android.intent.action.VIEW");
                                    intent.setData(Uri.parse("http://cafebazaar.ir/app/?id=com.PouyaApp.NotkadehSantour"));
                                    startActivity(intent);
                                }
                            });
                    alertDialog.setPositiveButton("انصراف",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                    alertDialog.show();
                }

                break;
            }


            case R.id.button_kookHelp: {

                activity = new Intent(MainMenu.this, KookHelp.class);


                startActivity(activity);
                break;
            }
            case R.id.mail: {
                String emailAddress[] = {"KookYar@gmail.com"};
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.putExtra(Intent.EXTRA_EMAIL,
                        emailAddress);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT,
                        "کوک یار سنتور");
                emailIntent.setType("plane/text");
                PackageInfo pInfo = null;
                try {
                    pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                } catch (NameNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                String version = pInfo.versionName;
                emailIntent.putExtra(Intent.EXTRA_TEXT,
                        "App Version = " + version + "\nAPI version = "
                                + Build.VERSION.SDK_INT
                                + "\nPhone Model = " + Build.MANUFACTURER + " "
                                + Build.MODEL
                                + "\n-----------------------------------\n");

                startActivity(emailIntent);
                break;
            }

            case R.id.exitButton: {
                backButtonHandler();
                break;
            }
            case R.id.metro: {
                try {
                    PackageManager a = getPackageManager();
                    activity = new Intent(getPackageManager().getLaunchIntentForPackage("com.pouyaApp.metoronome"));
                    startActivity(activity);
                } catch (Exception e) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                            MainMenu.this);
                    alertDialog.setTitle("متاسفانه مترونوم بر روی گوشی شما نصب نیست!");
                    alertDialog.setMessage("برای دریافت رایگان مترونوم از بازار بر روی دکمه زیر کلیک کنید");
                    alertDialog.setIcon(R.drawable.kookyar);
                    alertDialog.setNegativeButton("دریافت مترونوم",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent("android.intent.action.VIEW");
                                    intent.setData(Uri.parse("http://cafebazaar.ir/app/?id=com.pouyaApp.metoronome"));
                                    startActivity(intent);
                                }
                            });
                    alertDialog.setPositiveButton("انصراف",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                    alertDialog.show();
                }

                break;
            }
        }

    }

    @Override
    public void onBackPressed() {

        backButtonHandler();

    }

    public void backButtonHandler() {
        Boolean voit = mPrefs.getBoolean("vote",
                false);
        if (!voit) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                    MainMenu.this);
            alertDialog.setTitle("آیا میخواهید واقعا از کوک یار خارج بشوید ؟!");
            alertDialog.setMessage("اگه تونستین سازتونو با کوک یار کوک کنین، قبل خارج شدن به کوک یار پنج ستاره بدهید :)");
            alertDialog.setIcon(R.drawable.kookyar);
            alertDialog.setPositiveButton("بله",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
            alertDialog.setNegativeButton("خیر",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            alertDialog.setNeutralButton("ستاره به کوک یار",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent("android.intent.action.EDIT");
                            intent.setData(Uri.parse("bazaar://details?id=com.PouyaApp.KookYaRSantooR"));
                            startActivity(intent);
                            SharedPreferences.Editor editor = mPrefs.edit();
                            editor.putBoolean("vote", true);
                            editor.commit();
                        }
                    });
            alertDialog.show();
        } else finish();
    }

}
