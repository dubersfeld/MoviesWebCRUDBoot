package com.dub.repositories;

import java.util.List;

import com.dub.entities.Director;

public interface DirectorServices {

	List<Director> getAllDirectors();
	
	Director getDirector(long id);	
	Director getDirector(String firstName, String lastName);
	
	void deleteDirector(long id);
	void createDirector(Director director);
	void updateDirector(Director director);
	long numberOfDirectors();
}

