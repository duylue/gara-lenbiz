package com.lendbiz.gara.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

	private String username;
	private String sessionToken;
	private String isTwoFA;
	private String role;
}
