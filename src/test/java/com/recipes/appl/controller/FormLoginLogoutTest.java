package com.recipes.appl.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.recipes.appl.security.RolesEnum;

/**
 * @author Kastalski Sergey
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class FormLoginLogoutTest {
	
	@Autowired
	private WebApplicationContext wac;
	
	private MockMvc mvc;
	
	@Before
	public void setUp() {
	    mvc = MockMvcBuilders.webAppContextSetup(wac).apply(springSecurity()).build();
	}
	
	@Test
	public void testUserShouldBeAuthenticated() throws Exception {
		mvc.perform(formLogin("/login").user("admin").password("admin"))
			.andDo(print())
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/admin/ingredients"))
			.andExpect(authenticated().withUsername("admin").withRoles(RolesEnum.ADMIN_ROLE.getValue()));
	}
	
	@Test
	public void unknownUserLogin() throws Exception {
		mvc.perform(formLogin("/login").user("user").password("user"))
			.andDo(print())
			.andExpect(unauthenticated());
	}
	
	@Test
	public void adminLogout() throws Exception {
		mvc.perform(logout("/logout"))
			.andDo(print())
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/login?logout"));
	}
	
}
