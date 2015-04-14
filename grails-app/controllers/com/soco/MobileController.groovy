package com.soco

import grails.converters.JSON
import org.codehaus.groovy.grails.web.json.JSONObject
import grails.plugin.springsecurity.SpringSecurityUtils

class MobileController {
	public static final String SUCCESS = "success";
	public static final String FAIL = "failure";
	
	def springSecurityService
	def mailService

    def index() { 
		render "This is for mobile!"
	}
	
	/*
	 * Convenient to change request.JSON format
	 * */
	def getRequestJSON(){
		return request.JSON
	}
	
	/*
	 * fetch value from json by key
	 * */
	def getRequestValueByNameFromJSON(JSONObject json, String name){
		def value = null;
		def ret = false;
		try{
			if(json instanceof JSONObject && json.containsKey(name)){
				value = json.get(name);
				log.debug(name + ":" + value)
				ret = true
			}
		} catch (Exception e){
			log.error(e.message)
		}
		return [ret,value]
	}
	
	/* createActivity
	 * @param in
	   {
		  "name":"activity1",
		  "type":"activity",
		  "tag":"public",
		  "signature":"123sdf"
		}
		@return 
		{ id:’1’, status:’succcess’ }
		OR
		{ status:’failure’ }
	 * */
	def createActivity() {
		def jsonObject = getRequestJSON()
		def activity = new Activity()
		JSONObject answer = activity.parseJsonObject(jsonObject)
		
		if(answer.get("status").equals(MobileController.SUCCESS)){
			if(activity.save()){
				def aid = activity.getId()
				def user_id = springSecurityService.currentUser.id
				def relation = UserActivityController.RELATION_OWN
				def status = UserActivityController.STATUS_ACCEPT
				
				UserActivityController uac = new UserActivityController();
				boolean ret = uac.saveUserActivity(user_id, aid, relation, status);
				if(ret){
					def jsonStr = "{id:'"+aid+"',status:"+MobileController.SUCCESS+"}"
					answer = JSON.parse(jsonStr)
				} else {
					activity.delete(flush:true)
					def jsonStr = "{status:"+MobileController.FAIL+"}"
					answer = JSON.parse(jsonStr)
					log.error("activity save fail.");
				}
				
			}
			else{
				def jsonStr = "{status:"+MobileController.FAIL+"}"
				answer = JSON.parse(jsonStr)
				log.error("activity save fail.");
			}
		} else {
			log.error("activity save fail.");
		}
		
		render answer
	}
	
	/* updateActivity
	 * @param in
	 	{
		  "name":"activity2",
		  "activity":1
		}
	   @return
		{ status:’succcess’ }
		OR
		{ status:’failure’ }
	 * */
	def updateActivity(){
		JSONObject json = new JSONObject()
		try{
			def jsonObject = getRequestJSON()
			long aid
			def name
			boolean ret1, ret2;
			(ret1, aid) = getRequestValueByNameFromJSON(jsonObject, "activity");
			(ret2, name) = getRequestValueByNameFromJSON(jsonObject, "name");
			if(ret1){
				def sql = "from Activity where id=" + aid
				def activity = Activity.executeQuery(sql)
				if(ret2 && activity.size() == 1) {
					Activity.executeUpdate("update Activity set name=? where id=?", [name,aid])
					json.put("status", MobileController.SUCCESS);
				} else {
					json.put("status", MobileController.FAIL);
					json.put("message", "activity is not existent.");
				}
			} else {
				json.put("status", MobileController.FAIL);
				json.put("message", "activity key is not existent in request message.");
			}
		}catch(Exception e){
			log.error(e.getMessage())
			json.put("status", MobileController.FAIL);
		}
		render json
	}
	
	/* archiveActivity
	 * @param in
	 	{ "activity":2 }
	 	
		@return
		{ status:’succcess’ }
		OR
		{ status:’failure’ }
	 * */
	def archiveActivity(){
		JSONObject json = new JSONObject()
		try{
			def jsonObject = getRequestJSON()
			long aid
			boolean ret
			(ret, aid) = getRequestValueByNameFromJSON(jsonObject, "activity");
			if(ret){
				def sql = "from Activity where id=" + aid
				def activity = Activity.executeQuery(sql)
				if(activity.size() == 1) {
					def achieved = true
					Activity.executeUpdate("update Activity set is_archived=? where id=?", [achieved,aid])
					json.put("status", MobileController.SUCCESS);
				} else {
					json.put("status", MobileController.FAIL);
					json.put("message", "activity is not existent.");
				}
			} else {
				json.put("status", MobileController.FAIL);
				json.put("message", "activity key is not existent in request message.");
			}
		}catch(Exception e){
			log.error(e.getMessage())
			json.put("status", MobileController.FAIL);
		}
		render json
	}
	
