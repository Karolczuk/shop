package com.app.service;

import com.app.dto.ProductDto;
import com.app.exception.AppException;
import com.app.exception.ProductServiceException;
import com.app.mapper.Mappers;

import com.app.model.Product;
import com.app.repository.ProductRepository;
import com.app.validator.CreateProductValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Long createProduct(ProductDto createProductDto) {
        CreateProductValidator createProductValidator = new CreateProductValidator();
        var errors = createProductValidator.validate(createProductDto);
        if (createProductValidator.hasErrors()) {
            var errorsMessage = errors
                    .entrySet()
                    .stream()
                    .map(e -> e.getKey() + ": " + e.getValue())
                    .collect(Collectors.joining(", "));
            throw new ProductServiceException("Create product validation errors -  " + errorsMessage);
        }

        var product = Mappers.fromProductDtoToProduct(createProductDto);
        return productRepository
                .save(product)
                .getId();
    }

    public List<ProductDto> getAllProducts() {
        return productRepository
                .findAll()
                .stream()
                .map(Mappers::fromProductProductDto)
                .collect(Collectors.toList());
    }


    public ProductDto deleteProduct(Long id) {

        if (id == null) {
            throw new AppException("delete exception - id is null");
        }

        var product = productRepository
                .findById(id)
                .orElseThrow(() -> new AppException("delete exception - no product with id " + id));

        productRepository.delete(product);
        return Mappers.fromProductProductDto(product);

    }

    public ProductDto editProduct(Long id, ProductDto productDto) {

        if (id == null) {
            throw new ProductServiceException("Update product exception - id is null");
        }
        if (productDto == null) {
            throw new ProductServiceException("Update product exception - movie object is null");
        }

        var product = productRepository
                .findById(id)
                .orElseThrow(() -> new ProductServiceException("Update product exception - no product with id " + id));

        product.setName(productDto.getName());
        product.setDiscount(productDto.getDiscount());
        product.setPrice(productDto.getPrice());

        return Mappers.fromProductProductDto(productRepository.save(product));

    }

    public ProductDto findById(Long id) {

        Product product = productRepository
                .findById(id)
                .orElseThrow(() -> new AppException("No product with id " + id));

        return Mappers.fromProductProductDto(product);

    }

    public ProductDto findByName(String name) {

        Product product = productRepository
                .findByName(name).get();

        return Mappers.fromProductProductDto(product);


    }

    public ProductDto setProductCount(ProductDto productDto, Long count) {
        if (productDto == null) {
            throw new ProductServiceException("Update product exception - product object is null");
        }

        productDto.setCount(productDto.getCount() - count);
        Product product = Mappers.fromProductDtoToProduct(productDto);

        if (productDto.getCount() < count) {
            throw new ProductServiceException("Too small quantity in stock");

        }

        if (productDto.getCount() != 0) {
            return Mappers.fromProductProductDto(productRepository.save(product));

        }

        productRepository.delete(product);
        return null;

    }


}
