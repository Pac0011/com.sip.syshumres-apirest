package com.sip.syshumres_apirest.mappers;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

public class ListMapper {
	
	@Autowired
	private ModelMapper modelMapper;
	
	public ListMapper() {
	}
	
	public <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
	    return source
	      .stream()
	      .map(element -> modelMapper.map(element, targetClass))
	      .collect(Collectors.toList());
	}
	
	public <T> List<T> toList(Iterable<T> iterable) {
	    return StreamSupport.stream(iterable.spliterator(), false)
	                        .collect(Collectors.toList());
	}
	
}
