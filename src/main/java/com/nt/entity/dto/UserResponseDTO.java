package com.nt.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class UserResponseDTO {
		
	private Integer id;
	private String username;
	private String email;
	private String role;
}
