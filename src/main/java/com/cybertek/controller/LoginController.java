package com.cybertek.controller;

import com.cybertek.annotation.DefaultExceptionMessage;
import com.cybertek.annotation.ExecutionTimeAnnotation;
import com.cybertek.dto.MailDTO;
import com.cybertek.dto.UserDTO;
import com.cybertek.entity.ConfirmationTokn;
import com.cybertek.entity.ResponseWrapper;
import com.cybertek.entity.User;
import com.cybertek.entity.common.AuthenticationRequest;
import com.cybertek.exception.TicketingProjectException;
import com.cybertek.mapper.MapperUtil;
import com.cybertek.service.ConfirmationTokenService;
import com.cybertek.service.UserService;
import com.cybertek.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Authentication Controller",description = "Authenticate API")
public class LoginController {
	private AuthenticationManager authenticationManager;
	private UserService userService;
	private MapperUtil mapperUtil;
	private JwtUtil jwtUtil;
    private ConfirmationTokenService confirmationTokenService;


	public LoginController(AuthenticationManager authenticationManager, UserService userService, MapperUtil mapperUtil, JwtUtil jwtUtil, ConfirmationTokenService confirmationTokenService) {
		this.authenticationManager = authenticationManager;
		this.userService = userService;
		this.mapperUtil = mapperUtil;
		this.jwtUtil = jwtUtil;
		this.confirmationTokenService = confirmationTokenService;
	}

	@PostMapping("/authenticate")
	@DefaultExceptionMessage(defaultMessage = "Bad credentials")
	@Operation(summary = "Login to authenticate")
	@ExecutionTimeAnnotation
public ResponseEntity<ResponseWrapper>doLogin(@RequestBody AuthenticationRequest authenticationRequest) throws TicketingProjectException {
	String password=authenticationRequest.getPassword();
	String username=authenticationRequest.getUsername();
		UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(username,password);
		authenticationManager.authenticate(authenticationToken);

		UserDTO foundUser=userService.findByUserName(username);
		User convertedUser=mapperUtil.convert(foundUser,new User());
		if(!foundUser.isEnabled()){
			throw new TicketingProjectException("Please verify your user!");
		}
		String jwtToken= jwtUtil.generateToken(convertedUser);
		return ResponseEntity.ok(new ResponseWrapper("Login successful",jwtToken));

}

	@GetMapping("/confirmation")
	@DefaultExceptionMessage(defaultMessage = "Failed to confirm email please try again ")
	@Operation(summary = "Confirm account")
public ResponseEntity<ResponseWrapper>confirmEmail(@RequestParam("token") String token) throws TicketingProjectException {
		ConfirmationTokn confirmationTokn= confirmationTokenService.readByToken(token);
		UserDTO confirmUser = userService.confirm(confirmationTokn.getUser());
		confirmationTokenService.delete(confirmationTokn);
		return ResponseEntity.ok(new ResponseWrapper("User has been confirmed ", confirmUser));

}




}

