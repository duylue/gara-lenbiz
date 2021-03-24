package com.lendbiz.gara.service.impl;

import java.io.FileInputStream;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.lendbiz.gara.constants.Constants;
import com.lendbiz.gara.constants.ErrorCode;
import com.lendbiz.gara.constants.JsonMapper;
import com.lendbiz.gara.dto.request.AuthCounterSignRequest;
import com.lendbiz.gara.dto.request.GetOtpRequest;
import com.lendbiz.gara.dto.request.PrepareCertificateRequest;
import com.lendbiz.gara.dto.request.PrepareMultipleFileRequest;
import com.lendbiz.gara.dto.request.ProfileInfoRequest;
import com.lendbiz.gara.dto.request.ReGenerateAuthCodeRequest;
import com.lendbiz.gara.dto.request.ReGenerateOtpRequest;
import com.lendbiz.gara.dto.request.SignContractRequest;
import com.lendbiz.gara.dto.request.VerifyOtpRequest;
import com.lendbiz.gara.dto.response.FileResponse;
import com.lendbiz.gara.dto.response.FptIdInfoResponse;
import com.lendbiz.gara.dto.response.FptImgSelfieResponse;
import com.lendbiz.gara.dto.response.FptResponse;
import com.lendbiz.gara.dto.response.GetOtpResponse;
import com.lendbiz.gara.dto.response.InfoIdentity;
import com.lendbiz.gara.dto.response.LoginResponse;
import com.lendbiz.gara.dto.response.OtpResponse;
import com.lendbiz.gara.dto.response.SignCloudResponse;
import com.lendbiz.gara.dto.response.SignPdfResponse;
import com.lendbiz.gara.dto.response.UserRegisterResponse;
import com.lendbiz.gara.entity.CfMast;
import com.lendbiz.gara.entity.EsignOtp;
import com.lendbiz.gara.entity.GrFileInfo;
import com.lendbiz.gara.entity.UserRegisterFace;
import com.lendbiz.gara.model.MultipleSignedFileData;
import com.lendbiz.gara.model.MultipleSigningFileData;
import com.lendbiz.gara.model.SignCloudMetaData;
import com.lendbiz.gara.model.exception.BusinessException;
import com.lendbiz.gara.model.exception.InputInvalidExeption;
import com.lendbiz.gara.model.exception.InvalidFileException;
import com.lendbiz.gara.repository.CfMastRepo;
import com.lendbiz.gara.repository.CfMastRepository;
import com.lendbiz.gara.repository.EsignOtpRepository;
import com.lendbiz.gara.repository.GrFileInfoRepository;
import com.lendbiz.gara.repository.UserRegisterFaceRepository;
import com.lendbiz.gara.service.BaseService;
import com.lendbiz.gara.service.EsignService;
import com.lendbiz.gara.service.FPTService;
import com.lendbiz.gara.service.FilesStorageService;
import com.lendbiz.gara.service.GaraService;
import com.lendbiz.gara.service.SavisService;
import com.lendbiz.gara.utils.StringUtil;
import com.lendbiz.gara.utils.Utils;

@Service("garaService")
public class GaraServiceImpl extends BaseService implements GaraService {

	private static final String GR_MA_HSBT = "MA_HSBT";
	public static String notificationTemplate = "[LENDBIZ] Ma xac thuc (OTP) cua Quy khach la {AuthorizeCode}. Vui long dien ma so nay de ky Hop dong Dien Tu va khong cung cap OTP cho bat ky ai";
	public static String notificationSubject = "[LENDBIZ] Ma xac thuc (OTP)";

	public GaraServiceImpl(Environment env) {
		super(env);
	}

	@Autowired
	private CfMastRepository cfMastRepository;

	@Autowired
	private CfMastRepo cfMastRep;

	@Autowired
	private FilesStorageService storageService;

	@Autowired
	private GrFileInfoRepository fileRepository;

	@Autowired
	private FPTService fptService;

	@Autowired
	private EsignService esignService;

	@Autowired
	private SavisService savisService;

	@Autowired
	UserRegisterFaceRepository userRegisterFaceResponse;

