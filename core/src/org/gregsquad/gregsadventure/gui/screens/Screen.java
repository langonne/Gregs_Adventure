package org.gregsquad.gregsadventure.gui.screens;

import org.gregsquad.gregsadventure.GregsAdventure;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;

public abstract class Screen implements com.badlogic.gdx.Screen {
    public static final int DEFAULT_WIDTH = 1920;
    public static final int DEFAULT_HEIGHT = 1080;

    protected final GregsAdventure gui;
    protected final AssetManager assets;
    protected Skin skin;
    protected Stage stage;

    public Screen(GregsAdventure gui, AssetManager assets) {
        this.gui = gui;
        this.assets = assets;

        stage = new Stage(new FitViewport(DEFAULT_WIDTH, DEFAULT_HEIGHT));
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
        // TODO: Implement pause logic
    }

    @Override
    public void resume() {
        // TODO: Implement resume logic
    }

    @Override
    public void hide() {
        // TODO: Implement hide logic
    }
}
