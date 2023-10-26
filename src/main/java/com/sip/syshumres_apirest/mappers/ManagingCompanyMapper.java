package com.sip.syshumres_apirest.mappers;

import java.util.ArrayList;
import java.util.Comparator;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.sip.syshumres_entities.Address;
import com.sip.syshumres_entities.ManagingCompany;
import com.sip.syshumres_entities.TypeHiring;
import com.sip.syshumres_entities.dtos.ManagingCompanyDTO;
import com.sip.syshumres_entities.dtos.common.EntitySelectDTO;
import com.sip.syshumres_entities.dtos.AddressDTO;
import com.sip.syshumres_utils.StringTrim;


public class ManagingCompanyMapper {
	
	@Autowired
    private ModelMapper modelMapper;
	
	public ManagingCompanyMapper() {// Noncompliant - method is empty
	}
	
	public ManagingCompanyDTO toDto(ManagingCompany entity) {
		ManagingCompanyDTO dto = new ManagingCompanyDTO();
		
		dto.setId(entity.getId());
		dto.setDescription(entity.getDescription());
		
		dto.setPhoneNumber(entity.getPhoneNumber());
	    dto.setExtPhoneNumber(entity.getExtPhoneNumber());
	    dto.setShortPhoneNumber(entity.getShortPhoneNumber());
	    dto.setEmail(entity.getEmail());
	    
		dto.setCompanyName(entity.getCompanyName());
		dto.setRfc(entity.getRfc());
		dto.setEmployerRegistration(entity.getEmployerRegistration());
		dto.setLegalRepresentative(entity.getLegalRepresentative());
		
		if (entity.getTypeHiring() != null) {
	        dto.setTypeHiring(modelMapper.map(entity.getTypeHiring(), EntitySelectDTO.class));
	    }
		ArrayList<EntitySelectDTO> officesDTO = new ArrayList<>();
		if (entity.getBranchOffices() != null) {
			entity.getBranchOffices().forEach(child -> 
				officesDTO.add(new EntitySelectDTO(child.getId(), child.getDescription()))
			);
		}
		officesDTO.sort(Comparator.comparing(EntitySelectDTO::getDescription));
		dto.setBranchOffices(officesDTO);
		if (entity.getAddress() != null) {
			dto.setAddress(this.modelMapper.map(entity.getAddress(), AddressDTO.class));
		}
		if (entity.getAddressFiscal() != null) {
			dto.setAddressFiscal(this.modelMapper.map(entity.getAddressFiscal(), AddressDTO.class));
		}
		dto.setEnabled(entity.isEnabled());
		
		return dto;
	}
	
	public ManagingCompany toSaveEntity(ManagingCompanyDTO entity) {
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
		if (entity.getTypeHiring() != null) {
	        e.setTypeHiring(this.modelMapper.map(entity.getTypeHiring(), TypeHiring.class));
	    }
		if (entity.getAddress() != null) {
			e.setAddress(this.modelMapper.map(entity.getAddress(), Address.class));
		}
		if (entity.getAddressFiscal() != null) {
			e.setAddressFiscal(this.modelMapper.map(entity.getAddressFiscal(), Address.class));
		}
		//e.setBranchOffices(entity.getBranchOffices())
		e.setEnabled(entity.isEnabled());
		
		return e;
	}
	
	public ManagingCompany toEditEntity(ManagingCompany e, ManagingCompanyDTO entity) {
		e.setDescription(StringTrim.trimAndRemoveDiacriticalMarks(entity.getDescription()));
		e.setCompanyName(StringTrim.trimAndRemoveDiacriticalMarks(entity.getCompanyName()));
		e.setPhoneNumber(StringTrim.trimAndRemoveDiacriticalMarks(entity.getPhoneNumber()));
		e.setExtPhoneNumber(StringTrim.trimAndRemoveDiacriticalMarks(entity.getExtPhoneNumber()));
		e.setShortPhoneNumber(StringTrim.trimAndRemoveDiacriticalMarks(entity.getShortPhoneNumber()));
		e.setEmail(StringTrim.trimAndRemoveDiacriticalMarks(entity.getEmail()));
		e.setRfc(StringTrim.trimAndRemoveDiacriticalMarks(entity.getRfc()));
		e.setEmployerRegistration(StringTrim.trimAndRemoveDiacriticalMarks(entity.getEmployerRegistration()));
		e.setLegalRepresentative(StringTrim.trimAndRemoveDiacriticalMarks(entity.getLegalRepresentative()));
		if (entity.getTypeHiring() != null) {
	        e.setTypeHiring(this.modelMapper.map(entity.getTypeHiring(), TypeHiring.class));
	    }
		if (entity.getAddress() != null) {
			e.setAddress(this.modelMapper.map(entity.getAddress(), Address.class));
		}
		if (entity.getAddressFiscal() != null) {
			e.setAddressFiscal(this.modelMapper.map(entity.getAddressFiscal(), Address.class));
		}
		//e.setBranchOffices(entity.getBranchOffices())
		e.setEnabled(entity.isEnabled());
		
		return e;
	}

}
