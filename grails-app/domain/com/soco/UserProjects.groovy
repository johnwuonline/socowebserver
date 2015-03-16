package com.soco

class UserProjects {
	long user_id
	long project_id
	String relation
	String status

    static constraints = {
		user_id()
		project_id()
		relation()
		status()
    }
	
	UserProjects(userid, prjid, rel, stat){
		this.user_id = userid
		this.project_id = prjid
		this.relation = rel
		this.status = stat
	}
	
	def usersByProjectToJson(projectid){
		def user_list = UserProjects.executeQuery("select user_id from UserProjects where project_id=?",projectid);
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
