package com.example.large_2025.mapper;

import com.example.large_2025.pojo.dto.PretrainedModelDto;
import com.example.large_2025.pojo.PretrainedModel;
import org.springframework.stereotype.Component;

@Component
public class PretrainedModelMapper {

    /**
     * 将实体映射为DTO
     */
    public PretrainedModelDto entityToDto(PretrainedModel entity) {
        if (entity == null) {
            return null;
        }

        return new PretrainedModelDto(
                entity.getModelId(),
                entity.getModelName(),
                entity.getModelPartition(),
                entity.getModelFilePath(),
                entity.getUpdateTimestamp()
        );
    }

    /**
     * 将DTO映射为实体
     */
    public PretrainedModel dtoToEntity(PretrainedModelDto dto) {
        if (dto == null) {
            return null;
        }

        PretrainedModel entity = new PretrainedModel();
        entity.setModelId(dto.getModelId());
        entity.setModelName(dto.getModelName());
        entity.setModelPartition(dto.getModelPartition());
        entity.setModelFilePath(dto.getModelFilePath());
        entity.setUpdateTimestamp(dto.getUpdateTimestamp());

        return entity;
    }

    /**
     * 更新实体的数据（不包括ID）
     */
    public void updateEntityFromDto(PretrainedModel entity, PretrainedModelDto dto) {
        if (entity == null || dto == null) {
            return;
        }

        if (dto.getModelName() != null) {
            entity.setModelName(dto.getModelName());
        }
        if (dto.getModelPartition() != null) {
            entity.setModelPartition(dto.getModelPartition());
        }
        if (dto.getModelFilePath() != null) {
            entity.setModelFilePath(dto.getModelFilePath());
        }
    }
}