package com.lendbiz.gara.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReGenerateAuthCodeRequest {

	private String agreementUUID;
	private String notificationTemplate;
	private String notificationSubject;
	private int authorizeMethod;
}
