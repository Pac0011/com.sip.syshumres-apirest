package com.sip.syshumres_apirest.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.sip.syshumres_entities.EmployeeProfile;
import com.sip.syshumres_entities.dtos.EmployeeClinicalDataDTO;
import com.sip.syshumres_entities.dtos.EmployeeDocumentDTO;
import com.sip.syshumres_entities.dtos.EmployeeGeneralDataDTO;
import com.sip.syshumres_entities.dtos.EmployeeLaborDataDTO;
import com.sip.syshumres_entities.dtos.EmployeePayrollDTO;
import com.sip.syshumres_entities.dtos.EmployeeProfileDTO;
import com.sip.syshumres_entities.dtos.EmployeeProfileViewDTO;
import com.sip.syshumres_entities.dtos.EntitySelectDTO;
import com.sip.syshumres_utils.StringTrim;


public class EmployeeProfileMapper {
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private EmployeeClinicalDataMapper employeeClinicalDataMapper;
	
	@Autowired
	private EmployeePayrollMapper employeePayrollMapper;
	
	@Autowired
	private EmployeeGeneralDataMapper employeeGeneralDataMapper;
	
	@Autowired
	private EmployeeLaborDataMapper employeeLaborDataMapper;
  	
	public EmployeeProfileMapper() {
	}
	
	public EmployeeProfileViewDTO toViewDto(EmployeeProfile entity) {
		EmployeeProfileViewDTO dto = new EmployeeProfileViewDTO();
		
		dto.setId(entity.getId());
		dto.setEmployeeNumber(entity.getEmployeeNumber());
		dto.setFirstName(entity.getFirstName());
		dto.setLastName(entity.getLastName());
		dto.setLastNameSecond(entity.getLastNameSecond());
		dto.setCurp(entity.getCurp());
		
		if (entity.getEmployeePayroll() != null) {
			dto.setNss(entity.getEmployeePayroll().getNss());
			dto.setRfc(entity.getEmployeePayroll().getRfc());
			if (entity.getEmployeePayroll().getTypeHiring() != null) {
			    dto.setTypeHiring(entity.getEmployeePayroll().getTypeHiring().getDescription());
			}
		}
		if (entity.getEmployeeOperation() != null) {
			if (entity.getEmployeeOperation().getService() != null) {
		        dto.setService(entity.getEmployeeOperation().getService().getCode());
			}
		}
		if (entity.getEmployeeArea() != null) {
	    	dto.setEmployeeArea(entity.getEmployeeArea().getDescription());
	    }
		if (entity.getEmployeePositionProfile() != null) {
		    dto.setEmployeePositionProfile(entity.getEmployeePositionProfile().getDescription());
		}
		if (entity.getEmployeePosition() != null) {
		    dto.setEmployeePosition(entity.getEmployeePosition().getDescription());
		}
		if (entity.getEmployeeStatus() != null) {
			dto.setEmployeeStatus(entity.getEmployeeStatus().getDescription());
		}
		if (entity.getBranchOffice() != null) {
			dto.setBranchOffice(entity.getBranchOffice().getDescription());
		}
		
		return dto;
	}
	
