package com.sip.syshumres_apirest.mappers;

import java.util.ArrayList;
import java.util.Comparator;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.sip.syshumres_entities.ManagingCompany;
import com.sip.syshumres_entities.dtos.EntitySelectDTO;
import com.sip.syshumres_entities.dtos.ManagingCompanyDTO;
import com.sip.syshumres_utils.StringTrim;


public class ManagingCompanyMapper {
	
	@Autowired
    private ModelMapper modelMapper;
	
	public ManagingCompanyMapper() {
	}
	
	public ManagingCompanyDTO toDto(ManagingCompany entity) {
		ManagingCompanyDTO dto = new ManagingCompanyDTO();
		
		dto.setId(entity.getId());
		dto.setDescription(entity.getDescription());
		
		dto.setPhoneNumber(entity.getPhoneNumber());
	    dto.setExtPhoneNumber(entity.getExtPhoneNumber());
	    dto.setShortPhoneNumber(entity.getShortPhoneNumber());
	    dto.setFullPhoneNumber(entity.getFullPhoneNumber());
	    dto.setEmail(entity.getEmail());
	    
		dto.setCompanyName(entity.getCompanyName());
		dto.setRfc(entity.getRfc());
		dto.setEmployerRegistration(entity.getEmployerRegistration());
		dto.setLegalRepresentative(entity.getLegalRepresentative());
		
		if (entity.getTypeHiring() != null) {
	        dto.setTypeHiring(modelMapper.map(entity.getTypeHiring(), EntitySelectDTO.class));
	    }
		ArrayList<EntitySelectDTO> officesDTO = new ArrayList<EntitySelectDTO>();
		if (entity.getBranchOffices() != null) {
			entity.getBranchOffices().forEach(child -> {
				officesDTO.add(new EntitySelectDTO(child.getId(), child.getDescription()));
			});
		}
		officesDTO.sort(Comparator.comparing(EntitySelectDTO::getDescription));
		dto.setBranchOffices(officesDTO);
		dto.setAddress(entity.getAddress());
	    dto.setAddressFiscal(entity.getAddressFiscal());
		dto.setEnabled(entity.isEnabled());
		
		return dto;
	}
	
	public ManagingCompany toSaveEntity(ManagingCompany entity) {
		ManagingCompany e = new ManagingCompany();
		e.setDescription(StringTrim.trimAndRemoveDiacriticalMarks(entity.getDescription()));
		e.setCompanyName(StringTrim.trimAndRemoveDiacriticalMarks(entity.getCompanyName()));
		e.setPhoneNumber(StringTrim.trimAndRemoveDiacriticalMarks(entity.getPhoneNumber()));
		e.setExtPhoneNumber(StringTrim.trimAndRemoveDiacriticalMarks(entity.getExtPhoneNumber()));
		e.setShortPhoneNumber(StringTrim.trimAndRemoveDiacriticalMarks(entity.getShortPhoneNumber()));
		e.setEmail(StringTrim.trimAndRemoveDiacriticalMarks(entity.getEmail()));
		e.setRfc(StringTrim.trimAndRemoveDiacriticalMarks(entity.getRfc()));
		e.setEmployerRegistration(StringTrim.trimAndRemoveDiacriticalMarks(entity.getEmployerRegistration()));
		e.setLegalRepresentative(StringTrim.trimAndRemoveDiacriticalMarks(entity.getLegalRepresentative()));
		e.setTypeHiring(entity.getTypeHiring());
		e.setAddress(entity.getAddress());
		e.setAddressFiscal(entity.getAddressFiscal());
		//e.setBranchOffices(entity.getBranchOffices());
		e.setEnabled(entity.isEnabled());
		
		return e;
	}
	
	public ManagingCompany toEditEntity(ManagingCompany e, ManagingCompany entity) {
		e.setDescription(StringTrim.trimAndRemoveDiacriticalMarks(entity.getDescription()));
		e.setCompanyName(StringTrim.trimAndRemoveDiacriticalMarks(entity.getCompanyName()));
		e.setPhoneNumber(StringTrim.trimAndRemoveDiacriticalMarks(entity.getPhoneNumber()));
		e.setExtPhoneNumber(StringTrim.trimAndRemoveDiacriticalMarks(entity.getExtPhoneNumber()));
		e.setShortPhoneNumber(StringTrim.trimAndRemoveDiacriticalMarks(entity.getShortPhoneNumber()));
		e.setEmail(StringTrim.trimAndRemoveDiacriticalMarks(entity.getEmail()));
		e.setRfc(StringTrim.trimAndRemoveDiacriticalMarks(entity.getRfc()));
		e.setEmployerRegistration(StringTrim.trimAndRemoveDiacriticalMarks(entity.getEmployerRegistration()));
		e.setLegalRepresentative(StringTrim.trimAndRemoveDiacriticalMarks(entity.getLegalRepresentative()));
		e.setTypeHiring(entity.getTypeHiring());
		e.setAddress(entity.getAddress());
		e.setAddressFiscal(entity.getAddressFiscal());
		//e.setBranchOffices(entity.getBranchOffices());
		e.setEnabled(entity.isEnabled());
		
		return e;
	}

}
