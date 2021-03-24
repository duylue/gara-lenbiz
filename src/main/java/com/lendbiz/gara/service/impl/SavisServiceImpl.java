package com.lendbiz.gara.service.impl;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lendbiz.gara.constants.Constants;
import com.lendbiz.gara.constants.ErrorCode;
import com.lendbiz.gara.constants.JsonMapper;
import com.lendbiz.gara.dto.request.SignContractRequest;
import com.lendbiz.gara.dto.response.InfoIdentity;
import com.lendbiz.gara.dto.response.OtpResponse;
import com.lendbiz.gara.dto.response.SignPdfResponse;
import com.lendbiz.gara.dto.response.UserRegisterResponse;
import com.lendbiz.gara.model.exception.BusinessException;
import com.lendbiz.gara.service.BaseService;
import com.lendbiz.gara.service.SavisService;
import com.lendbiz.gara.utils.StringUtil;

@Service
public class SavisServiceImpl extends BaseService implements SavisService {

	@Autowired
	private RestTemplate restTemplate;
	private final String isSelfie = "TRUE";

	public SavisServiceImpl(Environment env) {
		super(env);
	}

	@Override
	public Optional<InfoIdentity> callPredict(MultipartFile file, InfoIdentity identity, String type) {
		logger.info("---------Start call predict---------------");
		final String uri = Constants.ESIGN_PREDICT;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		headers.set(Constants.ESIGN_API_KEY, Constants.ESIGN_VALUE_HEADER);

		MultiValueMap<String, Object> multiValueMap = new LinkedMultiValueMap<>();
		ByteArrayResource contentsAsResource = convertFile(file);
		multiValueMap.add("input", contentsAsResource);

		HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(multiValueMap, headers);
		ResponseEntity<String> responseEntityStr = restTemplate.postForEntity(uri, request, String.class);

		// mapping response
		if (responseEntityStr.getStatusCodeValue() == 200) {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode root;
			logger.info("[Call perdict] response : {}", responseEntityStr.getBody());
			if (Constants.TYPE_FRONT_IDENTITY.equalsIgnoreCase(type)) {
				try {
					root = mapper.readTree(responseEntityStr.getBody());
					identity.setIdentityId(root.get("output").get(0).get("id").get("value").asText());
					identity.setBirthday(root.get("output").get(0).get("ngay_sinh").get("normalized")
							.get("value_unidecode").asText());
					identity.setAddress(root.get("output").get(0).get("nguyen_quan").get("value_unidecode").asText());

				} catch (JsonProcessingException e) {
					throw new BusinessException(ErrorCode.FAILED_TO_JSON, ErrorCode.FAILED_TO_JSON_DESCRIPTION);
				}
			} else if (Constants.TYPE_BACK_IDENTITY.equalsIgnoreCase(type)) {
				try {
					root = mapper.readTree(responseEntityStr.getBody());
					identity.setDateIssued(root.get("output").get(0).get("ngay_cap").get("normalized")
							.get("value_unidecode").asText());
				} catch (JsonProcessingException e) {
					throw new BusinessException(ErrorCode.FAILED_TO_JSON, ErrorCode.FAILED_TO_JSON_DESCRIPTION);
				}
			}

		} else {
			throw new BusinessException(ErrorCode.FAILED_TO_EXECUTE, ErrorCode.FAILED_TO_EXECUTE_DESCRIPTION);
		}
		logger.info("[Call perdict] infoIdentity result : {}", JsonMapper.writeValueAsString(identity));

		return Optional.of(identity);
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
	public Boolean callCheckSelfie(MultipartFile frontId, MultipartFile selfie, String threshold) {
		logger.info("---------Start call face_general---------------");
		final String uri = Constants.ESIGN_FACE_GENERAL;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		headers.set(Constants.ESIGN_API_KEY, Constants.ESIGN_VALUE_HEADER);

		MultiValueMap<String, Object> multiValueMap = new LinkedMultiValueMap<>();
		ByteArrayResource imageCard = convertFile(frontId);
		multiValueMap.add("image_card", imageCard);
		ByteArrayResource imageGeneral = convertFile(selfie);
		multiValueMap.add("image_general", imageGeneral);
		multiValueMap.add("threshold", threshold);

		HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(multiValueMap, headers);

		ResponseEntity<String> responseEntityStr = restTemplate.postForEntity(uri, request, String.class);

		// mapping response
		if (responseEntityStr.getStatusCodeValue() == 200) {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode root;
			logger.info("[Call face_general] response : {}", responseEntityStr.getBody());
			try {
				root = mapper.readTree(responseEntityStr.getBody());
				if (root.get("output").get("is_matched").get("value").asText().equalsIgnoreCase(isSelfie)) {
					return true;
				} else {
					return false;
				}
			} catch (JsonProcessingException e) {
				throw new BusinessException(ErrorCode.FAILED_TO_JSON, ErrorCode.FAILED_TO_JSON_DESCRIPTION);
			}

		} else {
			throw new BusinessException(ErrorCode.FAILED_TO_EXECUTE, ErrorCode.FAILED_TO_EXECUTE_DESCRIPTION);
		}
	}

	@Override
	public Optional<OtpResponse> getOtp() {
		// TODO Auto-generated method stub
		logger.info("---------Start call api get otp---------------");
		final String uri = Constants.GET_OTP;
		HttpHeaders headers = new HttpHeaders();
//		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		headers.set(Constants.OTP_API_KEY, Constants.OTP_VALUE_HEADER);
		headers.set("Authorization", "Basic Og==");

		ResponseEntity<String> responseEntityStr = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(headers),
				String.class);

		// mapping response
		if (responseEntityStr.getStatusCodeValue() == 200) {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode root;
			logger.info("[Call api get otp] response : {}", responseEntityStr.getBody());
			try {
				root = mapper.readTree(responseEntityStr.getBody());
				OtpResponse response = new OtpResponse();
				response.setErrorCode(root.get("code").toString());
				response.setMessage(root.get("message").toString());
				response.setOtp(root.get("data").toString());

				return Optional.of(response);
			} catch (JsonProcessingException e) {
				throw new BusinessException(ErrorCode.FAILED_TO_JSON, ErrorCode.FAILED_TO_JSON_DESCRIPTION);
			}

		} else {
			throw new BusinessException(ErrorCode.FAILED_TO_EXECUTE, ErrorCode.FAILED_TO_EXECUTE_DESCRIPTION);
		}
	}

