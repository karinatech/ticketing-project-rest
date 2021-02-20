package com.cybertek.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "confirmation_email")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Where(clause = "is_deleted=false")
public class ConfirmationTokn extends BaseEntity{
    private String token;

    @OneToOne(targetEntity =User.class)
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDate expiredDate;

    public Boolean isTokenValid(LocalDate date){
        LocalDate now =LocalDate.now();
        return date.isEqual(now)|| date.isEqual(now.plusDays(1));
    }
    public ConfirmationTokn(User user){
        this.user=user;
        expiredDate=LocalDate.now().plusDays(1);

        token= UUID.randomUUID().toString();

    }

}
