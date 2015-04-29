package com.soco

import grails.converters.JSON

class UserController extends grails.plugin.springsecurity.ui.UserController {
	
	def springSecurityService;
	
	def getInforAsJson(){
		response.setContentType("application/json")
		render '[{"a1":"1","a2":"2"},{"b1":"11","b2":"12"}]'
	}
	
	def openSharedActivity(){
		def inaid = -1;
		def sig = "";

		inaid = request.getParameter("id");
		sig = request.getParameter("sig");
		
		if(inaid && inaid != -1 && sig && !sig.equals("")){
			def inviteAct = InviteActivity.get(inaid);
			if(inviteAct && inviteAct.signature.equals(sig) && inviteAct.status == 0){
				MobileController mc = new MobileController();
				try{
					def user = (User)springSecurityService.currentUser;
					def user_id = user.getId();
					def aid = inviteAct.activity_id;
					def email = inviteAct.invitee_email;
					if( email.equals(user.email)) {
						InviteActivityController iac = new InviteActivityController();
						def ret = iac.updateStatusByEmailActivityID(email, aid, 1);
						if(ret){
							/*
							 * add activity attribute
							 * */
							UserActivityController uac = new UserActivityController()
							uac.saveUserActivity(user_id, aid, UserActivityController.RELATION_MEMBER, UserActivityController.STATUS_ACCEPT);
							/*
							 * get activity
							 * */
							def sql = "from Activity where id="+aid;
							def current_activity = Activity.executeQuery(sql);
							//
							[current_activity:current_activity];
						}
					}
				}catch(Exception e){
					log.error(e.message);
				}
			}
		}

	}
	
	def index(){
		
	}
	
	def tree(){
		render (view:"tree");
	}
	
	def dashboard(){
		try{
			def sql = "from User";
			def userList = User.executeQuery(sql);
			
			sql = "from Activity";
			def actList = Activity.executeQuery(sql);
			
			sql = "from ActivityAttribute";
			def aaList = ActivityAttribute.executeQuery(sql);
			
			sql = "from UserActivity";
			def uaList = Activity.executeQuery(sql);
			
			sql = "from Message";
			def msgList = Message.executeQuery(sql);
			
			sql = "from UserMessage";
			def umList = UserMessage.executeQuery(sql);
			
			sql = "from InviteActivity";
			def iaList = UserMessage.executeQuery(sql);
			
			//
			def currentUser = springSecurityService.currentUser;
			
			//
			[currentUser:currentUser, actList:actList, aaList:aaList, userList:userList, uaList:uaList, msgList:msgList, umList:umList, iaList:iaList]
		}catch(Exception e){
			log.error(e.getMessage());
		}
	}
}
