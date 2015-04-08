package com.soco

import org.codehaus.groovy.grails.web.json.JSONObject

class ActivityAttribute {
	
	long activity_id
	String name
	String type
	String value
	
	Date create_date
	Date last_modify_date
	long create_user_id
	long last_modify_user_id

    static constraints = {
		activity_id blank:false
		name blank:false
		type blank:false
		value blank:false
    }
	
	def parseJsonObject(json){
		boolean error = false
		JSONObject attr = (JSONObject)json
		try{
			if(!error && attr.containsKey("name")){
				this.name = attr.getString("name");
			} else {
				error = true
				log.error("No name in requestion json.")
			}
			if(!error && attr.containsKey("type")){
				this.type = attr.getString("type");
			} else {
				error = true
				log.error("No type in requestion json.")
			}
			if(!error && attr.containsKey("value")){
				this.value = attr.getString("value");
			} else {
				error = true
				log.error("No value in requestion json.")
			}
		}catch(Exception e){
			log.error(e.getMessage())
			error = true
		}
		return !error
	}
	
	def toJsonString(){
		return "{name:'"+name+"',type:'"+type+"',value:'"+value+"'}";
	}
}
