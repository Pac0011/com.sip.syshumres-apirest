package com.sip.syshumres_apirest.mappers;

import java.util.ArrayList;
import java.util.Comparator;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.sip.syshumres_entities.BranchOffice;
import com.sip.syshumres_entities.User;
import com.sip.syshumres_entities.dtos.UserDTO;
import com.sip.syshumres_entities.dtos.EntitySelectDTO;
import com.sip.syshumres_utils.StringTrim;

public class UserMapper {
	
	@Autowired
	private ModelMapper modelMapper;
	
	public UserMapper() {
	}

	public UserDTO toDto(User entity) {
		UserDTO dto = new UserDTO();
		
		dto.setId(entity.getId());
		dto.setFirstName(entity.getFirstName());
		dto.setUsername(entity.getUsername());
		dto.setEmail(entity.getEmail());
		
		if (entity.getBranchOffice() != null) {
	        dto.setBranchOffice(modelMapper.map(entity.getBranchOffice(), EntitySelectDTO.class));
	    }
		
		ArrayList<EntitySelectDTO> authoritiesDTO = new ArrayList<EntitySelectDTO>();
		if (entity.getAuthorities() != null) {
			entity.getAuthorities().forEach(authority -> {
				EntitySelectDTO dtoE = new EntitySelectDTO();
				dtoE.setId(authority.getId());
				dtoE.setDescription(authority.getDescription());
				authoritiesDTO.add(dtoE);
			});
		}
		authoritiesDTO.sort(Comparator.comparing(EntitySelectDTO::getDescription));
		dto.setAuthorities(authoritiesDTO);
		
		dto.setTokenExpired(entity.isTokenExpired());
		dto.setEnabled(entity.isEnabled());
		dto.setMultiBranchOffice(entity.isMultiBranchOffice());
		dto.setSeeAllBranchs(entity.isSeeAllBranchs());
		
		return dto;
	}
	
	public User toSaveEntity(UserDTO entity) {
		User e = new User();
		e.setFirstName(StringTrim.trimAndRemoveDiacriticalMarks(entity.getFirstName()));
		e.setEmail(StringTrim.trimAndRemoveDiacriticalMarks(entity.getEmail()));
		e.setUsername(StringTrim.trimAndRemoveDiacriticalMarks(entity.getUsername()));
		e.setTokenExpired(entity.isTokenExpired());
		e.setEnabled(entity.isEnabled());
		if (entity.getBranchOffice() != null) {
	        e.setBranchOffice(this.modelMapper.map(entity.getBranchOffice(), BranchOffice.class));
	    }
		e.setMultiBranchOffice(entity.isMultiBranchOffice());
		e.setSeeAllBranchs(entity.isSeeAllBranchs());
		
		return e;
	}
	
	public User toEditEntity(User e, UserDTO entity) {
        e.setFirstName(StringTrim.trimAndRemoveDiacriticalMarks(entity.getFirstName()));
		e.setEmail(StringTrim.trimAndRemoveDiacriticalMarks(entity.getEmail()));
		e.setUsername(StringTrim.trimAndRemoveDiacriticalMarks(entity.getUsername()));
		e.setTokenExpired(entity.isTokenExpired());
		e.setEnabled(entity.isEnabled());
		if (entity.getBranchOffice() != null) {
	        e.setBranchOffice(this.modelMapper.map(entity.getBranchOffice(), BranchOffice.class));
	    }
		e.setMultiBranchOffice(entity.isMultiBranchOffice());
		e.setSeeAllBranchs(entity.isSeeAllBranchs());
		
		return e;
	}

}
