package com.security.app.controller;

import java.util.Collections;

import org.apache.catalina.authenticator.SpnegoAuthenticator.AuthenticateAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.security.app.dto.AuthResponseDto;
import com.security.app.dto.LoginDto;
import com.security.app.dto.RegisterDto;
import com.security.app.models.Roles;
import com.security.app.models.UserEntity;
import com.security.app.repository.RoleRepository;
import com.security.app.repository.UserRepository;
import com.security.app.security.JWTGenerator;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	private AuthenticationManager authenticationManager;
	private UserRepository userRepository;
	private RoleRepository roleRepository;
	private PasswordEncoder passwordEncoder;
	private JWTGenerator jwtGenerator;

	@Autowired
	public AuthController(AuthenticationManager authenticationManager,UserRepository userRepository, RoleRepository roleRepository,
			PasswordEncoder passwordEncoder,JWTGenerator jwtGenerator) {
		super();
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager;
		this.jwtGenerator = jwtGenerator;
	}
	
	@PostMapping("/login")
	public ResponseEntity<AuthResponseDto> login(@RequestBody LoginDto loginDto){
		Authentication authentication=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String token=jwtGenerator.generateToken(authentication);
		return new ResponseEntity<>(new AuthResponseDto(token), HttpStatus.OK);
	}

	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody RegisterDto registerDto) {
		if (userRepository.existsByUsername(registerDto.getUsername())) {
			return new ResponseEntity<>("Username is taken", HttpStatus.BAD_REQUEST);
		}
		UserEntity user = new UserEntity();
		user.setUsername(registerDto.getUsername());
		user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
		Roles roles = roleRepository.findByName("USER").get();
		user.setRoles(Collections.singletonList(roles));
		userRepository.save(user);
		return new ResponseEntity<>("User registration Success!", HttpStatus.OK);
	}

}
