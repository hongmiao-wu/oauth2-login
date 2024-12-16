package com.example.oauth2_login.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.oauth2_login.model.Product;
import com.example.oauth2_login.repository.ProductRepository;

@Service
public class ProductService {
	@Autowired
	private ProductRepository productRepository;
	
	public Product createProduct(Product product)
	{
		return productRepository.save(product);
	}
	
	public List<Product> getAllProducts()
	{
		return productRepository.findAll();
	}
	
	public Optional<Product> getProductById(Integer id)
	{
		return productRepository.findById(id);
	}
	
	public Product updateProductById(Integer id, Product product)
	{
		return productRepository.findById(id)
				.map(existingProduct -> {
					existingProduct.setName(product.getName());
					existingProduct.setDescription(product.getDescription());
					existingProduct.setPrice(product.getPrice());
					existingProduct.setStockQuantity(product.getStockQuantity());
					return productRepository.save(existingProduct);
				})
				.orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
	}
	
	public void deleteProductById(Integer id)
	{
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
		productRepository.delete(product);
	}

}
