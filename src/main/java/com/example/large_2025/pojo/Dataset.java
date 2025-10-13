package com.example.large_2025.pojo;

import jakarta.persistence.*;

@Entity
@Table(name = "tab_dataset")
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

    /**
     * 数据集文件的存储路径
     */
    @Column(name = "dataset_file_path", nullable = false, length = 255)
    private String datasetFilePath;

    /**
     * 数据集更新时间戳
     */
    @Column(name = "update_timestamp", nullable = false)
    private Long updateTimestamp;

    // 无参构造函数（JPA要求）
    public Dataset() {
    }

    // 有参构造函数（方便使用）
    public Dataset(String datasetCategory, String datasetName, String datasetFilePath) {
        this.datasetCategory = datasetCategory;
        this.datasetName = datasetName;
        this.datasetFilePath = datasetFilePath;
    }

    // JPA回调方法，在实体被持久化之前自动设置时间戳
    @PrePersist  // ✓ 新增：插入时也要设置时间戳
    public void prePersist() {
        this.updateTimestamp = System.currentTimeMillis();
    }

    // JPA回调方法，在实体被更新时自动设置更新时间戳
    @PreUpdate
    public void preUpdate() {
        this.updateTimestamp = System.currentTimeMillis();
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
        return "Dataset{" +
                "datasetId=" + datasetId +
                ", datasetCategory='" + datasetCategory + '\'' +
                ", datasetName='" + datasetName + '\'' +
                ", datasetFilePath='" + datasetFilePath + '\'' +
                ", updateTimestamp=" + updateTimestamp +
                '}';
    }
}