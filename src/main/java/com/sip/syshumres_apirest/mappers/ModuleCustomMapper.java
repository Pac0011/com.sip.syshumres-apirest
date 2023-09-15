package com.sip.syshumres_apirest.mappers;

import java.util.ArrayList;
import java.util.Comparator;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.sip.syshumres_entities.Module;
import com.sip.syshumres_entities.dtos.ModuleDTO;
import com.sip.syshumres_entities.dtos.EntitySelectDTO;
import com.sip.syshumres_utils.StringTrim;

public class ModuleCustomMapper {
	
	@Autowired
	private ModelMapper modelMapper;
	
	public ModuleCustomMapper() {
	}
	
	public ModuleDTO toDto(Module entity) {
		ModuleDTO dto = new ModuleDTO();
		
		dto.setId(entity.getId());
		dto.setDescription(entity.getDescription());
		dto.setDetail(entity.getDetail());
		dto.setUrl(entity.getUrl());
		dto.setUrlMenu(entity.getUrlMenu());
		dto.setIcon(entity.getIcon());
		
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
		ArrayList<EntitySelectDTO> authoritiesDTO = new ArrayList<EntitySelectDTO>();
		if (entity.getAuthorities() != null) {
			entity.getAuthorities().forEach(module -> {
				EntitySelectDTO dtoE = new EntitySelectDTO();
				dtoE.setId(module.getId());
				dtoE.setDescription(module.getDescription());
				authoritiesDTO.add(dtoE);
			});
		}
		authoritiesDTO.sort(Comparator.comparing(EntitySelectDTO::getDescription));
		dto.setAuthorities(authoritiesDTO);
		dto.setEnabled(entity.isEnabled());
		
		return dto;
	}
	
	public EntitySelectDTO toSelectDto(Module entity) {
		return this.modelMapper.map(entity, EntitySelectDTO.class);
	}
	
	public Module toSaveEntity(ModuleDTO entity) {
		Module e = new Module();
		e.setDescription(StringTrim.trimAndRemoveDiacriticalMarks(entity.getDescription()));
		e.setDetail(StringTrim.trimAndRemoveDiacriticalMarks(entity.getDetail()));
		e.setUrl(StringTrim.trimAndRemoveDiacriticalMarks(entity.getUrl()));
		e.setUrlMenu(StringTrim.trimAndRemoveDiacriticalMarks(entity.getUrlMenu()));
		e.setIcon(StringTrim.trimAndRemoveDiacriticalMarks(entity.getIcon()));
		if (entity.getFather() != null) {
	        e.setFather(this.modelMapper.map(entity.getFather(), Module.class));
	    }
		e.setEnabled(entity.isEnabled());
		
		return e;
	}
	
	public Module toEditEntity(Module e, ModuleDTO entity) {
		e.setDescription(StringTrim.trimAndRemoveDiacriticalMarks(entity.getDescription()));
		e.setDetail(StringTrim.trimAndRemoveDiacriticalMarks(entity.getDetail()));
		e.setUrl(StringTrim.trimAndRemoveDiacriticalMarks(entity.getUrl()));
		e.setUrlMenu(StringTrim.trimAndRemoveDiacriticalMarks(entity.getUrlMenu()));
		e.setIcon(StringTrim.trimAndRemoveDiacriticalMarks(entity.getIcon()));
		if (entity.getFather() != null) {
	        e.setFather(this.modelMapper.map(entity.getFather(), Module.class));
	    }
		e.setEnabled(entity.isEnabled());
		
		return e;
	}

}
