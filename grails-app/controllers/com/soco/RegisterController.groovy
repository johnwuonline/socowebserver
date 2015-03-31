package com.soco

import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.authentication.dao.NullSaltSource
import grails.plugin.springsecurity.ui.RegistrationCode

class RegisterController extends grails.plugin.springsecurity.ui.RegisterController {
	

	def mobileRegister(RegisterCommand command) {
		if (command.hasErrors()) {
			//render view: 'index', model: [command: command]
			def jsonStr = '"username":"'+command.username+'","status":"failure"'
			if (command.errors.hasFieldErrors('username')) {
				jsonStr = jsonStr + ',"username":"error"' 
			}
			if (command.errors.hasFieldErrors('email')) {
				jsonStr = jsonStr + ',"email":"error"'
			}
			if (command.errors.hasFieldErrors('password')) {
				jsonStr = jsonStr + ',"password":"error"'
			}
			if (command.errors.hasFieldErrors('password2')) {
				jsonStr = jsonStr + ',"password2":"error"'
			}
			log.warn(jsonStr)
			response.setContentType("application/json")
			render '[{'+jsonStr+'}]'
			return
		}

		String salt = saltSource instanceof NullSaltSource ? null : command.username
		def user = lookupUserClass().newInstance(email: command.email, username: command.username,
				accountLocked: true, enabled: true)

		if (user != null){
			user.setCreateDate(new Date())
			user.setPlainPassword(command.password)
			user.setMobilePhone("12314143")
			user.setLastLoginTime(new Date())
		}
		
		RegistrationCode registrationCode = springSecurityUiService.register(user, command.password, salt)
		if (registrationCode == null || registrationCode.hasErrors()) {
			// null means problem creating the user
			//flash.error = message(code: 'spring.security.ui.register.miscError')
			//flash.chainedParams = params
			//redirect action: 'index'
			def err = message(code: 'spring.security.ui.register.miscError')
			log.warn(err)
			response.setContentType("application/json")
			render '[{"username":"'+command.username+'","status":"failure","error":"'+err+'"}]'
			return
		}

		String url = generateLink('verifyRegistration', [t: registrationCode.token])

		def conf = SpringSecurityUtils.securityConfig
		def body = conf.ui.register.emailBody
		if (body.contains('$')) {
			body = evaluate(body, [user: user, url: url])
		}
		mailService.sendMail {
			to command.email
			from conf.ui.register.emailFrom
			subject conf.ui.register.emailSubject
			html body.toString()
		}

		//render view: 'index', model: [emailSent: true]
		response.setContentType("application/json")
		render '[{"username":"'+command.username+'","status":"success"}]'
	}
	
	def register(RegisterCommand command) {
		
		if (command.hasErrors()) {
			//render view: 'index', model: [command: command]
			def jsonStr = '"username":"'+command.username+'","status":"failure"'
			if (command.errors.hasFieldErrors('username')) {
				jsonStr = jsonStr + ',"username":"error"' 
			}
			if (command.errors.hasFieldErrors('email')) {
				jsonStr = jsonStr + ',"email":"error"'
			}
			if (command.errors.hasFieldErrors('password')) {
				jsonStr = jsonStr + ',"password":"error"'
			}
			if (command.errors.hasFieldErrors('password2')) {
				jsonStr = jsonStr + ',"password2":"error"'
			}
			log.warn(jsonStr)
			//response.setContentType("application/json")
			//render '[{'+jsonStr+'}]'
			//return
			
			render view: 'index', model: [command: command]
			return
		}

		String salt = saltSource instanceof NullSaltSource ? null : command.username
		def user = lookupUserClass().newInstance(email: command.email, username: command.username,
				accountLocked: true, enabled: true)

		if (user != null){
			user.setCreateDate(new Date())
			user.setPlainPassword(command.password)
			user.setMobilePhone("12314143")
			user.setLastLoginTime(new Date())
		}

		RegistrationCode registrationCode = springSecurityUiService.register(user, command.password, salt)
		if (registrationCode == null || registrationCode.hasErrors()) {
			// null means problem creating the user
			flash.error = message(code: 'spring.security.ui.register.miscError')
			flash.chainedParams = params
			redirect action: 'index'
			return
		}

		String url = generateLink('verifyRegistration', [t: registrationCode.token])

		def conf = SpringSecurityUtils.securityConfig
		def body = conf.ui.register.emailBody
		if (body.contains('$')) {
			body = evaluate(body, [user: user, url: url])
		}
		mailService.sendMail {
			to command.email
			from conf.ui.register.emailFrom
			subject conf.ui.register.emailSubject
			html body.toString()
		}

		render view: 'index', model: [emailSent: true]
	}

	
}


class RegisterCommand {
	
	String username
	String email
	String password
	String password2

	def grailsApplication

	static constraints = {
		username blank: false, validator: { value, command ->
			if (value) {
				def User = command.grailsApplication.getDomainClass(
					SpringSecurityUtils.securityConfig.userLookup.userDomainClassName).clazz
				if (User.findByUsername(value)) {
					return 'registerCommand.username.unique'
				}
			}
		}
		email blank: false, email: true, validator: { value, command ->
			if (value) {
				def User = command.grailsApplication.getDomainClass(
					SpringSecurityUtils.securityConfig.userLookup.userDomainClassName).clazz
				if (User.findByUsernameOrEmail(value,value)) {
					return 'registerCommand.email.unique'
				}
			}
		}
		password blank: false, validator: RegisterController.passwordValidator
		password2 validator: RegisterController.password2Validator
	}
}