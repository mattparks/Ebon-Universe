package game.blocks;

import flounder.engine.*;
import flounder.maths.*;
import flounder.resources.*;
import flounder.textures.*;
import game.models.*;

import java.util.*;

public class BlockType {
	private static List<BlockType> blockTypes = new ArrayList<>();

	static {
		new BlockType("game::grass", null, new Colour(105, 196, 63, true), 1.0f);
		new BlockType("game::dirt", null, new Colour(121, 85, 58, true), 1.0f);
		new BlockType("game::stone", null, new Colour(127, 127, 127, true), 1.0f);
	}

	private final String name;
	private final Texture texture;
	private final Colour colour;
	private final float extent;

	public BlockType(final String name, final Texture texture, final Colour colour, final float extent) {
		this.name = name;
		this.texture = texture;
		this.colour = colour;
		this.extent = extent;

		boolean typeExists = false;

		for (final BlockType type : blockTypes) {
			if (type.getName().equals(name)) {
				FlounderLogger.error("Block type with name " + name + " already exists! Ignoring new type request.");
				typeExists = true;
			}
		}

		if (!typeExists) {
			blockTypes.add(this);
		}
	}

	public String getName() {
		return name;
	}

	public static BlockType get(final String name) {
		for (final BlockType type : blockTypes) {
			if (type.getName().equals(name)) {
				return type;
			}
		}

		FlounderLogger.error("Could not get block with type: " + name + ". Returning null!");
		return null;
	}

	public Texture getTexture() {
		return texture;
	}

	public Colour getColour() {
		return colour;
	}

	public float getExtent() {
		return extent;
	}
}
