package com.lendbiz.gara.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ESIGN_OTP")
@NamedQuery(name = "EsignOtp.findAll", query = "SELECT e FROM EsignOtp e")
@Setter
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EsignOtp {

	@Id
	private String id;
	
	@Column(name = "CUSTID")
	private String custid;
	
	@Column(name = "OTP")
	private String otp;
	
	@Column(name = "CREATE_DATE")
	private Date createDate;
	
	@Column(name = "NRESULT")
	private Long nResult;
}
