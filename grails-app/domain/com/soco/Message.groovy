package com.soco

import grails.converters.JSON
import java.text.SimpleDateFormat
import java.util.Date
import org.codehaus.groovy.grails.web.json.JSONObject;

class Message {
	
	int from_type;
	String from_id;
	int to_type;
	String to_id;
	Date send_date_time;
	int context_type;
	String context;
	String from_device;

    static constraints = {
    }
	
	def parseJsonObject(JSONObject jsonObj){
		def jsonStr = "{}"
		def error = false
		
		try{
			if(!error && jsonObj.containsKey("from_type") ){
				this.from_type = jsonObj.get("from_type")
			}else{
				jsonStr = "{error:'from_type',status:'failure'}"
				error = true
			}
			
			if(!error && jsonObj.containsKey("from_id") ){
				this.from_id = jsonObj.get("from_id")
			}else{
				jsonStr = "{error:'from_id',status:'failure'}"
				error = true
			}
			
			if(!error && jsonObj.containsKey("to_type") ){
				this.to_type = jsonObj.get("to_type")
			}else{
				jsonStr = "{error:'to_type',status:'failure'}"
				error = true
			}
			
			if(!error && jsonObj.containsKey("to_id") ){
				this.to_id = jsonObj.get("to_id")
			}else{
				jsonStr = "{error:'to_id',status:'failure'}"
				error = true
			}
			
			if(!error && jsonObj.containsKey("send_date_time") ){
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				this.send_date_time = formatter.parse(jsonObj.get("send_date_time"));
			}else{
				jsonStr = "{error:'send_date_time',status:'failure'}"
				error = true
			}
			
			if(!error && jsonObj.containsKey("context_type") ){
				this.context_type = jsonObj.get("context_type")
			}else{
				jsonStr = "{error:'context_type',status:'failure'}"
				error = true
			}
			
			if(!error && jsonObj.containsKey("context") ){
				this.context = jsonObj.get("context")
			}else{
				jsonStr = "{error:'context',status:'failure'}"
				error = true
			}
			
			if(!error && jsonObj.containsKey("from_device") ){
				this.from_device = jsonObj.get("from_device")
			}else{
				jsonStr = "{error:'from_device',status:'failure'}"
				error = true
			}
			
			if(!error){
				jsonStr = "{status:'success'}"
			} else {
			
			}
		} catch(Exception e){
			jsonStr = "{status: 'failure'}"
			log.error(e.message)
		}
		return JSON.parse(jsonStr)
	}
	
	def toJsonString(){
		return "{from_type:"+this.from_type +
			 ",from_id:'" + this.from_id +
			 "',to_type:" + this.to_type  +
			 ",to_id:'" + this.to_id  +
			 "',send_date_time:'" + this.send_date_time.format("yyyy-MM-dd HH:mm:ss") +
			 "',context_type:" + this.context_type +
			 ",context:'" + this.context +
			 "'}"; 
	}
	
	def toJsonString(key, value){
		return "{from_type:"+this.from_type +
			  ",from_id:'" + this.from_id +
			  "',to_type:" + this.to_type +
			  ",to_id:'" + this.to_id +
			  "',send_date_time:'" + this.send_date_time.format("yyyy-MM-dd HH:mm:ss") +
			  "',context_type:" + this.context_type +
			  ",context:'" + this.context +
			  "'," + key + ":'" + value + "'}";
	}
}
