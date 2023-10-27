package com.sip.syshumres_apirest.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.sip.syshumres_entities.User;
import com.sip.syshumres_entities.Authority;
import com.sip.syshumres_entities.BranchOffice;


public class UserDetailsImpl implements UserDetails {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final User user;
	
	private static final Logger logger = LoggerFactory.getLogger(UserDetailsImpl.class);

	public UserDetailsImpl() {
		super();
		this.user = null;
	}

	public UserDetailsImpl(User user) {
		super();
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
	   List<GrantedAuthority> authorities = new ArrayList<>();
       for (Authority autho: user.getAuthorities()) {    	    
    	    if (logger.isDebugEnabled()) {
			    logger.debug(autho.getDescription());
	   		}
            authorities.add(new SimpleGrantedAuthority(autho.getDescription()));
        }
		return authorities;
	}
	
	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	
	public String getFirstName() {
		return user.getFirstName();
	}
	
	public BranchOffice getBranchOffice() {
		return user.getBranchOffice();
	}

}
