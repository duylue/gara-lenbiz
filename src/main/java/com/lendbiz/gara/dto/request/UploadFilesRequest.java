package com.lendbiz.gara.dto.request;

import java.util.List;

import com.lendbiz.gara.model.FileInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UploadFilesRequest {
	private String custId;
	private List<FileInfo> files;

}
