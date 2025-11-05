package com.example.large_2025.pojo.dto;

import java.io.Serializable;

public class PretrainedModelDto implements Serializable {

    private Integer modelId;
    private String modelName;
    private String modelPartition;
    private String partititionUsage;
    private String usageDescription;
    private String modelFilePath;
    private Long updateTimestamp;

    public PretrainedModelDto() {
    }

    public PretrainedModelDto(Integer modelId, String modelName, String modelPartition,String partititionUsage,String usageDescription,
                              String modelFilePath, Long updateTimestamp) {
        this.modelId = modelId;
        this.modelName = modelName;
        this.modelPartition = modelPartition;
        this.partititionUsage = partititionUsage;
        this.usageDescription = usageDescription;
        this.modelFilePath = modelFilePath;
        this.updateTimestamp = updateTimestamp;
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

    public String getPartititionUsage() {return partititionUsage;}

    public void setPartititionUsage(String partititionUsage) {this.partititionUsage = partititionUsage;}

    public String getUsageDescription() {return usageDescription;}

    public void setUsageDescription(String usageDescription) {this.usageDescription = usageDescription;}

    public String getModelFilePath() {return modelFilePath;}

    public void setModelFilePath(String modelFilePath) {this.modelFilePath = modelFilePath;}

    public Long getUpdateTimestamp() {return updateTimestamp;}

    public void setUpdateTimestamp(Long updateTimestamp) {this.updateTimestamp = updateTimestamp;}

    @Override
    public String toString() {
        return "PretrainedModelDto{" +
                "modelId=" + modelId +
                ", modelName='" + modelName + '\'' +
                ", modelPartition='" + modelPartition + '\'' +
                ", partititionUsage='" + partititionUsage + '\'' +
                ", usageDescription='" + usageDescription + '\'' +
                ", modelFilePath='" + modelFilePath + '\'' +
                ", updateTimestamp=" + updateTimestamp +
                '}';
    }
}