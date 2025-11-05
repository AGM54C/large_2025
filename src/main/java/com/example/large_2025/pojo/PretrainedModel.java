package com.example.large_2025.pojo;

import jakarta.persistence.*;

@Entity
@Table(name = "tab_pretrained_model")
public class PretrainedModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "model_id")
    private Integer modelId;

    @Column(name = "model_name", nullable = false, unique = true, length = 100)
    private String modelName;

    @Column(name = "model_partition", nullable = false, length = 50)
    private String modelPartition;

    @Column(name = "partitition_usage", nullable = true, length = 50)
    private String partititionUsage;

    @Column(name = "usage_description", nullable = true, length = 50)
    private String usageDescription;

    @Column(name = "model_file_path", nullable = false, length = 255)
    private String modelFilePath;

    @Column(name = "update_timestamp", nullable = false)
    private Long updateTimestamp;

    // 无参构造函数
    public PretrainedModel() {
    }

    // 有参构造函数
    public PretrainedModel(String modelName, String modelPartition, String modelFilePath) {
        this.modelName = modelName;
        this.modelPartition = modelPartition;
        this.modelFilePath = modelFilePath;
        this.partititionUsage = partititionUsage;
        this.usageDescription = usageDescription;
    }

    @PrePersist
    public void prePersist() {
        this.updateTimestamp = System.currentTimeMillis();
    }

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

    public String getPartititionUsage() {
        return partititionUsage;
    }

    public void setPartititionUsage(String partititionUsage) {
        this.partititionUsage = partititionUsage;
    }

    public String getUsageDescription() {
        return usageDescription;
    }

    public void setUsageDescription(String usageDescription) {
        this.usageDescription = usageDescription;
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
                ", partitionUsage='" + partititionUsage + '\'' +
                ", UsageDescription='" + usageDescription + '\'' +
                ", modelFilePath='" + modelFilePath + '\'' +
                ", updateTimestamp=" + updateTimestamp +
                '}';
    }
}