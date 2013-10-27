package app.maikol.catcam;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import app.maikol.catcam.delegate.RemoteImageDelegate;
import app.maikol.catcam.model.Comment;
import app.maikol.catcam.model.Photo;
import app.maikol.catcam.model.PublicPhoto;
import app.maikol.catcam.util.HttpConnectionManager;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by Miguel on 19/05/13.
 */
public class CustomGallery extends Fragment implements RemoteImageDelegate{

	AsyncHttpResponseHandler imageHandler = new AsyncHttpResponseHandler(){
		
		@Override
	    public void onSuccess(String response) {
	    	try {
	    		JSONArray jsonResponse  = new JSONArray(response);
				if (!photos.isEmpty()){
					Bitmap bitmap = Photo.decodeString(jsonResponse.getJSONObject(0).getString("image"));
					photos.get(0).setBitmap(bitmap);
				}
			} catch (JSONException e) {
				Toast.makeText(CustomGallery.this.getActivity(), "Error receiving image",  Toast.LENGTH_SHORT);
			}
	        System.out.println(response);
	        mAdapter.notifyDataSetChanged();
	        
	    }
		
		public void onFailure(Throwable arg0, String arg1) {
			System.out.println("FALLO");
		};
	    
	
	
	};
	public ListView myHorizontalListView;
	ProgressBar mProgressBar;

	List<Photo> photos = new ArrayList<Photo>();

	ImageLoader imageLoader;
	ImageLoaderConfiguration config;
	
	String mUsername;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final SharedPreferences sharedPreferences = getActivity().getPreferences(
				Context.MODE_MULTI_PROCESS);
		mUsername = sharedPreferences.getString("username", "");
		
		RelativeLayout galleryView = (RelativeLayout) inflater.inflate(
				R.layout.gallery, container, false);
		mProgressBar = (ProgressBar) galleryView.findViewById(R.id.progressBar);
		mProgressBar.setVisibility(View.VISIBLE);

		myHorizontalListView = (ListView) galleryView
				.findViewById(R.id.scrollImages);
		// myHorizontalListView =
		// (CustomHorizontalScroll)findViewById(R.id.scrollImages);

		String ExternalStorageDirectoryPath = Environment
				.getExternalStorageDirectory().getAbsolutePath();

		String targetPath = ExternalStorageDirectoryPath + "/Catcam/";
		File targetDirector = new File(targetPath);

		final File[] filesFromSD = targetDirector.listFiles();

		FileLoaderASync fileLoader = new FileLoaderASync();
		fileLoader.execute(filesFromSD);
		if (imageLoader!= null){
			imageLoader.destroy();
		}
		imageLoader = ImageLoader.getInstance();

		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.gallery).cacheInMemory(true)
				.cacheOnDisc().build();

		config = new ImageLoaderConfiguration.Builder(this.getActivity())
				.threadPoolSize(5).threadPriority(Thread.MIN_PRIORITY + 2)
				.denyCacheImageMultipleSizesInMemory()
				.memoryCache(new UsingFreqLimitedMemoryCache(2000000)) // You
																		// can
																		// pass
																		// your
																		// own
																		// memory
																		// cache
																		// implementation
				// .discCache(new UnlimitedDiscCache(cacheDir)) // You can pass
				// your own disc cache implementation
				// .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
				.defaultDisplayImageOptions(options).build();

		imageLoader.init(ImageLoaderConfiguration.createDefault(this
				.getActivity()));
		return galleryView;
	}
	
	

	private BaseAdapter mAdapter = new BaseAdapter() {

		@Override
		public int getCount() {
			return photos.size();
		}

		@Override
		public Photo getItem(int position) {
			return photos.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// if (convertView == null) {
			final Photo photo = getItem(position);

			View retval = LayoutInflater.from(parent.getContext()).inflate(
					R.layout.gallery_item, null);

			final ImageView imageView = (ImageView) retval
					.findViewById(R.id.imageView);
			// imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
			// imageView.setImageBitmap(photo.bm);
			imageLoader.displayImage("file://" + photo.file.getAbsolutePath(),
					imageView);
//			imageView.setImageBitmap(photo.bm);
			
			imageView.setOnLongClickListener(new View.OnLongClickListener() {

				public boolean onLongClick(View view) {
//					Intent intent = new Intent();
//					intent.setAction(Intent.ACTION_VIEW);
//					String ExternalStorageDirectoryPath = Environment
//							.getExternalStorageDirectory().getAbsolutePath();
//					Log.d("Catcam", photo.file.getName());
//					intent.setDataAndType(
//							Uri.parse("file://" + photo.file.getAbsolutePath()),
//							"image/*");
//					CustomGallery.this.startActivity(intent);

					
					HttpConnectionManager.saveImage(photo,mUsername);
					return true;
				}
			});

			TextView title = (TextView) retval.findViewById(R.id.textView);
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");

			title.setText(sdf.format(photo.file.lastModified()));

			return retval;
			// } else {
			// return convertView;
			// }
		}

	};

	public class FileLoaderASync extends AsyncTask<File[], Void, Void> {

		@Override
		protected void onPreExecute() {
			// put a preloder
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(File[]... arg0) {
			// int i = 0;
			 for (File file : arg0[0]) {
			 // if (i < 10)
			 photos.add(new Photo(file));
			 // i++;
			 }
			
//			HttpConnectionManager.getImageFromId("", imageHandler); 

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			myHorizontalListView.setAdapter(mAdapter);
			mProgressBar.setVisibility(View.GONE);

		}

	}

	@Override
	public void didGetImage(PublicPhoto photo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void didGetImage(Integer index, PublicPhoto photo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void didFailGetImage(int i) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void didGetComment(int i, Comment comment) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void didFailGetComment(int i) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
    public void setMenuVisibility(final boolean visible) {
        if (!visible) {
        	for (Photo p : photos){
        		p.getBitmap().recycle();
        	}
        }
    }

	// @Override
	// protected void onResume() {
	// super.onResume();
	// }
}
