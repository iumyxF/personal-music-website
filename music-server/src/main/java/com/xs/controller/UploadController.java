package com.xs.controller;

import com.xs.enums.FilePathEnum;
import com.xs.strategy.context.UploadStrategyContext;
import com.xs.util.FileUtils;
import com.xs.vo.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import javax.annotation.Resource;

@Slf4j
@RestController
public class UploadController {

    @Resource
    private UploadStrategyContext uploadStrategyContext;

    /**
     * 上传头像
     */
    @PostMapping("/upload")
    public R<String> upload(@RequestParam("pic") MultipartFile file) {
        return R.ok(uploadStrategyContext.executeUploadStrategy(file, FilePathEnum.AVATAR.getPath()));
    }

    /**
     * 上传mp3
     */
    @PostMapping("/uploadAudio")
    public R<String> uploadAudio(@RequestParam("mp3") MultipartFile file) {
        return R.ok(uploadStrategyContext.executeUploadStrategy(file, FilePathEnum.VOICE.getPath()));
    }

    @PostMapping("/uploadAudio/chunk")
    public R<String> uploadChunk(@RequestParam MultipartFile file,
                                 @RequestParam String fileId,
                                 @RequestParam long start,
                                 @RequestParam long totalSize,
                                 @RequestParam boolean isLast) {
        if (null == file) {
            return R.fail("请选择文件");
        }
        try {
            uploadStrategyContext.uploadFileChunk(file, fileId, start, totalSize, FilePathEnum.VOICE.getPath());
            if (isLast) {
                // 文件名 使用 MD5.后缀
                String md5 = FileUtils.getMd5(file.getInputStream());
                String extName = FileUtils.getExtName(file.getOriginalFilename());
                String fileName = md5 + extName;
                String fileUrl = uploadStrategyContext.mergeFileChunk(fileId, fileName, FilePathEnum.VOICE.getPath());
                return R.ok("上传完成", fileUrl);
            }
            return R.ok("分片上传成功");
        } catch (Exception e) {
            log.error("分片上传失败,cause = {}", e.getMessage());
            return R.fail("分片上传失败: " + e.getMessage());
        }
    }

    /**
     * 查询已上传的分片范围
     */
    @GetMapping("/uploadAudio/chunk")
    public R<List<Long>> getUploadedChunks(@RequestParam String fileId) {
        List<Long> uploadedChunks = uploadStrategyContext.getUploadedChunks(fileId, FilePathEnum.VOICE.getPath());
        return R.ok(uploadedChunks);
    }
}
