package blocks;

import flounder.engine.*;
import flounder.maths.*;

import java.util.*;

public class BlockType {
	private static List<BlockType> BLOCK_TYPES = new ArrayList<>();
	private static float BLOCK_EXTENT = 1.0f;

	static {
		new BlockType("game::bedrock", new Colour(47.0f, 47.0f, 47.0f, true), BLOCK_EXTENT);
		new BlockType("game::grass", new Colour(105.0f, 196.0f, 63.0f, true), BLOCK_EXTENT);
		new BlockType("game::sand", new Colour(223.0f, 209.0f, 162.0f, true), BLOCK_EXTENT);
		new BlockType("game::dirt", new Colour(121.0f, 85.0f, 58.0f, true), BLOCK_EXTENT);
		new BlockType("game::water", new Colour(35.0f, 86.0f, 240.0f, true), BLOCK_EXTENT);
		new BlockType("game::stone", new Colour(127.0f, 127.0f, 127.0f, true), BLOCK_EXTENT);

		new BlockType("game::coalOre", new Colour(49.0f, 49.0f, 49.0f, true), BLOCK_EXTENT);
		new BlockType("game::ironOre", new Colour(202.0f, 164.0f, 143.0f, true), BLOCK_EXTENT);
		new BlockType("game::goldOre", new Colour(236.0f, 236.0f, 80.0f, true), BLOCK_EXTENT);

		new BlockType("game::wood", new Colour(81.0f, 63.0f, 30.0f, true), BLOCK_EXTENT);
		new BlockType("game::leafs", new Colour(10.0f, 77.0f, 0.0f, true), BLOCK_EXTENT);
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
