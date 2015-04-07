package com.soco

import java.util.Date;
import grails.converters.JSON
import org.codehaus.groovy.grails.web.json.JSONObject;

class Activity {

    String name
	String tag
	String signature = ""
	Integer active = 1
	Boolean is_achieved = false
	Boolean is_deleted = false
	String type = ""
	

    static constraints = {
		name blank:false
		tag blank: false
		signature blank: false
		active blank: false
		is_achieved blank:false
		is_deleted blank: false
		type blank:false
    }
	
	def createActivity(JSONObject jsonObj) {
		log.debug(jsonObj.toString())
		
		return parseJsonAndSave(jsonObj)
	}
	
	def parseJsonAndSave(JSONObject jsonObj){
		def jsonStr = "{}"
		def error = false
		
		try{
			if(!error && jsonObj.containsKey("name") ){
				this.name = jsonObj.get("name")
			}else{
				jsonStr = "{name:'error',status:'failure'}"
				error = true
			}
			
			if(!error && jsonObj.containsKey("tag") ){
				this.tag = jsonObj.get("tag")
			}else{
				jsonStr = "{tag:'error',status:'failure'}"
				error = true
			}
			
			if(!error && jsonObj.containsKey("type") ){
				this.type = jsonObj.get("type")
			}else{
				jsonStr = "{type:'error',status:'failure'}"
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
		return "{name:'"+this.name+"',tag:'"+this.tag+"',signature:'"+this.signature+"',type:'"+this.type+"',is_achieved:"+this.is_achieved+"}"
	}
}
