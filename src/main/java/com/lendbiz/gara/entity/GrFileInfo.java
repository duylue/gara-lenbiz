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
@Table(name = "GRFILEINFO")
@NamedQuery(name = "GrFileInfo.findAll", query = "SELECT g FROM GrFileInfo g")
@Setter
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GrFileInfo {

	@Id
	private String id;

	@Column(name = "CUSTID")
	private String custId;

	@Column(name = "KEY")
	private String key;

	@Column(name = "VALUE")
	private String value;

	@Column(name = "TYPE")
	private String type;

	@Column(name = "DESCRIPTION")
	private String description;
	
	@Column(name = "CREATEDATE")
	private Date createDate;

	@Column(name = "NAME")
	private String name;
}
