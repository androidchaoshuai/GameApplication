package com.jicent.jetrun.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.jicent.jetrun.screen.FatherScreen;
/*
 * 骨骼动画 
 */
public class SkeletonActor extends Actor{
	private FatherScreen screen;
	protected Skeleton skeleton;
	protected SkeletonRenderer renderer;
	protected AnimationState animationState;
	protected AnimationStateData stateData;
//	protected SkeletonBounds skeletonBounds;
	private float halfWidth;//单帧图片的一半宽度
	protected float halfHeight;//单帧图片一半的高度
	protected boolean isInit;
	
	public SkeletonActor(FatherScreen screen) {
		this.screen=screen;
	}
	
	public SkeletonActor(FatherScreen screen, String atlasPath,String jsonPath) {
		this(screen);
		setFile(atlasPath, jsonPath);
//		TextureAtlas atlas=new TextureAtlas(Gdx.files.internal(atlasPath));
//		SkeletonJson json=new SkeletonJson(atlas);
//		SkeletonData skeletonData=json.readSkeletonData(Gdx.files.internal(jsonPath));
//		skeleton=new Skeleton(skeletonData);
//		stateData=new AnimationStateData(skeletonData);
//		animationState=new AnimationState(stateData);
//		animationState=new AnimationState(new AnimationStateData(skeletonData));
//		renderer=new SkeletonRenderer();
//		skeletonBounds = new SkeletonBounds();
		
	}
	
	protected void setFile(String atlasPath,String jsonPath) {
		isInit=true;
//		TextureAtlas atlas=new TextureAtlas(Gdx.files.internal(atlasPath));
		TextureAtlas atlas=screen.getAtlas(atlasPath);
		SkeletonJson json=new SkeletonJson(atlas);
		json.setScale(0.8f);
		SkeletonData skeletonData=json.readSkeletonData(Gdx.files.internal(jsonPath));
		skeleton=new Skeleton(skeletonData);
		stateData=new AnimationStateData(skeletonData);
		animationState=new AnimationState(stateData);
		renderer=new SkeletonRenderer();
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
//		batch.setColor(Color.RED);
		if (isInit) {
			Color color = this.getColor();
			skeleton.setColor(new Color(color.r, color.g, color.b, color.a * parentAlpha));
			renderer.draw(batch, skeleton);
		}
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		if (isInit) {
			animationState.update(delta);
			animationState.apply(skeleton);
			skeleton.updateWorldTransform();
			skeleton.setPosition(getX()+halfWidth, getY()+halfHeight);
		}
//		skeletonBounds.update(skeleton, true);
	}
	
	@Override
	public void setBounds(float x, float y, float width, float height) {
		super.setBounds(x, y, width, height);
		halfWidth=width/2;
	}
	
	@Override
	public void setSize(float width, float height) {
		super.setSize(width, height);
		halfWidth=width/2;
	}
	
}
