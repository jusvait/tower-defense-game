package TD;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title  = "Tower Defense";
		config.width  = CONSTANTS.WINDOW_WIDTH();
		config.height = CONSTANTS.WINDOW_HEIGHT();
		config.resizable = false;

		config.addIcon("icon.png", Files.FileType.Internal);
		// Limit frames per second to 30
		config.foregroundFPS = 30;
		config.backgroundFPS = 30;

		new LwjglApplication(new TowerDefense(), config);
	}
}
