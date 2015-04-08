package com.soco

class InviteActivityController {

    def index() { }
	
	/*
	 * */
	def updateStatusByEmailActivityID(email, aid, status){
		def ret = false
		try{
			def sql = "update InviteActivity set status="+status+" where invitee_email='"+email+"' and activity_id="+aid;
			def cnt = InviteActivity.executeUpdate(sql);
			log.debug("update return "+cnt);
			if(cnt > 0){
				ret = true;
			}else{
				ret = false;
			}
		}catch(Exception e){
			ret = false;
			log.error("<updateStatusByEmailActivityID> error:"+e.getMessage());
		}
		return ret;
	}
	
	def addInviteActivity(InviteActivity ia){
		boolean ret = false;
		try{
			def sql = "from InviteActivity where invitee_email='"+ia.invitee_email+"' and activity_id="+ia.activity_id;
			def iaList = InviteActivity.executeQuery(sql);
			if(iaList.size() == 0 && ia.save()){
				ret = true;
			}
		}catch(Exception e){
			log.error(e.getMessage());
		}
		return ret;
	}
}
