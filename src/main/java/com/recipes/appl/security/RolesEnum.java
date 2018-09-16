package com.recipes.appl.security;

/**
 * @author Kastalski Sergey
 */
public enum RolesEnum {
	
	ADMIN_ROLE("ADMIN");
	
	private String role;
	
	private RolesEnum(final String role) {
		this.role = role;
	}
	
	public String getValue() {
		return role;
	}
	
	@Override
	public String toString() {
		return role;
	}
	
}
