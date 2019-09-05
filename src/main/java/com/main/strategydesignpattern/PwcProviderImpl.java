package com.main.strategydesignpattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.main.model.ApplicationModel;
import com.main.service.ApplicationService;

@Component
public class PwcProviderImpl implements ProviderStrategyIF{

	@Autowired
	private ApplicationService applicationService; 
	
	public void setApplicationService(ApplicationService applicationService) {
		this.applicationService = applicationService;
	}
	
	@Override
	public ApplicationModel executeOperation() {
		return applicationService.getAllDetailsPivotal();
	}

}
