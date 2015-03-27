package com.soco

class ActivityAttribute {
	
	long activity_id
	String name
	int name_index
	String type
	String value

    static constraints = {
		activity_id blank:false
		name blank:false
		name_index blank:false
		type blank:false
		value blank:false
    }
	
	def toJsonString(){
		return "{name:"+name+",index:"+name_index+",type:"+type+",value:"+value+"}";
	}
}
