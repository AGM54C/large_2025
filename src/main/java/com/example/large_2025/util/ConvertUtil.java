package com.example.large_2025.util;

import com.example.large_2025.pojo.dto.PretrainedModelDto;
import com.example.large_2025.pojo.dto.DatasetDto;
import com.example.large_2025.pojo.dto.UserDto;
import com.example.large_2025.pojo.PretrainedModel;
import com.example.large_2025.pojo.Dataset;
import com.example.large_2025.pojo.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ConvertUtil {

    // ==================== PretrainedModel 转换方法 ====================

    /**
     * 将 PretrainedModel 实体转换为 PretrainedModelDto
     */
    public PretrainedModelDto PretrainedModeltoDto(PretrainedModel model) {
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
    public PretrainedModel PretrainedModelDtotoEntity(PretrainedModelDto dto) {
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
    public List<PretrainedModelDto> PretrainedModelListtoDtoList(List<PretrainedModel> models) {
        if (models == null) {
            return null;
        }

        List<PretrainedModelDto> dtoList = new ArrayList<>();
        for (PretrainedModel model : models) {
            dtoList.add(PretrainedModeltoDto(model));
        }

        return dtoList;
    }

    /**
     * 将 PretrainedModelDto 列表转换为 PretrainedModel 列表
     */
    public List<PretrainedModel> PretrainedModelDtoListtoEntityList(List<PretrainedModelDto> dtos) {
        if (dtos == null) {
            return null;
        }

        List<PretrainedModel> modelList = new ArrayList<>();
        for (PretrainedModelDto dto : dtos) {
            modelList.add(PretrainedModelDtotoEntity(dto));
        }

        return modelList;
    }

    // ==================== Dataset 转换方法 ====================

    /**
     * 将 Dataset 实体转换为 DatasetDto
     */
    public DatasetDto DatasettoDto(Dataset dataset) {
        if (dataset == null) {
            return null;
        }

        DatasetDto dto = new DatasetDto();
        dto.setDatasetId(dataset.getDatasetId());
        dto.setDatasetCategory(dataset.getDatasetCategory());
        dto.setDatasetName(dataset.getDatasetName());
        dto.setDatasetFilePath(dataset.getDatasetFilePath());
        dto.setUpdateTimestamp(dataset.getUpdateTimestamp());

        return dto;
    }

    /**
     * 将 DatasetDto 转换为 Dataset 实体
     */
    public Dataset DatasetDtotoEntity(DatasetDto dto) {
        if (dto == null) {
            return null;
        }

        Dataset dataset = new Dataset();
        dataset.setDatasetId(dto.getDatasetId());
        dataset.setDatasetCategory(dto.getDatasetCategory());
        dataset.setDatasetName(dto.getDatasetName());
        dataset.setDatasetFilePath(dto.getDatasetFilePath());
        dataset.setUpdateTimestamp(dto.getUpdateTimestamp());

        return dataset;
    }

    /**
     * 将 Dataset 列表转换为 DatasetDto 列表
     */
    public List<DatasetDto> DatasetListtoDtoList(List<Dataset> datasets) {
        if (datasets == null) {
            return null;
        }

        List<DatasetDto> dtoList = new ArrayList<>();
        for (Dataset dataset : datasets) {
            dtoList.add(DatasettoDto(dataset));
        }

        return dtoList;
    }

    /**
     * 将 DatasetDto 列表转换为 Dataset 列表
     */
    public List<Dataset> DatasetDtoListtoEntityList(List<DatasetDto> dtos) {
        if (dtos == null) {
            return null;
        }

        List<Dataset> datasetList = new ArrayList<>();
        for (DatasetDto dto : dtos) {
            datasetList.add(DatasetDtotoEntity(dto));
        }

        return datasetList;
    }

    // ==================== User 转换方法 ====================

    /**
     * User实体转化为UserDto
     */
    public UserDto UsertoDto(User user) {
        if (user == null) {
            return null;
        }

        UserDto dto = new UserDto();
        dto.setUserId(user.getUserId());
        dto.setNickname(user.getNickname());
        dto.setEmail(user.getEmail());
        dto.setPassword(user.getPassword());

        return dto;
    }

    /**
     * UserDto转化为User实体
     */
    public User UserDtotoEntity(UserDto dto) {
        if (dto == null) {
            return null;
        }

        User user = new User();
        user.setUserId(dto.getUserId());
        user.setNickname(dto.getNickname());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());

        return user;
    }

}