import flounder.devices.*;
import flounder.guis.*;
import flounder.resources.*;
import flounder.textures.*;

import java.util.*;

public class MainGuiExample extends GuiComponent {
	private GuiTexture guiLogos;

	public MainGuiExample() {
		guiLogos = new GuiTexture(Texture.newTexture(new MyFile(MyFile.RES_FOLDER, "icon.png")).create());
	}

	@Override
	protected void updateSelf() {
		guiLogos.setPosition(0.25f, 0.25f, 0.5f * ManagerDevices.getDisplay().getDisplayAspectRatio(), 0.5f * ManagerDevices.getDisplay().getDisplayAspectRatio());
		guiLogos.update();
	}

	@Override
	protected void getGuiTextures(List<GuiTexture> guiTextures) {
		guiTextures.add(guiLogos);
	}
}
