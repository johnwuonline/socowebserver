package com.soco

class UserActivity {
    long user_id
	long activity_id
	String relation
	String status

    static constraints = {
		user_id()
		activity_id()
		relation()
		status()
    }
	
	UserActivity(userid, prjid, rel, stat){
		this.user_id = userid
		this.activity_id = prjid
		this.relation = rel
		this.status = stat
	}
	
	def usersByProjectToJson(activityid){
		def user_list = UserActivity.executeQuery("select user_id from UserActivity where activity_id=?",activityid);
		def ite = user_list.iterator();
		def users_str = "{users:'";
		if(user_list.size() > 0){
			ite.eachWithIndex { item, index ->
				users_str += item + ",";
			}
			users_str = users_str.substring(0, users_str.length() - 1);
		}
		users_str += "'}";
		return users_str;
	}
}
