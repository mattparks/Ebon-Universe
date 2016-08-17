package game.post;

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
		filterOutput = FBO.newFBO(1.0f).create();
	}

	public boolean willRunDemo() {
		return OptionsPost.POST_EFFECT > 0 && OptionsPost.POST_EFFECT <= TOTAL_EFFECTS;
	}

	@Override
	public void renderPipeline(FBO startFBO) {
		switch (OptionsPost.POST_EFFECT) {
			case 1:
				filterEmboss.applyFilter(startFBO.getColourTexture(0));
				filterEmboss.fbo.resolveFBO(0, 0, filterOutput);
				break;
			case 2:
				filterGray.applyFilter(startFBO.getColourTexture(0));
				filterGray.fbo.resolveFBO(0, 0, filterOutput);
				break;
			case 3:
				filterNegative.applyFilter(startFBO.getColourTexture(0));
				filterNegative.fbo.resolveFBO(0, 0, filterOutput);
				break;
			case 4:
				filterPixel.applyFilter(startFBO.getColourTexture(0));
				filterPixel.fbo.resolveFBO(0, 0, filterOutput);
				break;
			case 5:
				filterSepia.applyFilter(startFBO.getColourTexture(0));
				filterSepia.fbo.resolveFBO(0, 0, filterOutput);
				break;
			case 6:
				filterTone.applyFilter(startFBO.getColourTexture(0));
				filterTone.fbo.resolveFBO(0, 0, filterOutput);
				break;
			case 7:
				filterWobble.applyFilter(startFBO.getColourTexture(0));
				filterWobble.fbo.resolveFBO(0, 0, filterOutput);
				break;
			default:
				startFBO.resolveFBO(0, 0, filterOutput);
				break;
		}

		if (OptionsPost.FILTER_FXAA) {
			filterFXAA.applyFilter(filterOutput.getColourTexture(0));
			filterFXAA.fbo.resolveFBO(0, 0, filterOutput);
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