	/* addAttributeByActivityID
	 * @param in
	 	{
		  "activity":1,
		  "attribute":[{name:location,type:string,value:'hong kong'},
		  			   {name:'start time',type:string,value:'2015-03-23'}]
		}
		
		@return
		{ status:’succcess’ }
		OR
		{ status:’failure’,attribute:[{name:'Name', type:'String', value:'v'},{...}] }
	 * */
	def addAttributeByActivityID(){
		JSONObject json = new JSONObject();
		try{
			def jsonObject = getRequestJSON();
			long aid;
			def user_id = springSecurityService.currentUser.id;
			boolean ret;
			def attrList = new ArrayList();
			(ret, aid) = getRequestValueByNameFromJSON(jsonObject, "activity");
			if(ret){
				def sql = "from Activity where id=" + aid;
				def activity = Activity.executeQuery(sql);
				if(activity.size() == 1) {
					def attributes;
					(ret, attributes) = getRequestValueByNameFromJSON(jsonObject, "attribute");
					if(ret){
						log.debug("attributes size:"+attributes.size());
						attributes.eachWithIndex { item, index ->
							if(item instanceof JSONObject){
								ActivityAttribute aa = new ActivityAttribute();
								if(aa.parseJsonObject(item)){
									sql = "from ActivityAttribute where activity_id="+aid+" and name='"+aa.name+"' and type='"+aa.type+"' and value='"+aa.value+"'";
									def aaList = ActivityAttribute.executeQuery(sql);
									if(aaList.size() == 0){
										aa.activity_id = aid;
										aa.create_date = new Date();
										aa.last_modify_date = new Date();
										aa.create_user_id = user_id;
										aa.last_modify_user_id = user_id;
										if(aa.save()){
											log.debug("activity attribute save successfully.activity id:"+aid+", name:"+aa.name);
										}else{
											ret = false;
										}
									}else{
										ret = false;
									}
									/*
									ActivityAttributeController aac = new ActivityAttributeController()
									if(aac.addAttribute(aid, aa.name, aa.type, aa.value, user_id)){
										log.debug("activity attribute save successfully.activity id:"+aid+", name:"+aa.name);
									}else{
										ret = false;
										attrList.add(aa.toJsonString());
										log.debug("activity attribute save failed. activity id:"+aid+", name:"+aa.name);
									}
									*/
								} else {
									ret = false;
									log.debug("there are some errors in request message.activity id:"+aid)
								}
							} else {
								ret = false;
								log.debug(item+" is not JSONObject type.activity id:"+aid)
							}
						}
					} else {
						json.put("message", "parse attribute failed.activity id:"+aid);
					}
				} else {
					json.put("message", "activity is not existent.activity id:"+aid);
				}
			}
			//
			if(ret){
				json.put("status", MobileController.SUCCESS);
			}else{
				json.put("status", MobileController.FAIL);
				json.put("attribute", attrList.toString());
			}
		}catch(Exception e){
			log.error(e.message)
			json.put("status", MobileController.FAIL);
		}
		render json
	}
	
