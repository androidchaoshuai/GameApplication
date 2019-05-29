package com.jicent.jetrun.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.jicent.jetrun.screen.GameScreen;
import com.jicent.jetrun.utils.AnimateFactory;
import com.jicent.jetrun.utils.SoundUtil;

public class BangControl extends Group{

	GameScreen screen;
	
	public BangControl(GameScreen screen){
		this.screen=screen;
	}
	
	public void addBang(float x,float y){
		SoundUtil.playSound(screen.main.getManager(), "dead");
		BangAnimEx a=new BangAnimEx(0.1f, AnimateFactory.getTextureRegions(screen, "res/bang.png", 159, 172), PlayMode.NORMAL);
		a.setPosition(x-159/2, y-172/2);
		this.addActor(a);
	}
	
	
	class BangAnimEx extends Actor {
		private Animation animation;
		private float timer;
		
		public BangAnimEx(float duration, TextureRegion[] textureExs, PlayMode mode) {
			animation=new Animation(duration, textureExs) ;
			animation.setPlayMode(mode);
		}
		@Override
		public void act(float delta) {
			super.act(delta);
			timer+=delta;
			if(animation.isAnimationFinished(timer)){
				remove();
			}
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
	}
}
