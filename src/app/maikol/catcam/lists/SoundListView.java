package app.maikol.catcam.lists;

import android.widget.GridView;
import app.maikol.catcam.components.SoundButton;

public class SoundListView{

	GridView gridView;
	SoundButton soundButton;
	
	public SoundButton getSoundButton() {
		return soundButton;
	}

	public void setSoundButton(SoundButton soundButton) {
		this.soundButton = soundButton;
	}
	
	public SoundListView(GridView gridView) {
		this.gridView = gridView;
	}

	public void setGridView(GridView gv){
		this.gridView = gv;
	}
	
	public GridView getGridView(){
		return this.gridView;
	}

}
