package game.uis;

import flounder.engine.*;
import flounder.guis.*;
import flounder.visual.*;

import java.util.*;

public class OverlayShading extends GuiComponent {
	private ValueDriver mainDriver;

	public OverlayShading() {
		mainDriver = new ConstantDriver(-MenuGame.SLIDE_SCALAR);

	/*	Text normalsTime = Text.newText("Normals (World)").setFontSize(1.2f).centre().create();
		normalsTime.setColour(MenuGame.TEXT_COLOUR);
		normalsTime.setBorderColour(0.15f, 0.15f, 0.15f);
		normalsTime.setBorder(new ConstantDriver(0.04f));
		super.addText(normalsTime, -0.375f, 0.02f, 1.0f);

		Text specularTime = Text.newText("Specular").setFontSize(1.2f).centre().create();
		specularTime.setColour(MenuGame.TEXT_COLOUR);
		specularTime.setBorderColour(0.15f, 0.15f, 0.15f);
		specularTime.setBorder(new ConstantDriver(0.04f));
		super.addText(specularTime, 0.375f, 0.02f, 1.0f);

		Text colourTime = Text.newText("Colour / Diffuse").setFontSize(1.2f).centre().create();
		colourTime.setColour(MenuGame.TEXT_COLOUR);
		colourTime.setBorderColour(0.15f, 0.15f, 0.15f);
		colourTime.setBorder(new ConstantDriver(0.04f));
		super.addText(colourTime, -0.375f, 0.94f, 1.0f);

		Text positionTime = Text.newText("Position (World)").setFontSize(1.2f).centre().create();
		positionTime.setColour(MenuGame.TEXT_COLOUR);
		positionTime.setBorderColour(0.15f, 0.15f, 0.15f);
		positionTime.setBorder(new ConstantDriver(0.04f));
		super.addText(positionTime, 0.375f, 0.94f, 1.0f);*/

		super.show(true);
	}

	@Override
	public void show(boolean visible) {
		mainDriver = new SlideDriver(getRelativeX(), visible ? 0.0f : -MenuGame.SLIDE_SCALAR, MenuGameBackground.SLIDE_TIME);
	}

	@Override
	protected void updateSelf() {
		float mainValue = mainDriver.update(FlounderEngine.getDelta());

		if (mainValue == -MenuGame.SLIDE_SCALAR) {
			super.show(true);
		} else {
			super.show(true);
		}

		super.setRelativeX(mainValue);
	}

	@Override
	protected void getGuiTextures(List<GuiTexture> guiTextures) {

	}
}
