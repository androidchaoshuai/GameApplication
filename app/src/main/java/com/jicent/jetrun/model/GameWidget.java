package com.jicent.jetrun.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.jicent.jetrun.data.GameState;
import com.jicent.jetrun.data.StaticVariable;
import com.jicent.jetrun.extensions.ProcessEx.ProcessType;
import com.jicent.jetrun.screen.GameScreen;
import com.jicent.jetrun.utils.DialogUtil;
import com.jicent.jetrun.utils.LayoutDebug;
import com.jicent.jetrun.utils.SPUtil;

public class GameWidget extends Group{
	private GameScreen screen;
	private Label distanceLabel,coinNumLabel;
	public int realDistance,coinNum;
	public float pixDistance;
	public Lottery lottery;
	
	
	public GameWidget(GameScreen screen){
		this.screen=screen;
		
		Image img=new Image(screen.getTexture("res/miBar.png"));
		img.setPosition(430,492);
		addActor(img);
		
		 
		distanceLabel=new Label(""+realDistance, new LabelStyle(screen.getBitmapFont("font/scoreFont.fnt"), Color.WHITE));
		distanceLabel.setFontScale(0.5f);
		distanceLabel.setPosition(518-distanceLabel.getTextBounds().width/2,518);
		addActor(distanceLabel);
 
		Image img1=new Image(screen.getTexture("res/coinBar.png"));
		img1.setPosition(70,492);
		addActor(img1);
		
		coinNumLabel=new Label(""+StaticVariable.coinNum, new LabelStyle(screen.getBitmapFont("font/scoreFont.fnt"), Color.WHITE));
		coinNumLabel.setFontScale(0.5f);
		coinNumLabel.setPosition(179-coinNumLabel.getTextBounds().width/2,518);
		addActor(coinNumLabel);
		
		StaticVariable.rewardDistance=500;
		
		
	}
	
	@Override
	public void act(float delta) {
		if (!screen.control.isStopGame) {
			super.act(delta);
			distanceLabel.setPosition(518-distanceLabel.getTextBounds().width/2,518);
			coinNumLabel.setPosition(179-coinNumLabel.getTextBounds().width/2,518);
			
			realDistance=(int) (pixDistance/150);
			distanceLabel.setText(""+realDistance);
			if (realDistance>=StaticVariable.rewardDistance) {
				screen.changeState(GameState.pause);
				lottery=new Lottery(screen);
				DialogUtil.show(screen,lottery, ProcessType.empty);
			}
		}
	}
	
	public void addCoinNum(int num){
		this.coinNum+=num;
		StaticVariable.coinNum+=num;
		coinNumLabel.setText(""+StaticVariable.coinNum);
	}
	
	public void upDataCoin(){
		coinNumLabel.setText(""+StaticVariable.coinNum);
	}
	
}
