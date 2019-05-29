package com.jicent.jetrun.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.jicent.jetrun.data.AnimationKind;
import com.jicent.jetrun.data.BuffType;
import com.jicent.jetrun.data.DialogType;
import com.jicent.jetrun.data.GameState;
import com.jicent.jetrun.data.HeroState;
import com.jicent.jetrun.data.StaticVariable;
import com.jicent.jetrun.extensions.ProcessEx.ProcessType;
import com.jicent.jetrun.screen.GameScreen;
import com.jicent.jetrun.utils.AnimateFactory;
import com.jicent.jetrun.utils.DialogUtil;
import com.jicent.jetrun.utils.InfoToast;
import com.jicent.jetrun.utils.LogUtil;
import com.jicent.jetrun.utils.SPUtil;
import com.jicent.jetrun.utils.SoundUtil;

public class Hero extends SkeletonActor {
	private GameScreen screen;
	private HeroState heroState;
	private float ySpeed;
	private Rectangle rect=new Rectangle();
	private boolean isShield,isSpeedUp,isAttract;
	private boolean isShake,isDeath;
	private boolean isRideMount,haveGun;
	private float deathYSpeed,bgReduceStep;
	private boolean isStart;
	private float petOffsetY,heroLastY;//宠物上下抖动的偏移量，角色前一帧的y的坐标
	private boolean petUp; 
	
	//技能相关
	int nextGetPropD=1000;//下次获得护盾的距离
	boolean hasRevive=true;//是否有复活
	
	private static String[] rolesName={"freeRole","loliRole","catRole","bunnyRole","chinaRole"};
	
	public Hero(final GameScreen screen) { 
		super(screen, "role/"+rolesName[StaticVariable.roleKind]+".atlas", "role/"+rolesName[StaticVariable.roleKind]+".json");
		this.screen = screen;
		
		
		isStart=true;
		setState(HeroState.run);
		setPosition(-70, 30);
		setSize(50, 80);
		
		addAction(Actions.sequence(Actions.moveTo(250, 30,1f),Actions.run(new Runnable() {
			
			@Override
			public void run() {
				isStart=false;

				//技能相关开场飞行
				if(StaticVariable.roleKind==StaticVariable.role3){
					InfoToast.show(screen, "角色技能：开场加速");
					screen.control.addBuff(BuffType.speedUp);	
				}
				//技能相关永久吸金
				if(StaticVariable.roleKind==StaticVariable.role4){
					InfoToast.show(screen, "角色技能：永久吸金");
					screen.control.addBuff(BuffType.attract);	
				}
			}
		})));
		
		
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		if (!screen.control.isStopGame) {
			if (!isSpeedUp&&!isDeath&&!isStart) {
				positionControl();
			}else if (isDeath) {
				deathMove();
			}
			
			if (heroLastY==getY()&&heroState!=HeroState.up&&heroState!=HeroState.down) {//让宠物的轨迹进行上下的抖动，只有在角色在竖直坐标上没有变化的情况下
				screen.control.trackList.add(petShake());
			}else {
				screen.control.trackList.add(getY()+70);
				heroLastY=getY();
			}
		}
		
		//技能相关
		if(StaticVariable.roleKind==StaticVariable.role1){
			if(screen.widget.realDistance>nextGetPropD){
				InfoToast.show(screen, "角色技能：获得一个护盾");
				StaticVariable.shieldCount+=1;
				SPUtil.commit(screen.main.getSp(), "shieldCount", StaticVariable.shieldCount);
				nextGetPropD+=1000;
			}
		}
		
		
	}
	private float petShake(){
		if (petUp) {
			petOffsetY+=1;
			if (petOffsetY>4) {
				petUp=false;
			}
		}else {
			petOffsetY-=1f;
			if (petOffsetY<-4) {
				petUp=true;
			}
		}
		return getY()+70+petOffsetY;
	}

//	public boolean couldTouch(){
//		return !isStart&&!isSpeedUp&&!isDeath;
//	}
	
	private void deathMove() {
		if (deathYSpeed>0) {
			if (ySpeed>0) {
				ySpeed-=0.6f;
				this.moveBy(0, ySpeed);
			}else {
				ySpeed-=0.6f;
				this.moveBy(0, ySpeed);
				if (this.getY()<30) {
					deathYSpeed-=4;
					if (deathYSpeed<=0) { 
						setAnim(AnimationKind.death, false);
					}else { 
						ySpeed=deathYSpeed; 
					}
				}
			}
		}else {
			screen.speed-=bgReduceStep;
			if (screen.speed<0) {
				screen.speed=0;
				
				//技能相关
				if(hasRevive&&StaticVariable.roleKind==StaticVariable.role2){
					InfoToast.show(screen, "角色技能：免费复活一次");
					
					screen.changeState(GameState.running);
					screen.speed=6;
					screen.hero.setDeath(false);
					screen.hero.skeleton.setSlotsToSetupPose();
					screen.control.addBuff(BuffType.speedUp);	
					
					hasRevive=false;
				}else{
					screen.changeState(GameState.pause);
					DialogUtil.show(screen, screen.dialog.getDialog(DialogType.revieve), ProcessType.empty);
					System.out.println("dialog");
				}
			}
		}
	}

