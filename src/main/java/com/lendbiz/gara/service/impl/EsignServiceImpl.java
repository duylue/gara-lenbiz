package com.lendbiz.gara.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lendbiz.gara.constants.Constants;
import com.lendbiz.gara.constants.ErrorCode;
import com.lendbiz.gara.constants.JsonMapper;
import com.lendbiz.gara.dto.request.AuthCounterSignRequest;
import com.lendbiz.gara.dto.request.PrepareCertificateRequest;
import com.lendbiz.gara.dto.request.PrepareMultipleFileRequest;
import com.lendbiz.gara.dto.request.ReGenerateAuthCodeRequest;
import com.lendbiz.gara.dto.response.FptResponse;
import com.lendbiz.gara.dto.response.GetOtpResponse;
import com.lendbiz.gara.dto.response.SignCloudResponse;
import com.lendbiz.gara.model.exception.BusinessException;
import com.lendbiz.gara.service.BaseService;
import com.lendbiz.gara.service.EsignService;

import net.minidev.json.JSONObject;

@Service("esignService")
public class EsignServiceImpl extends BaseService implements EsignService {

	public EsignServiceImpl(Environment env) {
		super(env);
	}
	@Autowired
	private RestTemplate restTemplate;

	@Override
	public FptResponse<?> registerSign(PrepareCertificateRequest request) throws Exception {
		logger.info("---------Start register sign---------------");
		final String uri = Constants.ESIGN_REGISTER_URL;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		// set data request
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("agreementUUID", request.getAgreementUUID());
		jsonObject.put("personalName", request.getPersonalName());
		jsonObject.put("personalID", request.getPersonalID());
		jsonObject.put("citizenID", request.getCitizenID());
		jsonObject.put("location", request.getLocation());

		jsonObject.put("stateProvince", request.getStateProvince());
		jsonObject.put("country", request.getCountry());
		jsonObject.put("frontSideOfIDDocument", request.getFrontSideOfIDDocument());
		jsonObject.put("backSideOfIDDocument", request.getBackSideOfIDDocument());
		jsonObject.put("authorizationEmail", request.getAuthorizationEmail());
		jsonObject.put("authorizationMobileNo", request.getAuthorizationMobileNo());

		logger.info("[ESIGN REGISTER] request register esign: {}", JsonMapper.writeValueAsString(jsonObject));
		// call service
		HttpEntity<Object> input = new HttpEntity<Object>(jsonObject, headers);

		ResponseEntity<String> responseEntityStr = restTemplate.postForEntity(uri, input, String.class);
		logger.info("[REGISTER ESIGN] register success with response: {}", JsonMapper.writeValueAsString(responseEntityStr));
		FptResponse<String> response = new FptResponse<String>();

		if (responseEntityStr.getStatusCodeValue() == 200) {
		} else {
			throw new BusinessException(ErrorCode.FAILED_TO_EXECUTE, ErrorCode.FAILED_TO_EXECUTE_DESCRIPTION);
		}

		return response;
	}

	@Override
	public FptResponse<?> uploadMultipleFiles(PrepareMultipleFileRequest request) throws Exception {
		logger.info("-----------Start uploadMultipleFiles Contract----------");
		final String uri = Constants.ESIGN_UPLOAD_URL;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		// set data request
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("agreementUUID", request.getAgreementUUID());
		jsonObject.put("authorizeMethod", request.getAuthorizeMethod());
		jsonObject.put("authorizeCode", request.getAuthorizeCode());
		jsonObject.put("notificationTemplate", request.getNotificationTemplate());
		jsonObject.put("notificationSubject", request.getNotificationSubject());
		jsonObject.put("listOfSigningFileData", request.getListOfSigningFileData());

		// call service
		HttpEntity<Object> input = new HttpEntity<Object>(jsonObject, headers);

		ResponseEntity<String> responseEntityStr = restTemplate.postForEntity(uri, input, String.class);

		FptResponse<GetOtpResponse> response = new FptResponse<GetOtpResponse>();

		if (responseEntityStr.getStatusCodeValue() == 200) {
			ObjectMapper mapper = new ObjectMapper();
			logger.info("[UPLOAD CONTRACT] upload sucess with response: {}",
					JsonMapper.writeValueAsString(responseEntityStr));
			JsonNode root;
			try {
				GetOtpResponse otp = new GetOtpResponse();
				root = mapper.readTree(responseEntityStr.getBody());
				otp.setBillCode(mapper.readValue(root.get("data").toString(), String.class));
				response.setData(otp);

			} catch (JsonProcessingException e) {
				throw new BusinessException(ErrorCode.FAILED_TO_JSON, ErrorCode.FAILED_TO_JSON_DESCRIPTION);
			}
		} else {
			throw new BusinessException(ErrorCode.FAILED_TO_EXECUTE, ErrorCode.FAILED_TO_EXECUTE_DESCRIPTION);
		}

		return response;
	}

