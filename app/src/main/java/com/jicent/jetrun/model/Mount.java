package com.jicent.jetrun.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.esotericsoftware.spine.attachments.SkeletonAttachment;
import com.jicent.jetrun.screen.GameScreen;
import com.jicent.jetrun.utils.SoundUtil;
/*
 * 坐骑
 */
public class Mount extends SkeletonActor{
	private GameScreen screen;
	
	public Mount(GameScreen screen) {
		super(screen);
		this.screen=screen;
		setFile("mount/motor.atlas", "mount/motor.json");
	}
	//设置坐骑的动画
	public void setMountAnim(String name){
		if (name.equals("setUp")) {
			animationState.setAnimation(0,"setUp", false);
			SkeletonAttachment attachment=new SkeletonAttachment("motor");
			attachment.setSkeleton(skeleton);
			screen.hero.skeleton.findSlot("mount").setAttachment(attachment);
		}else if (name.equals("run")){
			animationState.setAnimation(0, "run", true);
			SoundUtil.playMountSound(screen.main.getManager(), "motor");
		}
	}
	 
	public void removeMount(){
		isInit=false;
		screen.hero.skeleton.setSlotsToSetupPose();
		screen.hero.skeleton.findSlot("mount").setAttachment(null);
		SoundUtil.stopMountSound();

		//爆炸声音，粒子效果
		SoundUtil.playSound(screen.main.getManager(), "motorCrush");
		screen.mountCrush.addCrush(screen.hero.getX()+screen.hero.getWidth()/2, screen.hero.getY()+ screen.hero.getHeight()/2);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		if (isInit) {
			Color color = this.getColor();
			skeleton.setColor(new Color(color.r, color.g, color.b, color.a * parentAlpha));
		}
	}
}