	//人物动画的控制
	private void animControl(){
		if (haveGun) {//如果人物拿枪，设置拿枪时的上，下，跑的动画
			gunAnimContorl();
		}else if (isRideMount) {//如果人物骑坐骑，设置骑坐骑时的上，下，跑的动画
			mountAnimControl();
		}else {//设置普通的上，下，跑的动画
			normalAnimControl();
		}
	}
	
	//人物拿枪的动画控制
	private void gunAnimContorl() {
		switch (heroState) {
		case down:
			setAnim(AnimationKind.gunDown, false);
			break;
		case run:
			setAnim(AnimationKind.gunRun, true);
			break;
		case up:
			skeleton.setBonesToSetupPose();//重置为初始位置
			animationState.addAnimation(0, AnimationKind.gunUp.name(), true, 0);
			break;
		default:break;
		}
	}

	//人物骑坐骑的动画控制
	private void mountAnimControl() {
		switch (heroState) {
		case down:
			//设置角色的动画
			setAnim(AnimationKind.motorDown, false);
			//可以设置装配到角色身上的坐骑的动画
			break;
		case run:
			setAnim(AnimationKind.motorRun, false);
			break;
		case up:
			ySpeed=20;
			setAnim(AnimationKind.motorUp, false);
			break;
		default:break;
		}
	}

	//普通时的动画控制，即背火箭筒的动画
	private void normalAnimControl() {
		switch (heroState) {
		case down://背火箭筒下落动画
			setAnim(AnimationKind.normalDown, false);
			break;
		case run://奔跑
			setAnim(AnimationKind.normalRun, true);
			break;
		case up://上升
			skeleton.setBonesToSetupPose();//重置为初始位置
			animationState.setAnimation(0, AnimationKind.normalUp.name(), true);
			break;
			default:
				break;
		}
	}
	
	//人物位置控制的方法，主要区分坐骑上下运动
	private void positionControl() {
		if (haveGun) {
			gunContorl();
		}else if (isRideMount) {
			mountControl();
		}else {
			normalControl();
		}
	}

	private void normalControl() {
		switch (heroState) {
		case down:
			//加速下落
			if (ySpeed <= -7f) {
				ySpeed = -7f;
			} else {
				ySpeed -= 0.3f;
			}
			this.moveBy(0, ySpeed);
			//落到地上
			if (getY() < 30) {
				ySpeed = 0f;
				setState(HeroState.run);
			}else if (getY() > 540 - getHeight()) {
				//不出顶
				setY(540 - getHeight());
				ySpeed = 0;
			}
			break;
		case run:
			//往前跑不过250像素
			if (getX() >= 250) {
				setX(250);
			}else {
				this.moveBy(4, 0);
			}
			break;
		case up:
			//上升力最大不过7
			if (ySpeed >= 8f) {
				ySpeed = 8;
			} else {
				ySpeed += 0.5f;
			}
			this.moveBy(0, ySpeed);
			if (getY() > 540 - getHeight()) {
				setY(540 - getHeight());
				ySpeed = 0;
			}else if (getY() < 30) {
				ySpeed = 0f;
				setY(30);
			}
			break;
			default:break;
		}
	}

	private void gunContorl() {
		normalControl();
	}

	private void mountControl(){
		switch (heroState) {
		case down:
			if (ySpeed <= -10f) {
				ySpeed = -10f;
			} else {
				ySpeed -= 0.8f;
			}
			this.moveBy(0, ySpeed);
			if (getY() < 60) {
				setY(60);
				ySpeed = 0f;
				setState(HeroState.run);
//				LogUtil.e(Hero.class.getSimpleName(), "234", true);
			}
			break;
		case run:
			if (getX() >= 250) {
				setX(250);
			}else {
				this.moveBy(4, 0);
			}
			break;
		case up:
			ySpeed-=0.8f;
			if (getY() > 540 - getHeight()||ySpeed<=0) {
//				setY(540 - getHeight());
				ySpeed = 0;
				setState(HeroState.down);
			}else {
				this.moveBy(0, ySpeed);
			}
			break;
			default:break;
		}
	}

