package com.baitap.demo.controller;

import com.baitap.demo.entity.Category;
import com.baitap.demo.entity.Product;
import com.baitap.demo.entity.User;
import com.baitap.demo.repository.CategoryRepository;
import com.baitap.demo.repository.ProductRepository;
import com.baitap.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @QueryMapping
    public List<Product> productsSortedByPrice() {
        return productRepository.findAllByOrderByPriceAsc();
    }

    @QueryMapping
    public List<Product> productsByCategory(@Argument Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    @QueryMapping
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @QueryMapping
    public Product getProduct(@Argument Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @MutationMapping
    public Product createProduct(@Argument String title, @Argument Integer quantity, @Argument String description,
            @Argument Double price, @Argument Long categoryId, @Argument Long userId) {
        Product product = new Product();
        product.setTitle(title);
        product.setQuantity(quantity);
        product.setDescription(description);
        product.setPrice(price);

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        product.setCategory(category);
        product.setUser(user);

        return productRepository.save(product);
    }

    @MutationMapping
    public Product updateProduct(@Argument Long id, @Argument String title, @Argument Integer quantity,
            @Argument String description, @Argument Double price, @Argument Long categoryId, @Argument Long userId) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        if (title != null)
            product.setTitle(title);
        if (quantity != null)
            product.setQuantity(quantity);
        if (description != null)
            product.setDescription(description);
        if (price != null)
            product.setPrice(price);
        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategory(category);
        }
        if (userId != null) {
            User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
            product.setUser(user);
        }
        return productRepository.save(product);
    }

    @MutationMapping
    public Boolean deleteProduct(@Argument Long id) {
        productRepository.deleteById(id);
        return true;
    }
}