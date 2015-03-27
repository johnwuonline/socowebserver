package com.soco

import grails.converters.JSON

class UserController extends grails.plugin.springsecurity.ui.UserController {
	
	def getInforAsJson(){
		response.setContentType("application/json")
		render '[{"a1":"1","a2":"2"},{"b1":"11","b2":"12"}]'
	}
	
}
