package controller;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;


import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.range.Range;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import model.Batch;
import model.Step;
import model.TreatmentUnit;

public class ElasticRequests {

	private static String cluster = "MOST";
	private static Settings settings  = Settings.builder().put("cluster.name", cluster).build();

	// Initialisation de la connexion
	public static TransportClient startES(Settings settings) throws UnknownHostException {
		TransportClient client = new PreBuiltTransportClient(settings)
				.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
		return client;
	}
	
	
	// --------------------------------
	//---------Récupération du nombre de resultats
	//--------------------------------- 
	
	//nb total
	public static long getNbAllResults(String date, Boolean isCTI) throws JsonProcessingException, IOException{
		String dateFrom = date + "T00:00:00Z";
		String dateTo = date + "T23:59:59Z";
		Client client = startES(settings);
		QueryBuilder filter = QueryBuilders.rangeQuery("startTime").from(dateFrom).to(dateTo);

		BoolQueryBuilder query = QueryBuilders.boolQuery()
				.filter(QueryBuilders.matchQuery("status", "STARTED"));	
		if(isCTI == false){
			query.mustNot(QueryBuilders.matchQuery("treatmentName", "CTI"));
		}
		//Effectue la requête vers ElasticSearch
		SearchResponse response = client.prepareSearch("most")
				.setTypes("treatments")
				.setQuery(query)	
				.setPostFilter(filter)
				.execute().actionGet();
		long nb= response.getHits().getTotalHits();
		client.close();
		return nb;				
	}
	
	//nb status
		public static long getNbResultsByStatus(String date, String status, Boolean isCTI) throws JsonProcessingException, IOException{
			String dateFrom = date + "T00:00:00Z";
			String dateTo = date + "T23:59:59Z";
			Client client = startES(settings);
			QueryBuilder filter = QueryBuilders.rangeQuery("startTime").from(dateFrom).to(dateTo);			
			BoolQueryBuilder query = QueryBuilders.boolQuery()
					.filter(QueryBuilders.matchQuery("status", status));	
			if(isCTI == false){
				query.mustNot(QueryBuilders.matchQuery("treatmentName", "CTI"));
			}
			//Effectue la requête vers ElasticSearch
			SearchResponse response = client.prepareSearch("most")
					.setTypes("treatments")
					.setQuery(query)	
					.setPostFilter(filter)
					.execute().actionGet();
			long nb= response.getHits().getTotalHits();
			client.close();
			return nb;				
		}
	
	
		// --------------------------------
		//---------Récupération des treatments
		//--------------------------------- 
			//USED
		//récupère tous les ids de batchs
		public static ArrayList<String> getTreatmentsIds(String date, Integer page, boolean isCTI) throws JsonProcessingException, IOException{
			ArrayList<String> treatmentsIds = new ArrayList<String>();
			page = page *10;
			ObjectMapper mapper =  new ObjectMapper();
			Batch treatBatch = new Batch();
			String dateFrom = date + "T00:00:00Z";
			String dateTo = date + "T23:59:59Z";

			Client client = startES(settings);
			QueryBuilder filter = QueryBuilders.rangeQuery("startTime").from(dateFrom).to(dateTo);			
			BoolQueryBuilder query = QueryBuilders.boolQuery()
					.filter(QueryBuilders.matchQuery("status", "STARTED"));	
			if(isCTI == false){
				query.mustNot(QueryBuilders.matchQuery("treatmentName", "CTI"));
			}
			//Effectue la requête vers ElasticSearch
			SearchResponse response = client.prepareSearch("most")
					.setTypes("treatments")
					.setQuery(query)
					.setPostFilter(filter)
					.addSort("startTime", SortOrder.ASC) //Tri
					.setFrom(page)
					.setSize(10).get();

			//Récupération de tous les hits
			
				for(SearchHit hit : response.getHits().getHits()) {
					treatBatch = (mapper.reader().forType(Batch.class).readValue(hit.getSourceAsString()));
					treatmentsIds.add(treatBatch.getTreatmentId());
				}
				//response = client.prepareSearchScroll(response.getScrollId()).setScroll(new TimeValue(60000)).execute().actionGet();
			//}
			 //stops when zero hits
			client.close();
			return treatmentsIds;		
		}
		
