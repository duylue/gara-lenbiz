package com.lendbiz.gara.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileInfoRequest {

	//ten doanh nghiep
	private String fullName;
	private String idCode;
	private String idDate;
	private Float revenue;

	private String exFullName;
	private String dateOfBirth;
	private String refIdCode;
	private String refIdDate;
	private String refIdPlace;

	private String address;
	private String relationAddress;
	private String mobileSms;

	private String email;
	private String refName;
	private String phone;

	private Float mrLoanLimit;
	private String time2rc;
}
