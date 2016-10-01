package editor;

import editor.entities.*;
import editor.particles.*;
import flounder.engine.*;
import flounder.fonts.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class EbonEditor {
	private static JFrame jFrame;
	private static FlounderEntrance entrance;

	public static void main(String[] args) {
		jFrame = new JFrame("Flounder Editor");
		jFrame.setSize(300, 420);
		jFrame.setResizable(true);

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
			ex.printStackTrace();
		}

		jFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		jFrame.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				jFrame.setVisible(false);
				System.exit(1);
			}
		});

		jFrame.setLayout(new FlowLayout());

		JRadioButton optionParticle = new JRadioButton("Particles");
		JRadioButton optionEntities = new JRadioButton("Entities");
		JButton buttonSubmit = new JButton("Submit");

		buttonSubmit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				start(optionParticle.isSelected(), optionEntities.isSelected());
			}
		});

		ButtonGroup group = new ButtonGroup();
		group.add(optionParticle);
		group.add(optionEntities);

		jFrame.add(optionParticle);
		jFrame.add(optionEntities);
		jFrame.add(buttonSubmit);
		jFrame.pack();

		jFrame.setSize(jFrame.getWidth() + 64, jFrame.getHeight() + 8);
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - jFrame.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - jFrame.getHeight()) / 2);
		jFrame.setLocation(x, y);
		jFrame.setVisible(true);
	}

	public static void start(boolean particles, boolean entities) {
		FlounderEngine.loadEngineStatics("Ebon Editors");

		if (particles) {
			entrance = new EditorParticles(new EditorCamera(), new EditorRenderer(), new EditorGuis());
		} else if (entities) {
			entrance = new EditorEntities(new EditorCamera(), new EditorRenderer(), new EditorGuis());
		} else {
			System.err.println("No editor selected!");
		}

		jFrame.setVisible(false);
		entrance.startEngine(FlounderFonts.FFF_FORWARD);
		jFrame.setVisible(true);
	}
}
