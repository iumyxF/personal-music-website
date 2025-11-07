package com.xs.assistant;

import com.alibaba.dashscope.app.Application;
import com.alibaba.dashscope.app.ApplicationParam;
import com.alibaba.dashscope.app.ApplicationResult;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.xs.dto.assistant.CommentAnalysisRequest;
import com.xs.util.JacksonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author fzy
 * @description: 百炼平台
 * @date 2025-11-07 14:20
 */
@Slf4j
@Component
public class BailianAssistant {

    @Value("${bailian.apiKey}")
    private String apiKey;
    @Value("${bailian.appId}")
    private String appId;

    public String analysisComment(CommentAnalysisRequest request) {
        String requestContent = JacksonUtils.writeValueAsString(request);
        log.info("requestContent: {}", requestContent);
        try {
            ApplicationParam param = ApplicationParam.builder()
                    .apiKey(apiKey)
                    .appId(appId)
                    .prompt(requestContent)
                    .build();
            Application application = new Application();
            ApplicationResult result = application.call(param);
            log.info("result: {}", result);
            return result.getOutput().getText();
        } catch (NoApiKeyException | InputRequiredException e) {
            throw new RuntimeException(e);
        }
    }
}
