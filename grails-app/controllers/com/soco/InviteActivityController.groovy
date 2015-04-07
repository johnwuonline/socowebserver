package com.soco

class InviteActivityController {

    def index() { }
	
	/*
	 * */
	def updateStatusByEmailActivityID(email, aid, status){
		def ret = false
		try{
			def sql = "update InviteActivity set status="+status+" where invitee_email='"+email+"' and activity_id="+aid
			def cnt = InviteActivity.executeUpdate(sql)
			log.debug("update return "+cnt)
			ret = true
		}catch(Exception e){
			ret = false
			log.error("<updateStatusByEmailActivityID> error:"+e.getMessage())
		}
		return ret
	}
}