		public static HashMap<String, Batch> getTreatments(String date, Integer page, boolean isCTI, String status) 
				throws JsonProcessingException, IOException{
			HashMap<String, Batch> treatments = new HashMap<String, Batch>();
			page = page *10;
			ObjectMapper mapper =  new ObjectMapper();
			Batch treatBatch = new Batch();
			String dateFrom = date + "T00:00:00Z";
			String dateTo = date + "T23:59:59Z";

			Client client = startES(settings);
			QueryBuilder filter = QueryBuilders.rangeQuery("startTime").from(dateFrom).to(dateTo);			
			BoolQueryBuilder query = QueryBuilders.boolQuery()
					.filter(QueryBuilders.matchQuery("status", status));	
			if(isCTI == false){
				query.mustNot(QueryBuilders.matchQuery("treatmentName", "CTI"));
			}
			//Effectue la requête vers ElasticSearch
			SearchResponse response = client.prepareSearch("most")
					.setTypes("treatments")
					.setQuery(query)
					.setPostFilter(filter)
					.addSort("startTime", SortOrder.ASC) //Tri
					.setFrom(page)
					.setSize(10).get();
			//récupère les résultats
				for(SearchHit hit : response.getHits().getHits()) {
					treatBatch = (mapper.reader().forType(Batch.class).readValue(hit.getSourceAsString()));
					treatments.put(treatBatch.getTreatmentId(), treatBatch);
				}
			client.close();
			return treatments;		
		}

		//récupère tous les ids de batchs par statut
		public static HashMap<String, Batch> getTreatmentsIdsStatus(String date, String status, Integer page, boolean isCTI) throws JsonProcessingException, IOException{
			HashMap<String, Batch> treatmentsIds = new HashMap<String, Batch>();
			page = page *10;
			ObjectMapper mapper =  new ObjectMapper();
			Batch treatBatch = new Batch();
			String dateFrom = date + "T00:00:00Z";
			String dateTo = date + "T23:59:59Z";

			Client client = startES(settings);
			QueryBuilder filter = QueryBuilders.rangeQuery("startTime").from(dateFrom).to(dateTo);			
			BoolQueryBuilder query = QueryBuilders.boolQuery()
					.filter(QueryBuilders.matchQuery("status", status));	
			if(isCTI == false){
				query.mustNot(QueryBuilders.matchQuery("treatmentName", "CTI"));
			}
			//Effectue la requête vers ElasticSearch
			SearchResponse response = client.prepareSearch("most")
					.setTypes("treatments")
					.setQuery(query)
					.setPostFilter(filter)
					.addSort("startTime", SortOrder.ASC) //Tri
					.setFrom(page)
					.setSize(10).get();

			//Récupération de tous les hits
			
				for(SearchHit hit : response.getHits().getHits()) {
					treatBatch = (mapper.reader().forType(Batch.class).readValue(hit.getSourceAsString()));
					treatmentsIds.put(treatBatch.getTreatmentId(), treatBatch);
				}
				//response = client.prepareSearchScroll(response.getScrollId()).setScroll(new TimeValue(60000)).execute().actionGet();
			//}
			 //stops when zero hits
			client.close();
			return treatmentsIds;		
		}
	
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		

	//RECUPERATION BATCHS
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
					treatUnit = (mapper.reader().forType(TreatmentUnit.class).readValue(hit.getSourceAsString()));
					treatUnitList.put(treatUnit.getUnitId(), treatUnit);
				}
				response = client.prepareSearchScroll(response.getScrollId()).setScroll(new TimeValue(60000)).execute().actionGet();
			}
			while(response.getHits().getHits().length != 0); //stops when zero hits
			client.close();
			return treatUnitList;
		}
		
	
	//////--------------------------- Sans fonction Scroll ---------------------////
						
		// RECUPERATION DES ETAPES

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
				.get();

		//Récupération de tous les hits
		do {
			for(SearchHit hit : response.getHits().getHits()) {
				step = (mapper.reader().forType(Step.class).readValue(hit.getSourceAsString()));
				stepsList.put(step.stepExecutionId, step);
			}
			response = client.prepareSearchScroll(response.getScrollId()).setScroll(new TimeValue(60000)).execute().actionGet();
		}
		while(response.getHits().getHits().length != 0); //stops when zero hits

		client.close();
		return stepsList;
	}

