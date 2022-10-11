package com.hau.ketnguyen.it.service.impl;

import com.hau.ketnguyen.it.common.exception.APIException;
import com.hau.ketnguyen.it.entity.auth.User;
import com.hau.ketnguyen.it.entity.auth.UserVerification;
import com.hau.ketnguyen.it.repository.UserReps;
import com.hau.ketnguyen.it.service.EmailService;
import com.hau.ketnguyen.it.service.UserVerificationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {
    private final UserVerificationService userVerificationService;
    private final JavaMailSender javaMailSender;
    private final UserReps userReps;
    private final String senderMail;

    public EmailServiceImpl(UserVerificationService userVerificationService, JavaMailSender javaMailSender,
                            UserReps userReps, @Value("${spring.mail.username}") String senderMail) {
        this.userVerificationService = userVerificationService;
        this.javaMailSender = javaMailSender;
        this.userReps = userReps;
        this.senderMail = senderMail;
    }

    /**
     * Send email verify when the user register account
     * @RequestParam : userId, httpSeverletRequest
     * */
    @Override
    public void sendMail(Integer userId, HttpServletRequest request) throws MessagingException {
        Optional<UserVerification> userVerification = userVerificationService.findByUserId(userId);
        User user = userReps.findById(userId).orElseThrow(() -> APIException.from(HttpStatus.NOT_FOUND).withMessage("User not found"));
        String url = ServletUriComponentsBuilder.fromRequestUri(request)
                .replacePath(request.getContextPath())
                .build()
                .toUriString();
        //check if the user has a code
        if (userVerification.isPresent()) {
            String code = userVerification.get().getCode();
            String msg = getMailBody(url, code);
            MimeMessage mimeMessage = getMimeMessage(user.getEmail(), msg);
            javaMailSender.send(mimeMessage);
        }
    }
    
    /**
     * Create email
     * */
    private MimeMessage getMimeMessage(String email, String msg) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

        mimeMessageHelper.setFrom(senderMail);
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Email address verification !");
        mimeMessageHelper.setText(msg, true);
        return mimeMessage;
    }

    /**
     * Create content email*/
    private String getMailBody(String url, String code){
        String start = StringUtils.join("<a href=", url , "/activation?code=", code, ">");
        String end = "</a>";
        return StringUtils.join("Click ", start, " here ", end, " to active account !");
    }
}
