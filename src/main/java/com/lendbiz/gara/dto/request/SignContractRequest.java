package com.lendbiz.gara.dto.request;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignContractRequest {

	private String page;
	private String llx;
	private String lly;
	private String urx;
	private String ury;
	private String detail;
	private String reason;
	private String location;
	private String contactInfo;
	private MultipartFile signature;
	
}
