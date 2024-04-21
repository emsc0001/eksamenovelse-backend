package com.example.demo.service;

import com.example.demo.dto.ProductDTO;
import com.example.demo.entity.Product;
import com.example.demo.errorhandling.exception.NotFoundException;
import com.example.demo.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product toEntity(ProductDTO productDTO) {
        Product product = new Product();
        product.setId(productDTO.getId());
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setWeightInGrams(productDTO.getWeightInGrams());
        product.setAvailable(productDTO.getAvailable());
        return product;
    }

    public ProductDTO toDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setPrice(product.getPrice());
        productDTO.setWeightInGrams(product.getWeightInGrams());
        productDTO.setAvailable(product.getAvailable());
        return productDTO;
    }

    public List<ProductDTO> getAll(String nameSearch) {
        if (nameSearch != null) {
            return productRepository.findByNameContaining(nameSearch).stream().map(this::toDTO).toList();
        }
        return productRepository.findAll().stream().map(this::toDTO).toList();
    }

    public Optional<ProductDTO> getById(Long id) {
        var product = productRepository.findById(id);
        return product.map(this::toDTO);
    }

    public ProductDTO save(ProductDTO productDTO) {
        var product = toEntity(productDTO);
        product = productRepository.save(product);
        return toDTO(product);
    }

    public ProductDTO update(Long id, ProductDTO productDTO) {
        var productToUpdate = productRepository.findById(id).orElseThrow(NotFoundException::new);
        var newProduct = toEntity(productDTO);

        productToUpdate.setName(newProduct.getName());
        productToUpdate.setPrice(newProduct.getPrice());
        productToUpdate.setWeightInGrams(newProduct.getWeightInGrams());
        productToUpdate.setAvailable(newProduct.getAvailable());

        var updatedProduct = productRepository.save(productToUpdate);
        return toDTO(updatedProduct);
    }

    public void delete(Long id) {
        var product = productRepository.findById(id).orElseThrow(NotFoundException::new);
        product.setAvailable(false);
        productRepository.save(product);
    }
}
