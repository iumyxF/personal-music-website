package com.xs.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.xs.assistant.BailianAssistant;
import com.xs.domain.Comment;
import com.xs.domain.CommentAnalysis;
import com.xs.domain.Song;
import com.xs.dto.assistant.CommentAnalysisRequest;
import com.xs.mapper.CommentAnalysisMapper;
import com.xs.service.CommentAnalysisService;
import com.xs.service.CommentService;
import com.xs.service.SongService;
import com.xs.util.JacksonUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author iumyx
 * @description 针对表【comment_analysis(评论分析表)】的数据库操作Service实现
 * @createDate 2025-11-07 09:29:23
 */
@Service
public class CommentAnalysisServiceImpl extends ServiceImpl<CommentAnalysisMapper, CommentAnalysis> implements CommentAnalysisService {

    @Resource
    private CommentAnalysisMapper commentAnalysisMapper;
    @Resource
    private CommentService commentService;
    @Resource
    private SongService songService;
    @Resource
    private BailianAssistant bailianAssistant;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<CommentAnalysis> doAnalysis(Long songId) {
        Song song = songService.getById(songId);
        if (null == song) {
            return null;
        }
        // 获取所有未解析的评论
        List<Comment> commentList = commentService.listBySongIdNotAnalysis(songId);
        if (CollectionUtils.isEmpty(commentList)) {
            return null;
        }
        CommentAnalysisRequest commentAnalysisRequest = getCommentAnalysisRequest(song, commentList);
        // 解析
        String result = bailianAssistant.analysisComment(commentAnalysisRequest);
        if (null == result) {
            return null;
        }
        // 构建CommentAnalysis
        List<ObjectNode> objectNodeList = JacksonUtils.readValueAsList(result, ObjectNode.class);
        if (CollectionUtils.isEmpty(objectNodeList)) {
            return null;
        }
        LocalDateTime now = LocalDateTime.now();
        List<CommentAnalysis> commentAnalysisList = new ArrayList<>(objectNodeList.size());
        objectNodeList.forEach(objectNode -> {
            CommentAnalysis analysis = new CommentAnalysis();
            analysis.setCommentId(objectNode.get("comment_id").asLong());
            analysis.setIsSpam(objectNode.get("is_spam").asBoolean());
            analysis.setSpamType(objectNode.get("spam_type").asText());
            analysis.setTopicType(objectNode.get("topic_type").asText());
            analysis.setEmotionType(objectNode.get("emotion_type").asInt());
            analysis.setEmotionScore(objectNode.get("emotion_score").asDouble());
            analysis.setAnalysisTime(now);
            commentAnalysisList.add(analysis);
        });
        // 标记评论已分析
        commentService.markAnalyzed(commentList.stream().map(Comment::getId).toList());
        saveBatch(commentAnalysisList);
        return commentAnalysisList;
    }

    /**
     * 构建CommentAnalysisRequest
     *
     * @param song        歌曲信息
     * @param commentList 评论列表
     * @return CommentAnalysisRequest
     */
    private CommentAnalysisRequest getCommentAnalysisRequest(Song song, List<Comment> commentList) {
        CommentAnalysisRequest commentAnalysisRequest = new CommentAnalysisRequest();
        commentAnalysisRequest.setSongId(String.valueOf(song.getId()));
        commentAnalysisRequest.setSongName(song.getName());
        commentAnalysisRequest.setLyrics(song.getLyric());
        commentAnalysisRequest.setSongAnalysis(song.getIntroduction());
        List<CommentAnalysisRequest.UserContent> userContentList = new ArrayList<>(commentList.size());
        for (Comment comment : commentList) {
            CommentAnalysisRequest.UserContent userContent = new CommentAnalysisRequest.UserContent();
            userContent.setCommentId(comment.getId().toString());
            userContent.setContent(comment.getContent());
            userContentList.add(userContent);
        }
        commentAnalysisRequest.setUserContentList(userContentList);
        return commentAnalysisRequest;
    }
}
