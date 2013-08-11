package org.ilanguage.spyornot.perception;

import java.util.ArrayList;
import java.util.Random;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MainGameFragment extends Fragment {

	private ArrayList<Integer> mAudioIds;
	private int audioIndex;
	private int maxNumberOfStimuliPerGroup = 5;
	private int numberOfGroups = 3;
	MediaPlayer mPlayer;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_main_game, container,
				false);

		Button button = (Button) view.findViewById(R.id.next_audio_button);
		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				playNextAudio();
			}
		});

		// Play first audio file on initial load; do not replay audio file on
		// orientation change, but keep audio index and initially randomized
		// audio files
		if (savedInstanceState != null) {
			audioIndex = savedInstanceState.getInt("audioIndex");
			mAudioIds = savedInstanceState.getIntegerArrayList("mAudioIds");
		} else {
			audioIndex = 0;
			mAudioIds = initializeRandomizedAudioStimuli();
			mPlayer = MediaPlayer.create(view.getContext(),
					mAudioIds.get(audioIndex));
			mPlayer.start();
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
						}
					});
			AlertDialog alertDialog = alert.create();
			alertDialog.show();
		} else {
			mPlayer = MediaPlayer.create(getView().getContext(),
					mAudioIds.get(audioIndex));
			mPlayer.start();
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

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("audioIndex", audioIndex);
		outState.putIntegerArrayList("mAudioIds", mAudioIds);
	}
}
