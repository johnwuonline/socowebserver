package com.soco

import grails.converters.JSON
import org.codehaus.groovy.grails.web.json.JSONObject

class MobileController {
	public static final String SUCCESS = "success";
	public static final String FAIL = "failure";
	
	def springSecurityService
	def sql_projects_by_user_id = "select project_id from UserProjects where user_id="

    def index() { 
		render "This is for mobile!"
	}
	
	def createProject() {
		def jsonObject = request.JSON
		def project = new Projects()
		JSONObject answer = project.createProject(jsonObject)
		
		if(answer.get("status").equals(MobileController.SUCCESS)){
			if(project.save()){
				def prj_id = project.getId()
				def user_id = springSecurityService.currentUser.id
				def relation = "OWNER"
				def status = "ACCEPT"
				def user_project = new UserProjects(user_id, prj_id, relation, status)
				if(user_project.save() ){
					def jsonStr = "{id:'"+prj_id+"',status:"+MobileController.SUCCESS+"}"
					answer = JSON.parse(jsonStr)
				}
				else{
					project.delete(flush:true)
					def jsonStr = "{status:"+MobileController.FAIL+"}"
					answer = JSON.parse(jsonStr)
					log.error("project save fail.");
				}
			}
			else{
				def jsonStr = "{status:"+MobileController.FAIL+"}"
				answer = JSON.parse(jsonStr)
				log.error("project save fail.");
			}
		} else {
			log.error("project save fail.");
		}
		
		render answer
	}
	
	/*
	 * 
	 * */
	def createActivityByProjectID(){
		JSONObject json = new JSONObject()
		try{
			def jsonObject = request.JSON
			if(jsonObject.containsKey("project")){
				def pid = jsonObject.get("project");
			
				log.debug("project id:"+pid)
				def sql = "from Projects where id=" + pid
				def projects = Projects.executeQuery(sql)
				if(projects.size() == 1) {
					Activity a = new Activity()
					a.project_id = pid;
					if(jsonObject.containsKey("latitude")){
						a.loc_latitude = jsonObject.get("latitude")
					}
					if(jsonObject.containsKey("longitude")){
						a.loc_longitude = jsonObject.get("longitude")
					}
					if(jsonObject.containsKey("name")){
						a.loc_name = jsonObject.get("name")
					}
					
					if(a.save()){
						def aid = a.getId()
						json.put("status", MobileController.SUCCESS)
						json.put("id", aid)
					} else {
						json.put("status", MobileController.FAIL);
						json.put("message", "project save failed.");
					}
				} else {
					json.put("status", MobileController.FAIL);
					json.put("message", "project is not existent.");
				}
			}
		}catch(Exception e){
			log.error(e.message)
			json.put("status", MobileController.FAIL);
		}
		render json
	}
	
	
		
	/*
	 * get Project details by project id
	 * */
	def getProjectByID(){
		JSONObject json = new JSONObject()
		try{
			def jsonObject = request.JSON
			if(jsonObject.containsKey("project")){
				def pid = jsonObject.get("project");
			
				log.debug("project id:"+pid)
				def sql = "from Projects where id=" + pid
				def projects = Projects.executeQuery(sql)
				def prj_ite = projects.iterator()
				prj_ite.eachWithIndex { prj, i ->
					json.put("project"+pid, prj.getJsonStr())
				}
			}
		}catch(Exception e){
			log.error(e.message)
			json.put("status", MobileController.FAIL);
		}
		render json
	}
	
	/* 
	 * get all projects list by current user
	 *  */
	def getAllProjectsByCurrentUser(){
		JSONObject json = new JSONObject()
		try{
			def user_id = springSecurityService.currentUser.id;
			def projects_list = UserProjects.executeQuery(sql_projects_by_user_id+user_id);
			def str = projects_list.join(",");
			json.put("status", MobileController.SUCCESS);
			json.put("projects", str);
			
		}catch(Exception e){
			log.error(e.message)
			json.put("status", MobileController.FAIL);
		}
		render json
	}
	
}
