# oauth2-legacy-lesson01

示例来自：https://github.com/spring-attic/spring-security-oauth2-boot/tree/main/samples

curl client:secret@localhost:8083/oauth/token -dgrant_type=client_credentials -dscope=read
curl client:secret@localhost:8083/oauth/token -dgrant_type=password -dusername=user -dpassword=password -dscope=read

http://localhost:8083/oauth/authorize?response_type=code&client_id=client&scope=read&redirect_uri=http://localhost:8082/lsso-client/login/oauth2/code/custom

OAuth2Server Filter链

Security filter chain: [
WebAsyncManagerIntegrationFilter
SecurityContextPersistenceFilter
HeaderWriterFilter
LogoutFilter
OAuth2AuthenticationProcessingFilter
UsernamePasswordAuthenticationFilter
DefaultLoginPageGeneratingFilter
DefaultLogoutPageGeneratingFilter
BasicAuthenticationFilter
RequestCacheAwareFilter
SecurityContextHolderAwareRequestFilter
AnonymousAuthenticationFilter
SessionManagementFilter
ExceptionTranslationFilter
FilterSecurityInterceptor
]

OAuth2Client Filter链

Security filter chain: [
WebAsyncManagerIntegrationFilter
SecurityContextPersistenceFilter
HeaderWriterFilter
CsrfFilter
LogoutFilter
OAuth2AuthorizationRequestRedirectFilter
OAuth2LoginAuthenticationFilter
DefaultLoginPageGeneratingFilter
DefaultLogoutPageGeneratingFilter
RequestCacheAwareFilter
SecurityContextHolderAwareRequestFilter
AnonymousAuthenticationFilter
SessionManagementFilter
ExceptionTranslationFilter
FilterSecurityInterceptor
]