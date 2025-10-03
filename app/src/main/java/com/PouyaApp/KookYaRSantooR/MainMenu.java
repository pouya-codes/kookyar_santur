package com.PouyaApp.KookYaRSantooR;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
import androidx.core.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.PouyaApp.KookYaRSantooR.R.id;

public class MainMenu extends Activity implements View.OnClickListener {
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_PHONE_STATE };

    Button tunerB, exitB, mailB, kookB, textB, shareB;

    private boolean shouldLaunchTuner = false;

    Toast exitToast;
    SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        setFace();

        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
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

        textB = (Button) findViewById(id.text);
        textB.setOnClickListener(this);

        shareB = (Button) findViewById(id.share);
        shareB.setOnClickListener(this);


    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        Intent activity;

        int viewId = arg0.getId();

        if (viewId == R.id.TunerButton) {
            if(!hasPermissions(PERMISSIONS)){
                shouldLaunchTuner = true;
                ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
            }
            else {
                activity = new Intent(MainMenu.this, KookType.class);
                startActivity(activity);
            }
        } else if (viewId == R.id.text) {
            activity = new Intent(MainMenu.this, ContentMenu.class);
            startActivity(activity);
        } else if (viewId == R.id.share) {
            try {
                // Use Intent with package name instead of getLaunchIntentForPackage
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.PouyaApp.NotkadehSantour");
                if (launchIntent != null) {
                    startActivity(launchIntent);
                } else {
                    throw new ActivityNotFoundException("App not found");
                }
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
        } else if (viewId == R.id.button_kookHelp) {
            activity = new Intent(MainMenu.this, KookHelp.class);
            startActivity(activity);
        } else if (viewId == R.id.mail) {
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
        } else if (viewId == R.id.exitButton) {
            backButtonHandler();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        
        if (requestCode == PERMISSION_ALL) {
            if (shouldLaunchTuner) {
                shouldLaunchTuner = false;
                
                // Check if all permissions were granted
                boolean allGranted = true;
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            allGranted = false;
                            break;
                        }
                    }
                } else {
                    allGranted = false;
                }
                
                if (allGranted) {
                    // Permissions granted, launch the tuner
                    Intent activity = new Intent(MainMenu.this, KookType.class);
                    startActivity(activity);
                } else {
                    // Permissions denied, show message
                    Toast.makeText(this, "کوک یار جهت کوک کردن ساز شما نیازمند دسترسی به میکروفن گوشی است", Toast.LENGTH_LONG).show();
                }
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
