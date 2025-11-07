package com.xs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xs.domain.CommentAnalysis;

/**
 * @author iumyx
 * @description 针对表【comment_analysis(评论分析表)】的数据库操作Service
 * @createDate 2025-11-07 09:29:23
 */
public interface CommentAnalysisService extends IService<CommentAnalysis> {

    /**
     * 评论分析
     *
     * @param commentId 评论id
     * @return 评论分析结果
     */
    CommentAnalysis doAnalysis(Long commentId);
}
