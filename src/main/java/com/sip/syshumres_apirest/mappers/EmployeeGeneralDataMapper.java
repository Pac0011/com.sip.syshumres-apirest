package com.sip.syshumres_apirest.mappers;

import com.sip.syshumres_entities.EmployeeGeneralData;
import com.sip.syshumres_utils.StringTrim;

public class EmployeeGeneralDataMapper {
	
	public EmployeeGeneralDataMapper() {
	}
	
	public EmployeeGeneralData toSaveEntity(EmployeeGeneralData entity) {
		EmployeeGeneralData e = new EmployeeGeneralData();
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
		
		e.setHaveCar(entity.isHaveCar());
		e.setHaveChildren(entity.isHaveChildren());
		e.setHaveDebts(entity.isHaveDebts());
		
		e.setHaveEconomicDependents(entity.isHaveEconomicDependents());
		e.setHaveHouse(entity.isHaveHouse());
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
		e.setPayRent(entity.isPayRent());
		
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
		
		e.setHaveCar(entity.isHaveCar());
		e.setHaveChildren(entity.isHaveChildren());
		e.setHaveDebts(entity.isHaveDebts());
		
		e.setHaveEconomicDependents(entity.isHaveEconomicDependents());
		e.setHaveHouse(entity.isHaveHouse());
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
		e.setPayRent(entity.isPayRent());
		
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
