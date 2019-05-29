package com.jicent.jetrun.extensions;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;

public class CollisionPolygon extends Polygon{
	
	public CollisionPolygon() {
		super();
	}
	
	public CollisionPolygon(float[] vertices) {
		super(vertices);
	}
	
	public CollisionPolygon(float[] vertices,float x, float y, float originX, float originY, float rotation) {
		super(vertices);
		setOrigin(originX, originY);
		setPosition(x, y);
		setRotation(rotation);
	}
	
	public CollisionPolygon(float[] vertices, float originX, float originY, float rotation) {
		this(vertices, 0, 0, originX, originY, rotation);
	}
	
	public CollisionPolygon(float[] vertices, float x, float y) {
		this(vertices, x, y, 0, 0, 0);
	}
	
	public CollisionPolygon(float[] vertices,float x, float y, float originX, float originY) {
		this(vertices, x, y, originX, originY, 0);
	}
	
	public boolean overlaps(Rectangle rect){
		float[] f={rect.x,rect.y,rect.x+rect.width,rect.y,rect.x+rect.width,rect.y+rect.height,rect.x,rect.y+rect.height};
		Polygon polygon=new Polygon(f);
		float[] vertices=polygon.getTransformedVertices();
		for (int i = 0; i < vertices.length; i+=2) {
			if (contains(vertices[i], vertices[i+1])) {             //坐标是x，y  因此可以判断是否包含点
				return true;
			}
		}
		vertices=getTransformedVertices();
		for (int i = 0; i < vertices.length; i+=2) {
			if (polygon.contains(vertices[i], vertices[i+1])) {
				return true;
			}
		}
		
		return false;
	}
	public boolean overlaps(Polygon polygon){
		float[] vertices=polygon.getTransformedVertices();
		for (int i = 0; i < vertices.length; i+=2) {
			if (contains(vertices[i], vertices[i+1])) {
				return true;
			}
		}
		vertices=getTransformedVertices();
		for (int i = 0; i < vertices.length; i+=2) {
			if (polygon.contains(vertices[i], vertices[i+1])) {    //坐标是x，y  因此可以判断是否包含点
				return true;
			}
		}
		
		return false;
	}
}
