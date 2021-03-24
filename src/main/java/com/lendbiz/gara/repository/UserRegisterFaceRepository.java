package com.lendbiz.gara.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lendbiz.gara.entity.UserRegisterFace;

@Repository
public interface UserRegisterFaceRepository extends JpaRepository<UserRegisterFace, String> {

}