	public EmployeeProfileDTO toDto(EmployeeProfile entity) {
		EmployeeProfileDTO dto = new EmployeeProfileDTO();
		
		dto.setId(entity.getId());
		dto.setCurp(entity.getCurp());
		dto.setFileCurp(entity.getFileCurp());
		dto.setFrontPhoto(entity.getFrontPhoto());
		dto.setLeftPhoto(entity.getLeftPhoto());
		dto.setRightPhoto(entity.getRightPhoto());
	    dto.setEmployeeNumber(entity.getEmployeeNumber());
	    dto.setEcript(entity.getEcript());
	    dto.setFirstName(entity.getFirstName());
	    dto.setLastName(entity.getLastName());
	    dto.setLastNameSecond(entity.getLastNameSecond());
	    dto.setPhoneNumber(entity.getPhoneNumber());
	    dto.setCellNumber(entity.getCellNumber());
	    dto.setEmail(entity.getEmail());
	    dto.setDateEmployment(entity.getDateEmployment());
	    dto.setDateLeave(entity.getDateLeave());
	    if (entity.getEmployeePosition() != null) {
	    	dto.setEmployeePosition(modelMapper.map(entity.getEmployeePosition(), EntitySelectDTO.class));
	    }
	    if (entity.getEmployeePosition().getEmployeeType() != null) {
	    	dto.setEmployeeType(modelMapper.map(entity.getEmployeePosition().getEmployeeType(), EntitySelectDTO.class));
	    }
	    if (entity.getEmployeePositionProfile() != null) {
	    	dto.setEmployeePositionProfile(modelMapper.map(entity.getEmployeePositionProfile(), EntitySelectDTO.class));
	    }
	    if (entity.getEmployeeArea() != null) {
	    	dto.setEmployeeArea(modelMapper.map(entity.getEmployeeArea(), EntitySelectDTO.class));
	    }
	    if (entity.getMaritalStatus() != null) {
	        dto.setMaritalStatus(modelMapper.map(entity.getMaritalStatus(), EntitySelectDTO.class));
	    }
	    if (entity.getGender() != null) {
	        dto.setGender(modelMapper.map(entity.getGender(), EntitySelectDTO.class));
	    }
	    if (entity.getEmployeeStatus() != null) {
	        dto.setEmployeeStatus(modelMapper.map(entity.getEmployeeStatus(), EntitySelectDTO.class));
	    }
	    if (entity.getBranchOffice() != null) {
	        dto.setBranchOffice(modelMapper.map(entity.getBranchOffice(), EntitySelectDTO.class));
	    }
	    dto.setDateBirth(entity.getDateBirth());
	    dto.setEmployeeAddress(entity.getEmployeeAddress());
	    if (entity.getEmployeeClinicalData() != null) {
	    	//Te mapea un objeto con varios campos siempre y cuando los nombres coincidan
	    	dto.setEmployeeClinicalData(modelMapper.map(entity.getEmployeeClinicalData(), EmployeeClinicalDataDTO.class));
	    }
	    if (entity.getEmployeeGeneralData() != null) {
	    	dto.setEmployeeGeneralData(modelMapper.map(entity.getEmployeeGeneralData(), EmployeeGeneralDataDTO.class));
	    }
	    if (entity.getEmployeeLaborData() != null) {
	    	dto.setEmployeeLaborData(modelMapper.map(entity.getEmployeeLaborData(), EmployeeLaborDataDTO.class));
	    }
	    if (entity.getEmployeePayroll() != null) {
	    	dto.setEmployeePayroll(modelMapper.map(entity.getEmployeePayroll(), EmployeePayrollDTO.class));
	    }
	    if (entity.getEmployeeDocuments() != null) {
	    	List<EmployeeDocumentDTO> listDTO =  entity.getEmployeeDocuments().stream()
			    .map(entityDoc -> modelMapper.map(entityDoc, EmployeeDocumentDTO.class))
			    .collect(Collectors.toList());
	    	dto.setEmployeeDocuments(listDTO);
	    }
		
		return dto;
	}
	
