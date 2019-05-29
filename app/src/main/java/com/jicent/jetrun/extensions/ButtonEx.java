package com.jicent.jetrun.extensions;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.jicent.jetrun.screen.FatherScreen;
/**
 * 按钮特效类，负责实现按钮的效果,游戏中大部分按钮均为此类对象
 * @author yujia
 *
 */
public class ButtonEx extends Group {
	private boolean isDownActionDone,isTouched;
	private int isUpOnBounds;//0手指未抬起，1手指抬起并在控件范围内，-1手指抬起但是不在控件范围内
	private FatherScreen screen;
	private InputListenerEx inputListenerEx;
	private float scaleX,scaleY;
	private Image bgImg;
	
	public ButtonEx(FatherScreen screen,TextureEx textureEx){
		this.screen=screen;
		bgImg=new Image(textureEx);
		this.addActor(bgImg);
		setSize(bgImg.getWidth(), bgImg.getHeight());
		initialize();
	}
	
	public ButtonEx(FatherScreen screen,TextureRegion textureEx){
		this.screen=screen;
		bgImg=new Image(textureEx);
		this.addActor(bgImg);
		setSize(bgImg.getWidth(), bgImg.getHeight());
		initialize();
	}
	/**
	 * 按钮声明,直接原大小，居中
	 * @param screen 按钮所在的screen
	 * @param textureEx 按钮背景图片
	 * @param text 按钮文字
	 * @param font 使用的字体
	 * @param fontColor 字体颜色
	 */
	public ButtonEx(FatherScreen screen,TextureEx textureEx,String text,BitmapFont font,Color fontColor) {
		this(screen, textureEx);
		Label label = new Label(text, new LabelStyle(font, fontColor));
		label.setName("textLabel");
		label.setPosition((textureEx.getWidth()-label.getPrefWidth())/2, (textureEx.getHeight()-label.getPrefHeight())/2);
//		LogUtil.e(ButtonEx.class.getSimpleName(), (textureEx.getWidth()-label.getPrefWidth())/2+"  "+(textureEx.getHeight()-label.getPrefHeight())/2, true);
		this.addActor(label);
	}
	/**
	 * 按钮声明，按照缩放大小居中
	 * @param screen 按钮所在的screen
	 * @param textureEx 按钮背景图片
	 * @param text 按钮文字
	 * @param font 使用的字体
	 * @param fontColor 字体颜色
	 * @param scale 文字大小缩放比
	 */
	public ButtonEx(FatherScreen screen,TextureEx textureEx,String text,BitmapFont font,Color fontColor,float scale) {
		this(screen, textureEx);
		Label label = new Label(text, new LabelStyle(font, fontColor));
		float lastPrefHeight=label.getPrefHeight();
		label.setFontScale(scale);
		float currPrefHeight=label.getPrefHeight();
		//设置字体label居中
		label.setPosition((textureEx.getWidth()-label.getPrefWidth())/2, (textureEx.getHeight()+lastPrefHeight)/2-currPrefHeight);
		this.addActor(label);
	}
	/**
	 * 按钮声明
	 * @param screen 按钮所在的screen
	 * @param textureEx 按钮背景图片
	 * @param textT 按钮内容图片
	 */
	public ButtonEx(FatherScreen screen,TextureEx textureEx,TextureEx textT) {
		this(screen, textureEx);
		Image text=new Image(textT);
		text.setPosition((textureEx.getWidth()-text.getWidth())/2, (textureEx.getHeight()-text.getHeight())/2);
		this.addActor(text);
	}
	/**
	 * 按钮声明，自定义大小和位置
	 * @param screen 按钮所在的screen
	 * @param textureEx 按钮背景图片
	 * @param text 按钮文字
	 * @param font 使用的字体
	 * @param fontColor 字体颜色
	 * @param scale 文字大小缩放比
	 * @param x 文字在按钮中的位置
	 * @param y 文字在按钮中的位置
	 */
	public ButtonEx(FatherScreen screen,TextureEx textureEx,String text,BitmapFont font,Color fontColor,float scale,float x, float y) {
		this(screen, textureEx);
		Label label = new Label(text, new LabelStyle(font, fontColor));
		label.setFontScale(scale);
		label.setPosition(x, y);
		this.addActor(label);
	}
	
