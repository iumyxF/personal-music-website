package com.xs.strategy;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 上传策略
 */
public interface UploadStrategy {

    /**
     * 上传文件
     *
     * @param file 文件
     * @param path 上传路径
     * @return {@link String} 文件地址
     */
    String uploadFile(MultipartFile file, String path);

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
    void uploadFileChunk(MultipartFile file, String fileId, long start, long totalSize, String path) throws IOException;

    /**
     * 合并文件分片
     *
     * @param fileId   文件唯一标识
     * @param fileName 文件名
     * @param path     上传路径
     * @return {@link String} 文件地址
     * @throws IOException IO异常
     */
    String mergeFileChunk(String fileId, String fileName, String path) throws IOException;

    /**
     * 查询已上传的分片范围
     *
     * @param fileId 文件唯一标识
     * @param path   上传路径
     * @return 已上传的分片起始位置列表
     */
    List<Long> getUploadedChunks(String fileId, String path);

}
