package org.gregsquad.gregsadventure.gui.screens;

import org.gregsquad.gregsadventure.GregsAdventure;

import java.util.ArrayList;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;

import com.badlogic.gdx.Gdx;

import org.gregsquad.gregsadventure.game.Player;
import org.gregsquad.gregserver.Client;
import org.gregsquad.gregserver.Server;


public class StartScreen extends Screen{

    private static final int MIN_PLAYERS = 1;
    private static final int MAX_PLAYERS = 6;
    private static final int NAME_MAX_LENGTH = 20;

    private Client client;

    private Table table;

    private TextButton clientButton;
    private TextButton serverButton;
    private TextButton cancelButton;

    private boolean wrongNameDisplayed = false; // global variable to avoid displaying the same error message multiple times
    private boolean gameStarted = false; // global variable for the lobby loop

    
    
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

        serverButton.addListener(new ChangeListener() { // Créer une partie
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
                            new Thread(() -> {
                                server.run();
                            }).start();

                            client = new Client("localhost", Integer.parseInt(port.getText()), name.getText());

                            new Thread(() -> {
                                client.run();
                            }).start();

                            table.clear();

                            TextButton confirmButton = new TextButton("Confirmer", skin);
                            TextButton cancelButton = new TextButton("Annuler", skin);

                            confirmButton.addListener(new ChangeListener() {
                                @Override
                                public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                                    if (client.getPlayerList().size() < MIN_PLAYERS || client.getPlayerList().size() > MAX_PLAYERS) {
                                        table.add("Il faut entre 3 et 6 joueurs pour jouer.");
                                    }
                                    else {
                                        gameStarted = true;
                                        client.initGame();
                                        gui.setScreen(new GameScreen(gui, assets, client, client.getId()));
                                    }
                                }
                            });

                            cancelButton.addListener(new ChangeListener() {
                                @Override
                                public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                                    gui.setScreen(new StartScreen(gui, assets));
                                }
                            });

                            
                            new Thread(() -> {
                                while(!gameStarted) {
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    
                                    displayPlayers(client, table);

                                    table.add(confirmButton).fillX().uniformX();
                                    table.row();
                                    table.add(cancelButton).fillX().uniformX();
                                }
                            }).start();


                        } // fin else (=nom valide)
                    }
                }); // fin confirmButton.addListener

                cancelButton.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                        gui.setScreen(new StartScreen(gui, assets));
                    }
                });

            }
        });

        clientButton.addListener(new ChangeListener() { // Rejoindre une partie
            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                table.clear();

                TextField name = new TextField("", skin);
                TextField ip = new TextField("localhost", skin);
                TextField port = new TextField("27093", skin);
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
                            client = new Client(ip.getText(), Integer.parseInt(port.getText()), name.getText());
                            new Thread(() -> {
                                client.run();
                            }).start();

                            //Wait 0.5s for the server to send the player list
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            
                            new Thread(() -> {
                                // Here was a wait
                                System.out.println(client.getInitGame());
                                while(!client.getInitGame()) {
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    
                                    displayPlayers(client, table);

                                    table.row();
                                    table.add(cancelButton).fillX().uniformX();
                                    cancelButton.addListener(new ChangeListener() {
                                        @Override
                                        public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                                            // TODO disconnect from server
                                            gui.setScreen(new StartScreen(gui, assets));
                                        }
                                    });
                                }
                                Gdx.app.postRunnable(() -> gui.setScreen(new GameScreen(gui, assets, client, client.getId())));
                            }).start();
                            /////////
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

    private void displayPlayers(Client client, Table table) {
        table.clear();
        ArrayList<Player> players = client.getPlayerList();

        table.add("Joueurs : " + players.size() + "/6");
        table.row();
        for (Player player : players) {
            table.add("- " + player.getName() + '\n');
        }
        table.row();
    }
}