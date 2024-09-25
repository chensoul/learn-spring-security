package com.chensoul.lss.security;

import javax.servlet.http.HttpServletRequest;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

public class CustomWebAuthenticationDetails extends WebAuthenticationDetails {

  private String verificationCode;

  public CustomWebAuthenticationDetails(HttpServletRequest request) {
    super(request);
    verificationCode = request.getParameter("code");
  }

  public String getVerificationCode() {
    return verificationCode;
  }

  public void setVerificationCode(String verificationCode) {
    this.verificationCode = verificationCode;
  }
}
