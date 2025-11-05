package com.example.large_2025.mapper;

import com.example.large_2025.pojo.PretrainedModel;
import org.apache.ibatis.annotations.*;

import java.sql.Timestamp;
import java.util.List;

@Mapper
public interface PretrainedModelMapper {

    /**
     * 添加预训练模型
     */
    @Insert("INSERT INTO tab_pretrained_model(model_name, model_partition, model_file_path, update_timestamp) " +
            "VALUES(#{modelName}, #{modelPartition}, #{modelFilePath}, now())")
    @Options(useGeneratedKeys = true, keyProperty = "modelId", keyColumn = "model_id")
    void add(PretrainedModel model);

    /**
     * 根据ID查询预训练模型
     */
    @Select("SELECT * FROM tab_pretrained_model WHERE model_id = #{modelId}")
    PretrainedModel findById(Integer modelId);

    /**
     * 根据模型名称查询
     */
    @Select("SELECT * FROM tab_pretrained_model WHERE model_name = #{modelName}")
    PretrainedModel findByModelName(String modelName);

    /**
     * 查询所有预训练模型
     */
    @Select("SELECT * FROM tab_pretrained_model ORDER BY update_timestamp DESC")
    List<PretrainedModel> findAll();

    /**
     * 根据分区查询模型列表
     */
    @Select("SELECT * FROM tab_pretrained_model WHERE model_partition = #{modelPartition}")
    List<PretrainedModel> findByPartition(String modelPartition);

    /**
     * 根据分区和模块查询模型列表
     * 这个方法对应service中的findByUsage方法
     */
    @Select("SELECT * FROM tab_pretrained_model WHERE model_partition = #{partition} AND partition_usage = #{usage}")
    List<PretrainedModel> findByUsage(@Param("partition") String partition, @Param("usage") String usage);

    /**
     * 根据分区、模块和作用描述查询模型列表
     * 这个方法对应service中的findByDescription方法
     */
    @Select("SELECT * FROM tab_pretrained_model WHERE model_partition = #{partition} " +
            "AND partition_usage = #{usage} AND usage_description = #{description}")
    List<PretrainedModel> findByDescription(@Param("partition") String partition,
                                            @Param("usage") String usage,
                                            @Param("description") String description);

    /**
     * 删除预训练模型
     */
    @Delete("DELETE FROM tab_pretrained_model WHERE model_id = #{modelId}")
    void delete(Integer modelId);

    /**
     * 更新模型名称
     */
    @Update("UPDATE tab_pretrained_model SET model_name = #{modelName}, update_timestamp = now() " +
            "WHERE model_id = #{modelId}")
    void updateModelName(@Param("modelName") String modelName, @Param("modelId") Long modelId);

    /**
     * 更新模型分区
     */
    @Update("UPDATE tab_pretrained_model SET model_partition = #{modelPartition}, update_timestamp = now() " +
            "WHERE model_id = #{modelId}")
    void updateModelPartition(@Param("modelPartition") String modelPartition, @Param("modelId") Long modelId);

    /**
     * 更新模型文件路径
     */
    @Update("UPDATE tab_pretrained_model SET model_file_path = #{modelFilePath}, update_timestamp = now() " +
            "WHERE model_id = #{modelId}")
    void updateModelFilePath(@Param("modelFilePath") String modelFilePath, @Param("modelId") Long modelId);

    /**
     * 全量更新模型信息
     */
    @Update("UPDATE tab_pretrained_model SET " +
            "model_name = #{modelName}, " +
            "model_partition = #{modelPartition}, " +
            "model_file_path = #{modelFilePath}, " +
            "update_timestamp = now() " +
            "WHERE model_id = #{modelId}")
    void update(PretrainedModel model);

    /**
     * 选择性更新模型信息（只更新非空字段）
     */
    @Update("<script>" +
            "UPDATE tab_pretrained_model " +
            "<set>" +
            "<if test='modelName != null and modelName != \"\"'>" +
            "model_name = #{modelName}," +
            "</if>" +
            "<if test='modelPartition != null and modelPartition != \"\"'>" +
            "model_partition = #{modelPartition}," +
            "</if>" +
            "<if test='modelFilePath != null and modelFilePath != \"\"'>" +
            "model_file_path = #{modelFilePath}," +
            "</if>" +
            "update_timestamp = now()" +
            "</set>" +
            "WHERE model_id = #{modelId}" +
            "</script>")
    void updateSelective(PretrainedModel model);

    /**
     * 搜索模型（支持模糊查询）
     */
    @Select("<script>" +
            "SELECT * FROM tab_pretrained_model WHERE 1=1 " +
            "<if test='keyword != null and keyword != \"\"'>" +
            "AND (model_name LIKE CONCAT('%', #{keyword}, '%') " +
            "OR model_partition LIKE CONCAT('%', #{keyword}, '%')) " +
            "</if>" +
            "ORDER BY update_timestamp DESC " +
            "LIMIT #{limit}" +
            "</script>")
    List<PretrainedModel> searchModels(@Param("keyword") String keyword, @Param("limit") int limit);

    /**
     * 批量删除模型
     */
    @Delete("<script>" +
            "DELETE FROM tab_pretrained_model WHERE model_id IN " +
            "<foreach collection='modelIds' item='id' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "</script>")
    void batchDelete(@Param("modelIds") List<Integer> modelIds);

    /**
     * 统计模型总数
     */
    @Select("SELECT COUNT(*) FROM tab_pretrained_model")
    int count();

    /**
     * 根据分区统计模型数量
     */
    @Select("SELECT COUNT(*) FROM tab_pretrained_model WHERE model_partition = #{modelPartition}")
    int countByPartition(String modelPartition);
}