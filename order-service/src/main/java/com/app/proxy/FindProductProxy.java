package com.app.proxy;

import com.app.dto.GetProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "product-service")
public interface FindProductProxy {
//    @GetMapping("/find/product/{name}")
//    GetProductDto findByProduct(@PathVariable String name);
//
    @GetMapping("products/find/id/{id}")
    GetProductDto findById(@PathVariable Long id);

//    @GetMapping("products/find/{name}")
//    GetProductDto findByName(@PathVariable String name);

    @GetMapping("products/setCount/{count}")
    GetProductDto setProductCount(@RequestBody GetProductDto productDto, @PathVariable Long count);

}
