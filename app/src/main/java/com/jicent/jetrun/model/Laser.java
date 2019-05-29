package com.jicent.jetrun.model;

import org.json.JSONArray;
import org.json.JSONObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.jicent.jetrun.extensions.CollisionPolygon;
import com.jicent.jetrun.screen.GameScreen;
import com.jicent.jetrun.utils.InfoToast;
/*
 * 激光
 */

import com.jicent.jetrun.utils.SoundUtil;


public class Laser extends Group {
	private GameScreen screen;
	private float timer; 
	public boolean isProductLaser;//提醒金币，导弹，空中障碍物等类，要产生激光的标识
	private String[] typeNames;
	public boolean isLaserOut=true;//激光是否已走
	
	public Laser(GameScreen screen) {
		this.screen = screen;
		//存放激光出现的顺序和出现的类型
		typeNames=new String[]{"closeTo","closeTo1","normal0","normal1","exchange","exchange1","exchange2"};//
	}

	@Override
	public void act(float delta) {
		if (!screen.control.isStopGame) {
			super.act(delta);
			if (!this.hasChildren()) {
				if (!isProductLaser) {
					//如果角色处于加速或坐骑中，不产生激光
					if (!screen.hero.isSpeedUp()&&!screen.hero.isRideMount()&&!screen.hero.isHaveGun()) {
						timer+=delta;
						if (timer>15) {
							timer=0;
							isProductLaser=true;
						}
					}
				}else {
					if (!screen.coinControl.hasChildren()&&!screen.standBarrierControl.hasChildren()
							&&!screen.rotationBarrierControl.hasChildren()&&!screen.missile.hasChildren()) {
						//随机添加激光
						addLaser(typeNames[MathUtils.random(typeNames.length-1)]);
						isLaserOut=false;
						isProductLaser=false;
					}
				}
			}
		}
	}
	
