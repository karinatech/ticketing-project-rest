package com.cybertek.controller;

import com.cybertek.annotation.DefaultExceptionMessage;
import com.cybertek.dto.MailDTO;
import com.cybertek.dto.RoleDTO;
import com.cybertek.dto.UserDTO;
import com.cybertek.entity.ConfirmationTokn;
import com.cybertek.entity.ResponseWrapper;
import com.cybertek.entity.User;
import com.cybertek.exception.TicketingProjectException;
import com.cybertek.mapper.MapperUtil;
import com.cybertek.service.ConfirmationTokenService;
import com.cybertek.service.RoleService;
import com.cybertek.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@Tag(name = "User Controller",description = "User API")
public class UserController {
private MapperUtil mapperUtil;
private UserService userService;
private RoleService roleService;
private ConfirmationTokenService confirmationTokenService;
    @Value("${app.local-url")
    private String BASE_URL;

    public UserController(MapperUtil mapperUtil, UserService userService, RoleService roleService, ConfirmationTokenService confirmationTokenService) {
        this.mapperUtil = mapperUtil;
        this.userService = userService;
        this.roleService = roleService;
        this.confirmationTokenService = confirmationTokenService;
    }


    @PostMapping("/create-user")
    @DefaultExceptionMessage(defaultMessage = "Something went wrong")
    @Operation(summary = "Create an account ")
    @PreAuthorize("hasAnyAuthority('Admin')")
    public ResponseEntity<ResponseWrapper> doRegister(@RequestBody UserDTO userDTO) throws TicketingProjectException {
        UserDTO createdUser = userService.save(userDTO);
        sendEmail(createEmail(createdUser));
        return ResponseEntity.ok(new ResponseWrapper("Useer has been createed ", createdUser));
    }

    @GetMapping
    @DefaultExceptionMessage(defaultMessage = "Something went wrong")
    @Operation(summary = "Read all users")
    @PreAuthorize("hasAnyAuthority('Admin')")
    public ResponseEntity<ResponseWrapper>readAll(){
        //Business logic bring the data
        //bind it to view
        List<UserDTO> result= userService.listAllUsers();
        return ResponseEntity.ok(new ResponseWrapper("Successfully retrieved useres ",result));
    }
    @GetMapping("/{username}")
    @DefaultExceptionMessage(defaultMessage = "Something went wrong")
    @Operation(summary = "Read user by userName")
  //Only admin ahould see other profiles or current user can see his/her profiles
    public ResponseEntity<ResponseWrapper>readByUserName(@PathVariable("username") String userName){
       UserDTO user= userService.findByUserName(userName);
        return ResponseEntity.ok(new ResponseWrapper("Successfully retrieved user ",user));
    }

    @PutMapping
    @DefaultExceptionMessage(defaultMessage = "Something went wrong")
    @Operation(summary = "Update user")
    public ResponseEntity<ResponseWrapper>updateUser(@RequestBody UserDTO user) throws TicketingProjectException, AccessDeniedException {
UserDTO userDTO= userService.update(user);
return ResponseEntity.ok(new ResponseWrapper("Successfully updated user ",userDTO));
    }

    @DeleteMapping("/{username")
    @DefaultExceptionMessage(defaultMessage = "Something went wrong")
    @Operation(summary = "Delete user")
    @PreAuthorize("hasAnyAuthority('Admin')")
    public ResponseEntity<ResponseWrapper>deleteUser(@PathVariable("username") String  username) throws TicketingProjectException {
        userService.delete(username);

        return ResponseEntity.ok(new ResponseWrapper("Successfully deleted user "));
    }
    @GetMapping("/role")
    @DefaultExceptionMessage(defaultMessage = "Something went wrong")
    @Operation(summary = "Delete user")
    @PreAuthorize("hasAnyAuthority('Admin','Manager')")
    public ResponseEntity<ResponseWrapper>readByRole(@RequestParam String role){
       List<UserDTO> userDTOList= userService.listAllByRole(role);
        return ResponseEntity.ok(new ResponseWrapper("Successfully read users by role ",userDTOList));
    }

























    private MailDTO createEmail(UserDTO userDTO){
        User user=mapperUtil.convert(userDTO,new User());
        ConfirmationTokn confirmationTokn= new ConfirmationTokn(user);
        confirmationTokn.setIsDeleted(false);
        ConfirmationTokn createdConfirmToken = confirmationTokenService.save(confirmationTokn);
        return MailDTO.builder().emailTo(user.getUserName())
                .token(createdConfirmToken.getToken())
                .subject("Confirm registration")
                .message("To confirm your account please click heree ")
                .url(BASE_URL+"/confirmation?token=").build();


    }
    private void sendEmail(MailDTO mailDTO){
        SimpleMailMessage mailMessage= new SimpleMailMessage();
        mailMessage.setTo(mailDTO.getEmailTo());
        mailMessage.setSubject(mailDTO.getSubject());
        mailMessage.setText(mailDTO.getMessage()+mailDTO.getUrl()+mailDTO.getToken());
        confirmationTokenService.sendEmail(mailMessage);
    }


}
