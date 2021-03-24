package com.lendbiz.gara.dto.response;

import org.springframework.stereotype.Controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Controller
@AllArgsConstructor
@NoArgsConstructor
public class FptImgSelfieResponse {

	private String result;
	private String providerCode;
}
