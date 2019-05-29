package com.jicent.jetrun.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.SnapshotArray;
import com.jicent.jetrun.extensions.CollisionPolygon;
import com.jicent.jetrun.screen.GameScreen;
import com.jicent.jetrun.utils.InfoToast;
import com.jicent.jetrun.utils.SoundUtil;
/*
 * 导弹
 */
public class Missile extends Group{
	private GameScreen screen;
	private float timer;
	protected boolean clear;//是否清屏的标志
	private float clearTimer;
	
	public Missile(GameScreen screen) {
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
					Actor actor=actors.get(i);
					if (actor instanceof TanHao) {
						actor.remove();
					}else {
						SingleMissile sin=(SingleMissile)actor;
						sin.clear=true;
						sin.addAction(Actions.sequence(Actions.moveBy(960,540, 0.2f),Actions.removeActor()));
					}
				}
			}else if(clearTimer!=0){
				clearTimer+=delta;
				if (clearTimer>1f) {//清屏时间为1秒
					clearTimer=0;
					clear=false;
					timer=0;
				}
			}else {
				timer+=delta;
				if (timer>5) {
					timer=0;
					if (!screen.laser.isProductLaser&&!screen.laser.hasChildren()) {
						this.addActor(new TanHao(screen));
					}
				}
			}
		}
	}
	
	public void addMissile(float y){
		this.addActor(new SingleMissile(y));
	}
	
	class SingleMissile extends Actor{
		private Animation anim;
		private float timer;
		private CollisionPolygon missilePolygon;
		public int blood;
		protected boolean clear;
		
		public SingleMissile(float y) {
			TextureRegion[] regions=new TextureRegion[2];
			for (int i = 0; i < regions.length; i++) {
				regions[i]=new TextureRegion(screen.getTexture("res/missile"+i+".png"));
			}
			anim=new Animation(0.1f,regions);
			anim.setPlayMode(PlayMode.LOOP);
			setBounds(960, y+50-24, 184, 48);
			missilePolygon=new CollisionPolygon(new float[]{6,6,102,14,103,31,6,40}, getX(), getY());
			blood=5;
			SoundUtil.playSound(screen.main.getManager(), "missileO");
		}
		
		@Override
		public void act(float delta) {
			if (!screen.control.isStopGame) {
				super.act(delta);
				timer+=delta;
				moveBy(-(screen.speed+15), 0);
				if (getX()<-getWidth()||this.blood<=0) {
					this.remove();
				}else if(!clear){
					missilePolygon.setPosition(getX(), getY());
					if (!screen.hero.isSpeedUp()&&!screen.hero.isShake()&&!screen.hero.isDeath()) {
						if (!(screen.hero.getX()+screen.hero.getWidth()<getX()||screen.hero.getX()>getX()+getWidth())) {
							if (missilePolygon.overlaps(screen.hero.getPolygon())) {
								if (screen.hero.isShield()) {
									screen.hero.setShield(false);
//									this.remove();
								}else if (screen.hero.isRideMount()) {
									screen.hero.setRideMount(false);
								}else {
//									InfoToast.show(screen, "撞到了");
									screen.hero.setDeath(true);
//									this.remove();
								}
								this.remove();
								screen.bangControl.addBang(this.getX()+this.getWidth()/2, this.getY()+this.getHeight()/2);
							}
						}
					}
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
			if (missilePolygon.overlaps(polygon)) {
				blood--;
				return true;
			}
			return false;
		}
	}
}
