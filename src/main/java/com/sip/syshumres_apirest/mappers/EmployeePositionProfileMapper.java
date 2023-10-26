package com.sip.syshumres_apirest.mappers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.sip.syshumres_entities.EmployeePositionProfile;
import com.sip.syshumres_entities.TypeStaff;
import com.sip.syshumres_entities.dtos.EmployeePositionProfileDTO;
import com.sip.syshumres_entities.dtos.common.EntitySelectDTO;
import com.sip.syshumres_utils.StringTrim;


public class EmployeePositionProfileMapper {
	
	@Autowired
	private ModelMapper modelMapper;
	
	public EmployeePositionProfileMapper() {// Noncompliant - method is empty
	}
	
	public EntitySelectDTO toSelectDto(EmployeePositionProfile entity) {
		return this.modelMapper.map(entity, EntitySelectDTO.class);
	}
	
	public EmployeePositionProfileDTO toDto(EmployeePositionProfile entity) {
		EmployeePositionProfileDTO dto = new EmployeePositionProfileDTO();
		dto.setId(entity.getId());
		dto.setDescription(StringTrim.trimAndRemoveDiacriticalMarks(entity.getDescription()));
		dto.setPayroll(entity.isPayroll());
		dto.setHasStaff(entity.isHasStaff());
		if (entity.getTypeStaff() != null) {
	        dto.setTypeStaff(this.modelMapper.map(entity.getTypeStaff(), EntitySelectDTO.class));
	    }
		dto.setEnabled(entity.isEnabled());
		
		return dto;
	}
	
	public EmployeePositionProfile toSaveEntity(EmployeePositionProfileDTO entity) {
		EmployeePositionProfile e = new EmployeePositionProfile();
		e.setDescription(StringTrim.trimAndRemoveDiacriticalMarks(entity.getDescription()));
		e.setPayroll(entity.isPayroll());
		e.setHasStaff(entity.isHasStaff());
		if (entity.getTypeStaff() != null) {
	        e.setTypeStaff(this.modelMapper.map(entity.getTypeStaff(), TypeStaff.class));
	    }
		e.setEnabled(entity.isEnabled());
		
		return e;
	}
	
	public EmployeePositionProfile toEditEntity(EmployeePositionProfile e, EmployeePositionProfileDTO entity) {
		e.setDescription(StringTrim.trimAndRemoveDiacriticalMarks(entity.getDescription()));
		e.setPayroll(entity.isPayroll());
		e.setHasStaff(entity.isHasStaff());
		if (entity.getTypeStaff() != null) {
	        e.setTypeStaff(this.modelMapper.map(entity.getTypeStaff(), TypeStaff.class));
	    }
		e.setEnabled(entity.isEnabled());
		
		return e;
	}

}
