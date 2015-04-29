package com.soco

class InviteActivity {
	
	long inviter_id
	String inviter_name
	String invitee_email
	long activity_id
	Date invite_time
	/*
	 * status: 0: not received , 1:accepted, 2:rejected
	 * */
	int status
	String signature;

    static constraints = {
    }
	
	def toJsonString(){
		return "{inviter:'"+this.inviter_name+"',activity:"+this.activity_id+",date:'"+this.invite_time.toString()+"'}"
	}
}
