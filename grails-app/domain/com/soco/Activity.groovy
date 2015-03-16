package com.soco

class Activity {
	
	long project_id;
	
	String loc_latitude;
	String loc_longitude;
	String loc_name;

    static constraints = {
    }
	
	def getJsonStr(){
		return "{'id':"+this.getId()+",'project':"+this.project_id+",'name':"+this.loc_name+",'longitude':"+this.loc_longitude+",'latitude':"+this.loc_latitude+"}"
	}
}
