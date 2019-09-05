package com.main.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.main.model.ApplicationModel;
import com.main.service.ApplicationService;

@RestController
public class ApplicationController {
	
	@Autowired
	private ApplicationService applicationService;
	
	public void setApplicationService(ApplicationService applicationService){
		this.applicationService = applicationService;
	}
	
	
	@RequestMapping("/cf")
	public List<ApplicationModel> getAllDetails() {
		List<ApplicationModel> list = new ArrayList<>();
		
		list.add(applicationService.getAllDetailsPivotal());
		list.add(applicationService.getAllDetailsBlueMix());
		
		return list;
	}

	@RequestMapping("/cf/{provider}")
	public ApplicationModel getDetailsByProvider(@PathVariable String provider) {
		return null;
	}
}
