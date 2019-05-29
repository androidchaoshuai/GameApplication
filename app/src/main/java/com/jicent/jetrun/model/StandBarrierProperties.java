package com.jicent.jetrun.model;

public class StandBarrierProperties {

	private float x;
	private float y;
	private float width,height;
	private float rotation;
	
	public StandBarrierProperties(float x, float y, float width, float height, float rotation) {
		this.rotation=rotation;
		this.x=x;
		this.y=y;
		this.width=width;
		this.height=height;
	}
	
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getRotation() {
		return rotation;
	}
	public void setRotation(float rotation) {
		this.rotation = rotation;
	}
	
	
}
