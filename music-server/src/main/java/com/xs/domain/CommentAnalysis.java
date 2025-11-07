package com.xs.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.xs.enums.EmotionEnums;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author fzy
 * @description:
 * @date 2025-11-07 09:15
 */
@Data
public class CommentAnalysis implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 评论ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long commentId;

    /**
     * 情感类型
     * {@link EmotionEnums}
     */
    @TableField("emotion_type")
    private Integer emotionType;

    /**
     * 情感倾向得分（置信度）
     */
    @TableField("emotion_score")
    private Double emotionScore;

    /**
     * 评论主题分类
     */
    @TableField("topic_type")
    private String topicType;

    /**
     * 是否为垃圾评论（0 - 正常，1 - 垃圾）
     */
    @TableField("is_spam")
    private Boolean isSpam;

    /**
     * 垃圾评论类型（如 “广告信息”“辱骂信息”“垃圾信息”）
     */
    @TableField("spam_type")
    private String spamType;

    /**
     * AI 分析完成时间
     */
    @TableField("analysis_time")
    private LocalDateTime analysisTime;

    /**
     * 分析所用 AI 模型版本
     */
    @TableField("model_version")
    private String modelVersion;
}
