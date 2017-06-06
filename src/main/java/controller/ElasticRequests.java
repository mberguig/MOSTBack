package controller;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import model.Batch;
import model.Step;
import model.TreatmentUnit;

public class ElasticRequests {

	private String cluster = "MOST";
	private static Settings settings  = Settings.builder().put("cluster.name", "MOST").build();

	// Initialisation de la connexion
	public static TransportClient startES(Settings settings) throws UnknownHostException {
		TransportClient client = new PreBuiltTransportClient(settings)
				.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
		return client;
	}


	//Récupère toutes les étapes
	public static HashMap<String, Step> getAllSteps() throws JsonProcessingException, IOException{
		HashMap<String, Step> stepsList = new HashMap<String, Step>();
		ObjectMapper mapper =  new ObjectMapper();
		Step step = new Step();

		Client client = startES(settings);

		//Effectue la requête vers ElasticSearch
		SearchResponse response = client.prepareSearch("steps")
				.setTypes("stepExecution")
				.setScroll(new TimeValue(60000))
				.setQuery(QueryBuilders.matchAllQuery())
				.setSize(100).get();

		//Récupération de tous les hits
		do {
			for(SearchHit hit : response.getHits().getHits()) {
				System.out.println("hit : " + hit);
				step = (mapper.reader().forType(Step.class).readValue(hit.getSourceAsString()));
				stepsList.put(step.stepExecutionId, step);
			}
			response = client.prepareSearchScroll(response.getScrollId()).setScroll(new TimeValue(60000)).execute().actionGet();
		}
		while(response.getHits().getHits().length != 0); //stops when zero hits

		client.close();
		return stepsList;
	}


	public static HashMap<String, Step> getStartedSteps() throws JsonProcessingException, IOException{
		HashMap<String, Step> stepsList = new HashMap<String, Step>();
		ObjectMapper mapper =  new ObjectMapper();
		Step step = new Step();

		Client client = startES(settings);

		//Effectue la requête vers ElasticSearch
		SearchResponse response = client.prepareSearch("steps")
				.setTypes("stepExecution")
				.setScroll(new TimeValue(60000))
				.setQuery(QueryBuilders.matchQuery("status", "STARTED"))
				.setSize(100).get();

		//Récupération de tous les hits
		do {
			for(SearchHit hit : response.getHits().getHits()) {
				System.out.println("hit : " + hit);
				step = (mapper.reader().forType(Step.class).readValue(hit.getSourceAsString()));
				stepsList.put(step.stepExecutionId, step);
			}
			response = client.prepareSearchScroll(response.getScrollId()).setScroll(new TimeValue(60000)).execute().actionGet();
		}
		while(response.getHits().getHits().length != 0); //stops when zero hits

		client.close();
		return stepsList;
	}

	
	
	public static HashMap<String, Step> getFinishedSteps() throws JsonProcessingException, IOException{
		HashMap<String, Step> stepsList = new HashMap<String, Step>();
		ObjectMapper mapper =  new ObjectMapper();
		Step step = new Step();

		Client client = startES(settings);

		//Effectue la requête vers ElasticSearch
		SearchResponse response = client.prepareSearch("steps")
				.setTypes("stepExecution")
				.setScroll(new TimeValue(60000))
				.setQuery(QueryBuilders.matchQuery("status", "OK OR KOF OR KOT"))
				.setSize(100).get();

		//Récupération de tous les hits
		do {
			for(SearchHit hit : response.getHits().getHits()) {
				System.out.println("hit : " + hit);
				step = (mapper.reader().forType(Step.class).readValue(hit.getSourceAsString()));
				stepsList.put(step.stepExecutionId, step);
			}
			response = client.prepareSearchScroll(response.getScrollId()).setScroll(new TimeValue(60000)).execute().actionGet();
		}
		while(response.getHits().getHits().length != 0); //stops when zero hits

		client.close();
		return stepsList;
	}

	
	
