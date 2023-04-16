package org.sid.springmvc.web;

import java.util.List;

import org.sid.springmvc.dao.PatientRepository;
import org.sid.springmvc.entities.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.validation.Valid;


@Controller
public class PatientController {
	@Autowired
	private PatientRepository patientRepository;
	
	@GetMapping(path = "/")
	public String index() {
		return "index";
	}
	
	@GetMapping(path = "/patients")
	public String patients(Model model, 
			@RequestParam(name="page", defaultValue="0")int page, 
			@RequestParam(name="size", defaultValue="5")int size,
			@RequestParam(name="keyword",defaultValue="")String mc){
		Page<Patient> pagePatients = patientRepository.findByNameContains(mc,PageRequest.of(page, size));
		model.addAttribute("patients",pagePatients);
		model.addAttribute("pages",new int[pagePatients.getTotalPages()]);
		model.addAttribute("currentPage",page);
		model.addAttribute("size",size);
		model.addAttribute("keyword",mc);
		return "patients";
	}
	
	
	  @GetMapping(path = "/deletePatient") public String delete(Long id,String
	  keyword,int page , int size) { patientRepository.deleteById(id); return
	  "redirect:/patients?page="+page+"&size"+size+"&keyword="+keyword ; }
	  
	 
	@GetMapping(path="deletePatient2")
	public String delete2(Long id,String keyword,int page , int size,Model model) {
		patientRepository.deleteById(id);
		return patients(model,page,size,keyword);
	}
	
	
	// affiche le formulaire d'ajout d'un nouveau patient
	@GetMapping("/formPatient")
	public String formPatient(Model model) {
		model.addAttribute("patient", new Patient());
		model.addAttribute("mode","new");
		return "formPatient";
	}
	
	
	// on demande a spring mvc de faire la validation un fois qu'il stocke les donnes dans 
	// l'obj Patient faire la validation qui se trouve dans le L'entite Patient, 
	//l'obj BindingResult est une collection qui contient la liste des erreus genere pendant la validation des donnes inserees
	@PostMapping("/savePatient")
	public String savePatient(@Valid Patient p,BindingResult bindingResult,Model model) {
		if(bindingResult.hasErrors()) return "formPatient"; 
		Patient p1=patientRepository.save(p);
		Patient p2= patientRepository.findById(p1.getId()).get();
		
		model.addAttribute("patient", p2);
		return "confirmation";
	}
	
	// affiche le formulaire de modification de patient
	@GetMapping(path = "/editPatient")
	public String editPatient(Long id,Model model) {
		Patient p = patientRepository.findById(id).get();
		model.addAttribute("patient",p);
		model.addAttribute("mode","update");
		return "formPatient";
	}
	
	@GetMapping("/listPatients")
	@ResponseBody
	public List<Patient> list() {
		return patientRepository.findAll();
	}


}
