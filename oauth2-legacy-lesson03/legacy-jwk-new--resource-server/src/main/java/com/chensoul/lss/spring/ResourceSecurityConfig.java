package com.chensoul.lss.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ResourceSecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {// @formatter:off
        http.authorizeHttpRequests(authorize -> authorize
				// JwtGrantedAuthoritiesConverter
				.mvcMatchers(HttpMethod.GET, "/api/projects/**").hasAuthority("SCOPE_read")
                .mvcMatchers(HttpMethod.POST, "/api/projects").hasAuthority("SCOPE_write")
                .anyRequest().authenticated())
            .oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer.jwt(Customizer.withDefaults()));
        return http.build();
    }//@formatter:on

}