	/* updateAttributeByActivityID
	 * @param in
		 {
		  "activity":1,
		  "attribute":[{name:location,type:string,value:’hong kong’,new_value:’hong kong, china'}]
		}
		
		@return
		{ status:’succcess’ }
		OR
		{ status:’failure’,attribute:[{name:'Name', type:'String', value:'v'},{...}] }
	 * */
	def updateAttributeByActivityID(){
		JSONObject json = new JSONObject()
		try{
			def jsonObject = getRequestJSON()
			def user_id = springSecurityService.currentUser.id
			long aid;
			boolean ret;
			def attrList = new ArrayList();
			(ret, aid) = getRequestValueByNameFromJSON(jsonObject, "activity");
			if(ret){
				def sql = "from Activity where id=" + aid
				def activity = Activity.executeQuery(sql)
				if(activity.size() == 1) {
					def attributes;
					(ret, attributes) = getRequestValueByNameFromJSON(jsonObject, "attribute")
					if(ret){
						log.debug("attributes size:"+attributes.size())
						attributes.eachWithIndex { item, index ->
							if(item instanceof JSONObject){
								ActivityAttribute aa = new ActivityAttribute()
								if(aa.parseJsonObject(item)){
									def new_value;
									def flag;
									aa.activity_id = aid;
									(flag, new_value) = getRequestValueByNameFromJSON(item, "new_value")
									if(flag){
										ActivityAttributeController aac = new ActivityAttributeController()
										if(aac.updateAttribute(aa.activity_id, aa.name, aa.type, aa.value, new_value, user_id)){
											log.debug("activity attribute update successfully.activity id:"+aa.activity_id+", name:"+aa.name);
										}else{
											ret = false;
											attrList.add(aa.toJsonString());
											log.debug("activity attribute update failed. activity id:"+aa.activity_id+", name:"+aa.name);
										}
									}else{
										ret = false;
										log.debug("there are no new_value in request message.activity id:"+aid)
									}
								}else {
									ret = false;
									log.debug("there are some errors in request message.activity id:"+aid)
								}
							} else {
								ret = false;
								log.debug(item+" is not JSONObject type.activity id:"+aid)
							}
						}
					} else {
						json.put("message", "parse attribute failed.activity id:"+aid);
					}
				} else {
					json.put("message", "activity is not existent.activity id:"+aid);
				}
			}
			//
			if(ret){
				json.put("status", MobileController.SUCCESS);
			}else{
				json.put("status", MobileController.FAIL);
				json.put("attribute", attrList.toString());
			}
		}catch(Exception e){
			log.error(e.message)
			json.put("status", MobileController.FAIL);
		}
		render json
	}
	
	/* deleteAttributeByActivityID
	 * @param in
		 {
		  "activity":1,
		  "attribute":[{name:location,type:string,value:’hong kong’}]
		}
		
		@return
		{ status:’succcess’ }
		OR
		{ status:’failure’,attribute:[{name:'Name', type:'String', value:'v'},{...}] }
	 * */
	def deleteAttributeByActivityID(){
		JSONObject json = new JSONObject()
		try{
			def jsonObject = getRequestJSON()
			def user_id = springSecurityService.currentUser.id
			long aid;
			boolean ret;
			def attrList = new ArrayList();
			(ret, aid) = getRequestValueByNameFromJSON(jsonObject, "activity");
			if(ret){
				def sql = "from Activity where id=" + aid
				def activity = Activity.executeQuery(sql)
				if(activity.size() == 1) {
					def attributes;
					(ret, attributes) = getRequestValueByNameFromJSON(jsonObject, "attribute")
					if(ret){
						log.debug("attributes size:"+attributes.size())
						attributes.eachWithIndex { item, index ->
							if(item instanceof JSONObject){
								ActivityAttribute aa = new ActivityAttribute()
								if(aa.parseJsonObject(item)){
									ActivityAttributeController aac = new ActivityAttributeController()
									aa.activity_id = aid;
									if(aac.deleteAttribute(aa.activity_id, aa.name, aa.type, aa.value)){
										log.debug("activity attribute update successfully.activity id:"+aa.activity_id+", name:"+aa.name);
									}else{
										ret = false;
										attrList.add(aa.toJsonString());
										log.debug("activity attribute delete failed. activity id:"+aa.activity_id+", name:"+aa.name);
									}
								}else {
									ret = false;
									log.debug("there are some errors in request message.activity id:"+aid)
								}
							} else {
								ret = false;
								log.debug(item+" is not JSONObject type.activity id:"+aid)
							}
						}
					} else {
						json.put("message", "parse attribute failed.activity id:"+aid);
					}
				} else {
					json.put("message", "activity is not existent.activity id:"+aid);
				}
			}
			//
			if(ret){
				json.put("status", MobileController.SUCCESS);
			}else{
				json.put("status", MobileController.FAIL);
				json.put("attribute", attrList.toString());
			}
		}catch(Exception e){
			log.error(e.message)
			json.put("status", MobileController.FAIL);
		}
		render json
	}
		
