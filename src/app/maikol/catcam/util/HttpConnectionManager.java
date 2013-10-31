package app.maikol.catcam.util;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import app.maikol.catcam.delegate.RemoteImageDelegate;
import app.maikol.catcam.model.Comment;
import app.maikol.catcam.model.Photo;
import app.maikol.catcam.model.PublicPhoto;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class HttpConnectionManager {
	
	
	public final static int CODE_GET_ALL_IMAGES=0;
	public final static int CODE_GET_IMAGES_BY_USER=1;
	public static String BASE_URL = "http://91.117.108.11:2403";
	public static String PHOTOS_PATH = "/photos";
	public static String COMMENTS_PATH = "/comments";
	
	public static int TIMEOUT = 10000;
	
	public static void getAllImages(final RemoteImageDelegate remoteImageDelegate){
		AsyncHttpClient client = new AsyncHttpClient();
//		final AsyncHttpResponseHandler imageHandler = remoteImageDelegate;
		client.setTimeout(TIMEOUT);
		client.get(BASE_URL + PHOTOS_PATH, new AsyncHttpResponseHandler(){
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
		client.setTimeout(TIMEOUT);
		RequestParams params = new RequestParams();
		params.put("username", user);
		System.out.println("Requesting image for username:" + user);
		client.get(BASE_URL + PHOTOS_PATH,params, new AsyncHttpResponseHandler(){
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
		client.setTimeout(TIMEOUT);
		RequestParams requestParams = new RequestParams();
		
		
		String imageBase64 = photo.encode();
		requestParams.put("image", imageBase64);
		requestParams.put("username", mUsername);
		client.post(BASE_URL+ PHOTOS_PATH, requestParams ,new AsyncHttpResponseHandler(){
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
	
private static Comment getCommentFromJSONElement(JSONObject jsonElement){
		
		Comment c = null;
		try{
		String imageId = jsonElement.getString("imageId");
		String comment = jsonElement.getString("comment");
		String username = jsonElement.getString("username");
		String description ="";

		c = new Comment(imageId,username,comment);
//		description = jsonElement.getString("description");
		}catch(JSONException je){
		}

		return c;
	}
	
	public static void getCommentsFromPhoto(String imageId, final RemoteImageDelegate remoteImageDelegate){
		AsyncHttpClient client = new AsyncHttpClient();
		client.setTimeout(TIMEOUT);
		RequestParams params = new RequestParams();
		params.put("imageId", imageId);
		System.out.println("Requesting comments for photo:" + imageId);
		client.get(BASE_URL + COMMENTS_PATH,params, new AsyncHttpResponseHandler(){
			@Override
			public void onSuccess(String arg0) {
				JSONArray imagesArray = null;
				try {

					System.out.println("JSON retrieved : " + arg0);
					if (arg0!=null)
					imagesArray = new JSONArray(arg0);
					
					for (int i=0;i<imagesArray.length();i++){
						System.out.println("comment " + String.valueOf(i) + " retrieved");
						
						Comment comment = getCommentFromJSONElement(imagesArray.getJSONObject(i));
						if (comment!= null){
							remoteImageDelegate.didGetComment(i,comment);
						}else{
							remoteImageDelegate.didFailGetComment(-1);
						}
					}
				} catch (JSONException e) {
					remoteImageDelegate.didFailGetComment(-1);
				}
			}
			
			@Override
			public void onFailure(Throwable arg0, String arg1) {
				// TODO Auto-generated method stub
				super.onFailure(arg0, arg1);
			}
		});
	}

	public static void saveComment(Comment newComment) {
		System.out.println("Saving comment for commetn:" + newComment.getImageId() + " - " + newComment.getUsername() + " - " + newComment.getComment());
		AsyncHttpClient client = new AsyncHttpClient();
		client.setTimeout(TIMEOUT);
		RequestParams requestParams = new RequestParams();
		
		
		requestParams.put("imageId", newComment.getImageId());
		requestParams.put("username", newComment.getUsername());
		requestParams.put("comment", newComment.getComment());
		client.post(BASE_URL + COMMENTS_PATH, requestParams ,new AsyncHttpResponseHandler(){
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
}
