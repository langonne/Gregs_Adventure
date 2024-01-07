package org.gregsquad.gregsadventure;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import org.gregsquad.gregsadventure.gui.screens.TitleScreen;

import com.badlogic.gdx.Game;

public class GregsAdventure extends Game {
	private AssetManager assets;
	private TitleScreen titleScreen;

	@Override
	public void create () {
		Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());

		assets = new AssetManager();

		//batch = new SpriteBatch();
		//img = new Texture("badlogic.jpg");
		titleScreen = new TitleScreen(this, assets);
		this.setScreen(titleScreen);
	}

	@Override
	public void render () {
		super.render();
		//ScreenUtils.clear(1, 0, 0, 1);
		//batch.begin();
		//batch.draw(img, 0, 0);
		//batch.end();
	}
	
	@Override
	public void dispose () {
		titleScreen.dispose();
		assets.dispose(); // A faire en dernier
	}
		
	private void readPreferences() {
		Preferences prefs = Gdx.app.getPreferences("gregs-config");
		prefs.flush();
	}
}
