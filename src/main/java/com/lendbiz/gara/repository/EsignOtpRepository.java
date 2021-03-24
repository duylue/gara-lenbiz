package com.lendbiz.gara.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lendbiz.gara.entity.EsignOtp;


@Repository
public interface EsignOtpRepository extends JpaRepository<EsignOtp, String> {

	@Query(value = "SELECT COUNT(*) FROM ESIGN_OTP WHERE CUSTID = ?1 AND " 
					+ "TO_DATE(TO_CHAR(CAST(create_date AS DATE), 'DD-MON-YY'), 'DD-MON-YY') = TO_DATE(TO_CHAR(CAST(SYSDATE AS DATE), 'DD-MON-YY'), 'DD-MON-YY')", nativeQuery = true)
	Long getCountOtp(String custId);
}
