package their.globalvariable.domain

enum Env {
	DEVELOPMENT("Development"),
	PRODUCTION("Production"),
	STAGING("Staging");

	private String id
	String getId() {
		return this.id
	}
	Env(String id) {
		this.id = id
	}
}