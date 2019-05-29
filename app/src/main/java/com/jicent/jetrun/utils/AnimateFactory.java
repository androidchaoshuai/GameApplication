package com.jicent.jetrun.utils;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.jicent.jetrun.screen.FatherScreen;

public class AnimateFactory {

	
	public static Animation getAnimate(FatherScreen screen,String frontPath,int count,String endPath,float frameDuration,PlayMode playMode){
		TextureRegion tmp[] = new TextureRegion[count];
		for (int i = 0; i < tmp.length; i++) {
			tmp[i] = new TextureRegion(screen.getTexture(frontPath + i + endPath));
		}
		Animation a=new Animation(frameDuration, tmp);
		a.setPlayMode(playMode);
		return a;
	}

	public static Animation getAnimate(FatherScreen screen,String filePath,int tileW,int tileH,float frameDuration,PlayMode playMode){
		TextureRegion[][] tr= TextureRegion.split(screen.getTexture(filePath), tileW, tileH);
		TextureRegion[] tmp=new TextureRegion[tr.length*tr[0].length];
		
		int k=0;
		for (int i = 0; i < tr.length; i++) {
			for (int j = 0; j < tr[i].length; j++) {
				tmp[k++]=tr[i][j];
			}
		}
		Animation a=new Animation(frameDuration, tmp);
		a.setPlayMode(playMode);
		return a;
	}

	public static TextureRegion[] getTextureRegions(FatherScreen screen,String filePath,int tileW,int tileH){
		TextureRegion[][] tr= TextureRegion.split(screen.getTexture(filePath), tileW, tileH);
		TextureRegion[] tmp=new TextureRegion[tr.length*tr[0].length];
		int k=0;
		for (int i = 0; i < tr.length; i++) {
			for (int j = 0; j < tr[i].length; j++) {
				tmp[k++]=tr[i][j];
			}
		}
		return tmp;
	}
	
}
