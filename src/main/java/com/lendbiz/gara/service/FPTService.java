package com.lendbiz.gara.service;

import org.springframework.web.multipart.MultipartFile;

import com.lendbiz.gara.dto.response.FptResponse;

public interface FPTService {
	FptResponse<?> login();

	FptResponse<?> getInfoIndetity(MultipartFile frontImage, MultipartFile backImage, String session);
	
	FptResponse<?> checkImageSelfie(MultipartFile sourceImage, MultipartFile targetImage, String session);

	FptResponse<?> verifyMobile(String refIdCode, String mobile, String provider, String session);
}
