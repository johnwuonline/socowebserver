package com.soco

class ActivityFile {
	long activity_id;
	long file_id;

    static constraints = {
    }
	
	def toJsonString(){
		return "{'activity_id':"+activity_id+", 'file_id':"+file_id+"}";
	}
}