	@Override
	public Boolean validateOtp(String otp) {
		logger.info("---------Start call validate otp---------------");
		final String uri = Constants.VALIDATE_OTP;
		HttpHeaders headers = new HttpHeaders();
		headers.set(Constants.OTP_API_KEY, Constants.OTP_VALUE_HEADER);
		headers.set("verificationCode", otp);
		headers.set("Authorization", "Basic Og==");

		ResponseEntity<String> responseEntityStr = restTemplate.postForEntity(uri, new HttpEntity<>(headers),
				String.class);

		// mapping response
		if (responseEntityStr.getStatusCodeValue() == 200) {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode root;
			logger.info("[Call api validate otp] response : {}", responseEntityStr.getBody());
			try {
				root = mapper.readTree(responseEntityStr.getBody());
				if (root.get("data").asBoolean()) {
					return true;
				} else {
					return false;
				}
			} catch (JsonProcessingException e) {
				throw new BusinessException(ErrorCode.FAILED_TO_JSON, ErrorCode.FAILED_TO_JSON_DESCRIPTION);
			}

		} else {
			throw new BusinessException(ErrorCode.FAILED_TO_EXECUTE, ErrorCode.FAILED_TO_EXECUTE_DESCRIPTION);
		}
	}

	@Override
	public Optional<UserRegisterResponse> callRegisterKyc(MultipartFile frontId) {
		logger.info("---------Start call register user face---------------");
		final String uri = Constants.ESIGN_REGISTER;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		headers.set(Constants.ESIGN_API_KEY, Constants.ESIGN_VALUE_HEADER);

		MultiValueMap<String, Object> multiValueMap = new LinkedMultiValueMap<>();
		ByteArrayResource imageCard = convertFile(frontId);
		multiValueMap.add("image", imageCard);
		multiValueMap.add("metadata", Constants.METADATA);
		multiValueMap.add("user_id", "269EC824-0D4C-46E3-8749-9F4CCDCB12A7");

		HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(multiValueMap, headers);

		ResponseEntity<String> responseEntityStr = restTemplate.postForEntity(uri, request, String.class);

		// mapping response
		if (responseEntityStr.getStatusCodeValue() == 200) {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode root;
			logger.info("[Call register user face] response : {}", responseEntityStr.getBody());
			try {
				root = mapper.readTree(responseEntityStr.getBody());
				UserRegisterResponse result = mappingObRegiset(root);
				return Optional.of(result);
			} catch (JsonProcessingException e) {
				throw new BusinessException(ErrorCode.FAILED_TO_JSON, ErrorCode.FAILED_TO_JSON_DESCRIPTION);
			}

		} else {
			throw new BusinessException(ErrorCode.FAILED_TO_EXECUTE, ErrorCode.FAILED_TO_EXECUTE_DESCRIPTION);
		}
	}