	public EmployeeProfile toSaveEntity(EmployeeProfile entity) {
		EmployeeProfile e = new EmployeeProfile();
		e.setFirstName(StringTrim.trimAndRemoveDiacriticalMarks(entity.getFirstName()));
		e.setLastName(StringTrim.trimAndRemoveDiacriticalMarks(entity.getLastName()));
		e.setLastNameSecond(StringTrim.trimAndRemoveDiacriticalMarks(entity.getLastNameSecond()));
		
		//agregar si existe un pariente en base al nombre, pero aqui debe de ir el reponse no como error
		e.setPhoneNumber(StringTrim.trimAndRemoveDiacriticalMarks(entity.getPhoneNumber()));
		e.setCellNumber(StringTrim.trimAndRemoveDiacriticalMarks(entity.getCellNumber()));		
		e.setEmail(StringTrim.trimAndRemoveDiacriticalMarks(entity.getEmail()));
		
		e.setCurp(StringTrim.trimAndRemoveDiacriticalMarks(entity.getCurp()));
		e.setMaritalStatus(entity.getMaritalStatus());
		e.setGender(entity.getGender());
		
		e.setDateBirth(entity.getDateBirth());
		e.setEmployeeAddress(entity.getEmployeeAddress());
		e.setEmployeePosition(entity.getEmployeePosition());
		e.setEmployeePositionProfile(entity.getEmployeePositionProfile());
		//Adm
		if (entity.getEmployeeArea() != null) {
			e.setEmployeeArea(entity.getEmployeeArea());
		}
		if (entity.getEmployeeClinicalData() != null) {
		    e.setEmployeeClinicalData(employeeClinicalDataMapper.
		    		toSaveEntity(entity.getEmployeeClinicalData()));
		}
		if (entity.getEmployeeGeneralData() != null) {
		    e.setEmployeeGeneralData(employeeGeneralDataMapper.
		    		toSaveEntity(entity.getEmployeeGeneralData()));
		}
		if (entity.getEmployeeLaborData() != null) {		    
		    e.setEmployeeLaborData(employeeLaborDataMapper.
		    		toSaveEntity(entity.getEmployeeLaborData()));
		}
		if (entity.getEmployeePayroll() != null) {
			e.setEmployeePayroll(employeePayrollMapper.
					toSaveEntity(entity.getEmployeePayroll()));
		}
	
		return e;
	}
	
	public EmployeeProfile toEditEntity(EmployeeProfile e, EmployeeProfile entity) {
		e.setFirstName(StringTrim.trimAndRemoveDiacriticalMarks(entity.getFirstName()));
		e.setLastName(StringTrim.trimAndRemoveDiacriticalMarks(entity.getLastName()));
		e.setLastNameSecond(StringTrim.trimAndRemoveDiacriticalMarks(entity.getLastNameSecond()));
		
		//agregar si existe un pariente en base al nombre, pero aqui debe de ir el reponse no como error
		e.setPhoneNumber(StringTrim.trimAndRemoveDiacriticalMarks(entity.getPhoneNumber()));
		e.setCellNumber(StringTrim.trimAndRemoveDiacriticalMarks(entity.getCellNumber()));		
		e.setEmail(StringTrim.trimAndRemoveDiacriticalMarks(entity.getEmail()));
		
		e.setCurp(StringTrim.trimAndRemoveDiacriticalMarks(entity.getCurp()));
		e.setMaritalStatus(entity.getMaritalStatus());
		e.setGender(entity.getGender());
		
		e.setDateBirth(entity.getDateBirth());
		e.setEmployeeAddress(entity.getEmployeeAddress());
		e.setEmployeePosition(entity.getEmployeePosition());
		e.setEmployeePositionProfile(entity.getEmployeePositionProfile());
		e.setDateEmployment(entity.getDateEmployment());
		//Adm
		if (entity.getEmployeeArea() != null) {
			e.setEmployeeArea(entity.getEmployeeArea());
		}
		if (entity.getEmployeeClinicalData() != null) {
			e.setEmployeeClinicalData(employeeClinicalDataMapper.
					toEditEntity(e.getEmployeeClinicalData(), entity.getEmployeeClinicalData()));
		}
		if (entity.getEmployeeGeneralData() != null) {
		    e.setEmployeeGeneralData(employeeGeneralDataMapper.
		    		toEditEntity(e.getEmployeeGeneralData(), entity.getEmployeeGeneralData()));
		}
		if (entity.getEmployeeLaborData() != null) {
		    e.setEmployeeLaborData(employeeLaborDataMapper.
		    		toEditEntity(e.getEmployeeLaborData(), entity.getEmployeeLaborData()));
		}
		if (entity.getEmployeePayroll() != null) {
			e.setEmployeePayroll(this.employeePayrollMapper.
					toEditEntity(e.getEmployeePayroll(), entity.getEmployeePayroll()));
		}
		
		return e;
	}

}