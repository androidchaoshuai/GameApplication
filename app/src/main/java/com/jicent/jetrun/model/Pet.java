package com.jicent.jetrun.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.jicent.jetrun.data.StaticVariable;
import com.jicent.jetrun.screen.GameScreen;
import com.jicent.jetrun.utils.LogUtil;
/*
 * 宠物
 */
public class Pet extends Actor {
	private GameScreen screen;
	private Animation anim;
	private float timer;
	
	public Pet(GameScreen screen) {
		this.screen=screen;
		TextureRegion[] keyFrames = null;
		switch (StaticVariable.roleKind) {
		case 0:
			keyFrames=new TextureRegion[2];
			break;
		case 1:
			keyFrames=new TextureRegion[1];
			break;
		case 2:
			keyFrames=new TextureRegion[3];
			break;
		case 3:
			keyFrames=new TextureRegion[4];
			break;
		case 4:
			keyFrames=new TextureRegion[2];
			break;
		default:
			break;
		}
		for (int i = 0; i < keyFrames.length; i++) {
			keyFrames[i]=new TextureRegion(screen.getTexture("res/pet"+StaticVariable.roleKind+i+".png"));
		}
		anim=new Animation(0.1f, keyFrames);
		anim.setPlayMode(PlayMode.LOOP);
		setSize(67, 45);
		setPosition(screen.hero.getX()-getWidth()-40, screen.hero.getY()+70);
	}
	
	@Override
	public void act(float delta) { 
		if (!screen.control.isStopGame) {
			super.act(delta);
			timer+=delta;
			if(screen.control.trackList.size()>0){
				setPosition(screen.hero.getX()-getWidth()-30, screen.control.trackList.get(0));
				screen.control.trackList.remove(0);
			}
		}
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		Color color=getColor();
		batch.setColor(color.r,color.g,color.b,color.a*parentAlpha);
		
		batch.draw(anim.getKeyFrame(timer), getX(), getY(), getWidth(), getHeight());
	}
}
