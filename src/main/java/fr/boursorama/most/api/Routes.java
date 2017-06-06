package fr.boursorama.most.api;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.elasticsearch.search.SearchHit;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import controller.ElasticRequests;
import controller.GetSteps;
import controller.GetTreatmentUnits;
import model.Batch;
import model.Step;
import model.TreatmentUnit;

@RestController
@RequestMapping("/api/")
public class Routes {

	@RequestMapping(value = "/step", method = RequestMethod.GET, headers = "Accept=application/json")
	public List<Step> getStep() throws IOException{
		List<Step> steps = new ArrayList<Step>();
		steps = GetSteps.Request();
		return steps;
	}
	
	@RequestMapping(value = "/steps", method = RequestMethod.GET, headers = "Accept=application/json")
	public HashMap<String, Step> getSteps() throws IOException{
		List<Step> stepsList = GetSteps.Request();
		HashMap<String, Step> steps = GetSteps.groupByStepName(stepsList);
		
		return steps;
	}
	
	@RequestMapping(value = "/batchs", method = RequestMethod.GET, headers = "Accept=application/json")
	public HashMap<String, Batch> getBatch() throws IOException{
		HashMap<String, Batch> batchs = new HashMap<String, Batch>();

		batchs = GetTreatmentUnits.getTreatUnits();	
		
		return batchs;
	}
	
	@RequestMapping(value = "/stepList", method = RequestMethod.GET, headers = "Accept=application/json")
	public HashMap<String, Step> getStepList() throws IOException{
		HashMap<String, Step> stepList = new HashMap<String, Step>();

		stepList = ElasticRequests.getStartedSteps();	
		
		return stepList;
	}
	
	
	
	/*
	@RequestMapping(value = "/batch", method = RequestMethod.GET, headers = "Accept=application/json")
	public HashMap<String, TreatmentUnit> getBatch() throws IOException{
		HashMap<String, TreatmentUnit> batchs = new HashMap<String, TreatmentUnit>();
		batchs = getTreatmentUnits.getTreatUnits();	
		
		return batchs;
	}*/
	
	
}
