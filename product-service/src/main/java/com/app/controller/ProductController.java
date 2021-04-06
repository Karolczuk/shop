package com.app.controller;

import com.app.dto.ProductDto;
import com.app.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public Long addProduct(@RequestBody ProductDto productDto) {
        return productService.createProduct(productDto);
    }


    @PutMapping("/update/{id}")
    public ProductDto editProduct(@PathVariable Long id, @RequestBody ProductDto productDto) {
        return productService.editProduct(id, productDto);
    }

    @GetMapping("/findAll")
    public List<ProductDto> findAllProducts() {
        return productService.getAllProducts();
    }

    @DeleteMapping("{id}")
    public ProductDto deleteProduct(@PathVariable Long id) {
        return productService.deleteProduct(id);
    }

    @GetMapping("/find/id/{id}")
    public ProductDto findById(@PathVariable Long id){
        return productService.findById(id);
    }

    @GetMapping("/find/{name}")
    public ProductDto findByName(@PathVariable String name){
        return productService.findByName(name);
    }

    @GetMapping("/setCount/{count}")
    public ProductDto setProductCount(@RequestBody ProductDto productDto,  @PathVariable Long count){
        return productService.setProductCount(productDto, count);
    }

}

