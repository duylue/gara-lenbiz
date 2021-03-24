package com.lendbiz.gara.service.impl;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lendbiz.gara.constants.Constants;
import com.lendbiz.gara.constants.ErrorCode;
import com.lendbiz.gara.dto.response.FptIdInfoResponse;
import com.lendbiz.gara.dto.response.FptImgSelfieResponse;
import com.lendbiz.gara.dto.response.FptMobileResponse;
import com.lendbiz.gara.dto.response.FptResponse;
import com.lendbiz.gara.dto.response.LoginResponse;
import com.lendbiz.gara.model.exception.BusinessException;
import com.lendbiz.gara.service.BaseService;
import com.lendbiz.gara.service.FPTService;
import com.lendbiz.gara.utils.Utils;

import net.minidev.json.JSONObject;

@Service("fPTService")
public class FPTServiceImpl extends BaseService implements FPTService {

	public FPTServiceImpl(Environment env) {
		super(env);
		// TODO Auto-generated constructor stub
	}

	@Autowired
	private RestTemplate restTemplate;

	@Override
	public FptResponse<?> login() {
		final String uri = Constants.URI_LOGIN;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		// set data request
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("username", Constants.USER_FPT);
		jsonObject.put("password", Constants.PASS_FPT);

		// call service
		HttpEntity<String> request = new HttpEntity<String>(jsonObject.toString(), headers);

		ResponseEntity<String> responseEntityStr = restTemplate.postForEntity(uri, request, String.class);

		FptResponse<LoginResponse> response = new FptResponse<LoginResponse>();

		if (responseEntityStr.getStatusCodeValue() == 200) {
			ObjectMapper mapper = new ObjectMapper();

			JsonNode root;
			try {
				root = mapper.readTree(responseEntityStr.getBody());
				response.setErrorCode(root.get("ErrorCode").toString());
				response.setMessage(root.get("Message").toString());
				logger.info("message {}", root.get("Message").toString());
				response.setData(mapper.readValue(root.get("Data").toString(), LoginResponse.class));

			} catch (JsonProcessingException e) {
				throw new BusinessException(ErrorCode.FAILED_TO_JSON, ErrorCode.FAILED_TO_JSON_DESCRIPTION);
			}
		} else {
			throw new BusinessException(ErrorCode.FAILED_TO_EXECUTE, ErrorCode.FAILED_TO_EXECUTE_DESCRIPTION);
		}

		return response;
	}

	@Override
	public FptResponse<?> getInfoIndetity(MultipartFile frontImage, MultipartFile backImage, String session) {
		final String uri = Constants.URI_VERIFICATION;

		// set headers
		HttpHeaders headers = new HttpHeaders();
		String authHeader = "Bearer " + session;
		headers.set("Authorization", authHeader);
		String id = Utils.generateId(8);

		// set data in request
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);

		MultiValueMap<String, Object> multiValueMap = new LinkedMultiValueMap<>();
		multiValueMap.add("requestId", id);
		multiValueMap.add("type", "id");

		ByteArrayResource contentsAsResource = convertFile(frontImage);
		multiValueMap.add("frontImage", contentsAsResource);

		ByteArrayResource resourceBack = convertFile(backImage);
		multiValueMap.add("backImage", resourceBack);

		HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(multiValueMap, headers);

		// call service fpt
		ResponseEntity<String> responseEntityStr = restTemplate.postForEntity(uri, request, String.class);
		FptResponse<FptIdInfoResponse> response = new FptResponse<FptIdInfoResponse>();

		// mapping response
		if (responseEntityStr.getStatusCodeValue() == 200) {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode root;
			try {
				root = mapper.readTree(responseEntityStr.getBody());
				response.setErrorCode(root.get("ErrorCode").toString());
				response.setMessage(root.get("Message").toString());

				FptIdInfoResponse info = new FptIdInfoResponse();

				info.setId(mapper.readValue(root.get("Data").get("frontImage").get("id").toString(), String.class));
				info.setIssueDate(
						mapper.readValue(root.get("Data").get("backImage").get("issue_date").toString(), String.class));
				info.setIssueLoc(
						mapper.readValue(root.get("Data").get("backImage").get("issue_loc").toString(), String.class));
				response.setData(info);

			} catch (JsonProcessingException e) {
				throw new BusinessException(ErrorCode.FAILED_TO_JSON, ErrorCode.FAILED_TO_JSON_DESCRIPTION);
			}
		} else {
			throw new BusinessException(ErrorCode.FAILED_TO_EXECUTE, ErrorCode.FAILED_TO_EXECUTE_DESCRIPTION);
		}

