package blocks;

import flounder.engine.*;
import flounder.maths.*;

import java.util.*;

public class BlockType {
	private static List<BlockType> BLOCK_TYPES = new ArrayList<>();

	static {
		new BlockType("game::grass", new Colour(105.0f, 196.0f, 63.0f, true), 1.0f);
		new BlockType("game::dirt", new Colour(121.0f, 85.0f, 58.0f, true), 1.0f);
		new BlockType("game::stone", new Colour(127.0f, 127.0f, 127.0f, true), 1.0f);
	}

	private final String name;
	private final Colour colour;
	private final float extent;

	public BlockType(final String name, final Colour colour, final float extent) {
		this.name = name;
		this.colour = colour;
		this.extent = extent;

		boolean typeExists = false;

		for (final BlockType type : BLOCK_TYPES) {
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

	public static BlockType get(final String name) {
		for (final BlockType type : BLOCK_TYPES) {
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

	public float getExtent() {
		return extent;
	}
}
