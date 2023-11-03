package com.sip.syshumres_apirest.controllers;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sip.syshumres_apirest.controllers.common.CommonController;
import com.sip.syshumres_apirest.enums.StatusMessages;
import com.sip.syshumres_apirest.mappers.BranchOfficeMapper;
import com.sip.syshumres_apirest.mappers.ListMapper;
import com.sip.syshumres_apirest.mappers.ManagingCompanyMapper;
import com.sip.syshumres_entities.BranchOffice;
import com.sip.syshumres_entities.ManagingCompany;
import com.sip.syshumres_entities.dtos.BranchOfficeDTO;
import com.sip.syshumres_entities.dtos.ManagingCompanyDTO;
import com.sip.syshumres_entities.dtos.ResponseDTO;
import com.sip.syshumres_entities.utils.ErrorsBindingFields;
import com.sip.syshumres_exceptions.CreateRegisterException;
import com.sip.syshumres_exceptions.EntityIdNotFoundException;
import com.sip.syshumres_exceptions.IdsEntityNotEqualsException;
import com.sip.syshumres_exceptions.InvalidIdException;
import com.sip.syshumres_exceptions.UpdateRegisterException;
import com.sip.syshumres_services.ManagingCompanyService;
import com.sip.syshumres_utils.StringTrim;


@RestController
@RequestMapping(ManagingCompanyController.URLENDPOINT)
public class ManagingCompanyController extends CommonController {
	
	public static final String URLENDPOINT = "managing-companies";
	public static final String BRANCHOFFICES = "/branch-offices";
	public static final String ABRANCHOFFICES = "/assign-branch-offices";
	public static final String RBRANCHOFFICE = "/remove-branch-office";
	
	private static final String MSG_ID = "Id administradora ";
	private static final String MSG_NOT_FOUND = " no encontrado";
	
	private final ManagingCompanyService service;
	
	private final ManagingCompanyMapper customMapper;
	
	private final BranchOfficeMapper customMapper2;
	
	private final ListMapper listMapper;
		
	@Autowired
	public ManagingCompanyController(ManagingCompanyService service, 
			ManagingCompanyMapper customMapper,
			BranchOfficeMapper customMapper2,
			ListMapper listMapper) {
		this.service = service;
		this.customMapper = customMapper;
		this.customMapper2 = customMapper2;
		this.listMapper = listMapper;
		this.filter = "";
	}
	
	@GetMapping(ID + BRANCHOFFICES)
	public ResponseEntity<ManagingCompanyDTO> getBranchOffices(@PathVariable Long id) 
			throws EntityIdNotFoundException, InvalidIdException {
		if (id <= 0) {
			throw new InvalidIdException();
		}
		Optional<ManagingCompany> entity = service.findById(id);
		if(entity.isEmpty()) {
			throw new EntityIdNotFoundException(MSG_ID + id + MSG_NOT_FOUND);
		}
				
		return ResponseEntity.ok(customMapper.toDto(entity.get()));
	}

	@GetMapping(PAGE)
	public ResponseEntity<Page<ManagingCompanyDTO>> list(Pageable pageable) {
		Page<ManagingCompany> entities = this.service.findByFilterSession(this.filter, pageable);
		
		Page<ManagingCompanyDTO> entitiesPageDTO = entities.map(customMapper::toDto);

		return ResponseEntity.ok().body(entitiesPageDTO);
	}
	
