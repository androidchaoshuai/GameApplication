package com.jicent.jetrun.model;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.SnapshotArray;
import com.jicent.jetrun.extensions.AnimEx;
import com.jicent.jetrun.extensions.CollisionPolygon;
import com.jicent.jetrun.extensions.TextureEx;
import com.jicent.jetrun.model.StandBarrierControl.StandBarrier;
import com.jicent.jetrun.screen.GameScreen;
import com.jicent.jetrun.utils.InfoToast;
/*
 * 会旋转的障碍
 */
public class RotationBarrierControl extends Group{
	private GameScreen screen;
	private List<Rectangle> barrierList;
	protected boolean clear;
	private float clearTimer;

	public RotationBarrierControl(GameScreen screen) {
		this.screen=screen;
		barrierList=new LinkedList<Rectangle>();
	}
	
	protected void addBarrier(Rectangle rectangle) {
		barrierList.add(rectangle);
	}
	
	@Override
	public void act(float delta) {
		if (!screen.control.isStopGame) {
			super.act(delta);
			if (clear&&clearTimer==0) {
				clearTimer+=delta;
				SnapshotArray<Actor> actors=getChildren();
				for (int i = 0,length=actors.size; i < length; i++) {
					RotationBarrier rotationBarrier=(RotationBarrier)actors.get(i);
					if (rotationBarrier.getX()<=1000) {
						rotationBarrier.clear=true;
					}
				}
			}else if (clearTimer!=0) {
				clearTimer+=delta;
				if (clearTimer>1f) {
					clearTimer=0;
					clear=false;
				}
			}else {
				for (int i = barrierList.size()-1; i >=0; i--) {
					Rectangle rectangle=barrierList.get(i);
					float positionX=rectangle.x-screen.map.offset;
					if (positionX<1000&&positionX>960) {//相对位置进入屏幕时，加入相应的对象
						if (!screen.laser.isProductLaser&&!screen.laser.hasChildren()) {
							this.addActor(new RotationBarrier(positionX,rectangle.y,rectangle.width,rectangle.height));
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
	
	class RotationBarrier extends AnimEx{
		protected boolean clear;
		protected CollisionPolygon rotationPolygon;
		public int blood;
		private TextureRegion clearRegion;
		
		public RotationBarrier(float x, float y, float width, float height) {
			super(0.1f,new TextureEx[]{screen.getTexture("res/standBarrier0.png"),screen.getTexture("res/standBarrier1.png"),screen.getTexture("res/standBarrier2.png"),screen.getTexture("res/standBarrier3.png")},PlayMode.LOOP);
			clearRegion=new TextureRegion(screen.getTexture("res/standBarrier4.png"));
			setBounds(x, y, width, height);
			setOrigin(getWidth()/2, getHeight()/2);
			//x，y 方向上，碰撞范围需要调整的比例
			float scaleX=width/346;
			float scaleY=height/88;
			//根据加载的大小，动态调整碰撞范围
			rotationPolygon=new CollisionPolygon(new float[]{36*scaleX,32*scaleY, 302*scaleX,32*scaleY, 302*scaleX,61*scaleY, 36*scaleX,61*scaleY},getX(), getY(),getOriginX(), getOriginY());
			blood=5;
		}
		
		@Override
		public void act(float delta) {
			if (!screen.control.isStopGame) {
				super.act(delta);
				if (!screen.hero.isStart()) {
					moveBy(-screen.speed, 0);
					if (!clear) {
						rotateBy(2);
					}
					if (getX()<-getWidth()||blood<=0) {//相对位置移出屏幕时，移除相应的对象
						this.remove();
					}else if (!clear){
						rotationPolygon.setPosition(getX(), getY());
						rotationPolygon.setRotation(getRotation());
						if (!screen.hero.isSpeedUp()&&!screen.hero.isShake()&&!screen.hero.isDeath()) {
							if (!(screen.hero.getX()+screen.hero.getWidth()<getX()||screen.hero.getX()>getX()+getWidth())) {
								if (rotationPolygon.overlaps(screen.hero.getPolygon())) {
									
									this.remove();
									screen.bangControl.addBang(this.getX()+this.getWidth()/2, this.getY()+this.getHeight()/2);
									
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
			if (!clear&&rotationPolygon.overlaps(polygon)) {
				blood--;
				return true;
			}
			return false;
		}
	}
}
