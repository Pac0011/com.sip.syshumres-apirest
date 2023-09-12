package com.sip.syshumres_apirest.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sip.syshumres_apirest.mappers.AuthorityMapper;
import com.sip.syshumres_apirest.mappers.BranchOfficeMapper;
import com.sip.syshumres_apirest.mappers.CostCenterMapper;
import com.sip.syshumres_apirest.mappers.EmployeeAreaMapper;
import com.sip.syshumres_apirest.mappers.EmployeeClinicalDataMapper;
import com.sip.syshumres_apirest.mappers.EmployeeGeneralDataMapper;
import com.sip.syshumres_apirest.mappers.EmployeeLaborDataMapper;
import com.sip.syshumres_apirest.mappers.EmployeePayrollMapper;
import com.sip.syshumres_apirest.mappers.EmployeePositionMapper;
import com.sip.syshumres_apirest.mappers.EmployeePositionProfileMapper;
import com.sip.syshumres_apirest.mappers.EmployeeProfileMapper;
import com.sip.syshumres_apirest.mappers.ManagingCompanyMapper;
import com.sip.syshumres_apirest.mappers.ModuleCustomMapper;
import com.sip.syshumres_apirest.mappers.ProspectProfileMapper;
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
	
	@Bean CostCenterMapper costCenterMapper() {
		return new CostCenterMapper();
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
	
	@Bean
	public EmployeeAreaMapper employeeAreaMapper() {
		return new EmployeeAreaMapper();
	}
	
	@Bean
	public EmployeeProfileMapper employeeProfileMapper() {
		return new EmployeeProfileMapper();
	}
	
	@Bean
	public EmployeeClinicalDataMapper employeeClinicalDataMapper() {
		return new EmployeeClinicalDataMapper();
		
	}
	
	@Bean
	public EmployeePayrollMapper employeePayrollMapper() {
		return new EmployeePayrollMapper();
	}
	
	@Bean
	public EmployeeGeneralDataMapper employeeGeneralDataMapper() {
		return new EmployeeGeneralDataMapper();
	}
	
	@Bean
	public EmployeeLaborDataMapper employeeLaborDataMapper() {
		return new EmployeeLaborDataMapper();
	}
	
	@Bean
	public EmployeePositionMapper employeePositionMapper() {
		return new EmployeePositionMapper();
	}
	
	@Bean
	public EmployeePositionProfileMapper employeePositionProfileMapper() {
		return new EmployeePositionProfileMapper();
	}
	
	@Bean
	public ManagingCompanyMapper managingCompanyMapper() {
		return new ManagingCompanyMapper();
	}
	
	@Bean
	public ProspectProfileMapper prospectProfileMapper() {
		return new ProspectProfileMapper();
	}
}