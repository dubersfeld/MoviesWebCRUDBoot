package com.dub;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.junit4.SpringRunner;

import com.dub.entities.Actor;
import com.dub.exceptions.ActorNotFoundException;
import com.dub.exceptions.DuplicateActorException;
import com.dub.repositories.ActorRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {
	
	@Test
	public void contextLoads() {
	}
	
	/*
	@Test
	public void findAllActors() {
		List<Actor> actors = actorDAO.findAll();
		
		for (Actor actor : actors) {
			System.out.println(actor.getFirstName() + " " 
												+ actor.getLastName());
		}
	}
	
	@Test
	public void updateActor() {
		Actor actor = new Actor();
		actor.setId(105);
		
		java.text.SimpleDateFormat sdf = 
		     new java.text.SimpleDateFormat("yyyy-MM-dd");

		String dateString = "1980-12-12";
		
		actor.setFirstName("Paul");
		actor.setLastName("Enclume");
		
		Date date;
		try {
			date = sdf.parse(dateString);
			actor.setBirthDate(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		// actual update
		actorDAO.update(actor);
	}
	
	/*
	@Test
	public void updateActorNotFound() {
		Actor actor = new Actor();
		actor.setId(42);
		
		java.text.SimpleDateFormat sdf = 
		     new java.text.SimpleDateFormat("yyyy-MM-dd");

		String dateString = "1980-12-12";
		
		actor.setFirstName("Paul");
		actor.setLastName("Enclume");
		
		Date date;
		try {
			date = sdf.parse(dateString);
			actor.setBirthDate(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		try {
			// actual update
			actorRepository.update(actor);
		} catch (ActorNotFoundException e) {
			System.out.println("Update exception caught: " + e);
		}
	}
	
	@Test
	public void getActorById() {
		//long id = 101;
		long id = 666;
		try {
			Actor actor = actorRepository.getActor(id);
			System.out.println("getActorById: " + actor);
		} catch (ActorNotFoundException e) {
			System.out.println("By Id exception caught: " + e);
		}
	}

	
	
	@Test
	public void getActorByName() {
		Actor actor = actorRepository.getActor("James", "Dean");
		System.out.println("actorByName: " + actor);
	}
	
	@Test
	public void getActorByNameNotFound() {
		try {
		Actor actor = actorRepository.getActor("Johnny", "Walker");
		System.out.println("actorByName: " + actor);
		} catch (ActorNotFoundException e) {
			System.out.println("By name exception caught: " + e);
		}
	}
	
	
	
	@Test
	public void addActor() {
		Actor newActor = new Actor();
		
		java.text.SimpleDateFormat sdf = 
			     new java.text.SimpleDateFormat("yyyy-MM-dd");

		String dateString = "1980-12-12";
			
		newActor.setFirstName("Albert");
		newActor.setLastName("Einstein");
		
		Date date;
			
		try {	
			date = sdf.parse(dateString);
			newActor.setBirthDate(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
			
		try {
		actorRepository.create(newActor);
		} catch (DuplicateActorException e) {
			System.out.println("Add exception caught: " + e);
		}
		
		System.out.println("new id: " + newActor.getId());
	
	}
	
	@Test
	public void deleteActor() {
		long id = 117;
		try {
			actorRepository.delete(id);
		} catch (ActorNotFoundException e) {
			System.out.println("Delete exception caught: " + e);
		}
	}
*/
}
