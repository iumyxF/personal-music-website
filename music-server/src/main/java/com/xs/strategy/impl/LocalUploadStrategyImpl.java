package com.xs.strategy.impl;

import com.xs.enums.FileExtEnum;
import com.xs.exception.BizException;
import com.xs.util.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * 本地上传策略
 */
@Slf4j
@Service("localUploadStrategyImpl")
public class LocalUploadStrategyImpl extends AbstractUploadStrategyImpl {

    /**
     * 本地路径
     */
    @Value("${upload.local.path}")
    private String localPath;

    /**
     * 访问url
     */
    @Value("${upload.local.url}")
    private String localUrl;

    @Override
    public Boolean exists(String filePath) {
        return new File(localPath + filePath).exists();
    }

    @Override
    public void upload(String path, String fileName, InputStream inputStream) throws IOException {
        // 判断目录是否存在
        File directory = new File(localPath + path);
        if (!directory.exists() && !directory.mkdirs()) {
            throw new BizException("创建目录失败");
        }
        // 写入文件
        File file = new File(localPath + path + fileName);
        String ext = FileUtils.getExtName(fileName);
        if (FileExtEnum.getFileExt(ext) == FileExtEnum.MD || FileExtEnum.getFileExt(ext) == FileExtEnum.TXT) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                 BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                int charRead;
                while ((charRead = reader.read()) != -1) {
                    writer.write(charRead);
                }
                writer.flush();
            }
        } else {
            try (BufferedInputStream bis = new BufferedInputStream(inputStream);
                 BufferedOutputStream bos = new BufferedOutputStream(Files.newOutputStream(file.toPath()))) {
                byte[] bytes = new byte[1024];
                int length;
                while ((length = bis.read(bytes)) != -1) {
                    bos.write(bytes, 0, length);
                }
                bos.flush();
            }
        }
    }

    @Override
    public void uploadChunk(String path, String fileId, InputStream inputStream, long start) throws IOException {
        // 创建目录
        File directory = new File(localPath + path + "chunks" + File.separator + fileId + File.separator);
        if (!directory.exists() && !directory.mkdirs()) {
            throw new BizException("创建分片目录失败");
        }
        // 保存分片文件，使用起始位置作为分片文件名
        File chunkFile = new File(directory, String.valueOf(start));
        try (FileOutputStream fos = new FileOutputStream(chunkFile);
             BufferedInputStream bis = new BufferedInputStream(inputStream)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, length);
            }
            fos.flush();
        }
    }

    @Override
    public void mergeChunk(String path, String fileId, String fileName) throws IOException {
        // 分片目录
        File chunksDir = new File(localPath + path + "chunks" + File.separator + fileId + File.separator);
        if (!chunksDir.exists()) {
            throw new BizException("分片目录不存在");
        }
        // 目标文件
        File targetFile = new File(localPath + path + fileName);
        // 确保目标文件的父目录存在
        if (!targetFile.getParentFile().exists() && !targetFile.getParentFile().mkdirs()) {
            throw new BizException("创建目标文件目录失败");
        }
        // 文件合并
        try (FileChannel outChannel = FileChannel.open(targetFile.toPath(), StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
            // 获取所有分片文件并按起始位置排序
            File[] chunkFiles = chunksDir.listFiles();
            if (chunkFiles == null || chunkFiles.length == 0) {
                throw new BizException("没有找到分片文件");
            }
            // 排序
            Arrays.sort(chunkFiles, Comparator.comparingLong(f -> Long.parseLong(f.getName())));
            // 合并所有分片
            ByteBuffer buffer = ByteBuffer.allocateDirect(1024 * 1024);
            for (File chunkFile : chunkFiles) {
                try (FileChannel inChannel = FileChannel.open(chunkFile.toPath(), StandardOpenOption.READ)) {
                    buffer.clear();
                    int bytesRead;
                    while ((bytesRead = inChannel.read(buffer)) != -1) {
                        // buffer切换成读模式输出文件数据
                        buffer.flip();
                        while (buffer.hasRemaining()) {
                            outChannel.write(buffer);
                        }
                        buffer.clear();
                    }
                }
                // 删除分片文件
                chunkFile.delete();
            }
        }
        // 删除分片目录
        chunksDir.delete();
    }

    @Override
    public String getFileAccessUrl(String filePath) {
        return localUrl + filePath;
    }

    @Override
    public List<Long> getUploadedChunkList(String path, String fileId) {
        List<Long> uploadedChunks = new ArrayList<>();
        // 分片目录
        File chunksDir = new File(localPath + path + "chunks" + File.separator + fileId + File.separator);
        // 检查分片目录是否存在
        if (chunksDir.exists() && chunksDir.isDirectory()) {
            // 获取目录中的所有分片文件
            File[] chunkFiles = chunksDir.listFiles();
            if (chunkFiles != null) {
                for (File chunkFile : chunkFiles) {
                    try {
                        // 文件名即为分片的起始位置
                        long startPosition = Long.parseLong(chunkFile.getName());
                        uploadedChunks.add(startPosition);
                    } catch (NumberFormatException e) {
                        // 忽略无效的文件名
                        log.warn("不合法的分片文件名称: {}", chunkFile.getName());
                    }
                }
            }
        }
        // 按起始位置排序
        uploadedChunks.sort(Long::compareTo);
        return uploadedChunks;
    }
}