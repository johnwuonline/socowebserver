package com.soco

import grails.converters.JSON

import org.codehaus.groovy.grails.web.json.JSONObject;

class ExchangeController {
	
	def springSecurityService;
	

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
		def user_id = springSecurityService.currentUser.id;
		Message message = new Message();
		JSONObject answer = message.parseJsonObject(jsonObject);
		
		if(answer.get("status").equals(MobileController.SUCCESS)){
			if(message.save()){
				//convert to user message
				MessageController mc = new MessageController();
				def userMsgList = mc.convert2UserMsg(message, user_id);
				userMsgList.eachWithIndex { item, index ->
					def um = (UserMessage)item;
					if(um.save()){
						//
					}else{
						log.error("<sendOut> user message save failed for user id:"+um.to_user_id+", msg id:"+um.message_id);
					}
				}
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
		JSONObject json = new JSONObject();
		try{
			def user_id = springSecurityService.currentUser.id;
			UserMessageController umc = new UserMessageController();
			json.put("message", umc.getUserMsgByUserID(user_id));
			json.put("status", MobileController.SUCCESS);
			if(umc.getNumOfUserMsgByUserID(user_id) > 0){
				json.put("finish", 0);
			}else{
				json.put("finish", 1);
			}
		}catch(Exception e){
			log.error(e.getMessage());
			json.put("status", MobileController.FAIL);
		}
		render json;
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
		JSONObject json = new JSONObject();
		try{
			def sigList;
			def ret;
			(ret, sigList) = getRequestValueByNameFromJSON(getRequestJSON(), "ack");
			if(ret){
				sigList.eachWithIndex { item, index ->
					if(item instanceof JSONObject){
						def signature
						(ret, signature) = getRequestValueByNameFromJSON(item, "signature");
						if(ret){
							def sql = "delete UserMessage where signature='"+signature+"' and status="+UserMessageController.STATUS_SENT;
							ret = UserMessage.executeUpdate(sql);
						}else{
						}
					}
				}
				json.put("status", MobileController.SUCCESS);
			}else{
				json.put("status", MobileController.FAIL);
			}
		}catch(Exception e){
			log.error(e.getMessage());
			json.put("status", MobileController.FAIL);
		}
		render json;
	}
}
