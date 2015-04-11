package com.soco

import grails.converters.JSON

class UserController extends grails.plugin.springsecurity.ui.UserController {
	
	def getInforAsJson(){
		response.setContentType("application/json")
		render '[{"a1":"1","a2":"2"},{"b1":"11","b2":"12"}]'
	}
	
	def openSharedActivity(){
		render "Your friend send you an invitation."
	}
	
	def index(){
		
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
			
			//
			[actList:actList, aaList:aaList, userList:userList, uaList:uaList, msgList:msgList, umList:umList]
		}catch(Exception e){
			log.error(e.getMessage());
		}
	}
}
