package UE8;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class GraphTest1 {

	/**
	 * Fields of <tt>MyEdge</tt> are to be interpreted as:
	 * <tt>in</tt>... to
	 * <tt>out</tt>... from
	 */
	private static final boolean OUT_IS_FROM_VERTEX__IN_IS_TO_VERTEX = true;

	private final Graph graph = new Graph();

	@Test
	void testGetNumberOfVertices() {
		graph.insertVertex(new Vertex("0"));
		graph.insertVertex(new Vertex("1"));
		graph.insertVertex(new Vertex("2"));

		assertEquals(3, graph.getNumberOfVertices());
	}

	@Test
	void testGetNumberOfVerticesBeforeInsert() {
		assertEquals(0, graph.getNumberOfVertices());
	}

	@Test
	void testGetVertices() {
		graph.insertVertex(new Vertex("0"));
		graph.insertVertex(new Vertex("1"));

		assertArrayEquals(new Vertex[]{new Vertex("0"), new Vertex("1")}, graph.getVertices());
	}

	@Test
	void testGetVerticesBeforeInsert() {
		assertArrayEquals(new Vertex[0], graph.getVertices());
	}

	@Test
	void testInsertVertex() {
		assertEquals(0, graph.insertVertex(new Vertex("0")));
		assertEquals(1, graph.insertVertex(new Vertex("1")));
		assertEquals(2, graph.insertVertex(new Vertex("2")));
	}

	@Test
	void testInsertDuplicateVertex() {
		graph.insertVertex(new Vertex("0"));
		assertTrue(graph.insertVertex(new Vertex("0")) < 0);
	}

	@Test
	void testInsertNullVertex() {
		assertThrows(IllegalArgumentException.class, () -> graph.insertVertex(null));
		assertEquals(0, graph.getNumberOfVertices());
	}

	@Test
	void testHasEdge() {
		graph.insertVertex(new Vertex("0"));
		graph.insertVertex(new Vertex("1"));
		graph.insertVertex(new Vertex("2"));

		graph.insertEdge(0, 1, 2);
		assertEquals(2, graph.hasEdge(0, 1));
		assertEquals(-1, graph.hasEdge(1, 0), "Graph is directed");

		graph.hasEdge(0, 2);
		assertEquals(-1, graph.hasEdge(1, 2));
	}

	@Test
	void testHasEdgeInvalidIndex() {
		graph.insertVertex(new Vertex("0"));
		graph.insertVertex(new Vertex("1"));

		assertThrows(IllegalArgumentException.class, () -> graph.insertEdge(1, 2, 0));
	}

	@Test
	void testHasEdgeSelf() {
		graph.insertVertex(new Vertex("0"));
		graph.insertVertex(new Vertex("1"));

		assertThrows(IllegalArgumentException.class, () -> graph.insertEdge(1, 1, 0));
	}

	@Test
	void testInsertEdge() {
		graph.insertVertex(new Vertex("0"));
		graph.insertVertex(new Vertex("1"));
		graph.insertVertex(new Vertex("2"));

		assertTrue(graph.insertEdge(0, 1, 0));
		assertTrue(graph.insertEdge(0, 2, 0));
		assertTrue(graph.insertEdge(2, 1, 0));
		assertTrue(graph.insertEdge(1, 2, 0));
		assertFalse(graph.insertEdge(1, 2, 0));
	}

	@Test
	void testGetAdjacencyMatrix() {
		graph.insertVertex(new Vertex("0"));
		graph.insertVertex(new Vertex("1"));
		graph.insertVertex(new Vertex("2"));
		graph.insertVertex(new Vertex("3"));

		graph.insertEdge(0, 1, 0);
		graph.insertEdge(3, 1, 0);
		graph.insertEdge(3, 2, 0);
		graph.insertEdge(2, 3, 0);

		final int[][] adjacencyMatrix = graph.getAdjacencyMatrix();
		assertEquals(0, adjacencyMatrix[0][0]);
		assertEquals(1, adjacencyMatrix[0][1]);
		assertEquals(0, adjacencyMatrix[0][2]);
		assertEquals(0, adjacencyMatrix[0][3]);

		assertEquals(0, adjacencyMatrix[1][0]);
		assertEquals(0, adjacencyMatrix[1][1]);
		assertEquals(0, adjacencyMatrix[1][2]);
		assertEquals(0, adjacencyMatrix[1][3]);

		assertEquals(0, adjacencyMatrix[2][0]);
		assertEquals(0, adjacencyMatrix[2][1]);
		assertEquals(0, adjacencyMatrix[2][2]);
		assertEquals(1, adjacencyMatrix[2][3]);

		assertEquals(0, adjacencyMatrix[3][0]);
		assertEquals(1, adjacencyMatrix[3][1]);
		assertEquals(1, adjacencyMatrix[3][2]);
		assertEquals(0, adjacencyMatrix[3][3]);
	}

	@Test
	void testGetAdjacencyMatrixEmpty() {
		final int[][] adjacent = graph.getAdjacencyMatrix();
		assertEquals(0, adjacent.length);
	}

	@Test
	void testGetAdjacentVertices() {
		graph.insertVertex(new Vertex("0"));
		graph.insertVertex(new Vertex("1"));
		graph.insertVertex(new Vertex("2"));
		graph.insertVertex(new Vertex("3"));
		graph.insertVertex(new Vertex("4"));
		graph.insertVertex(new Vertex("5"));

		graph.insertEdge(0, 1, 0);
		graph.insertEdge(2, 1, 0);
		graph.insertEdge(4, 5, 0);
		graph.insertEdge(0, 4, 0);
		graph.insertEdge(2, 0, 0);

		assertArrayEquals(new Vertex[]{new Vertex("1"), new Vertex("4")}, graph.getAdjacentVertices(0));
	}

	@Test
	void testGetNumberOfEdges() {
		assertEquals(0, graph.getNumberOfEdges(), "Empty graph has 0 edges");

		graph.insertVertex(new Vertex("0"));
		graph.insertVertex(new Vertex("1"));
		graph.insertVertex(new Vertex("2"));

		graph.insertEdge(0, 1, 0);
		assertEquals(1, graph.getNumberOfEdges());

		graph.insertEdge(0, 2, 0);
		assertEquals(2, graph.getNumberOfEdges());

		graph.insertEdge(2, 1, 0);
		assertEquals(3, graph.getNumberOfEdges());

		graph.insertEdge(1, 2, 0);
		assertEquals(4, graph.getNumberOfEdges());

		graph.insertEdge(1, 2, 0);
		assertEquals(4, graph.getNumberOfEdges());

		assertThrows(IllegalArgumentException.class, () -> graph.insertEdge(1, 1, 0));
		assertEquals(4, graph.getNumberOfEdges());
	}

	@Test
	void testGetEdges() {
		assertNotNull(graph.getEdges(), "Array must not be null");
		assertEquals(0, graph.getEdges().length, "Empty edges must be of size 0");

		graph.insertVertex(new Vertex("0"));
		graph.insertVertex(new Vertex("1"));
		graph.insertVertex(new Vertex("2"));
		graph.insertVertex(new Vertex("3"));
		graph.insertVertex(new Vertex("4"));
		graph.insertVertex(new Vertex("5"));

		graph.insertEdge(0, 1, 2);
		graph.insertEdge(2, 1, 8);
		graph.insertEdge(4, 5, 9);
		graph.insertEdge(0, 4, 3);
		graph.insertEdge(2, 0, 7);

		final MyEdge[] edges = graph.getEdges();
		assertNotNull(edges, "Array must not be null");
		assertEquals(5, edges.length, "Only return Array up to nEdges!");
		assertArrayEquals(new MyEdge[]{
				createEdge(0, 1, 2),
				createEdge(2, 1, 8),
				createEdge(4, 5, 9),
				createEdge(0, 4, 3),
				createEdge(2, 0, 7)
		}, edges, "Edges created incorrectly; have you tried flipping OUT_IS_FROM__IN_IS_TO?");
	}

	private MyEdge createEdge(final int v1, final int v2, final int weight) {
		return OUT_IS_FROM_VERTEX__IN_IS_TO_VERTEX ? new Edge(v1, v2, weight) : new Edge(v2, v1, weight);
	}

	@Test
	void testGetAdjacentVerticesNoneAdjacent() {
		graph.insertVertex(new Vertex("0"));
		graph.insertVertex(new Vertex("1"));
		graph.insertVertex(new Vertex("2"));

		graph.insertEdge(2, 1, 0);

		assertArrayEquals(new Vertex[0], graph.getAdjacentVertices(0));
	}

	@Test
	void testGetAdjacentVerticesEmpty() {
		assertThrows(IllegalArgumentException.class, () -> graph.getAdjacentVertices(0));
	}

	@Test
	void testGetAdjacentVerticesInvalidIndex() {
		graph.insertVertex(new Vertex("0"));
		graph.insertVertex(new Vertex("1"));
		graph.insertVertex(new Vertex("2"));
		graph.insertVertex(new Vertex("3"));
		graph.insertVertex(new Vertex("4"));
		graph.insertVertex(new Vertex("5"));

		assertThrows(IllegalArgumentException.class, () -> graph.getAdjacentVertices(8));
	}

	private static class Edge extends MyEdge {
		private Edge(final int from, final int to, final int weight) {
			out = from;
			in = to;
			this.weight = weight;
		}

		@Override
		public boolean equals(final Object obj) {
			if (!(obj instanceof MyEdge)) return false;

			final MyEdge other = (MyEdge) obj;
			return in == other.in && out == other.out && weight == other.weight;
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

}