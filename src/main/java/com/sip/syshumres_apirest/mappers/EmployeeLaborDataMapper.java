package com.sip.syshumres_apirest.mappers;

import com.sip.syshumres_entities.EmployeeLaborData;
import com.sip.syshumres_utils.StringTrim;

public class EmployeeLaborDataMapper {
	
	public EmployeeLaborDataMapper() {
	}
	
	public EmployeeLaborData toSaveEntity(EmployeeLaborData entity) {
		EmployeeLaborData e = new EmployeeLaborData();
		e.setActivitiesLastJob(StringTrim.
				trimAndRemoveDiacriticalMarks(entity.getActivitiesLastJob()));
		e.setCompanyLastJob(StringTrim.
				trimAndRemoveDiacriticalMarks(entity.getCompanyLastJob()));
		e.setCompanyPenultimate2Job(StringTrim.
				trimAndRemoveDiacriticalMarks(entity.getCompanyPenultimate2Job()));
		
		e.setCompanyPenultimateJob(StringTrim.
				trimAndRemoveDiacriticalMarks(entity.getCompanyPenultimateJob()));
		e.setContactLastJob(StringTrim.
				trimAndRemoveDiacriticalMarks(entity.getContactLastJob()));
		e.setContactPenultimate2Job(StringTrim.
				trimAndRemoveDiacriticalMarks(entity.getContactPenultimate2Job()));
		
		e.setContactPenultimateJob(StringTrim.
				trimAndRemoveDiacriticalMarks(entity.getContactPenultimateJob()));
		e.setDetailExperienceSecurity(StringTrim.
				trimAndRemoveDiacriticalMarks(entity.getDetailExperienceSecurity()));
		e.setEndDateLastJob(entity.getEndDateLastJob());
		
		e.setEndDatePenultimate2Job(entity.getEndDatePenultimate2Job());
		e.setEndDatePenultimateJob(entity.getEndDatePenultimateJob());
		e.setHaveExperienceSecurity(entity.isHaveExperienceSecurity());
		
		e.setPhoneLastJob(entity.getPhoneLastJob());
		e.setPhonePenultimate2Job(entity.getPhonePenultimate2Job());
		e.setPhonePenultimateJob(entity.getPhoneLastJob());
		
		e.setReasonQuitJobLastJob(entity.getReasonQuitJobLastJob());
		e.setReasonQuitJobPenultimate2Job(entity.getReasonQuitJobPenultimate2Job());
		e.setReasonQuitJobPenultimateJob(entity.getReasonQuitJobPenultimateJob());
		
		e.setSalaryLastJob(entity.getSalaryLastJob());
		e.setSalaryPenultimate2Job(entity.getSalaryPenultimate2Job());
		e.setSalaryPenultimateJob(entity.getSalaryPenultimateJob());
		
		e.setStartDateLastJob(entity.getStartDateLastJob());
		e.setStartDatePenultimate2Job(entity.getStartDatePenultimate2Job());
		e.setStartDatePenultimateJob(entity.getStartDatePenultimateJob());
		
		e.setToolsUse(StringTrim.
				trimAndRemoveDiacriticalMarks(entity.getToolsUse()));
		
		return e;
	}
	
	public EmployeeLaborData toEditEntity(EmployeeLaborData e, EmployeeLaborData entity) {
		e.setActivitiesLastJob(StringTrim.
				trimAndRemoveDiacriticalMarks(entity.getActivitiesLastJob()));
		e.setCompanyLastJob(StringTrim.
				trimAndRemoveDiacriticalMarks(entity.getCompanyLastJob()));
		e.setCompanyPenultimate2Job(StringTrim.
				trimAndRemoveDiacriticalMarks(entity.getCompanyPenultimate2Job()));
		
		e.setCompanyPenultimateJob(StringTrim.
				trimAndRemoveDiacriticalMarks(entity.getCompanyPenultimateJob()));
		e.setContactLastJob(StringTrim.
				trimAndRemoveDiacriticalMarks(entity.getContactLastJob()));
		e.setContactPenultimate2Job(StringTrim.
				trimAndRemoveDiacriticalMarks(entity.getContactPenultimate2Job()));
		
		e.setContactPenultimateJob(StringTrim.
				trimAndRemoveDiacriticalMarks(entity.getContactPenultimateJob()));
		e.setDetailExperienceSecurity(StringTrim.
				trimAndRemoveDiacriticalMarks(entity.getDetailExperienceSecurity()));
		e.setEndDateLastJob(entity.getEndDateLastJob());
		
		e.setEndDatePenultimate2Job(entity.getEndDatePenultimate2Job());
		e.setEndDatePenultimateJob(entity.getEndDatePenultimateJob());
		e.setHaveExperienceSecurity(entity.isHaveExperienceSecurity());
		
		e.setPhoneLastJob(entity.getPhoneLastJob());
		e.setPhonePenultimate2Job(entity.getPhonePenultimate2Job());
		e.setPhonePenultimateJob(entity.getPhoneLastJob());
		
		e.setReasonQuitJobLastJob(entity.getReasonQuitJobLastJob());
		e.setReasonQuitJobPenultimate2Job(entity.getReasonQuitJobPenultimate2Job());
		e.setReasonQuitJobPenultimateJob(entity.getReasonQuitJobPenultimateJob());
		
		e.setSalaryLastJob(entity.getSalaryLastJob());
		e.setSalaryPenultimate2Job(entity.getSalaryPenultimate2Job());
		e.setSalaryPenultimateJob(entity.getSalaryPenultimateJob());
		
		e.setStartDateLastJob(entity.getStartDateLastJob());
		e.setStartDatePenultimate2Job(entity.getStartDatePenultimate2Job());
		e.setStartDatePenultimateJob(entity.getStartDatePenultimateJob());
		
		e.setToolsUse(StringTrim.
				trimAndRemoveDiacriticalMarks(entity.getToolsUse()));
		
		return e;
	}

}
