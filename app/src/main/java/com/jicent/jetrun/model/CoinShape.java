package com.jicent.jetrun.model;

import java.util.LinkedList;
import java.util.List;

import android.graphics.PointF;
/**
 * 金币形状的模型，存储该形状中金币的相对于屏幕的坐标点
 * 用于控制金币组合的形状不被破坏
 * @author yujia
 *
 */
public class CoinShape {
	private String shapeName;
	private List<PointF> pointList;
	protected float leftEgde;
	
	public CoinShape(String name) {
		this.shapeName=name;
		pointList=new LinkedList<PointF>();
	}
	
	public void addPointF(float x, float y){
		if (pointList.isEmpty()) {
			leftEgde=x;
		}else {
			if (x<leftEgde) {
				leftEgde=x;
			}
		}
		pointList.add(new PointF(x, y));
	}
	
	public List<PointF> getPointFList(){
		return pointList;
	}
	
	public void pointClear(){
		pointList.clear();
		pointList=null;
	}
}
