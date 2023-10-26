package com.sip.syshumres_apirest.mappers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.sip.syshumres_entities.EmployeeGeneralData;
import com.sip.syshumres_entities.ExpertType;
import com.sip.syshumres_entities.SchoolGrade;
import com.sip.syshumres_entities.SchoolGradeComplete;
import com.sip.syshumres_entities.DriverLicenseType;
import com.sip.syshumres_entities.DriverLicenseValidity;
import com.sip.syshumres_entities.dtos.EmployeeGeneralDataDTO;
import com.sip.syshumres_utils.StringTrim;

public class EmployeeGeneralDataMapper {
	
	@Autowired
	private ModelMapper modelMapper;
	
	public EmployeeGeneralDataMapper() {// Noncompliant - method is empty
	}
	
	public EmployeeGeneralData toSaveEntity(EmployeeGeneralDataDTO entity) {
		EmployeeGeneralData e = new EmployeeGeneralData();
		e.setAmountDebt(entity.getAmountDebt());
		e.setAmountRent(entity.getAmountRent());		
		if (entity.getBasicSecurity() != null) {
	        e.setBasicSecurity(this.modelMapper.map(entity.getBasicSecurity(), ExpertType.class));
	    }
		e.setBornIn(StringTrim.
				trimAndRemoveDiacriticalMarks(entity.getBornIn()));
		e.setDriverLicenseDate(entity.getDriverLicenseDate());
		e.setDriverLicenseNumber(StringTrim.
				trimAndRemoveDiacriticalMarks(entity.getDriverLicenseNumber()));
		if (entity.getDriverLicenseType() != null) {
	        e.setDriverLicenseType(this.modelMapper.map(entity.getDriverLicenseType(), DriverLicenseType.class));
	    }
		if (entity.getDriverLicenseValidity() != null) {
	        e.setDriverLicenseValidity(this.modelMapper.map(entity.getDriverLicenseValidity(), DriverLicenseValidity.class));
	    }
		e.setGeneralReferences(StringTrim.
				trimAndRemoveDiacriticalMarks(entity.getGeneralReferences()));
		
		e.setHaveCar(entity.getHaveCar());
		e.setHaveChildren(entity.getHaveChildren());
		e.setHaveDebts(entity.getHaveDebts());
		
		e.setHaveEconomicDependents(entity.getHaveEconomicDependents());
		e.setHaveHouse(entity.getHaveHouse());
		e.setHowManyChildren(entity.getHowManyChildren());		
		if (entity.getInductionBasicSystems() != null) {
	        e.setInductionBasicSystems(this.modelMapper.map(entity.getInductionBasicSystems(), 
	        		ExpertType.class));
	    }
		if (entity.getInductionRecord() != null) {
	        e.setInductionRecord(this.modelMapper.map(entity.getInductionRecord(), 
	        		ExpertType.class));
	    }
		e.setIne(StringTrim.
				trimAndRemoveDiacriticalMarks(entity.getIne()));
		
		e.setLanguagesSpeak(StringTrim.
				trimAndRemoveDiacriticalMarks(entity.getLanguagesSpeak()));
		e.setLiveWith(StringTrim.
				trimAndRemoveDiacriticalMarks(entity.getLiveWith()));
		if (entity.getManagementTonfaPr24() != null) {
	        e.setManagementTonfaPr24(this.modelMapper.map(entity.getManagementTonfaPr24(), 
	        		ExpertType.class));
	    }	
		e.setMilitaryCertificate(StringTrim.
				trimAndRemoveDiacriticalMarks(entity.getMilitaryCertificate()));
		e.setMonthlyExpenses(entity.getMonthlyExpenses());
		e.setNationality(StringTrim.
				trimAndRemoveDiacriticalMarks(entity.getNationality()));
		
		e.setObservations(StringTrim.
				trimAndRemoveDiacriticalMarks(entity.getObservations()));
		e.setParticularReferences(StringTrim.
				trimAndRemoveDiacriticalMarks(entity.getParticularReferences()));
		e.setParticularReferences1(StringTrim.
				trimAndRemoveDiacriticalMarks(entity.getParticularReferences1()));
		
		e.setParticularReferences2(StringTrim.
				trimAndRemoveDiacriticalMarks(entity.getParticularReferences2()));
		e.setParticularReferences3(StringTrim.
				trimAndRemoveDiacriticalMarks(entity.getParticularReferences3()));
		e.setPayRent(entity.getPayRent());
		
		e.setPhoneForMessages(StringTrim.
				trimAndRemoveDiacriticalMarks(entity.getPhoneForMessages()));
		e.setPhoneReferences1(StringTrim.
				trimAndRemoveDiacriticalMarks(entity.getPhoneReferences1()));
		e.setPhoneReferences2(StringTrim.
				trimAndRemoveDiacriticalMarks(entity.getPhoneReferences2()));
		
		e.setPhoneReferences3(StringTrim.
				trimAndRemoveDiacriticalMarks(entity.getPhoneReferences3()));		
		if (entity.getPreventiveReactiveManagement() != null) {
	        e.setPreventiveReactiveManagement(this.modelMapper.map(entity.getPreventiveReactiveManagement(), 
	        		ExpertType.class));
	    }
		e.setRelationshipReferences1(entity.getRelationshipReferences1());
		e.setRelationshipReferences2(entity.getRelationshipReferences2());
		e.setRelationshipReferences3(entity.getRelationshipReferences3());
		if (entity.getSchoolGrade() != null) {
	        e.setSchoolGrade(this.modelMapper.map(entity.getSchoolGrade(), 
	        		SchoolGrade.class));
	    }		
		if (entity.getSchoolGradeComplete() != null) {
	        e.setSchoolGradeComplete(this.modelMapper.map(entity.getSchoolGradeComplete(), 
	        		SchoolGradeComplete.class));
	    }
		e.setSkills(StringTrim.
				trimAndRemoveDiacriticalMarks(entity.getSkills()));
		
		return e;
	}
	
