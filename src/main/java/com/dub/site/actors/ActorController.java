package com.dub.site.actors;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.dub.entities.Actor;
import com.dub.entities.ActorWithPhoto;
import com.dub.exceptions.ActorNotFoundException;
import com.dub.exceptions.DuplicateActorException;
import com.dub.exceptions.PhotoNotFoundException;
import com.dub.repositories.ActorServices;
import com.dub.site.NameForm;

@Controller
public class ActorController {
	 
	
	@Autowired
	private ActorServices actorServices;
	  
	@GetMapping(value = "actorQueries")
		public String actorQueries() {
			return "actors/actorQueries";	
	}
	
	@GetMapping("numberOfActors")
	public String numberOfActors(Map<String, Object> model) {	
		Long number = actorServices.numberOfActors();
		model.put("number", number);
		return "actors/numberOfActors";
	}// numberOfActors
	
	
	@GetMapping("getActor")
	public String actorIdForm(ModelMap model) {
		model.addAttribute("actorIdForm", new ActorIdForm());
		return "actors/getActor";
	}// getActor
	
	@PostMapping("getActor")
	public String actorIdSubmit(@Valid ActorIdForm actorIdForm,
			BindingResult result,
			ModelMap model) {	
		if (result.hasErrors()) {
            return "actors/getActor";
        }
		
		try  {
			Actor actor = actorServices.getActor(actorIdForm.getId());
			
			model.put("actor", actor);
			
			return "actors/getActorResult";
		} catch (ActorNotFoundException e) {
			result.rejectValue("id", "notFound", "actor not found");	
			return "actors/getActor";
		} catch (RuntimeException e) {
			return "error";
		}
	}// getActor
	
	@GetMapping("getActorByName")
	public String actorForm(ModelMap model) {
		model.addAttribute("actorName", new NameForm());
		return "actors/getActorByName";
	}// getActor
	
	@PostMapping("getActorByName")
	public String actorSubmit(@Valid @ModelAttribute("actorName") NameForm form,
			BindingResult bindingResult,
			ModelMap model) {		
		if (bindingResult.hasErrors()) {
            return "actors/getActorByName";
        }
				
		try {
			Actor actor = actorServices.getActor(form.getFirstName(), 
				form.getLastName());
			
			model.put("actor", actor);
		
			return "actors/getActorResult";
		} catch (ActorNotFoundException e) {
			return "actors/getActorNoResult";
		} catch (RuntimeException e) {
			return "error";
		}
	}// getActor
	
	@GetMapping("allActors")
    public ModelAndView allActors() {
       
		List<Actor> actors = actorServices.getAllActors();

        Map<String, Object> params = new HashMap<>();
        params.put("actors", actors);

        return new ModelAndView("actors/allActors", params);
    }
	
	@GetMapping(value = "createActor")
	public ModelAndView createActor(ModelMap model) {
		model.addAttribute("actorForm", new ActorForm());
		return new ModelAndView("actors/createActor", model);
	}// createActor

	@PostMapping(value = "createActor")
	public String createActor(
			@Valid @ModelAttribute("actorForm") ActorForm form,
			BindingResult result, ModelMap model) {
		
		if (result.hasErrors()) {
			return "actors/createActor";
		} else {
			Actor actor = new Actor();
			actor.setFirstName(form.getFirstName());
			actor.setLastName(form.getLastName());
			actor.setBirthDate(form.getBirthDate());
			try {
				actorServices.createActor(actor);	
				model.addAttribute("actor", actor);
				return "actors/createActorSuccess";
			} catch (DuplicateActorException e) {
				result.rejectValue("firstName", "duplicate", "name already present");				
				return "actors/createActor";
			} catch (RuntimeException e) {
				return "actors/error";
			}
		}// if		
	}// createActor
	
	
	@GetMapping(value = "updateActor")
	public ModelAndView updateActor(ModelMap model) {
		model.addAttribute("getActor", new ActorIdForm());
		return new ModelAndView("actors/updateActor1", model);
	}// updateActor
			
	@PostMapping(value = "updateActor1")
	public String updateActor(
				@Valid @ModelAttribute("getActor") ActorIdForm form,
				BindingResult result, ModelMap model) {		
		if (result.hasErrors()) {
			return "actors/updateActor1";
		} else {
			try {
				long actorId = form.getId();
				Actor actor = actorServices.getActor(actorId);
				model.addAttribute("actor", actor);
				return "actors/updateActor2";
			} catch (ActorNotFoundException e) {
				result.rejectValue("id", "notFound", "actor not found");							
				return "actors/updateActor1";
			} catch (RuntimeException e) {
				return "actors/error";
			}// try
		}// if				
	}// updateActor
	
