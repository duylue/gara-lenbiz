package com.lendbiz.gara.repository;

import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.lendbiz.gara.entity.CfMast;

@Repository("cfMastRepository")
public interface CfMastRepository extends CrudRepository<CfMast, String> {
	
	@Procedure(procedureName = "GRREQJOIN")
	public String register( String pv_Type,  String pv_Fullname,
			String Pv_IDCode,  String Pv_IDDate,  float Pv_Revenue,
			 String Pv_exFullname, String Pv_Address, String Pv_DateOfBirth,
			 String Pv_exMobile, String Pv_Email, String Pv_Phone,
			 float Pv_LoanLimit, String Pv_RefIDCode, String Pv_RefIDDate,
			 String Pv_RefIDPlace, String Pv_RefName, String Pv_RelationAddress,
			 String Pv_Time2rc);
}
