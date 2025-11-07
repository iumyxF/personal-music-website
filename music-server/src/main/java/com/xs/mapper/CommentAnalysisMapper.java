package com.xs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xs.domain.CommentAnalysis;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author iumyx
 * @description 针对表【comment_analysis(评论分析表)】的数据库操作Mapper
 * @createDate 2025-11-07 09:29:23
 * @Entity com.xs.domain.CommentAnalysis
 */
@Repository
@Mapper
public interface CommentAnalysisMapper extends BaseMapper<CommentAnalysis> {

}
