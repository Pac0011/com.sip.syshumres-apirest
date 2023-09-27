package com.sip.syshumres_apirest.mappers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.sip.syshumres_entities.EmployeePayroll;
import com.sip.syshumres_entities.DinningRoom;
import com.sip.syshumres_entities.EmployeeBank;
import com.sip.syshumres_entities.InvoicedResourceType;
import com.sip.syshumres_entities.FactorDiscountInfonavit;
import com.sip.syshumres_entities.PayrollType;
import com.sip.syshumres_entities.TypeHiring;
import com.sip.syshumres_entities.AddressState;
import com.sip.syshumres_entities.dtos.EmployeePayrollDTO;
import com.sip.syshumres_utils.StringTrim;

public class EmployeePayrollMapper {
	
	@Autowired
	private ModelMapper modelMapper;
	
	public EmployeePayrollMapper() {
	}
	
	public EmployeePayroll toSaveEntity(EmployeePayrollDTO entity) {
		EmployeePayroll e = new EmployeePayroll();
		e.setAmountAlimony(entity.getAmountAlimony());
		e.setAmountFactorDiscountFonacot(entity.getAmountFactorDiscountFonacot());
		e.setAmountFactorDiscountInfonavit(entity.getAmountFactorDiscountInfonavit());
		
		e.setAmountSgmm(entity.getAmountSgmm());
		e.setBankAccount(StringTrim.
				trimAndRemoveDiacriticalMarks(entity.getBankAccount()));
		e.setClabe(StringTrim.
				trimAndRemoveDiacriticalMarks(entity.getClabe()));
		
		e.setCreditNumberInfonavit(StringTrim.
				trimAndRemoveDiacriticalMarks(entity.getCreditNumberInfonavit()));		
		if (entity.getDinningRoom() != null) {
	        e.setDinningRoom(this.modelMapper.map(entity.getDinningRoom(), DinningRoom.class));
	    }
		
		//EmployeeAddressFiscal
		if (entity.getEmployeeAddressFiscal().getAddressState() != null) {
			e.getEmployeeAddressFiscal().setAddressState(this.modelMapper
					.map(entity.getEmployeeAddressFiscal().getAddressState(), 
							AddressState.class));
		}
		e.getEmployeeAddressFiscal().
			setCity(StringTrim.trimAndRemoveDiacriticalMarks(entity.
					getEmployeeAddressFiscal().getCity()));
		e.getEmployeeAddressFiscal().
			setColony(StringTrim.trimAndRemoveDiacriticalMarks(entity.
					getEmployeeAddressFiscal().getColony()));
		
		e.getEmployeeAddressFiscal().
			setMunicipality(StringTrim.trimAndRemoveDiacriticalMarks(entity.
					getEmployeeAddressFiscal().getMunicipality()));
		e.getEmployeeAddressFiscal().
			setNumber(StringTrim.trimAndRemoveDiacriticalMarks(entity.
					getEmployeeAddressFiscal().getNumber()));
		e.getEmployeeAddressFiscal().
			setNumberInterior(StringTrim.trimAndRemoveDiacriticalMarks(entity.
					getEmployeeAddressFiscal().getNumberInterior()));
		
		e.getEmployeeAddressFiscal().
			setStreet(StringTrim.trimAndRemoveDiacriticalMarks(entity.
					getEmployeeAddressFiscal().getStreet()));
		e.getEmployeeAddressFiscal().
			setZip(StringTrim.trimAndRemoveDiacriticalMarks(entity.
					getEmployeeAddressFiscal().getZip()));
		///////////
		if (entity.getEmployeeBank() != null) {
	        e.setEmployeeBank(this.modelMapper.map(entity.getEmployeeBank(), EmployeeBank.class));
	    }		
		if (entity.getFactorDiscountInfonavit() != null) {
	        e.setFactorDiscountInfonavit(this.modelMapper.map(entity.getFactorDiscountInfonavit(), 
	        		FactorDiscountInfonavit.class));
	    }
		e.setHaveAlimony(entity.getHaveAlimony());
		
		e.setHaveFonacotCredit(entity.getHaveFonacotCredit());
		e.setHaveInfonavitCredit(entity.getHaveInfonavitCredit());
		e.setHaveSgmm(entity.getHaveSgmm());		
		if (entity.getInvoicedResourceType() != null) {
	        e.setInvoicedResourceType(this.modelMapper.map(entity.getInvoicedResourceType(), 
	        		InvoicedResourceType.class));
	    }
		
		e.setNss(StringTrim.
				trimAndRemoveDiacriticalMarks(entity.getNss()));
		e.setNumberFonacotCredit(StringTrim.
				trimAndRemoveDiacriticalMarks(entity.getNumberFonacotCredit()));
		if (entity.getPayrollType() != null) {
	        e.setPayrollType(this.modelMapper.map(entity.getPayrollType(), 
	        		PayrollType.class));
	    }
		
		e.setRfc(entity.getRfc());
		e.setSalaryMonthly(entity.getSalaryMonthly());
		
		e.setSalaryMonthlyLetter(StringTrim.
				trimAndRemoveDiacriticalMarks(entity.getSalaryMonthlyLetter()));
		e.setSdb(entity.getSdb());
		e.setSdi(entity.getSdi());
		if (entity.getTypeHiring() != null) {
	        e.setTypeHiring(this.modelMapper.map(entity.getTypeHiring(), 
	        		TypeHiring.class));
	    }
		
		return e;
	}
	
