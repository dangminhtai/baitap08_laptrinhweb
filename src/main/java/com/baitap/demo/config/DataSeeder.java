package com.baitap.demo.config;

import com.baitap.demo.entity.Category;
import com.baitap.demo.entity.Product;
import com.baitap.demo.entity.User;
import com.baitap.demo.repository.CategoryRepository;
import com.baitap.demo.repository.ProductRepository;
import com.baitap.demo.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class DataSeeder implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public DataSeeder(CategoryRepository categoryRepository, UserRepository userRepository,
            ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (categoryRepository.count() == 0) {
            // Categories
            Category c1 = new Category();
            c1.setName("Electronics");
            c1.setImages("electronics.jpg");

            Category c2 = new Category();
            c2.setName("Books");
            c2.setImages("books.jpg");

            categoryRepository.saveAll(Arrays.asList(c1, c2));

            // Users
            User u1 = new User();
            u1.setFullname("Nguyen Van A");
            u1.setEmail("a@example.com");
            u1.setPassword("123456");
            u1.setPhone("0901234567");
            u1.setCategories(Arrays.asList(c1, c2)); // Interested in both

            User u2 = new User();
            u2.setFullname("Tran Thi B");
            u2.setEmail("b@example.com");
            u2.setPassword("password");
            u2.setPhone("0909876543");
            u2.setCategories(Arrays.asList(c2)); // Interested in Books only

            userRepository.saveAll(Arrays.asList(u1, u2));

            // Products
            Product p1 = new Product();
            p1.setTitle("iPhone 15");
            p1.setQuantity(10);
            p1.setDescription("Latest iPhone");
            p1.setPrice(999.99);
            p1.setCategory(c1);
            p1.setUser(u1);

            Product p2 = new Product();
            p2.setTitle("Spring Boot in Action");
            p2.setQuantity(5);
            p2.setDescription("Great book for Java devs");
            p2.setPrice(45.00);
            p2.setCategory(c2);
            p2.setUser(u2);

            Product p3 = new Product();
            p3.setTitle("Laptop Dell");
            p3.setQuantity(3);
            p3.setDescription("Powerful laptop");
            p3.setPrice(1200.00);
            p3.setCategory(c1);
            p3.setUser(u1);

            productRepository.saveAll(Arrays.asList(p1, p2, p3));

            System.out.println("Sample data seeded successfully!");
        }
    }
}
