package UE8;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

	// Contains list of components, which in turn contain indices of their vertices
	private List<List<Integer>> components;

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
		if (v == null) throw new IllegalArgumentException("Vertices must not be null");

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
		if (v1 < 0 || v1 >= nVertices)
			throw new IllegalArgumentException("Invalid Index (v1=" + v1 + ")");
		if (v2 < 0 || v2 >= nVertices)
			throw new IllegalArgumentException("Invalid Index (v2=" + v2 + ")");
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
		if (v < 0 || v >= vertices.length)
			throw new IllegalArgumentException("Invalid Index (v=" + v + ")");
		if (vertices[v] == null)
			throw new IllegalArgumentException("Specified vertex is unknown (v=" + v + ")");

		final Adjacency adjacency = getAdjacentBitVector(v, (a, b) -> hasEdge(a, b) >= 0);
		final MyVertex[] adj = new MyVertex[adjacency.count];

		for (int i = 0, count = 0; i < nVertices; i++) {
			if (adjacency.vertices[i]) {
				adj[count++] = vertices[i];
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
		components = new ArrayList<>();

		for (int i = 0, component = 0; i < visited.length; i++) {
			if (!visited[i]) {
				components.add(new ArrayList<>());
				components.get(component).add(i);
				visited[i] = true;
				DFS(visited, i, -1, component++);
			}
		}
		updateDFS = false;
	}

	private void DFS(final boolean[] visited, final int child, final int par, final int component) {
		final boolean[] adjacent = getAdjacentBitVector(child, (a, b) -> hasUndirectedEdge(a, b) >= 0).vertices;

		for (int i = 0; i < nVertices; i++) {
			if (adjacent[i] && i != child && i != par && hasUndirectedEdge(child, i) >= 0) {
				if (components.get(component).contains(i)) {
					isCyclic = true;
				} else {
					visited[i] = true;
					components.get(component).add(i);
					DFS(visited, i, child, component);
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
		return components.size();
	}

	public void printComponents() {
		System.out.println(this);
	}

	public boolean isCyclic() {
		DFS();
		return isCyclic;
	}

	@Override
	public String toString() {
		DFS();
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < components.size(); i++) {
			sb.append(component(i)).append('\n');
		}
		return sb.toString();
	}

	private String component(final int index) {
		final StringJoiner sj = new StringJoiner(", ", "{", "}");

		final List<Integer> component = components.get(index);
		for (final Integer vertex : component) {
			sj.add(String.valueOf(vertex));
		}

		return String.format("Component #%d: ", index + 1) + sj.toString();
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