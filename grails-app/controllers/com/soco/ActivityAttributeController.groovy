package com.soco

class ActivityAttributeController {

    def index() { }
	
	
	boolean updateAttribute(aid, name, type, old_value, new_value, uid){
		boolean ret = false;
		try{
			String sql = "from ActivityAttribute where activity_id="+aid+" and name='"+name+"' and type='"+type+"' and value='"+old_value+"'"
			def aaList = ActivityAttribute.executeQuery(sql)
			if(aaList.size() == 1){
				Date date = new Date()
				String modify_date = date.format("yyyy-MM-dd HH:mm:ss");
				long aaid = aaList[0].getId()
				int cnt = ActivityAttribute.executeUpdate("update ActivityAttribute set value = ?, last_modify_date= ?, last_modify_user_id=? where id = ?",
					 										[new_value, date, uid, aaid]);
				log.debug("activity attribute update successfully.");
				ret = true;
			}else{
				log.error("<updateAttribute> activity attribute is not existent for activity id:"+aid+" and name:"+name);
				ret = false;
			}
		}catch(Exception e){
			log.error(e.getMessage())
		}
		return ret;
	}
	
	boolean deleteAttribute(aid, name, type, value){
		boolean ret = false;
		try{
			String sql = "from ActivityAttribute where activity_id="+aid+" and name='"+name+"' and type='"+type+"' and value='"+value+"'"
			def aaList = ActivityAttribute.executeQuery(sql)
			if(aaList.size() == 1){
				long aaid = aaList[0].getId()
				int cnt = ActivityAttribute.executeUpdate("delete ActivityAttribute where id = ?", [aaid]);
				log.debug("activity attribute delete successfully.");
				ret = true;
			}else{
				log.error("<deleteAttribute> activity attribute is not existent for activity id:"+aid+" and name:"+name);
				ret = false;
			}
		}catch(Exception e){
			log.error(e.getMessage())
		}
		return ret;
	}
}
