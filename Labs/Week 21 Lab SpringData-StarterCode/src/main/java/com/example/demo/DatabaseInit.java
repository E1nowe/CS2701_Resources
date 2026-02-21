package com.example.demo;

import com.example.demo.Models.*;
import com.example.demo.Repos.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DatabaseInit implements CommandLineRunner {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ProduceRepository produceRepository;
    private final SellerProduceRepository sellerProduceRepository;
    private final OrderItemRepository orderItemRepository;

    // Constructor injection
    public DatabaseInit(
        UserRepository userRepository,
        OrderRepository orderRepository,
        ProduceRepository produceRepository,
        SellerProduceRepository sellerProduceRepository,
        OrderItemRepository orderItemRepository) {
    this.userRepository = userRepository;
    this.orderRepository = orderRepository;
    this.produceRepository = produceRepository;
    this.sellerProduceRepository = sellerProduceRepository;
    this.orderItemRepository = orderItemRepository;
}

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        System.out.println("=== Starting Database Initialization ===");

        // Clear existing data (optional - helps avoid duplicate errors)
        clearExistingData();

        // Step 1: Create and save users
        createUsers();

        // Step 2: Create and save produce types
        createProduce();

        // Step 3: Add selling/stock information
        createSellerProduce();

        // Step 4: Create and save orders
        createOrders();

        System.out.println("=== Database Initialization Complete ===");
    }

    private void clearExistingData() {
        System.out.println("Clearing existing data...");
        orderItemRepository.deleteAll();
        orderRepository.deleteAll();
        sellerProduceRepository.deleteAll();
        produceRepository.deleteAll();
        userRepository.deleteAll();
    }

    private void createUsers() {
        System.out.println("Creating users...");

        // Bob (buyer)
        User bob = new User("Bob", "buyer");
        userRepository.save(bob);
        System.out.println("Saved Bob (buyer)");

        // Prapanch (seller)
        User prapanch = new User("Prapanch", "seller");
        userRepository.save(prapanch);
        System.out.println("Saved Prapanch (seller)");

        // Ademola (both)
        User ademola = new User("Ademola", "both");
        userRepository.save(ademola);
        System.out.println("Saved Ademola (both)");

        // Zhixian (buyer)
        User zhixian = new User("Zhixian", "buyer");
        userRepository.save(zhixian);
        System.out.println("Saved Zhixian (buyer)");
    }

    private void createProduce() {
        System.out.println("Creating produce...");

        // Create three produce types
        Produce apple = new Produce("Apple");
        produceRepository.save(apple);
        System.out.println("Saved Apple");

        Produce lettuce = new Produce("Lettuce");
        produceRepository.save(lettuce);
        System.out.println("Saved Lettuce");

        Produce potatoes = new Produce("Potatoes");
        produceRepository.save(potatoes);
        System.out.println("Saved Potatoes");
    }

    private void createSellerProduce() {
        System.out.println("Creating seller produce...");

        // Find users
        User prapanch = userRepository.findByName("Prapanch")
            .orElseThrow(() -> new RuntimeException("Prapanch not found"));
        User ademola = userRepository.findByName("Ademola")
            .orElseThrow(() -> new RuntimeException("Ademola not found"));

        // Find produce
        Produce apple = produceRepository.findByName("Apple")
            .orElseThrow(() -> new RuntimeException("Apple not found"));
        Produce lettuce = produceRepository.findByName("Lettuce")
            .orElseThrow(() -> new RuntimeException("Lettuce not found"));
        Produce potatoes = produceRepository.findByName("Potatoes")
            .orElseThrow(() -> new RuntimeException("Potatoes not found"));

        // Prapanch sells apples for £0.15 (stock 100)
        SellerProduce prapanchApple = new SellerProduce(prapanch, apple, 0.15, 100);
        sellerProduceRepository.save(prapanchApple);
        System.out.println("Prapanch: Apples @ £0.15, stock 100");

        // Prapanch sells lettuce for £0.25 (stock 20)
        SellerProduce prapanchLettuce = new SellerProduce(prapanch, lettuce, 0.25, 20);
        sellerProduceRepository.save(prapanchLettuce);
        System.out.println("Prapanch: Lettuce @ £0.25, stock 20");

        // Ademola sells apples for £0.30 (stock 50)
        SellerProduce ademolaApple = new SellerProduce(ademola, apple, 0.30, 50);
        sellerProduceRepository.save(ademolaApple);
        System.out.println("Ademola: Apples @ £0.30, stock 50");

        // Ademola sells potatoes for £0.05 (stock 30)
        SellerProduce ademolaPotatoes = new SellerProduce(ademola, potatoes, 0.05, 30);
        sellerProduceRepository.save(ademolaPotatoes);
        System.out.println("Ademola: Potatoes @ £0.05, stock 30");
    }

    private void createOrders() {
        System.out.println("Creating orders...");

        // Find users
        User bob = userRepository.findByName("Bob")
            .orElseThrow(() -> new RuntimeException("Bob not found"));
        User zhixian = userRepository.findByName("Zhixian")
            .orElseThrow(() -> new RuntimeException("Zhixian not found"));
        User prapanch = userRepository.findByName("Prapanch")
            .orElseThrow(() -> new RuntimeException("Prapanch not found"));
        User ademola = userRepository.findByName("Ademola")
            .orElseThrow(() -> new RuntimeException("Ademola not found"));
        
        // Find produce
        Produce apple = produceRepository.findByName("Apple")
            .orElseThrow(() -> new RuntimeException("Apple not found"));
        Produce lettuce = produceRepository.findByName("Lettuce")
            .orElseThrow(() -> new RuntimeException("Lettuce not found"));
        Produce potatoes = produceRepository.findByName("Potatoes")
            .orElseThrow(() -> new RuntimeException("Potatoes not found"));

        // Find seller produce
        SellerProduce ademolaApple = sellerProduceRepository.findBySellerAndProduce(ademola, apple)
            .orElseThrow(() -> new RuntimeException("Ademola's apples not found"));
        SellerProduce prapanchLettuce = sellerProduceRepository.findBySellerAndProduce(prapanch, lettuce)
            .orElseThrow(() -> new RuntimeException("Prapanch's lettuce not found"));
        SellerProduce prapanchApple = sellerProduceRepository.findBySellerAndProduce(prapanch, apple)
            .orElseThrow(() -> new RuntimeException("Prapanch's apples not found"));
        SellerProduce ademolaPotatoes = sellerProduceRepository.findBySellerAndProduce(ademola, potatoes)
            .orElseThrow(() -> new RuntimeException("Ademola's potatoes not found"));

        // Bob's order: 2 apples from Ademola, 1 lettuce from Prapanch
        Order bobsOrder = new Order(bob);
        orderRepository.save(bobsOrder);
        
        OrderItem item1 = new OrderItem(bobsOrder, ademolaApple, 2, ademolaApple.getPrice());
        orderItemRepository.save(item1);
        
        OrderItem item2 = new OrderItem(bobsOrder, prapanchLettuce, 1, prapanchLettuce.getPrice());
        orderItemRepository.save(item2);
        
        bobsOrder.getOrderItems().add(item1);
        bobsOrder.getOrderItems().add(item2);
        orderRepository.save(bobsOrder);
        
        System.out.println("Bob's order created: 2 Ademola apples, 1 Prapanch lettuce");

        // Zhixian's order: 10 apples from Prapanch, 15 potatoes from Ademola
        Order zhixianOrder = new Order(zhixian);
        orderRepository.save(zhixianOrder);
        
        OrderItem item3 = new OrderItem(zhixianOrder, prapanchApple, 10, prapanchApple.getPrice());
        orderItemRepository.save(item3);
        
        OrderItem item4 = new OrderItem(zhixianOrder, ademolaPotatoes, 15, ademolaPotatoes.getPrice());
        orderItemRepository.save(item4);
        
        zhixianOrder.getOrderItems().add(item3);
        zhixianOrder.getOrderItems().add(item4);
        orderRepository.save(zhixianOrder);
        
        System.out.println("Zhixian's order created: 10 Prapanch apples, 15 Ademola potatoes");
    }
}