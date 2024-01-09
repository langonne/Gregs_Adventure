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

import java.util.ArrayList;

import org.gregsquad.gregsadventure.GregsAdventure;

import org.gregsquad.gregserver.Client;
import org.gregsquad.gregsadventure.game.Player;
import org.gregsquad.gregsadventure.card.ConfigLoader;

public class GameScreen extends Screen {

    private static final float TOOLTIP_DELAY = 0.1f;

    public static final int BUTTON_SIZE = 100;
    public static final int PADDING = 10;

    private Client client;

    private int id;
    private String name;

    private Deck deck;

    private TooltipManager tooltipManager;

    private SpriteBatch batch;
    private BitmapFont font;

    private TextButton inventory;
    private Table inventoryTable;

    private ArrayList<Player> players;
    private Player player;

    public GameScreen(GregsAdventure gui, AssetManager assets, Client client, int id) {
        super(gui, assets);

        this.client = client;
        this.id = id;

        players = client.getPlayerList();
        player = players.get(id);
        this.name = player.getName();

        // Settings of the tooltips
        tooltipManager = TooltipManager.getInstance();
        tooltipManager.initialTime = TOOLTIP_DELAY;
        tooltipManager.resetTime = TOOLTIP_DELAY;
        tooltipManager.subsequentTime = TOOLTIP_DELAY;
        tooltipManager.hideAll();
    }

    
    @Override
    public void show() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.BLACK);
        font.getData().setScale(2);

        skin = assets.get("skin/uiskin.json", Skin.class);


        Table table = new Table(); // Create a table that fills the screen. Everything else will go inside this table.

        table.setFillParent(true);
        stage.addActor(table); // Add the table to the stage

        inventory = new TextButton("Inventory", skin);
        inventory.setSize(BUTTON_SIZE, BUTTON_SIZE);
        inventory.setPosition(DEFAULT_WIDTH - BUTTON_SIZE - PADDING, PADDING);
        inventory.addListener(new TextTooltip("Inventory", skin));



        stage.addActor(inventory);

        TextButton startButton = new TextButton("Start", skin);
        

        /*Image img = new Image(new Texture(Gdx.files.internal("gechter.png")));
        img.setPosition(400, 100);
        img.addListener(new TextTooltip("c Gechter mais en mieux", skin));
        img.setSize(500, 500);
        stage.addActor(img);*/
        

        TextTooltip txt = new TextTooltip("G une enorme bite", skin);
        startButton.addListener(txt);
        
        table.add(startButton).fillX().uniformX();


        // Load all the card textures
        System.out.println("ENORME CHIBREEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEe");
        System.out.println(ConfigLoader.getInt("numberOfCards"));
        int[] id = ConfigLoader.getIdArray();
        for (int i = 0; i < ConfigLoader.getInt("numberOfCards"); i++) {
            System.out.println(id[i] + "chargée");
            assets.load("cards/" + id[i] + ".png", Texture.class);
            assets.finishLoading();
        }

        new Thread(() -> {
            while (client.getInitGame()) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                players = client.getPlayers();
                player = players.get(id);

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        }).start();

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(135 / 255f, 206 / 255f, 250 / 255f, 1);
        stage.act();
        batch.begin();
        font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 25, 25);
        font.draw(batch, name + " - " + "Level : " + player.getLevel() + " - " + "Atk : " + player.getDamage(), 60, 1040);
        font.setColor(Color.BLACK);
        font.getData().setScale(2);

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

    private 
}
