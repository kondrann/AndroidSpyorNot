package org.ilanguage.spyornot.perception;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class MainPageFragment extends Fragment {
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_main_page, container,
				false);

		// Set sideBar menu properties if in view
		MainMenuFragment mainMenuFragment = (MainMenuFragment) getFragmentManager().findFragmentById(R.id.main_menu_fragment);
		if (mainMenuFragment.isInLayout()) {
			TextView step1 = (TextView) mainMenuFragment.getView().findViewById(R.id.menu_step1);
			TextView step2 = (TextView) mainMenuFragment.getView().findViewById(R.id.menu_step2);
			TextView step3 = (TextView) mainMenuFragment.getView().findViewById(R.id.menu_step3);
			step1.setTextColor(Color.parseColor("#F5DC49"));
			step2.setTextColor(Color.parseColor("#FFFFFF"));
			step3.setTextColor(Color.parseColor("#FFFFFF"));
		}
		
		Button button = (Button) view.findViewById(R.id.start_game_button);
		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				AlertDialog.Builder alert = new AlertDialog.Builder(view
						.getContext());
				alert.setTitle(R.string.app_name);
				alert.setMessage(R.string.ready);

				alert.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								dialog.cancel();
								startGame();
							}
						});
				AlertDialog alertDialog = alert.create();
				alertDialog.show();
			}
		});
		return view;
	}

	public void startGame() {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		MainGameFragment fb = new MainGameFragment();
		ft.replace(R.id.main_fragment_container, fb, "MainGameFragment");
		ft.addToBackStack(null);
		ft.commit();
	}
}
