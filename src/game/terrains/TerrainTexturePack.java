package game.terrains;

import flounder.textures.*;

/**
 * Class that represents a pack of terrain textures.
 */
public class TerrainTexturePack {
	private Texture backgroundTexture;
	private Texture rTexture;
	private Texture gTexture;
	private Texture bTexture;

	/**
	 * Creates a new terrain texture pack.
	 *
	 * @param backgroundTexture The default texture to renderObjects with.
	 * @param rTexture On the blend map, any r indicates this texture will be rendered.
	 * @param gTexture On the blend map, any g indicates this texture will be rendered.
	 * @param bTexture On the blend map, any b indicates this texture will be rendered.
	 */
	public TerrainTexturePack(Texture backgroundTexture, Texture rTexture, Texture gTexture, Texture bTexture) {
		this.backgroundTexture = backgroundTexture;
		this.rTexture = rTexture;
		this.gTexture = gTexture;
		this.bTexture = bTexture;
	}

	public Texture getBackgroundTexture() {
		return backgroundTexture;
	}

	public Texture getRTexture() {
		return rTexture;
	}

	public Texture getGTexture() {
		return gTexture;
	}

	public Texture getBTexture() {
		return bTexture;
	}
}
