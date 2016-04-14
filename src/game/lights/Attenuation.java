package game.lights;

import flounder.maths.vectors.*;

/**
 * Attenuation is used in calculating the range of engine.lights.
 */
public class Attenuation {
	private float constant;
	private float linear;
	private float exponent;
	private final Vector3f attenuationVector;

	/**
	 * Creates a Attenuation object used in engine.lights. The calculation used is as follows:<br>
	 * {@code factor = constant + (linear * cameraDistance) + (exponent * (cameraDistance * cameraDistance))}
	 *
	 * @param constant The constant Attenuation value.
	 * @param linear The linear Attenuation value.
	 * @param exponent The exponent Attenuation value.
	 */
	public Attenuation(final float constant, final float linear, final float exponent) {
		this.constant = constant;
		this.linear = linear;
		this.exponent = exponent;
		this.attenuationVector = new Vector3f();
	}

	public void set(final Attenuation source) {
		this.constant = source.constant;
		this.linear = source.linear;
		this.exponent = source.exponent;
	}

	/**
	 * @return Returns a equivalent but in Vector3f form.
	 */
	public Vector3f toVector() {
		attenuationVector.set(constant, linear, exponent);
		return attenuationVector;
	}

	public float getConstant() {
		return constant;
	}

	public void setConstant(float constant) {
		this.constant = constant;
	}

	public float getLinear() {
		return linear;
	}

	public void setLinear(float linear) {
		this.linear = linear;
	}

	public float getExponent() {
		return exponent;
	}

	public void setExponent(float exponent) {
		this.exponent = exponent;
	}
}

