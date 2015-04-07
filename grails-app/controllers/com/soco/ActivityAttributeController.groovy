package com.soco

class ActivityAttributeController {

    def index() { }
	
	def addAttribute(aid, name, type, value){
		def ret
		try{
			def sql = "from ActivityAttribute where aid="+aid+" and name='"+name+"'"
			def aa = ActivityAttribute.executeQuery(sql)
			def name_index = aa.size() + 1
			sql = "insert into ActivityAttribute(activity_id,name,name_index,type,value) " 
				  + "values("+aid+",'"+name+"',"+name_index+",'"+type+"','"+value+"')"
			def cnt = ActivityAttribute.executeUpdate(sql)
			ret = name_index
		}catch(Exception e){
			log.error(e.getMessage())
		}
		return ret
	}
}
