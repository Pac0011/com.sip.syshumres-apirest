package com.sip.syshumres_apirest.mappers;

import java.util.ArrayList;
import java.util.Comparator;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.sip.syshumres_entities.EmployeeArea;
import com.sip.syshumres_entities.dtos.EmployeeAreaDTO;
import com.sip.syshumres_entities.dtos.EntitySelectDTO;
import com.sip.syshumres_utils.StringTrim;


public class EmployeeAreaMapper {
	
	@Autowired
	private ModelMapper modelMapper;
  	
	public EmployeeAreaMapper() {
	}
	
	public EmployeeAreaDTO toDto(EmployeeArea entity) {
		EmployeeAreaDTO dto = new EmployeeAreaDTO();
		
		dto.setId(entity.getId());
		dto.setDescription(entity.getDescription());
		if (entity.getCostCenter() != null) {
	        dto.setCostCenter(modelMapper.map(entity.getCostCenter(), EntitySelectDTO.class));
	    }
	    if (entity.getFather() != null) {
	        dto.setFather(modelMapper.map(entity.getFather(), EntitySelectDTO.class));
	    }
		ArrayList<EntitySelectDTO> childsDTO = new ArrayList<EntitySelectDTO>();
		if (entity.getChilds() != null) {
			entity.getChilds().forEach(child -> {
				EntitySelectDTO dtoE = new EntitySelectDTO();
				dtoE.setId(child.getId());
				dtoE.setDescription(child.getDescription());
				childsDTO.add(dtoE);
			});
		}
		childsDTO.sort(Comparator.comparing(EntitySelectDTO::getDescription));
		dto.setChilds(childsDTO);
		dto.setEnabled(entity.isEnabled());
		
	    return dto;	
	}
	
	public EntitySelectDTO toSelectDto(EmployeeArea entity) {
		return this.modelMapper.map(entity, EntitySelectDTO.class);
	}
	
	public EmployeeArea toSaveEntity(EmployeeArea entity) {
		EmployeeArea e = new EmployeeArea();
		e.setDescription(StringTrim.trimAndRemoveDiacriticalMarks(entity.getDescription()));
		e.setCostCenter(entity.getCostCenter());
		e.setFather(entity.getFather());
		//e.setChilds(entity.getChilds());
		e.setEnabled(entity.isEnabled());
	
		return e;
	}
	
	public EmployeeArea toEditEntity(EmployeeArea e, EmployeeArea entity) {
		e.setDescription(StringTrim.trimAndRemoveDiacriticalMarks(entity.getDescription()));
		e.setCostCenter(entity.getCostCenter());
		e.setFather(entity.getFather());
		//e.setChilds(entity.getChilds());
		e.setEnabled(entity.isEnabled());
		
		return e;
	}

}
