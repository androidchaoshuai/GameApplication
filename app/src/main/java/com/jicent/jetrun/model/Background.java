package com.jicent.jetrun.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.jicent.jetrun.screen.GameScreen;
public class Background extends Group {
	private GameScreen screen;
	
	public Background(GameScreen screen) {
		this.screen=screen;
		init();
	}
	private void init() {
		this.clear();
		
		Layer layer=new Layer(0.5f, screen.getTexture("res/scene00.png"));
		this.addActor(layer);
		layer=new Layer(1f, screen.getTexture("res/scene02.png"));
		this.addActor(layer);
		layer=new Layer(0.8f, screen.getTexture("res/scene01.png"));
		this.addActor(layer);
		
	}
	class Layer extends Actor{
		
		public float layerSpeed=0;

		public float speedFac;
		private Texture texture;
		private float x1=0,x2=960;
		public boolean isOut=false;//处于最上一层，遮住人物
		
		
		public Layer(float speedFac,Texture texture){
			this.speedFac=speedFac;
			this.texture=texture;
		}
		@Override
		public void draw(Batch batch, float parentAlpha) {
			Color c=getColor();
			batch.setColor(c.r, c.g, c.b, c.a*parentAlpha);
			
			batch.draw(texture, x1, getY());
			batch.draw(texture, x2, getY());
			
		}
		@Override
		public void act(float delta) {
			super.act(delta);
			if (!screen.control.isStopGame&&!screen.hero.isStart()) {
				if(isOut){
					setZIndex(Background.this.getChildren().size-1);
				}
				layerSpeed=speedFac*screen.speed;
				
				x1-=layerSpeed;
				screen.widget.pixDistance+=layerSpeed;
				x2=x1+960;
				if(x1<=-960){
					x1=0;
				}
			}
		}
		@Override
		public String getName() {
			return "layer";
		}
	}
}
