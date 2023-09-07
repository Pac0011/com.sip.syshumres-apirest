package com.sip.syshumres_apirest.mappers;

import com.sip.syshumres_entities.EmployeeClinicalData;
import com.sip.syshumres_utils.StringTrim;

public class EmployeeClinicalDataMapper {
	  	
	public EmployeeClinicalDataMapper() {
	}
	
	public EmployeeClinicalData toSaveEntity(EmployeeClinicalData entity) {
		EmployeeClinicalData e = new EmployeeClinicalData();
		
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
