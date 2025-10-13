package com.example.large_2025.pojo;

import jakarta.persistence.*;

@Entity
@Table(name = "tab_pretrained_model")
public class PretrainedModel {
    /**
     * 预训练模型ID，自增主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "model_id")
    private Integer modelId;

    /**
     * 预训练模型名称,是模型加训练数据集的组合，如ResNet50_CIFAR10
     */
    @Column(name = "model_name", nullable = false, unique = true, length = 100)
    private String modelName;

    /**
     * 类脑预训练模型分区，是一个枚举类型，包含NLP类，CV类，Audio类
     */
    @Column(name = "model_partition", nullable = false, length = 50)
    private String modelPartition;


}

