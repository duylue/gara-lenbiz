package com.lendbiz.gara.service;

import org.springframework.web.multipart.MultipartFile;

public interface FilesStorageService {
	
	public String init(String key, String custId);

	public void save(MultipartFile file);
	
	public void saveContract(byte[] file, String path);
	
	public void saveContract(byte[] file, String path, String fileName, String key, String custId);
}
