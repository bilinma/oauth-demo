package com.hg.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthBearerClientRequest;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthAccessTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthResourceResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 
 * @ClassName: ServerController   
 * @Description: oauth服务端Controller
 * @Description:
 */

@RequestMapping("/server")
@Controller
public class ServerController{
	
	private static Logger logger = Logger.getLogger(ServerController.class);
	
	String clientId = null;
	String clientSecret = null;
    String accessTokenUrl = null;
    String userInfoUrl = null;
    String redirectUrl = null;
    String response_type = null;
    String code= null;
    
	
	//提交申请code的请求
	@RequestMapping("/requestServerCode")
	public String requestServerFirst(HttpServletRequest request, HttpServletResponse response, RedirectAttributes attr) throws OAuthProblemException{
		clientId = "clientId";
		clientSecret = "clientSecret";
	    accessTokenUrl = "responseCode";
	    userInfoUrl = "userInfoUrl";
	    redirectUrl = "http://localhost:8080/oauthclient01/server/callbackCode";
	    response_type = "code";
	    
		OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
		String requestUrl = null;
		try {
			//构建oauthd的请求。设置请求服务地址（accessTokenUrl）、clientId、response_type、redirectUrl
			OAuthClientRequest accessTokenRequest = OAuthClientRequest
					.authorizationLocation(accessTokenUrl)
			        .setResponseType(response_type)
			        .setClientId(clientId)
			        .setRedirectURI(redirectUrl)
			        .buildQueryMessage();
			requestUrl = accessTokenRequest.getLocationUri();
			logger.debug(requestUrl);
		} catch (OAuthSystemException e) {
			e.printStackTrace();
		}
		return "redirect:http://localhost:8080/oauthserver/"+requestUrl ;
	}
	
	//接受客户端返回的code，提交申请access token的请求
	@RequestMapping("/callbackCode")
	public Object toLogin(HttpServletRequest request) throws OAuthProblemException{
		logger.debug("-----------客户端/callbackCode--------------------------------------------------------------------------------");
		clientId = "clientId";
		clientSecret = "clientSecret";
		accessTokenUrl="http://localhost:8080/oauthserver/responseAccessToken";
	    userInfoUrl = "userInfoUrl";
	    redirectUrl = "http://localhost:8080/oauthclient01/server/accessToken";
	    HttpServletRequest httpRequest = (HttpServletRequest) request;
	    code = httpRequest.getParameter("code"); 
	    logger.debug(code);
	    OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
	    try {
			OAuthClientRequest accessTokenRequest = OAuthClientRequest
					.tokenLocation(accessTokenUrl)
			        .setGrantType(GrantType.AUTHORIZATION_CODE)
			        .setClientId(clientId)
			        .setClientSecret(clientSecret)
			        .setCode(code)
			        .setRedirectURI(redirectUrl)
			        .buildQueryMessage();
			//去服务端请求access token，并返回响应
			OAuthAccessTokenResponse oAuthResponse = oAuthClient.accessToken(accessTokenRequest, OAuth.HttpMethod.POST);
			//获取服务端返回过来的access token 
			String accessToken = oAuthResponse.getAccessToken();
			//查看access token是否过期
            Long expiresIn = oAuthResponse.getExpiresIn();
            logger.debug("客户端/callbackCode方法的token：：："+accessToken);
            logger.debug("-----------客户端/callbackCode--------------------------------------------------------------------------------");
            return "redirect:http://localhost:8080/oauthclient01/server/accessToken?accessToken="+accessToken;
		} catch (OAuthSystemException e) {
			e.printStackTrace();
		}
	    return null;
	}
	
	//接受服务端传回来的access token，由此token去请求服务端的资源（用户信息等）
	@RequestMapping("/accessToken")
	public ModelAndView accessToken(String accessToken) {
		logger.debug("---------客户端/accessToken----------------------------------------------------------------------------------");
		userInfoUrl = "http://localhost:8080/oauthserver/userInfo";
		logger.debug("accessToken");
		OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
		
		try {
	        OAuthClientRequest userInfoRequest = new OAuthBearerClientRequest(userInfoUrl)
	        .setAccessToken(accessToken).buildQueryMessage();
	        OAuthResourceResponse resourceResponse = oAuthClient.resource(userInfoRequest, OAuth.HttpMethod.GET, OAuthResourceResponse.class);
	        String username = resourceResponse.getBody();
	        logger.debug(username);
	        ModelAndView modelAndView = new ModelAndView("usernamePage");
	        modelAndView.addObject("username", username);
	        logger.debug("---------客户端/accessToken----------------------------------------------------------------------------------");
	        return modelAndView;
		} catch (OAuthSystemException e) {
			e.printStackTrace();
		} catch (OAuthProblemException e) {
			e.printStackTrace();
		}
		logger.debug("---------客户端/accessToken----------------------------------------------------------------------------------");
		return null;
	}
	
}