package com.soco

class MessageController {
	
	public static final int TYPE_USER_NAME = 0;
	public static final int TYPE_USER_EMAIL = 1;
	public static final int TYPE_ACTIVITY_ID = 2;
	
	public static final int TYPE_CONTEXT_STRING = 0;
	public static final int TYPE_CONTEXT_IMAGE = 1;

    def index() { }
	
	
	def convert2UserMsg(Message msg){
		
		UserMessage um = new UserMessage();
		um.from_type = msg.from_type;
		um.from_id = msg.from_id;
		switch (msg.to_type) {
			case MessageController.TYPE_USER_NAME:
			case MessageController.TYPE_USER_EMAIL:
				User user = User.findByUsernameOrEmail(msg.to_id, msg.to_id);
				if(user){
					um.to_user_id = user.getId();
				}
				break;
			case MessageController.TYPE_ACTIVITY_ID:
				def userList = UserActivityController.getUsersByActivityID(msg.to_id.toBigInteger());
				break;
			default:
				break;
		}
		
	}
}