//USED
	public static HashMap<String, Step> getSteps(String unitId, String status) throws JsonProcessingException, IOException{
		HashMap<String, Step> stepsList = new HashMap<String, Step>();
		ObjectMapper mapper =  new ObjectMapper();
		Step step = new Step();

		Client client = startES(settings);		
		BoolQueryBuilder query = QueryBuilders.boolQuery()
				.filter(QueryBuilders.matchQuery("unitId", unitId))
				.filter(QueryBuilders.matchQuery("status", status));	
		
		//Effectue la requête vers ElasticSearch
		SearchResponse response = client.prepareSearch("most")
				.setTypes("steps")
				.setScroll(new TimeValue(60000))
				.setQuery(query)
				//.setPostFilter(filter)
				.setSize(100).get();

		//Récupération de tous les hits
		do {
			for(SearchHit hit : response.getHits().getHits()) {
				step = (mapper.reader().forType(Step.class).readValue(hit.getSourceAsString()));
				stepsList.put(step.stepExecutionId, step);
			}
			response = client.prepareSearchScroll(response.getScrollId()).setScroll(new TimeValue(60000)).execute().actionGet();
		}
		while(response.getHits().getHits().length != 0); //stops when zero hits

		client.close();
		return stepsList;
	}


//---------------------- Récupération ALL Batchs ---------------------------------------//
				
				public static HashMap<String, TreatmentUnit> getStartedUnits(String date) throws JsonProcessingException, IOException{
					HashMap<String, TreatmentUnit> treatUnitList = new HashMap<String, TreatmentUnit>();
					ObjectMapper mapper =  new ObjectMapper();
					TreatmentUnit treatUnit = new TreatmentUnit();
					String dateFrom = date + "T00:00:00Z";
					String dateTo = date + "T23:59:59Z";

					Client client = startES(settings);
					QueryBuilder filter = QueryBuilders.rangeQuery("startTime").from(dateFrom).to(dateTo);			
					QueryBuilder query = QueryBuilders.matchQuery("status", "STARTED");
					//Effectue la requête vers ElasticSearch
					SearchResponse response = client.prepareSearch("unit")
							.setTypes("unitExecution")
							.setQuery(query)
							.setPostFilter(filter)
							.addSort("startTime", SortOrder.DESC) //Tri
							.setFrom(2)
							.setSize(10).get();

					//Récupération de tous les hits
					
						for(SearchHit hit : response.getHits().getHits()) {
							treatUnit = (mapper.reader().forType(TreatmentUnit.class).readValue(hit.getSourceAsString()));
							treatUnitList.put(treatUnit.getUnitId(), treatUnit);
						}
						//response = client.prepareSearchScroll(response.getScrollId()).setScroll(new TimeValue(60000)).execute().actionGet();
					//}
					 //stops when zero hits
					client.close();
					return treatUnitList;
				}
				
				//récupère l'unité de traitement finie qui correspond à l'unitId donné
				public static TreatmentUnit getFinishedBatch(String unitId) throws JsonProcessingException, IOException{
					ObjectMapper mapper = new ObjectMapper();
					Client client = startES(settings);		
					BoolQueryBuilder query = QueryBuilders.boolQuery()
							.filter(QueryBuilders.matchQuery("unitId", unitId))
							.filter(QueryBuilders.matchQuery("status", "OK OR KOF OR KOT"));						
					
					//Effectue la requête vers ElasticSearch
					SearchResponse response = client.prepareSearch("unit")
							.setTypes("unitExecution")
							.setQuery(query)
							.execute().actionGet();
					
					SearchHit[] searchHits = response.getHits().getHits();
				    TreatmentUnit treatUnit=(mapper.reader().forType(TreatmentUnit.class).readValue(searchHits[0].getSourceAsString()));
					client.close();
					return treatUnit;				
				}
				
				
