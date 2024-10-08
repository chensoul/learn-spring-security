/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.chensoul.lss;

import java.util.UUID;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableResourceServer
@RestController
public class SampleSecureOAuth2ActuatorApplication {

  @GetMapping("/")
  public Message home() {
    return new Message("Hello World");
  }

  @Bean
  UserDetailsService userDetailsService() throws Exception {
    UserDetails user = User.withDefaultPasswordEncoder().username("user").password("pass").roles("USER")
            .build();
    return new InMemoryUserDetailsManager(user);
  }

  public static void main(String[] args) {
    SpringApplication.run(SampleSecureOAuth2ActuatorApplication.class, args);
  }

  class Message {

    private String id = UUID.randomUUID().toString();

    private String value;

    public Message(String value) {
      this.value = value;
    }

    public String getId() {
      return this.id;
    }

    public String getValue() {
      return this.value;
    }

  }

}