	@Autowired
	EsignOtpRepository esignOtpRepo;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public String register(ProfileInfoRequest input) {
		// check input
		if (ObjectUtils.isEmpty(input))
			throw new InputInvalidExeption();

		String cif = callProcedureRegister(input);
		return Utils.encodeCif(cif);
	}

	private String callProcedureRegister(ProfileInfoRequest input) {
		
		logger.info("[REGISTER ProfileInfoRequest]: {}, {}, {}", input.getDateOfBirth(), input.getRefIdDate(), input.getIdDate());
		
		input.setIdDate(Utils.formatDateString(input.getIdDate()));
		input.setRefIdDate(Utils.formatDateString(input.getRefIdDate()));
		input.setDateOfBirth(Utils.formatDateString(input.getDateOfBirth()));
		
		logger.info("[REGISTER ProfileInfoRequest]: {}, {}, {}", input.getDateOfBirth(), input.getRefIdDate(), input.getIdDate());

		try {
			String response = cfMastRep.grReqJoin(input);

			logger.info("[API REGISTER] register response : {}", JsonMapper.writeValueAsString(response));

			return response;
		} catch (Exception e) {
			logger.info("[API REGISTER] ERROR : {}", e.getMessage());
			throw new BusinessException("08", e.getMessage());
		}

	}

	@Override
	public String upload(MultipartFile[] files, String key, String custId, String hsbt) {
		uploadMultiFile(files, key, custId);
		saveHsbtToDB(hsbt, custId);

		return Constants.SUCCESS_DEC;
	}

	private void uploadFile(MultipartFile file, String key, String custId, String description, String type) {
		String direct = "";
		direct = storageService.init(key, custId);
		try {
			storageService.save(file);

			FileResponse fileInfo = new FileResponse();
			fileInfo.setName(file.getOriginalFilename());
			fileInfo.setUrl(direct + "\\" + file.getOriginalFilename());
			fileInfo.setSize(file.getSize());

			// save info to db
			saveFileToDB(fileInfo, key, custId, description, type);

			logger.info("[API UPLOAD] Uploaded the file successfully: ", file.getOriginalFilename());
		} catch (Exception e) {
			logger.info("[API UPLOAD] ERROR : {}", e.getMessage());
			throw new InvalidFileException(e.getMessage());
		}
	}

	private void uploadMultiFile(MultipartFile[] files, String key, String custId) {
		String direct = "";
		direct = storageService.init(key, custId);
		for (MultipartFile file : files) {
			try {
				storageService.save(file);

				FileResponse fileInfo = new FileResponse();
				fileInfo.setName(file.getOriginalFilename());
				fileInfo.setUrl(direct + "\\" + file.getOriginalFilename());
				fileInfo.setType(file.getContentType());
				fileInfo.setSize(file.getSize());
				fileInfo.setName(file.getOriginalFilename());

				// save info to db
				saveFileToDB(fileInfo, key, custId, null, null);

				logger.info("[API UPLOAD] Uploaded the file successfully: ", file.getOriginalFilename());
			} catch (Exception e) {
				
				logger.info("[API UPLOAD] ERROR : {}", e.getMessage());
				throw new InvalidFileException(e.getMessage());
			}
		}
	}

	private void saveHsbtToDB(String hsbt, String custId) {
		GrFileInfo entity = new GrFileInfo();
		entity.setId(UUID.randomUUID().toString());
		entity.setCustId(custId);
		entity.setKey(GR_MA_HSBT);
		entity.setValue(hsbt);

		try {
			fileRepository.save(entity);
		} catch (Exception e) {
			logger.info("[API UPLOAD] Can not save hsbt to DB: {}", hsbt);
		}
	}

	private void saveFileToDB(FileResponse fileInfo, String key, String custId, String description, String type) {
		GrFileInfo entity = new GrFileInfo();
		entity.setId(UUID.randomUUID().toString());
		entity.setCustId(custId);
		entity.setKey(key);
		entity.setValue(fileInfo.getUrl());
		entity.setType(type);
		entity.setDescription(description);
		entity.setCreateDate(new Date(Calendar.getInstance().getTimeInMillis()));
		entity.setName(fileInfo.getName());

		try {
			fileRepository.save(entity);
		} catch (Exception e) {
			logger.info("[API UPLOAD] Can not save file to DB: {}", fileInfo.getName());
		}
	}

