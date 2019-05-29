package com.jicent.jetrun.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.spine.AnimationState.AnimationStateAdapter;
import com.esotericsoftware.spine.Event;
import com.esotericsoftware.spine.attachments.SkeletonAttachment;
import com.jicent.jetrun.data.StaticVariable;
import com.jicent.jetrun.screen.GameScreen;
import com.jicent.jetrun.utils.SoundUtil;

public class Gun extends SkeletonActor{
	private GameScreen screen;
	private float timer=0,shootTime=0;
	private boolean isStartShoot;
	
	private Vector2[] bulletOffset={new Vector2(0, 0),
									new Vector2(0, 10),
									new Vector2(0, 8),
									new Vector2(0, 5),
									new Vector2(0, 5)};
	
	public Gun(GameScreen screen) {
		super(screen);
		this.screen=screen; 
		setFile("gun/gun.atlas", "gun/gun.json");
	}
	 
	//设置枪的动画 
	public void setGunAnim(String name){
			if (name.equals("setup")) {
				animationState.setAnimation(0,name, false);
				SkeletonAttachment attachment=new SkeletonAttachment("gun");
				attachment.setSkeleton(skeleton);
				screen.hero.skeleton.findSlot("gun").setAttachment(attachment);
			}else if(name.equals("shoot")){
				isStartShoot=true;
				timer=0; 
				animationState.setAnimation(0, name, true);
//				animationState.addListener(new AnimationStateAdapter() {
//					@Override
//					public void event(int trackIndex, Event event) {
//						//发射子弹
//						if (event.getData().getName().equals("bullet")) {
//							switch (screen.hero.getHeroState()) {
//							case run:
//								screen.bullet.addBullet(screen.hero.skeleton.getX()+55,screen.hero.skeleton.getY()+42);
//								break;
//							case up:
//								screen.bullet.addBullet(screen.hero.skeleton.getX()+55,screen.hero.skeleton.getY()+47);
//								break;
//							case down:
//								screen.bullet.addBullet(screen.hero.skeleton.getX()+55,screen.hero.skeleton.getY()+45);
//								break;
//							default:
//								break;
//							}
//						}
//					}
//				});
			}
	}
	public void removeGun(){
//		isInit=false;
		screen.hero.skeleton.findSlot("gun").setAttachment(null);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		if (isInit) {
			Color color = this.getColor();
			skeleton.setColor(new Color(color.r, color.g, color.b, color.a * parentAlpha));
		}
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		if (isInit&&isStartShoot) {
			timer+=delta;
			  
			if(!screen.hero.isDeath()){
				shootTime-=delta;
				if(shootTime<=0){
					shootTime=0.08f;
					SoundUtil.playSound(screen.main.getManager(), "bullet");
					switch (screen.hero.getHeroState()) {
					case run:
						screen.bullet.addBullet(screen.hero.skeleton.getX()+53+bulletOffset[StaticVariable.roleKind].x,screen.hero.skeleton.getY()+31+bulletOffset[StaticVariable.roleKind].y);
						break;
					case up:
						screen.bullet.addBullet(screen.hero.skeleton.getX()+53+bulletOffset[StaticVariable.roleKind].x,screen.hero.skeleton.getY()+37+bulletOffset[StaticVariable.roleKind].y);
						break;
					case down:
						screen.bullet.addBullet(screen.hero.skeleton.getX()+53+bulletOffset[StaticVariable.roleKind].x,screen.hero.skeleton.getY()+35+bulletOffset[StaticVariable.roleKind].y);
						break;
					default:
						break;
					}
				}
			}
			
			if (timer>10) {
				screen.hero.setHaveGun(false);
				isStartShoot=false;
			}
		}
		
	}
}
