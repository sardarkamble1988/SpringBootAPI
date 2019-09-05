package com.main;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.main.controller.ApplicationController;
import com.main.model.ApplicationModel;
import com.main.service.ApplicationService;

import static org.mockito.Mockito.when;

public class ApplicationTest {

	@Mock
	private RestTemplate restTemplate;
	
	@InjectMocks
	private ApplicationService applicationService;
	
	@Before
	public void setUp() throws Exception{
		MockitoAnnotations.initMocks(this);
	}

	// This test will assert pivotal and blueMix providers
	@Test
	public void getAllDetails() throws Exception{
		ObjectMapper  mapper = new ObjectMapper();
		
		ApplicationModel pivotalModel = (ApplicationModel)mapper.readValue("{ \"description\" : \"xx\", \"api_version\" : \"yy\" }",
				ApplicationModel.class);
		
		ApplicationModel blueMixModel = (ApplicationModel)mapper.readValue("{ \"description\" : \"xx1\", \"api_version\" : \"yy1\" }",
				ApplicationModel.class);
		
		when(restTemplate.getForEntity("http://api.run.pivotal.io/v2/info", ApplicationModel.class)).
        thenReturn(new ResponseEntity<ApplicationModel>(pivotalModel, HttpStatus.OK));
        
        when(restTemplate.getForEntity("http://api.ng.bluemix.net/v2/info", ApplicationModel.class)).
        thenReturn(new ResponseEntity<ApplicationModel>(blueMixModel, HttpStatus.OK));
		
		
		ApplicationController applicationController = new ApplicationController();
		applicationService.setRestTemplate(restTemplate);
		applicationController.setApplicationService(applicationService);
		
		
		List<ApplicationModel> response = applicationController.getAllDetails();
		
		Assert.assertEquals(Arrays.asList(pivotalModel, blueMixModel), response);
	}
	
	// Get provider details with PWC
	@Test
	public void getDetailsByPWCProvider() throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		ApplicationController applicationController = new ApplicationController();
		
		ApplicationModel pivotalModel = (ApplicationModel)mapper.readValue("{ \"description\" : \"xx1\", \"api_version\" : \"yy1\" }",
				ApplicationModel.class);
		
		when(restTemplate.getForEntity("http://api.run.pivotal.io/v2/info", ApplicationModel.class)).
        thenReturn(new ResponseEntity<ApplicationModel>(pivotalModel, HttpStatus.OK));
        
		
		applicationController.setApplicationService(applicationService);
		ApplicationModel response = applicationController.getDetailsByProvider("PWC");
		
		Assert.assertEquals(pivotalModel, response);
	}
	

}
