package com.sip.syshumres_apirest.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sip.syshumres_apirest.mappers.BranchOfficeMapper;

@Configuration
public class ModelMapperConfig {
	
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
	
	@Bean
	public BranchOfficeMapper branchOfficeMapper() {
		return new BranchOfficeMapper();
	}
}