package com.kazurayam.ks.globalvariable.demo

import java.nio.file.Path

public class ImagePair {

	private Path expected;
	private Path actual;

	ImagePair(Path expected, Path actual) {
		this.expected = expected;
		this.actual = actual;
	}

	Path getExpected() {
		return this.expected
	}

	Path getActual() {
		return this.actual
	}
}
