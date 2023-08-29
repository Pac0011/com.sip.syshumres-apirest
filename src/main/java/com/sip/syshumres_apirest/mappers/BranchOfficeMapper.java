package com.sip.syshumres_apirest.mappers;

import java.util.ArrayList;
import java.util.Comparator;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.sip.syshumres_entities.BranchOffice;
import com.sip.syshumres_entities.dtos.BranchOfficeDTO;
import com.sip.syshumres_entities.dtos.EntitySelectDTO;
import com.sip.syshumres_utils.StringTrim;

public class BranchOfficeMapper {
	
	@Autowired
	private ModelMapper modelMapper;
	
	public BranchOfficeMapper() {
	}
	
	public BranchOfficeDTO toDto(BranchOffice entity) {
		BranchOfficeDTO dto = new BranchOfficeDTO();
		dto.setId(entity.getId());
		dto.setDescription(entity.getDescription());
		dto.setFullPhoneNumber(entity.getFullPhoneNumber());
		dto.setPhoneNumber(entity.getPhoneNumber());
	    dto.setExtPhoneNumber(entity.getExtPhoneNumber());
	    dto.setShortPhoneNumber(entity.getShortPhoneNumber());
		dto.setEmail(entity.getEmail());
		
		if (entity.getBranchOfficeType() != null) {
	        dto.setBranchOfficeType(this.modelMapper.map(entity.getBranchOfficeType(), EntitySelectDTO.class));
	    }
	    if (entity.getCostCenter() != null) {
	        dto.setCostCenter(this.modelMapper.map(entity.getCostCenter(), EntitySelectDTO.class));
	    }
	    if (entity.getRegion() != null) {
	        dto.setRegion(this.modelMapper.map(entity.getRegion(), EntitySelectDTO.class));
	    }
	    if (entity.getFather() != null) {
	        dto.setFather(this.modelMapper.map(entity.getFather(), EntitySelectDTO.class));
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
		
		dto.setAddress(entity.getAddress());
		dto.setEnabled(entity.isEnabled());
		//e.setManagingCompanies(null);
		
		return dto;
	}
	
	public EntitySelectDTO toSelectDto(BranchOffice entity) {
		return this.modelMapper.map(entity, EntitySelectDTO.class);
	}
	
	public BranchOffice toCreateEntity(BranchOffice entity) {
		BranchOffice e = new BranchOffice();
		e.setDescription(StringTrim.trimAndRemoveDiacriticalMarks(entity.getDescription()));
		e.setPhoneNumber(StringTrim.trimAndRemoveDiacriticalMarks(entity.getPhoneNumber()));
		e.setExtPhoneNumber(StringTrim.trimAndRemoveDiacriticalMarks(entity.getExtPhoneNumber()));
		e.setShortPhoneNumber(StringTrim.trimAndRemoveDiacriticalMarks(entity.getShortPhoneNumber()));
		e.setEmail(StringTrim.trimAndRemoveDiacriticalMarks(entity.getEmail()));
		e.setAddress(entity.getAddress());
		e.setBranchOfficeType(entity.getBranchOfficeType());
		//e.setManagingCompany(entity.getManagingCompany());
		e.setCostCenter(entity.getCostCenter());
		e.setRegion(entity.getRegion());
		e.setFather(entity.getFather());
		//e.setChilds(entity.getChilds());
		e.setEnabled(entity.isEnabled());
		
		return e;
	}
	
	public BranchOffice toEditEntity(BranchOffice e, BranchOffice entity) {
		e.setDescription(StringTrim.trimAndRemoveDiacriticalMarks(entity.getDescription()));
		e.setPhoneNumber(StringTrim.trimAndRemoveDiacriticalMarks(entity.getPhoneNumber()));
		e.setExtPhoneNumber(StringTrim.trimAndRemoveDiacriticalMarks(entity.getExtPhoneNumber()));
		e.setShortPhoneNumber(StringTrim.trimAndRemoveDiacriticalMarks(entity.getShortPhoneNumber()));
		e.setEmail(StringTrim.trimAndRemoveDiacriticalMarks(entity.getEmail()));
		e.setAddress(entity.getAddress());
		e.setBranchOfficeType(entity.getBranchOfficeType());
		//e.setManagingCompany(entity.getManagingCompany());
		e.setCostCenter(entity.getCostCenter());
		e.setRegion(entity.getRegion());
		e.setFather(entity.getFather());
		//e.setChilds(entity.getChilds());
		e.setEnabled(entity.isEnabled());
		
		return e;
	}

}
