package com.sip.syshumres_apirest.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sip.syshumres_apirest.controllers.common.CommonCatalogController;
import com.sip.syshumres_entities.OtherReasonQuitJob;
import com.sip.syshumres_services.OtherReasonQuitJobService;
import com.sip.syshumres_utils.StringTrim;


@RestController
@RequestMapping(OtherReasonQuitJobController.URLENDPOINT)
public class OtherReasonQuitJobController extends CommonCatalogController<OtherReasonQuitJob, 
	OtherReasonQuitJobService> {
	
	public static final String URLENDPOINT = "other-reason-quit-job";
	
	@GetMapping(PAGE)
	public ResponseEntity<Page<OtherReasonQuitJob>> list(Pageable pageable) {
		Page<OtherReasonQuitJob> entities = this.service.findByFilterSession(this.filter, pageable);

		return ResponseEntity.ok().body(entities);
	}
	
	/**
     * @param page          number of the page returned
     * @param size          number of entries in each page
     * @param sort  		Sort object
     * @return Page object with entitys after sorting
     */
	@GetMapping(PAGEORDER)
	public ResponseEntity<Page<OtherReasonQuitJob>> list(Pageable pageable, Sort sort) {
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
	public ResponseEntity<Page<OtherReasonQuitJob>> list(String text, Pageable pageable) {
		this.filter = StringTrim.trimAndRemoveDiacriticalMarks(text);
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
	public ResponseEntity<Page<OtherReasonQuitJob>> list(String text, Pageable pageable, Sort sort) {
		Pageable pageableOrder = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
		return this.list(text, pageableOrder);
	}

}
