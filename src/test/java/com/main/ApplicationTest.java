package com.main;

import static org.mockito.Mockito.when;

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
import com.main.strategydesignpattern.BluProviderImpl;
import com.main.strategydesignpattern.CommandMap;
import com.main.strategydesignpattern.InvalidProviderImpl;
import com.main.strategydesignpattern.PwcProviderImpl;

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
		ApplicationModel blueMixModel = (ApplicationModel)mapper.readValue("{ \"description\" : \"xx\", \"api_version\" : \"yy\" }",
				ApplicationModel.class);
		ApplicationModel pivotalModel = (ApplicationModel)mapper.readValue("{ \"description\" : \"xx1\", \"api_version\" : \"yy1\" }",
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
		applicationController.setCommandMap(getCommandMap());
		ApplicationModel response = applicationController.getDetailsByProvider("PWC");
			
		Assert.assertEquals(pivotalModel, response);
	}
	

	// Get provider details with BLU
	@Test
	public void getDetailsByBLUProvider() throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		ApplicationController applicationController = new ApplicationController();
		
		ApplicationModel blueMixModel = (ApplicationModel)mapper.readValue("{ \"description\" : \"xx1\", \"api_version\" : \"yy1\" }",
				ApplicationModel.class);
		
		getCommandMap();
		when(restTemplate.getForEntity("http://api.ng.bluemix.net/v2/info", ApplicationModel.class)).
        thenReturn(new ResponseEntity<ApplicationModel>(blueMixModel, HttpStatus.OK));
		
		applicationController.setApplicationService(applicationService);
		applicationController.setCommandMap(getCommandMap());
		ApplicationModel response = applicationController.getDetailsByProvider("BLU");
			
		Assert.assertEquals(blueMixModel, response);
	}


	// Get error details with invalid provider
	@Test
	public void getDetailsWithInvalidProvider() throws Exception{
		ApplicationController applicationController = new ApplicationController();
		applicationController.setApplicationService(applicationService);
		applicationController.setCommandMap(getCommandMap());
		ApplicationModel response = applicationController.getDetailsByProvider("BL");
			
		Assert.assertNotNull(response.getError());
	}
	

	// Test for internal server error
	@Test
	public void getDetailsInternalServerError() throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		ApplicationController applicationController = new ApplicationController();
		
		ApplicationModel blueMixModel = (ApplicationModel)mapper.readValue("{ \"description\" : \"xx1\", \"api_version\" : \"yy1\" }",
				ApplicationModel.class);
		
		when(restTemplate.getForEntity("http://api.ng.bluemix.net/v2/info", ApplicationModel.class)).
        thenReturn(new ResponseEntity<ApplicationModel>(blueMixModel, HttpStatus.INTERNAL_SERVER_ERROR));
        
		applicationController.setApplicationService(applicationService);
		applicationController.setCommandMap(getCommandMap());
		ApplicationModel response = applicationController.getDetailsByProvider("BLU");
			
		Assert.assertNull(response);
	}
	
	
	// Test for timeout the external API call.
	@Test(timeout = 2000)
	public void getDetailsTimeOutException() throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		ApplicationController applicationController = new ApplicationController();
		
		ApplicationModel blueMixModel = (ApplicationModel)mapper.readValue("{ \"description\" : \"xx1\", \"api_version\" : \"yy1\" }",
				ApplicationModel.class);
		
		
		//TimeUnit.SECONDS.sleep(2000);
		when(restTemplate.getForEntity("http://api.ng.bluemix.net/v2/info", ApplicationModel.class)).
        thenReturn(new ResponseEntity<ApplicationModel>(blueMixModel, HttpStatus.OK));
        
		applicationController.setApplicationService(applicationService);
		applicationController.setCommandMap(getCommandMap());
		ApplicationModel response = applicationController.getDetailsByProvider("BLU");
			
		Assert.assertEquals(blueMixModel, response);
	}
	
	private CommandMap getCommandMap(){
		CommandMap commandMap = new CommandMap();
		
		PwcProviderImpl pwcProviderImpl = new PwcProviderImpl();
		pwcProviderImpl.setApplicationService(applicationService);
		
		BluProviderImpl bluProviderImpl = new BluProviderImpl();
		bluProviderImpl.setApplicationService(applicationService);
		
		commandMap.put("PWC", pwcProviderImpl);
		commandMap.put("BLU", bluProviderImpl);
		commandMap.put("Invalid", new InvalidProviderImpl());
		
		return commandMap;
	}
}
