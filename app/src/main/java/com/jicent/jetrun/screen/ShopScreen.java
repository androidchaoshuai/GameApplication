package com.jicent.jetrun.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.jicent.jetrun.data.DialogType;
import com.jicent.jetrun.data.StaticVariable;
import com.jicent.jetrun.entry.GameMain;
import com.jicent.jetrun.extensions.ProcessEx.ProcessType;
import com.jicent.jetrun.model.LoginReward;
import com.jicent.jetrun.model.RankUi;
import com.jicent.jetrun.model.RoleShow;
import com.jicent.jetrun.model.ShopButton;
import com.jicent.jetrun.model.WindowDialog;
import com.jicent.jetrun.utils.DialogUtil;
import com.jicent.jetrun.utils.LayoutDebug;
import com.jicent.jetrun.utils.SoundUtil;

public class ShopScreen extends FatherScreen {

	public ShopButton shopButton;
	public RoleShow roleShow;
	private Stage frontStage;
	public RankUi rankUi;
	public boolean isShowExit=false;
	
	public ShopScreen(GameMain main, ProcessType type) {
		super(main, type);
	}

	@Override 
	public void show() {
		super.show();
		SoundUtil.playMusic("gameMusic"); 
		frontStage=new Stage(new StretchViewport(960, 540));
		
		Image bgImg=new Image(getTexture("res/shopBg.jpg"));
		mainStage.addActor(bgImg);
		
		Image logo=new Image(getTexture("res/logo.png"));
		logo.setPosition(960/2-logo.getWidth()/2,364);
		mainStage.addActor(logo);
		
		shopButton = new ShopButton(this);  //开始游戏按钮  --jn因为里面有退出游戏的按钮，所以让它在上面才行
		mainStage.addActor(shopButton);
		roleShow = new RoleShow(this);      //左右选人物的箭头
		mainStage.addActor(roleShow);
		
		
		Label label=new Label("客服电话：01051736930", new LabelStyle(getBitmapFont("font/allfont.fnt"), Color.WHITE));
		label.setFontScale(0.6f);
		label.setPosition(25, 450);
		mainStage.addActor(label);
		
		//界面 (具体显示什么还要根据情况来定,如商店界面，暂停界面，继续界面)
		dialog=new WindowDialog(this);
		
		//判断弹出每日登陆
		long oneDay=86400*1000;//一天有这么多毫秒
		long intervalTime=TimeUtils.millis()-StaticVariable.loginTime;
		if (intervalTime>oneDay) {
			if (intervalTime<oneDay*2) {
				StaticVariable.currDay++;
				if (StaticVariable.currDay>6) {
					StaticVariable.currDay=0;
				}
			}else {
				StaticVariable.currDay=0;
			}
			DialogUtil.show(this, new LoginReward(this), ProcessType.empty);
		}
		
	}
	@Override
	public void render(float delta) {
		mainStage.act();
		mainStage.draw();
		frontStage.act();
		frontStage.draw();
		dialogStage.act();
		dialogStage.draw();
		toastStage.act();
		toastStage.draw();

		// 支付完成后的操作，在stage都act和draw后进行调用
		payDeal();

		// 屏幕切换，在stage都act和draw后进行调用
		if (isChangeScreen) {
			main.setScreen(nextScreen);
		}
		if (StaticVariable.nameOk) {
			DialogUtil.show(this, dialog.getDialog(DialogType.RANK), ProcessType.dismiss);
			StaticVariable.nameOk=false;
		}
	}

	//资源清空时调用，系统在切屏的时候，会主动调用
	@Override
	public void hide() {
		super.hide();
	}

}
