package com.soco

class ActivityEvent {
	
	long user_id;
	long activity_id;
	/* operate type: add, update, delete */
	String event_operate_type;
	/* content type: activity, attribute, file */
	String event_content_type;
	/* content value: json string */
	String event_content_value;
	//
	String signature;

    static constraints = {
		signature unique: true
    }
	
	String toJsonString(){
		return "{'activity_id':"+activity_id+
				",'event_operate_type':'"+event_operate_type+
				"','event_content_type':'"+event_content_type+
				"','value':"+event_content_value+
				",'signature':'"+signature+
				"'}";
	}
}
