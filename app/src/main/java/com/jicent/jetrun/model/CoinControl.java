package com.jicent.jetrun.model;

import java.util.LinkedList;
import java.util.List;

import android.graphics.PointF;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.jicent.jetrun.extensions.CollisionPolygon;
import com.jicent.jetrun.screen.GameScreen;
import com.jicent.jetrun.utils.AnimateFactory;
import com.jicent.jetrun.utils.SoundUtil;

public class CoinControl extends Group{
	private GameScreen screen;
	private List<CoinShape> coinShapes;
	private List<PointF> coinList;
	private Animation coinAnima,eatAnima;
	
	public CoinControl(GameScreen screen) {
		this.screen=screen;
		coinShapes=new LinkedList<CoinShape>();
		coinList=new LinkedList<PointF>();
		coinAnima=AnimateFactory.getAnimate(screen, "res/coin.png", 24, 30, 0.06f, PlayMode.LOOP);
		eatAnima=AnimateFactory.getAnimate(screen, "res/coin1.png", 32,49, 0.05f, PlayMode.NORMAL);
	}
	
	protected void addShape(CoinShape shape){
		coinShapes.add(shape);
	}
	
	protected void addCoin(PointF pointF) {
		coinList.add(pointF);
	}
	
	@Override
	public void act(float delta) {
		if (!screen.control.isStopGame) {
			super.act(delta);
			for (int i = coinShapes.size()-1; i >=0; i--) {
				CoinShape shape=coinShapes.get(i);
				float positionX=shape.leftEgde-screen.map.offset;
				if (positionX<960) {//相对位置进入屏幕时，加入相应的对象
					if (!screen.laser.isProductLaser&&!screen.laser.hasChildren()) {
						//将该形状中所有的金币点都加到待绘制的数组中
						coinList.addAll(shape.getPointFList());
					}
					shape.pointClear();
					coinShapes.remove(i);
				}
			}
			for (int i = coinList.size()-1; i >=0; i--) {
				PointF pointF=coinList.get(i);
				float positionX=pointF.x-screen.map.offset;
				if (positionX<960) {//相对位置进入屏幕时，加入相应的对象
					this.addActor(new Coin(positionX,pointF.y));
					coinList.remove(i);
				}
			}
		}
	}
	
	@Override
	public boolean hasChildren() {
		return (super.hasChildren()||!coinList.isEmpty());
	}
	
	public void dispose(){
		coinList.clear();
		coinList=null;
		//先清理子对象中的集合数组
		for (int i = 0,length=coinShapes.size(); i < length; i++) {
			coinShapes.get(i).getPointFList().clear();
		}
		coinShapes.clear();
		coinShapes=null;
	}
	
	class Coin extends Actor{
		private CollisionPolygon coinPolygon;
		
		TextureRegion tr;
		float time=MathUtils.random(0f, 0.3f),dTime=0;
		boolean isEat=false;//已吃
		TextureRegion isEatT;
		private boolean isRotate=true;
		private float isRotateTime=1.5f;
		
		public Coin(float x, float y) {
			setBounds(x,y,24, 30);
			coinPolygon=new CollisionPolygon(new float[]{0,0,0,getHeight(),getWidth(),getHeight(),getWidth(),0});
			
		}
		 
		@Override
		public void draw(Batch batch, float parentAlpha) {
			Color color=this.getColor();
			batch.setColor(color.r,color.g,color.b,color.a*parentAlpha);
			if(isEat){
				isEatT=eatAnima.getKeyFrame(dTime);
				batch.draw(isEatT, getX()+12-16, getY()+15-24.5f);
				if(eatAnima.isAnimationFinished(dTime)){
					remove();
				}
			}else{
				tr=coinAnima.getKeyFrame(time);
				batch.draw(tr, getX(), getY(), getWidth(), getHeight());	
			}
		}
		
		@Override
		public void act(float delta) {
			if(isRotate){
				time+=delta;
				if(time>=coinAnima.getAnimationDuration()){
					isRotate=false;
					isRotateTime=0.6f;
				}
			}else{
				isRotateTime-=delta;
				if(isRotateTime<=0){
					isRotate=true;
					time=0;
				}
			} 
			
			if (!screen.control.isStopGame&&!screen.hero.isStart()) {
				super.act(delta);
				if(isEat){
					dTime+=delta;
				}
				moveBy(-screen.speed, 0);
				if (getX()<-getWidth()) {//相对位置移出屏幕时，移除相应的对象
					this.remove();
				}else {
					if (screen.hero.isAttract()||screen.hero.isSpeedUp()) {
						attractCoin();
					}
					if(!isEat)
					if (!(screen.hero.getX()+screen.hero.getWidth()<getX()||screen.hero.getX()>getX()+getWidth())) {
						coinPolygon.setPosition(getX(), getY());
						if (coinPolygon.overlaps(screen.hero.getPolygon())) {
							SoundUtil.playSound(screen.main.getManager(), "coin");
							screen.widget.addCoinNum(1);
							isEat=true;
//							this.remove();
						}
					}
				}
			}
		}

		private void attractCoin() {	
			float tempCoinX=getX()+getWidth()/2;
			float tempHeroX=screen.hero.getX()+screen.hero.getWidth()/2;
			if (Math.abs(tempCoinX-tempHeroX)<400f) {
				if (tempCoinX>tempHeroX) {
					this.moveBy(-((tempCoinX-tempHeroX)/5f+5), -(getY()+getHeight()/2-(screen.hero.getY()+screen.hero.getHeight()/2))/5);
				}else {
					this.moveBy(screen.speed+(-tempCoinX+tempHeroX)/10f+5, -(getY()+getHeight()/2-(screen.hero.getY()+screen.hero.getHeight()/2))/5);
				}
			}
		}
	}
}
