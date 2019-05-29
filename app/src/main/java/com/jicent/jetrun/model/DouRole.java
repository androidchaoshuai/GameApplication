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

public class DouRole extends Group {
	private GameScreen screen;
	private float timer,clearTimer;
	protected boolean clear;
	
	public DouRole(GameScreen screen) {
		this.screen=screen;
	}
	
	@Override
	public void act(float delta) {
		if (!screen.control.isStopGame&&!screen.hero.isStart()) {
			super.act(delta);
			if (clear&&clearTimer==0) {
				clearTimer+=delta;
				SnapshotArray<Actor> actors=getChildren();
				for (int i = 0,length=actors.size; i < length; i++) {
					SingleDou singleDou=(SingleDou) actors.get(i);
					singleDou.clear=true;
					singleDou.addAction(Actions.sequence(Actions.parallel(Actions.sequence(Actions.moveBy(480, 50, 0.2f),Actions.moveBy(480, -50, 0.2f)),Actions.rotateBy(5f)),Actions.removeActor()));
				}
			}else if (clearTimer!=0) {
				clearTimer+=delta;
				if (clearTimer>1f) {
					clearTimer=0;
					timer=0;
					clear=false;
				}
			}else {
				timer+=delta;
				if (timer>5f) {
					timer=MathUtils.random(-4,0);
					this.addActor(new SingleDou());
				}
			}
		}
		
	}
	
	class SingleDou extends Actor{
		protected boolean clear;
		private Animation anim;
		private float timer;
		private CollisionPolygon douPolygon;
		public int blood;
		
		public SingleDou() {
			TextureRegion[] regions=new TextureRegion[4];
			for (int i = 0; i < regions.length; i++) {
				regions[i]=new TextureRegion(screen.getTexture("res/dou"+i+".png"));
			}
			anim=new Animation(0.1f,regions);
			anim.setPlayMode(PlayMode.LOOP);
			setBounds(960, 30, 50, 70);
			douPolygon=new CollisionPolygon(new float[]{6,6,6,73,47,73,47,6}, getX(), getY());
			blood=2;
		}
		
		@Override
		public void act(float delta) {
			if (!screen.control.isStopGame) {
				super.act(delta);
				timer+=delta;
				moveBy(-(screen.speed+5), 0);
				if (getX()<-getWidth()||blood<=0) {
					this.remove();
				}else if(!clear){
					douPolygon.setPosition(getX(), getY());
//					if (!(screen.hero.getX()+screen.hero.getWidth()<getX()||screen.hero.getX()>getX()+getWidth())) {
//						if (douPolygon.overlaps(screen.hero.getPolygon())) {
//							//僵尸与人物碰撞
//							if (screen.hero.isShield()) {
//								screen.hero.setShield(false);
//							}else if (screen.hero.isRideMount()) {
//								screen.hero.setRideMount(false);
//							}else {
//								if(!screen.hero.isDeath())
//									screen.hero.setDeath(true);
//							}
//							this.remove();
//						}
//					}
				}
			}
		}
		
		@Override
		public void draw(Batch batch, float parentAlpha) {
			Color color = this.getColor();
			batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

			batch.draw(anim.getKeyFrame(timer), getX(), getY(), getWidth(), getHeight());
		}
		
		public boolean isCollisionBullet(CollisionPolygon polygon){
			if (douPolygon.overlaps(polygon)) {
				blood--;
				return true;
			}
			return false;
		}	
	}

}
