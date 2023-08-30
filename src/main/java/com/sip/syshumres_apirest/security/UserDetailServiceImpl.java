package com.sip.syshumres_apirest.security;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.sip.syshumres_entities.User;
import com.sip.syshumres_entities.Module;
import com.sip.syshumres_repositories.AuthorityRepository;
import com.sip.syshumres_repositories.ModuleRepository;
import com.sip.syshumres_repositories.UserRepository;


@Service
public class UserDetailServiceImpl implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AuthorityRepository authorityRepository;
	
	@Autowired
	private ModuleRepository moduleRepository;
	
	@Value("${SESSION.USER.NAME}")
	private String sessionUserName;

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
		session.setAttribute(this.sessionUserName, user);
		
		return new UserDetailsImpl(user);
	}
	
	@Transactional(readOnly = true)
	public List<Module> findModulesFatherByUsername(String username) {
		List<Module> modules = moduleRepository.findModulesFatherByUsername(username);
		
	    return modules;
	}
	
	@Transactional(readOnly = true)
	public List<Module> findModulesChildByUsername(String username) {
		List<Module> modules = moduleRepository.findModulesChildByUsername(username);
		
	    return modules;
	}

}
