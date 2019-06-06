package com.jicent.jetrun.screen;

import android.content.Intent;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.TimeUtils;
import com.jicent.jetrun.data.StaticVariable;
import com.jicent.jetrun.entry.GameMain;
import com.jicent.jetrun.entry.RegisterActivity;
import com.jicent.jetrun.extensions.ProcessEx.ProcessType;
import com.jicent.jetrun.model.LoginReward;
import com.jicent.jetrun.utils.DialogUtil;
import com.jicent.jetrun.utils.SPUtil;

public class LoadingScreen extends FatherScreen{
	private Label processLabel;
	private Sound sound;
	private long result;
	private float timer;

	public LoadingScreen(GameMain main, ProcessType type) {
		super(main, type);
		sound=Gdx.audio.newSound(Gdx.files.internal("sound/loading.mp3"));
	}

	@Override
	public void render(float delta) { 
		
		mainStage.act();
		mainStage.draw();
		
		if (processLabel!=null) {
			processLabel.setText(((int)(main.getManager().getProgress()*100))+"%");
			processLabel.setPosition(870+37-processLabel.getTextBounds().width/2, 37);
			if (main.getManager().update()) {  //加载完毕了才会为true --jn
				timer+=delta;
				if (timer>0.1f) {
					timer=0;
					//加载完毕进入注册界面
					//changeScreen(true, new ShopScreen(main, ProcessType.shopScreen));
					Intent intent = new Intent();
					intent.setClass(main.getActivity(), RegisterActivity.class);
					main.getActivity().startActivity(intent);
					//changeScreen(true, new RegisterScreen(main, ProcessType.registerScreen));
					//changeScreen(true, new ShopScreen(main, ProcessType.shopScreen));
				}
			}
			
		}

		// 屏幕切换，在stage都act和draw后进行调用
		if (isChangeScreen) {  //这个变量每次跳转到下一个Screen后，肯定为false  --jn 
			main.setScreen(nextScreen);
		} 
	}
	
//	//显示签到
//	private void showSignIn(){
//		//long oneDay=1000*3; //1000代表1秒
//		long oneDay=24 * 60 * 60 * 1000; 
//		long intervalTime=TimeUtils.millis()-StaticVariable.loginTime;
//		if (intervalTime>oneDay) {
//			if (intervalTime<oneDay*2) {
//				StaticVariable.currDay++;
//				if (StaticVariable.currDay>6) {
//					StaticVariable.currDay=0;
//				}
//			}else {
//				StaticVariable.currDay=0;
//			}
//			DialogUtil.show(this, new LoginReward(this), ProcessType.empty);
//		}else{   //如果间隔小于一天，则代表签到过了,就直接跳转到游戏界面  --jn
//			
//			this.changeScreen(true, new ShopScreen(this.main, ProcessType.shopScreen));
//		}
//	}

//	@Override
//	public void show() {
//		super.show();
//		variableInit();
//		
//		//long oneDay=1000*3; //1000代表1秒
////		long oneDay=1000 * 24 * 60 * 60; 
////		long intervalTime=TimeUtils.millis()-StaticVariable.loginTime;
////		if (intervalTime>oneDay) {
////			if (intervalTime<oneDay*2) {
////				StaticVariable.currDay++;
////				if (StaticVariable.currDay>6) {
////					StaticVariable.currDay=0;
////				}
////			}else {
////				StaticVariable.currDay=0;
////			}
////			DialogUtil.show(this, new LoginReward(this), ProcessType.empty);
////		}
//		showSignIn();
////		changeScreen(true, new ShopScreen(main, ProcessType.shopScreen));
////		mainStage.addActor(new LoginReward(this));
//	}

