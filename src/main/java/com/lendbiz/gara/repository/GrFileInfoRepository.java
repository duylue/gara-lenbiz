package com.lendbiz.gara.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lendbiz.gara.entity.GrFileInfo;

@Repository
public interface GrFileInfoRepository extends JpaRepository<GrFileInfo, String> {

	@Query(value = "SELECT * FROM GRFILEINFO where CUSTID = ?1 AND KEY = ?2"
			+ " and TYPE = ?3 ORDER BY CREATEDATE ASC", nativeQuery = true)
	Optional<GrFileInfo> findByCustIdAndKeyAndType(String custId, String key, String description);
	
	@Query(value = "SELECT * FROM GRFILEINFO where CUSTID = ?1 and KEY = ?2", nativeQuery = true)
	List<GrFileInfo> findByCustIdAndKey(String custId, String key);
}
