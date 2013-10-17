package app.maikol.catcam.util;

import java.io.ByteArrayOutputStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.util.Base64;
import android.widget.Toast;
import app.maikol.catcam.CustomGallery;
import app.maikol.catcam.ImagesGrid;
import app.maikol.catcam.delegate.RemoteImageDelegate;
import app.maikol.catcam.model.Photo;
import app.maikol.catcam.model.PublicPhoto;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class HttpConnectionManager {
	
	
	public final static int CODE_GET_ALL_IMAGES=0;
	public final static int CODE_GET_IMAGES_BY_USER=1;
	public static String BASE_URL = "http://192.168.0.13:2403/photos";
//	public static String BASE_URL = "http://54.218.168.93:2403/photos";

	public static void getImageFromId(String id, AsyncHttpResponseHandler handler){
		AsyncHttpClient client = new AsyncHttpClient();
		client.get("http://192.168.0.3:2403/photos?id="+id, handler);
		
	}
	
	public static void getAllImages(final RemoteImageDelegate remoteImageDelegate){
		AsyncHttpClient client = new AsyncHttpClient();
//		final AsyncHttpResponseHandler imageHandler = remoteImageDelegate;
		client.setTimeout(1000);
		client.get(BASE_URL, new AsyncHttpResponseHandler(){
			@Override
			public void onSuccess(String arg0) {
				JSONArray imagesArray = null;
				try {

					System.out.println("JSON retrieved : " + arg0);
					if (arg0!=null)
					imagesArray = new JSONArray(arg0);
					
					if (imagesArray != null){
						
						
//						imageHandler.onSuccess(String.valueOf(imagesArray.length()));
						for (int i=0;i<imagesArray.length();i++){
							System.out.println("image " + String.valueOf(i) + " retrieved");
							
							PublicPhoto photo = getPhotoFromJSONElement(imagesArray.getJSONObject(i));
							if (photo!= null){
							remoteImageDelegate.didGetImage(i,photo);
							}else{
								remoteImageDelegate.didFailGetImage(i);
							}
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
	}
	
	public static void getImagesByUser(String user, final RemoteImageDelegate remoteImageDelegate) {
		AsyncHttpClient client = new AsyncHttpClient();
		client.setTimeout(1000);
		RequestParams params = new RequestParams();
		params.put("username", user);
		System.out.println("Requesting image for username:" + user);
		client.get(BASE_URL,params, new AsyncHttpResponseHandler(){
			@Override
			public void onSuccess(String arg0) {
				JSONArray imagesArray = null;
				try {

					System.out.println("JSON retrieved : " + arg0);
					if (arg0!=null)
					imagesArray = new JSONArray(arg0);
					
					for (int i=0;i<imagesArray.length();i++){
						System.out.println("image " + String.valueOf(i) + " retrieved");
						
						PublicPhoto photo = getPhotoFromJSONElement(imagesArray.getJSONObject(i));
						if (photo!= null){
							remoteImageDelegate.didGetImage(i,photo);
						}else{
							remoteImageDelegate.didFailGetImage(-1);
						}
					}
				} catch (JSONException e) {
					remoteImageDelegate.didFailGetImage(-1);
				}
			}
			
			@Override
			public void onFailure(Throwable arg0, String arg1) {
				// TODO Auto-generated method stub
				super.onFailure(arg0, arg1);
			}
		});
		
	}
	
	
	public static void saveImage(Photo photo, String mUsername){
		System.out.println("Saving image for username:" + mUsername);
		AsyncHttpClient client = new AsyncHttpClient();
		client.setTimeout(1000);
		RequestParams requestParams = new RequestParams();
		
		
		String imageBase64 = photo.encode();
		requestParams.put("image", imageBase64);
		requestParams.put("username", mUsername);
		client.post(BASE_URL, requestParams ,new AsyncHttpResponseHandler(){
			@Override
			public void onSuccess(String arg0) {
				System.out.println(arg0);
				super.onSuccess(arg0);
			}
			
			@Override
			public void onFailure(Throwable arg0) {
				arg0.getMessage();
				super.onFailure(arg0);
			}
		});
		
		
		
	}
	
	
	private static PublicPhoto getPhotoFromJSONElement(JSONObject jsonElement){
		
		PublicPhoto pp = null;
		try{
		String imageId = jsonElement.getString("id");
		String imageBase64 = jsonElement.getString("image");
		String username = jsonElement.getString("username");
		String description ="";

		pp = new PublicPhoto(imageId, imageBase64, username, description);
//		description = jsonElement.getString("description");
		}catch(JSONException je){
		}

		return pp;
	}
}
