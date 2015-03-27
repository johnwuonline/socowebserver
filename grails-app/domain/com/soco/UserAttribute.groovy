package com.soco

class UserAttribute {
	
	long user_id;
	String name
	int name_index
	String type
	String value

    static constraints = {
		name blank:false
		name_index blank:false
		type blank:false
		value blank:false
    }
	
	
}
