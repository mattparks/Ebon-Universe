package game.planets;

import flounder.maths.*;
import flounder.maths.vectors.*;
import game.models.*;

import java.util.*;

public class PlanetChunk {
	private static final float SQRT_TWO = (float) Math.sqrt(2);
	private final Model model;
	private final Vector3f position;
	private final Vector3f rotation;
	private final Colour colour;
	private final Vector3f maxPos;
	private final float size;
	private final int vertexCount;
	public PlanetChunk[] children;
	private PlanetChunk parent;
	private int chunkLOD;

	public PlanetChunk(PlanetChunk parent, int chunkLOD, Vector3f position, Vector3f rotation, float size, int vertexCount) {
		this.children = null;
		this.parent = parent;
		this.chunkLOD = chunkLOD;

		Random random = new Random();

		this.position = position;
		this.rotation = rotation;
		this.colour = new Colour(random.nextFloat(), random.nextFloat(), random.nextFloat());
		this.size = size;
		this.vertexCount = vertexCount;
		this.model = generateGrid();
		this.maxPos = new Vector3f();
	}

	private Model generateGrid() {
		final float[] vertices = new float[vertexCount * vertexCount * 3];
		final int[] indices = new int[6 * (vertexCount - 1) * (vertexCount - 1)];

		int vertexPointer = 0;
		int indicesPointer = 0;

		for (int i = 0; i < vertexCount; i++) {
			for (int j = 0; j < vertexCount; j++) {
				vertices[vertexPointer * 3] = j / ((float) vertexCount - 1) * size;
				vertices[vertexPointer * 3 + 1] = 0;
				vertices[vertexPointer * 3 + 2] = i / ((float) vertexCount - 1) * size;
				vertexPointer++;
			}
		}

		for (int gz = 0; gz < vertexCount - 1; gz++) {
			for (int gx = 0; gx < vertexCount - 1; gx++) {
				// TODO: Around edges connect to every other vertices, should fix gaps between engine.terrains.
				int topLeft = gz * vertexCount + gx;
				int topRight = topLeft + 1;
				int bottomLeft = (gz + 1) * vertexCount + gx;
				int bottomRight = bottomLeft + 1;
				indices[indicesPointer++] = topLeft;
				indices[indicesPointer++] = bottomLeft;
				indices[indicesPointer++] = topRight;
				indices[indicesPointer++] = topRight;
				indices[indicesPointer++] = bottomLeft;
				indices[indicesPointer++] = bottomRight;
			}
		}

		return new Model(vertices, null, null, null, indices, 3);
	}

	public void splitToPosition(final float x, final float z) {
		if (this.intersects(x, z)) {
			if (children == null) {
				subdivide();
			}

			if (children != null) {
				for (PlanetChunk child : children) {
					if (child.intersects(x, z)) {
						child.splitToPosition(x, z);
					}
				}
			}
		}
	}

	public void resolveChildrenSplits(final PlanetChunk lastChunk, final PlanetChunk currentChunk) {
		// TODO: Resolve current unnecessary splits in this chunk & its children.

		if (children == null) { // No children splits to resolve!
			return;
		}


	}

	public void subdivide() {
		if (size / vertexCount <= 5 || children != null) { // FIXME: Better min limit!
			return;
		}

		this.children = new PlanetChunk[4];
		children[0] = new PlanetChunk(this, chunkLOD + 1, Vector3f.add(position, new Vector3f(0, 0, 0), null), rotation, size / 2, vertexCount);
		children[1] = new PlanetChunk(this, chunkLOD + 1, Vector3f.add(position, new Vector3f(size / 2, 0, 0), null), rotation, size / 2, vertexCount);
		children[2] = new PlanetChunk(this, chunkLOD + 1, Vector3f.add(position, new Vector3f(0, 0, size / 2), null), rotation, size / 2, vertexCount);
		children[3] = new PlanetChunk(this, chunkLOD + 1, Vector3f.add(position, new Vector3f(size / 2, 0, size / 2), null), rotation, size / 2, vertexCount);
	}

	public void submerge() {
		if (children != null) {
			for (final PlanetChunk child : children) {
				child.submerge();
				child.delete();
			}

			this.children = null;
		}
	}

	public List<PlanetChunk> getChunks() {
		final List<PlanetChunk> chunks = new ArrayList<>();

		if (children == null) {
			chunks.add(this);
		} else {
			for (PlanetChunk child : children) {
				chunks.addAll(child.getChunks());
			}
		}

		return chunks;
	}

	public PlanetChunk getAtPoint(final float x, final float z) {
		if (this.intersects(x, z)) {
			if (children == null) {
				subdivide();
			}

			if (children != null) { // If there are children get them, ignore the parent.
				PlanetChunk childrenChunk = null;

				for (final PlanetChunk child : children) {
					if ((childrenChunk = child.getAtPoint(x, z)) != null) {
						return childrenChunk;
					}
				}
			} else { // If there are no children return this.
				return this;
			}
		}

		return null;
	}

	public PlanetChunk getParentLastRelatance(final PlanetChunk other) {
		List<PlanetChunk> listTB1 = this.toRelationshipList(new ArrayList<>());
		List<PlanetChunk> listTB2 = other.toRelationshipList(new ArrayList<>());
		Collections.reverse(listTB1);
		Collections.reverse(listTB2);

		int motherBigG = 0; // Hopefully values smaller or equal to lod1 & lod2.

		while (motherBigG < Math.min(listTB1.size(), listTB2.size())) { // Gets the last chunk from the mother for this and the other to be most related.
			if (!listTB1.get(motherBigG).equals(listTB2.get(motherBigG))) { // If the chunks are now not the same, it knows its gone to far and the last chunk was the most related.
				motherBigG--; // Set pointer back one to where the parent chunks were the same.
				break;
			}

			motherBigG++; // If they are still related keep on going!
		}

		return this.readLevelUpwards(this.getChunkLOD() - motherBigG);
	}

	public List<PlanetChunk> toRelationshipList(final List<PlanetChunk> list) {
		list.add(this);

		if (parent == null) {
			return list;
		}

		return parent.toRelationshipList(list);
	}

	public PlanetChunk readLevelUpwards(int level) {
		if (level <= 0 || parent == null) {
			return this;
		}

		level -= 1;
		return parent.readLevelUpwards(level);
	}

	public boolean hasEventualParent(final PlanetChunk chunk) {
		if (chunk.equals(this)) {
			return true;
		} else if (parent == null) {
			return false;
		}

		return parent.hasEventualParent(chunk);
	}

	public PlanetChunk getFurthestParent() {
		if (parent == null) {
			return this;
		}

		return parent.getFurthestParent();
	}

	public boolean intersects(final float x, final float z) {
		Vector3f.add(this.position, new Vector3f(this.size, 0, this.size), maxPos); // The max position is the chunks position + its extents.

		if (x > this.position.x && x < maxPos.x) {
			if (z > this.position.z && z < maxPos.z) {
				return true;
			}
		}

		return false;
	}

	public boolean isChild(PlanetChunk chunk) {
		return !(chunk == null || children == null) && (children[0] == chunk || children[1] == chunk || children[2] == chunk || children[3] == chunk);
	}

	public boolean isDivided() {
		return children != null;
	}

	public void delete() {
		model.delete();
	}

	public PlanetChunk getParent() {
		return parent;
	}

	public int getChunkLOD() {
		return chunkLOD;
	}

	public Model getModel() {
		return model;
	}

	public Vector3f getPosition() {
		return position;
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public Colour getColour() {
		return colour;
	}
}
