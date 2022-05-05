package com.kazurayam.ks.globalvariable

import java.nio.file.Path

public class GveProfilePair implements Comparable<GveProfilePair> {
	
	GlobalVariableEntity gve
	Path profile
	
	GveProfilePair(GlobalVariableEntity gve, Path profile) {
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
		if (! (obj instanceof GveProfilePair)) {
			return false
		}
		GveProfilePair other = (GveProfilePair)obj
		if (this.getGlobalVariableEntity()== other.getGlobalVariableEntity() &&
			this.getProfile()== other.getProfile()) {
			return true
		} else
			return false
	}
	
	@Override
	int hashCode() {
		int hash = 7;
		hash = 31 * hash + this.getGlobalVariableEntity().hashCode();
		hash = 31 * hash + this.getProfile().hashCode();
		return hash;
	}
	
	@Override
	int compareTo(GveProfilePair other) {
		int nameCompResult = this.getGlobalVariableEntity().name().compareTo(other.getGlobalVariableEntity().name())
		if (nameCompResult == 0) {
			int profileCompResult = this.getProfile().compareTo(other.getProfile())
			return profileCompResult
		} else
			return nameCompResult
		
	}
	
	@Override
	String toString() {
		return "GlobalVariable." + this.getGlobalVariableEntity().name() + " in Profile " + this.getProfileName()
	}
}