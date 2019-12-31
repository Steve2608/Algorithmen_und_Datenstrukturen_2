package UE8;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class GraphTest2 {

	private final Graph graph = new Graph();

	@Test
	void testIsConnected() {
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

		System.out.println(Arrays.toString(graph.getEdges()));

		assertTrue(graph.isConnected());
	}

	@Test
	void testIsNotConnected() {
		graph.insertVertex(new Vertex("0"));
		graph.insertVertex(new Vertex("1"));

		graph.insertEdge(0, 1, 0);
		assertTrue(graph.isConnected());

		graph.insertVertex(new Vertex("2"));
		assertFalse(graph.isConnected());
	}

	@Test
	void testIsConnectedEmpty() {
		assertFalse(graph.isConnected());
	}

	@Test
	void testIsConnectedOneVertex() {
		graph.insertVertex(new Vertex("0"));
		assertTrue(graph.isConnected());
	}

	@Test
	void testGetNumberOfComponents() {
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
		assertDoesNotThrow(graph::printComponents);

		graph.insertVertex(new Vertex("6"));
		assertEquals(3, graph.getNumberOfComponents());
		assertDoesNotThrow(graph::printComponents);

		graph.insertEdge(6, 4, 0);
		assertEquals(2, graph.getNumberOfComponents());
		assertDoesNotThrow(graph::printComponents);

		graph.insertEdge(3, 6, 0);
		assertEquals(1, graph.getNumberOfComponents());
		assertDoesNotThrow(graph::printComponents);
	}

	@Test
	void testGetNumberOfComponentsEmpty() {
		assertEquals(0, graph.getNumberOfComponents());
	}

	@Test
	void testIsCyclic() {
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
	void testIsCyclicEmpty() {
		assertFalse(graph.isCyclic());
	}

	@Test
	void testIsCyclicOneVertex() {
		graph.insertVertex(new Vertex("0"));
		assertFalse(graph.isCyclic());
	}

	@Test
	void testIsCyclicTwoVertices() {
		graph.insertVertex(new Vertex("0"));
		graph.insertVertex(new Vertex("1"));

		graph.insertEdge(0, 1, 42);

		assertFalse(graph.isCyclic());
	}

	@Test
	void testIsConnectedStraightLine() {
		graph.insertVertex(new Vertex("0"));
		graph.insertVertex(new Vertex("1"));
		graph.insertVertex(new Vertex("2"));
		graph.insertVertex(new Vertex("3"));
		graph.insertVertex(new Vertex("4"));

		graph.insertEdge(0, 1, 0);
		graph.insertEdge(1, 2, 0);
		graph.insertEdge(2, 3, 0);
		graph.insertEdge(3, 4, 0);

		assertFalse(graph.isCyclic());
	}
}
