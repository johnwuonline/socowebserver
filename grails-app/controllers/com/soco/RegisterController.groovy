package com.soco

import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.authentication.dao.NullSaltSource
import grails.plugin.springsecurity.ui.RegisterCommand;
import grails.plugin.springsecurity.ui.RegistrationCode

class RegisterController extends grails.plugin.springsecurity.ui.RegisterController {
	

	def mobileRegister(RegisterCommand command) {
		if (command.hasErrors()) {
			render view: 'index', model: [command: command]
			return
		}

		String salt = saltSource instanceof NullSaltSource ? null : command.username
		def user = lookupUserClass().newInstance(email: command.email, username: command.username,
				accountLocked: true, enabled: true)

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

		//render view: 'index', model: [emailSent: true]
		response.setContentType("application/json")
		render '[{"username":"'+command.username+'","status":"ok"}]'
	}
}
