package com.soco

import grails.converters.JSON

import org.codehaus.groovy.grails.web.json.JSONObject

import com.fasterxml.jackson.annotation.JsonAnyGetter;

class Projects {
	String name

    static constraints = {
    }
	
	def createProject(JSONObject jsonObj) {
		println jsonObj
		def jsonStr = "{status:'success'}"
		def jsonResp = JSON.parse(jsonStr)
		return jsonResp
	}
}
