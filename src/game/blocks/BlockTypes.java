package game.blocks;

import flounder.engine.*;
import flounder.maths.*;
import flounder.maths.vectors.*;
import flounder.resources.*;
import flounder.sounds.*;
import flounder.textures.*;
import game.models.*;

import java.util.*;

public class BlockTypes {
	public static final float BLOCK_EXTENT = 1.0f;
	private static List<BlockTypes> BLOCK_TYPES = new ArrayList<>();

	public static TextureTessellatorAtlas tessellatorAtlas = new TextureTessellatorAtlas(256, 256, Texture.newTexture(new MyFile(MyFile.RES_FOLDER, "game/blocks", "atlas.png")).create());

	public static Model MODEL_DEFAULT_CUBE = LoaderOBJ.loadOBJ(new MyFile(MyFile.RES_FOLDER, "game/blocks", "cube.obj"));

	private static Sound SOUND_DEFAULT_PLACE = Sound.loadSoundNow(new MyFile(MyFile.RES_FOLDER, "sounds", "blockPlace.wav"), 1.0f);
	private static Sound SOUND_DEFAULT_BREAK = Sound.loadSoundNow(new MyFile(MyFile.RES_FOLDER, "sounds", "blockBreak.wav"), 1.0f);

	static {
		loadTexCoords();

		new BlockTypes("game::bedrock", new Colour(47.0f, 47.0f, 47.0f, true), MODEL_DEFAULT_CUBE, SOUND_DEFAULT_PLACE, SOUND_DEFAULT_BREAK);
		new BlockTypes("game::grass", new Colour(105.0f, 196.0f, 63.0f, true), MODEL_DEFAULT_CUBE, SOUND_DEFAULT_PLACE, SOUND_DEFAULT_BREAK);
		new BlockTypes("game::sand", new Colour(223.0f, 209.0f, 162.0f, true), MODEL_DEFAULT_CUBE, SOUND_DEFAULT_PLACE, SOUND_DEFAULT_BREAK);
		new BlockTypes("game::dirt", new Colour(121.0f, 85.0f, 58.0f, true), MODEL_DEFAULT_CUBE, SOUND_DEFAULT_PLACE, SOUND_DEFAULT_BREAK);
		new BlockTypes("game::water", new Colour(35.0f, 86.0f, 240.0f, true), MODEL_DEFAULT_CUBE, SOUND_DEFAULT_PLACE, SOUND_DEFAULT_BREAK);
		new BlockTypes("game::stone", new Colour(127.0f, 127.0f, 127.0f, true), MODEL_DEFAULT_CUBE, SOUND_DEFAULT_PLACE, SOUND_DEFAULT_BREAK);

		new BlockTypes("game::coalOre", new Colour(49.0f, 49.0f, 49.0f, true), MODEL_DEFAULT_CUBE, SOUND_DEFAULT_PLACE, SOUND_DEFAULT_BREAK);
		new BlockTypes("game::ironOre", new Colour(202.0f, 164.0f, 143.0f, true), MODEL_DEFAULT_CUBE, SOUND_DEFAULT_PLACE, SOUND_DEFAULT_BREAK);
		new BlockTypes("game::goldOre", new Colour(236.0f, 236.0f, 80.0f, true), MODEL_DEFAULT_CUBE, SOUND_DEFAULT_PLACE, SOUND_DEFAULT_BREAK);

		new BlockTypes("game::wood", new Colour(81.0f, 63.0f, 30.0f, true), MODEL_DEFAULT_CUBE, SOUND_DEFAULT_PLACE, SOUND_DEFAULT_BREAK);
		new BlockTypes("game::leafs", new Colour(10.0f, 77.0f, 0.0f, true), MODEL_DEFAULT_CUBE, SOUND_DEFAULT_PLACE, SOUND_DEFAULT_BREAK);
	}

	private static void loadTexCoords() {
		tessellatorAtlas.registerTextureCoords("Cobblestone", new Vector2f(0, 0));
		tessellatorAtlas.registerTextureCoords("Grass", new Vector2f(16, 0));
		tessellatorAtlas.registerTextureCoords("GrassSide", new Vector2f(32, 0));
		tessellatorAtlas.registerTextureCoords("GrassSideSnow", new Vector2f(48, 0));
		tessellatorAtlas.registerTextureCoords("GrassSnow", new Vector2f(64, 0));
		tessellatorAtlas.registerTextureCoords("Dirt", new Vector2f(80, 0));
		tessellatorAtlas.registerTextureCoords("Indes", new Vector2f(96, 0));
		tessellatorAtlas.registerTextureCoords("Ice", new Vector2f(112, 0));
		tessellatorAtlas.registerTextureCoords("Leaves", new Vector2f(128, 0));
		tessellatorAtlas.registerTextureCoords("LeavesSnow", new Vector2f(144, 0));
		tessellatorAtlas.registerTextureCoords("Sand", new Vector2f(160, 0));
		tessellatorAtlas.registerTextureCoords("SandSnow", new Vector2f(176, 0));
		tessellatorAtlas.registerTextureCoords("Water", new Vector2f(192, 0));
		tessellatorAtlas.registerTextureCoords("Stone", new Vector2f(208, 0));
		tessellatorAtlas.registerTextureCoords("Wood", new Vector2f(224, 0));
		tessellatorAtlas.registerTextureCoords("DimOre", new Vector2f(240, 0));
		tessellatorAtlas.registerTextureCoords("Glass", new Vector2f(0, 16));
		tessellatorAtlas.registerTextureCoords("GoldOre", new Vector2f(16, 16));
		tessellatorAtlas.registerTextureCoords("Lava", new Vector2f(32, 16));
		tessellatorAtlas.registerTextureCoords("LavaSide", new Vector2f(48, 16));
		tessellatorAtlas.registerTextureCoords("Pedestal", new Vector2f(64, 16));
		tessellatorAtlas.registerTextureCoords("PedestalBottom", new Vector2f(80, 16));
		tessellatorAtlas.registerTextureCoords("PedestalTop", new Vector2f(96, 16));
		tessellatorAtlas.registerTextureCoords("Missing", new Vector2f(240, 240));
	}

	private final String name;
	private final Colour colour;

	private final Model model;

	private final Sound placeSound;
	private final Sound breakSound;

	public BlockTypes(final String name, final Colour colour, final Model model, final Sound placeSound, final Sound breakSound) {
		this.name = name;
		this.colour = colour;

		this.model = model;

		this.placeSound = placeSound;
		this.breakSound = breakSound;

		boolean typeExists = false;

		for (final BlockTypes type : BLOCK_TYPES) {
			if (type.getName().equals(name)) {
				FlounderLogger.error("Block type with name " + name + " already exists! Ignoring new type request.");
				typeExists = true;
			}
		}

		if (!typeExists) {
			BLOCK_TYPES.add(this);
		}
	}

	public String getName() {
		return name;
	}

	public static BlockTypes get(final String name) {
		for (final BlockTypes type : BLOCK_TYPES) {
			if (type.getName().equals(name)) {
				return type;
			}
		}

		FlounderLogger.error("Could not get block with type: " + name + ". Returning null!");
		return null;
	}

	public Colour getColour() {
		return colour;
	}

	public Model getModel() {
		return model;
	}

	public Sound getPlaceSound() {
		return placeSound;
	}

	public Sound getBreakSound() {
		return breakSound;
	}
}
