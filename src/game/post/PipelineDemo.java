package game.post;

import flounder.engine.*;
import flounder.fbos.*;
import flounder.post.*;
import flounder.post.filters.*;
import game.options.*;

public class PipelineDemo extends PostPipeline {
	public static final int TOTAL_EFFECTS = 7;

	private FilterEmboss filterEmboss;
	private FilterGray filterGray;
	private FilterNegative filterNegative;
	private FilterPixel filterPixel;
	private FilterSepia filterSepia;
	private FilterTone filterTone;
	private FilterWobble filterWobble;
	private FilterFXAA filterFXAA;
	private FBO filterOutput;

	public PipelineDemo() {
		filterEmboss = new FilterEmboss();
		filterGray = new FilterGray();
		filterNegative = new FilterNegative();
		filterPixel = new FilterPixel();
		filterSepia = new FilterSepia();
		filterTone = new FilterTone();
		filterWobble = new FilterWobble();
		filterFXAA = new FilterFXAA();
		filterFXAA.setSpanMaxValue(4.0f);
		filterOutput = FBO.newFBO(FlounderEngine.getDevices().getDisplay().getWidth(), FlounderEngine.getDevices().getDisplay().getHeight()).fitToScreen().create();
	}

	@Override
	public void renderPipeline(FBO startFBO) {
		switch (OptionsPost.POST_EFFECT) {
			case 1:
				filterEmboss.applyFilter(startFBO.getColourTexture());
				filterEmboss.fbo.resolveFBO(filterOutput);
				break;
			case 2:
				filterGray.applyFilter(startFBO.getColourTexture());
				filterGray.fbo.resolveFBO(filterOutput);
				break;
			case 3:
				filterNegative.applyFilter(startFBO.getColourTexture());
				filterNegative.fbo.resolveFBO(filterOutput);
				break;
			case 4:
				filterPixel.applyFilter(startFBO.getColourTexture());
				filterPixel.fbo.resolveFBO(filterOutput);
				break;
			case 5:
				filterSepia.applyFilter(startFBO.getColourTexture());
				filterSepia.fbo.resolveFBO(filterOutput);
				break;
			case 6:
				filterTone.applyFilter(startFBO.getColourTexture());
				filterTone.fbo.resolveFBO(filterOutput);
				break;
			case 7:
				filterWobble.applyFilter(startFBO.getColourTexture());
				filterWobble.fbo.resolveFBO(filterOutput);
				break;
			default:
				startFBO.resolveFBO(filterOutput);
				break;
		}

		if (OptionsPost.FILTER_FXAA) {
			filterFXAA.applyFilter(filterOutput.getColourTexture());
			filterFXAA.fbo.resolveFBO(filterOutput);
		}
	}

	@Override
	public FBO getOutput() {
		return filterOutput;
	}

	@Override
	public void dispose() {
		filterEmboss.dispose();
		filterGray.dispose();
		filterNegative.dispose();
		filterPixel.dispose();
		filterSepia.dispose();
		filterTone.dispose();
		filterWobble.dispose();
		filterFXAA.dispose();
	}
}
