package com.lendbiz.gara.service;

import com.lendbiz.gara.dto.request.AuthCounterSignRequest;
import com.lendbiz.gara.dto.request.PrepareCertificateRequest;
import com.lendbiz.gara.dto.request.PrepareMultipleFileRequest;
import com.lendbiz.gara.dto.request.ReGenerateAuthCodeRequest;
import com.lendbiz.gara.dto.response.FptResponse;

public interface EsignService {

	public FptResponse<?> registerSign(PrepareCertificateRequest request) throws Exception;
	
	public FptResponse<?> uploadMultipleFiles(PrepareMultipleFileRequest request) throws Exception;
	
	public FptResponse<?> verifyOtp(AuthCounterSignRequest request) throws Exception;
	
	public FptResponse<?> regenerateOtp(ReGenerateAuthCodeRequest request) throws Exception;
	
}
