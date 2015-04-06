package com.soco

class User {

	transient springSecurityService

	String username
	String password
	String plainPassword
	String email
	String mobilePhone
	Date   createDate
	Date   lastLoginTime
	
	boolean enabled = true
	boolean accountExpired
	boolean accountLocked
	boolean passwordExpired

	static transients = ['springSecurityService']

	static constraints = {
		username blank: false, unique: true
		password blank: false
		email blank: true, nullable: true
		plainPassword blank: true, nullable: true
		mobilePhone blank: true, nullable: true
		createDate blank: true, nullable: true
		lastLoginTime blank: true, nullable: true
	}

	static mapping = {
		password column: '`password`'
	}

	Set<Role> getAuthorities() {
		UserRole.findAllByUser(this).collect { it.role }
	}

	def beforeInsert() {
		encodePassword()
	}

	def beforeUpdate() {
		if (isDirty('password')) {
			encodePassword()
		}
	}

	protected void encodePassword() {
		password = springSecurityService?.passwordEncoder ? springSecurityService.encodePassword(password) : password
	}
	
	def setCreateDate(Date date){
		createDate = date
	}
	
	def setPlainPassword(String password){
		plainPassword = password
	}
	
	def setMobilePhone(String mp){
		mobilePhone = mp
	}
	
	def setLastLoginTime(Date lltime){
		lastLoginTime = lltime
	}
	
	def getUserJson(){
		return "{'id':"+this.getId()+",'name':'"+this.username+"','email':'"+this.email+"'}"
	}
	
	
}
