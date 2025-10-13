package com.example.large_2025.repository;

import com.example.large_2025.pojo.PretrainedModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PretrainedModelRepository extends JpaRepository<PretrainedModel, Integer> {
    PretrainedModel findByModelName(String modelName);
}