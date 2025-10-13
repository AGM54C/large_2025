package com.example.large_2025.util;

import com.example.large_2025.pojo.dto.PretrainedModelDto;
import com.example.large_2025.pojo.PretrainedModel;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ConvertUtil {

    /**
     * 将 PretrainedModel 实体转换为 PretrainedModelDto
     */
    public PretrainedModelDto toDto(PretrainedModel model) {
        if (model == null) {
            return null;
        }

        PretrainedModelDto dto = new PretrainedModelDto();
        dto.setModelId(model.getModelId());
        dto.setModelName(model.getModelName());
        dto.setModelPartition(model.getModelPartition());
        dto.setModelFilePath(model.getModelFilePath());
        dto.setUpdateTimestamp(model.getUpdateTimestamp());

        return dto;
    }

    /**
     * 将 PretrainedModelDto 转换为 PretrainedModel 实体
     */
    public PretrainedModel toEntity(PretrainedModelDto dto) {
        if (dto == null) {
            return null;
        }

        PretrainedModel model = new PretrainedModel();
        model.setModelId(dto.getModelId());
        model.setModelName(dto.getModelName());
        model.setModelPartition(dto.getModelPartition());
        model.setModelFilePath(dto.getModelFilePath());
        model.setUpdateTimestamp(dto.getUpdateTimestamp());

        return model;
    }

    /**
     * 将 PretrainedModel 列表转换为 PretrainedModelDto 列表
     */
    public List<PretrainedModelDto> toDtoList(List<PretrainedModel> models) {
        if (models == null) {
            return null;
        }

        List<PretrainedModelDto> dtoList = new ArrayList<>();
        for (PretrainedModel model : models) {
            dtoList.add(toDto(model));
        }

        return dtoList;
    }

    /**
     * 将 PretrainedModelDto 列表转换为 PretrainedModel 列表
     */
    public List<PretrainedModel> toEntityList(List<PretrainedModelDto> dtos) {
        if (dtos == null) {
            return null;
        }

        List<PretrainedModel> modelList = new ArrayList<>();
        for (PretrainedModelDto dto : dtos) {
            modelList.add(toEntity(dto));
        }

        return modelList;
    }
}