package com.dub.site.directors;


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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.dub.entities.Director;
import com.dub.exceptions.DirectorNotFoundException;
import com.dub.exceptions.DuplicateDirectorException;
import com.dub.repositories.DirectorServices;
import com.dub.site.directors.DirectorIdForm;
import com.dub.site.directors.DirectorUpdateForm;
import com.dub.site.NameForm;

@Controller
public class DirectorController {
	 
	
	@Autowired
	private DirectorServices directorServices;
	  
	@GetMapping("directorQueries")
		public String directorQueries() {
		
			return "directors/directorQueries";	
	}
	
	@GetMapping("numberOfDirectors")
	public String numberOfDirectors(Map<String, Object> model) {
	
		Long number = directorServices.numberOfDirectors();
		model.put("number", number);
		return "directors/numberOfDirectors";
	}// numberOfDirectors
	
	
	@GetMapping("getDirector")
	public String directorIdForm(ModelMap model) {
		
		model.addAttribute("directorIdForm", new DirectorIdForm());
		return "directors/getDirector";
	}// getDirector
	
	@PostMapping("getDirector")
	public String directorIdSubmit(@Valid DirectorIdForm directorIdForm,
			BindingResult bindingResult,
			ModelMap model) {

		if (bindingResult.hasErrors()) {
            return "directors/getDirector";
        }
		
		Director director = directorServices.getDirector(directorIdForm.getId());
		
		
		model.put("director", director);
		
		return "directors/getDirectorResult";
	}// getDirector
	
	@GetMapping("getDirectorByName")
	public String directorForm(ModelMap model) {
		
		model.addAttribute("directorName", new NameForm());
		return "directors/getDirectorByName";
	}// getDirector
	
	@PostMapping("getDirectorByName")
	public String directorSubmit(@Valid @ModelAttribute("directorName") NameForm form,
			BindingResult bindingResult,
			ModelMap model) {

		
		if (bindingResult.hasErrors()) {
            return "directors/getDirectorByName";
        }
			
		try {
			Director director = directorServices.getDirector(form.getFirstName(), 
				form.getLastName());
		
			model.put("director", director);
		
			return "directors/getDirectorResult";
		} catch (DirectorNotFoundException e) {
			return "directors/getDirectorNoResult";
		} catch (RuntimeException e) {
			return "error";
		}
	}// getDirector
	
	@GetMapping("allDirectors")
    public ModelAndView allDirectors() {
       
		List<Director> directors = directorServices.getAllDirectors();

        Map<String, Object> params = new HashMap<>();
        params.put("directors", directors);

        return new ModelAndView("directors/allDirectors", params);
    }
	
	@GetMapping(value = "createDirector")
	public ModelAndView createDirector(ModelMap model) {
		model.addAttribute("directorForm", new DirectorForm());
		
		return new ModelAndView("directors/createDirector", model);
	}// createDirector

	@PostMapping(value = "createDirector")
	public String createDirector(
			@Valid @ModelAttribute("directorForm") DirectorForm form,
			BindingResult result, ModelMap model) {
		
		if (result.hasErrors()) {
			return "directors/createDirector";
		} else {
			Director director = new Director();
			director.setFirstName(form.getFirstName());
			director.setLastName(form.getLastName());
			director.setBirthDate(form.getBirthDate());
			try {
				directorServices.createDirector(director);	
				model.addAttribute("director", director);
				return "directors/createDirectorSuccess";
			} catch (DuplicateDirectorException e) {
				
				result.rejectValue("firstName", "duplicate", "name already present");				
				return "directors/createDirector";
			} catch (RuntimeException e) {
				return "directors/error";
			}
		}// if		
	}// createDirector
	
	
	@GetMapping(value = "updateDirector")
	public ModelAndView updateDirector(ModelMap model) {
		
		model.addAttribute("getDirector", new DirectorIdForm());
		return new ModelAndView("directors/updateDirector1", model);
	}// updateDirector
			
	@PostMapping(value = "updateDirector1")
	public String updateDirector(
				@Valid @ModelAttribute("getDirector") DirectorIdForm form,
				BindingResult result, ModelMap model) {		
		if (result.hasErrors()) {
	
			return "directors/updateDirector1";
		} else {
			try {
				long directorId = form.getId();
				Director director = directorServices.getDirector(directorId);
				model.addAttribute("director", director);
				return "directors/updateDirector2";
			} catch (DirectorNotFoundException e) {
				result.rejectValue("id", "notFound", "director not found");							
				return "directors/updateDirector1";
			} catch (RuntimeException e) {
				return "directors/error";
			}// try
		}// if				
	}// updateDirector
	
	@PostMapping(value = "updateDirector2")
	public String updateDirector2(
						@Valid @ModelAttribute("director") DirectorUpdateForm form,
						BindingResult result, ModelMap model) {		
		if (result.hasErrors()) {
			return "directors/updateDirector2";			
		} else {
			try {
				Director director = new Director();
				director.setFirstName(form.getFirstName());
				director.setLastName(form.getLastName());
				director.setBirthDate(form.getBirthDate());
				director.setId(form.getId());
				directorServices.updateDirector(director);			
				return "directors/updateDirectorSuccess";
			} catch (RuntimeException e) {
				return "directors/error";
			}				
		}// if		
	}// updateDirector
	
	
	@GetMapping(value = "deleteDirector")
	public ModelAndView deleteDirector(ModelMap model) {

		model.addAttribute("getDirector", new DirectorIdForm());
		return new ModelAndView("directors/deleteDirector", model);
	}// deleteDirector
	
	@PostMapping(value = "deleteDirector")
	public String deleteDirector(
			@Valid @ModelAttribute("getDirector") DirectorIdForm form,
			BindingResult result, ModelMap model) {	
		if (result.hasErrors()) {
			return "directors/deleteDirector";
		} else {
			try {
				long directorId = form.getId();
				Director director = directorServices.getDirector(directorId);
				model.addAttribute("director", director);
				directorServices.deleteDirector(directorId);		
				return "directors/deleteDirectorSuccess";
			} catch (DirectorNotFoundException e) {
				result.rejectValue("id", "notFound", "director not found");				
				return "directors/deleteDirector";
			} catch (RuntimeException e) {
				return "directors/error";
			}// try								
		}
	}// deleteDirector

}
