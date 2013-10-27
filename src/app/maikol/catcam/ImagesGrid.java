package app.maikol.catcam;

import java.util.TreeMap;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.Toast;
import app.maikol.catcam.components.ViewPagerDelegate;
import app.maikol.catcam.delegate.RemoteImageDelegate;
import app.maikol.catcam.model.Comment;
import app.maikol.catcam.model.PublicPhoto;
import app.maikol.catcam.util.HttpConnectionManager;

public class ImagesGrid extends Fragment implements RemoteImageDelegate {
	ViewPagerDelegate mViewPagerDelegate;
	
	GridView imageGridView;
	LayoutInflater mLayoutInflater;
	Display display;
	int photosInSD;
	TreeMap<Integer, PublicPhoto> unassignedPhotos = new TreeMap<Integer, PublicPhoto>();

	TreeMap<Integer, ImageView> views;

	private ImageAdapter mAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mViewPagerDelegate = (CustomPagerActivity) this.getActivity();
		// mActivity = this.get.getActivity();
		// preferences = mActivity.getPreferences(Context.MODE_PRIVATE);
		RelativeLayout imageGalleryGridLayout = (RelativeLayout) inflater
				.inflate(R.layout.image_gallery_grid, container, false);
		imageGridView = (GridView) imageGalleryGridLayout
				.findViewById(R.id.gridImage);
		mAdapter = new ImageAdapter(getActivity());
		mLayoutInflater = inflater;

		WindowManager wm = (WindowManager) this.getActivity().getBaseContext()
				.getSystemService(Context.WINDOW_SERVICE);
		display = wm.getDefaultDisplay();

		imageGridView.setAdapter(mAdapter);

		// new PhotoLoaderASync().execute();
//		views.clear();
		HttpConnectionManager.getAllImages(this);
		return imageGalleryGridLayout;
	}// = new
		// ImageAdapter(imageGridView.getContext());

//	public class PhotoLoaderASync extends AsyncTask<Void, Void, Void> {
//
//		@Override
//		protected void onPreExecute() {
//			// put a preloder
//			super.onPreExecute();
//		}
//
//		@Override
//		protected Void doInBackground(Void... arg0) {
//			return null;
//		}
//
//		@Override
//		protected void onPostExecute(Void result) {
//			super.onPostExecute(result);
//			mAdapter.notifyDataSetChanged();
//		}
//
//	}

	public class ImageAdapter extends BaseAdapter {
		private Context mContext;

		public ImageAdapter(Context c) {
			mContext = c;
			views = new TreeMap<Integer, ImageView>();
		}

		@Override
		public int getCount() {
			return unassignedPhotos.size();
		}

		@Override
		public ImageView getItem(int position) {
			return views.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ImageView v;

			// get the ImageView for this position in the GridView..
			v = getItem(position);
			
			// this ImageView might not be created yet..

			if (v == null) {
				Log.d("catcam", "This view is not created. create it.");

				// create a new ImageView..
				v = new ImageView(mContext);
				
				v.setScaleType(ScaleType.CENTER_CROP);
				v.setLayoutParams(new GridView.LayoutParams(
						display.getWidth() / 2, display.getHeight() / 2));
				// v.setLayoutParams(new GridView.LayoutParams(110, 110));
				// v.setPadding(10, 10, 10, 10);

				// I'm setting a default image here so that you don't
				// see black nothingness.. (just using an icon that
				// comes with the Android SDK)
				if (unassignedPhotos.get(position) != null) {
					v.setImageBitmap(unassignedPhotos.get(position).getBitmap());
				}
				// pass this Bundle along to the LoadImage class,
				// which is a subclass of Android's utility class
				// AsyncTask. Whatever happens in this class is
				// on its own thread.. the Bundle passes
				// the file to load and the position the photo
				// should be placed in the GridView..

				// puts this new ImageView and position in the HashMap.
				views.put(position, v);
				
				final int pos = position;
				v.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						mViewPagerDelegate.didReceiveImageDetailsRequest(unassignedPhotos.get(pos));
					}
				});
			} else {
				if (unassignedPhotos.get(position) != null) {
					v.setImageBitmap(unassignedPhotos.get(position).getBitmap());
				}
			}

			// return the view to the GridView..
			// at this point, the ImageView is only displaying the
			// default icon..
			return v;
		}
	}

	public void getImagesByUser(String user) {
		views.clear();
		unassignedPhotos.clear();
		HttpConnectionManager.getImagesByUser(user, this);
	}

	public void getAllImages() {
		views.clear();
		HttpConnectionManager.getAllImages(this);
	}

	@Override
	public void didGetImage(PublicPhoto photo) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void didGetImage(Integer index, PublicPhoto photo) {
		unassignedPhotos.put(index, photo);		
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void didFailGetImage(int error) {

		Toast.makeText(this.getActivity(), "Error receiving image",  Toast.LENGTH_SHORT);
		unassignedPhotos.put(error, null);	
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void didGetComment(int i, Comment comment) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void didFailGetComment(int i) {
		// TODO Auto-generated method stub
		
	}
}
