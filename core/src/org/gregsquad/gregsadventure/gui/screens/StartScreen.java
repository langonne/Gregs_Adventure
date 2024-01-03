package org.gregsquad.gregsadventure.gui.screens;

import org.gregsquad.gregsadventure.GregsAdventure;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;

import org.gregsquad.gregserver.Client;
import org.gregsquad.gregserver.Server;


public class StartScreen extends Screen{

    private static final int NAME_MAX_LENGTH = 20;

    private Table table;

    private TextButton clientButton;
    private TextButton serverButton;
    private TextButton cancelButton;

    private boolean wrongNameDisplayed = false; // global variable to avoid displaying the same error message multiple times
    
    
    public StartScreen(GregsAdventure gui, AssetManager assets) {
        super(gui, assets);
        //TODO Auto-generated constructor stub
    }

    @Override
    public void show() {
        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        skin = assets.get("skin/uiskin.json", Skin.class);
        table.setSkin(skin);

        serverButton = new TextButton("Créer une partie", skin);
        clientButton = new TextButton("Rejoindre une partie", skin);
        cancelButton = new TextButton("Retour", skin);

        table.add(serverButton).fillX().uniformX();
        table.row().pad(10, 0, 10, 0);
        table.add(clientButton).fillX().uniformX();
        table.row();
        table.add(cancelButton).fillX().uniformX();

        serverButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                table.clear();

                TextField name = new TextField("", skin);
                TextField port = new TextField("27093", skin);
                TextButton confirmButton = new TextButton("Confirmer", skin);
                TextButton cancelButton = new TextButton("Annuler", skin);

                table.add("Nom du joueur : ");
                table.add(name).fillX().uniformX();
                table.row().pad(10, 0, 10, 0);
                table.add("Port : ");
                table.add(port).fillX().uniformX();
                table.row();
                table.add(confirmButton).fillX().uniformX();
                table.row();
                table.add(cancelButton).fillX().uniformX();

                confirmButton.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                        if (name.getText().isEmpty() || name.getText().length() > NAME_MAX_LENGTH) {
                            if (!wrongNameDisplayed) {
                                wrongNameDisplayed = true;
                                table.add("Nom invalide");
                            }
                        }
                        else {
                            Server server = Server.getInstance();
                            server.init(Integer.parseInt(port.getText()));
                            server.run();

                            new Client("localhost", Integer.parseInt(port.getText()), name.getText());

                        }
                    }
                });

            }
        });

        clientButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                table.clear();

                TextField name = new TextField("", skin);
                TextField ip = new TextField("", skin);
                TextField port = new TextField("", skin);
                TextButton confirmButton = new TextButton("Confirmer", skin);
                TextButton cancelButton = new TextButton("Annuler", skin);    

                table.add("Nom du joueur : ");
                table.add(name).fillX().uniformX();
                table.row().pad(10, 0, 10, 0);
                table.add("Adresse IP : ");
                table.add(ip).fillX().uniformX();
                table.row();
                table.add("Port : ");
                table.add(port).fillX().uniformX();
                table.row();
                table.add(confirmButton).fillX().uniformX();
                table.row();
                table.add(cancelButton).fillX().uniformX(); 

                
                confirmButton.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {

                        if (name.getText().isEmpty() || name.getText().length() > NAME_MAX_LENGTH) {
                            if (!wrongNameDisplayed) {
                                wrongNameDisplayed = true;
                                table.add("Nom invalide");
                            }
                        }
                        else {
                            new Client(ip.getText(), Integer.parseInt(port.getText()), name.getText());
                        }
                    }
                });

                cancelButton.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                        gui.setScreen(new StartScreen(gui, assets));
                    }
                });
            }
        });

        cancelButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                gui.setScreen(new TitleScreen(gui, assets));
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
        // TODO Auto-generated method stub
    }
}