	//récupère tous les batchs
	public static HashMap<String, TreatmentUnit> getAllBatchs() throws JsonProcessingException, IOException{
		HashMap<String, TreatmentUnit> treatUnitList = new HashMap<String, TreatmentUnit>();
		ObjectMapper mapper =  new ObjectMapper();
		TreatmentUnit treatUnit = new TreatmentUnit();

		Client client = startES(settings);

		//Effectue la requête vers ElasticSearch
		SearchResponse response = client.prepareSearch("unit")
				.setTypes("unitExecution")
				.setScroll(new TimeValue(60000))
				.setQuery(QueryBuilders.matchAllQuery())
				.setSize(100).get();

		//Récupération de tous les hits
		do {
			for(SearchHit hit : response.getHits().getHits()) {
				System.out.println("hit : " + hit);
				treatUnit = (mapper.reader().forType(TreatmentUnit.class).readValue(hit.getSourceAsString()));
				treatUnitList.put(treatUnit.getUnitId(), treatUnit);
			}
			response = client.prepareSearchScroll(response.getScrollId()).setScroll(new TimeValue(60000)).execute().actionGet();
		}
		while(response.getHits().getHits().length != 0); //stops when zero hits

		client.close();
		return treatUnitList;
	}
	
	

	//récupère tous les batchs
	public static HashMap<String, TreatmentUnit> getStartedBatchs() throws JsonProcessingException, IOException{
		HashMap<String, TreatmentUnit> treatUnitList = new HashMap<String, TreatmentUnit>();
		ObjectMapper mapper =  new ObjectMapper();
		TreatmentUnit treatUnit = new TreatmentUnit();

		Client client = startES(settings);

		//Effectue la requête vers ElasticSearch
		SearchResponse response = client.prepareSearch("unit")
				.setTypes("unitExecution")
				.setScroll(new TimeValue(60000))
				.setQuery(QueryBuilders.matchQuery("status", "STARTED"))
				.setSize(100).get();

		//Récupération de tous les hits
		do {
			for(SearchHit hit : response.getHits().getHits()) {
				System.out.println("hit : " + hit.toString());
				treatUnit = (mapper.reader().forType(TreatmentUnit.class).readValue(hit.getSourceAsString()));
				treatUnitList.put(treatUnit.getUnitId(), treatUnit);
			}
			response = client.prepareSearchScroll(response.getScrollId()).setScroll(new TimeValue(60000)).execute().actionGet();
		}
		while(response.getHits().getHits().length != 0); //stops when zero hits

		client.close();
		return treatUnitList;
	}

	
	
	//récupère tous les batchs
	public static HashMap<String, TreatmentUnit> getFinishedBatchs() throws JsonProcessingException, IOException{
		HashMap<String, TreatmentUnit> treatUnitList = new HashMap<String, TreatmentUnit>();
		ObjectMapper mapper =  new ObjectMapper();
		TreatmentUnit treatUnit = new TreatmentUnit();

		Client client = startES(settings);

		//Effectue la requête vers ElasticSearch
		SearchResponse response = client.prepareSearch("unit")
				.setTypes("unitExecution")
				.setScroll(new TimeValue(60000))
				.setQuery(QueryBuilders.matchQuery("status", "OK OR KOF OR KOT"))
				.setSize(100).get();

		//Récupération de tous les hits
		do {
			for(SearchHit hit : response.getHits().getHits()) {
				System.out.println("hit : " + hit.toString());
				treatUnit = (mapper.reader().forType(TreatmentUnit.class).readValue(hit.getSourceAsString()));
				treatUnitList.put(treatUnit.getUnitId(), treatUnit);
			}
			response = client.prepareSearchScroll(response.getScrollId()).setScroll(new TimeValue(60000)).execute().actionGet();
		}
		while(response.getHits().getHits().length != 0); //stops when zero hits

		client.close();
		return treatUnitList;
	}



}
