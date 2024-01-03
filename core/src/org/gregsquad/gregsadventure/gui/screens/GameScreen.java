package org.gregsquad.gregsadventure.gui.screens;

import org.gregsquad.gregsadventure.GregsAdventure;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextTooltip;
import com.badlogic.gdx.scenes.scene2d.ui.TooltipManager;
import com.badlogic.gdx.utils.ScreenUtils;

import org.gregsquad.gregsadventure.GregsAdventure;

import org.gregsquad.gregserver.Client;

public class GameScreen extends Screen {

    public static final int BUTTON_SIZE = 100;
    public static final int PADDING = 10;

    private Client client;

    private TooltipManager tooltipManager;

    private SpriteBatch batch;
    private BitmapFont font;

    private TextButton inventory;

    public GameScreen(GregsAdventure gui, AssetManager assets, Client client) {
        super(gui, assets);

        this.client = client;

        // Settings of the tooltips
        tooltipManager = TooltipManager.getInstance();
        tooltipManager.initialTime = 0.1f;
        tooltipManager.resetTime = 0.1f;
        tooltipManager.subsequentTime = 0.1f;
        tooltipManager.hideAll();
    }

    
    @Override
    public void show() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.WHITE);

        skin = assets.get("gdx-skins/gdx-holo/skin/uiskin.json", Skin.class);


        Table table = new Table(); // Create a table that fills the screen. Everything else will go inside this table.
        table.setFillParent(true);
        stage.addActor(table); // Add the table to the stage

        inventory = new TextButton("Inventory", skin);
        inventory.setSize(BUTTON_SIZE, BUTTON_SIZE);
        inventory.setPosition(DEFAULT_WIDTH - BUTTON_SIZE - PADDING, PADDING);
        inventory.addListener(new TextTooltip("Inventory", skin));

        stage.addActor(inventory);

        TextButton startButton = new TextButton("Start", skin);
        

        Image img = new Image(new Texture(Gdx.files.internal("gechter.png")));
        img.setPosition(400, 100);
        img.addListener(new TextTooltip("c Gechter mais en mieux", skin));
        img.setSize(500, 500);
        

        TextTooltip txt = new TextTooltip("G une enorme bite", skin);
        startButton.addListener(txt);
        
        table.add(startButton).fillX().uniformX();
        stage.addActor(img);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(135 / 255f, 206 / 255f, 250 / 255f, 1);
        stage.act();
        batch.begin();
        font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 25, 25);
        font.draw(batch, "Name - " + "Level - " + "Atk", 60, 1040);

        if (Gdx.input.isTouched()) {
            int posY = (int) stage.getHeight() - Gdx.input.getY();
            font.draw(batch, "X: " + Gdx.input.getX() + " Y: " + posY, 25, 50);
        }

        batch.end();
        stage.draw();
    }

    @Override
    public void dispose() {
        // TODO implement dispose logic
    }
}
