package game.models;

import flounder.maths.*;
import flounder.textures.*;

/**
 * Represents a 3D model with a texture.
 */
public class ModelTextured {
	private final Model model;
	private final Texture modelTexture;
	private Texture normalTexture;

	private Colour colourMod;
	private boolean useFakeLighting;
	private float shineDamper;
	private float reflectivity;

	/**
	 * Creates a new textured model.
	 *
	 * @param model The raw model too be used in this object.
	 * @param modelTexture The texture that will be used when rendering this object.
	 */
	public ModelTextured(Model model, Texture modelTexture) {
		this(model, modelTexture, null);
	}

	/**
	 * Creates a new textured model with a normal map attached as well.
	 *
	 * @param model The raw model too be used in this object.
	 * @param modelTexture The texture that will be used when rendering this object.
	 * @param normalTexture The normal map that can be used when lighting this object.
	 */
	public ModelTextured(Model model, Texture modelTexture, Texture normalTexture) {
		this.model = model;
		this.modelTexture = modelTexture;
		this.normalTexture = normalTexture;
		colourMod = new Colour(0, 0, 0);
		shineDamper = 1;
		reflectivity = 0;
	}

	public Model getModel() {
		return model;
	}

	public Texture getModelTexture() {
		return modelTexture;
	}

	public Texture getNormalTexture() {
		return normalTexture;
	}

	public void setNormalTexture(Texture normalTexture) {
		this.normalTexture = normalTexture;
	}

	public Colour getColourMod() {
		return colourMod;
	}

	public void setColourMod(Colour colourMod) {
		this.colourMod = colourMod;
	}

	public boolean isUseFakeLighting() {
		return useFakeLighting;
	}

	public void setUseFakeLighting(boolean hasFakeLighting) {
		useFakeLighting = hasFakeLighting;
	}

	public float getShineDamper() {
		return shineDamper;
	}

	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}

	public float getReflectivity() {
		return reflectivity;
	}

	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}
}
