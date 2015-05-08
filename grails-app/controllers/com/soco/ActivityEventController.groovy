package com.soco

class ActivityEventController {

    def index() { }
	
	def getNumOfActivityEventByUserID(long user_id){
		
	}
	
	def addActivityUpdateEvent(uid, aid, name){
		boolean ret = false;
		try{
			ActivityEvent ae = new ActivityEvent();
			ae.user_id = uid;
			ae.activity_id = aid;
			ae.event_content_type = "activity";
			ae.event_operate_type = "update";
			ae.event_content_value = "{'name':'"+name+"'}";
			if(ae.save()){
				ret = true;
				log.debug("addActivityUpdateEvent. Save activity update event success.");
			}else{
				log.debug("addActivityUpdateEvent. Save activity update event failure.");
			}
		}catch(Exception e){
			log.debug(e.getMessage());
		}
		return ret;
	}
	
	def addActivityAttributeEvent(){
		
	}
	
	def addUserActivityEvent(){
		
	}
	
	def addActivityFileEvent(){
		
	}
}
