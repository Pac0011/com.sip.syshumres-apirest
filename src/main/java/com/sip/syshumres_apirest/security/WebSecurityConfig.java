package com.sip.syshumres_apirest.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class WebSecurityConfig {
	
	private final UserDetailsService userDetailService;
	
	private final JWTAuthorizationFilter jwtAuthorizationFilter;
	
	private final JWTService jwtService;
	
	@Value("${URL.DOCUMENTS.EMPLOYEES}")
	private String urlDocuments;
	
	@Value("${URL.ACCESS.LOGIN}")
	private String urlLogin;
	
	@Value("${URL.PASSWORD.RECOVERY}")
	private String urlRecovery;
	
	public WebSecurityConfig(UserDetailsService userDetailsService, 
			JWTAuthorizationFilter jwtAuthorizationFilter,
			JWTService jwtService) {
		super();
		this.userDetailService = userDetailsService;
		this.jwtAuthorizationFilter = jwtAuthorizationFilter;
		this.jwtService = jwtService;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authManager) throws Exception {
		
		JWTAuthenticationFilter jwtAuthenticationFilter = new JWTAuthenticationFilter(jwtService);
		jwtAuthenticationFilter.setAuthenticationManager(authManager);
		jwtAuthenticationFilter.setFilterProcessesUrl(this.urlLogin);
		
		//"/","/include/**","/css/**","/icons/**","/images/**","/js/**","/layer/**"
		String[] resources = new String[] {
                this.urlDocuments + "**", 
                this.urlRecovery + "**",
                "/home"
        };
		//.logout()
    	//	.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
    	//	.invalidateHttpSession(true)        // set invalidation state when logout
    	//	.deleteCookies(this.sessionUserName)
    	//	.and()
		
		return http
				.csrf().disable()//Se deshabilita por que es una ApiRest y no manda un formulario
				.formLogin().disable()//Se deshabilita por que es una ApiRest y no manda un formulario
				.logout().disable()//Se deshabilita por que es una ApiRest y no manda un formulario
				.authorizeRequests()
					.antMatchers(resources)
					.permitAll()
			        .antMatchers("/swagger-ui.html", "/swagger-ui/**", "/webjars/**", 
			        		"/swagger-resources/**", "/swagger-resources", "/v2/api-docs/**", 
			        		"/proxy/**")
			        .permitAll()
					.antMatchers("/users/**").hasAnyAuthority("ROLE_ADMIN")
					.antMatchers("/modules/**").hasAnyAuthority("ROLE_ADMIN")
					.antMatchers("/authorities/**").hasAnyAuthority("ROLE_ADMIN")
					.anyRequest()
						.authenticated()
					.and()
				.sessionManagement()
					.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
					.and()
				.addFilter(jwtAuthenticationFilter)
				.addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
				.build();
	}
	
	/*@Bean
	UserDetailsService userDetailService() {
		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
		manager.createUser(User.withUsername("admin")
				.password(passwordEncoder().encode("admin"))
				.roles()
				.build());
		
		return manager;
	}*/
	
	@Bean
	AuthenticationManager authManager(HttpSecurity http) throws Exception {
		return http.getSharedObject(AuthenticationManagerBuilder.class)
				.userDetailsService(userDetailService)
				.passwordEncoder(passwordEncoder())
				.and()
				.build();				
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	//public static void main(String[] args) {
	//	System.out.println("pass: " + new BCryptPasswordEncoder().encode("admin"));
	//}

}
