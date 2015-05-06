package com.soco

class ActivityEvent {
	
	long activity_id;
	/* operate type: add, update, delete */
	String event_operate_type;
	/* content type: activity, attribute, file */
	String event_content_type;
	/* content value: json string */
	String event_content_value;

    static constraints = {
    }
}
