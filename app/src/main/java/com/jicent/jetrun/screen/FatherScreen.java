package com.jicent.jetrun.screen;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import android.util.Log;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.TextureAtlasData;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.TextureAtlasData.Page;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.jicent.jetrun.data.StaticVariable;
import com.jicent.jetrun.entry.GameMain;
import com.jicent.jetrun.extensions.ProcessEx;
import com.jicent.jetrun.extensions.ProcessEx.ProcessType;
import com.jicent.jetrun.extensions.TextureAtlasEx;
import com.jicent.jetrun.extensions.TextureEx;
import com.jicent.jetrun.model.WindowDialog;
import com.jicent.jetrun.utils.DialogUtil;
import com.jicent.jetrun.utils.InfoToast;
import com.jicent.jetrun.utils.PayUtil;
import com.jicent.jetrun.utils.PayUtil.IPayCallback;
import com.jicent.jetrun.utils.PayUtil.PayType;
import com.jicent.jetrun.utils.SPUtil;

public class FatherScreen extends ScreenAdapter {
	public Stage mainStage,dialogStage,toastStage;//每个子类screen中需调用其draw()和act()方法
	public InputMultiplexer input;
	public GameMain main;
	public WindowDialog dialog;
	
	protected boolean isPayOk;//标志是否支付完成
	protected PayType payType;//支付类型
	protected ProcessEx process;
	protected ProcessType type;
	protected boolean isChangeScreen;
	protected FatherScreen nextScreen;
	 
	//存放临时申明的资源
	private Map<String, TextureEx> textureMap;
	private Map<String, BitmapFont> fontMap;
	private Map<String, ParticleEffectPool> particleMap;
	private Map<String, TextureAtlasEx> atlasMap;
	
	public FatherScreen(GameMain main,ProcessType type) {
		this.main=main;
		this.type=type;
		
		textureMap=new HashMap<String, TextureEx>();
		fontMap=new HashMap<String, BitmapFont>();
		particleMap=new HashMap<String, ParticleEffectPool>();
		atlasMap=new HashMap<String, TextureAtlasEx>();
	}

	@Override
	public void show() {
		dialogStage=new Stage(new StretchViewport(960,540));
		mainStage=new Stage(new StretchViewport(960,540));
		toastStage=new Stage(new StretchViewport(960,540));
		process=new ProcessEx(this,type);
		
		input=new InputMultiplexer();
		input.addProcessor(process);
		input.addProcessor(mainStage);
		Gdx.input.setInputProcessor(input);
	}
	
	public TextureEx getTexture(String filePath) {
		if (main.getManager().isLoaded(filePath, TextureEx.class)) {
			return main.getManager().get(filePath, TextureEx.class);
		}else if (textureMap.containsKey(filePath)) {
			return textureMap.get(filePath);
		} else {
			TextureEx texture = new TextureEx(filePath);
			
//			TextureEx texture = DecipherResUtil.getTexture(Gdx.files
//					.internal(filePath));
			textureMap.put(filePath, texture);
			return texture;
		}
	}

	public BitmapFont getBitmapFont(String filePath) {
		if (main.getManager().isLoaded(filePath, BitmapFont.class)) {
			return main.getManager().get(filePath, BitmapFont.class);
		}else if (fontMap.containsKey(filePath)) {
			return fontMap.get(filePath);
		} else {
			if (filePath.equals("default")) {
				BitmapFont font = new BitmapFont();
				font.getRegion().getTexture()
				.setFilter(TextureFilter.Linear, TextureFilter.Linear);
				fontMap.put(filePath, font);
				return font;
			} else {
				BitmapFont font = new BitmapFont(Gdx.files.internal(filePath),
						false);
				font.getRegion().getTexture()
				.setFilter(TextureFilter.Linear, TextureFilter.Linear);
				fontMap.put(filePath, font);
				return font;
			}
		}
	}
	
	public TiledMap getTiledMap(String filePath) {
		return main.getManager().get(filePath, TiledMap.class);
	}

	public TextureAtlasEx getAtlas(String filePath){
		if (main.getManager().isLoaded(filePath, TextureAtlasEx.class)) {
			return main.getManager().get(filePath, TextureAtlasEx.class);
		}else if (atlasMap.containsKey(filePath)) {
			return atlasMap.get(filePath);
		} else {
			FileHandle packFile=Gdx.files.internal(filePath);
			TextureAtlasData data=new TextureAtlasData(packFile, packFile.parent(), false);
			for (Page page : data.getPages()) {
				page.texture=new Texture(Gdx.files.internal(page.textureFile.path().replaceAll("\\\\", "/")));
				
//				page.texture=DecipherResUtil.getTexture(Gdx.files.internal(page.textureFile.path().replaceAll("\\\\", "/")));
			}
			return new TextureAtlasEx(data);
		}
	}
	
