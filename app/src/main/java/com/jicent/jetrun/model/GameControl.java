package com.jicent.jetrun.model;


import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.jicent.jetrun.data.BuffType;
import com.jicent.jetrun.data.DialogType;
import com.jicent.jetrun.data.GameState;
import com.jicent.jetrun.data.HeroState;
import com.jicent.jetrun.extensions.ProcessEx.ProcessType;
import com.jicent.jetrun.model.Background.Layer;
import com.jicent.jetrun.screen.GameScreen;
import com.jicent.jetrun.utils.DialogUtil;
import com.jicent.jetrun.utils.SoundUtil;

public class GameControl extends Group {
	private GameScreen screen;
	protected int touchCount;
	protected List<Float> trackList;//记录英雄移动的轨迹，便于设计宠物的移动轨迹
	public boolean isStopGame;
	private float speedOffset;

	public GameControl(final GameScreen screen) {
		this.screen=screen;
		this.setBounds(0, 0, 960, 540);
		trackList=new LinkedList<Float>();
		if(screen.pet!=null)
			for (int i = 0; i < 5; i++) {
				trackList.add(screen.pet.getY());//宠物和英雄移动的时间差
			}
		this.addListener(new InputListener(){
			
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				if (screen.hero.isSpeedUp()||screen.hero.isDeath()||screen.hero.isStart()) {
					return false;
				} else {
					if (touchCount==0) {
						screen.hero.setState(HeroState.up);
					}
					touchCount++;
					return true;
				}
			}
			
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if (touchCount>0&&!screen.hero.isDeath()&&!screen.hero.isStart()) {//如果touchCount==0，再减就不会响应touchDown了
					touchCount--;
					if (touchCount==0) {
						if (!screen.hero.isRideMount()) {
							screen.hero.setState(HeroState.down);
						}
					}
				}
			}
		});
	}
	//防止在手指未抬起时，游戏暂停，之后返回游戏角色继续向上飞的bug
	public void releaseTouchUp(){
		touchCount=0;
		if (!screen.hero.isRideMount()) {
			if (screen.hero.getHeroState()==HeroState.up) {
				screen.hero.setState(HeroState.down);
			}
		}
	}
	
	public void addBuff(BuffType type){
		if (!screen.hero.isDeath()) {
			
			if (type==BuffType.speedUp) { 
				if (!screen.hero.isSpeedUp()&&!screen.hero.isRideMount()&&!screen.hero.isHaveGun()) {
					screen.control.isStopGame=true;
					screen.hintLight.addHint(type);
					SoundUtil.playSound(screen.main.getManager(), "buff");
					
					Buff buff = new Buff(screen, type);
					screen.bg.addActorBefore(screen.hero, buff);
					buff.addFront();
				}
			}else if (type==BuffType.gun) {
				//由于按钮有个效果，点击按钮的时候，可能角色没有处于下列状态，按钮效果播放完毕后，就已经在这个状态了，所以需要判断
				if (!screen.hero.isHaveGun()&&!screen.hero.isSpeedUp()&&!screen.hero.isRideMount()) {
					screen.control.isStopGame=true;
					screen.hintLight.addHint(type);
					SoundUtil.playSound(screen.main.getManager(), "buff");
					
					screen.changeState(GameState.running);
					//拾取对应种类的枪
					screen.hero.setHaveGun(true);
				}
			}else if (type==BuffType.motor) {
				if (!screen.hero.isHaveGun()&&!screen.hero.isSpeedUp()&&!screen.hero.isRideMount()) {
					screen.control.isStopGame=true;
					screen.hintLight.addHint(type);
					SoundUtil.playSound(screen.main.getManager(), "buff");
					
					screen.changeState(GameState.running);
					screen.hero.setRideMount(true);
				}
			}else if(type==BuffType.shield){
				if (!screen.hero.isShield()) {
					screen.control.isStopGame=true;
					screen.hintLight.addHint(type);
					SoundUtil.playSound(screen.main.getManager(), "buff");
					
					addActor(new Buff(screen, type));
				}
			}else if (type==BuffType.attract) {
				if (!screen.hero.isAttract()) {
					screen.control.isStopGame=true;
					screen.hintLight.addHint(type);
					SoundUtil.playSound(screen.main.getManager(), "buff");
					
					screen.bg.addActorBefore(screen.hero, new Buff(screen, type));
				}
			}
		}
	}
	
	//设置加速
	public void addSpeed(float addFatcor){
		speedOffset=addFatcor;//记录速度的增量，方便恢复速度使用
		screen.speed+=addFatcor;
	}
	
	//恢复速度
	public void resumeSpeed(){
		screen.speed-=speedOffset;
		speedOffset=0;
	}
	
	public void dispose(){
		trackList.clear();
		trackList=null;
	}
}
