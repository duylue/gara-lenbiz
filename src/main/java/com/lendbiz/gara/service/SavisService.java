package com.lendbiz.gara.service;

import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.lendbiz.gara.dto.request.SignContractRequest;
import com.lendbiz.gara.dto.response.InfoIdentity;
import com.lendbiz.gara.dto.response.OtpResponse;
import com.lendbiz.gara.dto.response.SignPdfResponse;
import com.lendbiz.gara.dto.response.UserRegisterResponse;

public interface SavisService {

	Optional<InfoIdentity> callPredict(MultipartFile file, InfoIdentity identity, String type);

	Boolean callCheckSelfie(MultipartFile frontId, MultipartFile selfie, String threshold);
	
	Optional<UserRegisterResponse> callRegisterKyc(MultipartFile frontId);

	Optional<OtpResponse> getOtp();

	Boolean validateOtp(String otp);
	
	Optional<SignPdfResponse> signPdf(MultipartFile contract, SignContractRequest request);

}
