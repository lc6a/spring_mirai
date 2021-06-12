package com.lc.spring_mirai.web.controller.bot;

import com.lc.spring_mirai.annotation.BotController;
import com.lc.spring_mirai.annotation.EventFilter;
import com.lc.spring_mirai.annotation.RequestMapped;
import com.lc.spring_mirai.util.JsonUtil;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

@BotController
@EventFilter(MessageEvent.class)
public class QingYunKeBotController {

    @Autowired
    private JsonUtil jsonUtil;

    @RequestMapped
    public String answer(MessageChain messages) throws IOException {
        String msg = messages.contentToString();
        byte[] ret = new URL("http://api.qingyunke.com/api.php?key=free&appid=0&msg=" + msg)
                .openConnection().getInputStream().readAllBytes();
        String answer = jsonUtil.fromJson(new String(ret), QingYunKeResp.class).content;
        return "[青云客]" + answer;
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
