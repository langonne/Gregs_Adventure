package org.gregsquad.gregsadventure;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

// Debugging imports
import org.gregsquad.gregserver.*;
import org.gregsquad.gregsadventure.card.*;
import org.gregsquad.gregsadventure.game.*;
import java.util.logging.Logger;

public class GregsAdventure extends ApplicationAdapter {

	// Debugging variables
	private static final Logger LOGGER = Logger.getLogger(GregsAdventure.class.getName());

	SpriteBatch batch;
	Texture img;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		
		// Debugging code
		new Thread(() -> {
			Server server = new Server(27093);
			server.run();
		}).start();
		new Thread(() -> {
			Client client = new Client("localhost", 27093, "Greg");
			client.run();
		}).start();
		
		LOGGER.info("Server started");
	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 0, 0, 1);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();

	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
