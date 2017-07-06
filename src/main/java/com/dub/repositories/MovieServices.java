package com.dub.repositories;

import java.util.Date;
import java.util.List;

import com.dub.entities.DisplayMovie;
import com.dub.entities.Movie;


public interface MovieServices {

	List<DisplayMovie> getAllMovies();
	long numberOfMovies();
	
	Movie getMovie(long id);
	
	DisplayMovie getMovie(String title, Date releaseDate);
		
	List<DisplayMovie> getMovie(String title);
	
	void deleteMovie(long id);
	
	void createMovie(Movie movie);	
	
	void updateMovie(Movie movie);	

}
