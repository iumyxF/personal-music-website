SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin`
(
    `id`         bigint                                                        NOT NULL AUTO_INCREMENT COMMENT '主键',
    `username`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '账号',
    `password`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密码',
    `role`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户权限标志',
    `expiration` datetime                                                      NULL DEFAULT NULL COMMENT 'token有效期',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '管理员'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for collect
-- ----------------------------
DROP TABLE IF EXISTS `collect`;
CREATE TABLE `collect`
(
    `id`           bigint     NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id`      bigint     NULL DEFAULT NULL COMMENT '用户id',
    `type`         tinyint(1) NULL DEFAULT NULL COMMENT '收藏类型(1歌单0歌曲)',
    `song_id`      bigint     NULL DEFAULT NULL COMMENT '歌曲id',
    `song_list_id` bigint     NULL DEFAULT NULL COMMENT '歌单id',
    `create_time`  datetime   NULL DEFAULT NULL COMMENT '收藏时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '收藏'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment`
(
    `id`           bigint                                                    NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id`      bigint                                                    NULL DEFAULT NULL COMMENT '用户id',
    `type`         tinyint(1)                                                NULL DEFAULT NULL COMMENT '评论类型(1歌单0歌曲)',
    `song_id`      bigint                                                    NULL DEFAULT NULL COMMENT '歌曲id',
    `song_list_id` bigint                                                    NULL DEFAULT NULL COMMENT '歌单id',
    `content`      longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '评论内容',
    `create_time`  datetime                                                  NULL DEFAULT NULL COMMENT '评论时间',
    `up`           int                                                       NULL DEFAULT 0 COMMENT '评论点赞数',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '评论'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for consumer
-- ----------------------------
DROP TABLE IF EXISTS `consumer`;
CREATE TABLE `consumer`
(
    `id`           bigint                                                        NOT NULL AUTO_INCREMENT COMMENT '主键',
    `username`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '账号',
    `password`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密码',
    `sex`          tinyint(1)                                                    NULL DEFAULT NULL COMMENT '性别(1男0女)',
    `phone_num`    char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci     NULL DEFAULT NULL COMMENT '电话',
    `email`        char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci     NULL DEFAULT NULL COMMENT '电子邮箱',
    `birth`        datetime                                                      NULL DEFAULT NULL COMMENT '生日',
    `introduction` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci     NULL COMMENT '签名',
    `location`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '地区',
    `avatar`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像',
    `create_time`  datetime                                                      NULL DEFAULT NULL COMMENT '创建时间',
    `update_time`  datetime                                                      NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '前端用户'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for list_song
-- ----------------------------
DROP TABLE IF EXISTS `list_song`;
CREATE TABLE `list_song`
(
    `id`           bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
    `song_id`      bigint NULL DEFAULT NULL COMMENT '歌曲id',
    `song_list_id` bigint NULL DEFAULT NULL COMMENT '歌单id',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '歌单包含歌曲列表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for ranks
-- ----------------------------
DROP TABLE IF EXISTS `ranks`;
CREATE TABLE `ranks`
(
    `id`           bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
    `song_list_id` bigint NULL DEFAULT NULL COMMENT '歌单id',
    `consumer_id`  bigint NULL DEFAULT NULL COMMENT '用户id',
    `score`        int    NULL DEFAULT NULL COMMENT '评分',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '评价'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for recent_song
-- ----------------------------
DROP TABLE IF EXISTS `recent_song`;
CREATE TABLE `recent_song`
(
    `id`          bigint   NOT NULL COMMENT '主键',
    `user_id`     bigint   NULL DEFAULT NULL COMMENT '用户id',
    `song_id`     bigint   NULL DEFAULT NULL COMMENT '歌曲id',
    `count`       int      NULL DEFAULT 0 COMMENT '播放次数',
    `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for singer
-- ----------------------------
DROP TABLE IF EXISTS `singer`;
CREATE TABLE `singer`
(
    `id`           bigint                                                        NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '姓名',
    `sex`          tinyint(1)                                                    NULL DEFAULT NULL COMMENT '性别(1男0女)',
    `pic`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像',
    `birth`        datetime                                                      NULL DEFAULT NULL COMMENT '生日',
    `location`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '所属地区',
    `introduction` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci     NULL COMMENT '简介',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '歌手'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for song
-- ----------------------------
DROP TABLE IF EXISTS `song`;
CREATE TABLE `song`
(
    `id`           bigint                                                        NOT NULL AUTO_INCREMENT COMMENT '主键',
    `singer_id`    bigint                                                        NULL DEFAULT NULL COMMENT '歌手id',
    `name`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '歌名',
    `introduction` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci     NULL COMMENT '简介',
    `create_time`  datetime                                                      NULL DEFAULT NULL COMMENT '创建时间',
    `update_time`  datetime                                                      NULL DEFAULT NULL COMMENT '更新时间',
    `pic`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '歌曲图片',
    `lyric`        longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci     NULL COMMENT '歌词',
    `url`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '歌曲地址',
    `play_count`   int                                                           NULL DEFAULT 0 COMMENT '播放量',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '歌曲'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for song_list
-- ----------------------------
DROP TABLE IF EXISTS `song_list`;
CREATE TABLE `song_list`
(
    `id`           bigint                                                        NOT NULL AUTO_INCREMENT COMMENT '主键',
    `title`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标题',
    `pic`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '歌单图片',
    `introduction` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci     NULL COMMENT '歌单简介',
    `style`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '风格',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '歌单'
  ROW_FORMAT = DYNAMIC;

DROP TABLE IF EXISTS `comment_analysis`;
CREATE TABLE `comment_analysis`
(
    `id`            bigint NOT NULL COMMENT '主键',
    `comment_id`    bigint NOT NULL COMMENT '评论ID',
    `emotion_type`  int           DEFAULT NULL COMMENT '情感类型（参考EmotionEnums枚举）',
    `emotion_score` decimal(3, 2) DEFAULT NULL COMMENT '情感倾向得分（置信度）',
    `topic_type`    varchar(255)  DEFAULT NULL COMMENT '评论主题分类',
    `is_spam`       tinyint(1)    DEFAULT 0 COMMENT '是否为垃圾评论（0-正常，1-垃圾）',
    `spam_type`     varchar(50)   DEFAULT NULL COMMENT '垃圾评论类型（如“AD”“ABUSE”“SPAM”）',
    `analysis_time` datetime      DEFAULT NULL COMMENT 'AI分析完成时间',
    `model_version` varchar(100)  DEFAULT NULL COMMENT '分析所用AI模型版本',
    PRIMARY KEY (`id`),
    KEY `idx_comment_id` (`comment_id`) COMMENT '按评论ID查询分析结果的索引',
    KEY `idx_emotion_type` (`emotion_type`) COMMENT '按情感类型筛选的索引',
    KEY `idx_is_spam` (`is_spam`) COMMENT '按是否垃圾评论筛选的索引'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='评论分析表';


SET FOREIGN_KEY_CHECKS = 1;
