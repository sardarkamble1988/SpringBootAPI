package com.main.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.main.model.ApplicationModel;

@Service
public class ApplicationService {

	@Autowired
	private RestTemplate restTemplate;
	
	public void setRestTemplate(RestTemplate restTemplate){
		this.restTemplate = restTemplate;
	}

	public ApplicationModel getAllDetailsPivotal() {
		ResponseEntity<ApplicationModel> response =  restTemplate.getForEntity("http://api.run.pivotal.io/v2/info",
				ApplicationModel.class);
		return response.getStatusCode() == HttpStatus.OK ? response.getBody() : null;
	}

	public ApplicationModel getAllDetailsBlueMix() {
		ResponseEntity<ApplicationModel> response =  restTemplate.getForEntity("http://api.ng.bluemix.net/v2/info",
				ApplicationModel.class);
		return response.getStatusCode() == HttpStatus.OK ? response.getBody() : null;
	}
}
