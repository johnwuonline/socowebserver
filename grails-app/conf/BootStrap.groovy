import com.soco.Role

class BootStrap {

    def init = { servletContext ->
		Role roleUser = new Role(authority: "ROLE_USER")
		roleUser.save()
    }
    def destroy = {
    }
}
