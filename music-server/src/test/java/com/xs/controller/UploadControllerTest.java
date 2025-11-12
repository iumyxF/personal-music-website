package com.xs.controller;

import okhttp3.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author fzy
 * @description:
 * @date 2025-11-11 15:32
 */
@Disabled
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UploadControllerTest {

    @LocalServerPort
    private int port;

    private final OkHttpClient client = new OkHttpClient();
    // 测试文件路径
    private static final String ORIGINAL_FILE_PATH = "F:\\test\\video\\123.mp4";
    // 分片大小（1MB）
    private static final int CHUNK_SIZE = 1024 * 1024;

    @Test
    void testMultiChunkUpload() throws IOException {
        // 1. 准备原始文件并获取基本信息
        File originalFile = new File(ORIGINAL_FILE_PATH);
        long totalSize = originalFile.length();
        String fileId = String.valueOf(System.currentTimeMillis());
        int totalChunks = (int) Math.ceil((double) totalSize / CHUNK_SIZE); // 总分片数

        // 2. 循环分割文件并上传所有分片
        for (int chunkIndex = 0; chunkIndex < totalChunks; chunkIndex++) {
            // 计算当前分片的起始位置和结束位置
            long start = (long) chunkIndex * CHUNK_SIZE;
            long end = Math.min(start + CHUNK_SIZE, totalSize);
            boolean isLast = (chunkIndex == totalChunks - 1); // 是否最后一个分片

            // 读取当前分片的字节（从原始文件中截取）
            byte[] chunkBytes = readChunk(originalFile, start, end);

            // 3. 构建当前分片的请求
            Request request = buildChunkRequest(url(), fileId, start, totalSize, isLast, chunkBytes, originalFile.getName());

            // 4. 发送请求并验证当前分片上传成功
            try (Response response = client.newCall(request).execute()) {
                assertTrue(response.isSuccessful(), String.format("分片 %d/%d 上传失败，响应码：%d", chunkIndex + 1, totalChunks, response.code()));
            }
        }
    }

    /**
     * 从原始文件中读取指定范围的字节（分片内容）
     */
    private byte[] readChunk(File file, long start, long end) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            raf.seek(start); // 定位到起始位置
            int chunkLength = (int) (end - start);
            byte[] buffer = new byte[chunkLength];
            raf.readFully(buffer); // 读取分片内容
            return buffer;
        }
    }

    /**
     * 构建分片上传请求
     */
    private Request buildChunkRequest(String url, String fileId, long start, long totalSize,
                                      boolean isLast, byte[] chunkBytes, String originalFileName) {
        // 构建multipart/form-data请求体
        MultipartBody.Builder multipartBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);

        // 添加分片文件
        RequestBody fileBody = RequestBody.create(chunkBytes, MediaType.get("application/octet-stream"));
        multipartBuilder.addFormDataPart(
                "file",
                originalFileName, // 保持原始文件名
                fileBody
        );

        // 添加其他参数
        multipartBuilder.addFormDataPart("fileId", fileId);
        multipartBuilder.addFormDataPart("start", String.valueOf(start));
        multipartBuilder.addFormDataPart("totalSize", String.valueOf(totalSize));
        multipartBuilder.addFormDataPart("isLast", String.valueOf(isLast));

        // 构建请求
        return new Request.Builder()
                .url(url)
                .post(multipartBuilder.build())
                .build();
    }

    /**
     * 构建请求URL
     */
    private String url() {
        return "http://localhost:" + port + "/uploadAudio/chunk";
    }
}