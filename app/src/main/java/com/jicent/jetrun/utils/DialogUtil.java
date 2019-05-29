package com.jicent.jetrun.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.jicent.jetrun.extensions.ProcessEx;
import com.jicent.jetrun.extensions.ProcessEx.ProcessType;
import com.jicent.jetrun.screen.FatherScreen;

/**
 * 弹框类
 * @author yujia
 *
 */
public class DialogUtil{
	private static FatherScreen screen;
	private static Actor actor;
	private static float actorWidth,actorHeight;
	private static InputMultiplexer inputMultiplexer;
	public static boolean isShow;
	private static NextOperate operate;
	
	/**
	 * 显示dialog
	 * @param screen 显示dialog的screen
	 * @param actor 加入到dialog中的actor，如果是多个actor，添加到group中，然后将group传入
	 * @param actorWidth 传入的group或者actor的宽
	 * @param actorHeight 传入的group或者actor的高
	 * @param type 返回键的监听
	 */
	public static void show(FatherScreen screen,Actor actor,ProcessType type){
		if (!isShow) {
			DialogUtil.screen=screen;
			DialogUtil.actor=actor;
			DialogUtil.actorWidth=actor.getWidth();
			DialogUtil.actorHeight=actor.getHeight();
			isShow=true;
			inputMultiplexer=new InputMultiplexer();
			inputMultiplexer.addProcessor(new ProcessEx(screen, type));
			inputMultiplexer.addProcessor(screen.dialogStage);
			Gdx.input.setInputProcessor(inputMultiplexer);
			actor.setPosition(480-actorWidth/2, 540);
			
			Pixmap px=new Pixmap(960, 540, Format.RGBA4444);
			px.setColor(0f,0f,0f,0.5f);
			px.fill();
			screen.dialogStage.addActor(new Image(new Texture(px)));
			screen.dialogStage.addActor(actor);
			actor.addAction(Actions.sequence(
					Actions.moveTo(actor.getX(), 200-actorHeight/2, 0.2f),
					Actions.moveTo(actor.getX(), 270-actorHeight/2, 0.1f)));
		}
	}
	
	//移除弹窗，如果移除完弹窗后，不需要后续操作，调用此方法
	public static void dismiss(){
		if (isShow) {
			DialogUtil.actor.addAction(Actions.sequence(
					Actions.moveTo(DialogUtil.actor.getX(), 200-DialogUtil.actorHeight/2, 0.1f),
					Actions.moveTo(DialogUtil.actor.getX(), 540, 0.2f),
					Actions.run(new Runnable() {
						@Override
						public void run() {
							isShow=false;
							DialogUtil.screen.dialogStage.clear();
							Gdx.input.setInputProcessor(screen.input);
							clear();
						}
					})));
		}
	}
	
	//移除弹窗，并且弹窗移除完之后，需要进行界面上的操作，调用此方法
	public static void dismiss(NextOperate operate){
		if (isShow) {
			isShow=false;
			DialogUtil.operate=operate;
			DialogUtil.actor.addAction(Actions.sequence(
					Actions.moveTo(DialogUtil.actor.getX(), 200-DialogUtil.actorHeight/2, 0.1f),
					Actions.moveTo(DialogUtil.actor.getX(), 540, 0.2f),
					Actions.run(new Runnable() {
						@Override
						public void run() {
							DialogUtil.screen.dialogStage.clear();
							Gdx.input.setInputProcessor(screen.input);
							clear();
							DialogUtil.operate.nextDone();
						}
					})));
		}
	}
	
	private static void clear(){
		screen=null;
		actor=null;
		inputMultiplexer.clear();
		inputMultiplexer=null;
		actorWidth=0;
		actorHeight=0;
	}
	
}
