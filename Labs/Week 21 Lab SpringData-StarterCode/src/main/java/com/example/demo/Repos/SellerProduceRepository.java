package com.example.demo.Repos;

import com.example.demo.Models.SellerProduce;
import com.example.demo.Models.User;
import com.example.demo.Models.Produce;
import org.springframework.data.repository.CrudRepository;
import java.util.List;
import java.util.Optional;

public interface SellerProduceRepository extends CrudRepository<SellerProduce, Long> {
    Optional<SellerProduce> findBySellerAndProduce(User seller, Produce produce);
    List<SellerProduce> findBySeller(User seller);
}