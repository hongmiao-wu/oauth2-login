package com.example.oauth2_login.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.oauth2_login.model.Product;
import com.example.oauth2_login.service.ProductService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/product")
public class ProductController {
	
	@Autowired
	ProductService productService;
	
	@PostMapping
	@PreAuthorize("hasAuthority('PRODUCT_CREATE')")
	public ResponseEntity<Product> createProduct(@RequestBody Product product)
	{
		try {
			Product createdProduct = productService.createProduct(product);
			return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(product);
		}
	}
	
	@GetMapping("/all")
	@PreAuthorize("hasAuthority('PRODUCT_READ')")
	public ResponseEntity<List<Product>> getAllProducts()
	{
		List<Product> products = productService.getAllProducts();
		if (products.isEmpty())
		{
			return ResponseEntity.noContent().build();
		}
		
		return ResponseEntity.ok(products);
	}
	
	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('PRODUCT_READ')")
	public ResponseEntity<Product> getProductById(@PathVariable Integer id)
	{
		return productService.getProductById(id)
				.map(ResponseEntity::ok)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found with id: " + id));
	}
	
	// Update a product by ID
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('PRODUCT_UPDATE')")
    public ResponseEntity<Product> updateProductById(@PathVariable Integer id, @RequestBody Product product) {
        Product updatedProduct = productService.updateProductById(id, product);
        return ResponseEntity.ok(updatedProduct);
    }

    // Delete a product by ID
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('PRODUCT_DELETE')")
    public ResponseEntity<Void> deleteProductById(@PathVariable Integer id) {
        productService.deleteProductById(id);
        return ResponseEntity.noContent().build();
    }
	
	
}
