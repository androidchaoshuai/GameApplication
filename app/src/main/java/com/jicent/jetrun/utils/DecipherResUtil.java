package com.jicent.jetrun.utils;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.jicent.jetrun.extensions.TextureEx;

public class DecipherResUtil {
	// 密钥
	private static String Key = "jicent";
	// pixmap 矢量图
	private static Pixmap pixmap = null;
	// 写入文件、资源字节数组
	private static byte[] allBytes = null;

	public static Pixmap getPixmap(FileHandle handle) {
		pixmap = null;
		allBytes = null;
		try {
			allBytes = handle.readBytes();
			int byteCount = 0;
			for (int i = 0; i < allBytes.length; i++) {
				byteCount++;
				if (byteCount <= 20) {
					// 加密20个字节,停止解密
					allBytes[i] ^= Key.hashCode();
				} else {
					allBytes[i] = allBytes[i];
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} 
		pixmap = new Pixmap(allBytes, 0, allBytes.length);
		return pixmap;
	}
	
	public static TextureEx getTexture(FileHandle handle){
		return new TextureEx(getPixmap(handle));
	}

}
