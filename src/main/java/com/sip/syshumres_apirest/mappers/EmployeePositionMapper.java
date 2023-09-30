package com.sip.syshumres_apirest.mappers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.sip.syshumres_entities.EmployeePosition;
import com.sip.syshumres_entities.EmployeeType;
import com.sip.syshumres_entities.dtos.EmployeePositionDTO;
import com.sip.syshumres_entities.dtos.common.EntitySelectDTO;
import com.sip.syshumres_utils.StringTrim;


public class EmployeePositionMapper {
	
	@Autowired
	private ModelMapper modelMapper;
	
	public EmployeePositionMapper() {
	}
	
	public EntitySelectDTO toSelectDto(EmployeePosition entity) {
		return this.modelMapper.map(entity, EntitySelectDTO.class);
	}
	
	public EmployeePositionDTO toDto(EmployeePosition entity) {
		EmployeePositionDTO dto = new EmployeePositionDTO();
		dto.setId(entity.getId());
		dto.setDescription(entity.getDescription());
		
		if (entity.getEmployeeType() != null) {
	        dto.setEmployeeType(modelMapper.map(entity.getEmployeeType(), EntitySelectDTO.class));
	    }
		dto.setEnabled(entity.isEnabled());
		
		return dto;
	}
	
	public EmployeePosition toSaveEntity(EmployeePositionDTO entity) {
		EmployeePosition e = new EmployeePosition();
		e.setDescription(StringTrim.trimAndRemoveDiacriticalMarks(entity.getDescription()));
		
		if (entity.getEmployeeType() != null) {
	        e.setEmployeeType(this.modelMapper.map(entity.getEmployeeType(), EmployeeType.class));
	    }		
		e.setEnabled(entity.isEnabled());
		
		return e;
	}
	
	public EmployeePosition toEditEntity(EmployeePosition e, EmployeePositionDTO entity) {
		e.setDescription(StringTrim.trimAndRemoveDiacriticalMarks(entity.getDescription()));
		
		if (entity.getEmployeeType() != null) {
	        e.setEmployeeType(this.modelMapper.map(entity.getEmployeeType(), EmployeeType.class));
	    }
		e.setEnabled(entity.isEnabled());
		
		return e;
	}

}
