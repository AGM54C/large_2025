package com.example.large_2025.controller;

import com.example.large_2025.pojo.dto.PretrainedModelDto;
import com.example.large_2025.service.IPretrainedModelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/models")
@CrossOrigin(origins = "*")
public class PretrainedModelController {

    private static final Logger logger = LoggerFactory.getLogger(PretrainedModelController.class);

    @Autowired
    private IPretrainedModelService modelService;

    /**
     * 上传模型文件 (使用 multipart/form-data)
     * POST /api/models/upload
     * Content-Type: multipart/form-data
     *
     * @param modelName 模型名称
     * @param modelPartition 模型分区
     * @param file 上传的文件
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> uploadModel(
            @RequestParam("modelName") String modelName,
            @RequestParam("modelPartition") String modelPartition,
            @RequestParam("file") MultipartFile file) {

        Map<String, Object> response = new HashMap<>();

        try {
            // 验证文件是否为空
            if (file.isEmpty()) {
                response.put("success", false);
                response.put("message", "上传文件不能为空");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            logger.info("收到上传请求 - 模型名称: {}, 分区: {}, 文件: {}, 大小: {} bytes",
                    modelName, modelPartition, file.getOriginalFilename(), file.getSize());

            PretrainedModelDto dto = modelService.uploadModel(modelName, modelPartition, file);

            response.put("success", true);
            response.put("message", "模型上传成功");
            response.put("data", dto);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("模型上传失败", e);
            response.put("success", false);
            response.put("message", "模型上传失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 根据ID查询模型
     * GET /api/models/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getModelById(@PathVariable("id") Integer id) {
        Map<String, Object> response = new HashMap<>();

        try {
            PretrainedModelDto dto = modelService.findById(id);

            if (dto == null) {
                response.put("success", false);
                response.put("message", "模型不存在");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            response.put("success", true);
            response.put("data", dto);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("查询模型失败", e);
            response.put("success", false);
            response.put("message", "查询失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 根据名称查询模型
     * GET /api/models/name/{modelName}
     */
    @GetMapping("/name/{modelName}")
    public ResponseEntity<Map<String, Object>> getModelByName(@PathVariable("modelName") String modelName) {
        Map<String, Object> response = new HashMap<>();

        try {
            PretrainedModelDto dto = modelService.findByModelName(modelName);

            if (dto == null) {
                response.put("success", false);
                response.put("message", "模型不存在");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            response.put("success", true);
            response.put("data", dto);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("查询模型失败", e);
            response.put("success", false);
            response.put("message", "查询失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 查询所有模型
     * GET /api/models
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllModels() {
        Map<String, Object> response = new HashMap<>();

        try {
            List<PretrainedModelDto> dtoList = modelService.findAll();

            response.put("success", true);
            response.put("data", dtoList);
            response.put("count", dtoList.size());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("查询所有模型失败", e);
            response.put("success", false);
            response.put("message", "查询失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 根据分区查询模型
     * GET /api/models/partition/{partition}
     */
    @GetMapping("/partition/{partition}")
    public ResponseEntity<Map<String, Object>> getModelsByPartition(@PathVariable("partition") String partition) {
        Map<String, Object> response = new HashMap<>();

        try {
            List<PretrainedModelDto> dtoList = modelService.findByPartition(partition);

            response.put("success", true);
            response.put("data", dtoList);
            response.put("count", dtoList.size());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("根据分区查询模型失败", e);
            response.put("success", false);
            response.put("message", "查询失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 删除模型（根据ID）
     * DELETE /api/models/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteModel(@PathVariable("id") Integer id) {
        Map<String, Object> response = new HashMap<>();

        try {
            boolean deleted = modelService.deleteModel(id);

            if (deleted) {
                response.put("success", true);
                response.put("message", "模型删除成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "模型不存在");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

        } catch (Exception e) {
            logger.error("删除模型失败", e);
            response.put("success", false);
            response.put("message", "删除失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 删除模型（根据名称）
     * DELETE /api/models/name/{modelName}
     */
    @DeleteMapping("/name/{modelName}")
    public ResponseEntity<Map<String, Object>> deleteModelByName(@PathVariable("modelName") String modelName) {
        Map<String, Object> response = new HashMap<>();

        try {
            boolean deleted = modelService.deleteByModelName(modelName);

            if (deleted) {
                response.put("success", true);
                response.put("message", "模型删除成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "模型不存在");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

        } catch (Exception e) {
            logger.error("删除模型失败", e);
            response.put("success", false);
            response.put("message", "删除失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 更新模型信息
     * PUT /api/models/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateModel(
            @PathVariable("id") Integer id,
            @RequestBody PretrainedModelDto dto) {

        Map<String, Object> response = new HashMap<>();

        try {
            PretrainedModelDto updatedDto = modelService.updateModel(id, dto);

            response.put("success", true);
            response.put("message", "模型更新成功");
            response.put("data", updatedDto);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("更新模型失败", e);
            response.put("success", false);
            response.put("message", "更新失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 健康检查
     * GET /api/models/health
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "OK");
        response.put("service", "PretrainedModelService");
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }
}