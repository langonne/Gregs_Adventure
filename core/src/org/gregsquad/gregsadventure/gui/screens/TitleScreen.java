package org.gregsquad.gregsadventure.gui.screens;

import org.gregsquad.gregsadventure.GregsAdventure;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class TitleScreen extends Screen {

    private Table table;

    private TextButton startButton;
    private TextButton optionsButton;
    private TextButton exitButton;


    public TitleScreen(GregsAdventure gui, AssetManager assets) {
        super(gui, assets);
    }

    @Override
    public void show() {
        this.assets.load("skin/uiskin.json", Skin.class);
		this.assets.finishLoading(); // Blocks until all resources are loaded into memory

        table = new Table(); // Create a table that fills the screen. Everything else will go inside this table.
        table.setFillParent(true);
        stage.addActor(table); // Add the table to the stage.


        skin = assets.get("skin/uiskin.json", Skin.class);

        startButton = new TextButton("Jouer", skin);
        optionsButton = new TextButton("Options", skin);
        exitButton = new TextButton("Exit", skin);

        
        table.add(startButton).fillX().uniformX();
        table.row().pad(10, 0, 10, 0);
        table.add(optionsButton).fillX().uniformX();
        table.row();
        table.add(exitButton).fillX().uniformX();

        startButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                gui.setScreen(new StartScreen(gui, assets));
            }
        });

        optionsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                table.setSkin(skin);
                table.add("Pas de chance, on a pas eu le temps de faire le menu des options").fillX().uniformX();
            }
        });

        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                Gdx.app.exit();
            }
        }); 
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(135 / 255f, 206 / 255f, 250 / 255f, 1);
        stage.act();
        stage.draw();
    }

    @Override
    public void dispose() {
        assets.unload("skin/uiskin.json");
        stage.dispose();
    }
}
