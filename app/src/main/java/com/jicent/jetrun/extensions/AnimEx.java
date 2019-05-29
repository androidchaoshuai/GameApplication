package com.jicent.jetrun.extensions;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class AnimEx extends Actor {
	private Animation animation;
	private float timer;
	
	public AnimEx(float duration, TextureEx[] textureExs, PlayMode mode) {
		TextureRegion[] regions=new TextureRegion[textureExs.length];
		for (int i = 0; i < regions.length; i++) {
			regions[i]=new TextureRegion(textureExs[i]);
		}
		animation=new Animation(duration, regions) ;
		animation.setPlayMode(mode);
	}
	public AnimEx(float duration, TextureRegion[] textureRegions, PlayMode mode) {
		animation=new Animation(duration, textureRegions) ;
		animation.setPlayMode(mode);
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		timer+=delta;
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		Color color = this.getColor();
		batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
		TextureRegion region=animation.getKeyFrame(timer);
		
		if (getWidth()==0||getHeight()==0) {
			batch.draw(region, getX(), getY(), getOriginX(), getOriginY(), region.getRegionWidth(), region.getRegionHeight(), getScaleX(), getScaleY(), getRotation());
		}else {
			batch.draw(region, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
		}
	}
	public boolean isFinish(){
		return animation.isAnimationFinished(timer);
	}
	
}
