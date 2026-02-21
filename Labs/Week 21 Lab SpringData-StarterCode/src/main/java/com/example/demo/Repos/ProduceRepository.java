package com.example.demo.Repos;

import com.example.demo.Models.Produce;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

public interface ProduceRepository extends CrudRepository<Produce, Long> {
    Optional<Produce> findByName(String name);
}