	@Override
	public String verifyIdentity(MultipartFile[] files, String key, String custId) {
		// Todo check input
		uploadFile(files[0], key, custId, null, Constants.TYPE_FRONT_IDENTITY);
		uploadFile(files[1], key, custId, null, Constants.TYPE_BACK_IDENTITY);
		uploadFile(files[2], key, custId, null, Constants.TYPE_SELFIE);

		// Call api kyc
		FptResponse<?> response = fptService.login();
		logger.info("[VERIFY IDENTITY] login to fpt service response: {}", JsonMapper.writeValueAsString(response));

		if (!response.getErrorCode().equals(Constants.SUCCESS))
			throw new BusinessException(response.getErrorCode(), response.getMessage());

		LoginResponse data = (LoginResponse) response.getData();

		// check info identity
		FptResponse<?> infoIdentity = fptService.getInfoIndetity(files[0], files[1], data.getSessionToken());
		logger.info("[VERIFY IDENTITY] infoIdentity response: {}", JsonMapper.writeValueAsString(infoIdentity));

		if (!infoIdentity.getErrorCode().equals(Constants.SUCCESS))
			throw new BusinessException(infoIdentity.getErrorCode(), infoIdentity.getMessage());

		FptIdInfoResponse idInfo = (FptIdInfoResponse) infoIdentity.getData();

		if (isInvalidInfoIden(idInfo, custId)) {
			throw new BusinessException(ErrorCode.FAILED_IDENTITY, ErrorCode.FAILED_IDENTITY_DESCRIPTION);
		}

		// check image selfie
		FptResponse<?> objSelfie = fptService.checkImageSelfie(files[2], files[0], data.getSessionToken());
		logger.info("[VERIFY IDENTITY] objSelfie response: {}", JsonMapper.writeValueAsString(objSelfie));

		if (!objSelfie.getErrorCode().equals(Constants.SUCCESS))
			throw new BusinessException(objSelfie.getErrorCode(), objSelfie.getMessage());

		FptImgSelfieResponse imgSelfie = (FptImgSelfieResponse) objSelfie.getData();
		if (!imgSelfie.getResult().equalsIgnoreCase("SAME"))
			throw new BusinessException(ErrorCode.FAILED_SELFIE, ErrorCode.FAILED_SELFIE_DESCRIPTION);

		return Constants.SUCCESS_DEC;
	}

	private boolean isInvalidInfoIden(FptIdInfoResponse data, String custId) {
		// get info identity from db
		Optional<CfMast> objCfmast = cfMastRepository.findById(custId);
		if (!objCfmast.isPresent())
			return true;

		CfMast cfmast = objCfmast.get();
		if (!cfmast.getRefIdCode().equals(data.getId())
				|| !Utils.isCompareDate(cfmast.getRefIdDate(), data.getIssueDate()))
			return true;

		return false;
	}

