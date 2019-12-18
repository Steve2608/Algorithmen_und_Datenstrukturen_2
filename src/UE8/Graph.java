package UE8;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Graph {

	protected MyVertex[] vertices = new MyVertex[1];
	protected Edge[] edges = new Edge[0];

	// pointers for first free Index of Array since there is NO way to remove vertices
	private int nVertices = 0;
	private int nEdges = 0;

	private boolean isCyclic = false;

	// Contains list of components, which in turn contain indices of their vertices
	private List<List<Integer>> components;

	public int getNumberOfVertices() {
		return nVertices;
	}

	public MyVertex[] getVertices() {
		final MyVertex[] result = new MyVertex[nVertices];
		System.arraycopy(vertices, 0, result, 0, nVertices);
		return result;
	}

	public int insertVertex(MyVertex v) throws IllegalArgumentException {
		if (v == null) throw new IllegalArgumentException("Cannot insert null-Element");

		for (int i = 0; i < getNumberOfVertices(); i++) {
			if (vertices[i].equals(v)) return -1;
		}

		addVertex(v);
		return nVertices - 1;
	}

	private void addVertex(MyVertex v) {
		if (nVertices >= vertices.length) {
			final int doubleSize = vertices.length * 2;
			vertices = Arrays.copyOf(vertices, doubleSize);
			edges = Arrays.copyOf(edges, doubleSize * (doubleSize - 1));
		}
		vertices[nVertices++] = v;
	}

	public boolean hasEdge(int v1, int v2) throws IllegalArgumentException {
		checkEdge(v1, v2);
		return hasEdge(new Edge(v1, v2));
	}

	private boolean hasEdge(Edge e) {
		for (int i = 0; i < nEdges; i++) {
			if (e.equals(edges[i])) {
				return true;
			}
		}
		return false;
	}

	public boolean hasUndirectedEdge(int v1, int v2) {
		checkEdge(v1, v2);
		return hasEdge(new UndirectedEdge(v1, v2));
	}

	public boolean insertEdge(int v1, int v2) throws IllegalArgumentException {
		checkEdge(v1, v2);

		final Edge e = new Edge(v1, v2);
		// checking if edge already exists
		for (int i = 0; i < nEdges; i++) {
			if (e.equals(edges[i])) {
				return false;
			}
		}

		edges[nEdges++] = e;
		return true;
	}

	private void checkEdge(int v1, int v2) {
		if (v1 == v2) throw new IllegalArgumentException("Ident vertex indices (v1= " + v1 + " | v2= " + v2 + ")");
		if (v1 < 0 || v1 >= vertices.length) throw new IllegalArgumentException("Invalid Index (v1=" + v1 + ")");
		if (v2 < 0 || v2 >= vertices.length) throw new IllegalArgumentException("Invalid Index (v2=" + v2 + ")");
		if (vertices[v1] == null) throw new IllegalArgumentException("Specified vertex is unknown (v1=" + v1 + ")");
		if (vertices[v2] == null) throw new IllegalArgumentException("Specified vertex is unknown (v2=" + v2 + ")");
	}

	public int[][] getAdjacencyMatrix() {
		final int[][] matrix = new int[getNumberOfVertices()][getNumberOfVertices()];

		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				// Setting to zero is not actually necessary because JVM already does that
				matrix[i][j] = (i != j && hasEdge(i, j)) ? 1 : 0;
			}
		}
		return matrix;
	}

	public MyVertex[] getAdjacentVertices(int v) throws IllegalArgumentException {
		if (v < 0 || v >= vertices.length) throw new IllegalArgumentException("Invalid Index (v=" + v + ")");
		if (vertices[v] == null) throw new IllegalArgumentException("Specified vertex is unknown (v=" + v + ")");

		// creating biggest possible array
		final MyVertex[] adjacent = new MyVertex[nVertices - 1];
		int cnt = 0;
		for (int i = 0; i < nVertices; i++) {
			if (v != i && hasEdge(v, i)) {
				adjacent[cnt++] = vertices[i];
			}
		}
		// Fitting to correct size
		return cnt == adjacent.length ? adjacent : Arrays.copyOf(adjacent, cnt);
	}

	private void DFS() {
		isCyclic = false;

		// marking every field as not visited
		final boolean[] visited = new boolean[nVertices];
		components = new ArrayList<>();

		for (int i = 0, component = 0; i < visited.length; i++) {
			if (!visited[i]) {
				components.add(new ArrayList<>());
				components.get(component).add(i);
				visited[i] = true;
				DFS(visited, i, component++);
			}
		}
	}

	private void DFS(boolean[] visited, int self, int component) {
		for (int i = 0; i < nVertices; i++) {
			if (i != self && hasUndirectedEdge(i, self)) {
				if (visited[i]) {
					isCyclic = true;
				} else {
					visited[i] = true;
					components.get(component).add(i);
					DFS(visited, i, component);
				}
			}
		}
	}

	public boolean isConnected() {
		DFS();
		// graph is only connected if there is ONE undirected component
		return components.size() == 1;
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
			sb.append(component(i));
		}
		return sb.toString();
	}

	private String component(int index) {
		final StringBuilder s = new StringBuilder();
		s.append("Component #").append(index + 1).append(" : ");
		for (int i = 0; i < components.get(index).size(); i++) {
			s.append(components.get(index).get(i)).append(" | ");
		}
		return s.append("\n").toString();
	}

	protected static class Edge {

		public int out, in;

		public Edge(int out, int in) {
			this.out = out;
			this.in = in;
		}

		@Override
		public boolean equals(Object other) {
			if (other == null) return false;
			if (this == other) return true;

			if (other.getClass() != this.getClass()) return false;

			final Edge e = (Edge) other;
			return this.in == e.in && this.out == e.out;
		}

	}

	private static class UndirectedEdge extends Edge {

		public UndirectedEdge(int out, int in) {
			super(out, in);
		}

		@Override
		public boolean equals(Object other) {
			if (other == null) return false;
			if (this == other) return true;

			if (!(other instanceof Edge)) return false;

			final Edge e = (Edge) other;
			return this.in == e.in && this.out == e.out || this.in == e.out && this.out == e.in;
		}
	}

}