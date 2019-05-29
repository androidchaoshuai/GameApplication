package com.jicent.jetrun.model;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.jicent.jetrun.screen.GameScreen;

public class ExplodeLight extends Group{
	private GameScreen screen;

	public ExplodeLight(GameScreen screen){
		this.screen=screen;
				
	}
	
	public void addExplode(){
		screen.dou.clear=true;
		screen.standBarrierControl.clear=true;
		screen.rotationBarrierControl.clear=true;
		screen.spikeweed.clear=true;
		screen.missile.clear=true;
		
		Image lightImg=new Image(screen.getTexture("res/explodeLight.png"));
		lightImg.setPosition(screen.hero.getX()+screen.hero.getWidth()/2, screen.hero.getY()+screen.hero.getHeight()/2);
		lightImg.setOrigin(lightImg.getWidth()/2, lightImg.getHeight()/2);
		lightImg.setScale(0);
		lightImg.addAction(Actions.sequence(Actions.scaleTo(5, 5, 0.2f),Actions.removeActor()));
		this.addActor(lightImg);
	}
	
}


