package app.maikol.catcam;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import app.maikol.catcam.adapters.SoundAdapter;
import app.maikol.catcam.components.SoundButton;
import app.maikol.catcam.lists.SoundListView;
import app.maikol.catcam.model.Sound;

public class SoundMenu {

	private SoundButton btnSound1;
	private SoundButton btnSound2;
	private SoundButton btnSound3;
	private SoundButton btnSound4;
	private SoundButton btnSound5;
	private SoundButton btnSound6;

	SoundListView listView;
	LinearLayout menulayout;
	Context c;
	
	private SoundButton soundButton;

	public enum SOUNDS {
		CAT_SOUND("Cat sound"),

		BIP_BIP("Bip Bip"),

		MEW01("Mew01"),

		MEW02("Mew02"),

		MEW03("Mew03"),

		MEW04("Mew04"),

		MEW05("Mew05"),

		MEW06("Mew06"),

		MEW07("Mew07"),

		MEW08("Mew08"),

		MEW09("Mew09") ;

		private String name;

		private SOUNDS(String name) {
			this.name = name;

		}

		public String getLabel() {
			return name;
		}
	}

	public SoundMenu(Activity a) {
		c = a.getBaseContext();
		
		SharedPreferences prefs =
			     a.getPreferences(Context.MODE_PRIVATE);
			 
			final SharedPreferences.Editor editor = prefs.edit();
			
		btnSound1 = (SoundButton) a.findViewById(R.id.btnSound1);
		btnSound2 = (SoundButton) a.findViewById(R.id.btnSound2);
		btnSound3 = (SoundButton) a.findViewById(R.id.btnSound3);
		btnSound4 = (SoundButton) a.findViewById(R.id.btnSound4);
		
		List<Integer> buttonSoundIdLists = new ArrayList<Integer>();
		buttonSoundIdLists.add(prefs.getInt("button1Sound", R.raw.cat));
		buttonSoundIdLists.add(prefs.getInt("button2Sound", R.raw.send));
		buttonSoundIdLists.add(prefs.getInt("button3Sound", R.raw.mew01));
		buttonSoundIdLists.add(prefs.getInt("button4Sound", R.raw.mew02)); 	
		
		
		final ArrayList<Sound> soundsList = new ArrayList<Sound> ();
        for(SOUNDS sound: SOUNDS.values()){
        	Integer buttonId = 0;
        	Integer soundId = 0;
        	if(sound.equals(SOUNDS.CAT_SOUND)){
        		soundId = R.raw.cat;
			}
        	if(sound.equals(SOUNDS.BIP_BIP)){
        		soundId = R.raw.send;
			}
        	if(sound.equals(SOUNDS.MEW01)){
        		soundId = R.raw.mew01;
			}
        	if(sound.equals(SOUNDS.MEW01)){
        		soundId = R.raw.mew01;
			}if(sound.equals(SOUNDS.MEW02)){
        		soundId = R.raw.mew02;
			}if(sound.equals(SOUNDS.MEW03)){
        		soundId = R.raw.mew03;
			}if(sound.equals(SOUNDS.MEW04)){
        		soundId = R.raw.mew04;
			}if(sound.equals(SOUNDS.MEW05)){
        		soundId = R.raw.mew05;
			}if(sound.equals(SOUNDS.MEW06)){
        		soundId = R.raw.mew06;
			}if(sound.equals(SOUNDS.MEW07)){
        		soundId = R.raw.mew07;
			}if(sound.equals(SOUNDS.MEW08)){
        		soundId = R.raw.mew08;
			}if(sound.equals(SOUNDS.MEW09)){
        		soundId = R.raw.mew09;      		
        		
			}	
    		
			for (Integer sId : buttonSoundIdLists){
				if (sId == soundId){
					buttonId = buttonSoundIdLists.indexOf(sId);
				}
			}
    		
            soundsList.add(new Sound(sound.getLabel(),soundId, buttonId));
        }
		
		final SoundAdapter adapter = new SoundAdapter(a.getBaseContext(),
				soundsList);
//		ArrayAdapter arrayAdapter = new ArrayAdapter(a
//				.getApplicationContext(),
//				android.R.layout.simple_list_item_single_choice,
//				soundsList);
        menulayout = (LinearLayout)a.findViewById(R.id.soundmenu);
        menulayout.setVisibility(View.GONE);
        listView = new SoundListView((GridView) a.findViewById(R.id.listViewSound));
		listView.getGridView().setAdapter(adapter);
		listView.getGridView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0,
					View arg1, int position,
					long arg3) {
//                listView.setSelection(position);
				
                menulayout.setVisibility(View.GONE);
                
                // Asignamos el sonido al botón
                soundButton.setSound(soundsList.get(position));
                
                for (Sound s : soundsList ){
                	// Buscamos el índice de la lista de sonidos para asignarle el botón usado
                	if (s.getCurrentButtonIndex() == soundButton.index){
                		// Si el sonido nuevo ya tenia un boton asignado, intercambiar, sino vaciamos el elemento de la lista anterior
                		if (soundsList.get(position).getCurrentButtonIndex() != 0){
                			s.setCurrentButtonIndex(soundsList.get(position).getCurrentButtonIndex());
                		}else{
                			s.setCurrentButtonIndex(0);
                		}
                    	soundsList.get(position).setCurrentButtonIndex(soundButton.index);
                	}
                }
                soundsList.get(position).setCurrentButtonIndex(soundButton.index);
                
                editor.putInt("button"+soundButton.index+"Sound", soundsList.get(position).getSoundId());
                editor.commit();
                
                adapter.notifyDataSetChanged();
			}
		});
		
		btnSound1.setLongClickListener(this,menulayout,a);
		btnSound2.setLongClickListener(this,menulayout,a);
		btnSound3.setLongClickListener(this,menulayout,a);
		btnSound4.setLongClickListener(this,menulayout,a);
	}

	public void hide() {
		this.menulayout.setVisibility(View.GONE);
	}

	public void setSoundButton(SoundButton soundButton2) {
	this.soundButton = soundButton2;	
	}

}