		return response;
	}

	@Override
	public FptResponse<?> checkImageSelfie(MultipartFile sourceImage, MultipartFile targetImage, String session) {
		final String uri = Constants.URI_CHECK_SELFIE;

		// set headers
		HttpHeaders headers = new HttpHeaders();
		String authHeader = "Bearer " + session;
		headers.set("Authorization", authHeader);
		String id = Utils.generateId(8);

		// set data in request
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);

		MultiValueMap<String, Object> multiValueMap = new LinkedMultiValueMap<>();
		multiValueMap.add("requestId", id);

		ByteArrayResource contentsAsResource = convertFile(sourceImage);
		multiValueMap.add("sourceImage", contentsAsResource);

		ByteArrayResource resourceBack = convertFile(targetImage);
		multiValueMap.add("targetImage", resourceBack);

		HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(multiValueMap, headers);

		// call service fpt
		ResponseEntity<String> responseEntityStr = restTemplate.postForEntity(uri, request, String.class);
		FptResponse<FptImgSelfieResponse> response = new FptResponse<FptImgSelfieResponse>();

		// mapping response
		if (responseEntityStr.getStatusCodeValue() == 200) {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode root;
			try {
				root = mapper.readTree(responseEntityStr.getBody());
				response.setErrorCode(root.get("ErrorCode").toString());
				response.setMessage(root.get("Message").toString());

				FptImgSelfieResponse info = new FptImgSelfieResponse();
				info.setResult(mapper.readValue(root.get("Data").get("result").toString(), String.class));
				info.setProviderCode(mapper.readValue(root.get("Data").get("providerCode").toString(), String.class));

				response.setData(info);

			} catch (JsonProcessingException e) {
				throw new BusinessException(ErrorCode.FAILED_TO_JSON, ErrorCode.FAILED_TO_JSON_DESCRIPTION);
			}
		} else {
			throw new BusinessException(ErrorCode.FAILED_TO_EXECUTE, ErrorCode.FAILED_TO_EXECUTE_DESCRIPTION);
		}

		return response;
	}

	private ByteArrayResource convertFile(MultipartFile sourceImage) {
		ByteArrayResource resource = null;
		try {
			resource = new ByteArrayResource(sourceImage.getBytes()) {
				@Override
				public String getFilename() {
					return sourceImage.getOriginalFilename();
				}
			};
		} catch (IOException e) {
			throw new BusinessException(ErrorCode.FAILED_TO_FILE, ErrorCode.FAILED_TO_FILE_DESCRIPTION);
		}

		return resource;
	}

	@Override
	public FptResponse<?> verifyMobile(String refIdCode, String mobile, String provider, String session) {
		final String uri = Constants.URI_VERIFI_MOBILE;

		// set headers
		HttpHeaders headers = new HttpHeaders();
		String authHeader = "Bearer " + session;
		headers.set("Authorization", authHeader);

		// Set data request
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("requestId", Utils.generateId(8));
		jsonObject.put("id", refIdCode);
		jsonObject.put("phoneNumber", mobile);
		jsonObject.put("provider", provider);

		// call service
		HttpEntity<String> request = new HttpEntity<String>(jsonObject.toString(), headers);

		ResponseEntity<String> responseEntityStr = restTemplate.postForEntity(uri, request, String.class);

		FptResponse<FptMobileResponse> response = new FptResponse<FptMobileResponse>();

		if (responseEntityStr.getStatusCodeValue() == 200) {
			ObjectMapper mapper = new ObjectMapper();

			JsonNode root;
			try {
				root = mapper.readTree(responseEntityStr.getBody());
				response.setErrorCode(root.get("ErrorCode").toString());
				response.setMessage(root.get("Message").toString());

				FptMobileResponse info = new FptMobileResponse();
				info.setResult(mapper.readValue(root.get("Data").get("result").toString(), String.class));
				info.setProviderCode(mapper.readValue(root.get("Data").get("providerCode").toString(), String.class));

			} catch (JsonProcessingException e) {
				throw new BusinessException(ErrorCode.FAILED_TO_JSON, ErrorCode.FAILED_TO_JSON_DESCRIPTION);
			}
		} else {
			throw new BusinessException(ErrorCode.FAILED_TO_EXECUTE, ErrorCode.FAILED_TO_EXECUTE_DESCRIPTION);
		}

		return response;
	}

}
