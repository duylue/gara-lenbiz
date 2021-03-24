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
@Table(name = "USER_REGISTER_FACE")
@NamedQuery(name = "UserRegisterFace.findAll", query = "SELECT u FROM UserRegisterFace u")
@Setter
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterFace {

	@Id
	private String id;

	@Column(name = "USER_ID")
	private String userId;

	@Column(name = "USER_ATTR_ID")
	private String userAttrId;
	
	@Column(name = "GENDER")
	private String gender;
	
	@Column(name = "AGE")
	private String age;
	
	@Column(name = "LAST_UPDATE")
	private String lastUpdate;
	
	@Column(name = "GENERAL_URL")
	private String genneralUrl;
	
	@Column(name = "CROPPED_GENERAL_URL")
	private String croppedGeneralUrl;
	
	@Column(name = "CROPPED_URL")
	private String croppedUrl;
	
	@Column(name = "REQUEST_ID")
	private String requestId;
	
	@Column(name = "CREATE_DATE")
	private Date createDate;
	
	@Column(name = "RAW_USER_ID")
	private String rawUserId;
	
	@Column(name = "FACE_ATTR_ID")
	private String faceAttrId;
}
