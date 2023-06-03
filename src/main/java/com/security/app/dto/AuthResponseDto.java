package com.security.app.dto;

import lombok.Data;

@Data
public class AuthResponseDto {
	private String assessToken;
	private String tokenType="Bearer ";
	public AuthResponseDto(String assessToken) {
		super();
		this.assessToken = assessToken;
	}
	public String getAssessToken() {
		return assessToken;
	}
	public void setAssessToken(String assessToken) {
		this.assessToken = assessToken;
	}
	public String getTokenType() {
		return tokenType;
	}
	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}
	
}
