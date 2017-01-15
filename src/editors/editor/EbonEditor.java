package editors.editor;

import editors.entities.*;
import editors.particles.*;
import flounder.devices.*;
import flounder.fonts.*;
import flounder.framework.*;
import flounder.resources.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Timer;
import java.util.*;

/**
 * The editors entrance and selection class.
 */
public class EbonEditor extends TimerTask {
	private JFrame frame;
	private JRadioButton optionParticle;
	private JRadioButton optionEntities;

	private boolean startEntrance;
	private boolean running;

	public static void main(String[] args) {
		Timer timer = new Timer();
		timer.schedule(new EbonEditor(), 0, 1000);
	}

	private EbonEditor() {
		frame = new JFrame("Flounder Editor");
		frame.setSize(300, 420);
		frame.setResizable(false);
		running = true;

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
			ex.printStackTrace();
		}

		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				frame.setVisible(false);
				running = false;
			}
		});

		frame.setLayout(new FlowLayout());

		optionParticle = new JRadioButton("Particles");
		optionEntities = new JRadioButton("Entities");
		JButton buttonSubmit = new JButton("Submit");

		buttonSubmit.addActionListener((ActionEvent actionEvent) -> {
			System.out.println("Starting entrance from button!");
			startEntrance = true;
		});

		ButtonGroup group = new ButtonGroup();
		group.add(optionParticle);
		group.add(optionEntities);

		frame.add(optionParticle);
		frame.add(optionEntities);
		frame.add(buttonSubmit);
		frame.pack();

		frame.setSize(frame.getWidth() + 64, frame.getHeight() + 8);
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
		frame.setLocation(x, y);
		frame.setVisible(true);
	}

	@Override
	public void run() {
		if (startEntrance) {
			System.out.println("Starting editor entrance.");
			startEntrance = false;

			if (optionParticle.isSelected()) {
				FlounderFramework entrance = new FlounderFramework("Ebon Editors", -1, new EditorRenderer(), new EditorCamera(), new EditorPlayer(), new EditorGuis(), new ExtensionParticles(), new FrameParticles());
				FlounderDisplay.setup(1080, 720, "Ebon Editor Particle", new MyFile[]{new MyFile(MyFile.RES_FOLDER, "flounder.png")}, false, true, 4, false, true);
				TextBuilder.DEFAULT_TYPE = FlounderFonts.FFF_FORWARD;
				frame.setVisible(false);
				entrance.run();
			} else if (optionEntities.isSelected()) {
				FlounderFramework entrance = new FlounderFramework("Ebon Editors", -1, new EditorRenderer(), new EditorCamera(), new EditorPlayer(), new EditorGuis(), new ExtensionEntities(), new FrameEntities());
				FlounderDisplay.setup(1080, 720, "Ebon Editor Entities", new MyFile[]{new MyFile(MyFile.RES_FOLDER, "flounder.png")}, false, true, 4, false, true);
				TextBuilder.DEFAULT_TYPE = FlounderFonts.FFF_FORWARD;
				frame.setVisible(false);
				entrance.run();
			} else {
				System.err.println("No ebon.editor selected!");
			}

			frame.setVisible(true);
			frame.toFront();
		}

		if (!running) {
			System.exit(0);
		}
	}
}
