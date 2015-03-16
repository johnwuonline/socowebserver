package com.soco

import grails.converters.JSON

import org.codehaus.groovy.grails.web.json.JSONObject

import com.fasterxml.jackson.annotation.JsonAnyGetter;

class Projects {
	String name
	String tag
	Date create_time = new Date()
	Date update_time = new Date()
	String signature = ""
	Integer active = 1
	Boolean is_deleted = false

    static constraints = {
		name blank:false
		tag blank: false
		create_time blank: false
		update_time blank: false
		signature blank: false
		active blank: false
		is_deleted blank: false
    }
	
	def createProject(JSONObject jsonObj) {
		log.debug(jsonObj.toString())
		
		return parseJsonAndSave(jsonObj)
	}
	
	def parseJsonAndSave(JSONObject jsonObj){
		def jsonStr = ""
		def error = false
		
		try{
			if(!error && jsonObj.containsKey("name") ){
				this.name = jsonObj.get("name")
			}else{
				jsonStr = "{name:'error',status:'failure'}"
				error = true
			}
			
			if(!error && jsonObj.containsKey("create_time") ){
				this.create_time = Date.parse("d/M/yyyy H:m:s",jsonObj.get("create_time"))
				
			}else{
				jsonStr = "{create_time:'error',status:'failure'}"
				error = true
			}
			
			if(!error && jsonObj.containsKey("update_time") ){
				this.update_time = Date.parse("d/M/yyyy H:m:s",jsonObj.get("update_time"))
			}else{
				jsonStr = "{update_time:'error',status:'failure'}"
				error = true
			}
			
			if(!error && jsonObj.containsKey("tag") ){
				this.tag = jsonObj.get("tag")
			}else{
				jsonStr = "{tag:'error',status:'failure'}"
				error = true
			}
			
			if(!error && jsonObj.containsKey("signature") ){
				this.signature = jsonObj.get("signature")
			}else{
				jsonStr = "{signature:'error',status:'failure'}"
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
	
	def getJsonStr(){
		return "{'name':"+this.name+",'create_time':"+this.create_time.toString()+"}"
	}
	
}