	@PostMapping(value = "updateActor2")
	public String updateActor2(
						@Valid @ModelAttribute("actor") ActorUpdateForm form,
						BindingResult result, ModelMap model) {		
		if (result.hasErrors()) {
			return "actors/updateActor2";			
		} else {
			try {
				Actor actor = new Actor();
				actor.setFirstName(form.getFirstName());
				actor.setLastName(form.getLastName());
				actor.setBirthDate(form.getBirthDate());
				actor.setId(form.getId());
				actorServices.updateActor(actor);			
				return "actors/updateActorSuccess";
			} catch (RuntimeException e) {
				return "actors/error";
			}				
		}// if		
	}// updateActor
	
	
	@GetMapping(value = "deleteActor")
	public ModelAndView deleteActor(ModelMap model) {
		model.addAttribute("getActor", new ActorIdForm());
		return new ModelAndView("actors/deleteActor", model);
	}// deleteActor
	
	@PostMapping(value = "deleteActor")
	public String deleteActor(
			@Valid @ModelAttribute("getActor") ActorIdForm form,
			BindingResult result, ModelMap model) {	
		if (result.hasErrors()) {
			return "actors/deleteActor";
		} else {
			try {
				long actorId = form.getId();
				Actor actor = actorServices.getActor(actorId);
				model.addAttribute("actor", actor);
				actorServices.deleteActor(actorId);		
				return "actors/deleteActorSuccess";
			} catch (ActorNotFoundException e) {
				result.rejectValue("id", "notFound", "actor not found");				
				return "actors/deleteActor";
			} catch (RuntimeException e) {
				return "actors/error";
			}// try								
		}
	}// deleteActor
	
	@GetMapping("createActorPhotoMulti")
	public ModelAndView createActorPhotoMulti(ModelMap model) {
		model.addAttribute("actorPhotoMulti", new ActorPhotoMultiForm());
		return new ModelAndView("actors/createActorPhotoMultipart", model);
	}// getActor

	@PostMapping("createActorPhotoMulti")      	 
	public String uploadActorPhoto(
            @Valid @ModelAttribute("actorPhotoMulti") ActorPhotoMultiForm form, 
            BindingResult result) {	 
			
		if (result.hasErrors()) {
			return "actors/createActorPhotoMultipart";
		}
		// Get name of uploaded file.
		MultipartFile uploadedFileRef = null;
		String fileName;
		long actorId= form.getActorId();
	 
		uploadedFileRef = form.getUploadedFile();
		fileName = form.getUploadedFile().getOriginalFilename();
	 
		String path = "/home/dominique/Pictures/tmp/" + fileName; 
       	 
		File outputFile = new File(path);
	 
		InputStream is = null;     
		OutputStream os = null;
	
		// This buffer will store the data read from 'uploadedFileRef'
		byte[] buffer = new byte[1000];
		int bytesRead = -1;
		int totalBytes = 0;
    
		// actual photo upload
		
		try {
			is = uploadedFileRef.getInputStream();
			os = new FileOutputStream(outputFile);
		
			while ((bytesRead = is.read(buffer)) != -1) {
				os.write(buffer);
				totalBytes += bytesRead;
			}
	
			if (totalBytes == 0 || totalBytes != uploadedFileRef.getSize()) {
				return "actors/error";
			}
		} catch (FileNotFoundException e) {
			result.rejectValue("uploadedFile", "notFound", "file not found");
			return "actors/createActorPhotoMultipart";	
		} catch (Exception e) {
			e.printStackTrace();	
		} finally{             
			try {
				is.close();
				os.close();
			} catch (NullPointerException e) {
				return "actors/createActorPhotoMultipart";	
			} catch (IOException e) {
				e.printStackTrace();
			}
		}// try
    	    	   	      
		// Now persist actor photo to the database  
		   
		CreateActorPhoto createActorPhoto = new CreateActorPhoto();	        
		createActorPhoto.setActorId(actorId);	        
		createActorPhoto.setImageFile(path);
        
		try {
			if (totalBytes > 65535) {
				// photo size too large for a BLOB					 
				result.rejectValue("uploadedFile", "size", "file exceeds 65535 bytes");
				return "actors/createActorPhotoMultipart";	
			}
			actorServices.createActorPhoto(createActorPhoto);
		} catch (ActorNotFoundException e) {
			result.rejectValue("actorId", "notFound", "actor not found");							
			return "actors/createActorPhotoMultipart";	
		} catch (IOException e) {
			return "actors/error";
		} catch (RuntimeException e) {
			return "actors/error";
		} finally {
			// always delete temporary file			
			try {
				Files.deleteIfExists(outputFile.toPath());
			} catch (IOException e) {
				e.printStackTrace();
			}	  
		}
		return "actors/createActorPhotoSuccess"; 
	}// uploadActorPhoto

	
	@GetMapping(value = "listAllActorsWithPhotos")
	public String listAllActorsWithPhotos(Map<String, Object> model) {				
		List<Actor> list;
		List<ActorWithPhoto> actors = new ArrayList<>();		
		try {
			list = actorServices.getAllActors();		
			for (Actor actor : list) {
				ActorWithPhoto actw = new ActorWithPhoto(actor);
				try {
					long photoId = actorServices.getPhotoId(actor);
					actw.setPhotoId(photoId);
				} catch(PhotoNotFoundException e) {
					actw.setPhotoId(0);
				}									
				actors.add(actw);
			}// for
			model.put("actors", actors);	
			return "actors/listActorsWithPhoto";
		} catch (RuntimeException e) {
			return "actors/error";
		}
        
	}// listAllActorsWithPhoto 
	
