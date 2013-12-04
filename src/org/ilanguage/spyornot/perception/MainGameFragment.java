package org.ilanguage.spyornot.perception;

import java.util.ArrayList;
import java.util.Random;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MainGameFragment extends Fragment {

	private ArrayList<Integer> mAudioIds;
	private int audioIndex;
	private int maxNumberOfStimuliPerGroup = 4;
	private int numberOfGroups = 3;
	private ArrayList<Integer> spiesOnBus = new ArrayList<Integer>();
	MediaPlayer mPlayer;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_main_game, container,
				false);

		// Set sideBar menu properties if in view
		MainMenuFragment mainMenuFragment = (MainMenuFragment) getFragmentManager()
				.findFragmentById(R.id.main_menu_fragment);
		if (mainMenuFragment.isInLayout()) {
			TextView step1 = (TextView) mainMenuFragment.getView()
					.findViewById(R.id.menu_step1);
			TextView step2 = (TextView) mainMenuFragment.getView()
					.findViewById(R.id.menu_step2);
			TextView step3 = (TextView) mainMenuFragment.getView()
					.findViewById(R.id.menu_step3);
			step1.setTextColor(Color.parseColor("#FFFFFF"));
			step2.setTextColor(Color.parseColor("#F5DC49"));
			step3.setTextColor(Color.parseColor("#FFFFFF"));
		}

		ImageView isOrIsNotASpy = (ImageView) view
				.findViewById(R.id.is_or_is_not_a_spy);

		isOrIsNotASpy.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {

					String speakerBackground = getResources().getResourceName(
							mAudioIds.get(audioIndex));
					if (speakerBackground.indexOf("russian") > -1) {
						Log.v("TEST", "SPY!");
					} else {
						Log.v("TEST", "NOT a spy!");
					}

					int x = (int) event.getX();
					int y = (int) event.getY();

					Log.v("TEST: ", "x: " + x + ", y: " + y);

					playNextAudio();

					return true;
				}
				return false;
			}
		});

		// Play first audio file on initial load; do not replay audio file on
		// orientation change, but keep audio index and initially randomized
		// audio files, as well as re-initializing the MediaPlayer so it is not
		// null
		if (savedInstanceState != null) {
			spiesOnBus = savedInstanceState.getIntegerArrayList("spiesOnBus");
			audioIndex = savedInstanceState.getInt("audioIndex");
			mAudioIds = savedInstanceState.getIntegerArrayList("mAudioIds");
			mPlayer = MediaPlayer.create(view.getContext(),
					mAudioIds.get(audioIndex));
			showHideSpies(view, audioIndex);
		} else {
			// Get resource IDs for Spies on Bus
			for (int i = 0; i < 12; i++) {
				String spiesOnBusId = "spy_on_bus" + (i + 1);
				int resId = getResources().getIdentifier(spiesOnBusId, "id",
						view.getContext().getPackageName());
				spiesOnBus.add(i, resId);
			}
			audioIndex = 0;
			mAudioIds = initializeRandomizedAudioStimuli();
			mPlayer = MediaPlayer.create(view.getContext(),
					mAudioIds.get(audioIndex));
			mPlayer.start();
			showHideSpies(view, audioIndex);
		}

		return view;
	};

	public ArrayList<Integer> initializeRandomizedAudioStimuli() {
		TypedArray russianAudio = getResources().obtainTypedArray(
				R.array.russian_stimuli);

		TypedArray bilingualAudio = getResources().obtainTypedArray(
				R.array.bilingual_stimuli);

		TypedArray monolingualAudio = getResources().obtainTypedArray(
				R.array.monolingual_stimuli);

		int[] russianAudioIds = new int[russianAudio.length()];
		for (int i = 0; i < russianAudio.length(); i++) {
			russianAudioIds[i] = russianAudio.getResourceId(i, -1);
		}

		int[] bilingualAudioIds = new int[bilingualAudio.length()];
		for (int i = 0; i < bilingualAudio.length(); i++) {
			bilingualAudioIds[i] = bilingualAudio.getResourceId(i, -1);
		}

		int[] monolingualAudioIds = new int[monolingualAudio.length()];
		for (int i = 0; i < monolingualAudio.length(); i++) {
			monolingualAudioIds[i] = monolingualAudio.getResourceId(i, -1);
		}

		shuffleArray(russianAudioIds);
		shuffleArray(bilingualAudioIds);
		shuffleArray(monolingualAudioIds);

		ArrayList<Integer> randomAudioIds = new ArrayList<Integer>();

		for (int i = 0; i < maxNumberOfStimuliPerGroup; i++) {
			randomAudioIds.add(russianAudioIds[i]);
			randomAudioIds.add(bilingualAudioIds[i]);
			randomAudioIds.add(monolingualAudioIds[i]);
		}

		russianAudio.recycle();
		bilingualAudio.recycle();
		monolingualAudio.recycle();

		return randomAudioIds;
	}

	public void playNextAudio() {
		// Do not allow user to go to next sound file until current sound file
		// has finished playing
		if (mPlayer.isPlaying()) {
			AlertDialog.Builder alert = new AlertDialog.Builder(getView()
					.getContext());
			alert.setTitle(R.string.app_name);
			alert.setMessage(R.string.wait);

			alert.setPositiveButton(R.string.ok,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							dialog.cancel();
						}
					});
			AlertDialog alertDialog = alert.create();
			alertDialog.show();
		} else {
			audioIndex++;
			if (audioIndex >= (maxNumberOfStimuliPerGroup * numberOfGroups)) {
				AlertDialog.Builder alert = new AlertDialog.Builder(getView()
						.getContext());
				alert.setTitle(R.string.app_name);
				alert.setMessage(R.string.game_over);

				alert.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								dialog.cancel();
								goToResultsScreen();
							}
						});
				AlertDialog alertDialog = alert.create();
				alertDialog.show();
			} else {
				mPlayer = MediaPlayer.create(getView().getContext(),
						mAudioIds.get(audioIndex));
				mPlayer.start();
				showHideSpies(getView(), audioIndex);
			}
		}
	}

	public void showHideSpies(View view, int index) {
		for (int i = 0; i <= index; i++) {
			ImageView showSpy = (ImageView) view.findViewById(spiesOnBus.get(i));
			showSpy.setVisibility(View.VISIBLE);
		}
	}
	
	static void shuffleArray(int[] ar) {
		Random rnd = new Random();
		for (int i = ar.length - 1; i >= 0; i--) {
			int index = rnd.nextInt(i + 1);
			// Simple swap
			int a = ar[index];
			ar[index] = ar[i];
			ar[i] = a;
		}
	}

	public void goToResultsScreen() {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ResultsScreenFragment fb = new ResultsScreenFragment();
		ft.replace(R.id.main_fragment_container, fb, "ResultsScreenFragment");
		ft.addToBackStack(null);
		ft.commit();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("audioIndex", audioIndex);
		outState.putIntegerArrayList("mAudioIds", mAudioIds);
		outState.putIntegerArrayList("spiesOnBus", spiesOnBus);
	}
}
