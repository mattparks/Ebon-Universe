package game.editors.particles;

import flounder.engine.*;
import flounder.engine.implementation.*;
import game.*;
import game.cameras.*;

public class EditorParticle {
	public static void main(String[] args) {
		Implementation implementation = new Implementation(new ParticleGame(), new CameraFocus(), new MainRenderer(), -1);
		FlounderEngine engine = new FlounderEngine(implementation, 1080, 720, "Particle Editor", true, true, 4, false);
		engine.startEngine(FlounderEngine.getFonts().bungee);
		System.exit(1);
	}
}
