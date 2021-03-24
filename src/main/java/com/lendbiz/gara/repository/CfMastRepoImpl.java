package com.lendbiz.gara.repository;

import java.sql.Types;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

import com.lendbiz.gara.dto.request.ProfileInfoRequest;
import com.lendbiz.gara.service.BaseService;

@Service
public class CfMastRepoImpl extends BaseService implements CfMastRepo {

	public CfMastRepoImpl(Environment env) {
		super(env);
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public String grReqJoin(ProfileInfoRequest input) {
		SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
				.withProcedureName("GRREQJOIN")
				.declareParameters(new SqlParameter("pv_Type", Types.VARCHAR))
				.declareParameters(new SqlParameter("pv_Fullname", Types.VARCHAR))
				.declareParameters(new SqlParameter("Pv_IDCode", Types.VARCHAR))
				.declareParameters(new SqlParameter("Pv_IDDate", Types.VARCHAR))
				
				.declareParameters(new SqlParameter("Pv_Revenue", Types.NUMERIC))
				.declareParameters(new SqlParameter("Pv_exFullname", Types.VARCHAR))
				.declareParameters(new SqlParameter("Pv_Address", Types.VARCHAR))
				.declareParameters(new SqlParameter("Pv_DateOfBirth", Types.VARCHAR))
				
				.declareParameters(new SqlParameter("Pv_exMobile", Types.VARCHAR))
				.declareParameters(new SqlParameter("Pv_Email", Types.VARCHAR))
				.declareParameters(new SqlParameter("Pv_Phone", Types.VARCHAR))
				.declareParameters(new SqlParameter("Pv_LoanLimit", Types.NUMERIC))
				
				.declareParameters(new SqlParameter("Pv_RefIDCode", Types.VARCHAR))
				.declareParameters(new SqlParameter("Pv_RefIDDate", Types.VARCHAR))
				.declareParameters(new SqlParameter("Pv_RefIDPlace", Types.VARCHAR))
				.declareParameters(new SqlParameter("Pv_RefName", Types.VARCHAR))
				
				.declareParameters(new SqlParameter("Pv_RelationAddress", Types.VARCHAR))
				.declareParameters(new SqlParameter("Pv_Time2rc", Types.VARCHAR))

				.declareParameters(new SqlOutParameter("v_error_msg", Types.VARCHAR));

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("pv_Type", "B");
		params.addValue("pv_Fullname", input.getFullName());
		params.addValue("Pv_IDCode", input.getIdCode());
		params.addValue("Pv_IDDate", input.getIdDate());
		
		params.addValue("Pv_Revenue", input.getRevenue());
		params.addValue("Pv_exFullname", input.getExFullName());
		params.addValue("Pv_Address", input.getAddress());
		params.addValue("Pv_DateOfBirth", input.getDateOfBirth());
		
		params.addValue("Pv_exMobile", input.getMobileSms());
		params.addValue("Pv_Email", input.getEmail());
		params.addValue("Pv_Phone", input.getPhone());
		params.addValue("Pv_LoanLimit", input.getMrLoanLimit());

		params.addValue("Pv_RefIDCode", input.getRefIdCode());
		params.addValue("Pv_RefIDDate", input.getRefIdDate());
		params.addValue("Pv_RefIDPlace", input.getRefIdPlace());
		params.addValue("Pv_RefName", input.getRefName());
		
		params.addValue("Pv_RelationAddress", input.getRelationAddress());
		params.addValue("Pv_Time2rc", input.getTime2rc());
		
		// Exec stored procedure
		Map<String, Object> map = jdbcCall.execute(params);
		String result = map.get("v_error_msg").toString();
		
		return result;
	}

}
