package com.main.strategydesignpattern;

import org.springframework.stereotype.Component;

import com.main.model.ApplicationModel;
import com.main.model.Error;

@Component
public class InvalidProviderImpl implements ProviderStrategyIF {

	@Override
	public ApplicationModel executeOperation() {
		ApplicationModel applicationModel = new ApplicationModel();
		Error error = new Error();
		error.setError("Invalid Input");
		error.setDescription("Please provide valid Input");
		applicationModel.setError(error);
		return applicationModel;
	}

}