	public EmployeePayroll toEditEntity(EmployeePayroll e, EmployeePayroll entity) {
		e.setAmountAlimony(entity.getAmountAlimony());
		e.setAmountFactorDiscountFonacot(entity.getAmountFactorDiscountFonacot());
		e.setAmountFactorDiscountInfonavit(entity.getAmountFactorDiscountInfonavit());
		
		e.setAmountSgmm(entity.getAmountSgmm());
		e.setBankAccount(StringTrim.
				trimAndRemoveDiacriticalMarks(entity.getBankAccount()));
		e.setClabe(StringTrim.
				trimAndRemoveDiacriticalMarks(entity.getClabe()));
		
		e.setCreditNumberInfonavit(StringTrim.
				trimAndRemoveDiacriticalMarks(entity.getCreditNumberInfonavit()));
		e.setDinningRoom(entity.getDinningRoom());
		
		//EmployeeAddressFiscal
		e.getEmployeeAddressFiscal().
			setAddressState(entity.getEmployeeAddressFiscal().getAddressState());
		e.getEmployeeAddressFiscal().
			setCity(StringTrim.trimAndRemoveDiacriticalMarks(entity.
					getEmployeeAddressFiscal().getCity()));
		e.getEmployeeAddressFiscal().
			setColony(StringTrim.trimAndRemoveDiacriticalMarks(entity.
					getEmployeeAddressFiscal().getColony()));
		
		e.getEmployeeAddressFiscal().
			setMunicipality(StringTrim.trimAndRemoveDiacriticalMarks(entity.
					getEmployeeAddressFiscal().getMunicipality()));
		e.getEmployeeAddressFiscal().
			setNumber(StringTrim.trimAndRemoveDiacriticalMarks(entity.
					getEmployeeAddressFiscal().getNumber()));
		e.getEmployeeAddressFiscal().
			setNumberInterior(StringTrim.trimAndRemoveDiacriticalMarks(entity.
					getEmployeeAddressFiscal().getNumberInterior()));
		
		e.getEmployeeAddressFiscal().
			setStreet(StringTrim.trimAndRemoveDiacriticalMarks(entity.
					getEmployeeAddressFiscal().getStreet()));
		e.getEmployeeAddressFiscal().
			setZip(StringTrim.trimAndRemoveDiacriticalMarks(entity.
					getEmployeeAddressFiscal().getZip()));
		///////////
		
		e.setEmployeeBank(entity.getEmployeeBank());
		e.setFactorDiscountInfonavit(entity.getFactorDiscountInfonavit());
		e.setHaveAlimony(entity.getHaveAlimony());
		
		e.setHaveFonacotCredit(entity.getHaveFonacotCredit());
		e.setHaveInfonavitCredit(entity.getHaveInfonavitCredit());
		e.setHaveSgmm(entity.getHaveSgmm());
		
		e.setInvoicedResourceType(entity.getInvoicedResourceType());
		e.setNss(StringTrim.
				trimAndRemoveDiacriticalMarks(entity.getNss()));
		e.setNumberFonacotCredit(StringTrim.
				trimAndRemoveDiacriticalMarks(entity.getNumberFonacotCredit()));
		
		e.setPayrollType(entity.getPayrollType());
		e.setRfc(entity.getRfc());
		e.setSalaryMonthly(entity.getSalaryMonthly());
		
		e.setSalaryMonthlyLetter(StringTrim.
				trimAndRemoveDiacriticalMarks(entity.getSalaryMonthlyLetter()));
		e.setSdb(entity.getSdb());
		e.setSdi(entity.getSdi());
		
		e.setTypeHiring(entity.getTypeHiring());
		
		return e;
	}

}
