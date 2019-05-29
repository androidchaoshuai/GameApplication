package com.jicent.jetrun.model;

import android.util.Log;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g3d.particles.influencers.DynamicsModifier.CentripetalAcceleration;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.TimeUtils;
import com.jicent.jetrun.data.StaticVariable;
import com.jicent.jetrun.extensions.ButtonEx;
import com.jicent.jetrun.extensions.ButtonEx.InputListenerEx;
import com.jicent.jetrun.screen.ShopScreen;
import com.jicent.jetrun.utils.PayUtil.PayType;
import com.jicent.jetrun.utils.LayoutDebug;
import com.jicent.jetrun.utils.SoundUtil;
/*
 * 人物选择界面
 */
public class RoleShow extends Group implements InputListenerEx{
	private ShopScreen screen;
	private ButtonEx leftBtn,rightBtn;
	public Image petImg;
	public Image roleImg;
	private Image imgCoinBg;
	private Label labelCoinNum;
	private Image infoImg;
	
	
	public RoleShow(ShopScreen screen) {
		this.screen=screen;
		
		petImg = new Image();  //宠物  --jn
		addActor(petImg);
		
		Image magicImg = new Image(screen.getTexture("res/magic0.png"));
		magicImg.setPosition(411,123);
		addActor(magicImg);
		roleImg = new Image();
		addActor(roleImg);
		Image magicImg1 = new Image(screen.getTexture("res/magic1.png"));
		magicImg1.setPosition(417,131);
		magicImg1.setTouchable(Touchable.disabled);
		addActor(magicImg1);
		
		
		
		Image infoBg= new Image(screen.getTexture("res/infoBg.png"));
		infoBg.setPosition(707,161);
		addActor(infoBg);
		
		infoImg= new Image(screen.getTexture("res/roleInfo"+StaticVariable.roleKind+".png"));
		infoImg.setPosition(707,161);
		addActor(infoImg);
		
		changeRole(StaticVariable.roleKind);
		
		TextureRegion region=new TextureRegion(screen.getTexture("res/arrow.png"));
		region.flip(true, false);
		
		leftBtn = new ButtonEx(screen, region);
		leftBtn.setPosition(302,213);
		leftBtn.addListener(this);
		addActor(leftBtn);
		
		rightBtn = new ButtonEx(screen, screen.getTexture("res/arrow.png"));
		rightBtn.setPosition(604,213);
		rightBtn.addListener(this);
		addActor(rightBtn);

		
		
		//金币显示  --jn
		imgCoinBg = new Image(screen.getTexture("res/coinBar.png"));
		imgCoinBg.setPosition(35,471);
		addActor(imgCoinBg);
		
		labelCoinNum = new Label(""+StaticVariable.coinNum, new LabelStyle(screen.getBitmapFont("font/scoreFont.fnt"), Color.WHITE));
		labelCoinNum.setFontScale(0.5f);
		labelCoinNum.setPosition(140-labelCoinNum.getTextBounds().width/2,499);
		addActor(labelCoinNum);  
		
//		new LayoutDebug(this,infoImg,infoBg).setFont(screen.getBitmapFont("font/allfont.fnt"));
	}
	
	//动态刷新金币数量  --jn
	public void updateCoin(){
		labelCoinNum.setText(""+StaticVariable.coinNum);
		labelCoinNum.setPosition(140-labelCoinNum.getTextBounds().width/2,499);
	}
	//通过选中得角色来设置开始按钮状态
	public void setStartBtnStatusBySelectedRole(){
		ButtonEx startBtn = screen.shopButton.getStartBtn();
		switch (StaticVariable.roleKind) {
		case StaticVariable.role0:
			startBtn.setVisible(true);
			break;
		case StaticVariable.role1:
			if(StaticVariable.isBuyRole1){
				startBtn.setVisible(true);
			}else{
				startBtn.setVisible(false);
			}
			break;
		case StaticVariable.role2:
			if(StaticVariable.isBuyRole2){
				startBtn.setVisible(true);
			}else{
				startBtn.setVisible(false);
			}
			break;
		case StaticVariable.role3:
			if(StaticVariable.isBuyRole3){
				startBtn.setVisible(true);
			}else{
				startBtn.setVisible(false);
			}
			break;
		case StaticVariable.role4:
			if(StaticVariable.isBuyRole4){
				startBtn.setVisible(true);
			}else{
				startBtn.setVisible(false);
			}
			break;
		
		}
	}
	
