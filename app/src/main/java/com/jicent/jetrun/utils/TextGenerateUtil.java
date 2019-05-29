package com.jicent.jetrun.utils;

import java.util.TreeSet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeBitmapFontData;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class TextGenerateUtil {
	private static FreeTypeFontGenerator generator;
	private static FreeTypeBitmapFontData data;
	private static TreeSet<String> strSet;
	
	static{
		generator=new FreeTypeFontGenerator(Gdx.files.absolute("/system/fonts/DroidSansFallback.ttf"));
		strSet=new TreeSet<String>();
	}
	
	public static BitmapFont getFont(String string){
		if (!string.equals("")) {
			FreeTypeFontParameter parameter = new FreeTypeFontParameter();
			parameter.size = 22;
			parameter.characters = getNoReapteStr(string);
			parameter.flip = false;
			data=generator.generateData(parameter);
		}else {
			data=generator.generateData(22);
		}
		return  new BitmapFont(data, data.getTextureRegions(), false);
	}
	
	private static String getNoReapteStr(String str){
		for (int i = 0; i < str.length(); i++) {
			strSet.add(""+str.charAt(i));
		}
		String tmpStr="";
		for (String string : strSet) {
			tmpStr+=string;
		}
		strSet.clear();
		return tmpStr;
	}

}