	@Override
	public GetOtpResponse getOtp(GetOtpRequest input) throws Exception {
		/** Step 1 register sign auto **/
		String uuId = UUID.randomUUID().toString();
		logger.info("[GET OTP] uuid regenerate : {}", uuId);

		// get info from db
		Optional<CfMast> objCfMast = cfMastRepository.findById(input.getCustId());

		if (!objCfMast.isPresent())
			throw new BusinessException("08", "Can not get data cfmast from db");
		CfMast cfMast = objCfMast.get();
		logger.info("[GET OTP] cfmast info : {}", JsonMapper.writeValueAsString(cfMast));

		PrepareCertificateRequest request = new PrepareCertificateRequest();
		// agreementUUID;
		request.setAgreementUUID(uuId);
		// personalName;
		request.setPersonalName(cfMast.getRefName());
		// personalID;
		request.setPersonalID("");
		// citizenID;
		request.setCitizenID(cfMast.getRefIdCode());
		// location;
		request.setLocation(cfMast.getRefIdPlace());
		// stateProvince;
		// country;
		request.setCountry("VN");
		// frontSideOfIDDocument;
		// backSideOfIDDocument;
		Optional<GrFileInfo> objFrontId = fileRepository.findByCustIdAndKeyAndType(input.getCustId(),
				Constants.KEY_E_KYC, Constants.TYPE_FRONT_IDENTITY);

		if (!objFrontId.isPresent())
			throw new BusinessException("08", "Can not get data front id from db");
		GrFileInfo frontId = objFrontId.get();
		logger.info("[GET OTP] frontIdentity info : {}", JsonMapper.writeValueAsString(frontId));

		byte[] frontSideOfIDDocument = IOUtils.toByteArray(new FileInputStream(frontId.getValue()));
		request.setFrontSideOfIDDocument(frontSideOfIDDocument);

		Optional<GrFileInfo> objBackId = fileRepository.findByCustIdAndKeyAndType(input.getCustId(),
				Constants.KEY_E_KYC, Constants.TYPE_BACK_IDENTITY);

		if (!objBackId.isPresent())
			throw new BusinessException("08", "Can not get data back id from db");
		GrFileInfo backId = objBackId.get();
		logger.info("[GET OTP] backIdentity info : {}", JsonMapper.writeValueAsString(backId));
		byte[] backSideOfIDDocument = IOUtils.toByteArray(new FileInputStream(backId.getValue()));
		request.setBackSideOfIDDocument(backSideOfIDDocument);

		// authorizationEmail;
		request.setAuthorizationEmail(cfMast.getEmail());
		// authorizationMobileNo;
		request.setAuthorizationMobileNo(input.getMobile());

		esignService.registerSign(request);
		/** Step 2 upload file contract **/
		return uploadFileContract(uuId, input);

	}

