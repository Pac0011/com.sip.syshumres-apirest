package com.sip.syshumres_apirest.mappers;

import java.util.ArrayList;
import java.util.Comparator;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.sip.syshumres_entities.Authority;
import com.sip.syshumres_entities.dtos.AuthorityDTO;
import com.sip.syshumres_entities.dtos.EntitySelectDTO;
import com.sip.syshumres_utils.StringTrim;

public class AuthorityMapper {
	
	@Autowired
	private ModelMapper modelMapper;
	
	public AuthorityMapper() {
	}
	
	public AuthorityDTO toDto(Authority entity) {
		AuthorityDTO dto = new AuthorityDTO();
		dto.setId(entity.getId());
		dto.setDescription(entity.getDescription());
		dto.setDetail(entity.getDetail());
		dto.setModulesDtos(null);
		
		ArrayList<EntitySelectDTO> usersDTO = new ArrayList<EntitySelectDTO>();
		if (entity.getUsers() != null) {
			entity.getUsers().forEach(user -> {
				usersDTO.add(new EntitySelectDTO(user.getId(), user.getUsername()));
			});
		}
		dto.setUsers(usersDTO);
		
		ArrayList<EntitySelectDTO> modulesDTO = new ArrayList<EntitySelectDTO>();
		if (entity.getModules() != null) {
			entity.getModules().forEach(module -> {
				modulesDTO.add(new EntitySelectDTO(module.getId(), module.getDescription()));
			});
		}
		modulesDTO.sort(Comparator.comparing(EntitySelectDTO::getDescription));
		dto.setModules(modulesDTO);
		dto.setEnabled(entity.isEnabled());
		
		return dto;
	}
	
	public EntitySelectDTO toSelectDto(Authority entity) {
		return this.modelMapper.map(entity, EntitySelectDTO.class);
	}
	
	public Authority toSaveEntity(AuthorityDTO entity) {
		Authority e = new Authority();
		e.setDescription(StringTrim.trimAndRemoveDiacriticalMarks(entity.getDescription()));
		e.setDetail(StringTrim.trimAndRemoveDiacriticalMarks(entity.getDetail()));
		e.setEnabled(entity.isEnabled());
		
		return e;
	}
	
	public Authority toEditEntity(Authority e, AuthorityDTO entity) {
		e.setDescription(StringTrim.trimAndRemoveDiacriticalMarks(entity.getDescription()));
		e.setDetail(StringTrim.trimAndRemoveDiacriticalMarks(entity.getDetail()));
		e.setEnabled(entity.isEnabled());
		
		return e;
	}

}
