package com.jicent.jetrun.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.SnapshotArray;
import com.jicent.jetrun.extensions.CollisionPolygon;
import com.jicent.jetrun.screen.GameScreen;

/*
 * 地上行走的刺猬
 */

public class Spikeweed extends Group{
	private GameScreen screen;
	private float timer;
	private float clearTimer;//清屏时间
	protected boolean clear;//捡到枪或者车的道具后，清理屏幕
	
	private float addTime=MathUtils.random(5, 8);
	
	public Spikeweed(GameScreen screen) {
		this.screen=screen;
	}
	
	@Override
	public void act(float delta) {
		if (!screen.control.isStopGame) {
			super.act(delta);
			if (clear&&clearTimer==0) {
				clearTimer+=delta;
				SnapshotArray<Actor> actors=getChildren();
				for (int i = 0,length=actors.size; i < length; i++) {
					SingleSpikeweed spikeweed=(SingleSpikeweed) actors.get(i);
					spikeweed.clear=true;
					spikeweed.addAction(Actions.sequence(Actions.moveBy(480, -50, 0.2f),Actions.removeActor()));
				}
			}else if (clearTimer!=0) {
				clearTimer+=delta;
				if (clearTimer>1f) {//清屏时间为1秒
					clearTimer=0;
					clear=false;
					timer=0;
				}
			}else {
				timer+=delta;
				if (timer>addTime&&screen.laser.isLaserOut) {
					timer=0;
					addTime-=0.1;
					this.addActor(new SingleSpikeweed());
				}
			}
		}
	}
	
	class SingleSpikeweed extends Actor{
		private Animation anim;
		private float timer;
		protected boolean clear;
		private CollisionPolygon spikeweedPolygon;
		
		public SingleSpikeweed() {
			TextureRegion[] regions=new TextureRegion[3];
			for (int i = 0; i < regions.length; i++) {
				regions[i]=new TextureRegion(screen.getTexture("res/spikeweed"+i+".png"));
			}
			anim=new Animation(0.15f, regions);
			anim.setPlayMode(PlayMode.LOOP);
			setBounds(960, 21, 74, 54);
			spikeweedPolygon=new CollisionPolygon(new float[]{9,6,64,3,65,30,10,36}, getX(), getY());
		}
		
		@Override
		public void draw(Batch batch, float parentAlpha) {
			Color color = this.getColor();
			batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
			batch.draw(anim.getKeyFrame(timer), getX(), getY(),getWidth(),getHeight());
		}
		
		@Override
		public void act(float delta) {
			if (!screen.control.isStopGame) {
				super.act(delta);
				timer+=delta;
				moveBy(-screen.speed, 0);
				if (getX()<-getWidth()) {
					this.remove();
				}else if(!clear){
					if (!screen.hero.isSpeedUp()&&!screen.hero.isShake()&&!screen.hero.isDeath()) {
						if (!(screen.hero.getX()+screen.hero.getWidth()<getX()||screen.hero.getX()>getX()+getWidth())) {
							spikeweedPolygon.setPosition(getX(), getY());
							if (spikeweedPolygon.overlaps(screen.hero.getPolygon())) {
								if (screen.hero.isShield()) {
									screen.hero.setShield(false);
								}else if (screen.hero.isRideMount()) {
									screen.hero.setRideMount(false);
								}else {
//									InfoToast.show(screen, "撞到了");
									screen.hero.setDeath(true);
								}
							}
						}
					}
				}
			}
		}
	}

}
