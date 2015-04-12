package com.soco

class UserMessageController {
	
	static final int STATUS_NOT_SENT = 0;
	static final int STATUS_SENT = 1;
	
	static final int MAX_MSG_EACH_PACKAGE = 10;

    def index() { }
	
	
	def getUserMsgByUserID(uid){
		def msgList = new ArrayList();
		try{
			def sql = "from UserMessage where to_user_id="+uid+" and status="+STATUS_NOT_SENT;
			def umList = UserMessage.executeQuery(sql);
			umList.eachWithIndex { item, index ->
				if(index < MAX_MSG_EACH_PACKAGE){
					def um = (UserMessage)item;
					def msg = Message.get(um.message_id);
					msgList.add(msg.toJsonString("signature",um.signature));
					//
					sql = "update UserMessage set status="+STATUS_SENT+" where id="+um.getId();
					UserMessage.executeUpdate(sql);
				}
			}
		}catch(Exception e){
			log.error(e.getMessage());
		}
		return msgList.toString();
	}
	
	def getNumOfUserMsgByUserID(uid){
		def num = 0;
		try{
			def sql = "from UserMessage where to_user_id="+uid+" and status="+STATUS_NOT_SENT;
			def umList = UserMessage.executeQuery(sql);
			num = umList.size();
		}catch(Exception e){
			log.error(e.getMessage());
		}
		return num;
	}
}
