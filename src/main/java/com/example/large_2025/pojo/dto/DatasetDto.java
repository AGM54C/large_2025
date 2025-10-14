package com.example.large_2025.pojo.dto;

import java.io.Serializable;

public class DatasetDto implements Serializable {

    private Integer datasetId;
    private String datasetCategory;
    private String datasetName;
    private String datasetFilePath;
    private Long updateTimestamp;

    public DatasetDto() {
    }

    public DatasetDto(Integer datasetId, String datasetCategory, String datasetName,
                      String datasetFilePath, Long updateTimestamp) {
        this.datasetId = datasetId;
        this.datasetCategory = datasetCategory;
        this.datasetName = datasetName;
        this.datasetFilePath = datasetFilePath;
        this.updateTimestamp = updateTimestamp;
    }

    // Getters and Setters
    public Integer getDatasetId() {
        return datasetId;
    }

    public void setDatasetId(Integer datasetId) {
        this.datasetId = datasetId;
    }

    public String getDatasetCategory() {
        return datasetCategory;
    }

    public void setDatasetCategory(String datasetCategory) {
        this.datasetCategory = datasetCategory;
    }

    public String getDatasetName() {
        return datasetName;
    }

    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }

    public String getDatasetFilePath() {
        return datasetFilePath;
    }

    public void setDatasetFilePath(String datasetFilePath) {
        this.datasetFilePath = datasetFilePath;
    }

    public Long getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Long updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    @Override
    public String toString() {
        return "DatasetDto{" +
                "datasetId=" + datasetId +
                ", datasetCategory='" + datasetCategory + '\'' +
                ", datasetName='" + datasetName + '\'' +
                ", datasetFilePath='" + datasetFilePath + '\'' +
                ", updateTimestamp=" + updateTimestamp +
                '}';
    }
}