	public EmployeeGeneralData toEditEntity(EmployeeGeneralData e, EmployeeGeneralData entity) {
		e.setAmountDebt(entity.getAmountDebt());
		e.setAmountRent(entity.getAmountRent());
		e.setBasicSecurity(entity.getBasicSecurity());
		
		e.setBornIn(StringTrim.
				trimAndRemoveDiacriticalMarks(entity.getBornIn()));
		e.setDriverLicenseDate(entity.getDriverLicenseDate());
		e.setDriverLicenseNumber(StringTrim.
				trimAndRemoveDiacriticalMarks(entity.getDriverLicenseNumber()));
		
		e.setDriverLicenseType(entity.getDriverLicenseType());
		e.setDriverLicenseValidity(entity.getDriverLicenseValidity());
		e.setGeneralReferences(StringTrim.
				trimAndRemoveDiacriticalMarks(entity.getGeneralReferences()));
		
		e.setHaveCar(entity.getHaveCar());
		e.setHaveChildren(entity.getHaveChildren());
		e.setHaveDebts(entity.getHaveDebts());
		
		e.setHaveEconomicDependents(entity.getHaveEconomicDependents());
		e.setHaveHouse(entity.getHaveHouse());
		e.setHowManyChildren(entity.getHowManyChildren());
		
		e.setInductionBasicSystems(entity.getInductionBasicSystems());
		e.setInductionRecord(entity.getInductionRecord());
		e.setIne(StringTrim.
				trimAndRemoveDiacriticalMarks(entity.getIne()));
		
		e.setLanguagesSpeak(StringTrim.
				trimAndRemoveDiacriticalMarks(entity.getLanguagesSpeak()));
		e.setLiveWith(StringTrim.
				trimAndRemoveDiacriticalMarks(entity.getLiveWith()));
		e.setManagementTonfaPr24(entity.getManagementTonfaPr24());
		
		e.setMilitaryCertificate(StringTrim.
				trimAndRemoveDiacriticalMarks(entity.getMilitaryCertificate()));
		e.setMonthlyExpenses(entity.getMonthlyExpenses());
		e.setNationality(StringTrim.
				trimAndRemoveDiacriticalMarks(entity.getNationality()));
		
		e.setObservations(StringTrim.
				trimAndRemoveDiacriticalMarks(entity.getObservations()));
		e.setParticularReferences(StringTrim.
				trimAndRemoveDiacriticalMarks(entity.getParticularReferences()));
		e.setParticularReferences1(StringTrim.
				trimAndRemoveDiacriticalMarks(entity.getParticularReferences1()));
		
		e.setParticularReferences2(StringTrim.
				trimAndRemoveDiacriticalMarks(entity.getParticularReferences2()));
		e.setParticularReferences3(StringTrim.
				trimAndRemoveDiacriticalMarks(entity.getParticularReferences3()));
		e.setPayRent(entity.getPayRent());
		
		e.setPhoneForMessages(StringTrim.
				trimAndRemoveDiacriticalMarks(entity.getPhoneForMessages()));
		e.setPhoneReferences1(StringTrim.
				trimAndRemoveDiacriticalMarks(entity.getPhoneReferences1()));
		e.setPhoneReferences2(StringTrim.
				trimAndRemoveDiacriticalMarks(entity.getPhoneReferences2()));
		
		e.setPhoneReferences3(StringTrim.
				trimAndRemoveDiacriticalMarks(entity.getPhoneReferences3()));
		e.setPreventiveReactiveManagement(entity.getPreventiveReactiveManagement());
		e.setRelationshipReferences1(entity.getRelationshipReferences1());
		
		e.setRelationshipReferences2(entity.getRelationshipReferences2());
		e.setRelationshipReferences3(entity.getRelationshipReferences3());
		e.setSchoolGrade(entity.getSchoolGrade());
		
		e.setSchoolGradeComplete(entity.getSchoolGradeComplete());
		e.setSkills(StringTrim.
				trimAndRemoveDiacriticalMarks(entity.getSkills()));
		
		return e;
	}

}