	private GetOtpResponse uploadFileContract(String uuId, GetOtpRequest input) throws Exception {
		byte[] fileData;
		String mimeType;
		MultipleSigningFileData fileItem;
		SignCloudMetaData signCloudMetaDataForItem;
		HashMap<String, String> singletonSigningForItem;
		HashMap<String, String> counterSigningForItem;

		PrepareMultipleFileRequest fileRequest = new PrepareMultipleFileRequest();
		fileRequest.setAgreementUUID(uuId);
		List<MultipleSigningFileData> listOfSigningFileData = new ArrayList<>();

		List<GrFileInfo> objFileInfo = fileRepository.findByCustIdAndKey(input.getCustId(), "E-CONTRACT");
		if (objFileInfo.isEmpty())
			throw new BusinessException("08", "Can not get data file info from db");
		for (GrFileInfo fileInfo : objFileInfo) {
			fileData = IOUtils.toByteArray(new FileInputStream(fileInfo.getValue()));
			mimeType = Constants.MIMETYPE_PDF;
			fileItem = new MultipleSigningFileData();

			fileItem.setSigningFileData(fileData);
			fileItem.setMimeType(mimeType);
			fileItem.setSigningFileName(fileInfo.getDescription());

			signCloudMetaDataForItem = new SignCloudMetaData();
			// -- SingletonSigning (Signature properties for customer)
			singletonSigningForItem = new HashMap<>();
			singletonSigningForItem.put("COUNTERSIGNENABLED", "True");
			singletonSigningForItem.put("PAGENO", "Last");
			singletonSigningForItem.put("POSITIONIDENTIFIER", "Ä�áº I DIá»†N BÃŠN A");
			singletonSigningForItem.put("RECTANGLEOFFSET", "-30,-100");
			singletonSigningForItem.put("RECTANGLESIZE", "170,70");
			singletonSigningForItem.put("VISIBLESIGNATURE", "True");
			singletonSigningForItem.put("VISUALSTATUS", "False");
			singletonSigningForItem.put("IMAGEANDTEXT", "False");
			singletonSigningForItem.put("TEXTDIRECTION", "LEFTTORIGHT");
			singletonSigningForItem.put("SHOWSIGNERINFO", "True");
			singletonSigningForItem.put("SIGNERINFOPREFIX", "KÃ½ bá»Ÿi:");
			singletonSigningForItem.put("SHOWDATETIME", "True");
			singletonSigningForItem.put("DATETIMEPREFIX", "KÃ½ ngÃ y:");
			singletonSigningForItem.put("SHOWREASON", "True");
			singletonSigningForItem.put("SIGNREASONPREFIX", "LÃ½ do:");
			singletonSigningForItem.put("SIGNREASON", "TÃ´i Ä‘á»“ng Ã½");
			singletonSigningForItem.put("SHOWLOCATION", "True");
			singletonSigningForItem.put("LOCATION", "HÃ  Ná»™i");
			singletonSigningForItem.put("LOCATIONPREFIX", "NÆ¡i kÃ½:");
			singletonSigningForItem.put("TEXTCOLOR", "black");
			signCloudMetaDataForItem.setSingletonSigning(singletonSigningForItem);

			// Comment out due to only customer signature required
			counterSigningForItem = new HashMap<>();
			counterSigningForItem.put("PAGENO", "Last");
			counterSigningForItem.put("POSITIONIDENTIFIER", "Ä�áº I DIá»†N BÃŠN B");
			counterSigningForItem.put("RECTANGLEOFFSET", "-30,-100");
			counterSigningForItem.put("RECTANGLESIZE", "170,70");
			counterSigningForItem.put("VISIBLESIGNATURE", "True");
			counterSigningForItem.put("VISUALSTATUS", "False");
			counterSigningForItem.put("IMAGEANDTEXT", "False");
			counterSigningForItem.put("TEXTDIRECTION", "LEFTTORIGHT");
			counterSigningForItem.put("SHOWSIGNERINFO", "True");
			counterSigningForItem.put("SIGNERINFOPREFIX", "KÃ½ bá»Ÿi:");
			counterSigningForItem.put("SHOWDATETIME", "True");
			counterSigningForItem.put("DATETIMEPREFIX", "KÃ½ ngÃ y:");
			counterSigningForItem.put("SHOWREASON", "True");
			counterSigningForItem.put("SIGNREASONPREFIX", "LÃ½ do:");
			counterSigningForItem.put("SIGNREASON", "TÃ´i Ä‘á»“ng Ã½");
			counterSigningForItem.put("SHOWLOCATION", "True");
			counterSigningForItem.put("LOCATION", "HÃ  Ná»™i");
			counterSigningForItem.put("LOCATIONPREFIX", "NÆ¡i kÃ½:");
			counterSigningForItem.put("TEXTCOLOR", "black");
			signCloudMetaDataForItem.setCounterSigning(counterSigningForItem);

			fileItem.setSignCloudMetaData(signCloudMetaDataForItem);

			listOfSigningFileData.add(fileItem);
		}
		fileRequest.setAuthorizeMethod(Constants.AUTHORISATION_METHOD_SMS);
		fileRequest.setNotificationTemplate(notificationTemplate);
		fileRequest.setNotificationSubject(notificationSubject);
		fileRequest.setListOfSigningFileData(listOfSigningFileData);

		FptResponse<?> response = esignService.uploadMultipleFiles(fileRequest);

		GetOtpResponse result = (GetOtpResponse) response.getData();
		result.setUuId(uuId);

		return result;
	}

	@Override
	public String verifyOtp(VerifyOtpRequest input) throws Exception {
		AuthCounterSignRequest request = new AuthCounterSignRequest();
		request.setAgreementUUID(input.getId());
		request.setAuthorizeCode(input.getOtp());
		request.setBillCode(input.getBillCode());

		FptResponse<?> esignRes = esignService.verifyOtp(request);
		SignCloudResponse response = (SignCloudResponse) esignRes.getData();
		logger.info("[verify Otp] signCloudResponse response: {}", JsonMapper.writeValueAsString(response));
		List<MultipleSignedFileData> multiFileSign = response.getMultipleSignedFileData();

		String path = storageService.init("E-CONTRACTED", input.getCustId());
		for (MultipleSignedFileData fileSign : multiFileSign) {
			String direct = path + "\\" + fileSign.getSignedFileName();
			logger.info("[verify Otp] direct {}", direct);
			storageService.saveContract(fileSign.getSignedFileData(), direct);

			FileResponse fileInfo = new FileResponse();
			fileInfo.setName(fileSign.getSignedFileName());
			fileInfo.setUrl(path);
			fileInfo.setType(fileSign.getMimeType());
			fileInfo.setSize(fileSign.getSignedFileData().length);

			// save info to db
			saveFileToDB(fileInfo, "E-CONTRACTED", input.getCustId(), "Ä�Ã£ kÃ½ há»£p Ä‘á»“ng", null);
		}

		return "Thanh Cong";
	}

