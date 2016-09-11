package game;

import flounder.engine.*;
import flounder.engine.implementation.*;
import flounder.exceptions.*;
import flounder.helpers.*;
import flounder.inputs.*;
import flounder.lights.*;
import flounder.maths.*;
import flounder.maths.vectors.*;
import flounder.parsing.*;
import flounder.particles.*;
import flounder.particles.loading.*;
import flounder.particles.spawns.*;
import flounder.resources.*;
import game.cameras.*;
import game.celestial.*;
import game.entities.loading.*;
import game.options.*;
import game.players.*;

import java.util.*;

import static org.lwjgl.glfw.GLFW.*;

public class MainGame extends IGame {
	public static final Config CONFIG = new Config(new MyFile("configs", "settings.conf"));
	public static final Config POST_CONFIG = new Config(new MyFile("configs", "post.conf"));
	public static final Config CONTROLS_CONFIG = new Config(new MyFile("configs", "controls_joystick.conf"));

	private KeyButton screenshot;
	private KeyButton fullscreen;
	private KeyButton polygons;
	private CompoundButton toggleMusic;
	private CompoundButton skipMusic;

	private IPlayer player;

	private boolean stillLoading;

	@Override
	public void init() {
		this.screenshot = new KeyButton(GLFW_KEY_F2);
		this.fullscreen = new KeyButton(GLFW_KEY_F11);
		this.polygons = new KeyButton(GLFW_KEY_P);
		this.toggleMusic = new CompoundButton(new KeyButton(GLFW_KEY_DOWN), new JoystickButton(OptionsControls.JOYSTICK_PORT, OptionsControls.JOYSTICK_MUSIC_PAUSE));
		this.skipMusic = new CompoundButton(new KeyButton(GLFW_KEY_LEFT, GLFW_KEY_RIGHT), new JoystickButton(OptionsControls.JOYSTICK_PORT, OptionsControls.JOYSTICK_MUSIC_SKIP));
		this.stillLoading = true;

		final int GALAXY_STARS = 5;
		final double GALAXY_VOLUME = GALAXY_STARS * 4.2;
		final double GALAXY_RADIUS = Math.pow(3.0 * (GALAXY_VOLUME / (4.0 * Math.PI)), 0.25);
		Star[] stars = new Star[GALAXY_STARS];

		for (int i = 0; i < GALAXY_STARS; i++) {
			Vector3f spawnPosition = new Vector3f();
			Maths.generateRandomUnitVector(spawnPosition);
			spawnPosition.scale((float) GALAXY_RADIUS);
			double a = Maths.RANDOM.nextDouble();
			double b = Maths.RANDOM.nextDouble();

			if (a > b) {
				double temp = a;
				a = b;
				b = temp;
			}

			double randX = b * Math.cos(6.283185307179586 * (a / b));
			double randY = b * Math.sin(6.283185307179586 * (a / b));
			float distance = new Vector2f((float) randX, (float) randY).length();
			spawnPosition.scale(distance);
			stars[i] = StarGenerator.generateStar(spawnPosition);
		}

		Arrays.sort(stars);

		for (Star star : stars) {
			Star.printSystem(star);
		}

		Star.StarType currentType = null;
		int currentTypeCount = 0;
		int currentTypePlanets = 0;
		int currentTypeHabitable = 0;

		int totalCount = 0;
		int totalPlanets = 0;
		int totalHabitable = 0;

		for (int i = 0; i <= stars.length; i++) {
			if (i >= stars.length || currentType != stars[i].getStarType()) {
				if (currentType != null) {
					System.err.println(currentType.name() + ": Stars=" + currentTypeCount + ", Planets=" + currentTypePlanets + ", Habitability=" + (Maths.roundToPlace(((double) currentTypeHabitable) / ((double) currentTypePlanets) * 100.0, 3)) + "%");
					totalCount += currentTypeCount;
					totalPlanets += currentTypePlanets;
					totalHabitable += currentTypeHabitable;
				}

				currentTypeCount = 0;
				currentTypePlanets = 0;
				currentTypeHabitable = 0;
			}

			if (i < stars.length) {
				currentType = stars[i].getStarType();
				currentTypeCount++;
				currentTypePlanets += stars[i].getChildObjects().size();

				for (Celestial celestial : stars[i].getChildObjects()) {
					if (celestial.supportsLife()) {
						currentTypeHabitable++;
					}
				}
			}
		}

		System.err.println("Total Stars=" + totalCount + ", Total Planets=" + totalPlanets + ", Total Habitability: " + (Maths.roundToPlace(((double) totalHabitable) / ((double) totalPlanets) * 100.0, 3)) + "%");
	}

