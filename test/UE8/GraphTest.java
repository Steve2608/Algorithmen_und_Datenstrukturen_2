package UE8;

import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;


public class GraphTest {

	private final Graph graph = new Graph();

	@Test
	public void getNumberOfVertices() {
		graph.insertVertex(new Vertex("0"));
		graph.insertVertex(new Vertex("1"));
		graph.insertVertex(new Vertex("2"));

		assertEquals(3, graph.getNumberOfVertices());
	}

	@Test
	public void getNumberOfVerticesBeforeInsert() {
		assertEquals(0, graph.getNumberOfVertices());
	}

	@Test
	public void getVertices() {
		graph.insertVertex(new Vertex("0"));
		graph.insertVertex(new Vertex("1"));

		assertArrayEquals(new Vertex[]{new Vertex("0"), new Vertex("1")}, graph.getVertices());
	}

	@Test
	public void getVerticesBeforeInsert() {
		assertArrayEquals(new Vertex[0], graph.getVertices());
	}

	@Test
	public void insertVertex() {
		assertEquals(0, graph.insertVertex(new Vertex("0")));
		assertEquals(1, graph.insertVertex(new Vertex("1")));
		assertEquals(2, graph.insertVertex(new Vertex("2")));
	}

	@Test
	public void insertDuplicateVertex() {
		graph.insertVertex(new Vertex("0"));
		assertTrue(graph.insertVertex(new Vertex("0")) < 0);
	}

	@Test
	public void insertNullVertex() {
		assertThrows(IllegalArgumentException.class, () -> graph.insertVertex(null));
		assertEquals(0, graph.getNumberOfVertices());
	}

	@Test
	public void hasEdge() {
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
	public void hasEdgeInvalidIndex() {
		graph.insertVertex(new Vertex("0"));
		graph.insertVertex(new Vertex("1"));

		assertThrows(IllegalArgumentException.class, () -> graph.insertEdge(1, 2, 0));
	}

	@Test
	public void hasEdgeSelf() {
		graph.insertVertex(new Vertex("0"));
		graph.insertVertex(new Vertex("1"));

		assertThrows(IllegalArgumentException.class, () -> graph.insertEdge(1, 1, 0));
	}

	@Test
	public void insertEdge() {
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
	public void getAdjacencyMatrix() {
		graph.insertVertex(new Vertex("0"));
		graph.insertVertex(new Vertex("1"));
		graph.insertVertex(new Vertex("2"));
		graph.insertVertex(new Vertex("3"));

		graph.insertEdge(0, 1, 0);
		graph.insertEdge(3, 1, 0);
		graph.insertEdge(3, 2, 0);
		graph.insertEdge(2, 3, 0);

		int[][] adjacencyMatrix = graph.getAdjacencyMatrix();
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
	public void getAdjacencyMatrixEmpty() {
		int[][] adjacent = graph.getAdjacencyMatrix();
		assertEquals(0, adjacent.length);
	}

	@Test
	public void getAdjacentVertices() {
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
	public void getAdjacentVerticesNoneAdjacent() {
		graph.insertVertex(new Vertex("0"));
		graph.insertVertex(new Vertex("1"));
		graph.insertVertex(new Vertex("2"));

		graph.insertEdge(2, 1, 0);

		assertArrayEquals(new Vertex[0], graph.getAdjacentVertices(0));
	}

	@Test
	public void getAdjacentVerticesEmpty() {
		assertThrows(IllegalArgumentException.class, () -> graph.getAdjacentVertices(0));
	}

	@Test
	public void getAdjacentVerticesInvalidIndex() {
		graph.insertVertex(new Vertex("0"));
		graph.insertVertex(new Vertex("1"));
		graph.insertVertex(new Vertex("2"));
		graph.insertVertex(new Vertex("3"));
		graph.insertVertex(new Vertex("4"));
		graph.insertVertex(new Vertex("5"));

		assertThrows(IllegalArgumentException.class, () -> graph.getAdjacentVertices(8));
	}

	@Test
	public void isConnected() {
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
		graph.insertEdge(0, 2, 0);
		graph.insertEdge(3, 2, 0);
		graph.insertEdge(1, 5, 0);

		assertTrue(graph.isConnected());
	}

	@Test
	public void isNotConnected() {
		graph.insertVertex(new Vertex("0"));
		graph.insertVertex(new Vertex("1"));

		graph.insertEdge(0, 1, 0);
		assertTrue(graph.isConnected());

		graph.insertVertex(new Vertex("2"));
		assertFalse(graph.isConnected());
	}

	@Test
	public void isConnectedEmpty() {
		assertFalse(graph.isConnected());
	}

	@Test
	public void isConnectedOneVertex() {
		graph.insertVertex(new Vertex("0"));
		assertTrue(graph.isConnected());
	}

	@Test
	public void getNumberOfComponents() {
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

		assertEquals(2, graph.getNumberOfComponents());

		graph.insertVertex(new Vertex("6"));
		assertEquals(3, graph.getNumberOfComponents());

		graph.insertEdge(6, 4, 0);
		assertEquals(2, graph.getNumberOfComponents());

		graph.insertEdge(3, 6, 0);
		assertEquals(1, graph.getNumberOfComponents());
	}

	@Test
	public void getNumberOfComponentsEmpty() {
		assertEquals(0, graph.getNumberOfComponents());
	}

	@Test
	public void isCyclic() {
		graph.insertVertex(new Vertex("0"));
		graph.insertVertex(new Vertex("1"));
		graph.insertVertex(new Vertex("2"));
		graph.insertVertex(new Vertex("3"));

		graph.insertEdge(0, 1, 0);
		graph.insertEdge(1, 2, 0);
		graph.insertEdge(2, 3, 0);
		graph.insertEdge(3, 0, 0);

		assertTrue(graph.isCyclic());

		graph.insertVertex(new Vertex("4"));

		graph.insertVertex(new Vertex("5"));
		graph.insertEdge(3, 4, 0);
		graph.insertEdge(4, 5, 0);
		graph.insertEdge(5, 0, 0);
		assertTrue(graph.isCyclic());
	}

	@Test
	public void isCyclicEmpty() {
		assertFalse(graph.isCyclic());
	}

	@Test
	public void isCyclicOneVertex() {
		graph.insertVertex(new Vertex("0"));
		assertFalse(graph.isCyclic());
	}

	private static class Vertex implements MyVertex {
		private final String name;

		public Vertex(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return String.format("Vertex{'%s'}", name);
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			Vertex vertex = (Vertex) o;
			return Objects.equals(name, vertex.name);
		}

		@Override
		public int hashCode() {
			return Objects.hash(name);
		}
	}

}