package com.main;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.main.controller.ApplicationController;
import com.main.model.ApplicationModel;

public class ApplicationTest {


	// This test will assert pivotal and blueMix providers
	@Test
	public void getAllDetails() throws Exception{
		ObjectMapper  mapper = new ObjectMapper();
		
		ApplicationModel pivotalModel = (ApplicationModel)mapper.readValue("{ \"description\" : \"xx\", \"api_version\" : \"yy\" }",
				ApplicationModel.class);
		
		ApplicationModel blueMixModel = (ApplicationModel)mapper.readValue("{ \"description\" : \"xx1\", \"api_version\" : \"yy1\" }",
				ApplicationModel.class);
		
		
		ApplicationController applicationController = new ApplicationController();
		List<ApplicationModel> response = applicationController.getAllDetails();
		
		Assert.assertEquals(Arrays.asList(pivotalModel, blueMixModel), response);
	}
	

}
