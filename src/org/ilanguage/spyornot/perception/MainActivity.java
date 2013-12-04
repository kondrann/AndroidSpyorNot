package org.ilanguage.spyornot.perception;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;

public class MainActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Set view for main_fragment_container; must be done programmatically
		// if we wish to swap out fragments later
		if (findViewById(R.id.main_fragment_container) != null) {

			if (savedInstanceState != null) {
				return;
			}

			MainPageFragment mainPageFragment = new MainPageFragment();

			// In case this activity was started with special instructions from
			// an Intent, pass the Intent's extras to the fragment as arguments
			mainPageFragment.setArguments(getIntent().getExtras());

			// Add the fragment to the 'main_fragment_container' FrameLayout
			getSupportFragmentManager().beginTransaction()
					.add(R.id.main_fragment_container, mainPageFragment).commit();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
