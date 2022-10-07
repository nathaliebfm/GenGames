package com.gengames.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.gengames.model.Categories;
import com.gengames.repository.CategoriesRepository;

@RestController
@RequestMapping("/categories")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CategoriesController {
	
	@Autowired
	private CategoriesRepository categoriesRepository;
	
	@GetMapping
	public ResponseEntity<List<Categories>> getAll(){
		return ResponseEntity.ok(categoriesRepository.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Categories> getById(@PathVariable Long id){
		return categoriesRepository.findById(id)
				.map(response -> ResponseEntity.ok(response))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}
	
	@GetMapping("/type/{type}")
	public ResponseEntity<List<Categories>> getByTitle(@PathVariable String type){
		return ResponseEntity.ok(categoriesRepository.findAllByTypeContainingIgnoreCase(type));
	}
	
	@PostMapping
	public ResponseEntity<Categories> post(@Valid @RequestBody Categories category){
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(categoriesRepository.save(category));
	}
	
	@PutMapping
	public ResponseEntity<Categories> put(@Valid @RequestBody Categories category){
		return categoriesRepository.findById(category.getId())
				.map(response -> ResponseEntity.status(HttpStatus.CREATED)
						.body(categoriesRepository.save(category)))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		Optional<Categories> category = categoriesRepository.findById(id);
		
		if(category.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		
		categoriesRepository.deleteById(id);
	}

}
