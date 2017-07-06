package com.dub.site.movies;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;


import com.dub.entities.DisplayMovie;
import com.dub.entities.Movie;
import com.dub.exceptions.DirectorNotFoundException;
import com.dub.exceptions.DuplicateMovieException;
import com.dub.exceptions.MovieNotFoundException;
import com.dub.repositories.MovieServices;


@Controller
public class MovieController {
	
	@Resource
	private MovieServices movieServices;
	
	
	@InitBinder({"movie", "movieKey"})	  
	protected void initBinder(WebDataBinder binder) {
	    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");		
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
	
		
	@GetMapping("movieQueries")
	public String movieQueries() {
		return "movies/movieQueries";
	}
	
	@GetMapping("listAllMovies")
	public String listAllMoviesWithDirName(Map<String, Object> model) 
	{		
		List<DisplayMovie> list = movieServices.getAllMovies();	
        model.put("movies", list);
        return "movies/listMovies";
	}
		
	@GetMapping("numberOfMovies")
	public String numberOfMovies(Map<String, Object> model) 
	{		
		Long number = movieServices.numberOfMovies();
		model.put("number", number);
		return "movies/numberOfMovies";
	}
	
	@GetMapping("getMovie")
	public ModelAndView getMovie(ModelMap model) {
		model.addAttribute("getMovie", new TitleForm());
		return new ModelAndView("movies/getMovie", model);
	}
	
	@PostMapping("getMovie")
	public String getMovie(
			@Valid @ModelAttribute("getMovie") TitleForm form,
			BindingResult result, ModelMap model) {
		
		if (result.hasErrors()) {
			return "movies/getMovie";
		} else {
			List<DisplayMovie> list = 
						movieServices.getMovie(form.getTitle());
			if (list.size() > 0) {
				model.put("movies", list);
				return "movies/getMovieResult";
			} else { 				
				model.addAttribute("backPage", "getMovie");
				return "movies/getMovieNoResult";			
			}
		}
	}
	
	@GetMapping("getSingleMovie")
	public ModelAndView getSingleMovie(ModelMap model) {
		model.addAttribute("movieKey", new TitleDateForm());		
		return new ModelAndView("movies/getSingleMovie", model);
	}
	
	@PostMapping("getSingleMovie")
	public String getSingleMovie(
			@Valid @ModelAttribute("movieKey") TitleDateForm form,
			BindingResult result, ModelMap model) 
	{	
		if (result.hasErrors()) {		
			return "movies/getSingleMovie";
		} else {	
			try {
				DisplayMovie displayMovie = movieServices.getMovie(
								form.getTitle(), form.getReleaseDate());																
				model.put("movie", displayMovie);
				
				return "movies/getSingleMovieResult";		
			} catch (MovieNotFoundException e) {
				model.addAttribute("backPage", "getSingleMovie");
				return "movies/getMovieNoResult";	
			} catch (RuntimeException e) {
				return "error";
			}	
		}
	}
	
	@GetMapping("createMovie")
	public ModelAndView createMovie(ModelMap model) {
		model.addAttribute("movie", new MovieForm());		
		return new ModelAndView("movies/createMovie", model);
	}

	@PostMapping("createMovie")
	public String createMovie(
						@Valid @ModelAttribute("movie") MovieForm form,
						BindingResult result, ModelMap model) 
	{
		if (result.hasErrors()) {	
			return "movies/createMovie";
		} else {
			try {
				Movie movie = new Movie();
				movie.setDirectorId(form.getDirectorId());
				movie.setReleaseDate(form.getReleaseDate());
				movie.setRunningTime(form.getRunningTime());
				movie.setTitle(form.getTitle());
				movieServices.createMovie(movie);
				model.addAttribute("movie", movie);
				return "movies/createMovieSuccess";		
			} catch (DuplicateMovieException e) {
				result.rejectValue("title", "duplicate", 
						"film already present");
				return "movies/createMovie";
			} catch (DirectorNotFoundException e) {
				result.rejectValue("directorId", "notFound", 
						"director not found");
				return "movies/createMovie";
			} catch (RuntimeException e) {
				return "error";
			}
						
		}
	}
	
	@GetMapping("deleteMovie")
	public ModelAndView deleteMovie(ModelMap model) {
		model.addAttribute("movieId", new MovieIdForm());
		return new ModelAndView("movies/deleteMovie", model);
	}
	
	@PostMapping("deleteMovie")
	public String deleteMovie(
						@Valid @ModelAttribute("movieId") MovieIdForm form,
						BindingResult result, ModelMap model) 
	{	
		if (result.hasErrors()) {
			return "movies/deleteMovie";
		} else {
			try {
				long id = form.getId();			
				movieServices.deleteMovie(id);			
				return "movies/deleteMovieSuccess";
			} catch (MovieNotFoundException e) {
				result.rejectValue("id", "notFound", "movie not found");				
				return "movies/deleteMovie";								
			} catch (RuntimeException e) {
				return "error";
			}		
		}
	}
	
	
	@GetMapping("updateMovie")
	public ModelAndView updateMovie(ModelMap model) {
		model.addAttribute("movieId", new MovieIdForm());
		return new ModelAndView("movies/updateMovie1", model);
	}
	
	@PostMapping("updateMovie1")
	public String updateMovie(
			@Valid @ModelAttribute("movieId") MovieIdForm form,
			BindingResult result, ModelMap model) {
		
		if (result.hasErrors()) {
			return "movies/updateMovie1";
		} else {
			try { 
				Movie movie = movieServices.getMovie(form.getId());
				model.addAttribute("movie", movie);									
				return "movies/updateMovie2";
			} catch (MovieNotFoundException e) {
				result.rejectValue("id", "notFound", "movie not found");									
				return "movies/updateMovie1";
			} catch (RuntimeException e) {
				return "error";
			}			
		}		 
	}
	
	@PostMapping("updateMovie2")
	public String updateMovie2(
			@Valid @ModelAttribute("movie") UpdateMovieForm form,
			BindingResult result, ModelMap model) {
		
		if (result.hasErrors()) {	
			return "movies/updateMovie2";
		} else {
			try {
				Movie movie = new Movie();
				movie.setDirectorId(form.getDirectorId());
				movie.setRunningTime(form.getRunningTime());
				movie.setId(form.getId());
				movie.setReleaseDate(form.getReleaseDate());
				movie.setTitle(form.getTitle());
				movieServices.updateMovie(movie);			
				return "movies/updateMovieSuccess";
			} catch (DirectorNotFoundException e) {
				result.rejectValue("directorId", "notFound", 
								"director not found");				
				return "movies/updateMovie2";	
			} 
		} 
	}	
}