package io.ionuth.invoice.resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/test")
public class TestResource {
	
	@GetMapping("/login")
	public String getLogin() {
		return "Login String";
	}
	
	@GetMapping("/other")
	public String getOther() {
		return "other";
	}
	
}
