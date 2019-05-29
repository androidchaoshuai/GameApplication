package com.jicent.jetrun.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.jicent.jetrun.data.BuffType;
import com.jicent.jetrun.data.DialogType;
import com.jicent.jetrun.data.GameState;
import com.jicent.jetrun.data.StaticVariable;
import com.jicent.jetrun.extensions.ButtonEx;
import com.jicent.jetrun.extensions.ButtonEx.InputListenerEx;
import com.jicent.jetrun.extensions.ProcessEx.ProcessType;
import com.jicent.jetrun.screen.GameScreen;
import com.jicent.jetrun.utils.DialogUtil;
import com.jicent.jetrun.utils.InfoToast;
import com.jicent.jetrun.utils.LayoutDebug;
import com.jicent.jetrun.utils.NextOperate;
import com.jicent.jetrun.utils.SPUtil;
import com.jicent.jetrun.utils.SoundUtil;

public class GameButton extends Group implements InputListenerEx{
	private GameScreen screen;
	private ButtonEx speedUpBtn,gunBtn,mountBtn,shieldBtn,attractBtn;
	private Label speedUpLabel,gunLabel,mountLabel,shieldLabel,attractLabel;
	private ButtonEx pauseBtn;
	
	private final int[] price={2000,1000,1000,800,200};
	
	public GameButton(GameScreen gameScreen) {
		this.screen=gameScreen;
		speedUpBtn=new ButtonEx(screen, screen.getTexture("res/speedUpBtn.png"));
		speedUpBtn.setBounds(660,10, 50, 45);
		speedUpBtn.addListener(this);
		this.addActor(speedUpBtn);
		speedUpLabel=new Label(""+StaticVariable.speedUpCount, new LabelStyle(screen.getBitmapFont("font/allfont.fnt"), Color.WHITE));
		speedUpLabel.setPosition(655,40);
		this.addActor(speedUpLabel);
		
		gunBtn=new ButtonEx(screen, screen.getTexture("res/gunBtn.png"));
		gunBtn.setBounds(720,10, 50, 45);
		gunBtn.addListener(this);
		this.addActor(gunBtn);
		gunLabel=new Label(""+StaticVariable.gunCount, new LabelStyle(screen.getBitmapFont("font/allfont.fnt"), Color.WHITE));
		gunLabel.setPosition(715,40);
		this.addActor(gunLabel);
		
		mountBtn=new ButtonEx(screen, screen.getTexture("res/mountBtn.png"));
		mountBtn.setBounds(780,10, 50, 45);
		mountBtn.addListener(this);
		this.addActor(mountBtn);
		mountLabel=new Label(""+StaticVariable.mountCount, new LabelStyle(screen.getBitmapFont("font/allfont.fnt"), Color.WHITE));
		mountLabel.setPosition(775,40);
		this.addActor(mountLabel);
		
		shieldBtn=new ButtonEx(screen, screen.getTexture("res/shieldBtn.png"));
		shieldBtn.setBounds(840,10, 50, 45);
		shieldBtn.addListener(this);
		this.addActor(shieldBtn);
		shieldLabel=new Label(""+StaticVariable.shieldCount, new LabelStyle(screen.getBitmapFont("font/allfont.fnt"), Color.WHITE));
		shieldLabel.setPosition(835,40);
		this.addActor(shieldLabel);
		
		attractBtn=new ButtonEx(screen, screen.getTexture("res/attractBtn.png"));
		attractBtn.setBounds(900,10, 50, 45);
		attractBtn.addListener(this);
		this.addActor(attractBtn);
		attractLabel=new Label(""+StaticVariable.attractCount, new LabelStyle(screen.getBitmapFont("font/allfont.fnt"), Color.WHITE));
		attractLabel.setPosition(895,40);
		this.addActor(attractLabel);
		
		pauseBtn=new ButtonEx(screen, screen.getTexture("res/pauseBtn.png"));
		pauseBtn.setBounds(890, 470, 60, 60);
		pauseBtn.addListener(this);
		this.addActor(pauseBtn);
		
		//价格label
		Label priceLabel=new Label(""+price[0], new LabelStyle(screen.getBitmapFont("font/allfont.fnt"), Color.WHITE));
		priceLabel.setFontScale(0.5f);
		priceLabel.setPosition(667,10);
		priceLabel.setTouchable(Touchable.disabled);
		this.addActor(priceLabel);
		
		priceLabel=new Label(""+price[1], new LabelStyle(screen.getBitmapFont("font/allfont.fnt"), Color.WHITE));
		priceLabel.setFontScale(0.5f);
		priceLabel.setPosition(726,10);
		priceLabel.setTouchable(Touchable.disabled);
		this.addActor(priceLabel);
		
		priceLabel=new Label(""+price[2], new LabelStyle(screen.getBitmapFont("font/allfont.fnt"), Color.WHITE));
		priceLabel.setFontScale(0.5f);
		priceLabel.setPosition(787,10);
		priceLabel.setTouchable(Touchable.disabled);
		this.addActor(priceLabel);
		
		priceLabel=new Label(""+price[3], new LabelStyle(screen.getBitmapFont("font/allfont.fnt"), Color.WHITE));
		priceLabel.setFontScale(0.5f);
		priceLabel.setPosition(851,10);
		priceLabel.setTouchable(Touchable.disabled);
		this.addActor(priceLabel);
		
		priceLabel=new Label(""+price[4], new LabelStyle(screen.getBitmapFont("font/allfont.fnt"), Color.WHITE));
		priceLabel.setFontScale(0.5f);
		priceLabel.setPosition(910,10);
		priceLabel.setTouchable(Touchable.disabled);
		this.addActor(priceLabel);
	}