	@Override
	public FptResponse<?> verifyOtp(AuthCounterSignRequest request) throws Exception {
		// TODO Auto-generated method stub
		logger.info("---------Start verifyOtp---------");
		final String uri = Constants.ESIGN_VERIFY_OTP;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		// set data request
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("agreementUUID", request.getAgreementUUID());
		jsonObject.put("authorizeCode", request.getAuthorizeCode());
		jsonObject.put("billCode", request.getBillCode());

		// call service
		HttpEntity<Object> input = new HttpEntity<Object>(jsonObject, headers);

		ResponseEntity<String> responseEntityStr = restTemplate.postForEntity(uri, input, String.class);

		FptResponse<SignCloudResponse> response = new FptResponse<SignCloudResponse>();

		if (responseEntityStr.getStatusCodeValue() == 200) {
			ObjectMapper mapper = new ObjectMapper();
			logger.info("[VERIFY OTP] verifyOtp success with Otp: {}",
					JsonMapper.writeValueAsString(responseEntityStr));
			JsonNode root;
			try {
				root = mapper.readTree(responseEntityStr.getBody());
				response.setData(mapper.readValue(root.get("data").toString(), SignCloudResponse.class));

			} catch (JsonProcessingException e) {
				throw new BusinessException(ErrorCode.FAILED_TO_JSON, ErrorCode.FAILED_TO_JSON_DESCRIPTION);
			}
		} else {
			throw new BusinessException(ErrorCode.FAILED_TO_EXECUTE, ErrorCode.FAILED_TO_EXECUTE_DESCRIPTION);
		}

		return response;
	}

	@Override
	public FptResponse<?> regenerateOtp(ReGenerateAuthCodeRequest request) throws Exception {
		// TODO Auto-generated method stub
		logger.info("----------Start regenerateOtp---------");
		final String uri = Constants.ESIGN_REGENERATE_OTP;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		// set data request
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("agreementUUID", request.getAgreementUUID());
		jsonObject.put("authorizeMethod", request.getAuthorizeMethod());
		jsonObject.put("notificationTemplate", request.getNotificationTemplate());
		jsonObject.put("notificationSubject", request.getNotificationSubject());

		// call service
		HttpEntity<Object> input = new HttpEntity<Object>(jsonObject, headers);

		ResponseEntity<String> responseEntityStr = restTemplate.postForEntity(uri, input, String.class);

		FptResponse<GetOtpResponse> response = new FptResponse<GetOtpResponse>();

		if (responseEntityStr.getStatusCodeValue() == 200) {
			ObjectMapper mapper = new ObjectMapper();
			logger.info("[REGENERATE OTP] regenerateOtp success: {}", JsonMapper.writeValueAsString(responseEntityStr));
			JsonNode root;
			try {
				GetOtpResponse otp = new GetOtpResponse();
				root = mapper.readTree(responseEntityStr.getBody());
				otp.setBillCode(mapper.readValue(root.get("data").toString(), String.class));
				response.setData(otp);

			} catch (JsonProcessingException e) {
				throw new BusinessException(ErrorCode.FAILED_TO_JSON, ErrorCode.FAILED_TO_JSON_DESCRIPTION);
			}
		} else {
			throw new BusinessException(ErrorCode.FAILED_TO_EXECUTE, ErrorCode.FAILED_TO_EXECUTE_DESCRIPTION);
		}

		return response;
	}

}
