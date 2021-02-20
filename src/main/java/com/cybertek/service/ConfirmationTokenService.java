package com.cybertek.service;

import com.cybertek.entity.ConfirmationTokn;
import com.cybertek.exception.TicketingProjectException;
import org.springframework.mail.SimpleMailMessage;

public interface ConfirmationTokenService {
    ConfirmationTokn save(ConfirmationTokn confirmationTokn);
    void sendEmail(SimpleMailMessage email);
    ConfirmationTokn readByToken(String token) throws TicketingProjectException;
    void delete(ConfirmationTokn confirmationTokn);



}