	/* getActivityByID
	 * get activity details by activity id
	 * @param in 
	 	{
		  "activity":1
		}
		
		@return
		{
			"status":"success",
			"attributes":"[ {name:location,index:49,type:string,value:hong kong}, 
							{name:start time,index:49,type:string,value:2015-03-23}, 
							{name:end time,index:49,type:string,value:2015-04-11}]”,
			"activity":"{name:test1,tag:2015-02-25,signature:123sdf,type:public,is_achieved:false}"
		}
		OR
		{
			status:’failure’
		}
	 * */
	def getActivityByID(){
		JSONObject json = null
		long aid;
		boolean ret;
		(ret, aid) = getRequestValueByNameFromJSON(getRequestJSON(), "activity");
		if(ret){
			ActivityController ac = new ActivityController()
			json = ac.getActivityByID(aid)
		} else {
			json = new JSONObject()
			json.put("status", MobileController.FAIL);
		}
			
		render json
	}
	
	/* getAllActivityByCurrentUser
	 * get all Activity list by current user
	 * 
	 * @param in NONE
	 * 
	 * @return {"projects":"8,9,10,11,12","status":"success"}
	 *  */
	def getAllActivityByCurrentUser(){
		JSONObject json = new JSONObject()
		try{
			def uid = springSecurityService.currentUser.id;
			def sql = "select activity_id from UserActivity where user_id="+uid
			def activity_list = UserActivity.executeQuery(sql);
			def str = activity_list.join(",");
			json.put("status", MobileController.SUCCESS);
			json.put("activity", str);
		}catch(Exception e){
			log.error(e.message)
			json.put("status", MobileController.FAIL);
		}
		render json
	}
	
	/* getAllFriendsForCurrentUser
	 * get contacts of current user
	 * @param in NONE
	 * @return a json format response includes all users profile 
	 	{ “friends”:"[{'id':1,'name':john,'email':'test@gmail.com'}]","status":"success" }
		OR
		{ status:’failure’ }
	 * */
	def getAllFriendsForCurrentUser(){
		JSONObject json = new JSONObject()
		try{
			long uid = springSecurityService.currentUser.id;
			def sql = "select friend_id from Friends where user_id=" + uid
			def friends = Friends.executeQuery(sql)
			def f_ite = friends.iterator()
			def attrList = new ArrayList();
			f_ite.eachWithIndex { fid, i ->
				def user = User.get(fid)
				attrList.add(user.getUserJson());
			}
			json.put("friends", attrList.toString())
			json.put("status", MobileController.SUCCESS);
			
		}catch(Exception e){
			log.error(e.message)
			json.put("status", MobileController.FAIL);
		}
		render json
	}
	
	/*getCurrentUserContacts
	 * @param in
	 	{  activity:1 }
		@return
		{
			“status”:”success",
			"friend":"[{'id':1,'name':'john','email':'wjmail@gmail.com'}]"
		}
		OR
		{ status:’failure’ }
	 * */
	def getCurrentUserContacts(){
		JSONObject json = new JSONObject()
		try{
			def uid = springSecurityService.currentUser.id;
			def sql = "select friend_id from Friends where user_id=" + uid;
			def friends = Friends.executeQuery(sql);
			def f_ite = friends.iterator();
			def fList = new ArrayList();
			f_ite.eachWithIndex { fid, i ->
				def u = User.get(fid);
				fList.add(u.getUserJson());
			}
			json.put("friend",fList.toString());
			json.put("status", MobileController.SUCCESS);
			
		}catch(Exception e){
			log.error(e.message);
			json.put("status", MobileController.FAIL);
		}
		render json;
	}
	
