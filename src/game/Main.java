package game;

import flounder.engine.*;
import org.lwjgl.glfw.*;

/**
 * The games main entry point.
 */
public class Main {
	//private static JFrame frame;
	//private static Canvas gameCanvas;

	public static void main(String[] args) {
		/*frame = new JFrame("Flounder 2.0");
		frame.setSize(1080, 720);
		frame.setResizable(true);
		frame.setIgnoreRepaint(true);
		frame.setLayout(new BorderLayout());

		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				ManagerDevices.getDisplay().requestClose();
			}
		});

		gameCanvas = new Canvas();
		gameCanvas.setIgnoreRepaint(true);
		gameCanvas.setSize(frame.getSize());
		frame.add(gameCanvas, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);

		gameCanvas.createBufferStrategy(2);*/
		/*BufferStrategy buffer = gameCanvas.getBufferStrategy();

		Graphics graphics = null;

		while (running) {
			graphics = buffer.getDrawGraphics();
			graphics.setColor( Color.BLACK );
			graphics.fillRect( 0, 0, 639, 479 );

			if(!buffer.contentsLost()) {
				buffer.show();
			}

			Thread.yield();
		}*/

		FlounderEngine.preinit(null, 1080, 720, "Flounder 2.0", 144, false, false, 0, false); // gameCanvas
		FlounderEngine.init(new IModule(new MainGame(), new MainCamera(), new MainMasterRenderer()));
		FlounderEngine.run();
		FlounderEngine.dispose();
		// frame.dispose();
	}
}
