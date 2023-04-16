package org.sid.springmvc.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	 @Bean
	    public InMemoryUserDetailsManager userDetailsService() {
	        UserDetails user = User.withUsername("user")
	            .password(passwordEncoder().encode("userPass"))
	            .roles("USER")
	            .build();
	        UserDetails admin = User.withUsername("admin")
	            .password(passwordEncoder().encode("adminPass"))
	            .roles("ADMIN","USER")
	            .build();
	        return new InMemoryUserDetailsManager(user, admin);
	    }
	 
	@Bean
	    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		 http.formLogin().loginPage("/login");
		 
	        http.csrf()
	            .disable()
	            .authorizeHttpRequests(auth -> {
						auth
						.requestMatchers("/admin/**" ,"/save**/**","/delete**/**","/form**/**")
						.hasRole("ADMIN")
						.requestMatchers("/patients**/**")
						.hasRole("USER")
						.requestMatchers("/user/**","/login","/webjars/**") 
						.permitAll()
						.anyRequest()
						.authenticated();
				});
	        
	       
	       
	        http.exceptionHandling().accessDeniedPage("/notAuthorized");
	            return http.build();
	      }
	    
	    @Bean 
	    public PasswordEncoder passwordEncoder() { 
	        return new BCryptPasswordEncoder(); 
	    }
	
}