package com.soco

class Friends {
	long user_id
	long friend_id

    static constraints = {
		user_id blank:false
		friend_id blank:false
    }
}
