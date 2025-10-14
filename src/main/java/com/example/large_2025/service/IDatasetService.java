package com.example.large_2025.service;

import com.example.large_2025.pojo.dto.DatasetDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IDatasetService {

    /**
     * 上传数据集文件
     * @param datasetName 数据集名称
     * @param datasetCategory 数据集分类（NLP、CV、Audio等）
     * @param file 上传的文件
     * @return 保存后的数据集信息
     */
    DatasetDto uploadDataset(String datasetName, String datasetCategory, MultipartFile file);

    /**
     * 根据ID查找数据集
     * @param datasetId 数据集ID
     * @return 数据集信息
     */
    DatasetDto findById(Integer datasetId);

    /**
     * 根据数据集名称查找数据集
     * @param datasetName 数据集名称
     * @return 数据集信息
     */
    DatasetDto findByDatasetName(String datasetName);

    /**
     * 查询所有数据集
     * @return 所有数据集列表
     */
    List<DatasetDto> findAll();

    /**
     * 根据分类查询数据集列表
     * @param category 分类名称
     * @return 数据集列表
     */
    List<DatasetDto> findByCategory(String category);

    /**
     * 删除数据集（包括文件和数据库记录）
     * @param datasetId 数据集ID
     * @return 是否删除成功
     */
    boolean deleteDataset(Integer datasetId);

    /**
     * 根据数据集名称删除数据集
     * @param datasetName 数据集名称
     * @return 是否删除成功
     */
    boolean deleteByDatasetName(String datasetName);

    /**
     * 更新数据集信息
     * @param datasetId 数据集ID
     * @param dto 更新的数据集信息
     * @return 更新后的数据集信息
     */
    DatasetDto updateDataset(Integer datasetId, DatasetDto dto);

    /**
     * 搜索数据集
     * @param keyword 搜索关键词
     * @param limit 返回结果数量限制
     * @return 数据集列表
     */
    List<DatasetDto> searchDatasets(String keyword, int limit);

    /**
     * 批量删除数据集
     * @param datasetIds 数据集ID列表
     * @return 是否删除成功
     */
    boolean batchDeleteDatasets(List<Integer> datasetIds);

    /**
     * 统计数据集总数
     * @return 数据集总数
     */
    int countDatasets();

    /**
     * 统计指定分类的数据集数量
     * @param category 分类名称
     * @return 数据集数量
     */
    int countByCategory(String category);
}