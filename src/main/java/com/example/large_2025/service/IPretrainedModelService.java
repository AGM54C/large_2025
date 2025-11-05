package com.example.large_2025.service;

import com.example.large_2025.pojo.dto.PretrainedModelDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IPretrainedModelService {

    /**
     * 上传模型文件
     * @param modelName 模型名称
     * @param modelPartition 模型分区
     * @param file 上传的文件
     * @return 保存后的模型信息
     */
    PretrainedModelDto uploadModel(String modelName, String modelPartition, String partititionUsage,String usageDescription,MultipartFile file);

    /**
     * 根据ID查找模型
     * @param modelId 模型ID
     * @return 模型信息
     */
    PretrainedModelDto findById(Integer modelId);

    /**
     * 根据模型名称查找模型
     * @param modelName 模型名称
     * @return 模型信息
     */
    PretrainedModelDto findByModelName(String modelName);

    /**
     * 查询所有模型
     * @return 所有模型列表
     */
    List<PretrainedModelDto> findAll();

    /**
     * 根据分区查询模型列表
     * @param partition 分区名称
     * @return 模型列表
     */
    List<PretrainedModelDto> findByPartition(String partition);

    /**
     * 根据分区与模块查询模型列表
     * @param partition
     * @param usage
     * @return 模型列表
     */
    List<PretrainedModelDto> findByUsage(String partition,String usage);

    /**
     * 根据分区与模块与作用查询模型列表
     * @param partition
     * @param usage
     * @param description
     * @return 模型列表
     */
    List<PretrainedModelDto> findByDescription(String partition,String usage,String description);

    /**
     * 删除模型（包括文件和数据库记录）
     * @param modelId 模型ID
     * @return 是否删除成功
     */
    boolean deleteModel(Integer modelId);

    /**
     * 根据模型名称删除模型
     * @param modelName 模型名称
     * @return 是否删除成功
     */
    boolean deleteByModelName(String modelName);

    /**
     * 更新模型信息
     * @param modelId 模型ID
     * @param dto 更新的模型信息
     * @return 更新后的模型信息
     */
    PretrainedModelDto updateModel(Integer modelId, PretrainedModelDto dto);

    /**
     * 搜索模型
     * @param keyword 搜索关键词
     * @param limit 返回结果数量限制
     * @return 模型列表
     */
    List<PretrainedModelDto> searchModels(String keyword, int limit);

    /**
     * 批量删除模型
     * @param modelIds 模型ID列表
     * @return 是否删除成功
     */
    boolean batchDeleteModels(List<Integer> modelIds);

    /**
     * 统计模型总数
     * @return 模型总数
     */
    int countModels();

    /**
     * 统计指定分区的模型数量
     * @param partition 分区名称
     * @return 模型数量
     */
    int countByPartition(String partition);
}