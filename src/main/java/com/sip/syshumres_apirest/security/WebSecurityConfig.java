package com.sip.syshumres_apirest.security;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.sip.syshumres_apirest.config.AppProperties;
import com.sip.syshumres_apirest.config.UploadProperties;

@Configuration
public class WebSecurityConfig {
	
	private final UserDetailsService userDetailService;
	
	private final JwtAuthorizationFilter jwtAuthorizationFilter;
	
	private final JwtService jwtService;	
			
	private final AppProperties appProperties;
	
	private final UploadProperties uploadProperties;
	
	private static final String ROLE_ADMIN = "ROLE_ADMIN";
	
	@Autowired
	public WebSecurityConfig(UserDetailsService userDetailsService, 
			JwtAuthorizationFilter jwtAuthorizationFilter,
			JwtService jwtService,
			AppProperties appProperties,
			UploadProperties uploadProperties) {
		super();
		this.userDetailService = userDetailsService;
		this.jwtAuthorizationFilter = jwtAuthorizationFilter;
		this.jwtService = jwtService;
		this.appProperties = appProperties;
		this.uploadProperties = uploadProperties;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authManager) throws Exception {
		
		JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtService);
		jwtAuthenticationFilter.setAuthenticationManager(authManager);
		jwtAuthenticationFilter.setFilterProcessesUrl(appProperties.getUrlAccessLogin());
		
		//"/","/include/**","/css/**","/icons/**","/images/**","/js/**","/layer/**"
		String[] resources = new String[] {
				uploadProperties.getUrlDocumentsEmployees() + "**", 
				appProperties.getUrlPasswordRecovery() + "**",
                "/home"
        };
		//.logout()
    	//	.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
    	//	.invalidateHttpSession(true)        // set invalidation state when logout
    	//	.deleteCookies(this.sessionUserName)
    	//	.and()
		
		return http
				.cors().and().csrf().disable()//Se deshabilita por que es una ApiRest y no manda un formulario
				.formLogin().disable()//Se deshabilita por que es una ApiRest y no manda un formulario
				.logout().disable()//Se deshabilita por que es una ApiRest y no manda un formulario
				.authorizeRequests()
					.antMatchers(resources)
					.permitAll()
			        .antMatchers("/swagger-ui.html", "/swagger-ui/**", "/webjars/**", 
			        		"/swagger-resources/**", "/swagger-resources", "/v2/api-docs/**", 
			        		"/proxy/**")
			        .permitAll()
					.antMatchers("/users/**").hasAnyAuthority(ROLE_ADMIN)
					.antMatchers("/modules/**").hasAnyAuthority(ROLE_ADMIN)
					.antMatchers("/authorities/**").hasAnyAuthority(ROLE_ADMIN)
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
	
	@Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:4200"); // Reemplaza con tu origen permitido
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("Content-Type");
        configuration.addAllowedHeader("Authorization");
        configuration.addExposedHeader("*");
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }
	
}
