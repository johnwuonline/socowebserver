package com.soco

import socowebserver.Utility;

class MessageController {
	
	public static final int TYPE_USER_NAME = 0;
	public static final int TYPE_USER_EMAIL = 1;
	public static final int TYPE_ACTIVITY_ID = 2;
	
	public static final int TYPE_CONTEXT_STRING = 0;
	public static final int TYPE_CONTEXT_IMAGE = 1;

    def index() { }
	
	
	def convert2UserMsg(Message msg, currentUserID){
		def msgList = new ArrayList();
		
		switch (msg.to_type) {
			case MessageController.TYPE_USER_NAME:
			case MessageController.TYPE_USER_EMAIL:
				User user = User.findByUsernameOrEmail(msg.to_id, msg.to_id);
				if(user){
					UserMessage um = new UserMessage();
					um.from_type = msg.from_type;
					um.from_id = msg.from_id;
					um.to_user_id = user.getId();
					um.message_id = msg.getId();
					um.status = UserMessageController.STATUS_NOT_SENT;
					def md5Str = msg.context_type.toString() + msg.context + msg.send_date_time;
					um.signature = Utility.getMD5(md5Str);
					msgList.add(um);
				}
				break;
			case MessageController.TYPE_ACTIVITY_ID:
				UserActivityController uac = new UserActivityController();
				def userList = uac.getUsersByActivityID(msg.to_id.toLong());
				userList.eachWithIndex { item, index ->
					def ua = (UserActivity)item;
					if(ua.user_id != currentUserID){
						UserMessage um = new UserMessage();
						um.from_type = msg.from_type;
						um.from_id = msg.from_id;
						um.to_user_id = ua.user_id;
						um.message_id = msg.getId();
						um.status = UserMessageController.STATUS_NOT_SENT;
						def md5Str = msg.context_type.toString() + msg.context + msg.send_date_time;
						um.signature = Utility.getMD5(md5Str);
						msgList.add(um);
					}
				}
				break;
			default:
				break;
		}
		return msgList;
	}
}
