package com.intech.soco

import com.soco.User
import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.userdetails.GrailsUser
import grails.plugin.springsecurity.userdetails.GrailsUserDetailsService

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.authority.GrantedAuthorityImpl
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException


class SoCoUserDetailsService implements GrailsUserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		User.withTransaction { status ->

         User user = User.findByUsername(username)
         if (!user) throw new UsernameNotFoundException(
                      'User not found', username)

         def authorities = user.authorities.collect {
             new GrantedAuthorityImpl(it.authority)
         }

         return new SoCoUserDetails(user.username, user.password, user.enabled,
            !user.accountExpired, !user.passwordExpired,
            !user.accountLocked, authorities ?: NO_ROLES, user.id,
            user.email)
      }
	}

	@Override
	public UserDetails loadUserByUsername(String username, boolean loadRoles)
			throws UsernameNotFoundException, DataAccessException {
		// TODO Auto-generated method stub
		return loadUserByUsername(username);
	}

}
