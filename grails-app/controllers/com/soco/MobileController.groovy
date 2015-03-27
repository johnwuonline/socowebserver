package com.soco

import grails.converters.JSON
import org.codehaus.groovy.grails.web.json.JSONObject

class MobileController {
	public static final String SUCCESS = "success";
	public static final String FAIL = "failure";
	
	def springSecurityService

    def index() { 
		render "This is for mobile!"
	}
	
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
	
	def createActivity() {
		def jsonObject = request.JSON
		def activity = new Activity()
		JSONObject answer = activity.createActivity(jsonObject)
		
		if(answer.get("status").equals(MobileController.SUCCESS)){
			if(activity.save()){
				def aid = activity.getId()
				def user_id = springSecurityService.currentUser.id
				def relation = "OWNER"
				def status = "ACCEPT"
				def sql = "from UserActivity where user_id="+user_id+" and activity_id="+aid
				def uaList = UserActivity.executeQuery(sql)
				if(uaList.size() > 0){
					def jsonStr = "{id:'"+aid+"',status:"+MobileController.SUCCESS+"}"
					answer = JSON.parse(jsonStr)
				} else {
					def user_activity = new UserActivity(user_id, aid, relation, status)
					if(user_activity.save() ){
						def jsonStr = "{id:'"+aid+"',status:"+MobileController.SUCCESS+"}"
						answer = JSON.parse(jsonStr)
					}
					else{
						activity.delete(flush:true)
						def jsonStr = "{status:"+MobileController.FAIL+"}"
						answer = JSON.parse(jsonStr)
						log.error("activity save fail.");
					}
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
	
	def updateActivity(){
		JSONObject json = new JSONObject()
		try{
			def jsonObject = request.JSON
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
	
	def achieveActivity(){
		JSONObject json = new JSONObject()
		try{
			def jsonObject = request.JSON
			long aid
			boolean ret
			(ret, aid) = getRequestValueByNameFromJSON(jsonObject, "activity");
			if(ret){
				def sql = "from Activity where id=" + aid
				def activity = Activity.executeQuery(sql)
				if(activity.size() == 1) {
					def achieved = true
					Activity.executeUpdate("update Activity set is_achieved=? where id=?", [achieved,aid])
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
	
	/*
	 * todo
	 * */
	def addUpdateAttributeByActivityID(){
		JSONObject json = new JSONObject()
		try{
			def jsonObject = request.JSON
			long aid;
			boolean ret;
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
								boolean error = false
								JSONObject attr = item
								ActivityAttribute aa = new ActivityAttribute()
								aa.activity_id = aid
								
								if(!error && attr.containsKey("name")){
									aa.name = attr.getString("name");
								} else {
									error = true
									log.debug("No name in requestion json.")
								}
								if(!error && attr.containsKey("index")){
									aa.name_index = attr.getString("index");
								} else {
									error = true
									log.debug("No index in requestion json.")
								}
								if(!error && attr.containsKey("type")){
									aa.type = attr.getString("type");
								} else {
									error = true
									log.debug("No type in requestion json.")
								}
								if(!error && attr.containsKey("value")){
									aa.value = attr.getString("value");
								} else {
									error = true
									log.debug("No value in requestion json.")
								}
								if(!error){
									//chech when existent in db
									sql = "from ActivityAttribute where activity_id="+aa.activity_id+" and name='"+aa.name+"' and name_index="+aa.name_index
									def act_attr = ActivityAttribute.executeQuery(sql)
									//if attribute is existent then update
									if(act_attr.size() > 0){
										ActivityAttribute.executeUpdate("update ActivityAttribute set type = ?, value = ? where activity_id = ? and name = ? and name_index = ?", [aa.type, aa.value, aa.activity_id, aa.name, aa.name_index]);
										json.put("status", MobileController.SUCCESS);
										log.debug("activity attribute update successfully.")
									} else {
										//else add new attribute
										if(aa.save()){
											json.put("status", MobileController.SUCCESS);
											log.debug("activity attribute save successfully.")
										} else {
											json.put("status", MobileController.FAIL);
											log.debug("activity attribute save failed.")
										}
									}
								} else {
								json.put("status", MobileController.FAIL);
									log.debug("there are some errors in request message.")
								}
							} else {
								json.put("status", MobileController.FAIL);
								log.debug(item+" is not JSONObject type.")
							}
						}
					} else {
						json.put("status", MobileController.FAIL);
						json.put("message", "parse attribute failed.");
					}
				} else {
					json.put("status", MobileController.FAIL);
					json.put("message", "activity is not existent.");
				}
			}
		}catch(Exception e){
			log.error(e.message)
			json.put("status", MobileController.FAIL);
		}
		render json
	}
	
	
		
	/*
	 * get activity details by activity id
	 * */
	def getActivityByID(){
		JSONObject json = new JSONObject()
		try{
			long aid;
			boolean ret;
			(ret, aid) = getRequestValueByNameFromJSON(request.JSON, "activity");
			if(ret){
				def sql = "from Activity where id=" + aid
				def activityList = Activity.executeQuery(sql)
				if(activityList.size() == 1){
					json.put("activity", activityList[0].getJsonStr())
					sql = "from ActivityAttribute where activity_id=" + aid
					def actAttrList = ActivityAttribute.executeQuery(sql);
					def actAttrIte = actAttrList.iterator()
					def attrList = new ArrayList();
					actAttrIte.eachWithIndex { item, index ->
						attrList.add(item.toJsonString());
					}
					json.put("attribute", attrList.toString())
					json.put("status", MobileController.SUCCESS);
				} else {
					log.error("The activity number is not 1. It is " + activityList.size())
					json.put("status", MobileController.FAIL);
				}
			} else {
				json.put("status", MobileController.FAIL);
			}
			//
		}catch(Exception e){
			log.error(e.message)
			json.put("status", MobileController.FAIL);
		}
		render json
	}
	
	/* 
	 * get all Activity list by current user
	 *  */
	def getAllActivityByCurrentUser(){
		JSONObject json = new JSONObject()
		try{
			def user_id = springSecurityService.currentUser.id;
			def sql = "select activity_id from UserActivity where user_id="+user_id
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
	
	/*
	 * get contacts of current user
	 * @return a json format response includes all users profile 
	 * */
	def getContactByUserID(){
		JSONObject json = new JSONObject()
		try{
			long uid;
			boolean ret;
			(ret, uid) = getRequestValueByNameFromJSON(request.JSON, "user");
			if(ret){
				def sql = "select friend_id from Friends where user_id=" + uid
				def friends = Friends.executeQuery(sql)
				def f_ite = friends.iterator()
				json.put("count",friends.size())
				f_ite.eachWithIndex { fid, i ->
					def u = User.get(fid)
					json.put("friend"+i, u.getUserJson())
				}
				json.put("status", MobileController.SUCCESS);
			}else{
				json.put("status", MobileController.FAIL);
			}
		}catch(Exception e){
			log.error(e.message)
			json.put("status", MobileController.FAIL);
		}
		render json
	}
	
	/*
	 * add friends
	 * */
	def addFriend(){
		
	}
	
	/*
	 * add recommend friends from contacts
	 * */
	def addRecommendFriends()
	{
		
	}
	
	/**/
}
