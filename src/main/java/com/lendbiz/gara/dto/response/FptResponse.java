package com.lendbiz.gara.dto.response;

import java.io.Serializable;

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
public class FptResponse<T> implements Serializable{

	private static final long serialVersionUID = 1L;

	protected String ErrorCode;
	protected String Message;

	protected T data;

}