	/* addFriend
	 * @param in {email:"test@test.com"}
	 * @return 	{ status:’succcess’} OR { status:’failure’ }
	 * */
	def addFriend(){
		JSONObject json = new JSONObject()
		try{
			def email;
			boolean ret;
			(ret, email) = getRequestValueByNameFromJSON(getRequestJSON(), "email");
			if(ret){
				def user_id = springSecurityService.currentUser.id;
				def sql = "from User where email='"+email+"'"
				def userList = User.executeQuery(sql)
				if(userList.size() > 0){
					def user = userList.get(0);
					def fid = user.getId()
					log.debug("friend id:" + fid)
					Friends f = new Friends()
					f.user_id = user_id
					f.friend_id = fid
					if(f.save()){
						json.put("status", MobileController.SUCCESS);
					} else {
						json.put("status", MobileController.FAIL);
						log.error("friend save failed for friend id:" + fid)
					}
				} else {
					json.put("status", MobileController.FAIL);
					log.error("there is no user for email:"+email)
				}
			}else{
				json.put("status", MobileController.FAIL);
				log.error("there is error in request json message.")
			}
		}catch(Exception e){
			log.error(e.message)
			json.put("status", MobileController.FAIL);
		}
		render json
	}
	
	/*
	 * add recommend friends from contacts
	 * */
	def addRecommendFriends()
	{
		
	}
	
	/*queryUserByEmailOrUsername
	 * search user when add a friend
	 * @param in { email:"test@test.com" } or { name: "john" }
	 * @return a user json format
	 	{“status”:"success","user":"{'id':1,'name':john,'email':"test@gmail.com}"}
		Or
		{ status:’failure’ }
	 * */
	def queryUserByEmailOrUsername()
	{
		JSONObject json = new JSONObject()
		try{
			def email;
			def sql = "";
			boolean ret;
			(ret, email) = getRequestValueByNameFromJSON(getRequestJSON(), "email");
			if(ret){
				sql = "from User where email='"+email+"'"
			}else{
				def username
				(ret, username) = getRequestValueByNameFromJSON(getRequestJSON(), "name");
				if(ret){
					sql = "from User where username='"+username+"'"
				}
			}
			if(sql != ""){
				def userList = User.executeQuery(sql)
				if(userList.size() > 0){
					def user = userList.get(0);
					json.put("user", user.getUserJson());
					json.put("status", MobileController.SUCCESS);
				} else {
					json.put("status", MobileController.FAIL);
					log.error("not find the user")
				}
			} else {
				json.put("status", MobileController.FAIL);
				log.error("there is no email or name in request json message.")
			}
		}catch(Exception e){
			log.error(e.message)
			json.put("status", MobileController.FAIL);
		}
		render json
	}
	
	/* inviteFriendShareActivity
	 * @param in { email:"test@test.com", activity: 1 } or { name: "john", activity: 1}
	 * @return a user json format
	 	{ status:’succcess’ } 
		OR
		{ status:’failure’ }
	 * */
	def inviteFriendShareActivity(){
		JSONObject json = new JSONObject()
		try{
			def email = "";
			def username = ""
			def sql = "";
			boolean ret;
			def user = (User)springSecurityService.currentUser
			def user_id = user.getId();
			
			(ret, email) = getRequestValueByNameFromJSON(getRequestJSON(), "email");
			if(ret){
				//sql = "from User where email='"+email+"'"
				username = email
			}else{
				(ret, username) = getRequestValueByNameFromJSON(getRequestJSON(), "name");
				if(!ret){
					//sql = "from User where username='"+username+"'"
					username = ""
				}
			}
			def aid;
			(ret, aid) = getRequestValueByNameFromJSON(getRequestJSON(), "activity");
			User invitee = User.findByUsernameOrEmail(username,username);
			if(ret){
				//save
				InviteActivity inact = new InviteActivity();
				InviteActivityController iac = new InviteActivityController();
				inact.inviter_id = user_id;
				inact.inviter_name = user.username;
				inact.activity_id = aid;
				inact.invite_time = new Date();
				inact.status = 0;
				if(invitee){
					// send an invitation in heartbeat
					inact.invitee_email = invitee.email;
					if(iac.addInviteActivity(inact)){
						json.put("status", MobileController.SUCCESS);
					}else{
						json.put("status", MobileController.FAIL);
					}
				} else {
					// not a user, then send out an email
					if(email != ""){
						inact.invitee_email = email
						if(iac.addInviteActivity(inact)){
							def conf = SpringSecurityUtils.securityConfig
							def body = "hi, please click http://localhost:8090/socoserver/user/openSharedActivity to see the invatation."
							def subjectStr = "An invitation from your friend"
							mailService.sendMail {
								to email
								from conf.ui.register.emailFrom
								subject subjectStr
								html body
							}
							
							json.put("status", MobileController.SUCCESS);
						}else{
							json.put("status", MobileController.FAIL);
						}
					} else {
						ret = false
						json.put("status", MobileController.FAIL);
						log.error("not find the user and no email")
					}
				}
				//
			} else {
				json.put("status", MobileController.FAIL);
				log.error("there is no email or name in request json message.")
			}
		}catch(Exception e){
			log.error(e.message)
			json.put("status", MobileController.FAIL);
		}
		render json
	}
	
