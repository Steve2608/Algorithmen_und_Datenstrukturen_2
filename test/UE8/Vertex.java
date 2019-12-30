package UE8;

import java.util.Objects;

class Vertex implements MyVertex {
	private final String name;

	Vertex(final String name) {
		this.name = name;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		final Vertex vertex = (Vertex) o;
		return Objects.equals(name, vertex.name);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(name);
	}

	@Override
	public String toString() {
		return String.format("Vertex{'%s'}", name);
	}
}