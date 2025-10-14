package com.example.large_2025.service.impl;

import com.example.large_2025.pojo.dto.PretrainedModelDto;
import com.example.large_2025.mapper.PretrainedModelMapper;
import com.example.large_2025.pojo.PretrainedModel;
import com.example.large_2025.service.IPretrainedModelService;
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
public class PretrainedModelService implements IPretrainedModelService {

    private static final Logger logger = LoggerFactory.getLogger(PretrainedModelService.class);

    private static final String PRETRAINED_DIR = "pretrained";

    @Autowired
    private PretrainedModelMapper modelMapper;

    @Autowired
    private ConvertUtil convertUtil;

    @Value("${file.upload.path}")
    private String uploadPath;

    @Override
    @Transactional
    public PretrainedModelDto uploadModel(String modelName, String modelPartition, MultipartFile file) {
        logger.info("开始上传模型: {}, 分区: {}, 文件名: {}", modelName, modelPartition, file.getOriginalFilename());

        // 检查模型名称是否已存在
        PretrainedModel existingModel = modelMapper.findByModelName(modelName);
        if (existingModel != null) {
            throw new RuntimeException("模型名称已存在: " + modelName);
        }

        // 验证文件
        if (file.isEmpty()) {
            throw new RuntimeException("上传文件为空");
        }

        try {
            // 创建存储目录: uploadPath/pretrained/modelPartition
            Path uploadDir = Paths.get(uploadPath, PRETRAINED_DIR, modelPartition);
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
            PretrainedModel model = new PretrainedModel();
            model.setModelName(modelName);
            model.setModelPartition(modelPartition);
            model.setModelFilePath(filePath.toString());

            modelMapper.add(model);
            logger.info("数据库记录保存成功，模型ID: {}", model.getModelId());

            return convertUtil.toDto(model);

        } catch (IOException e) {
            logger.error("文件上传失败", e);
            throw new RuntimeException("文件上传失败: " + e.getMessage(), e);
        }
    }

    @Override
    public PretrainedModelDto findById(Integer modelId) {
        logger.info("查找模型ID: {}", modelId);
        PretrainedModel model = modelMapper.findById(modelId);
        return convertUtil.toDto(model);
    }

    @Override
    public PretrainedModelDto findByModelName(String modelName) {
        logger.info("查找模型名称: {}", modelName);
        PretrainedModel model = modelMapper.findByModelName(modelName);
        return convertUtil.toDto(model);
    }

    @Override
    public List<PretrainedModelDto> findAll() {
        logger.info("查询所有模型");
        List<PretrainedModel> models = modelMapper.findAll();
        return convertUtil.toDtoList(models);
    }

    @Override
    public List<PretrainedModelDto> findByPartition(String partition) {
        logger.info("查询分区的模型: {}", partition);
        List<PretrainedModel> models = modelMapper.findByPartition(partition);
        return convertUtil.toDtoList(models);
    }

    @Override
    @Transactional
    public boolean deleteModel(Integer modelId) {
        logger.info("删除模型ID: {}", modelId);

        PretrainedModel model = modelMapper.findById(modelId);
        if (model == null) {
            logger.warn("模型不存在: {}", modelId);
            return false;
        }

        // 删除文件
        try {
            Path filePath = Paths.get(model.getModelFilePath());
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                logger.info("文件删除成功: {}", filePath);
            } else {
                logger.warn("文件不存在: {}", filePath);
            }
        } catch (IOException e) {
            logger.error("文件删除失败: {}", model.getModelFilePath(), e);
            // 继续删除数据库记录
        }

        // 删除数据库记录
        modelMapper.delete(modelId);
        logger.info("数据库记录删除成功: {}", modelId);

        return true;
    }

    @Override
    @Transactional
    public boolean deleteByModelName(String modelName) {
        logger.info("根据名称删除模型: {}", modelName);

        PretrainedModel model = modelMapper.findByModelName(modelName);
        if (model == null) {
            logger.warn("模型不存在: {}", modelName);
            return false;
        }

        return deleteModel(model.getModelId());
    }

    @Override
    @Transactional
    public PretrainedModelDto updateModel(Integer modelId, PretrainedModelDto dto) {
        logger.info("更新模型ID: {}", modelId);

        PretrainedModel model = modelMapper.findById(modelId);
        if (model == null) {
            throw new RuntimeException("模型不存在: " + modelId);
        }

        // 如果要更新模型名称，检查是否重复
        if (dto.getModelName() != null && !dto.getModelName().equals(model.getModelName())) {
            PretrainedModel existingModel = modelMapper.findByModelName(dto.getModelName());
            if (existingModel != null) {
                throw new RuntimeException("模型名称已存在: " + dto.getModelName());
            }
        }

        // 更新实体字段
        if (dto.getModelName() != null) {
            model.setModelName(dto.getModelName());
        }
        if (dto.getModelPartition() != null) {
            model.setModelPartition(dto.getModelPartition());
        }
        if (dto.getModelFilePath() != null) {
            model.setModelFilePath(dto.getModelFilePath());
        }

        // 使用选择性更新
        modelMapper.updateSelective(model);
        logger.info("模型更新成功: {}", model);

        // 重新查询获取最新数据
        PretrainedModel updatedModel = modelMapper.findById(modelId);
        return convertUtil.toDto(updatedModel);
    }

    @Override
    public List<PretrainedModelDto> searchModels(String keyword, int limit) {
        logger.info("搜索模型，关键词: {}, 限制: {}", keyword, limit);
        List<PretrainedModel> models = modelMapper.searchModels(keyword, limit);
        return convertUtil.toDtoList(models);
    }

    @Override
    @Transactional
    public boolean batchDeleteModels(List<Integer> modelIds) {
        logger.info("批量删除模型，ID列表: {}", modelIds);

        if (modelIds == null || modelIds.isEmpty()) {
            logger.warn("模型ID列表为空");
            return false;
        }

        // 先删除所有相关文件
        for (Integer modelId : modelIds) {
            PretrainedModel model = modelMapper.findById(modelId);
            if (model != null) {
                try {
                    Path filePath = Paths.get(model.getModelFilePath());
                    if (Files.exists(filePath)) {
                        Files.delete(filePath);
                        logger.info("文件删除成功: {}", filePath);
                    }
                } catch (IOException e) {
                    logger.error("文件删除失败: {}", model.getModelFilePath(), e);
                }
            }
        }

        // 批量删除数据库记录
        modelMapper.batchDelete(modelIds);
        logger.info("批量删除完成");

        return true;
    }

    @Override
    public int countModels() {
        logger.info("统计模型总数");
        return modelMapper.count();
    }

    @Override
    public int countByPartition(String partition) {
        logger.info("统计分区模型数量: {}", partition);
        return modelMapper.countByPartition(partition);
    }
}