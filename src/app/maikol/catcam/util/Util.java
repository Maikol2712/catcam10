package app.maikol.catcam.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class Util {

	public static final String PREFS_NAME = "CatcamPreferences";
	public static String USERNAME  = "username";
	
	public static String getMyUsername(Context context){
		SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		return preferences.getString(USERNAME, "CatcamUser");
	}
	
	public static void setMyUsername(Context context, String s){
		SharedPreferences preferences =context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		System.out.println("Saving username: " + s);
		editor.putString(USERNAME, s);
		editor.commit();
	}
}