	//更换角色和宠物的图片
	private void changeRole(int roleKind) {
		TextureRegionDrawable drawable=null;
		
		float rolePtX = 408f;
		float rolePtY = 140f;
		
		float petPtX = 546;
		float petPtY = 285;
		
		switch (roleKind) {
		case StaticVariable.role0:
			drawable=new TextureRegionDrawable(new TextureRegion(screen.getTexture(StaticVariable.isBuyPet0 ? "res/pet0Show.png" : "res/pet0ShowNot.png")));
			petImg.setDrawable(drawable);
			petImg.setBounds(petPtX,petPtY,drawable.getMinWidth(),drawable.getMinHeight());
			
			drawable=new TextureRegionDrawable(new TextureRegion(screen.getTexture("res/role0.png")));
			roleImg.setDrawable(drawable);
			roleImg.setBounds(rolePtX,rolePtY,drawable.getMinWidth(),drawable.getMinHeight());
			break;
		case StaticVariable.role1:
			drawable=new TextureRegionDrawable(new TextureRegion(screen.getTexture(StaticVariable.isBuyPet1 ? "res/pet1Show.png" : "res/pet1ShowNot.png")));
			petImg.setDrawable(drawable);
			petImg.setBounds(petPtX,petPtY,drawable.getMinWidth(),drawable.getMinHeight());
			
			drawable=new TextureRegionDrawable(new TextureRegion(screen.getTexture(StaticVariable.isBuyRole1 ? "res/role1.png" : "res/noBuyRole1.png")));
			roleImg.setDrawable(drawable);
			roleImg.setBounds(rolePtX,rolePtY,drawable.getMinWidth(),drawable.getMinHeight());
			
			break;
		case StaticVariable.role2:
			drawable=new TextureRegionDrawable(new TextureRegion(screen.getTexture(StaticVariable.isBuyPet2 ? "res/pet2Show.png" : "res/pet2ShowNot.png")));
			petImg.setDrawable(drawable);
			petImg.setBounds(petPtX,petPtY,drawable.getMinWidth(),drawable.getMinHeight());
			
			drawable=new TextureRegionDrawable(new TextureRegion(screen.getTexture(StaticVariable.isBuyRole2 ? "res/role2.png" : "res/noBuyRole2.png")));
			roleImg.setDrawable(drawable);
			roleImg.setBounds(rolePtX,rolePtY,drawable.getMinWidth(),drawable.getMinHeight());
			break;
		case StaticVariable.role3:
			drawable=new TextureRegionDrawable(new TextureRegion(screen.getTexture(StaticVariable.isBuyPet3 ? "res/pet3Show.png" : "res/pet3ShowNot.png")));
			petImg.setDrawable(drawable);
			petImg.setBounds(petPtX,petPtY,drawable.getMinWidth(),drawable.getMinHeight());
			
			drawable=new TextureRegionDrawable(new TextureRegion(screen.getTexture(StaticVariable.isBuyRole3 ? "res/role3.png" : "res/noBuyRole3.png")));
			roleImg.setDrawable(drawable);
			roleImg.setBounds(rolePtX,rolePtY,drawable.getMinWidth(),drawable.getMinHeight());
			break;
		case StaticVariable.role4:
			drawable=new TextureRegionDrawable(new TextureRegion(screen.getTexture(StaticVariable.isBuyPet4 ? "res/pet4Show.png" : "res/pet4ShowNot.png")));
			petImg.setDrawable(drawable);
			petImg.setBounds(petPtX,petPtY,drawable.getMinWidth(),drawable.getMinHeight());

			drawable=new TextureRegionDrawable(new TextureRegion(screen.getTexture(StaticVariable.isBuyRole4 ? "res/role4.png" : "res/noBuyRole4.png")));
			roleImg.setDrawable(drawable);
			roleImg.setBounds(rolePtX,rolePtY,drawable.getMinWidth(),drawable.getMinHeight());
			break;
		default:
			break;
		}
		// --jn
		setStartBtnStatusBySelectedRole();
		
		infoImg.setDrawable(new TextureRegionDrawable(new TextureRegion(screen.getTexture("res/roleInfo"+StaticVariable.roleKind+".png"))));
		//图片相应事件  --jn
		roleImg.addListener(new ClickListener(){
			
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				
				Log.e("JN====>", "图片被按下");
				switch (StaticVariable.roleKind) {
				case StaticVariable.role0:
					
					break;
				case StaticVariable.role1:
					if(!StaticVariable.isBuyRole1){
						screen.setPay(PayType.role1);
					}
					break;
				case StaticVariable.role2:
					if(!StaticVariable.isBuyRole2){
						screen.setPay(PayType.role2);
					}
					break;
				case StaticVariable.role3:
					if(!StaticVariable.isBuyRole3){
						screen.setPay(PayType.role3);
					}
					break;
				
				case StaticVariable.role4:
					if(!StaticVariable.isBuyRole4){
						screen.setPay(PayType.role4);
					}
					break;
				}
				
				return true;
			}
			
		});
		petImg.addListener(new ClickListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				switch (StaticVariable.roleKind) {
				case StaticVariable.role0:
					if(!StaticVariable.isBuyPet0){
						screen.setPay(PayType.pet0);
					}
					break;
				case StaticVariable.role1:
					if(!StaticVariable.isBuyPet1){
						screen.setPay(PayType.pet1);
					}
					break;
				case StaticVariable.role2:
					if(!StaticVariable.isBuyPet2){
						screen.setPay(PayType.pet2);
					}
					break;
				case StaticVariable.role3:
					if(!StaticVariable.isBuyPet3){
						screen.setPay(PayType.pet3);
					}
					break;
				case StaticVariable.role4:
					if(!StaticVariable.isBuyPet4){
						screen.setPay(PayType.pet4);
					}
					break;
				}
				return true;
			}
			
		});
	}

	@Override
	public boolean touchDown(Actor actor) {
		SoundUtil.playSound(screen.main.getManager(), "button");
		return true;
	}

	@Override
	public void touchUp(Actor actor) {
		
		if (actor == leftBtn) {
			StaticVariable.roleKind=previousRole();
			changeRole(StaticVariable.roleKind);
		}else if (actor == rightBtn) {
			StaticVariable.roleKind=nextRole();
			changeRole(StaticVariable.roleKind);
		}
	}

	//顺序：freeRole->bunnyRole->catRole->chinaRole->loliRole
	private int nextRole(){
		int nextRole=-1;
		switch (StaticVariable.roleKind) {
		case StaticVariable.role0:
			nextRole=StaticVariable.role1;
			break;
		case StaticVariable.role3:
			nextRole=StaticVariable.role4;
			break;
		case StaticVariable.role2:
			nextRole=StaticVariable.role3;
			break;
		case StaticVariable.role4:
			nextRole=StaticVariable.role0;
			break;
		case StaticVariable.role1:
			nextRole=StaticVariable.role2;
			break;
		}
		return nextRole;
	}
	
	private int previousRole(){
		int previousRole=-1;
		switch (StaticVariable.roleKind) {
		case StaticVariable.role0:
			previousRole=StaticVariable.role4;
			break;
		case StaticVariable.role3:
			previousRole=StaticVariable.role2;
			break;
		case StaticVariable.role2:
			previousRole=StaticVariable.role1;
			break;
		case StaticVariable.role4:
			previousRole=StaticVariable.role3;
			break;
		case StaticVariable.role1:
			previousRole=StaticVariable.role0;
			break;
		}
		return previousRole;
	}

}





