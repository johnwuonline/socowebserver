package com.soco

class UserActivityController {
	
	static final String RELATION_OWN = "OWNER"
	static final String RELATION_MEMBER = "MEMBER"
	static final String STATUS_ACCEPT = "ACCEPT"
	static final String STATUS_DENY = "DENY"

    def index() { }
	
	boolean saveUserActivity(user_id, aid, relation, status){
		boolean ret = false;
		
		try{
			def sql = "from UserActivity where user_id="+user_id+" and activity_id="+aid;
			def uaList = UserActivity.executeQuery(sql);
			if(uaList.size() > 0){
				ret = true;
			} else {
				def user_activity = new UserActivity(user_id, aid, relation, status);
				if(user_activity.save() ){
					ret = true;
				}
				else{
					ret = false;
					log.error("activity save fail.");
				}
			}
		}catch(Exception e){
			log.error(e.getMessage());
		}
		return ret;
	}
	
	def getUsersByActivityID(long aid){
		def uaList = [];
		try{
			def sql = "from UserActivity where activity_id="+aid;
			uaList = UserActivity.executeQuery(sql);
		}catch(Exception e){
			log.error(e.getMessage());
		}
		return uaList;
	}
	
	def getUsersJsonStrByActivityId(activityid){
		def user_list = UserActivity.executeQuery("from UserActivity where activity_id=?",activityid);
		def uaList = new ArrayList();
		
		user_list.eachWithIndex { item, index ->
			def ua = (UserActivity)item;
			uaList.add(ua.toJsonString());
		}
		
		return uaList.toString();
	}
}
