package com.jicent.jetrun.extensions;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.utils.Array;

public class TextureLoaderEx extends AsynchronousAssetLoader<TextureEx, TextureLoaderEx.TextureParameterEx>{
	static public class TextureLoaderInfo {
		String filename;
		TextureData data;
		TextureEx texture;
	};

	TextureLoaderInfo info = new TextureLoaderInfo();

	public TextureLoaderEx(FileHandleResolver resolver) {
		super(resolver);
	}

    static public class TextureParameterEx extends 
            AssetLoaderParameters<TextureEx> { 
		public TextureFilter minFilter = TextureFilter.Linear;
		public TextureFilter magFilter = TextureFilter.Linear;
		public TextureWrap wrapU = TextureWrap.ClampToEdge;
		public TextureWrap wrapV = TextureWrap.ClampToEdge;
    }

	@Override
	public Array<AssetDescriptor> getDependencies(String fileName,
			FileHandle file, TextureParameterEx parameter) {
		return null;
	}
	
	@Override
	public void loadAsync(AssetManager manager, String fileName,
			FileHandle file, TextureParameterEx parameter) {
		info.filename = fileName;
		Pixmap pixmap = null;
		Format format = null;
		boolean genMipMaps = false;
		info.texture = null;
		
		//不加密时要添加的代码
		pixmap = new Pixmap(file);
		info.data = new FileTextureDataEx(file, pixmap, format, genMipMaps);
		
		//加密时需要添加的代码
//		pixmap = DecipherResUtil.getPixmap(file);
//		info.data = new FileTextureDataEx(file, pixmap, format, genMipMaps);
		
	}

	@Override
	public TextureEx loadSync(AssetManager manager, String fileName,
			FileHandle file, TextureParameterEx parameter) {
		if (info == null) return null;
		TextureEx texture = info.texture;
		if (texture != null) {
			texture.load(info.data);
		} else {
			texture = new TextureEx(info.data);
		}
		if (parameter != null) {
			texture.setFilter(parameter.minFilter, parameter.magFilter);
			texture.setWrap(parameter.wrapU, parameter.wrapV);
		}
		return texture;
	}
} 

