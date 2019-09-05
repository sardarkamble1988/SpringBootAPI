package com.main.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.main.model.ApplicationModel;

@RestController
public class ApplicationController {
	
	@RequestMapping("/cf")
	public List<ApplicationModel> getAllDetails() {
		return null;
	}

}
