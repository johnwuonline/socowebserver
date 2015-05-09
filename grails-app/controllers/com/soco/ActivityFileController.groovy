package com.soco

class ActivityFileController {

    def index() { }
	
	def getFileJsonStrByActivityID(aid){
		def afList = new ArrayList();
		try{
			def sql = "from ActivityFile where activity_id="+aid;
			def fileList = ActivityFile.executeQuery(sql);
			
			fileList.eachWithIndex { item, index ->
				def af = (ActivityFile)item;
				afList.add(af.toJsonString());
			}
		}catch(Exception e){
			log.error(e.getMessage());
		}
		return afList.toString();
	}
}
