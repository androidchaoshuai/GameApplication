package com.jicent.jetrun.model;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.jicent.jetrun.data.BuffType;
import com.jicent.jetrun.extensions.CollisionPolygon;
import com.jicent.jetrun.screen.GameScreen;
import com.jicent.jetrun.utils.SoundUtil;

public class BuffInGame extends Group{
	private GameScreen screen;
	private float timer=MathUtils.random(5,10);
	
	public BuffInGame(GameScreen screen) {
		this.screen =screen;
		
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		if (!screen.hero.isHaveGun()&&!screen.hero.isSpeedUp()&&!screen.hero.isRideMount()
				&& !screen.hero.isDeath()) {
			timer-=delta;
			if (timer<=0) {
				timer=MathUtils.random(5,10);
				addActor(new BuffStyle(MathUtils.random(4)));
				screen.speed+=0.5f;//出现道具的时候加速
			}
		}else {
			timer=MathUtils.random(5,10);
		}
	}
	
	class BuffStyle extends Group{
		private boolean up,speedAdd;
		private float ySpeed;
		private CollisionPolygon polygon;
		private BuffType type;
		
		public BuffStyle(int index) {
			this.setBounds(960, 100, 100, 100);
			polygon=new CollisionPolygon(new float[]{50,0,100,50,50,100,0,50});
			up=true;
			speedAdd=true;
			Image iconImg=null;
			switch (index) {
			case 0://吸金币
				type=BuffType.attract;
				iconImg=new Image(screen.getTexture("res/attractIcon.png"));
				break;
			case 1://保护罩
				type=BuffType.shield;
				iconImg=new Image(screen.getTexture("res/shieldIcon.png"));
				break;
			case 2://加速
				type=BuffType.speedUp;
				iconImg=new Image(screen.getTexture("res/speedUpIcon.png"));
				break;
			case 3://车
				type=BuffType.motor;
				iconImg=new Image(screen.getTexture("res/motorIcon.png"));
				break;
			case 4://枪
				type=BuffType.gun;
				iconImg=new Image(screen.getTexture("res/gunIcon.png"));
				break;
			default:
				break;
			}
			iconImg.setBounds(10,10,80,80);
			addActor(iconImg);
			
			Image roundImg=new Image(screen.getTexture("res/propRound.png"));
			roundImg.setBounds(-2, -2, 104, 104);
			addActor(roundImg);
			
			Image rotateImg=new Image(screen.getTexture("res/propRotate.png"));
			rotateImg.setBounds(-1.5f, -6.5f, 103, 113);
			rotateImg.setOrigin(51.5f,56.5f);
			rotateImg.addAction(Actions.forever(Actions.rotateBy(30)));
			addActor(rotateImg);
			
		}
		
		@Override
		public void act(float delta) {
			super.act(delta);
			if (getX()<-getWidth()) {
				remove();
			}else {
				if (up) {
					if (speedAdd) {
						ySpeed+=0.2f;
						moveBy(-(screen.speed-4), ySpeed);
						if (getY()>=170) {
							speedAdd=false;
						}
					}else {
						ySpeed-=0.2f;
						moveBy(-(screen.speed-4), ySpeed);
						if (ySpeed<=0) {
							ySpeed=0;
							up=false;
							speedAdd=true;
						}
					}
				} else {
					if (speedAdd) {
						ySpeed+=0.2f;
						moveBy(-(screen.speed-4), -ySpeed);
						if (getY()<=170) {
							speedAdd=false;
						}
					}else {
						ySpeed-=0.2f;
						moveBy(-(screen.speed-4), -ySpeed);
						if (ySpeed<=0) {
							ySpeed=0;
							up=true;
							speedAdd=true;
						}
					}
				}
				if (!screen.hero.isDeath()) {
					if (!(screen.hero.getX()+screen.hero.getWidth()<getX()||screen.hero.getX()>getX()+getWidth())) {
						polygon.setPosition(getX(), getY());
						if (polygon.overlaps(screen.hero.getPolygon())) {
							screen.control.addBuff(type);
							remove();
						}
					}
				}
			}
		}
	}
}
