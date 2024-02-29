package com.clinic.admin.service;

public class ServiceDTO {
	private Integer id;
	private String name;

	private String checked;

	public ServiceDTO() {
	}

	public ServiceDTO(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getChecked() {
		return checked;
	}

	public void setChecked(String checked) {
		this.checked = checked;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}