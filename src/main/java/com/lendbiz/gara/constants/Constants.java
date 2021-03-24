package com.lendbiz.gara.constants;

public class Constants {
	public static final String SUCCESS = "0";

	public static final String TYPE_FRONT_IDENTITY = "FRONT_IDENTITY";
	public static final String TYPE_BACK_IDENTITY = "BACK_IDENTITY";
	public static final String TYPE_SELFIE = "SELFIE";
	public static final String KEY_E_KYC = "E-KYC";

	public static final String URI_LOGIN = "https://api.uat.trandata.io/account/unauth/v1/login";
	public static final String USER_FPT = "lendbiz";
	public static final String PASS_FPT = "lendbiz@123";
	public static final String URI_VERIFICATION = "https://api.uat.trandata.io/aggregator/api/v1/verification/v01_id";
	public static final String URI_CHECK_SELFIE = "https://api.uat.trandata.io/aggregator/api/v1/verification/v02_facematching";
	public static final String URI_VERIFI_MOBILE = "https://api.uat.trandata.io/aggregator/api/v1/verification/v03_phone";
	public static final String DIRECTORIES_ROOT = "\\\\10.5.0.1\\share\\Gara\\";
	public static final String SUCCESS_DEC = "Thanh Cong";

	// info call api savis
	public static final String ESIGN_PREDICT = "https://uat-gateway.digital-id.vn/s1-ekyc/1.0.0/predict";
	public static final String ESIGN_FACE_GENERAL = "https://uat-gateway.digital-id.vn/s1-ekyc/1.0.0/face_general";
	public static final String ESIGN_REGISTER = "https://uat-gateway.digital-id.vn/ekyc/1.0.0/register_user_face";
	public static final String GET_OTP = "https://uat-gateway.trustca.vn/otp/1.0/generate";
	public static final String VALIDATE_OTP = "https://uat-gateway.trustca.vn/otp/1.0/validate";
	public static final String SIGN_PDF = "https://uat-gateway.trustca.vn/signing-api/1.0/pdf";
	public static final String ESIGN_API_KEY = "apikey";
	public static final String ESIGN_VALUE_HEADER = "eyJ4NXQiOiJOVGRtWmpNNFpEazNOalkwWXpjNU1tWm1PRGd3TVRFM01XWXdOREU1TVdSbFpEZzROemM0WkE9PSIsImtpZCI6ImdhdGV3YXlfY2VydGlmaWNhdGVfYWxpYXMiLCJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJhZG1pbkBjYXJib24uc3VwZXIiLCJhcHBsaWNhdGlvbiI6eyJvd25lciI6ImFkbWluIiwidGllclF1b3RhVHlwZSI6bnVsbCwidGllciI6IlVubGltaXRlZCIsIm5hbWUiOiJla3ljLWFwcCIsImlkIjoyLCJ1dWlkIjoiYTQxNzY4ZTMtNDYwOS00NjdlLTkxZDEtZDNhYTJmYjRhNGQwIn0sImlzcyI6Imh0dHBzOlwvXC91YXQtYXBpcG9ydGFsLmRpZ2l0YWwtaWQudm46NDQzXC9vYXV0aDJcL3Rva2VuIiwidGllckluZm8iOnsiVW5saW1pdGVkIjp7InRpZXJRdW90YVR5cGUiOiJyZXF1ZXN0Q291bnQiLCJncmFwaFFMTWF4Q29tcGxleGl0eSI6MCwiZ3JhcGhRTE1heERlcHRoIjowLCJzdG9wT25RdW90YVJlYWNoIjp0cnVlLCJzcGlrZUFycmVzdExpbWl0IjowLCJzcGlrZUFycmVzdFVuaXQiOm51bGx9fSwia2V5dHlwZSI6IlBST0RVQ1RJT04iLCJwZXJtaXR0ZWRSZWZlcmVyIjoiIiwic3Vic2NyaWJlZEFQSXMiOlt7InN1YnNjcmliZXJUZW5hbnREb21haW4iOiJjYXJib24uc3VwZXIiLCJuYW1lIjoiZUtZQyIsImNvbnRleHQiOiJcL2VreWNcLzEuMC4wIiwicHVibGlzaGVyIjoiYWRtaW4iLCJ2ZXJzaW9uIjoiMS4wLjAiLCJzdWJzY3JpcHRpb25UaWVyIjoiVW5saW1pdGVkIn0seyJzdWJzY3JpYmVyVGVuYW50RG9tYWluIjoiY2FyYm9uLnN1cGVyIiwibmFtZSI6InMxLWVLWUMiLCJjb250ZXh0IjoiXC9zMS1la3ljXC8xLjAuMCIsInB1Ymxpc2hlciI6ImFkbWluIiwidmVyc2lvbiI6IjEuMC4wIiwic3Vic2NyaXB0aW9uVGllciI6IlVubGltaXRlZCJ9XSwicGVybWl0dGVkSVAiOiIiLCJpYXQiOjE2MTIzMzY3MDIsImp0aSI6Ijg1NDkzM2MyLTI4MDAtNDIxZS04NmYwLWJmOTcxNGRiMzk0OSJ9.B8VYdpJIpWP0GB7VZcJ3cElHB51iwjlKAw4RDDVwOX1VPp1pbmmkhy000KOMNIxvQuzG5S71bgwsha1_xQ_bvfjyiEHB6yATU2hPuSVw85-Iexgwd191XL-9DOXh6XRhUr_zKsJG2g9ct2ZjWGtWStgqn-qTgg0xXmBJC9et-XImkt0zGsSwH0l1k7QdCPgqjJYr30FJ4FBw1plzBa5_EUTmO0jd9iZkGl1iQYgqrD1Ra5NTPd4GTdIwDTDUKiVkUpmJmMeKgvEmLx862UhaJCZ84XFEYOoB4_taCjL5fHdic85s3cQ3yT7BUUFtYGfruLTn009LkdU2fTtll0j60g==";

