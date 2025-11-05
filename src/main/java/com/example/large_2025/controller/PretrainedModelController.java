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
     * @param partitionUsage 模块用途
     * @param usageDescription 作用描述
     * @param file 上传的文件
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> uploadModel(
            @RequestParam("modelName") String modelName,
            @RequestParam("modelPartition") String modelPartition,
            @RequestParam("partitionUsage") String partitionUsage,
            @RequestParam("usageDescription") String usageDescription,
            @RequestParam("file") MultipartFile file) {

        Map<String, Object> response = new HashMap<>();

        try {
            // 验证文件是否为空
            if (file.isEmpty()) {
                response.put("success", false);
                response.put("message", "上传文件不能为空");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // 验证模型名称和分区
            if (modelName == null || modelName.isEmpty() ||
                    modelPartition == null || modelPartition.isEmpty()) {
                response.put("success", false);
                response.put("message", "模型名称和分区不能为空");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            logger.info("收到上传请求 - 模型名称: {}, 分区: {}, 模块: {}, 作用: {}, 文件: {}, 大小: {} bytes",
                    modelName, modelPartition, partitionUsage, usageDescription,
                    file.getOriginalFilename(), file.getSize());

            // 调用service方法，传入所有参数
            PretrainedModelDto dto = modelService.uploadModel(modelName, modelPartition,
                    partitionUsage, usageDescription, file);

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
     * 根据分区和模块查询模型
     * GET /api/models/partition/{partition}/usage/{usage}
     * 这个端点对应service中的findByUsage方法
     */
    @GetMapping("/partition/{partition}/usage/{usage}")
    public ResponseEntity<Map<String, Object>> getModelsByUsage(
            @PathVariable("partition") String partition,
            @PathVariable("usage") String usage) {

        Map<String, Object> response = new HashMap<>();

        try {
            List<PretrainedModelDto> dtoList = modelService.findByUsage(partition, usage);

            response.put("success", true);
            response.put("data", dtoList);
            response.put("count", dtoList.size());
            response.put("partition", partition);
            response.put("usage", usage);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("根据分区和模块查询模型失败", e);
            response.put("success", false);
            response.put("message", "查询失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 根据分区、模块和描述查询模型
     * GET /api/models/partition/{partition}/usage/{usage}/description/{description}
     * 这个端点对应service中的findByDescription方法
     */
    @GetMapping("/partition/{partition}/usage/{usage}/description/{description}")
    public ResponseEntity<Map<String, Object>> getModelsByDescription(
            @PathVariable("partition") String partition,
            @PathVariable("usage") String usage,
            @PathVariable("description") String description) {

        Map<String, Object> response = new HashMap<>();

        try {
            List<PretrainedModelDto> dtoList = modelService.findByDescription(partition, usage, description);

            response.put("success", true);
            response.put("data", dtoList);
            response.put("count", dtoList.size());
            response.put("partition", partition);
            response.put("usage", usage);
            response.put("description", description);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("根据分区、模块和描述查询模型失败", e);
            response.put("success", false);
            response.put("message", "查询失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 搜索模型（支持模糊查询）
     * GET /api/models/search?keyword={keyword}&limit={limit}
     */
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchModels(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "limit", defaultValue = "10") int limit) {

        Map<String, Object> response = new HashMap<>();

        try {
            List<PretrainedModelDto> dtoList = modelService.searchModels(keyword, limit);

            response.put("success", true);
            response.put("data", dtoList);
            response.put("count", dtoList.size());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("搜索模型失败", e);
            response.put("success", false);
            response.put("message", "搜索失败: " + e.getMessage());
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
     * 批量删除模型
     * DELETE /api/models/batch
     * Request Body: {"modelIds": [1, 2, 3]}
     */
    @DeleteMapping("/batch")
    public ResponseEntity<Map<String, Object>> batchDeleteModels(@RequestBody Map<String, List<Integer>> request) {
        Map<String, Object> response = new HashMap<>();

        try {
            List<Integer> modelIds = request.get("modelIds");

            if (modelIds == null || modelIds.isEmpty()) {
                response.put("success", false);
                response.put("message", "模型ID列表不能为空");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            boolean deleted = modelService.batchDeleteModels(modelIds);

            if (deleted) {
                response.put("success", true);
                response.put("message", "批量删除成功");
                response.put("deletedCount", modelIds.size());
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "批量删除失败");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }

        } catch (Exception e) {
            logger.error("批量删除模型失败", e);
            response.put("success", false);
            response.put("message", "批量删除失败: " + e.getMessage());
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
     * 统计模型总数
     * GET /api/models/count
     */
    @GetMapping("/count")
    public ResponseEntity<Map<String, Object>> countModels() {
        Map<String, Object> response = new HashMap<>();

        try {
            int count = modelService.countModels();

            response.put("success", true);
            response.put("count", count);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("统计模型总数失败", e);
            response.put("success", false);
            response.put("message", "统计失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 统计指定分区的模型数量
     * GET /api/models/count/partition/{partition}
     */
    @GetMapping("/count/partition/{partition}")
    public ResponseEntity<Map<String, Object>> countByPartition(@PathVariable("partition") String partition) {
        Map<String, Object> response = new HashMap<>();

        try {
            int count = modelService.countByPartition(partition);

            response.put("success", true);
            response.put("partition", partition);
            response.put("count", count);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("统计分区模型数量失败", e);
            response.put("success", false);
            response.put("message", "统计失败: " + e.getMessage());
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