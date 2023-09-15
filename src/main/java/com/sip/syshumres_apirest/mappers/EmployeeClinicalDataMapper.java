package com.sip.syshumres_apirest.mappers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.sip.syshumres_entities.BloodType;
import com.sip.syshumres_entities.CovidVaccine;
import com.sip.syshumres_entities.EmployeeClinicalData;
import com.sip.syshumres_entities.EmployeeTypeHealth;
import com.sip.syshumres_entities.VaccineDose;
import com.sip.syshumres_entities.dtos.EmployeeClinicalDataDTO;
import com.sip.syshumres_utils.StringTrim;

public class EmployeeClinicalDataMapper {
	
	@Autowired
	private ModelMapper modelMapper;
	  	
	public EmployeeClinicalDataMapper() {
	}
	
	public EmployeeClinicalData toSaveEntity(EmployeeClinicalDataDTO entity) {
		EmployeeClinicalData e = new EmployeeClinicalData();
		
		if (entity.getBloodType() != null) {
	        e.setBloodType(this.modelMapper.map(entity.getBloodType(), BloodType.class));
	    }
		e.setCovidCertificate(StringTrim.
				trimAndRemoveDiacriticalMarks(entity.getCovidCertificate()));		
		if (entity.getCovidVaccine() != null) {
	        e.setCovidVaccine(this.modelMapper.map(entity.getCovidVaccine(), CovidVaccine.class));
	    }
		e.setDeclaredDiseases(StringTrim.
				trimAndRemoveDiacriticalMarks(entity.getDeclaredDiseases()));		
		if (entity.getEmployeeTypeHealth() != null) {
	        e.setEmployeeTypeHealth(this.modelMapper.map(entity.getEmployeeTypeHealth(), EmployeeTypeHealth.class));
	    }
		e.setHeight(entity.getHeight());
		e.setVaccinated(entity.isVaccinated());
		e.setMedicalAssistance(entity.getMedicalAssistance());
		e.setObservations(StringTrim.
				trimAndRemoveDiacriticalMarks(entity.getObservations()));
		
		e.setPsychometry(StringTrim.
				trimAndRemoveDiacriticalMarks(entity.getPsychometry()));
		if (entity.getVaccineDose() != null) {
	        e.setVaccineDose(this.modelMapper.map(entity.getVaccineDose(), VaccineDose.class));
	    }
		e.setWeight(entity.getWeight());
		
		return e;
	}
	
	public EmployeeClinicalData toEditEntity(EmployeeClinicalData e, EmployeeClinicalData entity) {
		e.setBloodType(entity.getBloodType());
		e.setCovidCertificate(StringTrim.
				trimAndRemoveDiacriticalMarks(entity.getCovidCertificate()));
		e.setCovidVaccine(entity.getCovidVaccine());
		
		e.setDeclaredDiseases(StringTrim.
				trimAndRemoveDiacriticalMarks(entity.getDeclaredDiseases()));
		e.setEmployeeTypeHealth(entity.getEmployeeTypeHealth());
		e.setHeight(entity.getHeight());
		
		e.setVaccinated(entity.isVaccinated());
		e.setMedicalAssistance(entity.getMedicalAssistance());
		e.setObservations(StringTrim.
				trimAndRemoveDiacriticalMarks(entity.getObservations()));
		
		e.setPsychometry(StringTrim.
				trimAndRemoveDiacriticalMarks(entity.getPsychometry()));
		e.setVaccineDose(entity.getVaccineDose());
		e.setWeight(entity.getWeight());
		
		return e;
	}

}