	public Rectangle getPolygon() {
		rect.set(getX(),getY(),getWidth(),getHeight());
		return rect;
	}
	//设置人物的状态，不管是拿枪，坐骑，普通，都有三种状态
	public void setState(HeroState state){
		if(state==HeroState.up){
			SoundUtil.playSound(screen.main.getManager(), "up");
		}
		if (isRideMount) {
			if (state==HeroState.up) {
				if (heroState==HeroState.run) {
					this.heroState=state;
					animControl();
				}
			}else {
				this.heroState=state;
				animControl();
			}
		}else if (haveGun) {
			this.heroState=state;
			animControl();
		}else if (!isSpeedUp) {
			this.heroState=state;
			animControl();
		}
	}
	//动画转换方法，如果有衔接动画，此方法不适用
	private void setAnim(AnimationKind kind,boolean loop) {
		skeleton.setBonesToSetupPose();//重置为初始位置
		animationState.setAnimation(0, kind.name(), loop);
	}

	public float getySpeed() {
		return ySpeed;
	}

	public void setySpeed(float ySpeed) {
		this.ySpeed = ySpeed;
	}

	public boolean isShield() {
		return isShield;
	}

	public void setShield(boolean isShield) {
		this.isShield = isShield;
	}
	
	public HeroState getHeroState() {
		return heroState;
	}

	public boolean isSpeedUp() {
		return isSpeedUp;
	}

	public void setSpeedUp(boolean isSpeedUp) {
		this.isSpeedUp = isSpeedUp;
		if (isSpeedUp) {
			SoundUtil.playSound(screen.main.getManager(), "speedUp");
			setAnim(AnimationKind.speedUp, true);
			this.addAction(Actions.sequence(Actions.moveTo(500, 270-getHeight()/2, 0.3f),Actions.run(new Runnable() {
				
				@Override
				public void run() {
					screen.speed+=30;
				}
			})));
		} else {
			screen.speed-=30;
//			setAnim(AnimationKind.normalDown, false);
			setState(HeroState.down);
			this.addAction(Actions.sequence(Actions.moveTo(250, 270-getHeight()/2, 0.3f),Actions.run(new Runnable() {
				
				@Override
				public void run() {
					ySpeed=0;
				}
			})));
		}
	}

	public boolean isAttract() {
		return isAttract;
	}

	public void setAttract(boolean isAttract) {
		this.isAttract = isAttract;
	}

	public boolean isShake() {
		return isShake;
	}

	public void setShake(boolean isShake) {
		if (!isShield&&!isSpeedUp) {
			if (isShake) {
				this.addAction(Actions.sequence(Actions.repeat(2, Actions.sequence(Actions.color(Color.RED, 0.2f),Actions.color(Color.WHITE,0.2f))),Actions.run(new Runnable() {
					
					@Override
					public void run() {
						Hero.this.isShake=false;
					}
				})));
			}
			this.isShake = isShake;
		}
	}

	public boolean isRideMount() {
		return isRideMount;
	}

	public void setRideMount(boolean isRideMount) {
		if (isRideMount) {
			this.isRideMount = isRideMount;
			setHero2MountAnim(HeroState.up);//设置角色对应该坐骑的动画
			screen.mount.setMountAnim("setUp");//设置坐骑的动画
			ySpeed=5;//重置设置向上时的初始速度
			screen.control.addSpeed(5);
		} else {
			screen.bangControl.addBang(getX()+getWidth()/2, getY()+getHeight()/2);
			this.isRideMount = isRideMount;
			screen.control.resumeSpeed();
			screen.mount.removeMount();
			setState(HeroState.down);
			ySpeed=0;
			setShake(true);
		}
	}
	
	//骑坐骑时首次设置动画
	private void setHero2MountAnim(HeroState state){
		this.heroState=state;
		animControl();
	}

	public boolean isStart() {
		return isStart;
	}

	public void setStart(boolean isStart) {
		this.isStart = isStart;
	}

	public boolean isHaveGun() {
		return haveGun;
	}

	public void setHaveGun(boolean haveGun) {
		if (haveGun) {
			this.haveGun = haveGun;
			setState(heroState);
			screen.gun.setGunAnim("setup");
		}else {
			this.haveGun = haveGun;
			setState(heroState);
			screen.gun.removeGun();
		}
	}

	public boolean isDeath() {
		return isDeath;
	}

	public void setDeath(boolean isDeath) {
		this.isDeath = isDeath;
		if (isDeath) {
			screen.bangControl.addBang(getX()+getWidth()/2, getY()+getHeight()/2);
			SoundUtil.playSound(screen.main.getManager(), "dead");
			if (haveGun) {
				setHaveGun(false);
			}
			setAnim(AnimationKind.rotate, true);
			deathYSpeed=15;
			ySpeed=deathYSpeed;
			bgReduceStep=screen.speed/100;
		}else {
			screen.control.touchCount=0;
		}
		
	}
}