package com.lendbiz.gara.service;

import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.lendbiz.gara.dto.request.GetOtpRequest;
import com.lendbiz.gara.dto.request.ProfileInfoRequest;
import com.lendbiz.gara.dto.request.ReGenerateOtpRequest;
import com.lendbiz.gara.dto.request.SignContractRequest;
import com.lendbiz.gara.dto.request.VerifyOtpRequest;
import com.lendbiz.gara.dto.response.GetOtpResponse;

public interface GaraService {

	String register(ProfileInfoRequest input);

	String upload(MultipartFile[] file, String key, String custId, String hsbt);

	String verifyIdentity(MultipartFile[] file, String key, String custId);

	GetOtpResponse getOtp(GetOtpRequest input) throws Exception;

	String verifyOtp(VerifyOtpRequest input) throws Exception;

	GetOtpResponse regenerateOtp(ReGenerateOtpRequest input) throws Exception;

	// Verify identity savis
	String verifyEkyc(MultipartFile[] files, String key, String custId);

	// Esign savis
	String savisGetOtp(String custId);
	
	String savisVerifyOtp(String custId, String otp);
	
	Optional signContract(String custId, MultipartFile contract, String otp, SignContractRequest request);
}