	private void addLaser(String name){
		try {
			//使用json文件存储激光的类型、位置，移动方式
			String data=Gdx.files.internal("laser/"+name+".json").readString();
			JSONArray array=new JSONArray(data);
			for (int i = 0,length=array.length(); i < length; i++) {
				JSONObject object=array.getJSONObject(i);
				float moveTo=-100f;
				if (object.has("moveTo")) {
					moveTo=object.getInt("moveTo");
				}
				String showOrder="";
				if (object.has("showOrder")) {
					showOrder=object.getString("showOrder");
				}
				this.addActor(new SingleLaser(getLaserType(object.getString("type")),object.getInt("position"),moveTo,showOrder));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//将类型字符串转换成对应的LasetType
	private LaserType getLaserType(String type){
		if (type.equals("normal")) {
			return LaserType.normal;
		}else if (type.equals("closeTo")) {
			return LaserType.closeTo;
		}else if (type.equals("exchange")) {
			return LaserType.exchange;
		}
		return null;
	}
	
	enum LaserType{
		normal,closeTo,exchange
	}

	class SingleLaser extends Actor {
		private Animation animation;
		private float timer;
		private int index;//激光动画的索引值
		private String showOrder;//激光发光的顺序串
		private int currShow,orderIndex;//当前时间激光是否显示的值，已经播放到的位置
		private boolean isDismiss;//是否需要结束激光的播放
		private LaserType type;
		private float moveTo;//激光移到的目的位置
		private CollisionPolygon laserPolygon;

		public SingleLaser(LaserType type,float y,float moveTo,String showOrder) {
			this.type=type;
			this.moveTo=moveTo;
			this.showOrder=showOrder;
			currShow=getShowIndex(orderIndex);
			setPosition(180, y);
			setSize(600, 56);
			getAnimation(index);
			laserPolygon=new CollisionPolygon(new float[]{43,17,558,17,558,37,43,37}, getX(), getY());
		}
		
		/**
		 * 获取发光字符串中的某个数值，0代表不发光，1代表按照正常流程发光
		 * @param orderIndex 已经进行到的位置索引
		 * @return 截取到的数字值
		 */
		private int getShowIndex(int orderIndex){
			if (orderIndex<showOrder.length()) {
				int result=Integer.parseInt(showOrder.substring(orderIndex, orderIndex+1));
				return result;
			}
			return 0;
		}

		private void getAnimation(int index) {
			TextureRegion[] regions=null;
			switch (index) {
			case 0://开始的动画
				regions=new TextureRegion[1];
				for (int j = 0; j < regions.length; j++) {
					regions[j]=new TextureRegion(screen.getTexture("res/a"+j+".png"));
				}
				animation=new Animation(0.1f, regions);
				break; 
			case 1://开始发光的动画
				SoundUtil.playSound(screen.main.getManager(), "laserW");
				regions=new TextureRegion[14];
				for (int j = 0; j < regions.length; j++) {
					regions[j]=new TextureRegion(screen.getTexture("res/a"+(j+1)+".png"));
				}
				animation=new Animation(0.1f, regions);
				break;
			case 2://一直发光的动画，此时应该可以检测碰撞了
				SoundUtil.playSound(screen.main.getManager(), "laserO");
				regions=new TextureRegion[4];
				for (int j = 0; j < regions.length; j++) {
					regions[j]=new TextureRegion(screen.getTexture("res/a"+(j+15)+".png"));
				}
				animation=new Animation(0.1f, regions);
				animation.setPlayMode(PlayMode.LOOP);
				break;
			case 3://从发光变为不发光的过渡
				isLaserOut=true;
				regions=new TextureRegion[3];
				for (int j = 0; j < regions.length; j++) {
					regions[j]=new TextureRegion(screen.getTexture("res/a"+(j+1)+".png"));
				}
				animation=new Animation(0.1f, regions);
				break;
			}
		}

		@Override
		public void act(float delta) {
			if (!screen.control.isStopGame) {
				super.act(delta);
				timer += delta;
				if (screen.hero.isSpeedUp()||screen.hero.isRideMount()||screen.hero.isHaveGun()) {//持枪、加速或者骑坐骑中，如果有激光，直接使其消失
					if (index!=0) {
						index=0;
						timer=0;
						getAnimation(index);
					}else {
						if (timer>1f) {
							this.addAction(Actions.sequence(Actions.fadeOut(0.3f),Actions.removeActor()));
						}
					}
				}else {
					if (isDismiss) {//发光->结束的动画变换
						if (index==3&&timer>0.3f) {
							//重置计时时间
							timer=0;
							index=0;
							dealChange();
						}else if (index==0&&timer>1f) {
							//重置计时时间
							timer=0;
							if (type==LaserType.exchange) {
								if (orderIndex==showOrder.length()-1) {
									this.addAction(Actions.sequence(Actions.fadeOut(0.3f),Actions.removeActor()));
								}else {
									currShow=getShowIndex(++orderIndex);
									getAnimation(index);
									isDismiss=false;
								}
							}else {
								this.addAction(Actions.sequence(Actions.fadeOut(0.3f),Actions.removeActor()));
							}
						}
					}else {//开始->发光的动画变换
						if (index==0&&timer>1f) {
							//重置计时时间
							timer=0;
							index++;
							dealChange();
						}else if (index==1&&timer>1.6f) {
							//重置计时时间
							timer=0;
							index++;
							dealChange();
						}else if (index==2) {
							//根据类型，进行操作
							switch (type) {
							case normal://常规的，持续4s后就开始消失了
								if (timer>2f) {
									//重置计时时间
									timer=0;
									index++;
									getAnimation(index);
									isDismiss=true;
								}
								//碰撞检测
								if (!screen.hero.isShake()&&!screen.hero.isDeath()) {
									if (laserPolygon.overlaps(screen.hero.getPolygon())) {
										if (screen.hero.isShield()) {
											screen.hero.setShield(false);
										}else {
//											InfoToast.show(screen, "撞到了");
											screen.hero.setDeath(true);
										}
									}
								}
								break;
							case closeTo://往目标位置移动，之后再消失
								if (timer>2f&&timer<3f) {
									timer=4f;
									this.addAction(Actions.sequence(Actions.moveTo(getX(), moveTo, 2f),Actions.delay(2f),Actions.run(new Runnable() {
										
										@Override
										public void run() {
											//重置计时时间
											timer=0;
											index++;
											getAnimation(index);
											isDismiss=true;
										}
									})));
								}
								
								if (!screen.hero.isShake()&&!screen.hero.isDeath()) {
									laserPolygon.setPosition(getX(), getY());
									//碰撞检测
									if (laserPolygon.overlaps(screen.hero.getPolygon())) {
										if (screen.hero.isShield()) {
											screen.hero.setShield(false);
										}else {
//											InfoToast.show(screen, "撞到了");
											screen.hero.setDeath(true);
										}
									}
								}
								break;
							case exchange:
								//当currShow==1时，才进行碰撞检测和动画的切换
								if (currShow==1) {
									if (timer>2f) {
										//重置计时时间
										timer=0;
										index++;
										getAnimation(index);
										isDismiss=true;
									}
									if (!screen.hero.isShake()&&!screen.hero.isDeath()) {
										//碰撞检测
										if (laserPolygon.overlaps(screen.hero.getPolygon())) {
											if (screen.hero.isShield()) {
												screen.hero.setShield(false);
											}else {
//												InfoToast.show(screen, "撞到了");
												screen.hero.setDeath(true);
											}
										}
									}
								}else {
									if (timer>4f) {
										//重置计时时间
										timer=0;
										index++;
										isDismiss=true;
									}
								}
								break;
							}
						}
					}
				}
			}
		
		}

		//动画进行切换时的处理方法，主要区分出exchange类型的处理方式
		private void dealChange() {
			if (type==LaserType.exchange) {
				//currShow为1的时候，按照正常流程处理，否则就不进行动画的切换
				if (currShow==1) {
					getAnimation(index);
				}
			}else {
				getAnimation(index);
			}
		}

		@Override
		public void draw(Batch batch, float parentAlpha) {
			Color color = this.getColor();
			batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

			batch.draw(animation.getKeyFrame(timer), getX(), getY(), getWidth(), getHeight());
		}
	}

}
