package app.maikol.catcam.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import app.maikol.catcam.R;
import app.maikol.catcam.model.Sound;

public class SoundAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater li;
	private ArrayList<Sound> optionsList = new ArrayList<Sound>();

	final String TAG = "OptionsMenuAdapter";

	public SoundAdapter(Context context, ArrayList<Sound> list) {
		super();
		Log.d(TAG, "Adapter constructor list size" + list.size());
		this.context = context;
		this.optionsList = list;
		li = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return optionsList.size();
	}

	@Override
	public Sound getItem(int position) {
		return optionsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return (long) position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.d(TAG, "getView size"+optionsList.size());
		View v=null;
		
//		if (convertView != null) {
//			v = convertView;
//		} else {
			Sound sound = getItem(position);
			v = li.inflate(R.layout.sound_menu_option, null);
			
			TextView soundNameView = (TextView) v.findViewById(R.id.soundName);
			soundNameView.setText(sound.getName());
			
			LinearLayout linearLayout = (LinearLayout) v.findViewById(R.id.soundoptionbutton);
			
			linearLayout.setBackgroundResource(sound.getBackgroundImageId());	
			
			
//		}
		
		
		
		return v;
	}
}
