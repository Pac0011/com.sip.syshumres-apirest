package com.sip.syshumres_apirest.controllers;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sip.syshumres_apirest.controllers.common.CommonController;
import com.sip.syshumres_apirest.enums.StatusMessages;
import com.sip.syshumres_apirest.mappers.BranchOfficeMapper;
import com.sip.syshumres_entities.BranchOffice;
import com.sip.syshumres_entities.dtos.BranchOfficeDTO;
import com.sip.syshumres_entities.dtos.ResponseDTO;
import com.sip.syshumres_entities.dtos.common.EntitySelectDTO;
import com.sip.syshumres_exceptions.CreateRegisterException;
import com.sip.syshumres_exceptions.EntityIdNotFoundException;
import com.sip.syshumres_exceptions.FatherAssignException;
import com.sip.syshumres_exceptions.IdsEntityNotEqualsException;
import com.sip.syshumres_exceptions.InvalidIdException;
import com.sip.syshumres_exceptions.MalFormedHeaderException;
import com.sip.syshumres_exceptions.UpdateRegisterException;
import com.sip.syshumres_entities.utils.ErrorsBindingFields;
import com.sip.syshumres_services.BranchOfficeService;
import com.sip.syshumres_utils.StringTrim;

import org.springframework.beans.factory.annotation.Autowired;


@RestController
@RequestMapping(BranchOfficeController.URLENDPOINT)
public class BranchOfficeController extends CommonController {
	
	public static final String URLENDPOINT = "branch-offices";
	
	private static final String MSG_ID = "Id sucursal ";
	private static final String MSG_NOT_FOUND = " no encontrado";
	
	private final BranchOfficeService service;
	
	private final BranchOfficeMapper customMapper;

	@Autowired
	public BranchOfficeController(BranchOfficeService service, 
			BranchOfficeMapper customMapper) {
		this.service = service;
		this.customMapper = customMapper;
		this.filter = "";
	}
	
	@GetMapping(ACTIVE)
	public ResponseEntity<List<EntitySelectDTO>> listActive() {
		return ResponseEntity.ok().body(service.findByEnabledTrueOrderByDescription().stream()
				.map(customMapper::toSelectDto)
				.toList());
	}
	
	@GetMapping(PAGE)
	public ResponseEntity<Page<BranchOfficeDTO>> list(Pageable pageable) {
		Page<BranchOffice> entities = this.service.findByFilterSession(this.filter, pageable);
		
		Page<BranchOfficeDTO> entitiesPageDTO = entities.map(customMapper::toDto);

		return ResponseEntity.ok().body(entitiesPageDTO);
	}
	
	/**
     * @param page          number of the page returned
     * @param size          number of entries in each page
     * @param sort  		Sort object
     * @return Page object with entitys after sorting
     */
	@GetMapping(PAGEORDER)
	public ResponseEntity<Page<BranchOfficeDTO>> list(Pageable pageable, Sort sort) {
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
	public ResponseEntity<Page<BranchOfficeDTO>> list(@RequestParam String q, Pageable pageable) {
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
	public ResponseEntity<Page<BranchOfficeDTO>> list(@RequestParam String q, Pageable pageable, Sort sort) {
		Pageable pageableOrder = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
		return this.list(q, pageableOrder);
	}
	
	@PostMapping
	public ResponseEntity<ResponseDTO> create(@Valid @RequestBody BranchOfficeDTO entity, BindingResult result) 
			throws CreateRegisterException {
		if (result.hasErrors()) {
			return ResponseEntity.badRequest()
					.body(ErrorsBindingFields.getErrors(result));
		}
		
		BranchOffice e = service.save(customMapper.toCreateEntity(entity));
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
	public ResponseEntity<BranchOfficeDTO> formEdit(@PathVariable Long id) 
			throws EntityIdNotFoundException, InvalidIdException {
		if (id <= 0) {
			throw new InvalidIdException();
		}
		Optional<BranchOffice> entity = this.service.findById(id);
		if(entity.isEmpty()) {
			throw new EntityIdNotFoundException(MSG_ID + id + MSG_NOT_FOUND);
		}
		
		return ResponseEntity.ok(customMapper.toDto(entity.get()));
	}
	
	@PutMapping(ID)
	public ResponseEntity<ResponseDTO> edit(@Valid @RequestBody BranchOfficeDTO entity, BindingResult result, @PathVariable Long id) 
			throws EntityIdNotFoundException, IdsEntityNotEqualsException, FatherAssignException
			, InvalidIdException, UpdateRegisterException {
		if (result.hasErrors()) {
			return ResponseEntity.badRequest()
					.body(ErrorsBindingFields.getErrors(result));
		}
		
		if (id <= 0) {
			throw new InvalidIdException();
		}
		if (!Objects.equals(id, entity.getId())) {
			throw new IdsEntityNotEqualsException("Ids de sucursal no coinciden para actualización");
        }
		Optional<BranchOffice> o = this.service.findById(id);
		if (!o.isPresent()) {
			throw new EntityIdNotFoundException(MSG_ID + id + MSG_NOT_FOUND);
		}
		
		BranchOffice entityDb = o.get();
		if (entity.getFather() != null && entityDb.getId().equals(entity.getFather().getId())) {
			throw new FatherAssignException("El padre de la sucursal no puede ser ella misma");
		}
		
		if (this.service.save(customMapper.toEditEntity(entityDb, entity)) == null) {
			throw new UpdateRegisterException();
		}
		ResponseDTO response = new ResponseDTO();
		response.addEntry(StatusMessages.MESSAGE_KEY.getMessage(), 
				StatusMessages.SUCCESS_UPDATE.getMessage());
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(response);
	}
	
	@GetMapping(ERROR + ID)
	public BranchOfficeDTO errorTest(@RequestHeader String authorization, @PathVariable Long id) 
			throws EntityIdNotFoundException, MalFormedHeaderException, InvalidIdException {
		if(id <= 0) {
			throw new InvalidIdException();
		}
		if(authorization.equals("kk")) {
			throw new MalFormedHeaderException("token: " + authorization);
		}
		
		Optional<BranchOffice> entity = this.service.findById(id);
		if(entity.isEmpty()) {
			throw new EntityIdNotFoundException(MSG_ID + id + MSG_NOT_FOUND);
		}
		
		return customMapper.toDto(entity.get());
	}

}
