package com.dub.site.directors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class DirectorIdForm {
	
	@Min(value  = 1, message = "validate.min.directorId")
	@NotNull(message = "validate.required.directorId")
	private long id;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
