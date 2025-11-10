package com.xs.assistant;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xs.domain.Comment;
import com.xs.domain.CommentAnalysis;
import com.xs.domain.Singer;
import com.xs.domain.Song;
import com.xs.service.CommentAnalysisService;
import com.xs.service.CommentService;
import com.xs.service.SingerService;
import com.xs.service.SongService;
import com.xs.util.JacksonUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fzy
 * @description:
 * @date 2025-11-07 14:41
 */
@SpringBootTest
class BailianAssistantTest {

    @Autowired
    SingerService singerService;
    @Autowired
    SongService songService;
    @Autowired
    CommentService commentService;
    @Autowired
    CommentAnalysisService commentAnalysisService;

    @Test
    void analysisComment() {
        // 初始化数据
        Singer singer = initSinger();
        Song song = initSong(singer.getId());
        initComment(song.getId(),
                List.of("这首歌可不只是比喻爱情噢，当你执着做一件事的时候结果往往会跟你想的不一样，甚至没有结果，但不要灰心丧气，葡萄一定会成熟的，就算不成熟也可以用来酿酒，自醉自救",
                        "‘我知日后 路上或没有更美的邂逅’。骆夏 向暖 很好听",
                        "祝大家冬至快乐",
                        "我是渣渣辉，是兄弟就来砍我")
        );
        List<CommentAnalysis> commentAnalysisList = commentAnalysisService.doAnalysis(song.getId());
        if (CollectionUtils.isEmpty(commentAnalysisList)) {
            System.out.println("分析结果 null");
        } else {
            System.out.println(JacksonUtils.writeValueAsString(commentAnalysisList));
        }
    }

    Singer initSinger() {
        Singer singer = singerService.getOne(new LambdaQueryWrapper<Singer>()
                .eq(Singer::getName, "陈奕迅"));
        if (null == singer) {
            singer = new Singer();
            singer.setName("陈奕迅");
            singer.setSex(1);
            singerService.save(singer);
        }
        return singer;
    }

    Song initSong(Long singerId) {
        Song song = songService.getOne(new LambdaQueryWrapper<Song>()
                .eq(Song::getName, "葡萄成熟时"));
        if (null == song) {
            song = new Song();
            song.setLyric("""
                    差不多冬至一早一晚还是有雨
                    当初的坚持现已令你很怀疑
                    很怀疑你最尾等到只有这枯枝
                    苦恋几多次悉心栽种全力灌注
                    所得竟不如别个后辈收成时
                    这一次你真的很介意
                    但见旁人谈情何引诱
                    问到何时葡萄先熟透
                    你要静候再静候
                    就算失收始终要守
                    日后尽量别教今天的泪白流
                    留低击伤你的石头
                    从错误里吸收
                    也许丰收月份尚未到你也得接受
                    或者要到你将爱酿成醇酒
                    时机先至熟透
                    应该怎么爱可惜书里从没记载
                    终于摸出来但岁月却不回来
                    不回来错过了春天可会再花开
                    一千种恋爱一些需要情泪灌溉
                    枯毁的温柔
                    在最后会长回来
                    错的爱乃必经的配菜
                    但见旁人谈情何引诱
                    问到何时葡萄先熟透
                    你要静候再静候
                    就算失收始终要守
                    日后尽量别教今天的泪白流
                    留低击伤你的石头
                    从错误里吸收
                    也许丰收月份尚未到你也得接受
                    或者要到你将爱酿成醇酒
                    时机先至熟透
                    想想天的一边亦有个某某在等候
                    wo wo wo yeah yeah~~
                    一心只等葡萄熟透
                    尝杯酒wo wowow
                    别让寂寞害你伤得一夜白头
                    仍得不需要的自由
                    和最耀眼伤口
                    我知日后路上或没有更美的邂逅
                    但当你智慧都蕴酿成红酒
                    仍可一醉自救
                    谁都辛酸过
                    哪个没有""");
            song.setName("葡萄成熟时");
            song.setSingerId(singerId);
            song.setIntroduction("""
                    《葡萄成熟时》是陈奕迅的一首歌曲，歌词中运用了“葡萄成熟”这一比喻，讲述了一段经历挫折与等待的爱情故事。
                    歌词解读：
                    “差不多冬至一早一晚还是有雨”，这里借用了冬天寒冷、阴雨连绵的景象，映射出主人公内心的孤独和落寞。
                    “当初的坚持现已令你很怀疑”，表明主人公在感情道路上遇到了困境，开始对自己的选择产生动摇。
                    “苦恋几多次悉心栽种全力灌注，所得竟不如别个后辈收成时”，通过对比他人收获爱情的甜蜜，主人公感叹自己付出的努力并未得到相应的回报。
                    “这一次你真的很介意”，表达了主人公内心对于失败的不甘心。
                    “但见旁人谈情何引诱，问到何时葡萄先熟透。你要静候再静候，就算失收始终要守”，这里强调了即使面临失败也要保持耐心与坚持的重要性。
                    “日后尽量别教今天的泪白流，留低击伤你的石头，从错误里吸收”，意味着要从过去的经历中吸取教训，并勇敢面对未来。
                    “也许丰收月份尚未到你也得接受，或者要到你将爱酿成醇酒，时机先至熟透”，这句话传达了一个信息：有时候我们需要更多时间去等待真爱的到来。
                    “应该怎么爱可惜书里从没记载，终于摸出来但岁月却不回来”，说明真正的爱情并没有固定的模式，每个人都需要在实践中摸索适合自己的方式。
                    “不回来错过了春天可会再花开，一千种恋爱一些需要情泪灌溉”，这里暗示了虽然错过了某些机会，但只要继续努力，依然有机会获得幸福。
                    “枯毁的温柔，在最后会长回来。错的爱乃必经的配菜”，表示即使是失败的感情经历，也能成为成长道路上宝贵的财富。
                    “想想天的一边亦有个某某在等候”，鼓励人们相信总会有人在等待着自己。
                    “一心只等葡萄熟透，尝杯酒。别让寂寞害你伤得一夜白头”，提醒大家不要因为寂寞而做出错误的选择。
                    “仍得不需要的自由，和最耀眼伤口。我知日后路上或没有更美的邂逅，但当你智慧都酝酿成红酒，仍可一醉自救”，这里表达了即使经历过伤痛，也能通过自我提升找到新的希望。""");
            songService.save(song);
        }
        return song;
    }

    void initComment(Long songId, List<String> contentList) {
        List<Comment> list = commentService.list(new LambdaQueryWrapper<Comment>()
                .eq(Comment::getSongId, songId));
        if (!CollectionUtils.isEmpty(list)) {
            return;
        }
        List<Comment> comments = new ArrayList<>(contentList.size());
        contentList.forEach(content -> {
            Comment c = new Comment();
            c.setUserId(RandomUtils.nextLong());
            c.setSongId(songId);
            c.setContent(content);
            comments.add(c);
        });
        commentService.saveBatch(comments);
    }
}