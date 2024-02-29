package com.letter2sea.be.common.util;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class MailSender {
    private static final String MAIL_TITLE = "[바다편지] 당신의 편지에 답장이 도착하였습니다!";
    private static final String MAIL_TEMPLATE =
        "안녕하세요, 익명의 소라게님!<br><br>"
            + "소라게님이 바다로 보낸 편지 '%s'가 누군가에게 닿아 답장을 받았어요.<br><br>"
            + "바다편지 서비스에 접속해서 답장 내용을 확인해 보세요!<br><br>"
            + "https://sea-letter-front.vercel.app<br><br>"
            + "감사합니다.";
    private final AmazonSimpleEmailService amazonSimpleEmailService;
    private final TemplateEngine htmlTemplateEngine;
    private final String from;

    public MailSender(AmazonSimpleEmailService amazonSimpleEmailService,
        TemplateEngine htmlTemplateEngine, @Value("${aws.ses.from}") String from) {
        this.amazonSimpleEmailService = amazonSimpleEmailService;
        this.htmlTemplateEngine = htmlTemplateEngine;
        this.from = from;
    }

    public void send(String letterTitle, String to) {
        Map<String, Object> variables = Map.of("data", String.format(MAIL_TEMPLATE, letterTitle));

        String content = htmlTemplateEngine.process("index", createContext(variables));
        SendEmailRequest sendEmailRequest = createSendEmailRequest(content, to);

        amazonSimpleEmailService.sendEmail(sendEmailRequest);

    }

    private Context createContext(Map<String, Object> variables) {
        Context context = new Context();
        context.setVariables(variables);

        return context;
    }

    private SendEmailRequest createSendEmailRequest(String content, String to) {
        return new SendEmailRequest()
            .withDestination(new Destination().withToAddresses(to))
            .withSource(from)
            .withMessage(new Message()
                .withSubject(new Content().withCharset(StandardCharsets.UTF_8.name()).withData(MAIL_TITLE))
                .withBody(new Body().withHtml(new Content().withCharset(StandardCharsets.UTF_8.name()).withData(content)))
            );
    }
}
