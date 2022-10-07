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

import com.gengames.model.Products;
import com.gengames.repository.CategoriesRepository;
import com.gengames.repository.ProductsRepository;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProductsController {
	
	@Autowired
	private ProductsRepository productsRepository;
	
	@Autowired
	private CategoriesRepository categoriesRepository;
	
	@GetMapping
	public ResponseEntity<List<Products>> getAll(){
		return ResponseEntity.ok(productsRepository.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Products> getById(@PathVariable Long id){
		return productsRepository.findById(id).map(response -> ResponseEntity.ok(response))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}
	
	@GetMapping("/name/{name}")
	public ResponseEntity<List<Products>> getByName (@PathVariable String name){
		return ResponseEntity.ok(productsRepository.findAllByNameContainingIgnoreCase(name));
	}
	
	@PostMapping
	public ResponseEntity<Products> post(@Valid @RequestBody Products product){
		if(categoriesRepository.existsById(product.getCategory().getId()))
			return ResponseEntity.status(HttpStatus.CREATED)
				.body(productsRepository.save(product));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}
	
	@PutMapping
	public ResponseEntity<Products> put(@Valid @RequestBody Products product){
		if(productsRepository.existsById(product.getId())) {
			if(categoriesRepository.existsById(product.getCategory().getId()))
				return ResponseEntity.status(HttpStatus.OK)
						.body(productsRepository.save(product));
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		Optional<Products> product = productsRepository.findById(id);
		if(product.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		productsRepository.deleteById(id);
	}

}
