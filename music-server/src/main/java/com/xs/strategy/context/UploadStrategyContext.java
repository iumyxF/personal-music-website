package com.xs.strategy.context;

import com.xs.strategy.UploadStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;

import static com.xs.enums.UploadModeEnum.getStrategy;

/**
 * 上传策略上下文
 */
@Service
public class UploadStrategyContext {
    /**
     * 上传模式
     */
    @Value("${upload.mode}")
    private String uploadMode;

    @Resource
    private Map<String, UploadStrategy> uploadStrategyMap;

    /**
     * 执行上传策略
     *
     * @param file 文件
     * @param path 路径
     * @return {@link String} 文件地址
     */
    public String executeUploadStrategy(MultipartFile file, String path) {
        return uploadStrategyMap.get(getStrategy(uploadMode)).uploadFile(file, path);
    }

    /**
     * 上传文件分片
     *
     * @param file      文件分片
     * @param fileId    文件唯一标识
     * @param start     分片起始位置
     * @param totalSize 文件总大小
     * @param path      上传路径
     * @throws IOException IO异常
     */
    public void uploadFileChunk(MultipartFile file, String fileId, long start, long totalSize, String path) throws IOException {
        uploadStrategyMap.get(getStrategy(uploadMode)).uploadFileChunk(file, fileId, start, totalSize, path);
    }

    /**
     * 合并文件分片
     *
     * @param fileId   文件唯一标识
     * @param fileName 文件名 (文件名+后缀)
     * @param path     上传路径
     * @return {@link String} 文件地址
     * @throws IOException IO异常
     */
    public String mergeFileChunk(String fileId, String fileName, String path) throws IOException {
        return uploadStrategyMap.get(getStrategy(uploadMode)).mergeFileChunk(fileId, fileName, path);
    }
}
