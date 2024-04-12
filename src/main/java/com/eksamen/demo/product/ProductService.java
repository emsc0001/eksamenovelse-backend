package com.eksamen.demo.product;

import java.util.List;

public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(null);
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product product) {
        Product productToUpdate = productRepository.findById(id).orElseThrow(null);
        productToUpdate.setName(product.getName());
        productToUpdate.setPrice(product.getPrice());
        productToUpdate.setWeightInGrams(product.getWeightInGrams());
        return productRepository.save(productToUpdate);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
