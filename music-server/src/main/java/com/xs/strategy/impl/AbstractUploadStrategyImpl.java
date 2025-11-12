package com.xs.strategy.impl;

import com.xs.exception.BizException;
import com.xs.strategy.UploadStrategy;
import com.xs.util.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 抽象上传模板
 */
@Slf4j
@Service
public abstract class AbstractUploadStrategyImpl implements UploadStrategy {

    @Override
    public String uploadFile(MultipartFile file, String path) {
        try {
            InputStream inputStream = file.getInputStream();
            String md5 = FileUtils.getMd5(inputStream);
            inputStream.close();

            String extName = FileUtils.getExtName(file.getOriginalFilename());
            String fileName = md5 + extName;
            // 防止重复上传
            if (!exists(fileName)) {
                upload(path, fileName, file.getInputStream());
            }
            // 返回文件访问路径
            return getFileAccessUrl(path + fileName);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BizException("文件上传失败");
        }
    }

    @Override
    public void uploadFileChunk(MultipartFile file, String fileId, long start, long totalSize, String path) throws IOException {
        // 上传文件分片
        uploadChunk(path, fileId, file.getInputStream(), start);
    }

    @Override
    public String mergeFileChunk(String fileId, String fileName, String path) throws IOException {
        // 合并文件分片
        mergeChunk(path, fileId, fileName);
        // 返回文件访问路径
        return getFileAccessUrl(path + fileName);
    }

    @Override
    public List<Long> getUploadedChunks(String fileId, String path) {
        return getUploadedChunkList(path, fileId);
    }

    /**
     * 判断文件是否存在
     *
     * @param filePath 文件路径
     * @return {@link Boolean}
     */
    public abstract Boolean exists(String filePath);

    /**
     * 上传
     *
     * @param path        路径
     * @param fileName    文件名（包含后缀）
     * @param inputStream 输入流
     * @throws IOException io异常
     */
    public abstract void upload(String path, String fileName, InputStream inputStream) throws IOException;

    /**
     * 上传文件分片
     *
     * @param path        路径
     * @param fileId      文件唯一标识
     * @param inputStream 输入流
     * @param start       分片起始位置
     * @throws IOException IO异常
     */
    public abstract void uploadChunk(String path, String fileId, InputStream inputStream, long start) throws IOException;

    /**
     * 合并文件分片
     *
     * @param path     路径
     * @param fileId   文件唯一标识
     * @param fileName 文件名
     * @throws IOException IO异常
     */
    public abstract void mergeChunk(String path, String fileId, String fileName) throws IOException;

    /**
     * 获取文件访问url
     *
     * @param filePath 文件路径
     * @return {@link String}
     */
    public abstract String getFileAccessUrl(String filePath);

    /**
     * 获取已上传的分片列表
     *
     * @param path   上传路径
     * @param fileId 文件唯一标识
     * @return 已上传的分片起始位置列表
     */
    public abstract List<Long> getUploadedChunkList(String path, String fileId);

}
