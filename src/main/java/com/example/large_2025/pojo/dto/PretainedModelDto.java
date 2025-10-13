package com.example.large_2025.pojo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import org.hibernate.validator.constraints.URL;
import org.hibernate.validator.constraints.UniqueElements;

/**
 * 预训练模型数据传输对象（DTO）
 * 用于前端与后端之间的数据传输和校验
 * 字段定义与PretainedModel实体类保持严格映射
 */
public class PretainedModelDto {
    // ================== 基本信息字段 ==================
    /** 模型ID（对应数据库model_id字段） */
    private Integer modelId;

    /**
     * 预训练模型名称
     * - 非空校验：名称不能为空
     * - 长度校验：最大长度100个字符（与数据库保持一致）
     * - 唯一性校验：名称在系统中必须唯一
     */    @NotBlank(message = "预训练模型名称不能为空")
    @Size(max = 100, message = "预训练模型名称长度不能超过100个字符")
    @UniqueElements
    private String modelName;

    /**
     * 预训练模型分区
     * - 非空校验：分区不能为空
     * - 长度校验：最大长度50个字符（与数据库保持一致）
     * - 枚举校验：分区必须是预定义的枚举值之一（NLP, CV, Audio）
     */    @NotBlank(message = "预训练模型分区不能为空")
    @Size(max = 50, message = "预训练模型分区长度不能超过50个字符")

    private String modelPartition;

}