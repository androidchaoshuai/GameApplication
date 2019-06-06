package com.jicent.jetrun.extensions;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.jicent.jetrun.data.DialogType;
import com.jicent.jetrun.data.GameState;
import com.jicent.jetrun.screen.FatherScreen;
import com.jicent.jetrun.screen.GameScreen;
import com.jicent.jetrun.screen.ShopScreen;
import com.jicent.jetrun.utils.DialogUtil;

public class ProcessEx extends InputAdapter{
	private ProcessType type;
	private FatherScreen screen;
	
	public ProcessEx(FatherScreen screen,ProcessType type){
		this.type=type;
		this.screen=screen;
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode==Keys.BACK) {
			switch (type) {
			case gameScreen:
				((GameScreen)screen).changeState(GameState.pause);
				DialogUtil.show(screen, screen.dialog.getDialog(DialogType.pause), ProcessType.empty);
				break;
			case loadingScreen:
				screen.main.showExitDialog();
				break;
			case shopScreen:
				((ShopScreen)screen).isShowExit=true;
				DialogUtil.show(screen, screen.dialog.getDialog(DialogType.coinGift), ProcessType.empty);
				break;
			case revieve:
				
				break;
			case dismiss:
				DialogUtil.dismiss();
				break;
			default:break;
			}
		}
		return false;
	}

	public enum ProcessType{
		gameScreen,registerScreen,loadingScreen,shopScreen, empty, revieve,dismiss
	}
}
