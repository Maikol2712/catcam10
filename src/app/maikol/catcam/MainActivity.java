package app.maikol.catcam;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.NoTitle;
import com.googlecode.androidannotations.annotations.ViewById;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.media.AudioManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

@EActivity(R.layout.main)
@NoTitle
@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class MainActivity extends Activity implements SurfaceHolder.Callback {

	private LayoutInflater myInflater = null;
	Camera myCamera;
	Parameters cameraParameters;
	AudioManager audio;
	PopUpWindow menu;
	SoundMenu soundMenu;

	View popUpView;

	byte[] tempdata;
	boolean myPreviewRunning = false;

	private SurfaceHolder mySurfaceHolder;

//	@ViewById(R.id.surface)
	private SurfaceView mySurfaceView;

	Button takePicture;

//	@ViewById(R.id.btnSound)
	Button playSound;

	int volume;
	Bitmap bm;

	int photoIndex = 0;

//	@ViewById(R.id.imgRightBanner)
	ImageView imgRightBanner;

//	@ViewById(R.id.imgbtnSound)
	ImageButton imgBtnSound;

//	@ViewById(R.id.imgButton)
	ImageButton imgButton;

//	@ViewById(R.id.btnSettings)
	ImageButton btnSettings;

//	@ViewById(R.id.btnGallery)
	ImageButton btnGallery;

//	@ViewById(R.id.progressBar)
	private ProgressBar mProgress;

	List<Integer> optionValues;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

		setContentView(R.layout.main);
		
		 mySurfaceView = (SurfaceView) findViewById(R.id.surface);
		mySurfaceHolder = mySurfaceView.getHolder();
		mySurfaceHolder.addCallback(this);
		mySurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		mySurfaceView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				menu.hide();
				soundMenu.hide();
			}
		});

		final ShutterCallback myShutterCallback = new ShutterCallback() {
			@Override
			public void onShutter() {

			}
		};

		final PictureCallback myPictureCallback = new PictureCallback() {
			@Override
			public void onPictureTaken(byte[] data, Camera myCamera) {
				// TODO Auto-generated method stub
				if (data != null) {
					tempdata = data;
					done();
				}
			}
		};

		final PictureCallback myJpeg = new PictureCallback() {
			@Override
			public void onPictureTaken(byte[] data, Camera myCamera) {

				if (data != null) {
					tempdata = data;
					done();
				}
			}
		};

		myInflater = LayoutInflater.from(this);
		 mProgress = (ProgressBar) findViewById(R.id.progressBar);
		View rightBannerView = myInflater.inflate(R.layout.rightbanner, null);
		View soundBanner = myInflater.inflate(R.layout.soundbanner, null);
		View soundButtonsView = myInflater.inflate(R.layout.sound_background,
				null);
		View overView = myInflater.inflate(R.layout.second, null);
		View overView2 = myInflater.inflate(R.layout.third, null);
		View settingsView = myInflater.inflate(R.layout.settings, null);
		popUpView = myInflater.inflate(R.layout.popup_menu, null);

		this.addContentView(rightBannerView, new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		this.addContentView(overView, new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		this.addContentView(overView2, new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		this.addContentView(soundBanner, new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		this.addContentView(settingsView, new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		this.addContentView(soundButtonsView, new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		this.addContentView(popUpView, new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

		soundMenu = new SoundMenu(this);

		 playSound = (Button) findViewById(R.id.btnSound);
		 imgBtnSound = (ImageButton) findViewById(R.id.imgbtnSound);
		 imgButton = (ImageButton) findViewById(R.id.imgButton);
		 btnSettings = (ImageButton) findViewById(R.id.btnSettings);
		 btnGallery = (ImageButton) findViewById(R.id.btnGallery);
		playSound.setVisibility(View.INVISIBLE);

		btnGallery.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				// intent.setType("image/*");
				// startActivity(intent);

				ProgressBarTask pAsyn = new ProgressBarTask();
				// pAsyn.execute();
				Intent intent = new Intent(MainActivity.this,
						CustomPagerActivity.class);
				startActivity(intent);
				// Set the transition -> method available from Android 2.0 and
				// beyond
				// overridePendingTransition(R.anim.rotate_out,R.anim.rotate_in);

				// Intent intent = new Intent(MainActivity.this,
				// CustomGallery.class);

				// Intent intent = new Intent(MainActivity.this,
				// PicSelectActivity.class);
				// startActivity(intent);
			}
		});

		btnSettings.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (menu.isVisible()) {
					menu.hide();
				} else {
					menu.show();
				}
			}
		});

		// this.imgRightBanner = (ImageView) findViewById(R.id.imgRightBanner);

		imgButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (myCamera.getParameters().getFlashMode() == null) {

					myCamera.getParameters().setFlashMode(
							Parameters.FLASH_MODE_OFF);
				}
				myCamera.setParameters(menu.getParameters());
				myCamera.takePicture(myShutterCallback, myPictureCallback,
						myJpeg);

			}
		});

		// Restore previous settings;

		SharedPreferences options = getPreferences(MODE_MULTI_PROCESS);
		int length = options.getInt("optionsLength", 0);
		if (length > 0) {
			optionValues = new ArrayList<Integer>();
			for (int i = 0; i < length; i++) {
				optionValues.add(options.getInt("options" + i, 0));
			}

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	void writeImageInFolder() {
		try {
			File directory = new File(Environment.getExternalStorageDirectory()
					+ File.separator + "CatCam/");
			if (!directory.exists()) {
				directory.mkdirs();
			}
			File f = null;
			boolean exists = false;
			while (f == null || exists) {
				f = new File(Environment.getExternalStorageDirectory()
						+ File.separator + "CatCam/catcam-" + photoIndex
						+ ".jpg");
				if (f.exists()) {
					exists = true;
					photoIndex++;
				} else {
					exists = false;
					f.createNewFile();
				}
			}
			FileOutputStream out = new FileOutputStream(f);
			bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
			bm.recycle();
			out.close();
			out = null;

			// SingleMediaScanner mediaScanner = new SingleMediaScanner(
			// getApplicationContext(), f);
			sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
					Uri.parse("file://"
							+ Environment.getExternalStorageDirectory())));

		} catch (Exception e) {
			bm.recycle();
			e.printStackTrace();
		}
	}

	void done() {
		if (bm != null) {
			bm.recycle();
			bm = null;
		}

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 5;
		bm = BitmapFactory.decodeByteArray(tempdata, 0, tempdata.length,
				options);

		tempdata = null;
		writeImageInFolder();
		bm.recycle();
		bm = null;
		// startActivity(new Intent(MainActivity.this, MainActivity.class));
		// finish();
		myCamera.startPreview();

		Bundle bundle = new Bundle();
		if (bundle == null) {
			Toast.makeText(this, "Picture can not be saved", Toast.LENGTH_SHORT)
					.show();
		}
		// finish();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		try {
			if (myPreviewRunning) {
				myCamera.stopPreview();
				myPreviewRunning = false;
			}
			Camera.Parameters p = myCamera.getParameters();
			// p.setPreviewSize(width, height);
			p = menu.getParameters();
			setPreviewResolution(p);
			// p.setPreviewSize(p.getSupportedPreviewSizes().get(0).width, p
			// .getSupportedPreviewSizes().get(0).height);
			myCamera.setParameters(p);
			myCamera.setPreviewDisplay(holder);
			myCamera.startPreview();
			System.out.println("startPreview"
					+ p.getSupportedPreviewSizes().get(3).width + " "
					+ p.getSupportedPreviewSizes().get(0).height);
			myCamera.autoFocus(null);
			myPreviewRunning = true;
		} catch (Exception e) {
			Log.e("ERROR", e.getMessage());
		}
	}

	private void setPreviewResolution(Parameters p) {
		int maxWidth = 0;
		int maxHeight = 0;
		p.setPreviewSize(p.getPictureSize().width, p.getPictureSize().height);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		myCamera = Camera.open();

		cameraParameters = myCamera.getParameters();
		menu = new PopUpWindow(this, cameraParameters, popUpView);
		if (optionValues != null && optionValues.size() > 0) {
			menu.setOptionsIndex(optionValues);
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		myCamera.stopPreview();
		myPreviewRunning = false;
		myCamera.release();
		myCamera = null;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			super.onBackPressed();
			finish();
			return true;
		}
		switch (keyCode) {
		case KeyEvent.KEYCODE_VOLUME_UP:
			audio.adjustStreamVolume(AudioManager.STREAM_MUSIC,
					AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
			return true;
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			audio.adjustStreamVolume(AudioManager.STREAM_MUSIC,
					AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
			return true;
		default:
			return false;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mProgress != null) {
			mProgress.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		SharedPreferences settings = getPreferences(MODE_MULTI_PROCESS);
		SharedPreferences.Editor editor = settings.edit();
		if (menu != null) {
			int[] options = menu.getCurrentOptionsIndex();

			if (options.length > 0) {
				editor.putInt("optionsLength", options.length);
				for (int i = 0; i < options.length; i++) {
					editor.putInt("options" + i, options[i]);
				}

			}
			editor.commit();
		}
	}

	private class ProgressBarTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			mProgress.setVisibility(View.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... voids) {
			return null;
		}
	}

}
