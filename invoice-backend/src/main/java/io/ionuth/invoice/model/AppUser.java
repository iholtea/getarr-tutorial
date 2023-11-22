package io.ionuth.invoice.model;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public class AppUser {
	
	private Long userId;
	
	@NotEmpty(message = "First name cannot be empty")
	private String firstName;
	
	@NotEmpty(message = "Last name cannot be empty")
	private String lastName;
	
	@NotEmpty(message = "Email cannot be empty")
	@Email(message = "Invalid email address")
	private String email;
	
	@NotEmpty(message = "Password cannot be empty")
	private String password;
	
	private String address;
	private String phone;
	private String title;
	private String description;
	private boolean enabled;
	private boolean locked;
	private boolean useMfa;
	private LocalDate createdDate;
	
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public boolean isLocked() {
		return locked;
	}
	public void setLocked(boolean locked) {
		this.locked = locked;
	}
	public boolean isUseMfa() {
		return useMfa;
	}
	public void setUseMfa(boolean useMfa) {
		this.useMfa = useMfa;
	}
	public LocalDate getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(LocalDate createdDate) {
		this.createdDate = createdDate;
	}
	
}
