package com.sip.syshumres_apirest.mappers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.sip.syshumres_entities.ProspectProfile;
import com.sip.syshumres_entities.dtos.ProspectProfileDTO;
import com.sip.syshumres_entities.dtos.EntitySelectDTO;
import com.sip.syshumres_utils.StringTrim;


public class ProspectProfileMapper {
	
	@Autowired
    private ModelMapper modelMapper;
	
	public ProspectProfileMapper() {
	}

	public ProspectProfileDTO toDto(ProspectProfile entity) {
	    ProspectProfileDTO dto = new ProspectProfileDTO();
		dto.setId(entity.getId());
		dto.setFullName(entity.getFullName());
		dto.setFirstName(entity.getFirstName());
		dto.setLastName(entity.getLastName());
		dto.setLastNameSecond(entity.getLastNameSecond());
		dto.setCurp(entity.getCurp());
		dto.setRfc(entity.getRfc());
	    dto.setEcript(entity.getEcript());
	    
	    dto.setPhoneNumber(entity.getPhoneNumber());
	    dto.setCellNumber(entity.getCellNumber());
	    dto.setEmail(entity.getEmail());
	    dto.setNationality(entity.getNationality());
	    dto.setObservations(entity.getObservations());
	    dto.setDateBirth(entity.getDateBirth());
		
		if (entity.getEmployeePosition() != null) {
		    dto.setEmployeePosition(this.modelMapper.map(entity.getEmployeePosition(), EntitySelectDTO.class));
		}
		if (entity.getProspectStatus() != null) {
			dto.setProspectStatus(this.modelMapper.map(entity.getProspectStatus(), EntitySelectDTO.class));
		}
		if (entity.getBranchOffice() != null) {
			dto.setBranchOffice(this.modelMapper.map(entity.getBranchOffice(), EntitySelectDTO.class));
		}
		if (entity.getGender() != null) {
	        dto.setGender(this.modelMapper.map(entity.getGender(), EntitySelectDTO.class));
	    }
		 
		return dto;
	}
	
	public ProspectProfile toEntity(ProspectProfileDTO dto) {
		ProspectProfile entity = new ProspectProfile();
		entity.setId(dto.getId());
		entity.setFirstName(dto.getFirstName());
		entity.setLastName(dto.getLastName());
		entity.setLastNameSecond(dto.getLastNameSecond());
		entity.setCurp(dto.getCurp());
		entity.setRfc(dto.getRfc());
		entity.setEcript(dto.getEcript());
		
		entity.setPhoneNumber(dto.getPhoneNumber());
		entity.setCellNumber(dto.getCellNumber());
		entity.setEmail(dto.getEmail());
		entity.setNationality(dto.getNationality());
		entity.setObservations(dto.getObservations());
		entity.setDateBirth(dto.getDateBirth());
	    
	    return entity;
	}
	
	public ProspectProfile toSaveEntity(ProspectProfile entity) {
		ProspectProfile e = new ProspectProfile();
		e.setFirstName(StringTrim.trimAndRemoveDiacriticalMarks(entity.getFirstName()));
		e.setLastName(StringTrim.trimAndRemoveDiacriticalMarks(entity.getLastName()));
		e.setLastNameSecond(StringTrim.trimAndRemoveDiacriticalMarks(entity.getLastNameSecond()));
		e.setCurp(StringTrim.trimAndRemoveDiacriticalMarks(entity.getCurp()));
		e.setRfc(StringTrim.trimAndRemoveDiacriticalMarks(entity.getRfc()));
		e.setPhoneNumber(StringTrim.trimAndRemoveDiacriticalMarks(entity.getPhoneNumber()));
		e.setCellNumber(StringTrim.trimAndRemoveDiacriticalMarks(entity.getCellNumber()));
		e.setEmail(StringTrim.trimAndRemoveDiacriticalMarks(entity.getEmail()));
		e.setNationality(StringTrim.trimAndRemoveDiacriticalMarks(entity.getNationality()));
		e.setObservations(StringTrim.trimAndRemoveDiacriticalMarks(entity.getObservations()));
		e.setProspectStatus(entity.getProspectStatus());
		
		return e;
	}
	
	public ProspectProfile toEditEntity(ProspectProfile e, ProspectProfile entity) {
		e.setFirstName(StringTrim.trimAndRemoveDiacriticalMarks(entity.getFirstName()));
		e.setLastName(StringTrim.trimAndRemoveDiacriticalMarks(entity.getLastName()));
		e.setLastNameSecond(StringTrim.trimAndRemoveDiacriticalMarks(entity.getLastNameSecond()));
		e.setCurp(StringTrim.trimAndRemoveDiacriticalMarks(entity.getCurp()));
		e.setRfc(StringTrim.trimAndRemoveDiacriticalMarks(entity.getRfc()));
		e.setPhoneNumber(StringTrim.trimAndRemoveDiacriticalMarks(entity.getPhoneNumber()));
		e.setCellNumber(StringTrim.trimAndRemoveDiacriticalMarks(entity.getCellNumber()));
		e.setEmail(StringTrim.trimAndRemoveDiacriticalMarks(entity.getEmail()));
		e.setNationality(StringTrim.trimAndRemoveDiacriticalMarks(entity.getNationality()));
		e.setObservations(StringTrim.trimAndRemoveDiacriticalMarks(entity.getObservations()));
		e.setProspectStatus(entity.getProspectStatus());
		
		return e;
	}
}
