package com.jicent.jetrun.extensions;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.TextureAtlasData;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.TextureAtlasData.Page;
import com.badlogic.gdx.utils.Array;

/** {@link AssetLoader} to load {@link TextureAtlas} instances. Passing a {@link TextureAtlasParameter} to
 * {@link AssetManager#load(String, Class, AssetLoaderParameters)} allows to specify whether the atlas regions should be flipped
 * on the y-axis or not.
 * @author mzechner */
public class TextureAtlasLoaderEx extends SynchronousAssetLoader<TextureAtlasEx, TextureAtlasLoaderEx.TextureAtlasParameter> {
	public TextureAtlasLoaderEx (FileHandleResolver resolver) {
		super(resolver);
	}

	TextureAtlasData data;

	@Override
	public TextureAtlasEx load (AssetManager assetManager, String fileName, FileHandle file, TextureAtlasParameter parameter) {
		for (Page page : data.getPages()) {
			//未加密
			Texture texture = assetManager.get(page.textureFile.path().replaceAll("\\\\", "/"), Texture.class);
			
			//加密后
//			Texture texture = assetManager.get(page.textureFile.path().replaceAll("\\\\", "/"), TextureEx.class);
			page.texture = texture;
		}

		return new TextureAtlasEx(data);
	}

	@Override
	public Array<AssetDescriptor> getDependencies (String fileName, FileHandle atlasFile, TextureAtlasParameter parameter) {
		FileHandle imgDir = atlasFile.parent();
		if (parameter != null)
			data = new TextureAtlasData(atlasFile, imgDir, parameter.flip);
		else {
			data = new TextureAtlasData(atlasFile, imgDir, false);
		}

		Array<AssetDescriptor> dependencies = new Array();
		for (Page page : data.getPages()) {
			//未加密
			TextureParameter params = new TextureParameter();
			params.format = page.format;
			params.genMipMaps = page.useMipMaps;
			params.minFilter = page.minFilter;
			params.magFilter = page.magFilter;
			dependencies.add(new AssetDescriptor(page.textureFile, Texture.class, params));
			
			//加密后
//			TextureParameterEx params = new TextureParameterEx();
//			dependencies.add(new AssetDescriptor(page.textureFile, TextureEx.class, params));
		}
		return dependencies;
	}

	static public class TextureAtlasParameter extends AssetLoaderParameters<TextureAtlasEx> {
		/** whether to flip the texture atlas vertically **/
		public boolean flip = false;

		public TextureAtlasParameter () {
		}

		public TextureAtlasParameter (boolean flip) {
			this.flip = flip;
		}
	}
}
