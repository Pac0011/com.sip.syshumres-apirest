package com.sip.syshumres_apirest.mappers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.sip.syshumres_entities.EmployeePositionProfile;
import com.sip.syshumres_entities.dtos.EmployeePositionProfileDTO;
import com.sip.syshumres_entities.dtos.EntitySelectDTO;
import com.sip.syshumres_utils.StringTrim;


public class EmployeePositionProfileMapper {
	
	@Autowired
	private ModelMapper modelMapper;
	
	public EmployeePositionProfileMapper() {
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
	
	public EmployeePositionProfile toSaveEntity(EmployeePositionProfile entity) {
		EmployeePositionProfile e = new EmployeePositionProfile();
		e.setDescription(StringTrim.trimAndRemoveDiacriticalMarks(entity.getDescription()));
		e.setPayroll(entity.isPayroll());
		e.setHasStaff(entity.isHasStaff());
		e.setTypeStaff(entity.getTypeStaff());
		e.setEnabled(entity.isEnabled());
		
		return e;
	}
	
	public EmployeePositionProfile toEditEntity(EmployeePositionProfile e, EmployeePositionProfile entity) {
		e.setDescription(StringTrim.trimAndRemoveDiacriticalMarks(entity.getDescription()));
		e.setPayroll(entity.isPayroll());
		e.setHasStaff(entity.isHasStaff());
		e.setTypeStaff(entity.getTypeStaff());
		e.setEnabled(entity.isEnabled());
		
		return e;
	}

}