	@GetMapping("getAllPhotosByActor")
	public ModelAndView getAllPhotosByActor(ModelMap model) {
		model.addAttribute("actorName", new NameForm());
		return new ModelAndView("actors/getAllPhotosByActor", model);
	}// getActor
		
	@PostMapping("getAllPhotosByActor")
	public String getAllPhotosByActor(
				@Valid @ModelAttribute("actorName") NameForm form,
				BindingResult result, ModelMap model) {			
		if (result.hasErrors()) {
			return "actors/getAllPhotosByActor";
		} else {				
			try {
				Actor actor = actorServices.getActor(
												form.getFirstName(),
												form.getLastName());
				List<Long> photoIds = 
							actorServices.getAllPhotoIds(actor);
				model.addAttribute("photoIds", photoIds);
				model.addAttribute("firstName", actor.getFirstName());
				model.addAttribute("lastName", actor.getLastName());
				return "actors/getAllPhotosByActorResult";
			} catch (ActorNotFoundException e) {				
				model.addAttribute("backPage", "getAllPhotosByActor");
				return "actors/getActorNoResult";
			} catch (RuntimeException e) {
				e.printStackTrace();
				return "actors/error";
			}// try
		}
	}// getActor	
	

	@GetMapping("getActorWithPhotoByName")
	public ModelAndView getActorWithPhotoByName(ModelMap model) {
		model.addAttribute("actorName", new NameForm());
		return new ModelAndView("actors/getActorWithPhotoByName", model);
	}// getActor
	
	@PostMapping("getActorWithPhotoByName")
	public String getActorWithPhotoByName(
				@Valid @ModelAttribute("actorName") NameForm form,
				BindingResult result, ModelMap model) {
			
		if (result.hasErrors()) {
			return "actors/getActorWithPhotoByName";
		} else {
			try {								
				Actor actor = actorServices.getActor(
												form.getFirstName(), 
												form.getLastName());
				ActorWithPhoto actw = new ActorWithPhoto(actor);
				try {
					long photoId = actorServices.getPhotoId(actor);
					actw.setPhotoId(photoId);	
				} catch (PhotoNotFoundException e) {
					actw.setPhotoId(0);	
				} 											
				model.addAttribute("actor", actw);	
				return "actors/getActorWithPhotoResult";
			} catch (ActorNotFoundException e) {
				model.addAttribute("backPage", "getActorWithPhotoByName");
				return "actors/getActorNoResult";
			} catch (RuntimeException e) {
				e.printStackTrace();
				return "actors/error";
			}
		}// if
	}// getActorWithPhotoByName
	
	

	@GetMapping("deleteActorPhoto")
	public ModelAndView deleteActorPhoto(ModelMap model) {
		model.addAttribute("getActorPhoto", new PhotoIdForm());
		return new ModelAndView("actors/deleteActorPhoto", model);
	}// deleteActorPhoto
	
	@PostMapping("deleteActorPhoto")
	public String deleteActorPhoto(
			@Valid @ModelAttribute("getActorPhoto") PhotoIdForm form,
			BindingResult result, ModelMap model) {	
		if (result.hasErrors()) {
			return "actors/deleteActorPhoto";
		}
		try {
			long photoId = form.getId();
			actorServices.deletePhoto(photoId);
			return "actors/deleteActorPhotoSuccess";
		} catch (PhotoNotFoundException e) {
			result.rejectValue("id", "notFound", "photo not found");				
			return "actors/deleteActorPhoto";
		} catch (RuntimeException e) {	
			return "actors/error";
		}
	}// deleteActorPhoto
	
	@GetMapping("doGetActorPhoto/{photoId}")
	public @ResponseBody byte[] doGetActorPhoto(@PathVariable("photoId") long photoId)  {
		try {
			byte[] imageBytes = actorServices.getPhotoData(photoId);
			return imageBytes;
		} catch (PhotoNotFoundException e) {
			return null;
		}// try
	}

}