	@Override
	public void show() {
		super.show();
		init();
		result=sound.play();
		//当音效没有准备好时，返回值为-1，此时不能显示界面
		while (result==-1L) {
			result=sound.play();
		}
		
		variableInit();
		
		
	} 
	//相当于单利类存储初始化  --jn
	private void variableInit() {
		StaticVariable.loginTime=main.getSp().getLong("loginTime", 0L);
		StaticVariable.currDay=main.getSp().getInt("currDay", -1);
		if(StaticVariable.useLocalMusicSetting)
		{
			StaticVariable.isMusicOn=main.getSp().getBoolean("isMusicOn", true);
			StaticVariable.isSoundOn=main.getSp().getBoolean("isSoundOn", true);
		}
		StaticVariable.bestScore=main.getSp().getInt("bestScore", 0);
		StaticVariable.coinNum=main.getSp().getInt("coinNum", 0);
		StaticVariable.roleKind=main.getSp().getInt("roleKind", 0);
		StaticVariable.name=SPUtil.getDataFormSp(main.getSp(), "name", "");
		
		//初始化道具个数
		StaticVariable.speedUpCount=main.getSp().getInt("speedUpCount", 0);
		StaticVariable.gunCount=main.getSp().getInt("gunCount", 0);
		StaticVariable.mountCount=main.getSp().getInt("mountCount", 0);
		StaticVariable.shieldCount=main.getSp().getInt("shieldCount", 0);
		StaticVariable.attractCount=main.getSp().getInt("attractCount", 0);
		
		//
		StaticVariable.isBuyRole1 = main.getSp().getBoolean("isBuyRole1", false);
		StaticVariable.isBuyRole2 = main.getSp().getBoolean("isBuyRole2", false);
		StaticVariable.isBuyRole3 = main.getSp().getBoolean("isBuyRole3", false);
		StaticVariable.isBuyRole4 = main.getSp().getBoolean("isBuyRole4", false);
		
		StaticVariable.isBuyPet0  =   main.getSp().getBoolean("isBuyPet0", false);
		StaticVariable.isBuyPet1  =   main.getSp().getBoolean("isBuyPet1", false);
		StaticVariable.isBuyPet2  =   main.getSp().getBoolean("isBuyPet2", false);
		StaticVariable.isBuyPet3  =   main.getSp().getBoolean("isBuyPet3", false);
		StaticVariable.isBuyPet4  =   main.getSp().getBoolean("isBuyPet4", false);
	}
	
	private void init() {
//		Image bgImage=new Image(getTexture("res/lightBg.png"));
//		bgImage.setBounds(10, 139, 960, 326);
//		bgImage.setColor(1, 1, 1, 0);
//		mainStage.addActor(bgImage);
//		bgImage.addAction(Actions.sequence(Actions.fadeIn(0.2f)));
		
		Image line=new Image(getTexture("loading/line.png")); 
		line.setPosition(0, 265);
		mainStage.addActor(line);
		 
		Image lightPoint=new Image(getTexture("loading/lightPoint.png"));
		lightPoint.setPosition(-33, 254);
		mainStage.addActor(lightPoint);
		 
		lightPoint.addAction(Actions.sequence(Actions.moveTo(600, 251, 0.8f),Actions.moveTo(960, 251, 0.1f),Actions.run(new Runnable() {
			
			@Override
			public void run() {
				Image roundImg=new Image(getTexture("loading/loadingRound.png"));
				roundImg.setPosition(870, 10);
				roundImg.setOrigin(37, 37);
				mainStage.addActor(roundImg);
				roundImg.addAction(Actions.forever(Actions.rotateBy(8)));
				
				processLabel=new Label("0%", new LabelStyle(getBitmapFont("default"), Color.WHITE));
				processLabel.setPosition(870+37-processLabel.getTextBounds().width/2,37);
				mainStage.addActor(processLabel);
				
			}
		})));
		
		Image title=new Image(getTexture("loading/lushi.png"));
		title.setPosition(339, 272);
//		title.setColor(1,1,1,0);
//		title.addAction(Actions.fadeIn(0.2f));
		mainStage.addActor(title);
		
//		lightPoint.addAction(Actions.sequence(Actions.moveTo(960, 251, 0.3f),Actions.addAction(Actions.fadeIn(0.2f), bgImage),Actions.addAction(Actions.fadeIn(0.2f),title)));
	}

	//资源清空时调用，系统在切屏的时候，会主动调用
	@Override
	public void hide() {
		super.hide();
		sound.dispose();
	}
}

