package com.app.validator;


import com.app.dto.ProductDto;
import com.app.validator.generic.AbstractValidator;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class CreateProductValidator extends AbstractValidator<ProductDto> {

  //  @Autowired
   // private ProductRepository productRepository;

    @Override
    public Map<String, String> validate(ProductDto createProductDto) {
        errors.clear();

        if (!isNameValid(createProductDto.getName())) {
            errors.put("name", "doesn't match expected regex");
        }

        if (Objects.isNull(createProductDto.getPrice())) {
            errors.put("price", "price value is null");
        }

        if (Objects.isNull(createProductDto.getDiscount())) {
            errors.put("discount", "discount value is null");
        }

    //    Optional<Product> byName = productRepository.findByName(createProductDto.getName());
//
//        if (byName.isPresent()) {
//            errors.put("product", "A product with this name already exists");
//        }
        return errors;
    }

    private boolean isNameValid(String name) {
        return name != null && name.matches("[A-Z ]+");
    }

}
