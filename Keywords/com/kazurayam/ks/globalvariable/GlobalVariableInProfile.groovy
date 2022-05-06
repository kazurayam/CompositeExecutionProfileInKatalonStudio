package com.kazurayam.ks.globalvariable

import java.nio.file.Path

import com.kazurayam.ks.globalvariable.xml.GlobalVariableEntity



/**
 * Pair of a GlobalVariable and its containing Profile
 *
 * @author kazurayam
 *
 */
public final class GlobalVariableInProfile implements Comparable<GlobalVariableInProfile> {

	GlobalVariableEntity gve
	Path profile

	GlobalVariableInProfile(GlobalVariableEntity gve, Path profile) {
		this.gve = gve
		this.profile = profile
	}

	GlobalVariableEntity getGlobalVariableEntity() {
		return this.gve
	}

	Path getProfile() {
		return this.profile
	}

	String getProfileName() {
		String fileName = this.profile.getFileName().toString()
		return fileName.substring(0, fileName.indexOf(".glbl"))
	}

	@Override
	boolean equals(Object obj) {
		if (! (obj instanceof GlobalVariableInProfile)) {
			return false
		}
		GlobalVariableInProfile other = (GlobalVariableInProfile)obj
		return this.getGlobalVariableEntity() == other.getGlobalVariableEntity() &&
				this.getProfile() == other.getProfile()
	}

	@Override
	int hashCode() {
		int hash = 7;
		hash = 31 * hash + this.getGlobalVariableEntity().hashCode();
		hash = 31 * hash + this.getProfile().hashCode();
		return hash;
	}

	@Override
	int compareTo(GlobalVariableInProfile other) {
		int nameCompResult = this.getGlobalVariableEntity().name().compareTo(other.getGlobalVariableEntity().name())
		if (nameCompResult == 0) {
			int profileCompResult = this.getProfile().compareTo(other.getProfile())
			return profileCompResult
		} else {
			return nameCompResult
		}
	}

	@Override
	String toString() {
		return this.getGlobalVariableEntity().name() +
				"\t" + this.getProfileName() +
				"\t" + this.getGlobalVariableEntity().initValue()
	}
}
