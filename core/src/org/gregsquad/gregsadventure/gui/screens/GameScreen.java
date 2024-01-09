package org.gregsquad.gregsadventure.gui.screens;

import org.gregsquad.gregsadventure.GregsAdventure;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextTooltip;
import com.badlogic.gdx.scenes.scene2d.ui.TooltipManager;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable; 
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;

import org.gregsquad.gregsadventure.GregsAdventure;

import org.gregsquad.gregserver.Client;
import org.gregsquad.gregsadventure.game.Player;
import org.gregsquad.gregsadventure.card.Card;
import org.gregsquad.gregsadventure.card.Deck;
import org.gregsquad.gregsadventure.card.ConfigLoader;

public class GameScreen extends Screen {

    private static final float TOOLTIP_DELAY = 0.1f;

    public static final int BUTTON_SIZE = 100;
    public static final int PADDING = 10;

    public static final int WAITING_TIME = 500;

    private Client client;

    private int id;
    private String name;

    private TooltipManager tooltipManager;

    private SpriteBatch batch;
    private BitmapFont font;

    private TextButton inventory;
    private TextButton endTurn;
    private TextButton donjonStack;
    private TextButton treasureStack;

    private Table inventoryTable;
    private Table cardTable;


    private ArrayList<Player> players;
    private Player player;

    private int currentPlayerId;

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

        cardTable = new Table();
        cardTable.setPosition(960, 170);

        stage.addActor(cardTable);

        inventory = new TextButton("Inventaire", skin);
        inventory.setSize(BUTTON_SIZE, BUTTON_SIZE);
        inventory.setPosition(DEFAULT_WIDTH - BUTTON_SIZE - PADDING, PADDING);
        inventory.addListener(new TextTooltip("Cliquez ici pour afficher votre inventaire.", skin));

        endTurn = new TextButton("Fin du tour", skin);
        endTurn.setSize(BUTTON_SIZE, BUTTON_SIZE);
        endTurn.setPosition(DEFAULT_WIDTH - BUTTON_SIZE - PADDING, BUTTON_SIZE + PADDING);
        endTurn.addListener(new TextTooltip("Cliquez ici pour terminer votre tour.", skin));
        endTurn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                if (currentPlayerId == id) {
                    client.endTurn();
                }
            }
        });

        donjonStack = new TextButton("Donjon", skin);
        donjonStack.setSize(BUTTON_SIZE, BUTTON_SIZE);
        donjonStack.setPosition(DEFAULT_WIDTH / 2 - BUTTON_SIZE - PADDING / 2, DEFAULT_HEIGHT / 2);
        donjonStack.addListener(new TextTooltip("Donjon \n\n Cliquez ici pour commencer votre tour.", skin));
        donjonStack.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                if (currentPlayerId == id) {
                    Card card = client.drawDonjonCard();
                    if (card != null) {
                        player.getDeck().addCard(card);
                        displayDeck();
                    }
                }
            }
        });

        treasureStack = new TextButton("Trésor", skin);
        treasureStack.setSize(BUTTON_SIZE, BUTTON_SIZE);
        treasureStack.setPosition(DEFAULT_WIDTH / 2 + PADDING / 2, DEFAULT_HEIGHT / 2);
        treasureStack.addListener(new TextTooltip("Trésor", skin));
        
        stage.addActor(inventory);
        stage.addActor(endTurn);
        stage.addActor(donjonStack);
        stage.addActor(treasureStack);

        // Load all the card textures
        int[] cardsId = ConfigLoader.getIdArray();
        for (int i = 0; i < ConfigLoader.getInt("numberOfCards"); i++) {
            System.out.println(cardsId[i] + "chargée");
            assets.load("cards/" + cardsId[i] + ".png", Texture.class);
            assets.finishLoading();
        }

        displayDeck();

        new Thread(() -> {
            while (client.getInitGame()) {
                wait(WAITING_TIME);
                players = client.getPlayerList();
                wait(WAITING_TIME);
                currentPlayerId = client.getCurrentPlayer().getId();
                wait(WAITING_TIME);
            }
        }).start();

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(135 / 255f, 206 / 255f, 250 / 255f, 1);
        stage.act();
        batch.begin();
        font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 5, 28);
        font.draw(batch, name + " - " + "Level : " + player.getLevel() + " - " + "Atk : " + player.getDamage(), 60, DEFAULT_HEIGHT - 40);
        font.draw(batch, "Current player : " + players.get(currentPlayerId).getName(), DEFAULT_WIDTH / 2 - 250, DEFAULT_HEIGHT - 40);

        batch.end();
        stage.draw();
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    private void displayDeck() {
        int i = 0;
        Deck deck = player.getDeck();
        cardTable.clear();

        for (Card card : player.getDeck().getCards()) {
            ImageButton img = new ImageButton(new TextureRegionDrawable(new TextureRegion(assets.get("cards/" + card.getId() + ".png", Texture.class))));
            cardTable.add(img).size(220, 300);
            img.addListener(new TextTooltip(card.getName() + "\n\n" + card.getDescription(), skin));
            // small padding between cards
            cardTable.add().pad(PADDING / 2);
            
            System.out.println("Card " + card.getName() + " added");
            i++;
        }
    }

    private void wait(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}