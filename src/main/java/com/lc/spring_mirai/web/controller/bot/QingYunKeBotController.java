package com.lc.spring_mirai.web.controller.bot;

import com.lc.spring_mirai.annotation.AtMeFilter;
import com.lc.spring_mirai.annotation.BotController;
import com.lc.spring_mirai.annotation.EventFilter;
import com.lc.spring_mirai.annotation.RequestMapped;
import com.lc.spring_mirai.util.JsonUtil;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChain;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@BotController
@EventFilter(MessageEvent.class)
public class QingYunKeBotController extends BaseBotController {

    @Autowired
    private JsonUtil jsonUtil;

    @RequestMapped
    @AtMeFilter
    public String answer(MessageChain messages) throws IOException {
        messages.removeIf(it -> it instanceof At);
        String msg = messages.contentToString();
        String urlEncodeMsg = URLEncoder.encode(msg, StandardCharsets.UTF_8);
        byte[] ret = new URL("http://api.qingyunke.com/api.php?key=free&appid=0&msg=" + urlEncodeMsg)
                .openConnection().getInputStream().readAllBytes();
        String answer = jsonUtil.fromJson(new String(ret), QingYunKeResp.class).content;
        return "[青云客]" + answer;
    }

    private String showName = "青云客智能聊天";

    @NotNull
    @Override
    public String getShowName() {
        return showName;
    }

    @Override
    public void setShowName(@NotNull String showName) {
        this.showName = showName;
    }

    private static class QingYunKeResp {
        private Integer result;
        private String content;

        public Integer getResult() {
            return result;
        }

        public void setResult(Integer result) {
            this.result = result;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
