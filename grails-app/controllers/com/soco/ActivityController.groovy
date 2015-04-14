package com.soco

import org.codehaus.groovy.grails.web.json.JSONObject

class ActivityController {

    def index() { }
	
	def showAllActivityAsGSP(){
		render "hello"
	}
	
	
	/*
	 * get activity details by activity id
	 * */
	def getActivityByID(aid){
		JSONObject json = new JSONObject()
		try{
			def sql = "from Activity where id=" + aid
			def activityList = Activity.executeQuery(sql)
			if(activityList.size() == 1){
				// put activity basic information
				json.put("activity", activityList[0].getJsonStr());
				// put activity attribute information
				sql = "from ActivityAttribute where activity_id=" + aid
				def actAttrList = ActivityAttribute.executeQuery(sql);
				def actAttrIte = actAttrList.iterator()
				def attrList = new ArrayList();
				actAttrIte.eachWithIndex { item, index ->
					attrList.add(item.toJsonString());
				}
				json.put("attributes", attrList.toString())
				// put user activity information
				UserActivityController uac = new UserActivityController();
				json.put("members", uac.getUsersJsonStrByActivityId(aid));
				// put status
				json.put("status", MobileController.SUCCESS);
			} else {
				log.error("The activity number is not 1. It is " + activityList.size())
				json.put("status", MobileController.FAIL);
			}
			//
		}catch(Exception e){
			log.error(e.message)
			json.put("status", MobileController.FAIL);
		}
		return json
	}
}
