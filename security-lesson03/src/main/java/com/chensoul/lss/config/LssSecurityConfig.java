package com.chensoul.lss.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class LssSecurityConfig extends WebSecurityConfigurerAdapter {
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.
			inMemoryAuthentication().passwordEncoder(passwordEncoder()).
			withUser("user").password(passwordEncoder().encode("pass")).roles("USER")
			.and()
			.withUser("admin").password(passwordEncoder().encode("pass")).roles("ADMIN");
		;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception { // @formatter:off
		http
			.authorizeRequests()
			.mvcMatchers(HttpMethod.GET,"/users/**").hasRole("ADMIN")
			.anyRequest().authenticated()

			.and().formLogin().permitAll()

			.and()
			.csrf().disable()
		;
	} // @formatter:on

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
