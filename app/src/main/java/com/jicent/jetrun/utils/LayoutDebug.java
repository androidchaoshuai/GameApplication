package com.jicent.jetrun.utils;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

public class LayoutDebug implements InputProcessor{

	private Stage stage;
	private Group group;
	private InputProcessor lastInput;
	private BitmapFont font;
	private DebugActor debugActor;
	
	private Actor actors[];
	private int debugIndex=0;
	public LayoutDebug(Stage stage,Actor ...actors ){
		this.actors=actors;
		this.stage=stage;
		init();
	}
	public LayoutDebug(Group group,Actor ...actors ){
		this.actors=actors;
		this.group=group;
		init();
	}
	public void setFont(BitmapFont font){
		this.font=font;
		debugActor.addFont();
	}
	private void init(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				LayoutDebug.this.lastInput=Gdx.input.getInputProcessor();
				Gdx.input.setInputProcessor(LayoutDebug.this);
			}
		}).start();
		debugActor=new DebugActor();
		if(stage!=null)
			stage.addActor(debugActor);
		if(group!=null)
			group.addActor(debugActor);
	}
	
	@Override
	public boolean keyDown(int keycode) {
		switch (keycode) {
		case Input.Keys.UP:
			actors[debugIndex].moveBy(0, 1);
			break;
		case Input.Keys.DOWN:
			actors[debugIndex].moveBy(0, -1);
			break;
		case Input.Keys.LEFT:
			actors[debugIndex].moveBy(-1, 0);
			break;
		case Input.Keys.RIGHT:
			actors[debugIndex].moveBy(1, 0);
			break;
		case Input.Keys.NUM_8:
			actors[debugIndex].moveBy(0, 10);
			break;
		case Input.Keys.NUM_5:
			actors[debugIndex].moveBy(0, -10);
			break;
		case Input.Keys.NUM_4:
			actors[debugIndex].moveBy(-10, 0);
			break;
		case Input.Keys.NUM_6:
			actors[debugIndex].moveBy(10, 0);
			break;
		case Input.Keys.NUM_1:
			actors[debugIndex].setHeight(actors[debugIndex].getHeight()+5);
			break;
		case Input.Keys.NUM_0:
			actors[debugIndex].setHeight(actors[debugIndex].getHeight()-5);
			break;
		case Input.Keys.NUM_3:
			actors[debugIndex].setWidth(actors[debugIndex].getWidth()+5);
			break;
		case Input.Keys.NUM_2:
			actors[debugIndex].setWidth(actors[debugIndex].getWidth()-5);
			break;
		case Input.Keys.NUM_7:
			actors[debugIndex].setRotation(actors[debugIndex].getRotation()+1);
			break;
		case Input.Keys.NUM_9:
			actors[debugIndex].setRotation(actors[debugIndex].getRotation()-1);
			break;
		case Input.Keys.MINUS:
			actors[debugIndex].setScale(actors[debugIndex].getScaleX()-0.1f);
			break;
		case Input.Keys.EQUALS:
			actors[debugIndex].setScale(actors[debugIndex].getScaleX()+0.1f);
			break;
		case Input.Keys.ENTER:
		case Input.Keys.MENU:
			debugActor.remove();
			debugActor=null;
			Gdx.input.setInputProcessor(lastInput);
			break;
		case Input.Keys.TAB:
		case Input.Keys.BACK:
			debugIndex++;
			if(debugIndex>=actors.length)debugIndex=0;
			break;
		default:
			break;
		}
		System.out.println(keycode);
		outPut();
		return false;
	}
	private void outPut(){
		System.out.println("actors["+debugIndex+"] x:"+(int)actors[debugIndex].getX()+" y:"+(int)actors[debugIndex].getY()+"----------\n" +
				"w:"+actors[debugIndex].getWidth()+" h:"+actors[debugIndex].getHeight()+"\n" +
				"Rotate:"+actors[debugIndex].getRotation()+" Scale:"+actors[debugIndex].getScaleX());
	}
	
	class DebugActor extends Actor{
		
		private TextureRegion tr;
		private Label infoLabel;
		
		public DebugActor(){
			Pixmap pix=new Pixmap(1,1,Format.RGBA8888);
			pix.setColor(0,0,0, 0.5f);
			pix.fill();
			tr=new TextureRegion(new Texture(pix));
			
			if(font!=null){
				addFont();
			}
		}
		public void addFont(){
			infoLabel=new Label("XY", new LabelStyle(font, Color.RED));
			infoLabel.setPosition(0,infoLabel.getTextBounds().height/2);
		}
		@Override
		public void act(float delta) {
			super.act(delta);
			if(stage!=null)
				setZIndex(stage.getActors().size-1);
			if(group!=null)
				setZIndex(group.getChildren().size-1);
			
			setBounds(actors[debugIndex].getX(),actors[debugIndex].getY(),(actors[debugIndex].getWidth()==0)?50:actors[debugIndex].getWidth(), (actors[debugIndex].getHeight())==0?50:actors[debugIndex].getHeight());
			
			if(infoLabel!=null)
				infoLabel.setText("X="+(int)getX()+" Y="+(int)getY());
		}
		@Override
		public void draw(Batch batch, float parentAlpha) {
			Color c=getColor();
			batch.setColor(c.r,c.g,c.b,c.a*parentAlpha);
			batch.draw(tr,getX(),getY(),getWidth(),getHeight());
			
			if(infoLabel!=null)
				infoLabel.draw(batch, parentAlpha);
		}
	}
	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	private final Rectangle upRect=new Rectangle(300,270,360,270);
	private final Rectangle downRect=new Rectangle(300,0,360,270);
	private final Rectangle leftRect=new Rectangle(0,0,300,540);
	private final Rectangle rightRect=new Rectangle(660,0,300,540);
	private final float factor=960f/Gdx.graphics.getWidth();
	private final float moveFactor=0.2f;
	float lastX,lastY;
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		float touchX=screenX*factor;
		float touchY=(540-screenY*factor);
		lastX=screenX*moveFactor;
		lastY=screenY*moveFactor;
		
//		System.out.println(screenY+"  "+screenX*factor+" "+(540-screenY*factor));
		if(upRect.contains(touchX, touchY)){
			actors[debugIndex].moveBy(0, 1);
		}else if(downRect.contains(touchX, touchY)){
			actors[debugIndex].moveBy(0, -1);
		}else if(leftRect.contains(touchX, touchY)){
			actors[debugIndex].moveBy(-1, 0);
		}else if(rightRect.contains(touchX, touchY)){
			actors[debugIndex].moveBy(1, 0);
		}
		outPut();
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		actors[debugIndex].moveBy(screenX*moveFactor-lastX, lastY-screenY*moveFactor);
		lastX=screenX*moveFactor;
		lastY=screenY*moveFactor;
		outPut();
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}