	/**
     * @param page          number of the page returned
     * @param size          number of entries in each page
     * @param sort  		Sort object
     * @return Page object with entitys after sorting
     */
	@GetMapping(PAGEORDER)
	public ResponseEntity<Page<ManagingCompanyDTO>> list(Pageable pageable, Sort sort) {
		this.filter = "";
		return this.list(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort));
	}
	
	/**
     * @param text			Filter for description, url, detail
     * @param page          number of the page returned
     * @param size          number of entries in each page
     * @return Page object with entitys after filtering
     */
	@GetMapping(PAGEFILTER)
	public ResponseEntity<Page<ManagingCompanyDTO>> list(@RequestParam String q, Pageable pageable) {
		this.filter = StringTrim.urlDecodingAndTrim(q);
		return this.list(pageable);
	}
	
	/**
     * @param text			Filter for description, url, detail
     * @param page          number of the page returned
     * @param size          number of entries in each page
     * @param sort  		Sort object
     * @return Page object with entitys after filtering and sorting
     */
	@GetMapping(PAGEFILTERORDER)
	public ResponseEntity<Page<ManagingCompanyDTO>> list(@RequestParam String q, Pageable pageable, Sort sort) {
		Pageable pageableOrder = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
		return this.list(q, pageableOrder);
	}
	
	@PostMapping
	public ResponseEntity<ResponseDTO> create(@Valid @RequestBody ManagingCompanyDTO entity, BindingResult result) 
	throws CreateRegisterException {
		if (result.hasErrors()) {
			return ResponseEntity.badRequest()
					.body(ErrorsBindingFields.getErrors(result));
		}
		
		ManagingCompany e = service.save(customMapper.toSaveEntity(entity));
		if (e == null) {
			throw new CreateRegisterException();
		}
		ResponseDTO response = new ResponseDTO();
		response.addEntry(StatusMessages.MESSAGE_KEY.getMessage(), 
				StatusMessages.SUCCESS_CREATE.getMessage());
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(response);
	}
	
	@GetMapping(ID)
	public ResponseEntity<ManagingCompanyDTO> formEdit(@PathVariable Long id) 
			throws EntityIdNotFoundException, InvalidIdException {
		if (id <= 0) {
			throw new InvalidIdException();
		}
		Optional<ManagingCompany> entity = this.service.findById(id);
		if(entity.isEmpty()) {
			throw new EntityIdNotFoundException(MSG_ID + id + MSG_NOT_FOUND);
		}
		
		return ResponseEntity.ok(customMapper.toDto(entity.get()));
	}
	
	@PutMapping(ID)
	public ResponseEntity<ResponseDTO> edit(@Valid @RequestBody ManagingCompanyDTO entity, BindingResult result, @PathVariable Long id) 
			throws EntityIdNotFoundException, IdsEntityNotEqualsException, InvalidIdException
	, UpdateRegisterException {
		if (result.hasErrors()) {
			return ResponseEntity.badRequest()
					.body(ErrorsBindingFields.getErrors(result));
		}
		
		if (id <= 0) {
			throw new InvalidIdException();
		}
		if(!Objects.equals(id, entity.getId())){
			throw new IdsEntityNotEqualsException("Ids de la administradora no coinciden para actualización");
        }
		Optional<ManagingCompany> o = this.service.findById(id);
		if (!o.isPresent()) {
			throw new EntityIdNotFoundException(MSG_ID + id + MSG_NOT_FOUND);
		}
		
		if (this.service.save(customMapper.toEditEntity(o.get(), entity)) == null) {
			throw new UpdateRegisterException();
		}
		ResponseDTO response = new ResponseDTO();
		response.addEntry(StatusMessages.MESSAGE_KEY.getMessage(), 
				StatusMessages.SUCCESS_UPDATE.getMessage());
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(response);
	}
	
	@PatchMapping(ID + ABRANCHOFFICES)
	public ResponseEntity<ResponseDTO> assignBranchOffices(@RequestBody List<BranchOfficeDTO> branchOffices, @PathVariable Long id) 
			throws EntityIdNotFoundException, InvalidIdException, UpdateRegisterException {
		if (id <= 0) {
			throw new InvalidIdException();
		}
		Optional<ManagingCompany> o = this.service.findById(id);
		if (!o.isPresent()) {
			throw new EntityIdNotFoundException(MSG_ID + id + MSG_NOT_FOUND);
		}
		
		ManagingCompany e = this.service.assignBranchOffices(o.get(), listMapper.mapList(branchOffices, BranchOffice.class));
		if (e == null) {
			throw new UpdateRegisterException(StatusMessages.ERROR_ADD.getMessage());
		}
		ResponseDTO response = new ResponseDTO();
		response.addEntry(StatusMessages.MESSAGE_KEY.getMessage(), 
				StatusMessages.SUCCESS_ADD.getMessage());
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@PatchMapping(ID + RBRANCHOFFICE)
	public ResponseEntity<ResponseDTO> removeBranchOffice(@RequestBody BranchOfficeDTO branchOffice, @PathVariable Long id) 
			throws EntityIdNotFoundException, InvalidIdException, UpdateRegisterException {
		if (id <= 0) {
			throw new InvalidIdException();
		}
		Optional<ManagingCompany> o = this.service.findById(id);
		if (!o.isPresent()) {
			throw new EntityIdNotFoundException(MSG_ID + id + MSG_NOT_FOUND);
		}

		ManagingCompany e = this.service.removeBranchOffice(o.get(), customMapper2.toEntity(branchOffice));
		if (e == null) {
			throw new UpdateRegisterException(StatusMessages.ERROR_REMOVE.getMessage());
		}
		ResponseDTO response = new ResponseDTO();
		response.addEntry(StatusMessages.MESSAGE_KEY.getMessage(), 
				StatusMessages.SUCCESS_REMOVE.getMessage());
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

}
