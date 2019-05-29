package com.jicent.jetrun.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.jicent.jetrun.data.DialogType;
import com.jicent.jetrun.data.StaticVariable;
import com.jicent.jetrun.extensions.ButtonEx;
import com.jicent.jetrun.extensions.ButtonEx.InputListenerEx;
import com.jicent.jetrun.extensions.ProcessEx.ProcessType;
import com.jicent.jetrun.screen.ShopScreen;
import com.jicent.jetrun.utils.DialogUtil;
import com.jicent.jetrun.utils.SoundUtil;

public class CoinBar extends Group{
	private ShopScreen screen;
	private Label numLabel;
	
	public CoinBar(final ShopScreen screen){
		this.screen=screen;
		this.setBounds(702,465, 251, 67);
		Image coinBg=new Image(screen.getTexture("res/coinBg.png"));
		coinBg.setPosition(16,0);
		addActor(coinBg);
		
		Image coinIconImg=new Image(screen.getTexture("res/coin.png"));
		coinIconImg.setPosition(1, 5);
		this.addActor(coinIconImg);
		
		numLabel=new Label(""+StaticVariable.coinNum, new LabelStyle(screen.getBitmapFont("font/allfont.fnt"), Color.WHITE));
		numLabel.setPosition(65, 34-numLabel.getPrefHeight()/2);
		addActor(numLabel);
		
		ButtonEx addBtn=new ButtonEx(screen, screen.getTexture("res/addBtn.png"));
		addBtn.setPosition(192, 3);
		addActor(addBtn);
		addBtn.addListener(new InputListenerEx() {
			
			@Override
			public void touchUp(Actor actor) {
				DialogUtil.show(screen, screen.dialog.getDialog(DialogType.coinGift), ProcessType.empty);
			}
			
			@Override
			public boolean touchDown(Actor actor) {
				SoundUtil.playSound(screen.main.getManager(), "button");
				return true;
			}
		});
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		numLabel.setText(""+StaticVariable.coinNum);
	}
}
