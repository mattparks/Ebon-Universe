package game.uis.screens;

import flounder.engine.*;
import flounder.fonts.*;
import flounder.guis.*;
import flounder.maths.*;
import flounder.resources.*;
import flounder.textures.*;
import flounder.visual.*;
import game.*;

import java.util.*;

public class ScreenStartup extends GuiComponent {
	private static final Colour STARTUP_BACKGROUND = new Colour(1.0f, 1.0f, 1.0f);

	public static final Colour TEXT_COLOUR = new Colour(0.75f, 0.75f, 0.75f);
	public static final float MAIN_TITLE_FONT_SIZE = 2.5f;
	public static final float MAIN_SUBTITLE_FONT_SIZE = 0.75f;
	private static final float LOAD_TIME = FlounderEngine.getLogger().inJar() ? 20.0f : 7.0f;

	private ValueDriver loadingDriver;
	private boolean stageLoadingStart;

	private GuiTexture logoTexture;

	private Text titleText;
	private Text subtitleText;
	private SinWaveDriver titleColourX;
	private SinWaveDriver titleColourY;

	public ScreenStartup() {
		loadingDriver = new SlideDriver(0.0f, 1.0f, LOAD_TIME / 2.0f);
		stageLoadingStart = true;

		logoTexture = new GuiTexture(Texture.newTexture(new MyFile(MyFile.RES_FOLDER, "flounder.png")).createInSecondThread());
		logoTexture.getTexture().setNumberOfRows(1);
		logoTexture.setSelectedRow(1);
		logoTexture.setAlphaDriver(loadingDriver);

		titleText = Text.newText("Powered By Flounder").centre().setFontSize(MAIN_TITLE_FONT_SIZE).create();
		titleText.setColour(TEXT_COLOUR);
		titleText.setBorderColour(TEXT_COLOUR.r, TEXT_COLOUR.g, TEXT_COLOUR.b);
		titleText.setGlowing(new SinWaveDriver(0.075f, 0.150f, 2.320f));
		titleText.setAlphaDriver(loadingDriver);
		addText(titleText, -0.5f, 0.6f, 2.0f);

		subtitleText = Text.newText("Copyright © 2015-2016, Equilibrium Games, All Rights Reserved.").centre().setFontSize(MAIN_SUBTITLE_FONT_SIZE).create();
		subtitleText.setColour(TEXT_COLOUR);
		subtitleText.setAlphaDriver(loadingDriver);
		subtitleText.setBorderColour(0.15f, 0.15f, 0.15f);
		subtitleText.setBorder(new ConstantDriver(0.04f));
		addText(subtitleText, -0.5f, 0.75f, 2.0f);

		titleColourX = new SinWaveDriver(0.0f, 1.0f, 40.0f);
		titleColourY = new SinWaveDriver(0.0f, 1.0f, 20.0f);

		MainGuis.STARTUP_COLOUR.set(STARTUP_BACKGROUND);
	}

	@Override
	protected void updateSelf() {
		float averageArea = (FlounderEngine.getDevices().getDisplay().getWidth() + FlounderEngine.getDevices().getDisplay().getHeight()) / 2.0f;
		float width = (250.0f / averageArea) * 1.25f;
		float height = (250.0f / averageArea) * 1.25f * FlounderEngine.getDevices().getDisplay().getAspectRatio();
		logoTexture.setPosition(0.5f - (width / 2.0f), 0.375f - (height / 2.0f), width, height);
		logoTexture.update();

		titleText.setColour(titleColourX.update(FlounderEngine.getDelta()), titleColourY.update(FlounderEngine.getDelta()), 0.3f);
		titleText.setBorderColour(titleText.getColour().r, titleText.getColour().g, titleText.getColour().b);

		if (stageLoadingStart && loadingDriver.update(FlounderEngine.getDelta()) == 1.0f) {
			stageLoadingStart = false;
			loadingDriver = new SlideDriver(1.0f, 0.0f, LOAD_TIME / 2.0f);
			logoTexture.setAlphaDriver(loadingDriver);
			titleText.setAlphaDriver(loadingDriver);
			subtitleText.setAlphaDriver(loadingDriver);
		}

		if (!stageLoadingStart) {
			float transparency = loadingDriver.update(FlounderEngine.getDelta());
			Colour.interpolate(Environment.getFog().getFogColour(), STARTUP_BACKGROUND, transparency, MainGuis.STARTUP_COLOUR);

			if (transparency == 0.0f) {
				show(false);
				FlounderEngine.getManagerGUI().openMenu();
			}
		}
	}

	@Override
	protected void getGuiTextures(List<GuiTexture> guiTextures) {
		guiTextures.add(logoTexture);
	}
}
