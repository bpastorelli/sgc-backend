package br.com.sgc.security.constants;

public class SecurityConstants {
	
	  public static final String SECRET = "SECRET_KEY";
	  public static final long EXPIRATION_TIME = 1200_000; // 20 mins
	  public static final String TOKEN_PREFIX = "Bearer ";
	  public static final String HEADER_STRING = "Authorization";
	  public static final String SIGN_UP_URL = "/api/services/controller/user";

}
