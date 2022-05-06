package glbl.demo

import java.nio.file.Path

public class ImagePair {

	private Path expected = null
	private Path actual = null

	ImagePair(Path expected, Path actual) {
		this.expected = expected;
		this.actual = actual;
	}

	ImagePair(Path lonesome) {
		this.expected = lonesome
	}

	Path getExpected() {
		return this.expected
	}

	Path getActual() {
		return this.actual
	}
}
