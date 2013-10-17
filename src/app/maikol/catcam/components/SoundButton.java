package app.maikol.catcam.components;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import app.maikol.catcam.R;
import app.maikol.catcam.SoundMenu;
import app.maikol.catcam.lists.SoundListView;
import app.maikol.catcam.model.Sound;

public class SoundButton extends ImageButton {

	Sound sound;

	public Integer index;

	public SoundButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (this.getId() == R.id.btnSound1)
			this.index = 1;
		if (this.getId() == R.id.btnSound2)
			this.index = 2;
		if (this.getId() == R.id.btnSound3)
			this.index = 3;
		if (this.getId() == R.id.btnSound4)
			this.index = 4;

		final Context c = context;
		// soundId = R.raw.send;
		// songLabel = SoundMenu.SOUNDS.BIP_BIP.getLabel();
		this.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (SoundButton.this != null) {
					try {
						MediaPlayer mp = MediaPlayer.create(c,
								SoundButton.this.sound.getSoundId());
						if (mp != null) {

							mp.start();

						}
						mp.setOnCompletionListener(new OnCompletionListener() {

							@Override
							public void onCompletion(MediaPlayer mp) {
								mp.release();
							}

						});
					} catch (Exception e) {

					}
				}

			}
		});

	}

	public void setLongClickListener(final SoundMenu soundMenu,
			final LinearLayout menulayout, final Activity a) {
		this.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				menulayout.setVisibility(View.VISIBLE);
				soundMenu.setSoundButton(SoundButton.this);
				return true;
			}
		});
	}

	public Sound getSound() {
		return this.sound;
	}

	public void setSound(Sound sound) {
		this.sound = sound;
	}

	public SoundButton(Context context) {
		super(context);
	}
}
