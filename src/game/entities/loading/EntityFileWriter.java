package game.entities.loading;

import flounder.maths.vectors.*;

import java.io.*;
import java.util.*;

public class EntityFileWriter {
	public static final int MAX_LINE_LENGTH = 5000;

	private FileWriter fileWriter;
	private int fileNestation;
	private int lineLength;

	public EntityFileWriter(FileWriter fileWriter) {
		this.fileWriter = fileWriter;
		this.fileNestation = 0;
		this.lineLength = 0;
	}

	public List<Vector3f> getVec3List(float[] input) {
		List<Vector3f> list = new ArrayList<>();

		int tripleCount = 0;
		Vector3f c = new Vector3f();

		for (float vertex : input) {
			if (tripleCount == 0) {
				c.x = vertex;
			} else if (tripleCount == 1) {
				c.y = vertex;
			} else if (tripleCount == 2) {
				c.z = vertex;
			}

			if (tripleCount >= 2) {
				tripleCount = 0;
				list.add(c);
				c = new Vector3f();
			} else {
				tripleCount++;
			}
		}

		return list;
	}

	public List<Vector2f> getVec2List(float[] input) {
		List<Vector2f> list = new ArrayList<>();

		int doubleCount = 0;
		Vector2f c = new Vector2f();

		for (float vertex : input) {
			if (doubleCount == 0) {
				c.x = vertex;
			} else if (doubleCount == 1) {
				c.y = vertex;
			}

			if (doubleCount >= 1) {
				doubleCount = 0;
				list.add(c);
				c = new Vector2f();
			} else {
				doubleCount++;
			}
		}

		return list;
	}


	/**
	 * Turns a Vector 4 into a printable string.
	 *
	 * @param v The vector to read from.
	 *
	 * @return The printable string.
	 */
	public String vec4String(Vector4f v) {
		return "[" + v.getX() + "/" + v.getY() + "/" + v.getZ() + "/" + v.getW() + "]";
	}

	/**
	 * Turns a Vector 3 into a printable string.
	 *
	 * @param v The vector to read from.
	 *
	 * @return The printable string.
	 */
	public String vec3String(Vector3f v) {
		return "[" + v.getX() + "/" + v.getY() + "/" + v.getZ() + "]";
	}

	/**
	 * Turns a Vector 2 into a printable string.
	 *
	 * @param v The vector to read from.
	 *
	 * @return The printable string.
	 */
	public String vec2String(Vector2f v) {
		return "[" + v.getX() + "/" + v.getY() + "]";
	}

	public void beginNewSegment(String name) throws IOException {
		name = getIndentations() + name + " {";
		fileWriter.write(name);
		enterBlankLine();
		lineLength = 0;
		fileNestation++;
	}

	public void endSegment(boolean enterTightSpace) throws IOException {
		enterBlankLine();
		fileNestation--;
		fileWriter.write(getIndentations() + "};");

		if (!enterTightSpace) {
			enterBlankLine();
			enterBlankLine();
		}
	}

	public String getIndentations() {
		String data = "";

		for (int i = 0; i < fileNestation; i++) {
			data += "	";
		}

		return data;
	}

	public void writeSegmentData(String data, boolean breakAfter) throws IOException {
		writeSegmentData(data);

		if (breakAfter) {
			lineLength = MAX_LINE_LENGTH;
		}
	}

	public void writeSegmentData(String... data) throws IOException {
		if (lineLength >= MAX_LINE_LENGTH) {
			lineLength = 0;
			enterBlankLine();
		}

		if (lineLength == 0) {
			fileWriter.write(getIndentations());
			lineLength = getIndentations().length();
		}

		for (String s : data) {
			lineLength += s.length();
			fileWriter.write(s);
		}
	}

	public void addComment(String... lines) throws IOException {
		fileWriter.write(getIndentations() + "/**");
		enterBlankLine();

		for (String line : lines) {
			fileWriter.write(getIndentations() + " * " + line);
			enterBlankLine();
		}

		fileWriter.write(getIndentations() + " */");
		enterBlankLine();
	}

	public void startFileLine(String data) throws IOException {
		fileWriter.write(data);
	}

	public void writeSingleLine(String data) throws IOException {
		enterBlankLine();
		fileWriter.write(data);
	}

	public void enterBlankLine() throws IOException {
		fileWriter.write("\n");
	}
}
