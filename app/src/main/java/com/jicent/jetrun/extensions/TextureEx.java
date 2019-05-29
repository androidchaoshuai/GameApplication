package com.jicent.jetrun.extensions;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;

public class TextureEx extends Texture {

	public TextureEx(Pixmap pixmap) {
		super(pixmap);
		this.setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}
	
	public TextureEx(TextureData data){
		super(data);
		this.setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}
	
	public TextureEx(String filename){
		super(filename);
		this.setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}

}
