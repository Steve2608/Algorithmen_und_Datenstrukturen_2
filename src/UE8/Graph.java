package UE8;

import java.util.Arrays;
import java.util.StringJoiner;
import java.util.function.BiPredicate;

public class Graph {

	protected MyVertex[] vertices = new MyVertex[1];
	protected MyEdge[] edges = new MyEdge[0];

	// pointers for first free Index of Array since there is NO way to remove vertices
	private int nVertices = 0;
	private int nEdges = 0;

	private boolean isCyclic = false;
	private boolean updateDFS = true;
	private int[] components;

	public int getNumberOfVertices() {
		return nVertices;
	}

	public MyVertex[] getVertices() {
		return Arrays.copyOf(vertices, nVertices);
	}

	public int insertVertex(final MyVertex v) {
		if (v == null) throw new IllegalArgumentException("Cannot insert null-Element");

		if (contains(v)) return -1;
		return addVertex(v);
	}

	private int addVertex(final MyVertex v) {
		if (nVertices >= vertices.length) {
			vertices = Arrays.copyOf(vertices, vertices.length * 2);
		}
		vertices[nVertices] = v;
		updateDFS = true;

		return nVertices++;
	}

	public int indexOf(final MyVertex v) {
		if (v == null) throw new IllegalArgumentException("Vertex must not be null");

		for (int i = 0; i < nVertices; i++) {
			if (vertices[i].equals(v)) return i;
		}
		return -1;
	}

	public boolean contains(final MyVertex v) {
		if (v == null) throw new IllegalArgumentException("Vertices must not be null");

		return indexOf(v) >= 0;
	}

	public int hasEdge(final int v1, final int v2) {
		checkEdge(v1, v2);
		return hasEdge(new DirectedEdge(v1, v2, -1));
	}

	private int hasEdge(final MyEdge e) {
		for (int i = 0; i < nEdges; i++) {
			if (e.equals(edges[i])) {
				return edges[i].weight;
			}
		}
		return -1;
	}

	public int hasUndirectedEdge(final int v1, final int v2) {
		checkEdge(v1, v2);
		return hasEdge(new UndirectedEdge(v1, v2, -1));
	}

	public boolean insertEdge(final int v1, final int v2, final int weight) {
		checkEdge(v1, v2);
		if (weight < 0) throw new IllegalArgumentException("Weight has to be positive");

		final DirectedEdge e = new DirectedEdge(v1, v2, weight);
		if (hasEdge(e) >= 0) return false;

		return addEdge(e);
	}

	private boolean addEdge(final MyEdge e) {
		if (nEdges >= edges.length) {
			edges = Arrays.copyOf(edges, nVertices * (nVertices - 1));
		}
		edges[nEdges++] = e;
		updateDFS = true;
		// in this implementation edges should always have room in the array
		return true;
	}

	private void checkEdge(final int v1, final int v2) {
		if (v1 == v2)
			throw new IllegalArgumentException("Ident vertex indices (v1= " + v1 + " | v2= " + v2 + ")");
		checkVertex(v1);
		checkVertex(v2);
	}

	private void checkVertex(final int v) {
		if (v < 0 || v >= nVertices)
			throw new IllegalArgumentException("Invalid Index (v=" + v + ")");
	}

	public int getNumberOfEdges() {
		return nEdges;
	}

	public MyEdge[] getEdges() {
		return Arrays.copyOf(edges, nEdges);
	}

	public int[][] getAdjacencyMatrix() {
		final int[][] matrix = new int[nVertices][nVertices];

		for (int i = 0; i < nVertices; i++) {
			for (int j = 0; j < nVertices; j++) {
				// Setting to zero is not actually necessary because JVM already does that
				matrix[i][j] = i != j && hasEdge(i, j) >= 0 ? 1 : 0;
			}
		}
		return matrix;
	}

	public MyVertex[] getAdjacentVertices(final int v) {
		checkVertex(v);

		final int[] adj = getAdjacentIndices(v, true);
		final MyVertex[] verts = new MyVertex[adj.length];
		for (int i = 0; i < adj.length; i++) {
			verts[i] = vertices[adj[i]];
		}
		return verts;
	}

