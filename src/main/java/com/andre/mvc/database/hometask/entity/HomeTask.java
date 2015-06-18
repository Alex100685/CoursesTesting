package com.andre.mvc.database.hometask.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Created by Velychko A. on 05.05.2015.
 */

@Entity
@Table(name="home_task")
public class HomeTask {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id")
	private int id;
	
	

	@Column(name ="task_text", nullable = false)
	private String taskText;
	
	@Column(name ="correct_result", nullable = false)
	private String taskCorrectResult;
	
	@Column(name ="difficulty_level", nullable = false)
	private int difficultyLevel;
	
	@OneToMany(mappedBy="homeTask", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<HomeTaskResult> homeTaskResults = new ArrayList<HomeTaskResult>();
	
	
	public int getId() {
		return id;
	}

	public List<HomeTaskResult> getHomeTaskResults() {
		return homeTaskResults;
	}

	public void setHomeTaskResults(List<HomeTaskResult> homeTaskResults) {
		this.homeTaskResults = homeTaskResults;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTaskText() {
		return taskText;
	}

	public void setTaskText(String taskText) {
		this.taskText = taskText;
	}

	public String getTaskCorrectResult() {
		return taskCorrectResult;
	}

	public void setTaskCorrectResult(String taskCorrectResult) {
		this.taskCorrectResult = taskCorrectResult;
	}

	public int getDifficultyLevel() {
		return difficultyLevel;
	}

	public void setDifficultyLevel(int difficultyLevel) {
		this.difficultyLevel = difficultyLevel;
	}
}