//---------------------- Récupération Batchs PAR STATUT ---------------------------------------//
				public static HashMap<String, TreatmentUnit> getUnitsByStatus(String date, String status) throws JsonProcessingException, IOException {
					System.out.println("getFinishedUnitsByStatus with status : " + status);
					HashMap<String, TreatmentUnit> treatUnitList = new HashMap<String, TreatmentUnit>();
					ObjectMapper mapper = new ObjectMapper();
					String dateFrom = date + "T00:00:00Z";
					String dateTo = date + "T23:59:59Z";
					Client client = startES(settings);
					QueryBuilder filter = QueryBuilders.rangeQuery("endTime").from(dateFrom).to(dateTo);			
					QueryBuilder query = QueryBuilders.matchQuery("status", status);
					
					
					//Effectue la requête vers ElasticSearch
					SearchResponse response = client.prepareSearch("unit")
							.setTypes("unitExecution")
							.setQuery(query)
							.setPostFilter(filter)
							.addSort("endTime", SortOrder.DESC)
							.execute().actionGet();
					
					 SearchHit[] searchHits = response.getHits().getHits();
				       for(SearchHit searchHit:searchHits) {
				    	   TreatmentUnit treatUnit=(mapper.reader().forType(TreatmentUnit.class).readValue(searchHit.getSourceAsString()));
				    	   System.out.println(treatUnit.toString());
				    	   treatUnitList.put(treatUnit.getUnitId(), treatUnit);
				       }
								
					client.close();
					return treatUnitList;				
				}
				
				public static TreatmentUnit getStartedUnitsByStatus(String unitId) throws JsonProcessingException, IOException{
					ObjectMapper mapper = new ObjectMapper();
					Client client = startES(settings);		
					BoolQueryBuilder query = QueryBuilders.boolQuery()
							.filter(QueryBuilders.matchQuery("unitId", unitId))
							.filter(QueryBuilders.matchQuery("status", "STARTED"));						
					
					//Effectue la requête vers ElasticSearch
					SearchResponse response = client.prepareSearch("unit")
							.setTypes("unitExecution")
							.setQuery(query)
							.execute().actionGet();
					
					SearchHit[] searchHits = response.getHits().getHits();
				    TreatmentUnit treatUnit=(mapper.reader().forType(TreatmentUnit.class).readValue(searchHits[0].getSourceAsString()));
					client.close();
					return treatUnit;				
				}
	
	
