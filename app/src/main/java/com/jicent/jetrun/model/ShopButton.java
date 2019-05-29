package com.jicent.jetrun.model;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.jicent.jetrun.data.DialogType;
import com.jicent.jetrun.data.StaticVariable;
import com.jicent.jetrun.extensions.ButtonEx;
import com.jicent.jetrun.extensions.ButtonEx.InputListenerEx;
import com.jicent.jetrun.extensions.ProcessEx.ProcessType;
import com.jicent.jetrun.screen.GameScreen;
import com.jicent.jetrun.screen.ShopScreen;
import com.jicent.jetrun.utils.DialogUtil;
import com.jicent.jetrun.utils.LayoutDebug;
import com.jicent.jetrun.utils.SPUtil;
import com.jicent.jetrun.utils.SoundUtil;
/*
 * 开始游戏按钮 
 */
public class ShopButton extends Group implements InputListenerEx{
	private ShopScreen screen;
	private ButtonEx startBtn;//,gunBtn,mountBtn,propBtn,
	private ButtonEx settingBtn,rankBtn; //设置
	private ButtonEx propBtn, coinBtn; //道具和金币
	
	public ButtonEx getStartBtn(){
		return startBtn;
	} 
	
	public ShopButton(ShopScreen screen) {
		this.screen=screen;
		startBtn = new ButtonEx(screen, screen.getTexture("res/startBtn.png"));
		startBtn.setPosition(960/2-startBtn.getWidth()/2, 19);
		startBtn.addListener(this);
		this.addActor(startBtn);
		
		
		//设置 --jn
		settingBtn = new ButtonEx(screen, screen.getTexture("res/setting.png"));
		settingBtn.setPosition(870,20);
		settingBtn.addListener(this);
		this.addActor(settingBtn);
		
		//道具,金币 --jn
		propBtn = new ButtonEx(screen, screen.getTexture("res/daoju.png"));
		propBtn.setPosition(18,7);
		propBtn.addListener(this);
		this.addActor(propBtn);
		
		coinBtn = new ButtonEx(screen, screen.getTexture("res/jinbi.png"));
		coinBtn.setPosition(10,115);
		coinBtn.addListener(this);
		this.addActor(coinBtn);

		rankBtn = new ButtonEx(screen, screen.getTexture("res/rankBtn.png"));
		rankBtn.setPosition(784,20);
		rankBtn.addListener(this);
		this.addActor(rankBtn);
		
//		new LayoutDebug(this, startBtn,settingBtn,propBtn,coinBtn,rankBtn).setFont(screen.getBitmapFont("font/allfont.fnt"));
	}

	@Override
	public boolean touchDown(Actor actor) {
		SoundUtil.playSound(screen.main.getManager(), "button");
		return true;
	}

	@Override
	public void touchUp(Actor actor) {
		if (actor==startBtn) {
			SPUtil.commit(screen.main.getSp(), "roleKind", StaticVariable.roleKind);
			screen.changeScreen(true, new GameScreen(screen.main, ProcessType.gameScreen));
			SoundUtil.stopMusic();
		}else if(actor == settingBtn){
			DialogUtil.show(screen, screen.dialog.getDialog(DialogType.set), ProcessType.dismiss);
		}else if(propBtn == actor){
			DialogUtil.show(screen, screen.dialog.getDialog(DialogType.propGift), ProcessType.dismiss);
		}else if(coinBtn == actor){
			DialogUtil.show(screen, screen.dialog.getDialog(DialogType.coinGift), ProcessType.dismiss);
		}else if (actor==rankBtn) {
			if (StaticVariable.name=="") {
				screen.main.getActivity().handler.sendEmptyMessage(0);
			}else {
				DialogUtil.show(screen, screen.dialog.getDialog(DialogType.RANK), ProcessType.dismiss);
			}
		}
	}
}
