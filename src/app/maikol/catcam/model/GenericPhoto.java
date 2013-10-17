package app.maikol.catcam.model;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class GenericPhoto implements Serializable {

	Bitmap bitmap;
	

	public GenericPhoto(){
		
	}
	

    
    public GenericPhoto(Bitmap bitmap){
    	this.bitmap = bitmap;
    }

	public  static Bitmap decodeString(String imageBase64){
		byte[] imageAsBytes = Base64.decode(imageBase64,
				Base64.DEFAULT);
		Bitmap bitmap = BitmapFactory.decodeByteArray(
				imageAsBytes, 0, imageAsBytes.length);
		
		return bitmap;
	}
	
	public static String encodeBitmap(Bitmap bm){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();  
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object   
		byte[] b = baos.toByteArray();
		
		String imageBase64 = Base64.encodeToString(b,Base64.DEFAULT);
		return imageBase64;
	}
	


	public String encode() {
		return this.encodeBitmap(bitmap);
	}



	public Bitmap getBitmap() {
		return bitmap;
	}



	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	
}
