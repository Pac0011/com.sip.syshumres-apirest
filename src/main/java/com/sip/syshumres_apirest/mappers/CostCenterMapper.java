package com.sip.syshumres_apirest.mappers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.sip.syshumres_entities.CostCenter;
import com.sip.syshumres_entities.dtos.CostCenterDTO;
import com.sip.syshumres_entities.dtos.common.EntitySelectDTO;
import com.sip.syshumres_utils.StringTrim;


public class CostCenterMapper {
  	
	@Autowired
	private ModelMapper modelMapper;
  	
	public CostCenterMapper() {
	}
	
	public CostCenterDTO toDto(CostCenter entity) {
		CostCenterDTO dto = new CostCenterDTO();
		
		dto.setId(entity.getId());
		dto.setDescription(entity.getDescription());
		dto.setCode(entity.getCode());
		dto.setEnabled(entity.isEnabled());
		
		return dto;
	}
	
	public EntitySelectDTO toSelectDto(CostCenter entity) {
		return this.modelMapper.map(entity, EntitySelectDTO.class);
	}
	
	public CostCenter toSaveEntity(CostCenterDTO entity) {
		CostCenter e = new CostCenter();
		e.setDescription(StringTrim.trimAndRemoveDiacriticalMarks(entity.getDescription()));
		e.setCode(StringTrim.trimAndRemoveDiacriticalMarks(entity.getCode()));
		e.setEnabled(entity.isEnabled());
		
		return e;
	}
	
	public CostCenter toEditEntity(CostCenter e, CostCenterDTO entity) {
		e.setDescription(StringTrim.trimAndRemoveDiacriticalMarks(entity.getDescription()));
		e.setCode(StringTrim.trimAndRemoveDiacriticalMarks(entity.getCode()));
		e.setEnabled(entity.isEnabled());
		
		return e;
	}

}
