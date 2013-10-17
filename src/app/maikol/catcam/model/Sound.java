package app.maikol.catcam.model;

import app.maikol.catcam.R;

public class Sound {

	String name;
	Integer soundId;
	Integer currentButtonIndex;

	public Sound(String name, Integer soundId, Integer currentButtonIndex) {
		this.name = name;
		this.soundId = soundId;
		this.currentButtonIndex = currentButtonIndex;
	}

	public Integer getBackgroundImageId() {
		
		if (currentButtonIndex == 1){
			return R.drawable.button1;			
		}			
		if (currentButtonIndex == 3){
			return R.drawable.button2;			
		}
		if (currentButtonIndex == 2){
			return R.drawable.button3;			
		}
		if (currentButtonIndex == 4){
			return R.drawable.button4;			
		}
		
		return R.drawable.button_none;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSoundId() {
		return soundId;
	}

	public void setSoundId(Integer soundId) {
		this.soundId = soundId;
	}

	public Integer getCurrentButtonIndex() {
		return currentButtonIndex;
	}

	public void setCurrentButtonIndex(Integer currentButtonIndex) {
		this.currentButtonIndex = currentButtonIndex;
	}

}
