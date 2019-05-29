package com.jicent.jetrun.entry;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader.BitmapFontParameter;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.TmxMapLoader.Parameters;
import com.jicent.jetrun.extensions.ProcessEx.ProcessType;
import com.jicent.jetrun.extensions.TextureAtlasEx;
import com.jicent.jetrun.extensions.TextureAtlasLoaderEx;
import com.jicent.jetrun.extensions.TextureEx;
import com.jicent.jetrun.extensions.TextureLoaderEx;
import com.jicent.jetrun.extensions.TextureLoaderEx.TextureParameterEx;
import com.jicent.jetrun.screen.LoadingScreen;
import com.jicent.jetrun.utils.SoundUtil;

public class GameMain extends Game {
	private MainActivity activity;
	private SharedPreferences sp;
	private AssetManager manager;
	private BitmapFontParameter fontPatameter;
	private TextureParameterEx textureParameter;
	private Parameters tmxParameter;
	
	public GameMain(MainActivity activity) {
		this.activity=activity;
		sp=activity.getSharedPreferences("data", Context.MODE_PRIVATE);
	}

	@Override
	public void create() {
		//键盘功能拦截
		Gdx.input.setCatchBackKey(true);
		Gdx.input.setCatchMenuKey(true);
		
		manager=new AssetManager();
		loadRes();
		
//		setScreen(new GameScreen(this,ProcessType.gameScreen));
//		setScreen(new ShopScreen(this, ProcessType.shopScreen));
		setScreen(new LoadingScreen(this, ProcessType.loadingScreen));
	}
	
	private void loadRes() {
		//预加载图片资源
		manager.setLoader(TextureEx.class, new TextureLoaderEx(new InternalFileHandleResolver()));
		textureParameter=new TextureParameterEx();
		
		//预加载atlas资源
		manager.setLoader(TextureAtlasEx.class, new TextureAtlasLoaderEx(new InternalFileHandleResolver()));
		
		//载入地图
		manager.setLoader(TiledMap.class,new TmxMapLoader());
		tmxParameter=new Parameters();
		tmxParameter.textureMagFilter=TextureFilter.Linear;
		tmxParameter.textureMinFilter=TextureFilter.Linear;
		for (int i = 0; i < 30; i++) {
			manager.load("map/map"+i+".tmx", TiledMap.class, tmxParameter);
		}
		
		fontPatameter=new BitmapFontParameter();
		fontPatameter.minFilter=TextureFilter.Linear;
		fontPatameter.magFilter=TextureFilter.Linear;
		
		manager.load("font/allfont.fnt", BitmapFont.class,fontPatameter);//字体
		
		manager.load("sound/button.mp3",Sound.class);
		manager.load("sound/motor.mp3",Sound.class);
		manager.load("sound/missileW.mp3",Sound.class);
		manager.load("sound/missileO.mp3",Sound.class);
		manager.load("sound/laserO.mp3",Sound.class);
		manager.load("sound/laserW.mp3",Sound.class);
		manager.load("sound/coin.mp3",Sound.class);
		manager.load("sound/speedUp.mp3",Sound.class);
		manager.load("sound/dead.mp3",Sound.class);
		manager.load("sound/buff.mp3",Sound.class);
		manager.load("sound/motorCrush.mp3",Sound.class);
		manager.load("sound/dead.mp3",Sound.class);
		manager.load("sound/bullet.mp3",Sound.class);
		manager.load("sound/up.mp3",Sound.class);
		
		manager.finishLoading();
	}
	public void showExitDialog(){
		activity.handler.post(new Runnable() {
			@Override
			public void run() {
				AlertDialog.Builder builder = new AlertDialog.Builder(activity);
				builder.setTitle("提示")
						.setMessage("退出游戏？")
						.setPositiveButton("是",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										SoundUtil.stopMusic();
										getManager().dispose();
										Gdx.app.exit();
									}
								})
						.setNegativeButton("否",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								}).create().show();
			}
		});
	}
	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(0, 0, 0, 1);
		super.render();
	}

	public MainActivity getActivity() {
		return activity;
	}

	public void setActivity(MainActivity activity) {
		this.activity = activity;
	}

	public SharedPreferences getSp() {
		return sp;
	}

	public void setSp(SharedPreferences sp) {
		this.sp = sp;
	}
	
	public AssetManager getManager() {
		return manager;
	}

	public void setManager(AssetManager manager) {
		this.manager = manager;
	}
}
