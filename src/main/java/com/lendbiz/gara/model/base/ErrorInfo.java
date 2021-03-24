package com.lendbiz.gara.model.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorInfo extends BaseController {
	private int status;
	private String error;
	private String errorCode;
	private String errorDesc;
	private String path;

	//Optional
//	private Object data;
}
