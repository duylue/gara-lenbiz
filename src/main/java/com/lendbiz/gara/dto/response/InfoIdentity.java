package com.lendbiz.gara.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InfoIdentity {

	private String identityId;
	private String birthday;
	private String address;
	private String dateIssued;

}