	// info call get otp
	public static final String OTP_API_KEY = "apiKey";
	public static final String OTP_VALUE_HEADER = "eyJ4NXQiOiJOVGRtWmpNNFpEazNOalkwWXpjNU1tWm1PRGd3TVRFM01XWXdOREU1TVdSbFpEZzROemM0WkE9PSIsImtpZCI6ImdhdGV3YXlfY2VydGlmaWNhdGVfYWxpYXMiLCJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJhZG1pbkBjYXJib24uc3VwZXIiLCJhcHBsaWNhdGlvbiI6eyJvd25lciI6ImFkbWluIiwidGllclF1b3RhVHlwZSI6bnVsbCwidGllciI6IlVubGltaXRlZCIsIm5hbWUiOiJMRU5EQklaIiwiaWQiOjcsInV1aWQiOiJkZmYxZWFlZS0xODFiLTQzOTUtYmJlMi1iOGRkYTc4OTI4NTIifSwiaXNzIjoiaHR0cHM6XC9cL3VhdC1hcGlwb3J0YWwudHJ1c3RjYS52bjo0NDNcL29hdXRoMlwvdG9rZW4iLCJ0aWVySW5mbyI6eyJVbmxpbWl0ZWQiOnsidGllclF1b3RhVHlwZSI6InJlcXVlc3RDb3VudCIsImdyYXBoUUxNYXhDb21wbGV4aXR5IjowLCJncmFwaFFMTWF4RGVwdGgiOjAsInN0b3BPblF1b3RhUmVhY2giOnRydWUsInNwaWtlQXJyZXN0TGltaXQiOjAsInNwaWtlQXJyZXN0VW5pdCI6bnVsbH19LCJrZXl0eXBlIjoiUFJPRFVDVElPTiIsInBlcm1pdHRlZFJlZmVyZXIiOiIiLCJzdWJzY3JpYmVkQVBJcyI6W3sic3Vic2NyaWJlclRlbmFudERvbWFpbiI6ImNhcmJvbi5zdXBlciIsIm5hbWUiOiJEaWdpdGFsLVRPVFAiLCJjb250ZXh0IjoiXC9vdHBcLzEuMCIsInB1Ymxpc2hlciI6ImFkbWluIiwidmVyc2lvbiI6IjEuMCIsInN1YnNjcmlwdGlvblRpZXIiOiJVbmxpbWl0ZWQifSx7InN1YnNjcmliZXJUZW5hbnREb21haW4iOiJjYXJib24uc3VwZXIiLCJuYW1lIjoiU2lnbmluZ0JveC1MQUJHcm91cC1TaWduaW5nLUFQSSIsImNvbnRleHQiOiJcL3NpZ25pbmctYXBpXC8xLjAiLCJwdWJsaXNoZXIiOiJhZG1pbiIsInZlcnNpb24iOiIxLjAiLCJzdWJzY3JpcHRpb25UaWVyIjoiVW5saW1pdGVkIn1dLCJwZXJtaXR0ZWRJUCI6IiIsImlhdCI6MTYxMjM0MzA4OCwianRpIjoiNDBiYjU4ODktNTUwMi00N2NjLWJjNzEtNzc5ZDljMTU0ZmViIn0=.KWGPc5LS55-gE7E_l2kjblRHyUagylaD6DTVUv_EOsYyDoVkZkoBvn_UVWl-jNT524Ck_GJKKX2FIHntUGmzOb9V12TAemuVhye6I9JRBve5I_qGKHH3g_K_F1w2CHE8DTxcRNStCDE65eBqapYCR6SltjPF_wpxIYvh8R2QIB-ztiynK6yyHOPFyVKGHgN-AJ4XgJ__dpgjtQHSilF2YRHFNxchEegtCR7iRQvn7MzfCtEAmUdPSH6Dd_f-u7xVC5Dl7DzgbO_14MAslHzXpFHmUhTcr5-SirEY2KaztsKZvFvbyJlpKI4aOPXzaf2fznNMUrmMah5OpMGkSQKmDQ==";

	public static final String ESIGN_REGISTER_URL = "http://localhost:8091/esign/v1.0/register";
	public static final String ESIGN_UPLOAD_URL = "http://localhost:8091/esign/v1.0/upload-contract";
	public static final String ESIGN_VERIFY_OTP = "http://localhost:8091/esign/v1.0/verify-otp";
	public static final String ESIGN_REGENERATE_OTP = "http://localhost:8091/esign/v1.0/regenerate-otp";

	public static final String MIMETYPE_PDF = "application/pdf";
	public static final int AUTHORISATION_METHOD_SMS = 1;
	
	public static final String THRESHOLD = "0.65";
	
	//metadate api use register face
	public static final String METADATA = "{\"source\":\"24a00f5d-e625-4bc4-8384-c906848c03f3\"}";
	
	public static final String KEY_CONTRACT = "CONTRACT";
	
	//info call sign pdf
	public static final String SLOT_LABEL = "test";
	public static final String USER_PIN = "123";
	public static final String ALIAS = "demouser";
	public static final String IS_VISIBLE = "true";
	
	public static final String KEY_CONTRACTED = "CONTRACTED";
	
}
