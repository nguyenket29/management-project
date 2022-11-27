package com.hau.ketnguyen.it.service.impl.auth;

import com.hau.ketnguyen.it.config.propertise.ApplicationPropertise;
import com.hau.ketnguyen.it.entity.auth.UserVerification;
import com.hau.ketnguyen.it.model.dto.auth.UserDTO;
import com.hau.ketnguyen.it.repository.auth.UserReps;
import com.hau.ketnguyen.it.service.EmailService;
import com.hau.ketnguyen.it.service.UserVerificationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {
    private final UserVerificationService userVerificationService;
    private final JavaMailSender javaMailSender;
    private final UserReps userReps;
    private final String senderMail;
    private final ApplicationPropertise applicationProperties;
    private final ApplicationPropertise.Mail mail;
    private final TemplateEngine templateEngine;
    private final String portFe;

    public EmailServiceImpl(UserVerificationService userVerificationService, JavaMailSender javaMailSender,
                            UserReps userReps, @Value("${spring.mail.username}") String senderMail,
                            ApplicationPropertise applicationProperties, TemplateEngine templateEngine, @Value("${port-fe}") String portFe) {
        this.userVerificationService = userVerificationService;
        this.javaMailSender = javaMailSender;
        this.userReps = userReps;
        this.senderMail = senderMail;
        this.applicationProperties = applicationProperties;
        this.mail = applicationProperties.getMail();
        this.templateEngine = templateEngine;
        this.portFe = portFe;
    }

    /**
     * Send email verify when the user register account
     * @RequestParam : userId, httpSeverletRequest
     * */
    @Override
    public void sendMail(UserDTO userDTO, HttpServletRequest request) throws MessagingException {
        Optional<UserVerification> userVerification = userVerificationService.findByUserId(userDTO.getId());
        String url = ServletUriComponentsBuilder.fromRequestUri(request)
                .replacePath(request.getContextPath())
                .build()
                .toUriString();

        //check if the user has a code
        if (userVerification.isPresent()) {
            String code = userVerification.get().getCode();
            Map<String, Object> variables = new HashMap<>();
            String sub = "Email address verification !";

            String start = StringUtils.join("<a href=", url, ":", portFe , "/pages/activation?code=", code, ">");
            String end = "</a>";
            String activeUrl = StringUtils.join("Click ", start, " here ", end, " to active account !");
            variables.put("url", activeUrl);
            sendEmail(sub, variables, null, null, userDTO);
        }
    }

    @Override
    public void sendEmail(String subject, Map<String, Object> variables, String pathFile,
                          String fileName, UserDTO userDTO) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());
        if (fileName != null && pathFile != null) {
            mimeMessageHelper.addAttachment(fileName, new File(pathFile));
        }
        Context context = new Context();
        context.setVariables(variables);

        String html = templateEngine.process("mail/send-mail", context);
        mimeMessageHelper.setTo(userDTO.getEmail());
        mimeMessageHelper.setText(html, true);
        mimeMessageHelper.setSubject(subject);
        javaMailSender.send(mimeMessage);
    }
}
