package com.soco

class MobileController {

    def index() { }
	
	def createProject() {
		def jsonObject = request.JSON
		def project = new Projects()
		def answer = project.createProject(jsonObject)
		render answer
	}
	
}
