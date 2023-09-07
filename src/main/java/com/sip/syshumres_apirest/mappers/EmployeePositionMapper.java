package com.sip.syshumres_apirest.mappers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.sip.syshumres_entities.EmployeePosition;
import com.sip.syshumres_entities.dtos.EntitySelectDTO;
import com.sip.syshumres_utils.StringTrim;


public class EmployeePositionMapper {
	
	@Autowired
	private ModelMapper modelMapper;
	
	public EmployeePositionMapper() {
	}
	
	public EntitySelectDTO toSelectDto(EmployeePosition entity) {
		return this.modelMapper.map(entity, EntitySelectDTO.class);
	}
	
	public EmployeePosition toSaveEntity(EmployeePosition entity) {
		EmployeePosition e = new EmployeePosition();
		e.setDescription(StringTrim.trimAndRemoveDiacriticalMarks(entity.getDescription()));
		e.setEmployeeType(entity.getEmployeeType());
		e.setEnabled(entity.isEnabled());
		
		return e;
	}
	
	public EmployeePosition toEditEntity(EmployeePosition e, EmployeePosition entity) {
		e.setDescription(StringTrim.trimAndRemoveDiacriticalMarks(entity.getDescription()));
		e.setEmployeeType(entity.getEmployeeType());
		e.setEnabled(entity.isEnabled());
		
		return e;
	}

}
