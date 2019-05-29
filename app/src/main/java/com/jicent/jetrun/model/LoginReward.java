package com.jicent.jetrun.model;


import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.TimeUtils;
import com.jicent.jetrun.data.StaticVariable;
import com.jicent.jetrun.extensions.ButtonEx;
import com.jicent.jetrun.extensions.ButtonEx.InputListenerEx;
import com.jicent.jetrun.screen.FatherScreen;
import com.jicent.jetrun.screen.ShopScreen;
import com.jicent.jetrun.utils.DialogUtil;
import com.jicent.jetrun.utils.InfoToast;
import com.jicent.jetrun.utils.NextOperate;
import com.jicent.jetrun.utils.SPUtil;
import com.jicent.jetrun.utils.SoundUtil;

public class LoginReward extends Group implements InputListenerEx{
	private ShopScreen screen;
	private ButtonEx btn;
//	private ButtonEx exitBtn;
	private float[][] positions;
	
	public LoginReward(ShopScreen shopScreen) {
		this.screen=shopScreen;
		positions=new float[][]{{85,286},{234,286},{378,286},{530,286},
								{85,120},{234,120},{378,120}};
		Image bgImg=new Image(shopScreen.getTexture("res/loginBg.png"));
		this.setSize(bgImg.getWidth(), bgImg.getHeight());
		this.addActor(bgImg);
		
		for (int i = 0; i < StaticVariable.currDay; i++) {
			Image img=new Image(screen.getTexture("res/complete.png"));
			img.setPosition(positions[i][0], positions[i][1]);
			this.addActor(img);
		}
		btn = new ButtonEx(shopScreen, shopScreen.getTexture("res/getBtn.png"));
		btn.setPosition(490, 80); 
		btn.addListener(this);
		this.addActor(btn);
	}

	@Override
	public boolean touchDown(Actor actor) {
		SoundUtil.playSound(screen.main.getManager(), "button");
		return true;
	}

	@Override
	public void touchUp(Actor actor) {
		if (actor==btn) {
			//领取物品
			StaticVariable.loginTime=TimeUtils.millis();
			SPUtil.commit(screen.main.getSp(), "loginTime",TimeUtils.millis());
			SPUtil.commit(screen.main.getSp(), "currDay", StaticVariable.currDay);
			DialogUtil.dismiss(new NextOperate() {
				@Override
				public void nextDone() {
					switch (StaticVariable.currDay) {
					case 0:
						InfoToast.show(screen, "恭喜您获得500金币！");
						StaticVariable.coinNum+=500;
						SPUtil.commit(screen.main.getSp(), "coinNum", StaticVariable.coinNum);
						break;
					case 1:
						InfoToast.show(screen, "恭喜您获得1个护盾道具！");
						StaticVariable.shieldCount+=1;
						SPUtil.commit(screen.main.getSp(), "shieldCount", StaticVariable.shieldCount);
						break;
					case 2:
						InfoToast.show(screen, "恭喜您获得1个步枪道具！");
						StaticVariable.gunCount+=1;
						SPUtil.commit(screen.main.getSp(), "gunCount", StaticVariable.gunCount);
						break;
					case 3:
						InfoToast.show(screen, "恭喜您获得1个战车道具！");						
						StaticVariable.mountCount+=1;
						SPUtil.commit(screen.main.getSp(), "mountCount", StaticVariable.mountCount);
						break;
					case 4:
						InfoToast.show(screen, "恭喜您获得3000金币！");						
						StaticVariable.coinNum+=3000;
						SPUtil.commit(screen.main.getSp(), "coinNum", StaticVariable.coinNum);
						break;
					case 5:
						InfoToast.show(screen, "恭喜您获得2个冲刺道具！");						
						StaticVariable.speedUpCount+=2;
						SPUtil.commit(screen.main.getSp(), "speedUpCount", StaticVariable.speedUpCount);
						break;
					case 6:
						InfoToast.show(screen, "恭喜您获得5000金币！");						
						StaticVariable.coinNum+=5000;
						SPUtil.commit(screen.main.getSp(), "coinNum", StaticVariable.coinNum);
						break;
					default:
						break;
					}
					screen.roleShow.updateCoin();
				}
			});
		}
	}
}