	public ParticleEffect getParticle(String effectFile, String imagesDir,int initNum,int maxNum){
		ParticleEffect effect=null;
		if (particleMap.containsKey(effectFile)) {
			effect=particleMap.get(effectFile).obtain();
		}else {
			effect=new ParticleEffect();
			effect.load(Gdx.files.internal(effectFile),Gdx.files.internal(imagesDir));
			ParticleEffectPool pool=new ParticleEffectPool(effect, initNum, maxNum);
			particleMap.put(effectFile, pool);
			effect=particleMap.get(effectFile).obtain();
		}
		return effect;
	}
	
	/**
	 * 清理临时建立的texture和bitmapFont
	 */
	public void clearTmpRes() {
		Set<String> strSet = textureMap.keySet();
		for (String string : strSet) {
			textureMap.get(string).dispose();
		}
		textureMap.clear();
		textureMap = null;
		
		strSet = fontMap.keySet();
		for (String string : strSet) {
			fontMap.get(string).dispose();
		}
		fontMap.clear();
		fontMap = null;

		strSet=particleMap.keySet();
		ParticleEffectPool tempPool=null;
		for (String string : strSet) {
			tempPool=particleMap.get(string);
			tempPool.obtain().dispose();
			tempPool.clear();
		}
		particleMap.clear();
		particleMap=null;
		
		strSet=atlasMap.keySet();
		for (String string : strSet) {
			atlasMap.get(string).dispose();
		}
		atlasMap.clear();
		atlasMap=null;
		
		strSet.clear();
		strSet=null;
	}

	/**
	 * 检测触摸点是否在监听的控件之内
	 * @param actor 得到监听响应的actor
	 * @param targetActor 预期得到监听响应actor
	 * @param x 触摸点x
	 * @param y 触摸点y
	 * @return true|false
	 */
	public boolean isOnBound(Actor actor, Actor targetActor, float x, float y) {
		if (actor == targetActor && x > 0 && y > 0
				&& x < targetActor.getWidth() && y < targetActor.getHeight()) {
			return true;
		}
		return false;
	}

	/**
	 * 对屏幕进行切换的方法，此方法防止stage的空指针异常
	 * @param isChangeScreen 是否开始切换屏幕
	 * @param nextScreen 要切换到的屏幕对象
	 */
	public void changeScreen(boolean isChangeScreen,FatherScreen nextScreen) {
		this.isChangeScreen = isChangeScreen;
		this.nextScreen=nextScreen;
	}

	
	private static boolean isPaying = false;
	public void setPay(final PayType type){
		Log.e("shuai  ", "buy isPaying is " + isPaying);
		if(isPaying)
		{
			Log.e("shuai  ", "go to buy?");
			// skip
		}
		else
		{
			isPaying = true;
			Log.e("shuai  ", "and this is go to buy");
			PayUtil.pay(main.getActivity(), type, new IPayCallback() {
				@Override
				public void onPayFinish(boolean isPayOk) {
					FatherScreen.this.isPayOk=isPayOk;
					payType=type;
					isPaying = false;
				}
			});
		}
	}
	
