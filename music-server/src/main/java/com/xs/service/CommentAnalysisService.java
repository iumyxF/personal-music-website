package com.xs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xs.domain.CommentAnalysis;

import java.util.List;

/**
 * @author iumyx
 * @description 针对表【comment_analysis(评论分析表)】的数据库操作Service
 * @createDate 2025-11-07 09:29:23
 */
public interface CommentAnalysisService extends IService<CommentAnalysis> {

    /**
     * 评论分析
     *
     * @param songId 评论id
     * @return 评论分析结果
     */
    List<CommentAnalysis> doAnalysis(Long songId);
}
