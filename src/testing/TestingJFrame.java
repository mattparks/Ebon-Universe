package testing;

import flounder.devices.*;
import flounder.framework.*;
import flounder.standard.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TestingJFrame extends IExtension implements IStandard {
	private JFrame frame;
	private JPanel renderPanel;

	public TestingJFrame() {
		super(FlounderStandard.class, FlounderDisplayJPanel.class);
	}

	@Override
	public void init() {
		frame = new JFrame();
		frame.setTitle("Testing");
		frame.setSize(FlounderDisplay.getWidth(), FlounderDisplay.getHeight());
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.setResizable(true);

		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent windowEvent) {
				if (JOptionPane.showConfirmDialog(frame,
						"Are you sure to close this window?", "Any unsaved work will be lost!",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
					FlounderFramework.requestClose();
				} else {
					frame.setVisible(true);
				}
			}
		});

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
			ex.printStackTrace();
		}

		renderPanel = FlounderDisplayJPanel.createPanel();
		frame.add(renderPanel);
		frame.setLocationByPlatform(true);
		frame.setVisible(true);
		frame.toFront();
	}

	@Override
	public void update() {
	}

	@Override
	public void dispose() {
		frame.setVisible(false);
		frame.dispose();
	}

	@Override
	public boolean isActive() {
		return true;
	}
}
