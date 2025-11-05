package com.example.large_2025.controller;

import com.example.large_2025.pojo.dto.DatasetDto;
import com.example.large_2025.service.IDatasetService;
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
@RequestMapping("/api/datasets")
public class DatasetController {

    private static final Logger logger = LoggerFactory.getLogger(DatasetController.class);

    @Autowired
    private IDatasetService datasetService;

    /**
     * 上传数据集文件 (使用 multipart/form-data)
     * POST /api/datasets/upload
     * Content-Type: multipart/form-data
     *
     * @param datasetName 数据集名称
     * @param datasetCategory 数据集分类（NLP、CV、Audio等）
     * @param file 上传的文件
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> uploadDataset(
            @RequestParam("datasetName") String datasetName,
            @RequestParam("datasetCategory") String datasetCategory,
            @RequestParam("file") MultipartFile file) {

        Map<String, Object> response = new HashMap<>();

        try {
            // 验证文件是否为空
            if (file.isEmpty()) {
                response.put("success", false);
                response.put("message", "上传文件不能为空");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            //验证数据集名称和分类
            if (datasetName == null || datasetName.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "数据集名称不能为空");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            logger.info("收到上传请求 - 数据集名称: {}, 分类: {}, 文件: {}, 大小: {} bytes",
                    datasetName, datasetCategory, file.getOriginalFilename(), file.getSize());

            DatasetDto dto = datasetService.uploadDataset(datasetName, datasetCategory, file);

            response.put("success", true);
            response.put("message", "数据集上传成功");
            response.put("data", dto);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("数据集上传失败", e);
            response.put("success", false);
            response.put("message", "数据集上传失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 根据ID查询数据集
     * GET /api/datasets/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getDatasetById(@PathVariable("id") Integer id) {
        Map<String, Object> response = new HashMap<>();

        try {
            DatasetDto dto = datasetService.findById(id);

            if (dto == null) {
                response.put("success", false);
                response.put("message", "数据集不存在");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            response.put("success", true);
            response.put("data", dto);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("查询数据集失败", e);
            response.put("success", false);
            response.put("message", "查询失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 根据名称查询数据集
     * GET /api/datasets/name/{datasetName}
     */
    @GetMapping("/name/{datasetName}")
    public ResponseEntity<Map<String, Object>> getDatasetByName(@PathVariable("datasetName") String datasetName) {
        Map<String, Object> response = new HashMap<>();

        try {
            DatasetDto dto = datasetService.findByDatasetName(datasetName);

            if (dto == null) {
                response.put("success", false);
                response.put("message", "数据集不存在");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            response.put("success", true);
            response.put("data", dto);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("查询数据集失败", e);
            response.put("success", false);
            response.put("message", "查询失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 查询所有数据集
     * GET /api/datasets
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllDatasets() {
        Map<String, Object> response = new HashMap<>();

        try {
            List<DatasetDto> dtoList = datasetService.findAll();

            response.put("success", true);
            response.put("data", dtoList);
            response.put("count", dtoList.size());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("查询所有数据集失败", e);
            response.put("success", false);
            response.put("message", "查询失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 根据分类查询数据集
     * GET /api/datasets/category/{category}
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<Map<String, Object>> getDatasetsByCategory(@PathVariable("category") String category) {
        Map<String, Object> response = new HashMap<>();

        try {
            List<DatasetDto> dtoList = datasetService.findByCategory(category);

            response.put("success", true);
            response.put("data", dtoList);
            response.put("count", dtoList.size());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("根据分类查询数据集失败", e);
            response.put("success", false);
            response.put("message", "查询失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 搜索数据集（支持模糊查询）
     * GET /api/datasets/search?keyword={keyword}&limit={limit}
     */
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchDatasets(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "limit", defaultValue = "10") int limit) {

        Map<String, Object> response = new HashMap<>();

        try {
            List<DatasetDto> dtoList = datasetService.searchDatasets(keyword, limit);

            response.put("success", true);
            response.put("data", dtoList);
            response.put("count", dtoList.size());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("搜索数据集失败", e);
            response.put("success", false);
            response.put("message", "搜索失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 删除数据集（根据ID）
     * DELETE /api/datasets/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteDataset(@PathVariable("id") Integer id) {
        Map<String, Object> response = new HashMap<>();

        try {
            boolean deleted = datasetService.deleteDataset(id);

            if (deleted) {
                response.put("success", true);
                response.put("message", "数据集删除成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "数据集不存在");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

        } catch (Exception e) {
            logger.error("删除数据集失败", e);
            response.put("success", false);
            response.put("message", "删除失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 删除数据集（根据名称）
     * DELETE /api/datasets/name/{datasetName}
     */
    @DeleteMapping("/name/{datasetName}")
    public ResponseEntity<Map<String, Object>> deleteDatasetByName(@PathVariable("datasetName") String datasetName) {
        Map<String, Object> response = new HashMap<>();

        try {
            boolean deleted = datasetService.deleteByDatasetName(datasetName);

            if (deleted) {
                response.put("success", true);
                response.put("message", "数据集删除成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "数据集不存在");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

        } catch (Exception e) {
            logger.error("删除数据集失败", e);
            response.put("success", false);
            response.put("message", "删除失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 批量删除数据集
     * DELETE /api/datasets/batch
     * Request Body: {"datasetIds": [1, 2, 3]}
     */
    @DeleteMapping("/batch")
    public ResponseEntity<Map<String, Object>> batchDeleteDatasets(@RequestBody Map<String, List<Integer>> request) {
        Map<String, Object> response = new HashMap<>();

        try {
            List<Integer> datasetIds = request.get("datasetIds");

            if (datasetIds == null || datasetIds.isEmpty()) {
                response.put("success", false);
                response.put("message", "数据集ID列表不能为空");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            boolean deleted = datasetService.batchDeleteDatasets(datasetIds);

            if (deleted) {
                response.put("success", true);
                response.put("message", "批量删除成功");
                response.put("deletedCount", datasetIds.size());
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "批量删除失败");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }

        } catch (Exception e) {
            logger.error("批量删除数据集失败", e);
            response.put("success", false);
            response.put("message", "批量删除失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 更新数据集信息
     * PUT /api/datasets/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateDataset(
            @PathVariable("id") Integer id,
            @RequestBody DatasetDto dto) {

        Map<String, Object> response = new HashMap<>();

        try {
            DatasetDto updatedDto = datasetService.updateDataset(id, dto);

            response.put("success", true);
            response.put("message", "数据集更新成功");
            response.put("data", updatedDto);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("更新数据集失败", e);
            response.put("success", false);
            response.put("message", "更新失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 统计数据集总数
     * GET /api/datasets/count
     */
    @GetMapping("/count")
    public ResponseEntity<Map<String, Object>> countDatasets() {
        Map<String, Object> response = new HashMap<>();

        try {
            int count = datasetService.countDatasets();

            response.put("success", true);
            response.put("count", count);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("统计数据集总数失败", e);
            response.put("success", false);
            response.put("message", "统计失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 统计指定分类的数据集数量
     * GET /api/datasets/count/category/{category}
     */
    @GetMapping("/count/category/{category}")
    public ResponseEntity<Map<String, Object>> countByCategory(@PathVariable("category") String category) {
        Map<String, Object> response = new HashMap<>();

        try {
            int count = datasetService.countByCategory(category);

            response.put("success", true);
            response.put("category", category);
            response.put("count", count);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("统计分类数据集数量失败", e);
            response.put("success", false);
            response.put("message", "统计失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 健康检查
     * GET /api/datasets/health
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "OK");
        response.put("service", "DatasetService");
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }
}