	private UserRegisterResponse mappingObRegiset(JsonNode root) {
		UserRegisterResponse response = new UserRegisterResponse();
		response.setUserAttrId(root.get("output").get("user_attributes").get("id").asText());
		response.setUserId(root.get("output").get("user_attributes").get("user_id").asText());
		response.setAge(root.get("output").get("user_attributes").get("age").asText());
		response.setCroppedGeneralUrl(root.get("output").get("user_attributes").get("cropped_general_url").asText());
		response.setCroppedUrl(root.get("output").get("face_attributes").get("cropped_url").asText());
		response.setFaceAttrId(root.get("output").get("face_attributes").get("id").asText());
		response.setGender(root.get("output").get("user_attributes").get("gender").asText());
		response.setGeneralUrl(root.get("output").get("user_attributes").get("general_url").asText());
		response.setImageUrl(root.get("output").get("face_attributes").get("image_url").asText());
		response.setLastUpdate(root.get("output").get("user_attributes").get("last_updated").asText());
		response.setRawUserId(root.get("output").get("face_attributes").get("raw_user_id").asText());
		response.setRequestId(root.get("request_id").asText());

		return response;
	}

	@Override
	public Optional<SignPdfResponse> signPdf(MultipartFile contract, SignContractRequest signRequest) {
		logger.info("---------Start call sign pdf---------------");
		final String uri = Constants.SIGN_PDF;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		headers.set(Constants.OTP_API_KEY, Constants.OTP_VALUE_HEADER);

		MultiValueMap<String, Object> multiValueMap = new LinkedMultiValueMap<>();
		ByteArrayResource imageCard = convertFile(contract);
		multiValueMap.add("fileSign", imageCard);
		multiValueMap.add("slotLabel", Constants.SLOT_LABEL);
		multiValueMap.add("userPin", Constants.USER_PIN);
		multiValueMap.add("alias", Constants.ALIAS);
		multiValueMap.add("isVisible", Constants.IS_VISIBLE);
		multiValueMap.add("page", signRequest.getPage());
		multiValueMap.add("llx", signRequest.getLlx());
		multiValueMap.add("lly", signRequest.getLly());
		multiValueMap.add("urx", signRequest.getUrx());
		multiValueMap.add("ury", signRequest.getUry());
		multiValueMap.add("detail", signRequest.getDetail());
		if (!StringUtil.isEmty(signRequest.getReason())) {
			multiValueMap.add("reason", "Ä�á»“ng Ã½ kÃ½ há»£p Ä‘á»“ng");
		}

		if (!StringUtil.isEmty(signRequest.getLocation())) {
			multiValueMap.add("location", "HÃ  Ná»™i");
		}

		if (!StringUtil.isEmty(signRequest.getContactInfo())) {
			multiValueMap.add("contactInfo", "khu Ä‘Ã´ thá»‹ Ä�áº¡i Kim");
		}

		if (!ObjectUtils.isEmpty(signRequest.getSignature())) {
			ByteArrayResource signature = convertFile(signRequest.getSignature());
			multiValueMap.add("image", signature);
		}

		HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(multiValueMap, headers);

		ResponseEntity<String> responseEntityStr = restTemplate.postForEntity(uri, request, String.class);

		// mapping response
		if (responseEntityStr.getStatusCodeValue() == 200) {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode root;
//			logger.info("[Call sign pdf] response : {}", responseEntityStr.getBody());
			try {
				root = mapper.readTree(responseEntityStr.getBody());
				SignPdfResponse result = new SignPdfResponse();
				result = mapper.readValue(root.toString(), SignPdfResponse.class);
//				result.setCode(root.get("code").toString());
//				result.setData(root.get("data").toString());

				return Optional.of(result);
			} catch (JsonProcessingException e) {
				throw new BusinessException(ErrorCode.FAILED_TO_JSON, ErrorCode.FAILED_TO_JSON_DESCRIPTION);
			}

		} else {
			throw new BusinessException(ErrorCode.FAILED_TO_EXECUTE, ErrorCode.FAILED_TO_EXECUTE_DESCRIPTION);
		}
	}

}
