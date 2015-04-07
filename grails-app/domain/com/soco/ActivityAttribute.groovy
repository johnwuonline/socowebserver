package com.soco

class ActivityAttribute {
	
	long activity_id
	String name
	String type
	String value
	
	Date create_date
	Date last_modify_date
	long create_user_id
	long last_modify_user_id

    static constraints = {
		activity_id blank:false
		name blank:false
		type blank:false
		value blank:false
    }
	
	def toJsonString(){
		return "{name:'"+name+"',type:'"+type+"',value:'"+value+"'}";
	}
}
