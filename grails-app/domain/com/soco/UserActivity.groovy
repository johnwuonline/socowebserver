package com.soco

class UserActivity {
    long user_id
	long activity_id
	String relation
	String status

    static constraints = {
		user_id(unique: ['activity_id'])
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
	
	def toJsonString(){
		return "{user_id:"+this.user_id+",activity_id:"+this.activity_id+",relation:'"+this.relation+"',status:'"+this.status+"'}";
	}
}