//-----------------------------GET BATCHS ALL ---------------------------------------------//
								
				//SELECT UNIQUE TreatmentId FROM Treatments  
				public static ArrayList<String> getTreatmentIds(String date, String limitDate) throws IOException{
					String excludedIds = "za1";
					Client client = startES(settings);	
					//String dateFrom = date + "T00:00:00Z";
					String dateFrom = date + "T"+limitDate+"Z";
					String dateTo = date + "T23:59:59Z";
					BoolQueryBuilder query = QueryBuilders.boolQuery()
							.mustNot(QueryBuilders.termQuery("treatmentId", excludedIds))
							.filter(QueryBuilders.matchQuery("status", "STARTED"));
					System.out.println("getTreatmentIds");
					SearchResponse response = client.prepareSearch("unit")
							.setTypes("unitExecution")
							.setQuery(query)
							.addSort("startTime", SortOrder.DESC)
							.addAggregation(AggregationBuilders.dateRange("date_range").field("startTime").addRange(dateFrom, dateTo)
									.subAggregation(AggregationBuilders.terms("by_TreatmentId").field("treatmentId").size(2)))							
							
							.execute().actionGet();			
					
					
					ArrayList<String> distinctIds = new ArrayList<String>();
					Range aggregation = (Range) response.getAggregations().asList().get(0);					
					StringTerms subAggr = (StringTerms) aggregation.getBuckets().get(0).getAggregations().asList().get(0);					
					 for (Bucket bucket:subAggr.getBuckets()){
						 System.out.println("Treatment id : "+bucket.getKey());
						 System.out.println("Docs count : "+bucket.getDocCount());
						 distinctIds.add((String) bucket.getKey());
					 }
					 
					System.out.println("total Hits : " +response.getHits().totalHits);
					 
					client.close();
					return distinctIds;	
				}
				

				//Get Started units TreatmentId = string request
				public static HashMap<String, TreatmentUnit> getUnitsByTreatmentId(String treatmentId, String status) throws IOException{
					ObjectMapper mapper = new ObjectMapper();
					Client client = startES(settings);
					HashMap<String, TreatmentUnit> startedUnits = new HashMap<String, TreatmentUnit>();
					BoolQueryBuilder query = QueryBuilders.boolQuery()
							.filter(QueryBuilders.matchQuery("treatmentId", treatmentId))
							.filter(QueryBuilders.matchQuery("status", status));	
					
					SearchResponse response = client.prepareSearch("most")
							.setTypes("units")
							.setQuery(query)
							.setScroll(new TimeValue(60000))
							.execute().actionGet();				
				       do {
							for(SearchHit hit : response.getHits().getHits()) {
								 TreatmentUnit treatUnit=(mapper.reader().forType(TreatmentUnit.class).readValue(hit.getSourceAsString()));
						    	   //System.out.println(treatUnit.toString());
						    	   startedUnits.put(treatUnit.getUnitId(), treatUnit);
							}
							response = client.prepareSearchScroll(response.getScrollId()).setScroll(new TimeValue(60000)).execute().actionGet();
						}
						while(response.getHits().getHits().length != 0); //stops when zero hits
				       
				       
								
					client.close();					
					return startedUnits;					
				}
			/*	
				//Get Finished units TreatmentId = string request
				public static HashMap<String, TreatmentUnit> getFinishedUnitsByTreatmentId(String treatmentId) throws IOException{
					ObjectMapper mapper = new ObjectMapper();
					Client client = startES(settings);
					HashMap<String, TreatmentUnit> finishedUnits = new HashMap<String, TreatmentUnit>();
					BoolQueryBuilder query = QueryBuilders.boolQuery()
							.filter(QueryBuilders.matchQuery("treatmentId", treatmentId))
							.filter(QueryBuilders.matchQuery("status", "OK OR KO OR KOT OR KOF"));	
					
					SearchResponse response = client.prepareSearch("most")
							.setTypes("units")
							.setQuery(query)
							.setScroll(new TimeValue(60000))
							.execute().actionGet();				
				       do {
							for(SearchHit hit : response.getHits().getHits()) {
								  TreatmentUnit treatUnit=(mapper.reader().forType(TreatmentUnit.class).readValue(hit.getSourceAsString()));
						    	  //System.out.println(treatUnit.toString());
						    	  finishedUnits.put(treatUnit.getUnitId(), treatUnit);
							}
							response = client.prepareSearchScroll(response.getScrollId()).setScroll(new TimeValue(60000)).execute().actionGet();
						}
						while(response.getHits().getHits().length != 0); //stops when zero hits
				       
								
					client.close();					
					return finishedUnits;					
				}*/
				
//-----------------------------GET BATCHS KOT ---------------------------------------------//				

				//SELECT UNIQUE TreatmentId FROM Treatments by status
		/*		public static ArrayList<String> getTreatmentIdsByStatus(String date, String limitDate, String status, ArrayList<String> exclusion) throws IOException{
					Client client = startES(settings);	
					String dateFrom = date + "T"+limitDate+"Z";
					String dateTo = date + "T23:59:59Z";

					BoolQueryBuilder query = QueryBuilders.boolQuery()
							.filter(QueryBuilders.matchQuery("status", status));
					for(String id:exclusion){
						query.mustNot(QueryBuilders.termQuery("treatmentId", id));
					}
					
					System.out.println("getTreatmentIdsByStatus");
					SearchResponse response = client.prepareSearch("unit")
							.setTypes("unitExecution")
							.setQuery(query)
							.addAggregation(AggregationBuilders.dateRange("date_range").field("endTime").addRange(dateFrom, dateTo)
									.subAggregation(AggregationBuilders.terms("by_TreatmentId").field("treatmentId").size(10)))							
							.addSort("endTime", SortOrder.DESC)
							.execute().actionGet();			
					
					
					ArrayList<String> distinctIds = new ArrayList<String>();
					Range aggregation = (Range) response.getAggregations().asList().get(0);					
					StringTerms subAggr = (StringTerms) aggregation.getBuckets().get(0).getAggregations().asList().get(0);					
					 for (Bucket bucket:subAggr.getBuckets()){
						 System.out.println("Treatment id : "+bucket.getKey());
						 distinctIds.add((String) bucket.getKey());
					 }
					 
					System.out.println("total Hits : " +response.getHits().totalHits);
					 
					client.close();
					return distinctIds;	
				}*/
				
	//----------------------------------SECONDE PARTIE PROJET ------------------//
				
				

}
