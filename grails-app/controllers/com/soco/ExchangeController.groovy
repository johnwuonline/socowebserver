package com.soco

import grails.converters.JSON

import org.codehaus.groovy.grails.web.json.JSONObject;

class ExchangeController {

    def index() { }
	
	
	/*
	 * Convenient to change request.JSON format
	 * */
	def getRequestJSON(){
		return request.JSON;
	}
	
	/*
	 * fetch value from json by key
	 * */
	def getRequestValueByNameFromJSON(JSONObject json, String name){
		def value = null;
		def ret = false;
		try{
			if(json instanceof JSONObject && json.containsKey(name)){
				value = json.get(name);
				log.debug(name + ":" + value);
				ret = true;
			}
		} catch (Exception e){
			log.error(e.message);
		}
		return [ret,value];
	}
	
	
	/* sendOut
	 * send out a message to an activity or user
	 * @param in
	   	{ 
	   		from_type: 1, from_id: "test@test.com",
	   		to_type: 2, to_id: 1,
	   		send_date_time: "2015-04-05 12:12:12",
	   		from_device: "android Lenovo",
	   		context_type: 1,
	   		context:"hi, how are you?"
   		}
	   @return
	   	{ status:"success"}
		OR
		{ status:"failure"}		
	 * */
	def sendOut(){
		def jsonObject = getRequestJSON();
		Message message = new Message();
		JSONObject answer = message.parseJsonObject(jsonObject);
		
		if(answer.get("status").equals(MobileController.SUCCESS)){
			if(message.save()){
				//convert to user message
				MessageController mc = new MessageController();
				def userMsgList = mc.convert2UserMsg(message);
			} else {
				def jsonStr = "{status:"+MobileController.FAIL+"}";
				answer = JSON.parse(jsonStr);
				log.error("activity save fail.");
			}
		}
		render answer;
	}
	
	/* receiveMessage
	 * @param in NONE
	 * @return 
	  	{
	  		status:"success",
	  		message:[{
		  		from_type: 1, from_id: "test@test.com",
		  		to_type: 2, to_id: 1,
		  		send_date_time: "2015-04-05 12:12:12",
		  		context_type: 1,
		  		signature: "AKFEI2345KL243LKJ234",
		   		context:"hi, how are you?"
		   		},{...}
	   		],
	   		finish:1
	  	}
	  	// finish: 1 - finish, 0 - not finish
	  	OR
		{ status:"failure"}	
	 * */
	def receiveMsg(){
		try{
		
		}catch(Exception e){
			log.error(e.getMessage());
		}
	}
	
	/* ackReceivedMsg
	 * @param in
	 	{
	 		ack:[{signature:"AKFEI2345KL243LKJ234"},{...}]
	 	}
	   @return
	   	{ status:"success"}
		OR
		{ status:"failure"}
	 * */
	def ackReceivedMsg(){
		try{
			
		}catch(Exception e){
			log.error(e.getMessage());
		}
	}
}
