package com.security.app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private CustomDetailServices userDetailsServices;

	private JwtAuthEntryPoint authEntryPoint;

	@Autowired
	public SecurityConfig(CustomDetailServices userDetailsServices, JwtAuthEntryPoint authEntryPoint) {
		this.userDetailsServices = userDetailsServices;
		this.authEntryPoint = authEntryPoint;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

//      http.exceptionHandling()
//		.authenticationEntryPoint(authEntryPoint)
//		.and()
//		.sessionManagement()
//		.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//		.and()
//        .authorizeRequests()
//        .requestMatchers("/api/auth/**").permitAll()
//        .anyRequest().authenticated()
//        .and()
//        .httpBasic();

		http.csrf(csrf -> csrf.disable());
		http.authorizeHttpRequests(
				(requests) -> requests.requestMatchers("/api/auth/**").permitAll().anyRequest().authenticated());
		http.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		http.exceptionHandling().authenticationEntryPoint(authEntryPoint);

		http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

//    @Bean
//    public UserDetailsService users() {
//        UserDetails admin = User.builder().username("admin").password("admin").roles("ADMIN").build();
//        UserDetails user = User.builder().username("user").password("admin").roles("USER").build();
//        return new InMemoryUserDetailsManager(admin, user);
//    }

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public JWTAuthenticationFilter jwtAuthenticationFilter() {
		return new JWTAuthenticationFilter();
	}
}
