package com.cybertek.implementation;

import com.cybertek.entity.ConfirmationTokn;
import com.cybertek.exception.TicketingProjectException;
import com.cybertek.repository.ConfirmationTookenRepo;
import com.cybertek.service.ConfirmationTokenService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {
   private ConfirmationTookenRepo confirmationTookenRepo;
    private JavaMailSender javaMailSender;

    public ConfirmationTokenServiceImpl(ConfirmationTookenRepo confirmationTookenRepo, JavaMailSender javaMailSender) {
        this.confirmationTookenRepo = confirmationTookenRepo;
        this.javaMailSender = javaMailSender;
    }

    @Override
    public ConfirmationTokn save(ConfirmationTokn confirmationTokn) {
        return confirmationTookenRepo.save(confirmationTokn);
    }

    @Override
    @Async
    public void sendEmail(SimpleMailMessage email) {
javaMailSender.send(email);
    }

    @Override
    public ConfirmationTokn readByToken(String token) throws TicketingProjectException {
        ConfirmationTokn confirmationTokn= confirmationTookenRepo.findByToken(token).orElse(null);
       if (confirmationTokn==null){
           throw new TicketingProjectException("This token does not exist");
       }
       if(!confirmationTokn.isTokenValid(confirmationTokn.getExpiredDate())){
           throw new TicketingProjectException("This token has been expired");
       }

        return confirmationTokn;
    }

    @Override
    public void delete(ConfirmationTokn confirmationTokn) {
confirmationTokn.setIsDeleted(true);
confirmationTookenRepo.save(confirmationTokn);

    }
}
