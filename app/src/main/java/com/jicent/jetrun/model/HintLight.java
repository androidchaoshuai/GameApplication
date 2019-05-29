package com.jicent.jetrun.model;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.jicent.jetrun.data.BuffType;
import com.jicent.jetrun.extensions.AnimEx;
import com.jicent.jetrun.extensions.TextureEx;
import com.jicent.jetrun.screen.GameScreen;
/**
 * 骑到车或者捡到枪后，游戏下方出现一个提示光条
 * @author yujia
 *
 */
public class HintLight extends Group{
	private GameScreen screen;
	private boolean hintDone;
	private BuffType type;
	
	public HintLight(GameScreen screen) {
		this.screen=screen;
	}
	
	public void addHint(final BuffType type){
		this.type=type;
		final AnimEx bgAnimEx=new AnimEx(0.1f, new TextureEx[]{screen.getTexture("res/hintLight0.png"),screen.getTexture("res/hintLight1.png")},PlayMode.LOOP);
		bgAnimEx.setPosition(0, 250);
		bgAnimEx.setOrigin(480, 61.5f);
		bgAnimEx.setScale(1, 0);
		addActor(bgAnimEx);
		bgAnimEx.addAction(Actions.sequence(Actions.scaleBy(1, 1, 0.2f),Actions.run(new Runnable() {
			
			@Override
			public void run() {
				Label hintLabel=null;
				switch (type) {
				case motor:
					hintLabel = new Label("狂野摩托", new LabelStyle(screen.getBitmapFont("font/hintFont.fnt"),Color.WHITE));
					break;
				case gun:
					hintLabel = new Label("沙漠之鹰", new LabelStyle(screen.getBitmapFont("font/hintFont.fnt"),Color.WHITE));
					break;
				case shield:
					hintLabel = new Label("超级护盾", new LabelStyle(screen.getBitmapFont("font/hintFont.fnt"),Color.WHITE));
					break;
				case speedUp:
					hintLabel = new Label("急速飞行", new LabelStyle(screen.getBitmapFont("font/hintFont.fnt"),Color.WHITE));
					break;
				case attract:
					hintLabel = new Label("金币吸收", new LabelStyle(screen.getBitmapFont("font/hintFont.fnt"),Color.WHITE));
					break;
				default:
					break;
				}
				hintLabel.setPosition(960, 250+61.5f-hintLabel.getPrefHeight()/2);
				hintLabel.addAction(Actions.sequence(Actions.moveTo(480-hintLabel.getPrefWidth()/2, hintLabel.getY(), 0.2f),Actions.delay(0.5f),Actions.moveTo(-hintLabel.getPrefWidth(), hintLabel.getY(), 0.2f),Actions.addAction(Actions.sequence(Actions.fadeOut(0.1f),Actions.run(new Runnable() {
					
					@Override
					public void run() {
						hintDone=true;
						HintLight.this.clear();
					}
				})), bgAnimEx)));
				addActor(hintLabel);
			}
		})));
		
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		if(hintDone){
			switch (type) {
			case motor:
					screen.mount.setMountAnim("run");
				break;
			case gun:
					screen.gun.setGunAnim("shoot");
				break;
			default:
				break;
			}
			screen.hintLight.hintDone=false;
			screen.control.isStopGame=false;
			screen.explodeLight.addExplode();
		}
	}
}
