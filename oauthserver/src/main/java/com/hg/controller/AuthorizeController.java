package com.hg.controller;

import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hg.service.UserService;

/**
 * 
 * @ClassName: LoginController
 * @Description: 登录controller
 * @Description:跳转到登录界面
 */

@Controller
public class AuthorizeController {

	private static Logger logger = Logger.getLogger(AuthorizeController.class);
	
	@Autowired
	private UserService userService;

	// 向客户端返回授权许可码 code
	@RequestMapping("/responseCode")
	public Object toShowUser(Model model, HttpServletRequest request) {
		logger.debug("----------服务端/responseCode--------------------------------------------------------------");

		try {
			// 构建OAuth 授权请求
			OAuthAuthzRequest oauthRequest = new OAuthAuthzRequest(request);
			/*
			 * oauthRequest.getClientId(); oauthRequest.getResponseType();
			 * oauthRequest.getRedirectURI();
			 * logger.debug(oauthRequest.getClientId());
			 * logger.debug(oauthRequest.getResponseType());
			 * logger.debug(oauthRequest.getRedirectURI());
			 */

			if (oauthRequest.getClientId() != null && oauthRequest.getClientId() != "") {
				// 设置授权码
				String authorizationCode = "authorizationCode";
				// 利用oauth授权请求设置responseType，目前仅支持CODE，另外还有TOKEN
				String responseType = oauthRequest.getParam(OAuth.OAUTH_RESPONSE_TYPE);
				// 进行OAuth响应构建
				OAuthASResponse.OAuthAuthorizationResponseBuilder builder = OAuthASResponse
						.authorizationResponse(request, HttpServletResponse.SC_FOUND);
				// 设置授权码
				builder.setCode(authorizationCode);
				// 得到到客户端重定向地址
				String redirectURI = oauthRequest.getParam(OAuth.OAUTH_REDIRECT_URI);
				// 构建响应
				final OAuthResponse response = builder.location(redirectURI).buildQueryMessage();
				logger.debug("服务端/responseCode内，返回的回调路径：" + response.getLocationUri());
				logger.debug("----------服务端/responseCode--------------------------------------------------------------");
				String responceUri = response.getLocationUri();

				// 根据OAuthResponse返回ResponseEntity响应
				HttpHeaders headers = new HttpHeaders();
				try {
					headers.setLocation(new URI(response.getLocationUri()));
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return "redirect:" + responceUri;
			}

		} catch (OAuthSystemException e) {
			e.printStackTrace();
		} catch (OAuthProblemException e) {
			e.printStackTrace();
		}
		logger.debug("----------服务端/responseCode--------------------------------------------------------------");
		return null;

	}

}