	public void generateWorlds() {
		Environment.init(new Fog(new Colour(1.0f, 1.0f, 1.0f), 0.003f, 2.0f, 0.0f, 50.0f), new Light(new Colour(0.85f, 0.85f, 0.85f), new Vector3f(0.0f, 2000.0f, 2000.0f)));

		// EntityLoader.load("dragon").createEntity(Environment.getEntitys(), new Vector3f(30, 0, 0), new Vector3f());
		EntityLoader.load("pane").createEntity(Environment.getEntities(), new Vector3f(), new Vector3f());
		EntityLoader.load("sphere").createEntity(Environment.getEntities(), Environment.getLights().get(0).position, new Vector3f());

		for (int n = 0; n < 32; n++) {
			for (int p = 0; p < 32; p++) {
				for (int q = 0; q < 32; q++) {
					if (Maths.RANDOM.nextInt(10) == 1) {
						EntityLoader.load("crate").createEntity(Environment.getEntities(), new Vector3f((n * 5) + 10, (p * 5) + 10, (q * 5) + 10), new Vector3f(0, Maths.RANDOM.nextInt(360), 0));
					}
				}
			}
		}

		ParticleSystem particleSystem = new ParticleSystem(new ArrayList<>(), null, 1750.0f, 1.9f, -0.03f);
		particleSystem.addParticleType(ParticleLoader.load("cosmic"));
		particleSystem.addParticleType(ParticleLoader.load("cosmicHot"));
		particleSystem.randomizeRotation();
		particleSystem.setSpawn(new SpawnCircle(20, new Vector3f(0.0f, 1.0f, 0.0f)));
		particleSystem.setSystemCentre(new Vector3f(0, 20, -10));
	}

	public void generatePlayer() {
		if (FlounderEngine.getCamera() instanceof CameraFocus) {
			this.player = new PlayerFocus();
		} else if (FlounderEngine.getCamera() instanceof CameraFPS) {
			this.player = new PlayerFPS();
		} else {
			throw new FlounderRuntimeException("Could not find IPlayer implementation for ICamera!");
		}

		this.player.init();
	}

	public void destroyWorld() {
		player = null;
		FlounderEngine.getParticles().clearAllParticles();
		Environment.destroy();
		System.gc();
	}

	@Override
	public void update() {
		if (screenshot.wasDown()) {
			FlounderEngine.getDevices().getDisplay().screenshot();
		}

		if (fullscreen.wasDown()) {
			FlounderEngine.getDevices().getDisplay().setFullscreen(!FlounderEngine.getDevices().getDisplay().isFullscreen());
		}

		if (polygons.wasDown()) {
			OpenGlUtils.goWireframe(!OpenGlUtils.isInWireframe());
		}

		if (toggleMusic.wasDown()) {
			if (FlounderEngine.getDevices().getSound().getMusicPlayer().isPaused()) {
				FlounderEngine.getDevices().getSound().getMusicPlayer().unpauseTrack();
			} else {
				FlounderEngine.getDevices().getSound().getMusicPlayer().pauseTrack();
			}
		}

		if (skipMusic.wasDown()) {
			MainSeed.randomize();
			FlounderEngine.getDevices().getSound().getMusicPlayer().skipTrack();
		}

		if (FlounderEngine.getManagerGUI().isMenuIsOpen()) {
			// Pause the music for the start screen.
			FlounderEngine.getDevices().getSound().getMusicPlayer().pauseTrack();
		} else if (!FlounderEngine.getManagerGUI().isMenuIsOpen() && stillLoading) {
			// Unpause the music for the main menu.
			stillLoading = false;
			//	FlounderEngine.getLogger().log("Starting main menu music.");
			//	FlounderEngine.getDevices().getSound().getMusicPlayer().unpauseTrack();
		}

		if (player != null) {
			player.update(FlounderEngine.getManagerGUI().isMenuIsOpen());
			update(player.getPosition(), player.getRotation());
		}

		Environment.update();
	}

	@Override
	public void dispose() {
		CONTROLS_CONFIG.dispose();
		POST_CONFIG.dispose();
		CONFIG.dispose();
	}
}

