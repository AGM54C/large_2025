package com.example.large_2025.mapper;

import com.example.large_2025.pojo.Dataset;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DatasetMapper {

    /**
     * 添加数据集
     */
    @Insert("INSERT INTO tab_dataset(dataset_name, dataset_category, dataset_file_path, update_timestamp) " +
            "VALUES(#{datasetName}, #{datasetCategory}, #{datasetFilePath}, now())")
    @Options(useGeneratedKeys = true, keyProperty = "datasetId", keyColumn = "dataset_id")
    void add(Dataset dataset);

    /**
     * 根据ID查询数据集
     */
    @Select("SELECT * FROM tab_dataset WHERE dataset_id = #{datasetId}")
    Dataset findById(Integer datasetId);

    /**
     * 根据数据集名称查询
     */
    @Select("SELECT * FROM tab_dataset WHERE dataset_name = #{datasetName}")
    Dataset findByDatasetName(String datasetName);

    /**
     * 查询所有数据集
     */
    @Select("SELECT * FROM tab_dataset ORDER BY update_timestamp DESC")
    List<Dataset> findAll();

    /**
     * 根据分类查询数据集列表
     */
    @Select("SELECT * FROM tab_dataset WHERE dataset_category = #{datasetCategory}")
    List<Dataset> findByCategory(String datasetCategory);

    /**
     * 删除数据集
     */
    @Delete("DELETE FROM tab_dataset WHERE dataset_id = #{datasetId}")
    void delete(Integer datasetId);

    /**
     * 更新数据集名称
     */
    @Update("UPDATE tab_dataset SET dataset_name = #{datasetName}, update_timestamp = now() " +
            "WHERE dataset_id = #{datasetId}")
    void updateDatasetName(@Param("datasetName") String datasetName, @Param("datasetId") Long datasetId);

    /**
     * 更新数据集分类
     */
    @Update("UPDATE tab_dataset SET dataset_category = #{datasetCategory}, update_timestamp = now() " +
            "WHERE dataset_id = #{datasetId}")
    void updateDatasetCategory(@Param("datasetCategory") String datasetCategory, @Param("datasetId") Long datasetId);

    /**
     * 更新数据集文件路径
     */
    @Update("UPDATE tab_dataset SET dataset_file_path = #{datasetFilePath}, update_timestamp = now() " +
            "WHERE dataset_id = #{datasetId}")
    void updateDatasetFilePath(@Param("datasetFilePath") String datasetFilePath, @Param("datasetId") Long datasetId);

    /**
     * 全量更新数据集信息
     */
    @Update("UPDATE tab_dataset SET " +
            "dataset_name = #{datasetName}, " +
            "dataset_category = #{datasetCategory}, " +
            "dataset_file_path = #{datasetFilePath}, " +
            "update_timestamp = now() " +
            "WHERE dataset_id = #{datasetId}")
    void update(Dataset dataset);

    /**
     * 选择性更新数据集信息（只更新非空字段）
     */
    @Update("<script>" +
            "UPDATE tab_dataset " +
            "<set>" +
            "<if test='datasetName != null and datasetName != \"\"'>" +
            "dataset_name = #{datasetName}," +
            "</if>" +
            "<if test='datasetCategory != null and datasetCategory != \"\"'>" +
            "dataset_category = #{datasetCategory}," +
            "</if>" +
            "<if test='datasetFilePath != null and datasetFilePath != \"\"'>" +
            "dataset_file_path = #{datasetFilePath}," +
            "</if>" +
            "update_timestamp = now()" +
            "</set>" +
            "WHERE dataset_id = #{datasetId}" +
            "</script>")
    void updateSelective(Dataset dataset);

    /**
     * 搜索数据集（支持模糊查询）
     */
    @Select("<script>" +
            "SELECT * FROM tab_dataset WHERE 1=1 " +
            "<if test='keyword != null and keyword != \"\"'>" +
            "AND (dataset_name LIKE CONCAT('%', #{keyword}, '%') " +
            "OR dataset_category LIKE CONCAT('%', #{keyword}, '%')) " +
            "</if>" +
            "ORDER BY update_timestamp DESC " +
            "LIMIT #{limit}" +
            "</script>")
    List<Dataset> searchDatasets(@Param("keyword") String keyword, @Param("limit") int limit);

    /**
     * 批量删除数据集
     */
    @Delete("<script>" +
            "DELETE FROM tab_dataset WHERE dataset_id IN " +
            "<foreach collection='datasetIds' item='id' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "</script>")
    void batchDelete(@Param("datasetIds") List<Integer> datasetIds);

    /**
     * 统计数据集总数
     */
    @Select("SELECT COUNT(*) FROM tab_dataset")
    int count();

    /**
     * 根据分类统计数据集数量
     */
    @Select("SELECT COUNT(*) FROM tab_dataset WHERE dataset_category = #{datasetCategory}")
    int countByCategory(String datasetCategory);
}