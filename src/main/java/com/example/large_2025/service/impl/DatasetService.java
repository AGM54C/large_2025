package com.example.large_2025.service.impl;

import com.example.large_2025.pojo.dto.DatasetDto;
import com.example.large_2025.mapper.DatasetMapper;
import com.example.large_2025.pojo.Dataset;
import com.example.large_2025.service.IDatasetService;
import com.example.large_2025.util.ConvertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class DatasetService implements IDatasetService {

    private static final Logger logger = LoggerFactory.getLogger(DatasetService.class);

    private static final String DATASET_DIR = "dataset";

    @Autowired
    private DatasetMapper datasetMapper;

    @Autowired
    private ConvertUtil convertUtil;

    @Value("${file.upload.path}")
    private String uploadPath;

    @Override
    @Transactional
    public DatasetDto uploadDataset(String datasetName, String datasetCategory, MultipartFile file) {
        logger.info("开始上传数据集: {}, 分类: {}, 文件名: {}", datasetName, datasetCategory, file.getOriginalFilename());

        // 检查数据集名称是否已存在
        Dataset existingDataset = datasetMapper.findByDatasetName(datasetName);
        if (existingDataset != null) {
            throw new RuntimeException("数据集名称已存在: " + datasetName);
        }

        // 验证文件
        if (file.isEmpty()) {
            throw new RuntimeException("上传文件为空");
        }

        try {
            // 创建存储目录: uploadPath/dataset/datasetCategory
            Path uploadDir = Paths.get(uploadPath, DATASET_DIR, datasetCategory);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
                logger.info("创建目录: {}", uploadDir);
            }

            // 生成文件名（使用原始文件名）
            String originalFilename = file.getOriginalFilename();
            Path filePath = uploadDir.resolve(originalFilename);

            // 如果文件已存在，添加时间戳避免冲突
            if (Files.exists(filePath)) {
                String timestamp = String.valueOf(System.currentTimeMillis());
                String nameWithoutExt = originalFilename.substring(0, originalFilename.lastIndexOf('.'));
                String extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
                originalFilename = nameWithoutExt + "_" + timestamp + extension;
                filePath = uploadDir.resolve(originalFilename);
            }

            // 保存文件
            file.transferTo(filePath.toFile());
            logger.info("文件保存成功: {}", filePath);

            // 保存数据库记录
            Dataset dataset = new Dataset();
            dataset.setDatasetName(datasetName);
            dataset.setDatasetCategory(datasetCategory);
            dataset.setDatasetFilePath(filePath.toString());

            datasetMapper.add(dataset);
            logger.info("数据库记录保存成功，数据集ID: {}", dataset.getDatasetId());

            return convertUtil.DatasettoDto(dataset);

        } catch (IOException e) {
            logger.error("文件上传失败", e);
            throw new RuntimeException("文件上传失败: " + e.getMessage(), e);
        }
    }

    @Override
    public DatasetDto findById(Integer datasetId) {
        logger.info("查找数据集ID: {}", datasetId);
        Dataset dataset = datasetMapper.findById(datasetId);
        return convertUtil.DatasettoDto(dataset);
    }

    @Override
    public DatasetDto findByDatasetName(String datasetName) {
        logger.info("查找数据集名称: {}", datasetName);
        Dataset dataset = datasetMapper.findByDatasetName(datasetName);
        return convertUtil.DatasettoDto(dataset);
    }

    @Override
    public List<DatasetDto> findAll() {
        logger.info("查询所有数据集");
        List<Dataset> datasets = datasetMapper.findAll();
        return convertUtil.DatasetListtoDtoList(datasets);
    }

    @Override
    public List<DatasetDto> findByCategory(String category) {
        logger.info("查询分类的数据集: {}", category);
        List<Dataset> datasets = datasetMapper.findByCategory(category);
        return convertUtil.DatasetListtoDtoList(datasets);
    }

    @Override
    @Transactional
    public boolean deleteDataset(Integer datasetId) {
        logger.info("删除数据集ID: {}", datasetId);

        Dataset dataset = datasetMapper.findById(datasetId);
        if (dataset == null) {
            logger.warn("数据集不存在: {}", datasetId);
            return false;
        }

        // 删除文件
        try {
            Path filePath = Paths.get(dataset.getDatasetFilePath());
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                logger.info("文件删除成功: {}", filePath);
            } else {
                logger.warn("文件不存在: {}", filePath);
            }
        } catch (IOException e) {
            logger.error("文件删除失败: {}", dataset.getDatasetFilePath(), e);
            // 继续删除数据库记录
        }

        // 删除数据库记录
        datasetMapper.delete(datasetId);
        logger.info("数据库记录删除成功: {}", datasetId);

        return true;
    }

    @Override
    @Transactional
    public boolean deleteByDatasetName(String datasetName) {
        logger.info("根据名称删除数据集: {}", datasetName);

        Dataset dataset = datasetMapper.findByDatasetName(datasetName);
        if (dataset == null) {
            logger.warn("数据集不存在: {}", datasetName);
            return false;
        }

        return deleteDataset(dataset.getDatasetId());
    }

    @Override
    @Transactional
    public DatasetDto updateDataset(Integer datasetId, DatasetDto dto) {
        logger.info("更新数据集ID: {}", datasetId);

        Dataset dataset = datasetMapper.findById(datasetId);
        if (dataset == null) {
            throw new RuntimeException("数据集不存在: " + datasetId);
        }

        // 如果要更新数据集名称，检查是否重复
        if (dto.getDatasetName() != null && !dto.getDatasetName().equals(dataset.getDatasetName())) {
            Dataset existingDataset = datasetMapper.findByDatasetName(dto.getDatasetName());
            if (existingDataset != null) {
                throw new RuntimeException("数据集名称已存在: " + dto.getDatasetName());
            }
        }

        // 更新实体字段
        if (dto.getDatasetName() != null) {
            dataset.setDatasetName(dto.getDatasetName());
        }
        if (dto.getDatasetCategory() != null) {
            dataset.setDatasetCategory(dto.getDatasetCategory());
        }
        if (dto.getDatasetFilePath() != null) {
            dataset.setDatasetFilePath(dto.getDatasetFilePath());
        }

        // 使用选择性更新
        datasetMapper.updateSelective(dataset);
        logger.info("数据集更新成功: {}", dataset);

        // 重新查询获取最新数据
        Dataset updatedDataset = datasetMapper.findById(datasetId);
        return convertUtil.DatasettoDto(updatedDataset);
    }

    @Override
    public List<DatasetDto> searchDatasets(String keyword, int limit) {
        logger.info("搜索数据集，关键词: {}, 限制: {}", keyword, limit);
        List<Dataset> datasets = datasetMapper.searchDatasets(keyword, limit);
        return convertUtil.DatasetListtoDtoList(datasets);
    }

    @Override
    @Transactional
    public boolean batchDeleteDatasets(List<Integer> datasetIds) {
        logger.info("批量删除数据集，ID列表: {}", datasetIds);

        if (datasetIds == null || datasetIds.isEmpty()) {
            logger.warn("数据集ID列表为空");
            return false;
        }

        // 先删除所有相关文件
        for (Integer datasetId : datasetIds) {
            Dataset dataset = datasetMapper.findById(datasetId);
            if (dataset != null) {
                try {
                    Path filePath = Paths.get(dataset.getDatasetFilePath());
                    if (Files.exists(filePath)) {
                        Files.delete(filePath);
                        logger.info("文件删除成功: {}", filePath);
                    }
                } catch (IOException e) {
                    logger.error("文件删除失败: {}", dataset.getDatasetFilePath(), e);
                }
            }
        }

        // 批量删除数据库记录
        datasetMapper.batchDelete(datasetIds);
        logger.info("批量删除完成");

        return true;
    }

    @Override
    public int countDatasets() {
        logger.info("统计数据集总数");
        return datasetMapper.count();
    }

    @Override
    public int countByCategory(String category) {
        logger.info("统计分类数据集数量: {}", category);
        return datasetMapper.countByCategory(category);
    }
}