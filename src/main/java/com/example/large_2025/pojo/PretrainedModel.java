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

    /**
     * 预训练模型文件的存储路径
     */
    @Column(name = "model_file_path", nullable = false, length = 255)
    private String modelFilePath;

    /**
     * 预训练模型更新时间戳
     */
    @Column(name = "update_timestamp", nullable = false)
    private Long updateTimestamp;

    //JPA回调方法，在实体被更新时自动设置更新时间戳
    @PreUpdate
    public void preUpdate() {
        this.updateTimestamp = System.currentTimeMillis();
    }

    // Getters and Setters
    public Integer getModelId() {
        return modelId;
    }
    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }
    public String getModelName() {
        return modelName;
    }
    public void setModelName(String modelName) {
        this.modelName = modelName;
    }
    public String getModelPartition() {
        return modelPartition;
    }
    public void setModelPartition(String modelPartition) {
        this.modelPartition = modelPartition;
    }
    public String getModelFilePath() {
        return modelFilePath;
    }
    public void setModelFilePath(String modelFilePath) {
        this.modelFilePath = modelFilePath;
    }
    public Long getUpdateTimestamp() {
        return updateTimestamp;
    }
    public void setUpdateTimestamp(Long updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    @Override
    public String toString() {
        return "PretrainedModel{" +
                "modelId=" + modelId +
                ", modelName='" + modelName + '\'' +
                ", modelPartition='" + modelPartition + '\'' +
                ", modelFilePath='" + modelFilePath + '\'' +
                ", updateTimestamp=" + updateTimestamp +
                '}';
    }
}