	@Override
	public GetOtpResponse regenerateOtp(ReGenerateOtpRequest input) throws Exception {
		// TODO Auto-generated method stub
		ReGenerateAuthCodeRequest request = new ReGenerateAuthCodeRequest();
		request.setAgreementUUID(input.getUuId());
		request.setAuthorizeMethod(Constants.AUTHORISATION_METHOD_SMS);
		request.setNotificationTemplate(notificationTemplate);
		request.setNotificationSubject(notificationSubject);

		FptResponse<?> response = esignService.regenerateOtp(request);
		GetOtpResponse result = (GetOtpResponse) response.getData();
		result.setUuId(input.getUuId());

		return null;
	}

	// Verify identity savis
	@Override
	public String verifyEkyc(MultipartFile[] files, String key, String custId) {
		// check validate input
		// save file to DB
		uploadFile(files[0], key, custId, "", Constants.TYPE_FRONT_IDENTITY);
		uploadFile(files[1], key, custId, "", Constants.TYPE_BACK_IDENTITY);
		uploadFile(files[2], key, custId, "", Constants.TYPE_SELFIE);
		// call info identity
		InfoIdentity identity = new InfoIdentity();
		Optional<InfoIdentity> optInfo = savisService.callPredict(files[0], identity, Constants.TYPE_FRONT_IDENTITY);
		optInfo = savisService.callPredict(files[1], identity, Constants.TYPE_BACK_IDENTITY);
		logger.info("[Call perdict] response identity: {}", JsonMapper.writeValueAsString(optInfo.get()));

		if (optInfo.isPresent() && isCheckIdentity(custId, optInfo.get())) {
			/** check anh selfie **/
			logger.info("Start call check selfie ================== : {}", JsonMapper
					.writeValueAsString(savisService.callCheckSelfie(files[0], files[2], Constants.THRESHOLD)));
			if (savisService.callCheckSelfie(files[0], files[2], Constants.THRESHOLD)) {
				/** call api register user face **/
				Optional<UserRegisterResponse> result = savisService.callRegisterKyc(files[0]);
				/** save info register user face to DB **/
				if (!result.isPresent())
					throw new BusinessException("08", "Loi dang ky thong tin cmnd");
				saveInfoRegisterUser(result.get());
				return Constants.SUCCESS_DEC;
			}
		}
		throw new BusinessException("08", "Fail!");
	}

	private Boolean isCheckIdentity(String custId, InfoIdentity infoIdentity) {
		logger.info("Start call check identity");

		Optional<CfMast> objCfmast = cfMastRepository.findById(custId);

		if (!objCfmast.isPresent()) {
			throw new BusinessException("08", "Can not get cfmast info by custId");
		}
		CfMast cfmast = objCfmast.get();

		logger.info("Cfmast info: {}", infoIdentity.getIdentityId());
		logger.info("Cfmast info: {}", cfmast.getRefIdCode());

		if (infoIdentity.getIdentityId().equalsIgnoreCase(cfmast.getRefIdCode())
				&& Utils.isCompareDate(cfmast.getDateOfBirth(), infoIdentity.getBirthday())
				&& Utils.isCompareDate(cfmast.getRefIdDate(), infoIdentity.getDateIssued())) {
			return true;
		}

		return false;
	}

