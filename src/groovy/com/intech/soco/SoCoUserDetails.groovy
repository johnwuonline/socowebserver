package com.intech.soco

import grails.plugin.springsecurity.userdetails.GrailsUser
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User

class SoCoUserDetails extends GrailsUser {

	final String email
	
	   SoCoUserDetails(String username, String password, boolean enabled,
					 boolean accountNonExpired, boolean credentialsNonExpired,
					 boolean accountNonLocked,
					 Collection<GrantedAuthority> authorities,
					 long id, String email) {
		  super(username, password, enabled, accountNonExpired,
				credentialsNonExpired, accountNonLocked, authorities, id)
	
		  this.email = email
	   }
}
