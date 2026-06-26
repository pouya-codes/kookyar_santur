package com.PouyaApp.KookYaRSantooR;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.WindowCompat;
import androidx.preference.PreferenceFragmentCompat;

public class Prefs extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WindowCompat.setDecorFitsSystemWindows(getWindow(), true);
		setContentView(R.layout.activity_prefs);

		Toolbar toolbar = findViewById(R.id.app_bar);
		setSupportActionBar(toolbar);
		if (getSupportActionBar() != null) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setTitle(R.string.setting);
		}

		if (savedInstanceState == null) {
			getSupportFragmentManager()
					.beginTransaction()
					.replace(R.id.prefs_container, new PrefsFragment())
					.commit();
		}
	}

	@Override
	public boolean onSupportNavigateUp() {
		finish();
		return true;
	}

	public static class PrefsFragment extends PreferenceFragmentCompat {
		@Override
		public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
			setPreferencesFromResource(R.xml.perfs, rootKey);
		}
	}
}
