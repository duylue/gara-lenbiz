/***************************************************************************
 * Copyright 2018 by VIETIS - All rights reserved.                *    
 **************************************************************************/
package com.lendbiz.gara.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for web pages.
 * Only two request mappings included intended for secured "single" 
 * page applications.
 */
@Controller
public class BaseController {

  @RequestMapping("/dang-ky-goi-von")
  public String home() {
    return "profile";
  }

  @RequestMapping("/xac-minh-danh-tinh")
  public String kyc() {
    return "kyc";
  }
  
  @RequestMapping("/ky-hop-dong")
  public String signature() {
    return "signature";
  }
  
  @RequestMapping("/tai-thong-tin")
  public String upload() {
    return "upload";
  }

  @RequestMapping("/thong-tin-hop-dong")
  public String infocontract() {
    return "infocontract";
  }

}