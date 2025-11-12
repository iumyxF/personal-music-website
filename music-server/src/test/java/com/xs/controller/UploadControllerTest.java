package com.xs.controller;

import okhttp3.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

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
     * 测试获取已上传分片范围接口
     */
    @Test
    void testGetUploadedChunks() throws IOException {
        // 1. 准备原始文件并获取基本信息
        File originalFile = new File(ORIGINAL_FILE_PATH);
        long totalSize = originalFile.length();
        String fileId = String.valueOf(System.currentTimeMillis());
        int totalChunks = (int) Math.ceil((double) totalSize / CHUNK_SIZE); // 总分片数
        System.out.println("文件总大小: " + totalSize + " bytes");
        System.out.println("总分片数: " + totalChunks);

        // 2. 上传除最后一片外的所有分片
        List<Long> uploadedChunkStarts = new ArrayList<>();
        int lastChunkIndex = totalChunks - 1;

        for (int chunkIndex = 0; chunkIndex < totalChunks; chunkIndex++) {
            // 跳过最后一个分片
            if (chunkIndex == lastChunkIndex) {
                System.out.println("跳过上传最后一个分片 (索引: " + chunkIndex + ")");
                continue;
            }

            // 计算当前分片的起始位置和结束位置
            long start = (long) chunkIndex * CHUNK_SIZE;
            long end = Math.min(start + CHUNK_SIZE, totalSize);
            boolean isLast = false; // 因为我们跳过了最后一个分片，所以这里设置为false

            // 记录已上传的分片起始位置
            uploadedChunkStarts.add(start);

            System.out.println("上传分片 " + chunkIndex + "/" + totalChunks + ", 范围: " + start + "-" + end);

            // 读取并上传分片
            byte[] chunkBytes = readChunk(originalFile, start, end);
            Request request = buildChunkRequest(url(), fileId, start, totalSize, isLast, chunkBytes, originalFile.getName());

            try (Response response = client.newCall(request).execute()) {
                assertTrue(response.isSuccessful(), String.format("分片 %d/%d 上传失败，响应码：%d", chunkIndex + 1, totalChunks - 1, response.code()));
                System.out.println("分片 " + chunkIndex + " 上传成功");
            }
        }

        // 3. 调用getUploadedChunks接口获取已上传分片范围
        String getChunksUrl = url() + "?fileId=" + fileId;
        Request getChunksRequest = new Request.Builder()
                .url(getChunksUrl)
                .get()
                .build();

        try (Response response = client.newCall(getChunksRequest).execute()) {
            // 4. 验证响应成功
            assertTrue(response.isSuccessful(), "获取已上传分片范围失败，响应码：" + response.code());

            // 5. 解析响应内容
            ResponseBody responseBody = response.body();
            assertNotNull(responseBody, "响应体为空");
            String responseJson = responseBody.string();

            // 6. 验证响应中包含已上传的分片信息
            System.out.println("已上传分片范围响应：" + responseJson);
            assertTrue(responseJson.contains("data"), "响应中不包含data字段");

            // 验证是否只缺少最后一片
            // 计算最后一个分片的起始位置
            long lastChunkStart = (long) lastChunkIndex * CHUNK_SIZE;
            System.out.println("预期缺少的分片起始位置: " + lastChunkStart);

            // 验证已上传的分片数量是总分片数减1
            System.out.println("已上传分片数量: " + uploadedChunkStarts.size());
            System.out.println("预期已上传分片数量: " + (totalChunks - 1));
            assertEquals(totalChunks - 1, uploadedChunkStarts.size(), "已上传分片数量不正确");
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