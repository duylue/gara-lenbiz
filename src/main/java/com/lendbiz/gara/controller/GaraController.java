package com.lendbiz.gara.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lendbiz.gara.constants.JsonMapper;
import com.lendbiz.gara.dto.request.GetOtpRequest;
import com.lendbiz.gara.dto.request.ProfileInfoRequest;
import com.lendbiz.gara.dto.request.ReGenerateOtpRequest;
import com.lendbiz.gara.dto.request.SignContractRequest;
import com.lendbiz.gara.dto.request.VerifyOtpRequest;
import com.lendbiz.gara.model.exception.BusinessException;
import com.lendbiz.gara.service.GaraService;
import com.lendbiz.gara.utils.StringUtil;
import com.lendbiz.gara.utils.Utils;

@RestController
@RequestMapping("/gara/v1.0")
public class GaraController extends AbstractController<GaraService> {

	@Autowired
	private GaraService service;

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody ProfileInfoRequest input) throws BusinessException {
		logger.info("[API REGISTER] request param:" + JsonMapper.writeValueAsString(input));

		return response(toResult(service.register(input)));
	}

	@PostMapping("/upload")
	public ResponseEntity<?> upload(@RequestParam("file") MultipartFile[] file, @RequestParam("key") String key,
			@RequestParam("cif") String custId, @RequestParam("hsbt") String hsbt) throws BusinessException {
		custId = Utils.decodeCif(custId);
		logger.info("[API UPLOAD] request param key:{} id:{} ma hsbt:{}", key, custId, hsbt);
		return response(toResult(service.upload(file, key, custId, hsbt)));
	}

	@PostMapping("/verify-identity")
	public ResponseEntity<?> veriry(@RequestParam("file") MultipartFile[] files, @RequestParam("key") String key,
			@RequestParam("cif") String custId) throws BusinessException {
		custId = Utils.decodeCif(custId);

		logger.info("[API VERIFY IDENTITY] request param key:{} id:{} ma hsbt:{}", key, custId);

		return response(toResult(service.verifyIdentity(files, key, custId)));
	}

	@PostMapping("/get-otp")
	public ResponseEntity<?> getOtp(@RequestBody GetOtpRequest input) throws Exception {
		input.setCustId(Utils.decodeCif(input.getCustId()));
		logger.info("[API GET OTP] request param:" + JsonMapper.writeValueAsString(input));
		return response(toResult(service.getOtp(input)));
	}

	@PostMapping("/verify-otp")
	public ResponseEntity<?> getOtp(@RequestBody VerifyOtpRequest input) throws Exception {
		input.setCustId(Utils.decodeCif(input.getCustId()));
		logger.info("[API VERIFY OTP] request param:" + JsonMapper.writeValueAsString(input));
		return response(toResult(service.verifyOtp(input)));
	}

	@PostMapping("/regenerate-otp")
	public ResponseEntity<?> regenerateOtp(@RequestBody ReGenerateOtpRequest input) throws Exception {
		logger.info("[API REGENERATE OTP] request param:" + JsonMapper.writeValueAsString(input));
		return response(toResult(service.regenerateOtp(input)));
	}

	// tuht add ekyc savis
	@PostMapping("/ekyc")
	public ResponseEntity<?> verifyIdentity(@RequestParam("file") MultipartFile[] files,
			@RequestParam("key") String key, @RequestParam("cif") String custId) throws BusinessException {

		custId = Utils.decodeCif(custId);

//		"011479";

		logger.info("[API VERIFY IDENTITY] request param key:{} id:{} ma hsbt:{}", key, custId);

		return response(toResult(service.verifyEkyc(files, key, custId)));
	}

	@PostMapping("/sv-get-otp")
	public ResponseEntity<?> veriry(@RequestBody GetOtpRequest input) throws BusinessException {
		input.setCustId(Utils.decodeCif(input.getCustId()));

		logger.info("[API GET OTP] request param:" + JsonMapper.writeValueAsString(input));
//		return response(toResult(service.savisGetOtp(input.getCustId())));
		return response(toResult(service.savisGetOtp(input.getCustId())));
	}

	@PostMapping("/sv-verify-otp")
	public ResponseEntity<?> savisVerifyOtp(@RequestBody VerifyOtpRequest input)
			throws BusinessException {
		logger.info("[sign-contract] request: {}", JsonMapper.writeValueAsString(input.getCustId()));
		String custId = Utils.decodeCif(input.getCustId());
//		String custId = Utils.decodeCif("011479");

		return response(toResult(service.savisVerifyOtp(custId, input.getOtp())));
	}

	@PostMapping("/sign-contract")
	public ResponseEntity<?> signPdf(@RequestParam("cif") String cif, @RequestParam("contract") MultipartFile contract,
			@RequestParam("otp") String otp, @RequestParam("page") String page, @RequestParam("llx") String llx,
			@RequestParam("lly") String lly, @RequestParam("urx") String urx, @RequestParam("ury") String ury,
			@RequestParam("detail") String detail, @RequestParam(value = "reason", required = false) String reason,
			@RequestParam(value = "location", required = false) String location,
			@RequestParam(value = "contactInfo", required = false) String contactInfo,
			@RequestParam(value = "signature", required = false) MultipartFile signature) throws BusinessException {
		logger.info("[sign-contract] request: {}", JsonMapper.writeValueAsString(cif));
//		String custId = Utils.decodeCif(cif);
		String custId = Utils.decodeCif("011479");
		SignContractRequest request = new SignContractRequest();
		request.setPage(page);
		request.setLlx(llx);
		request.setLly(lly);
		request.setUrx(urx);
		request.setUry(ury);
		request.setDetail(detail);
		if (!StringUtil.isEmty(reason)) {
			request.setReason(reason);
		}
		if (!StringUtil.isEmty(location)) {
			request.setLocation(location);
		}

		if (!StringUtil.isEmty(contactInfo)) {
			request.setContactInfo(contactInfo);
		}

		if (!ObjectUtils.isEmpty(signature)) {
			request.setSignature(signature);
		}

		return response(toResult(service.signContract(custId, contract, otp, request)));
	}

}
