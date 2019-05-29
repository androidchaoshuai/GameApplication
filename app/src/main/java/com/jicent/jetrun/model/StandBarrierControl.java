package com.jicent.jetrun.model;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.SnapshotArray;
import com.jicent.jetrun.extensions.AnimEx;
import com.jicent.jetrun.extensions.CollisionPolygon;
import com.jicent.jetrun.extensions.TextureEx;
import com.jicent.jetrun.screen.GameScreen;

public class StandBarrierControl extends Group{
	private GameScreen screen;
	private List<StandBarrierProperties> barrierList;
	protected boolean clear;//骑到车或者捡到枪后，是否需要清屏
	private float clearTime;//清屏持续时间

	public StandBarrierControl(GameScreen screen) {
		this.screen=screen;
		barrierList=new LinkedList<StandBarrierProperties>();
	}
	
	protected void addBarrier(StandBarrierProperties properties) {
		barrierList.add(properties);
	}
	
	@Override
	public void act(float delta) {
		if (!screen.control.isStopGame) {
			super.act(delta);
			if (clear&&clearTime==0) {//首次清屏
				clearTime+=delta;
				SnapshotArray<Actor> actors=getChildren();
				for (int i = 0,length=actors.size; i < length; i++) {
					StandBarrier standBarrier=(StandBarrier)actors.get(i);
					if (standBarrier.getX()<=1000) {
						standBarrier.clear=true;
					}
				}
			}else if (clearTime!=0) {//计算清屏时间，该时间内，障碍物不出现
				clearTime+=delta;
				if (clearTime>1f) {
					clearTime=0;
					clear=false;
				}
			}else {
				for (int i = barrierList.size()-1; i >=0; i--) {
					StandBarrierProperties properties=barrierList.get(i);
					float positionX=properties.getX()-screen.map.offset;
					if (positionX<1000&&positionX>960) {//相对位置进入屏幕时，加入相应的对象
						if (!screen.laser.isProductLaser&&!screen.laser.hasChildren()) {
							this.addActor(new StandBarrier(positionX,properties.getY(),properties.getWidth(),properties.getHeight(),properties.getRotation()));
						}
						barrierList.remove(i);
					}else if (positionX<=960) {
						barrierList.remove(i);
					}
				}
			}
		}
	}
	
	public void dispose(){
		barrierList.clear();
		barrierList=null;
	}
	
	class StandBarrier extends AnimEx{
		protected boolean clear;
		protected CollisionPolygon standPolygon;
		public int blood;
		private TextureRegion clearRegion;
		
		public StandBarrier(float x, float y, float width, float height, float rotation) {
			super(0.1f,new TextureEx[]{screen.getTexture("res/standBarrier0.png"),screen.getTexture("res/standBarrier1.png"),screen.getTexture("res/standBarrier2.png"),screen.getTexture("res/standBarrier3.png")},PlayMode.LOOP);
			clearRegion=new TextureRegion(screen.getTexture("res/standBarrier4.png"));
			setBounds(x, y, 164, 50);
			setOrigin(0, getHeight());
			setRotation(rotation);
			//x，y 方向上，碰撞范围需要调整的比例
			float scaleX=width/346;
			float scaleY=height/88;
			standPolygon=new CollisionPolygon(new float[]{36*scaleX,32*scaleY, 302*scaleX,32*scaleY, 302*scaleX,61*scaleY, 36*scaleX,61*scaleY},getX(), getY(),0, getHeight(),rotation);
			blood=5;
		}
		
		@Override
		public void act(float delta) {
			if (!screen.control.isStopGame) {
				super.act(delta);
				if (!screen.hero.isStart()) {
					moveBy(-screen.speed, 0);
					if (getX()<-getWidth()||blood<=0) {//相对位置移出屏幕时，移除相应的对象
						this.remove();
					}else if(!clear){
						standPolygon.setPosition(getX(), getY());
						if (!screen.hero.isSpeedUp()&&!screen.hero.isShake()&&!screen.hero.isDeath()) {
							if (!(screen.hero.getX()+screen.hero.getWidth()<getX()||screen.hero.getX()>getX()+getWidth())) {
								if (standPolygon.overlaps(screen.hero.getPolygon())) {
									
									this.remove();
									screen.bangControl.addBang(this.getX()+this.getWidth()/2, this.getY()+this.getHeight()/2);
									
									if (screen.hero.isShield()) {
										screen.hero.setShield(false);
									}else if (screen.hero.isRideMount()) {
										screen.hero.setRideMount(false);
									}else {
										screen.hero.setDeath(true);
									}
								}
							}
						}
					}
				}
			}
		}
		
		@Override
		public void draw(Batch batch, float parentAlpha) {
			if (clear) {
				Color color = this.getColor();
				batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
				
				if (getWidth()==0||getHeight()==0) {
					batch.draw(clearRegion, getX(), getY(), getOriginX(), getOriginY(), clearRegion.getRegionWidth(), clearRegion.getRegionHeight(), getScaleX(), getScaleY(), getRotation());
				}else {
					batch.draw(clearRegion, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
				}
			} else {
				super.draw(batch, parentAlpha);
			}
			
		}
		
		public boolean isCollisionBullet(CollisionPolygon polygon){
			if (!clear&&standPolygon.overlaps(polygon)) {
				blood--;
				return true;
			}
			return false;
		}
	}
}
