package com.sip.syshumres_apirest.mappers;

import java.util.ArrayList;
import java.util.Comparator;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.sip.syshumres_apirest.aspects.LogCreateEntity;
import com.sip.syshumres_apirest.aspects.LogEditEntity;
import com.sip.syshumres_entities.Address;
import com.sip.syshumres_entities.BranchOffice;
import com.sip.syshumres_entities.BranchOfficeType;
import com.sip.syshumres_entities.CostCenter;
import com.sip.syshumres_entities.Region;
import com.sip.syshumres_entities.dtos.BranchOfficeDTO;
import com.sip.syshumres_entities.dtos.common.EntitySelectDTO;
import com.sip.syshumres_entities.dtos.AddressDTO;
import com.sip.syshumres_utils.StringTrim;

public class BranchOfficeMapper {
	
	@Autowired
	private ModelMapper modelMapper;
	
	public BranchOfficeMapper() {// Noncompliant - method is empty
	}
	
	public BranchOffice toEntity(BranchOfficeDTO entity) {
		return modelMapper.map(entity, BranchOffice.class);
	}
	
	public BranchOfficeDTO toDto(BranchOffice entity) {
		BranchOfficeDTO dto = new BranchOfficeDTO();
		dto.setId(entity.getId());
		dto.setDescription(entity.getDescription());
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
	    
		ArrayList<EntitySelectDTO> childsDTO = new ArrayList<>();
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
		if (entity.getAddress() != null) {
			dto.setAddress(this.modelMapper.map(entity.getAddress(), AddressDTO.class));
		}
		dto.setEnabled(entity.isEnabled());
		//e.setManagingCompanies(null)
		
		return dto;
	}
	
	public EntitySelectDTO toSelectDto(BranchOffice entity) {
		return this.modelMapper.map(entity, EntitySelectDTO.class);
	}
	
	@LogCreateEntity
	public BranchOffice toCreateEntity(BranchOfficeDTO entity) {
		BranchOffice e = new BranchOffice();
		e.setDescription(StringTrim.trimAndRemoveDiacriticalMarks(entity.getDescription()));
		e.setPhoneNumber(StringTrim.trimAndRemoveDiacriticalMarks(entity.getPhoneNumber()));
		e.setExtPhoneNumber(StringTrim.trimAndRemoveDiacriticalMarks(entity.getExtPhoneNumber()));
		e.setShortPhoneNumber(StringTrim.trimAndRemoveDiacriticalMarks(entity.getShortPhoneNumber()));
		e.setEmail(StringTrim.trimAndRemoveDiacriticalMarks(entity.getEmail()));
		if (entity.getAddress() != null) {
			e.setAddress(this.modelMapper.map(entity.getAddress(), Address.class));
		}
		if (entity.getBranchOfficeType() != null) {
	        e.setBranchOfficeType(this.modelMapper.map(entity.getBranchOfficeType(), BranchOfficeType.class));
	    }
		//e.setManagingCompany(entity.getManagingCompany())
		if (entity.getCostCenter() != null) {
	        e.setCostCenter(this.modelMapper.map(entity.getCostCenter(), CostCenter.class));
	    }
		if (entity.getRegion() != null) {
	        e.setRegion(this.modelMapper.map(entity.getRegion(), Region.class));
	    }
		if (entity.getFather() != null) {
	        e.setFather(this.modelMapper.map(entity.getFather(), BranchOffice.class));
	    }
		//e.setChilds(entity.getChilds())
		e.setEnabled(entity.isEnabled());
		
		return e;
	}
	
	@LogEditEntity
	public BranchOffice toEditEntity(BranchOffice e, BranchOfficeDTO entity) {
		e.setDescription(StringTrim.trimAndRemoveDiacriticalMarks(entity.getDescription()));
		e.setPhoneNumber(StringTrim.trimAndRemoveDiacriticalMarks(entity.getPhoneNumber()));
		e.setExtPhoneNumber(StringTrim.trimAndRemoveDiacriticalMarks(entity.getExtPhoneNumber()));
		e.setShortPhoneNumber(StringTrim.trimAndRemoveDiacriticalMarks(entity.getShortPhoneNumber()));
		e.setEmail(StringTrim.trimAndRemoveDiacriticalMarks(entity.getEmail()));
		if (entity.getAddress() != null) {
		   e.setAddress(this.modelMapper.map(entity.getAddress(), Address.class));
		}
		if (entity.getBranchOfficeType() != null) {
	        e.setBranchOfficeType(this.modelMapper.map(entity.getBranchOfficeType(), BranchOfficeType.class));
	    }
		//e.setManagingCompany(entity.getManagingCompany())
		if (entity.getCostCenter() != null) {
	        e.setCostCenter(this.modelMapper.map(entity.getCostCenter(), CostCenter.class));
	    }
		if (entity.getRegion() != null) {
	        e.setRegion(this.modelMapper.map(entity.getRegion(), Region.class));
	    }
		if (entity.getFather() != null) {
	        e.setFather(this.modelMapper.map(entity.getFather(), BranchOffice.class));
	    }
		//e.setChilds(entity.getChilds())
		e.setEnabled(entity.isEnabled());
		
		return e;
	}

}
