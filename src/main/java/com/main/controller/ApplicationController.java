package com.main.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.main.model.ApplicationModel;
import com.main.service.ApplicationService;
import com.main.strategydesignpattern.CommandMap;

@RestController
public class ApplicationController {
	
	@Autowired
	private ApplicationService applicationService;
	
	@Autowired
	private CommandMap commandMap;
	
	public void setApplicationService(ApplicationService applicationService){
		this.applicationService = applicationService;
	}
	
	public void setCommandMap(CommandMap commandMap){
		this.commandMap = commandMap;
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
		provider = (provider.equals("PWC") || provider.equals("BLU")) ? provider : "Invalid"; 
		return commandMap.get(provider).executeOperation();
	}
}