	//覆写的时候需要类似的结构，可以选择性的覆写
	//在需要进行支付的screen中覆写此方法，并在render中调用
	public void payDeal(){
		if (payType != null) {
			if (isPayOk) {
				switch (payType) {
				case getAllCard:
					((GameScreen)this).widget.lottery.getAllCardProp();
					
					InfoToast.show(this, "购买成功");
					break;
				case coinGift:
					StaticVariable.coinNum+=80000;
					SPUtil.commit(main.getSp(), "coinNum", StaticVariable.coinNum);
					if(this instanceof GameScreen){
						((GameScreen)this).widget.upDataCoin();
					}
					if(this instanceof ShopScreen)
						((ShopScreen)this).roleShow.updateCoin();
//					DialogUtil.dismiss();
					InfoToast.show(this, "购买成功,获得80000金币");
					break;
				case propGift:
					StaticVariable.speedUpCount+=3;
					SPUtil.commit(main.getSp(), "speedUpCount", StaticVariable.speedUpCount);
					StaticVariable.gunCount+=10;
					SPUtil.commit(main.getSp(), "gunCount", StaticVariable.gunCount);
					StaticVariable.mountCount+=5;
					SPUtil.commit(main.getSp(), "mountCount", StaticVariable.mountCount);
					StaticVariable.shieldCount+=8;
					SPUtil.commit(main.getSp(), "shieldCount", StaticVariable.shieldCount);
					StaticVariable.attractCount+=10;
					SPUtil.commit(main.getSp(), "attractCount", StaticVariable.attractCount);
//					DialogUtil.dismiss();
					InfoToast.show(this, "购买成功");
					break;
				case role1:
					StaticVariable.isBuyRole1=true;
					SPUtil.commit(main.getSp(), "isBuyRole1", StaticVariable.isBuyRole1);
					((ShopScreen)this).roleShow.roleImg.setDrawable(new TextureRegionDrawable(new TextureRegion(getTexture("res/role1.png"))));
					((ShopScreen)this).shopButton.getStartBtn().setVisible(true);
					
					InfoToast.show(this, "购买成功");
					break;
				case role2:
					StaticVariable.isBuyRole2=true;
					SPUtil.commit(main.getSp(), "isBuyRole2", StaticVariable.isBuyRole2);
					((ShopScreen)this).roleShow.roleImg.setDrawable(new TextureRegionDrawable(new TextureRegion(getTexture("res/role2.png"))));
					((ShopScreen)this).shopButton.getStartBtn().setVisible(true);
					
					InfoToast.show(this, "购买成功");
					break;
				case role3:
					StaticVariable.isBuyRole3=true;
					SPUtil.commit(main.getSp(), "isBuyRole3", StaticVariable.isBuyRole3);
					((ShopScreen)this).roleShow.roleImg.setDrawable(new TextureRegionDrawable(new TextureRegion(getTexture("res/role3.png"))));
					((ShopScreen)this).shopButton.getStartBtn().setVisible(true);
					
					InfoToast.show(this, "购买成功");
					break;
				case role4:
					StaticVariable.isBuyRole4=true;
					SPUtil.commit(main.getSp(), "isBuyRole4", StaticVariable.isBuyRole4);
					((ShopScreen)this).roleShow.roleImg.setDrawable(new TextureRegionDrawable(new TextureRegion(getTexture("res/role4.png"))));
					((ShopScreen)this).shopButton.getStartBtn().setVisible(true);
					
					InfoToast.show(this, "购买成功");
					break;
				case pet0:
					StaticVariable.isBuyPet0=true;
					SPUtil.commit(main.getSp(), "isBuyPet0", StaticVariable.isBuyPet0);
					((ShopScreen)this).roleShow.petImg.setDrawable(new TextureRegionDrawable(new TextureRegion(getTexture("res/pet0Show.png"))));
					
					InfoToast.show(this, "购买成功");
					break;
				case pet1:
					StaticVariable.isBuyPet1=true;
					SPUtil.commit(main.getSp(), "isBuyPet1", StaticVariable.isBuyPet1);
					((ShopScreen)this).roleShow.petImg.setDrawable(new TextureRegionDrawable(new TextureRegion(getTexture("res/pet1Show.png"))));
					
					InfoToast.show(this, "购买成功");
					break;
				case pet2:
					StaticVariable.isBuyPet2=true;
					SPUtil.commit(main.getSp(), "isBuyPet2", StaticVariable.isBuyPet2);
					((ShopScreen)this).roleShow.petImg.setDrawable(new TextureRegionDrawable(new TextureRegion(getTexture("res/pet2Show.png"))));
					
					InfoToast.show(this, "购买成功");
					break;
				case pet3:
					StaticVariable.isBuyPet3=true;
					SPUtil.commit(main.getSp(), "isBuyPet3", StaticVariable.isBuyPet3);
					((ShopScreen)this).roleShow.petImg.setDrawable(new TextureRegionDrawable(new TextureRegion(getTexture("res/pet3Show.png"))));
					
					InfoToast.show(this, "购买成功");
					break;
		  		case pet4:
					StaticVariable.isBuyPet4=true;
					SPUtil.commit(main.getSp(), "isBuyPet4", StaticVariable.isBuyPet4);
					((ShopScreen)this).roleShow.petImg.setDrawable(new TextureRegionDrawable(new TextureRegion(getTexture("res/pet4Show.png"))));
					
					InfoToast.show(this, "购买成功");
					break;
				default:
					break;
				}
				
			} else {
				switch (payType) {
				case coinGift:
					
					break;
				case propGift:
					
					break;
				default:
					break;
				}
//				DialogUtil.dismiss();
				InfoToast.show(this, "购买失败");
			}
			payType = null;
		}		
	}
	
	//释放资源
	@Override
	public void hide() {
		clearTmpRes();
		dialogStage.dispose();
		dialogStage=null;
		mainStage.dispose();
		mainStage=null;
		toastStage.dispose();
		toastStage=null;
		input.clear();
		input=null;
	}
}
