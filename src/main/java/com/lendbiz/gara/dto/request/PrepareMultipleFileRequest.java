package com.lendbiz.gara.dto.request;

import java.util.List;

import com.lendbiz.gara.model.MultipleSigningFileData;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PrepareMultipleFileRequest {

	private String agreementUUID;
	private int authorizeMethod;
	private String authorizeCode;
	private String notificationTemplate;
	private String notificationSubject;
	private List<MultipleSigningFileData> listOfSigningFileData;
	
}