	/*HeartBeat
	 * @param in NONE
	 * @return 
	 	{
	 		"status":"success",
	 		"invitation":"[ {inviter:'',activity:1,date:'2015-04-06 11:17:33.0'}, 
	 						{inviter:'abc123',activity:1,date:'2015-04-06 19:09:42.0'}]",
			"message": "true"
		}
	 * */
	def HeartBeat(){
		JSONObject json = new JSONObject()
		try{
			def user = (User)springSecurityService.currentUser;
			def user_id = user.getId()
			/*
			 * save heart beat information into db
			 * */
			HeartBeat hb = new HeartBeat()
			hb.user_id = user_id
			hb.user_ip = "127.0.0.1"
			hb.user_port = 0
			hb.datetime = new Date()
			if(hb.save()){
				json.put("status", MobileController.SUCCESS);
			}else{
				Date d = new Date()
				log.error("["+d.toString()+"] <HeartBeat>: save error. For user id:"+user_id)
				json.put("status", MobileController.FAIL);
			}
			/* check the invite activity table and infor user invitation
			 * { invitation: [{inviter:"john", activity: 1, date:"2015-04-05 12:12:12"}, ... ]
			 * */
			def user_email = user.email
			def sql = "from InviteActivity where invitee_email='"+user_email+"' and status=0"
			def inviteList = InviteActivity.executeQuery(sql)
			def inviteAList = new ArrayList();
			inviteList.eachWithIndex { item, index->
				inviteAList.add(item.toJsonString())
			}
			if(inviteAList.size() > 0){
				json.put("invitation", inviteAList.toString())
			}
			/* check user message table and infor user
			 * { message: "true" }
			 * */
			UserMessageController umc = new UserMessageController();
			if(umc.getNumOfUserMsgByUserID(user_id) > 0){
				json.put("message","true");
			}
			
		}catch(Exception e){
			log.error(e.message)
			json.put("status", MobileController.FAIL);
		}
		render json
	}
	
	/* joinActivityByInvite
	 * @param in {activity:1}
	 * @return 
	 	{"status":"success",
	 	 "attributes":"[{name:'location',type:'string',value:'hong kong'}, 
	 	 				{name:'start time',type:'string',value:'2015-03-23'}, 
	 	 				{name:'end time’,type:'string',value:'2015-04-11'}]",
			"activity":"{name:'test1',tag:'2015-02-25',signature:'123sdf',type:'public',is_archived:false}"
		}
		OR
		 { status:’failure’ }
	 * */
	def joinActivityByInvite(){
		JSONObject json = null;
		try{
			def user = (User)springSecurityService.currentUser;
			def user_id = user.getId();
			def email = user.email;
			def aid;
			boolean ret;
			(ret, aid) = getRequestValueByNameFromJSON(getRequestJSON(), "activity");
			if(ret){
				InviteActivityController iac = new InviteActivityController();
				ret = iac.updateStatusByEmailActivityID(email, aid, 1);
				if(ret){
					/*
					 * add activity attribute
					 * */
					UserActivityController uac = new UserActivityController()
					uac.saveUserActivity(user_id, aid, UserActivityController.RELATION_MEMBER, UserActivityController.STATUS_ACCEPT);
					/*
					 * get activity
					 * */
					ActivityController ac = new ActivityController();
					json = ac.getActivityByID(aid);
				}else{
					json = new JSONObject();
					json.put("status", MobileController.FAIL);
				}
			}else{
				json = new JSONObject();
				json.put("status", MobileController.FAIL);
			}
		}catch(Exception e){
			log.error(e.message);
			json = new JSONObject();
			json.put("status", MobileController.FAIL);
		}
		render json;
	}
}