	private int[] getAdjacentIndices(final int v, final boolean directed) {
		checkVertex(v);

		final Adjacency adjacency;
		if (directed) {
			adjacency = getAdjacentBitVector(v, (a, b) -> hasEdge(a, b) >= 0);
		} else {
			adjacency = getAdjacentBitVector(v, (a, b) -> hasUndirectedEdge(a, b) >= 0);
		}

		final int[] adj = new int[adjacency.count];
		for (int i = 0, count = 0; i < nVertices; i++) {
			if (adjacency.vertices[i]) {
				adj[count++] = i;
			}
		}
		return adj;
	}

	private Adjacency getAdjacentBitVector(final int v, final BiPredicate<Integer, Integer> p) {
		final boolean[] adjacent = new boolean[nVertices];
		int count = 0;
		for (int i = 0; i < nVertices; i++) {
			if (i != v && p.test(v, i)) {
				adjacent[i] = true;
				count++;
			}
		}
		return new Adjacency(adjacent, count);
	}

	private void DFS() {
		if (!updateDFS) return;

		isCyclic = false;

		// marking every field as not visited
		final boolean[] visited = new boolean[nVertices];
		components = new int[nVertices];

		for (int i = 0, component = 1; i < visited.length; i++) {
			if (!visited[i]) {
				components[i] = component;
				visited[i] = true;
				DFS(visited, i, -1, component++);
			}
		}
		updateDFS = false;
	}

	private void DFS(final boolean[] visited, final int child, final int par, final int component) {
		final int[] adjacent = getAdjacentIndices(child, false);

		for (final int adj : adjacent) {
			if (adj != par && hasUndirectedEdge(child, adj) >= 0) {
				if (components[adj] == component) {
					isCyclic = true;
				} else {
					visited[adj] = true;
					components[adj] = component;
					DFS(visited, adj, child, component);
				}
			}
		}
	}

	public boolean isConnected() {
		// graph is only connected if there is ONE undirected component
		return getNumberOfComponents() == 1;
	}

	public int getNumberOfComponents() {
		DFS();

		int iComp = 0;
		for (final int component : components) {
			if (component > iComp) iComp = component;
		}
		return iComp;
	}

	public void printComponents() {
		System.out.println(toString());
	}

	public boolean isCyclic() {
		DFS();
		return isCyclic;
	}

	@Override
	public String toString() {
		final int nComp = getNumberOfComponents();

		final StringJoiner[] sbs = new StringJoiner[nComp];
		for (int i = 0; i < nComp; i++) {
			sbs[i] = new StringJoiner(", ", String.format("Component #%d: [", i + 1), "]");
			for (int j = 0; j < components.length; j++) {
				if (components[j] == i + 1) {
					sbs[i].add(String.valueOf(j));
				}
			}
		}

		final StringJoiner sj = new StringJoiner("\n", "Graph {\n", "\n}");
		for (final StringJoiner sb : sbs) {
			sj.add("\t" + sb.toString());
		}
		return sj.toString();
	}

	private static class Adjacency {
		private final boolean[] vertices;
		private final int count;

		private Adjacency(final boolean[] vertices, final int count) {
			this.vertices = vertices;
			this.count = count;
		}
	}

	protected static class DirectedEdge extends MyEdge {

		protected DirectedEdge(final int out, final int in, final int weight) {
			this.out = out;
			this.in = in;
			this.weight = weight;
		}

		@Override
		public boolean equals(final Object other) {
			if (other == null) return false;
			if (this == other) return true;

			if (other.getClass() != getClass()) return false;

			final DirectedEdge e = (DirectedEdge) other;
			return in == e.in && out == e.out;
		}

		@Override
		public int hashCode() {
			int result = out;
			result = 31 * result + in;
			result = 31 * result + weight;
			return result;
		}

		@Override
		public String toString() {
			return String.format("%d--(%d)->%d", out, weight, in);
		}
	}

	protected static class UndirectedEdge extends DirectedEdge {

		protected UndirectedEdge(final int out, final int in, final int weight) {
			super(out, in, weight);
		}

		@Override
		public boolean equals(final Object other) {
			if (other == null) return false;
			if (this == other) return true;

			if (!(other instanceof DirectedEdge)) return false;

			final DirectedEdge e = (DirectedEdge) other;
			return in == e.in && out == e.out || in == e.out && out == e.in;
		}

		// always implement equals and hashCode together
		@Override
		public int hashCode() {
			return super.hashCode();
		}

		@Override
		public String toString() {
			return String.format("%d--(%d)--%d", out, weight, in);
		}
	}

}