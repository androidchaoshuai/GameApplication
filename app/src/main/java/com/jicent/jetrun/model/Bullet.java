package com.jicent.jetrun.model;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.SnapshotArray;
import com.jicent.jetrun.extensions.CollisionPolygon;
import com.jicent.jetrun.model.DouRole.SingleDou;
import com.jicent.jetrun.model.Missile.SingleMissile;
import com.jicent.jetrun.model.RotationBarrierControl.RotationBarrier;
import com.jicent.jetrun.model.StandBarrierControl.StandBarrier;
import com.jicent.jetrun.screen.GameScreen;

public class Bullet extends Group{
	private GameScreen screen;
//	private List<SingleBullet> list;
	
	public Bullet(GameScreen screen){
		this.screen=screen;
//		list=new LinkedList<Bullet.SingleBullet>();
	}
	
	public void addBullet(float x, float y){
		addActor(new SingleBullet(x,y));
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
//		for (int i = list.size()-1; i >=0; i--) {
//			
//		}
	}
	
	class SingleBullet extends Image{
		private CollisionPolygon polygon;
		
		public SingleBullet(float x, float y){
			super(screen.getTexture("res/gunBullet.png"));
			setPosition(x, y);
			polygon=new CollisionPolygon(new float[]{0,0,24,0,24,9,0,9}, x, y);
		}
		
		@Override
		public void act(float delta) {
			super.act(delta);
			this.moveBy(15, 0);
			if (getX()>960) {
				this.remove();
			}else {
				//碰撞检测
				//枪可以打爆导弹
				polygon.setPosition(getX(), getY());
				SnapshotArray<Actor> array=screen.missile.getChildren();
				for (int i = 0,length=array.size; i < length; i++) {
					Actor actor=array.get(i);
					if (actor instanceof SingleMissile) {
						SingleMissile missile=(SingleMissile)actor;
						if (missile.isCollisionBullet(polygon)) {
							this.remove();
							if(missile.blood<=0){
								screen.bangControl.addBang(missile.getX()+missile.getWidth()/2, missile.getY()+missile.getHeight()/2);
							}
						}
					}
				}
				//枪可以打爆旋转障碍物
				array=screen.rotationBarrierControl.getChildren();
				for (int i = 0,length=array.size; i < length; i++) {
					RotationBarrier rotationBarrier=(RotationBarrier)array.get(i);
					if (rotationBarrier.isCollisionBullet(polygon)) {
						this.remove();
						if(rotationBarrier.blood<=0){
							screen.bangControl.addBang(rotationBarrier.getX()+rotationBarrier.getWidth()/2, rotationBarrier.getY()+rotationBarrier.getHeight()/2);
						}
					}
				}
				//枪可以打爆固定障碍物
				array=screen.standBarrierControl.getChildren();
				for (int i = 0,length=array.size; i < length; i++) {
					StandBarrier standBarrier=(StandBarrier)array.get(i);
					if (standBarrier.isCollisionBullet(polygon)) {
						this.remove();
						if(standBarrier.blood<=0){
							screen.bangControl.addBang(standBarrier.getX()+standBarrier.getWidth()/2, standBarrier.getY()+standBarrier.getHeight()/2);
						}
					}
				}
				//枪可以打爆doudou
				array=screen.dou.getChildren();
				for (int i = 0,length=array.size; i < length; i++) {
					SingleDou singleDou=(SingleDou)array.get(i);
					if (singleDou.isCollisionBullet(polygon)) {
						this.remove();
						if(singleDou.blood<=0){
							screen.bangControl.addBang(singleDou.getX()+singleDou.getWidth()/2, singleDou.getY()+singleDou.getHeight()/2);
						}
					}
				}
			}
		}
	}
}
