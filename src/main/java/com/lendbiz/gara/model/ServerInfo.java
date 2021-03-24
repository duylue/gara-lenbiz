package com.lendbiz.gara.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ServerInfo {
	private String serverName;
	private String serverType;
	private String serverRole;
	private String userId;
}
