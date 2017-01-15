package launcher;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;
import java.util.List;

public class EbonLauncher extends JFrame {
	public static void main(String[] args) {
		EbonLauncher launcher = new EbonLauncher();
	}

	private String saveDir = "launcher";
	private String saveJar = "Ebon-Universe.jar";

	private String version = "0.0";
	private boolean needDownload = true;

	private JProgressBar progressBar;
	private JProgressBar oprogressBar;
	private int i = 0;
	private List<Thread> threads = new ArrayList<>();
	private long lastTime = 0;

	public EbonLauncher() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) screenSize.getWidth();
		int height = (int) screenSize.getHeight();
		setTitle("Downloading Ebon Universe...");
		setBounds(width / 2 - 200, height / 2 - 50, 400, 100);
		setVisible(true);

		this.progressBar = new JProgressBar(0);
		progressBar.setBounds(0, 0, 400, 50);
		progressBar.setValue(0);

		this.oprogressBar = new JProgressBar(0);
		oprogressBar.setBounds(0, 50, 400, 50);
		oprogressBar.setValue(0);

		add(progressBar);
		add(oprogressBar);
		needDownload();

		if (needDownload) {
			deleteFolder(new File(System.getProperty("user.dir") + java.io.File.separator + saveDir));

			{
				File f = new File(saveDir + "/");

				if (!f.exists()) {
					if (!f.mkdir()) {
						System.out.println("Unable to create dir!!");
						System.exit(1);
					} else {
						// System.out.println("Created dir!");
					}
				} else {
					// System.out.println("Dir already exists");
				}
			}

			{
				File f = new File(saveDir + "/natives-win");

				if (!f.exists()) {
					if (!f.mkdir()) {
						System.out.println("Unable to create dir!!");
						System.exit(1);
					} else {
						//		System.out.println("Created dir!");
					}
				} else {
					//	System.out.println("Dir already exists");
				}
			}

			progressBar.setValue(10);

			try {
				{
					URL url = new URL("http://www.endcraft.net/webstart/ver");
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("HEAD");
					File fi = new File(saveDir + "/ver.txt");

					if (!fi.exists() || fi.length() != conn.getContentLength()) {
						download("http://www.endcraft.net/webstart/ver", saveDir + "/ver.txt", conn.getContentLength());
					}
				}

				{
					URL url = new URL("http://www.endcraft.net/webstart/res/download.txt");
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("HEAD");
					File fi = new File(saveDir + "/download.txt");

					if (!fi.exists() || fi.length() != conn.getContentLength()) {
						download("http://www.endcraft.net/webstart/res/download.txt", saveDir + "/download.txt", conn.getContentLength());
					}
				}

				progressBar.setValue(15);
				List<String> download = readTextFile(saveDir + "/download.txt");

				for (final String str : download) {
					URL url = new URL("http://www.endcraft.net/webstart/res/" + str);
					final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					progressBar.setValue(30);
					conn.setRequestMethod("HEAD");
					File fi = new File(saveDir + "/" + str);

					if (!fi.getParentFile().exists()) {
						fi.getParentFile().mkdirs();
					}

					if (!fi.exists() || fi.length() != conn.getContentLength()) {
						progressBar.setValue(50);

						Thread t = new Thread() {
							@Override
							public void run() {
								download("http://www.endcraft.net/webstart/res/" + str, saveDir + "/" + str, conn.getContentLength());
							}
						};

						t.start();
						threads.add(t);
					} else {
						System.out.println("No need to download resource!");
						progressBar.setValue(100);
					}
				}

				i++;
				oprogressBar.setValue((int) ((i * 100.0f) / download.size() - threads.size()));
			} catch (Exception e) {
				e.printStackTrace();
			}

			progressBar.setValue(50);
			File f = new File(saveDir + "/" + saveJar);

			try {
				URL url = new URL("http://www.endcraft.net/webstart/privateers.jar");
				final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("HEAD");

				if (needDownload || !f.exists() || f.length() != conn.getContentLength()) {
					Thread t = new Thread() {
						@Override
						public void run() {
							download("http://www.endcraft.net/webstart/privateers.jar", saveDir + "/" + saveJar, conn.getContentLength());
						}
					};

					t.start();
					threads.add(t);

				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			for (Thread t : threads) {
				try {
					t.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		progressBar.setValue(95);

		try {
			File dir = new File("settings");

			if (!dir.exists()) {
				dir.mkdirs();
			}

			progressBar.setValue(100);
			String exec = (System.getProperty("user.dir") + java.io.File.separator + saveDir + "/" + saveJar);
			ProcessBuilder pb = new ProcessBuilder("java", "-jar", exec);
			System.out.println(pb.command());
			System.out.println("Running " + exec);
			pb.start();
			System.exit(0);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Checks with the online versions to see if there are updates.
	 */
	private void needDownload() {
		File fileVersion = new File(System.getProperty("user.dir") + "/" + saveDir + "/ver.txt");

		try {
			List<String> lines = Files.readAllLines(Paths.get(fileVersion.getAbsolutePath()), StandardCharsets.UTF_8);

			for (String str : lines) {
				version = str;
				System.out.println("Current Version: " + str + ".");
			}
		} catch (IOException e) {
			//	e.printStackTrace();
		}

		progressBar.setValue(25);

		{
			try {
				URL urlVersion = new URL("http://www.endcraft.net/webstart/ver");

				progressBar.setValue(50);

				URLConnection urlConnection = urlVersion.openConnection();
				BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				String line;

				while ((line = reader.readLine()) != null) {
					System.out.println("Latest Version: " + line + ".");

					// Checks online version VS local.
					if (!line.contains(version)) {
						if (fileVersion.exists()) {
							fileVersion.delete();
						}

						fileVersion.createNewFile();

						PrintWriter writer = new PrintWriter(fileVersion.getAbsoluteFile(), "UTF-8");
						writer.println(line);
						writer.close();
						System.out.println("Updated to version " + line + " from version " + version + ".");

						needDownload = true;
					} else { // Checks if res or jars are missing.
						File fileJar = new File(System.getProperty("user.dir") + "/" + saveDir + "/" + saveJar);
						File fileDownload = new File(System.getProperty("user.dir") + "/" + saveDir + "/download.txt");

						needDownload = false;

						// If jar and downloads exist, there may be a version downloaded.
						if (fileJar.exists() && fileDownload.exists()) {
							List<String> filesDownloaded = readTextFile(fileDownload.getAbsolutePath());

							for (String s : filesDownloaded) {
								// If the file does not exist, everything will be re-downloaded.
								if (!new File(System.getProperty("user.dir") + "/" + saveDir + "/" + s).exists()) {
									needDownload = true;
									break;
								}
							}
						} else { // There may be no jar or full res folder.
							needDownload = true;
						}
					}
				}

				if (needDownload) {
					System.out.println("Downloading new version!");
				} else {
					System.out.println("No downloading is needed!");
				}

				progressBar.setValue(100);
			} catch (Exception e) {
				// e.printStackTrace();
			}
		}
	}

	/**
	 * Downloads a file from a source to a destination.
	 *
	 * @param source The source URL to download from.
	 * @param destination The file to download into.
	 * @param size The content-length from the URLs header field.
	 */
	private void download(String source, String destination, int size) {
		// Ten percent of the total download size.
		File ofile = new File(System.getProperty("user.dir"), destination);
		System.out.println("Downloading " + source + " To: " + destination);

		try {
			// Deletes old files.
			if (ofile.exists()) {
				ofile.delete();
			}

			// If there was an error creating the path, throw a exception.
			if (!ofile.createNewFile()) {
				throw new IOException("Can't create " + ofile.getAbsolutePath());
			}

			int inChar = 0;
			URL url = new URL(source);
			InputStream input = url.openStream();
			FileOutputStream fos = new FileOutputStream(ofile);

			// Reads all data chars.
			for (int i = 0; i < size && inChar != -1; i++) {
				if (System.nanoTime() > lastTime + 2000000000) {
					lastTime = System.nanoTime();
					int percentage = (int) ((i * 100.0f) / size);
					progressBar.setValue(((int) ((percentage * 100.0f) / 100)));
					setTitle(ofile.getName() + ": " + progressBar.getValue() + "%" + " Total: " + oprogressBar.getValue() + "%");
				}

				inChar = input.read();
				fos.write(inChar);
			}

			i++;
			oprogressBar.setValue((int) ((i * 100.0f) / threads.size()));
			input.close();
			fos.close();
		} catch (IOException e) {
			// e.printStackTrace();
		}
	}

	/**
	 * Reads a file into a array of lines.
	 *
	 * @param fileName The file to read.
	 *
	 * @return The read values.
	 */
	private static List<String> readTextFile(String fileName) {
		ArrayList<String> values = new ArrayList<>();
		FileReader file = null;

		try {
			file = new FileReader(fileName);
			BufferedReader reader = new BufferedReader(file);
			String line;

			while ((line = reader.readLine()) != null) {
				values.add(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			if (file != null) {
				try {
					file.close();
				} catch (IOException e) {
					// Ignore issues during closing
					// e.printStackTrace();
				}
			}
		}

		return values;
	}

	/**
	 * Deletes a folder and its contents.
	 *
	 * @param folder The folder to delete.
	 */
	private void deleteFolder(File folder) {
		File[] files = folder.listFiles();

		if (files != null) { //Some JVMs return null for empty dirs
			for (File f : files) {
				if (f.isDirectory()) {
					deleteFolder(f);
				} else {
					f.delete();
				}
			}
		}

		folder.delete();
	}
}