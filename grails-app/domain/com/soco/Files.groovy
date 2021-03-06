package com.soco

class Files {
	String display_name;
	String uri;
	String remote_path;
	String local_path;
	String username;
	long user_id;
	Date dateCreated;
	Date lastUpdated;

    static constraints = {
		display_name blank:false
		uri blank: true, nullable: true
		remote_path blank: true, nullable: true
		local_path blank: true, nullable: true
    }
	
	def toJsonString(){
		return "{'file_id':"+id+
				",'file_name':'"+display_name+
				"','uri':'"+uri+
				"','remote_path':'"+remote_path+
				"','local_path':'"+local_path+
				"','username':'"+username+
				"'}";
	}
	
}
