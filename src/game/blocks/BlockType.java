package game.blocks;

import flounder.engine.*;
import flounder.textures.*;

import java.util.*;

public class BlockType {
	private static List<BlockType> blockTypes = new ArrayList<>();

	static {
		new BlockType("game::grass", null, 1);
		new BlockType("game::dirt", null, 1);
		new BlockType("game::stone", null, 1);
	}

	private final String name;
	private final Texture texture;
	private final float extent;

	public BlockType(final String name, final Texture texture, final float extent) {
		this.name = name;
		this.texture = texture;
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

	public float getExtent() {
		return extent;
	}
}
