package com.soco

import socowebserver.Utility;
import org.codehaus.groovy.grails.web.json.JSONObject

class ActivityEventController {

    def index() { }
	
	def getNumOfActivityEventByUserID(long user_id){
		def num = 0;
		try{
			def sql = "from ActivityEvent where user_id="+user_id;
			def aeList = ActivityEvent.executeQuery(sql);
			num = aeList.size();
		}catch(Exception e){
			log.error(e.getMessage());
		}
		return num;
	}
	
	def getEventJsonObj(long user_id){
		JSONObject json = new JSONObject();
		try{
			def sql = "from ActivityEvent where user_id="+user_id;
			def aeList = ActivityEvent.executeQuery(sql);
			if(aeList.size() > 0){
				def aeIte = aeList.iterator();
				def aList = new ArrayList();
				aeIte.eachWithIndex { item, index ->
					ActivityEvent ae = (ActivityEvent)item;
					aList.add(ae.toJsonString());
				}
				json.put("activity", aList.toString());
				json.put("status", MobileController.SUCCESS);
			}else{
				json.put("status", MobileController.FAIL);
			}
		}catch(Exception e){
			log.error(e.getMessage());
			json.put("status", MobileController.FAIL);
		}
		return json;
	}
	
	def addActivityUpdateEvent(uid, aid, value, operate){
		boolean ret = false;
		try{
			def sql = "from UserActivity where activity_id="+aid;
			def uaList = UserActivity.executeQuery(sql);
			def uaIte = uaList.iterator();
			uaIte.eachWithIndex { item, index ->
				def ua = (UserActivity)item;
				if(ua.user_id != uid){
					ActivityEvent ae = new ActivityEvent();
					ae.user_id = ua.user_id;
					ae.activity_id = aid;
					ae.event_content_type = "activity";
					ae.event_operate_type = operate;
					ae.event_content_value = value;
					def md5Str = ua.user_id.toString() + aid.toString() + ae.event_content_type + ae.event_operate_type + ae.event_content_value;
					ae.signature = Utility.getMD5(md5Str);;
					if(ae.save()){
						ret = true;
						log.debug("addActivityUpdateEvent. Save activity update event success.");
					}else{
						log.debug("addActivityUpdateEvent. Save activity update event failure.");
					}
				}
			}
		}catch(Exception e){
			log.error(e.getMessage());
		}
		return ret;
	}
	
	def addActivityAttributeEvent(uid, aid, attribute, operate){
		
	}
	
	def addUserActivityEvent(){
		
	}
	
	def addActivityFileEvent(){
		
	}
}
