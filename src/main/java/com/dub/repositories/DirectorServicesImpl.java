package com.dub.repositories;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.dub.entities.Director;
import com.dub.exceptions.DirectorNotFoundException;
import com.dub.exceptions.DuplicateDirectorException;

@Service
public class DirectorServicesImpl implements DirectorServices {
	
	@Autowired 
	DirectorRepository directorRepository;

	@Override
	public List<Director> getAllDirectors() {
		return (List<Director>)directorRepository.findAll();
	}

	@Override
	public Director getDirector(long id) {
		Director director = directorRepository.findOne(id);
		if (director != null) {
			return director;
		} else {
			throw new DirectorNotFoundException();
		}
	}
	
	@Override
	public Director getDirector(String firstName, String lastName) {
		List<Director> list = directorRepository.findByFirstNameAndLastName(firstName, lastName);
		if (!list.isEmpty()) {
			return list.get(0);
		} else {
			throw new DirectorNotFoundException();
		}
	}
	

	@Override
	public void deleteDirector(long id) {
		try {
			directorRepository.delete(id);
		} catch (EmptyResultDataAccessException e) {
			throw new DirectorNotFoundException();
		}
	}

	@Override
	public void createDirector(Director director) {
		try {
			directorRepository.save(director);
		} catch (Exception e) {
			String ex = ExceptionUtils.getRootCauseMessage(e);
			if (ex.contains("director_unique")) {
				throw new DuplicateDirectorException();
			} else {
				throw e;
			}
		}
	}

	@Override
	public void updateDirector(Director director) {
		if (directorRepository.exists(director.getId())) {
			directorRepository.save(director);
		} else {
			throw new DirectorNotFoundException();
		}
	}

	@Override
	public long numberOfDirectors() {
		return directorRepository.count();
	}

}