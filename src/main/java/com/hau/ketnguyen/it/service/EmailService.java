package com.hau.ketnguyen.it.service;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

public interface EmailService {
    void sendMail(Integer userId, HttpServletRequest request) throws MessagingException;
}
