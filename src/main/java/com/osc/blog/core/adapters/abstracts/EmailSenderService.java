package com.osc.blog.core.adapters.abstracts;

public interface EmailSenderService {

    void send(String to, String email, String subject);
    void sendConfirmationEmail(String to, String name, String token);

}
