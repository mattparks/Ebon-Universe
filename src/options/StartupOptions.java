package options;

import javax.swing.*;
import java.awt.*;

public class StartupOptions extends JFrame {
	private final JPanel panel;

	private boolean fullscreen;
	private String resolution;

	public StartupOptions() {
		// The JFrame.
		super.setTitle("Voxel Options");
		super.setSize(450, 475);
		final Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		final int x = (int) ((dimension.getWidth() - super.getWidth()) / 2);
		final int y = (int) ((dimension.getHeight() - super.getHeight()) / 2);
		super.setLocation(x, y);
		super.setDefaultCloseOperation(EXIT_ON_CLOSE);

		// The main panel.
		panel = new JPanel();
		super.add(panel);

		// The Screen Resolution.
		final JLabel resLabel = new JLabel("Screen Resolution");
		resLabel.setVisible(true);
		panel.add(resLabel);

		final String[] resChoices = {"1024 x 768", "1152 x 864", "1280 x 720", "1280 x 768", "1280 x 800", "1280 x 960", "1280 x 1024", "1360 x 768", "1366 x 768",
				"1440 x 900", "1536 x 864", "1600 x 900", "1600 x 1200", "1680 x 1050", "1920 x 1080", "1920 x 1200", "2560 x 1080", "2560 x 1440"};
		final JComboBox<String> resDropdown = new JComboBox<>(resChoices);
		resDropdown.setSelectedItem(resolution = resChoices[3]); // TODO: Grab from config.
		resDropdown.addItemListener(e -> {
			resolution = (String) resDropdown.getSelectedItem();
		});
		resDropdown.setVisible(true);
		panel.add(resDropdown);

		// The fullscreen option.
		final JCheckBox fullscreenCheck = new JCheckBox("Fullscreen");
		fullscreenCheck.setSelected(fullscreen = true); // TODO: Grab from config.
		fullscreenCheck.addItemListener(e -> {
			fullscreen = !fullscreen;
		});
		fullscreenCheck.setVisible(true);
		panel.add(fullscreenCheck);

		// The start game button.
		JButton startButton = new JButton("Start Game");
		startButton.addItemListener(e -> {
			System.out.println("Fullscreen: " + fullscreen);
			System.out.println("Display Res: " + resolution);
		});
		startButton.setVisible(true);
		panel.add(startButton);

		// Shows the display.
		super.setVisible(true);
	}

	public void run() {
		while (super.isVisible()) {

		}
	}
}
