package their.globalvariable.domain

enum SaveHTML {
	YES(true),
	NO(false);

	private Boolean value
	Boolean isRequired() {
		return this.value
	}
	SaveHTML(Boolean value) {
		this.value = value
	}
}
