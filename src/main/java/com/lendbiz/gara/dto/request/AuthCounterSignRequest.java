package com.lendbiz.gara.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuthCounterSignRequest {

	private String agreementUUID;
    private String authorizeCode;
    private String billCode;
    
}
