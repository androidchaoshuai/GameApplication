package com.jicent.jetrun.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.jicent.jetrun.extensions.TextureEx;
import com.jicent.jetrun.screen.FatherScreen;

public class InfoToast{

	/**
	 * 显示提示信息界面，稍微详细点
	 * @param screen 所在screen
	 * @param info 内容
	 */
	public static void show(FatherScreen screen, String info) {
		screen.toastStage.clear();
		Pixmap px=new Pixmap(1, 1, Format.RGBA4444);
		px.setColor(0,0,0,0.5f);
		px.fill();
		
		final Group group=new Group();
		Label label=new Label(info, new LabelStyle(screen.getBitmapFont("font/allfont.fnt"), Color.WHITE));
		label.setPosition(10, 10);
		
		Image labelBg=new Image(new TextureEx(px));
		labelBg.setBounds(0, 0, label.getPrefWidth()+20, label.getPrefHeight()+14);
		
		group.setBounds(480 - label.getPrefWidth() / 2 -10, -label.getPrefHeight()-8, labelBg.getWidth(), labelBg.getHeight());
		group.addActor(labelBg);
		group.addActor(label);
		group.addAction(Actions.sequence(
				Actions.moveTo(group.getX(), 100, 0.5f),
				Actions.delay(1.5f), 
				Actions.fadeOut(0.5f),Actions.run(new Runnable() {
					
					@Override
					public void run() {
						group.remove();
					}
				})));
		screen.toastStage.addActor(group);
	}
	
}
