package com.example.large_2025.pojo;

import java.util.Date;
import java.util.List;
import jakarta.persistence.*;

public class Dataset {
    /**
     * 数据集ID，自增主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dataset_id")
    private Integer datasetId;

    /**
     * 数据集分类，分为NLP类，CV类，Audio类，与预训练模型分区对应
     */
    @Column(name = "dataset_category", nullable = false, length = 50)
    private String datasetCategory;

    /**
     * 数据集名称
     */
    @Column(name = "dataset_name", nullable = false, unique = true, length = 100)
    private String datasetName;
}