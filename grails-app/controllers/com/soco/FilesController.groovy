package com.soco

import org.codehaus.groovy.grails.web.json.JSONObject

class FilesController {

    def index() { }
	
	def getFileByFileID(fid){
		def jsonStr = "{'status':"+MobileController.FAIL+"}";
		try{
			def sql = "from Files where id=" + fid;
			def fList = Files.executeQuery(sql)
			if(fList.size() == 1){
				Files file = fList[0];
				jsonStr = file.toJsonString();
				
			} else {
				log.error("The activity number is not 1. It is " + fList.size());
			}
			//
		}catch(Exception e){
			log.error(e.message)
		}
		return jsonStr;
	}
}
