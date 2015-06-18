package com.andre.mvc.database.hometask.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by Velychko A. on 05.05.2015.
 */

@Entity
@Table(name="home_task_result")
public class HomeTaskResult {
	
	@Id
	@Column(name="id", nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name="client_id", nullable = false)
	private Long clientId;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="home_task_id")
	private HomeTask homeTask;
	
	@Column(name="is_executed", nullable = false)
	boolean isExecuted;
	
	@Column(name="attempt_quantity", nullable = false)
	private int attemptQuantity;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getClientId() {
		return clientId;
	}

	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}

	public HomeTask getHomeTask() {
		return homeTask;
	}

	public void setHomeTask(HomeTask homeTask) {
		this.homeTask = homeTask;
	}

	public boolean getIsExecuted() {
		return isExecuted;
	}

	public void setExecuted(boolean isExecuted) {
		this.isExecuted = isExecuted;
	}

	public int getAttemptQuantity() {
		return attemptQuantity;
	}

	public void setAttemptQuantity(int attemptQuantity) {
		this.attemptQuantity = attemptQuantity;
	}

	


}
