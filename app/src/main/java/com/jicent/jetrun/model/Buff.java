package com.jicent.jetrun.model;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.jicent.jetrun.data.BuffType;
import com.jicent.jetrun.data.HeroState;
import com.jicent.jetrun.data.StaticVariable;
import com.jicent.jetrun.extensions.AnimEx;
import com.jicent.jetrun.extensions.TextureEx;
import com.jicent.jetrun.screen.GameScreen;

public class Buff extends Group{
	private GameScreen screen;
	private BuffType type;
	private AnimEx buffImg;
	private Image frontImg;
	private float timer;

	public Buff(GameScreen screen, BuffType type) {
		this.screen=screen;
		this.type=type;
		TextureEx[] textureExs=null;
		switch (type) {
		case shield://盾
			textureExs = new TextureEx[3];
			for (int i = 0; i < textureExs.length; i++) {
				textureExs[i]=screen.getTexture("res/shield"+i+".png");
			}
			buffImg =new AnimEx(0.1f, textureExs, PlayMode.LOOP);
			buffImg.setPosition(screen.hero.getX()-50, screen.hero.getY()-16.5f);
			buffImg.setSize(150, 150); 
			screen.hero.setShield(true);
			this.addActor(buffImg);
			break;
		case speedUp:
			textureExs = new TextureEx[4];
			for (int i = 0; i < textureExs.length; i++) {
				textureExs[i]=screen.getTexture("res/speedUp"+i+".png");
			}
			buffImg =new AnimEx(0.1f, textureExs, PlayMode.LOOP);
			buffImg.setPosition(screen.hero.getX()-170f, screen.hero.getY()-43);
			screen.hero.setSpeedUp(true);
			this.addActor(buffImg);
			break;
		case attract:
			textureExs = new TextureEx[6];
			for (int i = 0; i < textureExs.length; i++) {
				textureExs[i]=screen.getTexture("res/attract"+i+".png");
			}
			buffImg =new AnimEx(0.1f, textureExs, PlayMode.LOOP);
			buffImg.setPosition(screen.hero.getX()-79, screen.hero.getY()-46);
			screen.hero.setAttract(true);
			this.addActor(buffImg);
			break;
		default:
			break;
		}
	}
	
	public void addFront(){
		frontImg=new Image(screen.getTexture("res/speedUpFront.png"));
		frontImg.setPosition(buffImg.getX()+173, buffImg.getY());
		screen.control.addActor(frontImg);
	}
	
	@Override
	public void act(float delta) {
		if (!screen.control.isStopGame) {
			super.act(delta);
			timer+=delta;
			switch (type) { 
			case shield:
				if (!screen.hero.isShield()) {
//				screen.hero.setShield(false);
					screen.hero.setShake(true);
					this.remove();
//					LogUtil.e(Buff.class.getSimpleName(), buffImg.getX()+"", true);
				}else {
					if (screen.hero.getHeroState()==HeroState.run) {
						buffImg.setPosition(screen.hero.getX()-43, screen.hero.getY()-16.5f);
					}else {
						buffImg.setPosition(screen.hero.getX()-50f, screen.hero.getY()-16.5f);
					}
				} 
				break;
			case speedUp:
				if (timer>5f) {
					screen.hero.setSpeedUp(false);
					screen.hero.setShake(true);
					frontImg.remove();
					this.remove();
				}else {
					buffImg.setPosition(screen.hero.getX()-170f, screen.hero.getY()-43);
					frontImg.setPosition(buffImg.getX()+173, buffImg.getY());
				}
				break;
			case attract:
				if (timer>5&&StaticVariable.roleKind!=StaticVariable.role4) {
					screen.hero.setAttract(false);
					this.remove();
				} else {
					buffImg.setPosition(screen.hero.getX()-79, screen.hero.getY()-46);
				}
				break;
			default:
				break;
			}
		}else{
			switch (type) { 
			case shield:
				if (screen.hero.getHeroState()==HeroState.run) {
					buffImg.setPosition(screen.hero.getX()-43, screen.hero.getY()-16.5f);
				}else {
					buffImg.setPosition(screen.hero.getX()-50f, screen.hero.getY()-16.5f);
				}
				break;
			case speedUp:
				buffImg.setPosition(screen.hero.getX()-170f, screen.hero.getY()-43);//游戏暂停，图片也跟着人
				frontImg.setPosition(buffImg.getX()+173, buffImg.getY());
				break;
			case attract:
				buffImg.setPosition(screen.hero.getX()-79, screen.hero.getY()-46);;//游戏暂停，图片也跟着人
				break;
			default:
				break;
			}
		}
	}
}
