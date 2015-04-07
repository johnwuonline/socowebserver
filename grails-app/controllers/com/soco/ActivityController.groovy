package com.soco

import org.codehaus.groovy.grails.web.json.JSONObject

class ActivityController {

    def index() { }
	
	
	/*
	 * get activity details by activity id
	 * */
	def getActivityByID(aid){
		JSONObject json = new JSONObject()
		try{
			def sql = "from Activity where id=" + aid
			def activityList = Activity.executeQuery(sql)
			if(activityList.size() == 1){
				json.put("activity", activityList[0].getJsonStr())
				sql = "from ActivityAttribute where activity_id=" + aid
				def actAttrList = ActivityAttribute.executeQuery(sql);
				def actAttrIte = actAttrList.iterator()
				def attrList = new ArrayList();
				actAttrIte.eachWithIndex { item, index ->
					attrList.add(item.toJsonString());
				}
				json.put("attributes", attrList.toString())
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
