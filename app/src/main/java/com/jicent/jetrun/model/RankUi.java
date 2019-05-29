package com.jicent.jetrun.model;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.jicent.jetrun.data.StaticVariable;
import com.jicent.jetrun.screen.FatherScreen;
import com.jicent.jetrun.screen.ShopScreen;
import com.jicent.jetrun.utils.HttpUtil;
import com.jicent.jetrun.utils.LayoutDebug;
import com.jicent.jetrun.utils.LogUtil;
import com.jicent.jetrun.utils.SPUtil;
import com.jicent.jetrun.utils.TextGenerateUtil;

public class RankUi extends Group{
	private FatherScreen screen;
	private ScrollPane pane;
	private boolean isShowRank;
	private JSONArray array;
	private ProgressDialog progressDialog;

	public RankUi(FatherScreen screen) {
		this.screen=screen;
		getArray();
		pane = new ScrollPane(null, new ScrollPane.ScrollPaneStyle(null,
				null, null, null, null));
		pane.setScrollingDisabled(true, false);
		this.addActor(pane);
	}
 
	@Override
	public void act(float delta) {
		super.act(delta);
		if (isShowRank) {
			Table table = new Table();
			JSONObject ret = null;
			TextureRegionDrawable[] itemBg=new TextureRegionDrawable[3];
			for (int i = 0; i < 3; i++) {
				itemBg[i]=new TextureRegionDrawable(new TextureRegion(screen.getTexture("res/rankBar"+i+".png")));
			}
			
			for (int i = 0; i < (array.length() - 1); i++) {
				try {
					ret = array.optJSONObject(i);
					if(ret.getString("name").equals(StaticVariable.name)){
						table.add(new RankItem(ret.getString("id"), ret.getString("name"), ret.getString("score"), ret.getString("ptype"), itemBg));
						table.row();
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			for (int i = 0; i < (array.length() - 1); i++) {
				try {
					ret = array.optJSONObject(i);
					if(ret.getString("name").equals(StaticVariable.name)){
						continue;
					}
					table.add(new RankItem(ret.getString("id"), ret.getString("name"), ret.getString("score"), ret.getString("ptype"), itemBg));
					table.row();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			for (Cell<Actor> cell : table.getCells()) {
				cell.pad(2);
			}
			pane.setWidget(table);
			pane.setBounds(0, 0, 422, 300);
			progressDialog.dismiss();
			isShowRank = false;
		}
	}
	
	public void getArray() {
		screen.main.getActivity().handler.post(new Runnable() {

			@Override
			public void run() {
				progressDialog = ProgressDialog.show(screen.main.getActivity(),
						"获取数据", "正在获取网络数据……");
				new Thread() {

					public void run() {
						String ptype=null;
						switch (StaticVariable.roleKind) {
						case 0:
							if (StaticVariable.isBuyPet0) {
								ptype="01";
							}else {
								ptype="00";
							}
							break;
						case 1:
							if (StaticVariable.isBuyPet1) {
								ptype="11";
							}else {
								ptype="10";
							}
							break;
						case 2:
							if (StaticVariable.isBuyPet2) {
								ptype="21";
							}else {
								ptype="20";
							}
							break;
						case 3:
							if (StaticVariable.isBuyPet3) {
								ptype="31";
							}else {
								ptype="30";
							}
							break;
						case 4:
							if (StaticVariable.isBuyPet4) {
								ptype="41";
							}else {
								ptype="40";
							}
							break;
						default:
							break;
						}
						array = HttpUtil.getDataFromServer(HttpUtil.infoUrl,
								screen.main.getActivity(), StaticVariable.name,
								StaticVariable.bestScore + "", ptype);
						if (array == null) {
							progressDialog.dismiss();
							screen.main.getActivity().handler
									.sendEmptyMessage(5);
//							Label label=new Label("获取数据失败，\n请检查网络设置！", new Label.LabelStyle(screen.getBitmapFont("font/allfont.fnt"),Color.WHITE));
//							label.setPosition(580,230);
//							screen.mainStage.addActor(label); 
						} else {
//							StaticVariable.isNewRecord = false;
//							SPUtil.commit(screen.main.getSp(), "isNewRecord", false);
							isShowRank = true;
						}
					}
				}.start();
			}
		});
	}
	
	class RankItem extends Table{

		public RankItem(String id, String name, String score, String ptype,TextureRegionDrawable[] bg){
			int tempId=Integer.parseInt(id);
			if (name.equals(StaticVariable.name)) {
				setBackground(bg[2]);
				StaticVariable.myOrder=tempId;
			}else {
				if (tempId<11) {
					setBackground(bg[0]);
				}else {
					setBackground(bg[1]);
				}
			}
			if (tempId<4) {
				Image orderIcon=null;
				if (tempId==1) {
					orderIcon=new Image(screen.getTexture("res/cup0.png"));
				}else if (tempId==2) {
					orderIcon=new Image(screen.getTexture("res/cup1.png"));
				}else if (tempId==3) {
					orderIcon=new Image(screen.getTexture("res/cup2.png"));
				}
				orderIcon.setPosition(35,3);
				this.addActor(orderIcon);
			}
			if(Integer.parseInt(id)>3){
				Label orderLabel=new Label(id, new Label.LabelStyle(screen.getBitmapFont("font/allfont.fnt"),Color.WHITE));
				if (tempId>11) {
					orderLabel.setFontScale(0.6f,1.0f);
				}
				orderLabel.setPosition(45-orderLabel.getPrefWidth()/2, 19-orderLabel.getPrefHeight()/2);
				this.addActor(orderLabel);
			}
			
//			BitmapFont font=TextGenerateUtil.getFont(name);
			BitmapFont font=screen.getBitmapFont("font/allfont.fnt");
			Label nameLabel=new Label(name, new Label.LabelStyle(font,Color.RED));
			nameLabel.setFontScale(0.6f);
			nameLabel.setPosition(146-nameLabel.getPrefWidth()/2, 17);
			this.addActor(nameLabel);
			
			Label scoreLabel=new Label(score, new Label.LabelStyle(screen.getBitmapFont("font/allfont.fnt"),Color.WHITE));
			scoreLabel.setFontScale(0.6f,1.0f);
			scoreLabel.setPosition(270-scoreLabel.getPrefWidth()/2, 19-scoreLabel.getPrefHeight()/2);
			this.addActor(scoreLabel);
			
			//添加角色头像和宠物头像
//			Image roleIcon=null;
//			Image petIcon=null;
//			int tempType=0;
//			if (!ptype.equals("null")) {
//				tempType=Integer.parseInt(ptype);
//			}
//			int roleIndex=tempType/10;
//			int petIndex=tempType%10;
//			switch (roleIndex) {
//			case 0:
//				roleIcon=new Image(screen.getTexture("res/role0Icon.png"));
//				if (petIndex==0) {//没有宠物
//					petIcon=new Image(screen.getTexture("res/role0Pet0.png"));
//				}else {
//					petIcon=new Image(screen.getTexture("res/role0Pet1.png"));
//				}
//				break;
//			case 1:
//				roleIcon=new Image(screen.getTexture("res/role1Icon.png"));
//				if (petIndex==0) {
//					petIcon=new Image(screen.getTexture("res/role1Pet0.png"));
//				}else {
//					petIcon=new Image(screen.getTexture("res/role1Pet1.png"));
//				}
//				break;
//			case 2:
//				roleIcon=new Image(screen.getTexture("res/role2Icon.png"));
//				if (petIndex==0) {
//					petIcon=new Image(screen.getTexture("res/role2Pet0.png"));
//				}else {
//					petIcon=new Image(screen.getTexture("res/role2Pet1.png"));
//				}
//				break;
//			case 3:
//				roleIcon=new Image(screen.getTexture("res/role3Icon.png"));
//				if (petIndex==0) {
//					petIcon=new Image(screen.getTexture("res/role3Pet0.png"));
//				}else {
//					petIcon=new Image(screen.getTexture("res/role3Pet1.png"));
//				}
//				break;
//			case 4:
//				roleIcon=new Image(screen.getTexture("res/role4Icon.png"));
//				if (petIndex==0) {
//					petIcon=new Image(screen.getTexture("res/role4Pet0.png"));
//				}else {
//					petIcon=new Image(screen.getTexture("res/role4Pet1.png"));
//				}
//				break;
//
//			default:
//				break;
//			}
//			roleIcon.setPosition(332, 2);
//			this.addActor(roleIcon);
//			petIcon.setPosition(363, 2);
//			this.addActor(petIcon);
			
		}
	}
}
