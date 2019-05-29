package com.jicent.jetrun.model;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.jicent.jetrun.screen.GameScreen;

public class MountCrush extends Actor {
	private List<ParticleEffect> mList;
	private GameScreen screen;
	
	public MountCrush(GameScreen screen) {
		this.screen=screen;
		mList = new LinkedList<ParticleEffect>();
	}
	
	public void addCrush(float x, float y) {
		ParticleEffect effect = null;
		effect = screen.getParticle("particle/motor.p", "particle", 1, 1);
		if (effect != null) {
			effect.setPosition(x, y);
			mList.add(effect);
		}
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		for (int i = mList.size() - 1; i >= 0; i--) {
			ParticleEffect effect = mList.get(i);
			if (effect.isComplete()) {
				mList.remove(i);
			} else {
				effect.draw(batch, Gdx.graphics.getDeltaTime());
			}
		}
	}
	
//	public boolean isCompletePlay(){
//		return mList.size()==0;
//	}
	
	public void clearRes(){
		mList.clear();
		mList=null;
	}
	
}
