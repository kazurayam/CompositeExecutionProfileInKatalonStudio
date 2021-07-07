package com.kazurayam.ks.globalvariable.demo

import java.awt.image.RenderedImage;
import java.nio.file.Path;

import javax.imageio.ImageIO

import org.apache.commons.codec.digest.DigestUtils

import groovy.xml.MarkupBuilder


class Reporter {

	private Path imgDir
	private List<ImagePair> imagePairs
	private String titleLeft = "left"
	private String titleRight = "right"

	Reporter(Path imgDir) {
		this.imgDir = imgDir
		imagePairs = new ArrayList<ImagePair>()
	}

	void setTitleLeft(String titleLeft) {
		this.titleLeft = titleLeft
	}

	void setTitleRight(String titleRight) {
		this.titleRight = titleRight
	}

	void add(ImagePair imagePair) {
		imagePairs.add(imagePair)
	}

	void add(Path lonesome) {
		imagePairs.add(new ImagePair(lonesome))
	}

	void report(Path html) {
		def writer = new StringWriter()
		def markup = new MarkupBuilder(writer)
		markup.html {
			body {
				imagePairs.eachWithIndex { imagePair, index ->
					h1("#${index + 1}")
					table {
						thead {
							tr {
								if (imagePair.getExpected() != null) {
									th(titleLeft)
								}
								if (imagePair.getActual() != null) {
									th(titleRight)
								}
							}
						}
						tbody {
							tr {
								td {
									if (imagePair.getExpected() != null) {
										img(src: imgDir.relativize(imagePair.getExpected()).toString(),
										alt: titleLeft, width:"500px")
									}
								}
								td {
									if (imagePair.getActual() != null) {
										img(src: imgDir.relativize(imagePair.getActual()).toString(),
										alt: titleRight, width:"500px")
									}
								}
							}
						}
					}
					hr()
				}
			}
		}
		html.toFile().text = writer.toString()
	}
}