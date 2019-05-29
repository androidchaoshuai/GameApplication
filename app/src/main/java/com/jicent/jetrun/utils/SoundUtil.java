package com.jicent.jetrun.utils;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.jicent.jetrun.data.StaticVariable;
/**
 * 背景音乐和游戏音效的播放工具
 * @author yujia
 *
 */
public class SoundUtil {
	private static Music music;
	private static Sound mountSound;
	private static long mLastTime;
	//播放背景音乐
	public static void playMusic(String name){ 
		if(StaticVariable.isMusicOn){
			if (music==null||!music.isPlaying()) {
				music=Gdx.audio.newMusic(Gdx.files.internal("sound/"+name+".mp3"));
				music.play();
				music.setLooping(true);
			}
		}
	}
	public static void stopMusic(){
		if (music!=null) {
			music.stop();
			music.dispose();
			music=null;
		}
	}
	
	//播放音效
	public static void playSound(AssetManager manager, String name){
		if(StaticVariable.isSoundOn){
			//限制声音快速播放 
			if (name.equals("coin")) {
				long current = System.currentTimeMillis();
				long diff =  current - mLastTime;
				if(diff > 100){
					Sound sound=manager.get("sound/"+name+".mp3", Sound.class);
					sound.play();
					mLastTime = current;
				}
			}else if (name.equals("bullet")) {
				Sound sound=manager.get("sound/"+name+".mp3", Sound.class);
				sound.play(0.2f);
			}else {
				Sound sound=manager.get("sound/"+name+".mp3", Sound.class);
				sound.play();
			}
		}
	}
	
	public static void playMountSound(AssetManager manager, String name){
		if(StaticVariable.isSoundOn){
			mountSound=manager.get("sound/"+name+".mp3", Sound.class);
			mountSound.loop();
			mountSound.play();
		}
	}
	
	public static void stopMountSound(){
		if (mountSound!=null) {
			mountSound.stop();
			mountSound=null;
		}
	}
	
	public static void pauseLoopSound(){
		if (mountSound!=null) {
			mountSound.pause();
		}
	}
	
	public static void resumeLoopSound(){
		if (mountSound!=null) {
			mountSound.resume();
		}
	}
}
