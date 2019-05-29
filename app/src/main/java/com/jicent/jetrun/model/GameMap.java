package com.jicent.jetrun.model;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.jicent.jetrun.screen.GameScreen;

public class GameMap extends Actor{
	private GameScreen screen;
	private float layerWidth;//需要加载的地图的宽度
	private float mapOffset;//地图中金币应该加上的偏移量
	private int mapIndex;//地图的索引值
	
	protected float offset;//镜头中心点相对于地图的移动距离
	
	public GameMap(GameScreen screen) {
		this.screen=screen;
		getData(mapIndex);
	}

	private void getData(int index) {
		TiledMap map=screen.main.getManager().get("map/map"+index+".tmx");
		MapProperties properties=map.getProperties();
		layerWidth=properties.get("width", Integer.class)*properties.get("tilewidth", Integer.class);
		MapLayers layers=map.getLayers();
		for (MapLayer mapLayer : layers) {
			MapObjects objects=mapLayer.getObjects();
			if (mapLayer.getName().startsWith("gold")) {
				CoinShape shape=new CoinShape("");
				for (MapObject mapObject : objects) {
					properties=mapObject.getProperties();
					//将每个金币的坐标点加到对应的金币形状中
					shape.addPointF(properties.get("x", Float.class)+mapOffset, properties.get("y", Float.class));
				}
				screen.coinControl.addShape(shape);
			}else {
				for (MapObject mapObject : objects) {
					properties=mapObject.getProperties();
					if(mapObject.getName().equals("standBarrier")){
						if (properties.containsKey("rotation")) {
							screen.standBarrierControl.addBarrier(new StandBarrierProperties(properties.get("x", Float.class)+mapOffset, properties.get("y", Float.class), properties.get("width", Float.class), properties.get("height", Float.class), -properties.get("rotation", Float.class)));//对象添加到stage中的偏移量
						}else {
							screen.standBarrierControl.addBarrier(new StandBarrierProperties(properties.get("x", Float.class)+mapOffset, properties.get("y", Float.class), properties.get("width", Float.class), properties.get("height", Float.class), 0));//对象添加到stage中的偏移量
						}
					} else if(mapObject.getName().equals("rotationBarrier")){
						screen.rotationBarrierControl.addBarrier(new Rectangle(properties.get("x", Float.class)+mapOffset, properties.get("y", Float.class),
								properties.get("width", Float.class), properties.get("height", Float.class)));//对象添加到stage中的偏移量
					}
				}
			}
		}
		mapOffset+=layerWidth;//读取完了之后，偏移量增加，以便下一个地图中的对象进行位置偏移的计算
	}
	
	@Override
	public void act(float delta) {
		if (!screen.control.isStopGame) {
			super.act(delta);
			if (!screen.hero.isStart()) {
				offset+=screen.speed;//镜头中心相对于地图向右移动的距离
				if (offset>mapOffset-960f) {//提前一屏把下一场景的金币坐标添加进来，防止突然出现
					mapIndex++;
					if (mapIndex>29) {
						mapIndex=0;
					}
					getData(mapIndex);
				}
			}
		}
	}
	
}
