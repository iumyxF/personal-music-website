package com.xs.dto.assistant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author fzy
 * @description:
 * @date 2025-11-07 14:20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentAnalysisRequest {

    private String songId;

    private String songName;

    private String lyrics;

    private String songAnalysis;

    private List<UserContent> userContentList;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserContent {

        private String commentId;

        private String content;
    }
}
