package com.main.strategydesignpattern;

import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;


@Component
@DependsOn({"bluProviderImpl","pwcProviderImpl", "invalidProviderImpl"})
public class CommandMap extends HashMap<String, ProviderStrategyIF>{
	
	private static final long serialVersionUID = 1L;

	@Autowired
	private PwcProviderImpl pwcProviderImpl;
	
	@Autowired
	private BluProviderImpl  bluProviderImpl;
	
	@Autowired
	private InvalidProviderImpl invalidProviderImpl;
	
	@PostConstruct
	public void populateObj(){
		this.put("PWC", pwcProviderImpl);
		this.put("BLU", bluProviderImpl);
		this.put("Invalid", invalidProviderImpl);
	}
}
