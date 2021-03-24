package com.lendbiz.gara.dto.response;

import org.springframework.stereotype.Controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Controller
@NoArgsConstructor
@AllArgsConstructor
public class FileResponse {

	private String name;
	private String url;
	private String type;
	private long size;
}