	private void saveInfoRegisterUser(UserRegisterResponse userRes) {
		UserRegisterFace entity = new UserRegisterFace();
		entity.setId(UUID.randomUUID().toString());
		entity.setAge(userRes.getAge());
		entity.setCroppedGeneralUrl(userRes.getCroppedGeneralUrl());
		entity.setCroppedUrl(userRes.getCroppedUrl());
		entity.setGender(userRes.getGender());
		entity.setGenneralUrl(userRes.getGeneralUrl());
		entity.setLastUpdate(userRes.getLastUpdate());
		entity.setRawUserId(userRes.getRawUserId());
		entity.setRequestId(userRes.getRequestId());
		entity.setUserAttrId(userRes.getUserAttrId());
		entity.setUserId(userRes.getUserId());
		entity.setFaceAttrId(userRes.getFaceAttrId());

		userRegisterFaceResponse.save(entity);
	}

	// tuht add esign savis
	@Override
	public String savisGetOtp(String custId) {
		/** check so lan cal get otp **/
		Long count = esignOtpRepo.getCountOtp(custId);
		if (count > 3) {
			throw new BusinessException("08", "Vuot qua so lan get otp trong ngay, toi da 3 lan");
		}
		/** call api get otp **/
		Optional<OtpResponse> otpResult = savisService.getOtp();
		if (!otpResult.isPresent()) {
			throw new BusinessException("08", "khong the lay otp tu api savis");
		}

		sendNotification(custId, otpResult.get().getOtp());
		/** save otp result to DB **/
		return otpResult.get().getOtp();
	}
	
	@Override
	public String savisVerifyOtp(String custId, String otp) {
		/** Check validate input **/
		if (StringUtil.isEmty(otp))
			throw new InputInvalidExeption();

		/** Call api validate otp **/
		if (!savisService.validateOtp(otp)) {
			throw new BusinessException("08", "Ma otp khong hop le");
		}
		try {
			saveOtpToDb(custId, otp);
		} catch (Exception e) {
			logger.info("Can not save otp to DB");
		}
		return Constants.SUCCESS_DEC;
	}

	public String sendNotification(String custId, String otp) {
		logger.info("[Get OTP] start send notification to user : {}", custId);
		SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("SEND_NOTIFICATION")
				.declareParameters(new SqlParameter("Pv_Custid", Types.VARCHAR))
				.declareParameters(new SqlParameter("Pv_Otp", Types.VARCHAR))

				.declareParameters(new SqlOutParameter("p_err_code", Types.INTEGER));

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("Pv_Custid", custId);
		params.addValue("Pv_Otp", otp);

		// Exec stored procedure
		Map<String, Object> map = jdbcCall.execute(params);
		String result = map.get("p_err_code").toString();

		logger.info("[Get OTP] end send notification to user : {}", result);
		return result;
	}

	@Override
	public Optional signContract(String custId, MultipartFile contract, String otp, SignContractRequest request) {
		/** Check validate input **/
		if (StringUtil.isEmty(otp))
			throw new InputInvalidExeption();
		uploadFile(contract, Constants.KEY_CONTRACT, custId, "Hop dong truoc khi ky", Constants.KEY_CONTRACT);

		/** Call api validate otp **/
		if (!savisService.validateOtp(otp)) {
			throw new BusinessException("08", "Ma otp khong hop le");
		}
		try {
			saveOtpToDb(custId, otp);
		} catch (Exception e) {
			logger.info("Can not save otp to DB");
		}

		/** Call api sign contract **/
		Optional<SignPdfResponse> otpSignResult = savisService.signPdf(contract, request);
//		logger.info("[Call sign pdf] response : {}", otpSignResult.get().getData());

		String path = storageService.init("CONTRACTED", custId);
		String direct = path + "\\" + contract.getOriginalFilename();
		logger.info("[Sign pdf] direct {}", direct);
		storageService.saveContract(otpSignResult.get().getData(), direct, contract.getOriginalFilename(),
				Constants.KEY_CONTRACTED, custId);
		/** Save file contract to DB **/
		return Optional.of(direct);
	}

	private void saveOtpToDb(String custId, String otp) {
		// TODO Auto-generated method stub
		EsignOtp entity = new EsignOtp();
		entity.setCustid(custId);
		entity.setId(UUID.randomUUID().toString());
		entity.setOtp(otp);
		entity.setCreateDate(new Date(Calendar.getInstance().getTime().getTime()));

		esignOtpRepo.save(entity);
	}

}
