package com.sip.syshumres_apirest.security;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.sip.syshumres_entities.User;
import com.sip.syshumres_apirest.config.AppProperties;
import com.sip.syshumres_entities.Module;
import com.sip.syshumres_repositories.AuthorityRepository;
import com.sip.syshumres_repositories.ModuleRepository;
import com.sip.syshumres_repositories.UserRepository;


@Service
public class UserDetailServiceImpl implements UserDetailsService {
	
	private final UserRepository userRepository;
	
	private final AuthorityRepository authorityRepository;
	
	private final ModuleRepository moduleRepository;
	
	private final AppProperties appProperties;
	
	@Autowired
	public UserDetailServiceImpl(UserRepository userRepository, AuthorityRepository authorityRepository,
			ModuleRepository moduleRepository, AppProperties appProperties) {
		super();
		this.userRepository = userRepository;
		this.authorityRepository = authorityRepository;
		this.moduleRepository = moduleRepository;
		this.appProperties = appProperties;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findOneByUsername(username)
			.orElseThrow(() -> new UsernameNotFoundException("El usuario " + username + " no existe"));
		
		//Add Authorities(Roles) to user
		user.setAuthorities(authorityRepository.findAuthoritiesByUsername(username));
		
		//Create session
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpSession session = attr.getRequest().getSession(true);
		session.setMaxInactiveInterval(15);
		session.setAttribute(appProperties.getSessionUserName(), user);
		
		return new UserDetailsImpl(user);
	}
	
	@Transactional(readOnly = true)
	public List<Module> findModulesFatherByUsername(String username) {
		return moduleRepository.findModulesFatherByUsername(username);
	}
	
	@Transactional(readOnly = true)
	public List<Module> findModulesChildByUsername(String username) {
		return moduleRepository.findModulesChildByUsername(username);
	}

}