	@Override
	public boolean touchDown(Actor actor) {
		if (screen.hero.isSpeedUp()||screen.hero.isDeath()||screen.hero.isStart()) {
			return false;
		}
		SoundUtil.playSound(screen.main.getManager(), "button");
		return true;
	}
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		speedUpLabel.setText(""+StaticVariable.speedUpCount);
		gunLabel.setText(""+StaticVariable.gunCount);
		mountLabel.setText(""+StaticVariable.mountCount);
		shieldLabel.setText(""+StaticVariable.shieldCount);
		attractLabel.setText(""+StaticVariable.attractCount);
	}
	@Override
	public void touchUp(Actor actor) {
		if (actor==speedUpBtn) {
			if (!screen.hero.isSpeedUp()&&!screen.hero.isRideMount()&&!screen.hero.isHaveGun()) {
				if(StaticVariable.speedUpCount>0){
					StaticVariable.speedUpCount-=1;
					SPUtil.commit(screen.main.getSp(), "speedUpCount", StaticVariable.speedUpCount);
					screen.control.addBuff(BuffType.speedUp);
				}else{
					if(StaticVariable.coinNum>=price[0]){
						StaticVariable.coinNum-=price[0];
						SPUtil.commit(screen.main.getSp(), "coinNum", StaticVariable.coinNum);		
						screen.control.addBuff(BuffType.speedUp);
						InfoToast.show(screen, "购买成功，消耗"+price[0]+"金币");
					}else{
						InfoToast.show(screen, "金币不足，不如直接买点道具吧！");
						//金币不够弹道具礼包
						screen.changeState(GameState.pause);
						DialogUtil.show(screen, screen.dialog.getDialog(DialogType.propGift), ProcessType.empty);	
					}
				}
			}
		}else if (actor==gunBtn) {
			if (!screen.hero.isHaveGun()&&!screen.hero.isSpeedUp()&&!screen.hero.isRideMount()) {
				if(StaticVariable.gunCount>0){
					StaticVariable.gunCount-=1;
					SPUtil.commit(screen.main.getSp(), "gunCount", StaticVariable.gunCount);
					screen.control.addBuff(BuffType.gun);
				}else{
					if(StaticVariable.coinNum>=price[1]){
						StaticVariable.coinNum-=price[1];
						SPUtil.commit(screen.main.getSp(), "coinNum", StaticVariable.coinNum);		
						screen.control.addBuff(BuffType.gun);
						InfoToast.show(screen, "购买成功，消耗"+price[1]+"金币");
					}else{
						InfoToast.show(screen, "金币不足，不如直接买点道具吧！");
						//金币不够弹道具礼包
						screen.changeState(GameState.pause);
						DialogUtil.show(screen, screen.dialog.getDialog(DialogType.propGift), ProcessType.empty);	
					}
				}
			}
		}else if (actor==shieldBtn) {
			if (!screen.hero.isShield()) {
				if(StaticVariable.shieldCount>0){
					StaticVariable.shieldCount-=1;
					SPUtil.commit(screen.main.getSp(), "shieldCount", StaticVariable.shieldCount);
					screen.control.addBuff(BuffType.shield);
				}else{
					if(StaticVariable.coinNum>=price[2]){
						StaticVariable.coinNum-=price[2];
						SPUtil.commit(screen.main.getSp(), "coinNum", StaticVariable.coinNum);		
						screen.control.addBuff(BuffType.shield);
						InfoToast.show(screen, "购买成功，消耗"+price[2]+"金币");
					}else{
						InfoToast.show(screen, "金币不足，不如直接买点道具吧！");
						//金币不够弹道具礼包
						screen.changeState(GameState.pause);
						DialogUtil.show(screen, screen.dialog.getDialog(DialogType.propGift), ProcessType.empty);	
					}
				}
			}
		}else if (actor==mountBtn) {
			if (!screen.hero.isHaveGun()&&!screen.hero.isSpeedUp()&&!screen.hero.isRideMount()) {
				if(StaticVariable.mountCount>0){
					StaticVariable.mountCount-=1;
					SPUtil.commit(screen.main.getSp(), "mountCount", StaticVariable.mountCount);
					screen.control.addBuff(BuffType.motor);
				}else{
					if(StaticVariable.coinNum>=price[3]){
						StaticVariable.coinNum-=price[3];
						SPUtil.commit(screen.main.getSp(), "coinNum", StaticVariable.coinNum);		
						screen.control.addBuff(BuffType.motor);
						InfoToast.show(screen, "购买成功，消耗"+price[3]+"金币");
					}else{
						InfoToast.show(screen, "金币不足，不如直接买点道具吧！");
						//金币不够弹道具礼包
						screen.changeState(GameState.pause);
						DialogUtil.show(screen, screen.dialog.getDialog(DialogType.propGift), ProcessType.empty);	
					}
				}
			}
		}else if (actor==attractBtn) {
			if (!screen.hero.isAttract()) {
				if(StaticVariable.attractCount>0){
					StaticVariable.attractCount-=1;
					SPUtil.commit(screen.main.getSp(), "gunCount", StaticVariable.attractCount);
					screen.control.addBuff(BuffType.attract);
				}else{
					if(StaticVariable.coinNum>=price[4]){
						StaticVariable.coinNum-=price[4];
						SPUtil.commit(screen.main.getSp(), "coinNum", StaticVariable.attractCount);		
						screen.control.addBuff(BuffType.attract);
						InfoToast.show(screen, "购买成功，消耗"+price[4]+"金币");
					}else{
						InfoToast.show(screen, "金币不足，不如直接买点道具吧！");
						//金币不够弹道具礼包
						screen.changeState(GameState.pause);
						DialogUtil.show(screen, screen.dialog.getDialog(DialogType.propGift), ProcessType.empty);	
					}
				}
			}
		}else if (actor==pauseBtn&&!screen.hero.isDeath()) {
			screen.changeState(GameState.pause);
			DialogUtil.show(screen, screen.dialog.getDialog(DialogType.pause), ProcessType.empty);
		}
	}

}