	/**
	 * 按钮声明
	 * @param screen 按钮所在的screen
	 * @param textureEx 按钮背景图片
	 * @param textT 按钮内容图片
	 * @param x 文字在按钮中的位置
	 * @param y 文字在按钮中的位置
	 */
	public ButtonEx(FatherScreen screen,TextureEx textureEx,TextureEx textT,float x, float y) {
		this(screen, textureEx);
		Image text=new Image(textT);
		text.setPosition(x,y);
		this.addActor(text);
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		if (isDownActionDone) {
			if (isUpOnBounds==1) {
				this.addAction(Actions.sequence(Actions.scaleTo(scaleX, scaleY, 0.1f),Actions.run(new Runnable() {
					
					@Override
					public void run() {
						isTouched=false;
						if (inputListenerEx!=null) {
							inputListenerEx.touchUp(ButtonEx.this);
						}						
					}
				})));
				isDownActionDone=false;
				isUpOnBounds=0;
			} else if(isUpOnBounds==-1){
				this.addAction(Actions.sequence(Actions.scaleTo(scaleX-0.1f,scaleY-0.1f, 0.1f),Actions.scaleTo(scaleX+0.05f, scaleY+0.05f, 0.08f),Actions.scaleTo(scaleX, scaleY, 0.05f),Actions.run(new Runnable() {
					
					@Override
					public void run() {
						isTouched=false;
					}
				})));
				isDownActionDone=false;
				isUpOnBounds=0;
			}
		}
	}
	
	private void initialize () {
		setOrigin(bgImg.getWidth()/2,bgImg.getHeight()/2);
		addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				if (pointer==0&&!isTouched) {
					
					if (inputListenerEx!=null&&inputListenerEx.touchDown(ButtonEx.this)) {
						isTouched=true;
						ButtonEx.this.addAction(Actions.sequence(Actions.scaleTo(scaleX+0.1f, scaleY+0.1f, 0.1f),Actions.run(new Runnable() {
							
							@Override
							public void run() {
								isDownActionDone=true;
							}
						})));
						return true;
					}
//					if (inputListenerEx!=null) {
//						inputListenerEx.touchDown(ButtonEx.this);
//					}
//					ButtonEx.this.addAction(Actions.sequence(Actions.scaleTo(scaleX+0.1f, scaleY+0.1f, 0.1f),Actions.run(new Runnable() {
//						
//						@Override
//						public void run() {
//							isDownActionDone=true;
//						}
//					})));
//					return true;
				}
				return false;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if (screen.isOnBound(event.getListenerActor(), ButtonEx.this, x, y)) {
					isUpOnBounds=1;
				}else {
					isUpOnBounds=-1;
				}
			}
		});
	}

	//给按钮添加自己声明的监听器
	public void addListener(InputListenerEx inputListenerEx){
		this.inputListenerEx=inputListenerEx;
	}
	
	public void setText(String text){
		Label label=findActor("textLabel");
		label.setText(text);
	}
	
	public String getText(){
		Label label=findActor("textLabel");
		return label.getText().toString();
	}
	
	@Override
	public void setSize(float width, float height) {
		super.setBounds(this.getX()+(width-bgImg.getWidth())/2, this.getY()+(height-bgImg.getHeight())/2, bgImg.getWidth(), bgImg.getHeight());
		scaleX=width/bgImg.getWidth();
		scaleY=height/bgImg.getHeight();
		this.setScale(scaleX, scaleY);
	}
	@Override
	public void setBounds(float x, float y, float width, float height) {
		super.setBounds(x+(width-bgImg.getWidth())/2, y+(height-bgImg.getHeight())/2, bgImg.getWidth(), bgImg.getHeight());
		scaleX=width/bgImg.getWidth();
		scaleY=height/bgImg.getHeight();
		this.setScale(scaleX, scaleY);
	}
	
	/**
	 * 监听器类，负责按钮的监听
	 * @author yujia
	 *
	 */
	public interface InputListenerEx{
		public boolean touchDown (Actor actor);
		public void touchUp (Actor actor);
	}
	
}
