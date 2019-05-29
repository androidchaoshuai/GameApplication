package com.jicent.jetrun.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.jicent.jetrun.data.ResultKind;
import com.jicent.jetrun.extensions.ButtonEx;
import com.jicent.jetrun.extensions.ButtonEx.InputListenerEx;
import com.jicent.jetrun.screen.GameScreen;
import com.jicent.jetrun.utils.SoundUtil;
/*
 * 抽奖结果 
 */
public class Result extends Group {
	private GameScreen screen;
	private ResultKind kind;
	private boolean isRecieve;
	
	public Result(final GameScreen screen,ResultKind kind){
		this.kind=kind;
		this.screen=screen;
		
		Pixmap pixmap=new Pixmap(960, 540, Format.RGBA4444);
		pixmap.setColor(0f,0f,0f,0.8f);
		pixmap.fill();
		this.addActor(new Image(new Texture(pixmap)));
		
		Image lightImg=new Image(screen.getTexture("res/light.png"));
		lightImg.setSize(800, 800);
		lightImg.setPosition(480-lightImg.getWidth()/2, 270-lightImg.getHeight()/2);
		this.addActor(lightImg);
		lightImg.setOrigin(lightImg.getWidth()/2,lightImg.getHeight()/2);
		lightImg.addAction(Actions.forever(Actions.rotateBy(2f)));
		
		switch (kind) {
		case lottery0:
			
			break;
		case lottery1:
			
			break;
		case lottery2:
			
			break;
		case lottery3:
			
			break;
		case lottery4:
			
			break;
		case lottery5:
			
			break;
		case lottery6:
			
			break;
		case lottery8:
			
			break;
		case lottery7:
			
			break;

		default:
			break;
		}
		
		Image iconImg=new Image(screen.getTexture("res/speedUpIcon.png"));
		iconImg.setOrigin(iconImg.getWidth()/2, iconImg.getHeight()/2);
		iconImg.setPosition(480-iconImg.getWidth()/2, 270-iconImg.getHeight()/2);
		iconImg.setScale(0);
		this.addActor(iconImg);
		iconImg.addAction(Actions.sequence(Actions.scaleTo(1, 1, 0.8f),Actions.run(new Runnable() {
			
			@Override
			public void run() {
				final ButtonEx btn=new ButtonEx(screen, screen.getTexture("res/btnBg.png"), "领取", screen.getBitmapFont("font/allfont.fnt"), Color.WHITE);
				btn.setBounds(402, 50, 156, 95);
				btn.addListener(new InputListenerEx() {
					
					@Override
					public void touchUp(Actor actor) {
//						LogUtil.e(Result.class.getSimpleName(), "领取", true);
						isRecieve=true;
					}
					
					@Override
					public boolean touchDown(Actor actor) {
						SoundUtil.playSound(screen.main.getManager(), "button");
						return true;
					}
				});
				addActor(btn);
			}
		})));
		
		
	}

	public boolean isRecieve() {
		return isRecieve;
	}

	public void setRecieve(boolean isRecieve) {
		this.isRecieve = isRecieve;
	}
	
	
}
