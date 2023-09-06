package com.sip.syshumres_apirest.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sip.syshumres_apirest.mappers.AuthorityMapper;
import com.sip.syshumres_apirest.mappers.BranchOfficeMapper;
import com.sip.syshumres_apirest.mappers.ModuleCustomMapper;
import com.sip.syshumres_apirest.mappers.UserMapper;

@Configuration
public class ModelMapperConfig {
	
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
	
	@Bean
	public AuthorityMapper authorityMapper() {
		return new AuthorityMapper();
	}
	
	@Bean
	public ModuleCustomMapper moduleCustomMapper() {
		return new ModuleCustomMapper();
	}
	
	@Bean
	public UserMapper userMapper() {
		return new UserMapper();
	}
	
	@Bean
	public BranchOfficeMapper branchOfficeMapper() {
		return new BranchOfficeMapper();
	}
}