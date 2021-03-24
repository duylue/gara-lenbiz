package com.lendbiz.gara.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.lendbiz.gara.constants.ErrorCode;
import com.lendbiz.gara.dto.response.LbResponse;
import com.lendbiz.gara.model.base.BaseController;
import com.lendbiz.gara.model.exception.NotFoundException;


public abstract class AbstractController<S> extends BaseController {
  
  @Autowired
  protected S service;
  
  protected <T> LbResponse<T> toResult(Optional<T> optional) {
      if (!optional.isPresent())
          throw new NotFoundException();
      return toResult(optional.get());
  }

  protected <T> LbResponse<T> toResult(T t) {
	  LbResponse<T> response = new LbResponse<T>();
      response.setStatus(ErrorCode.SUCCESS);
      response.setMessage(ErrorCode.SUCCESS_DESCRIPTION);
      response.setData(t);
      return response;
  }
  
  protected <T> LbResponse<T> toResult(String status, String message, T t) {
	  LbResponse<T> response = new LbResponse<T>();
      response.setStatus(status);
      response.setMessage(message);
      response.setData(t);
      return response;
  }
  
}
