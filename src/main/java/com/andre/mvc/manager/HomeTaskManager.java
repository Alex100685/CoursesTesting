package com.andre.mvc.manager;

import java.util.List;

import org.springframework.data.repository.query.Param;

import com.andre.mvc.controller.HomeWorkController.TestResult;
import com.andre.mvc.database.crm.entity.*;
import com.andre.mvc.database.hometask.entity.HomeTask;
import com.andre.mvc.database.hometask.entity.HomeTaskResult;
import com.andre.mvc.database.hometask.entity.Rating;

/**
 * Created by Velychko A. on 05.05.2015.
 */

public interface HomeTaskManager {
	
	public List <HomeTask> getHometaskList();
	
	public HomeTask getHomeTaskById(int id);
	
	public HomeTaskResult getHometaskByClientIdAndHomeTask(Long clientId, HomeTask homeTask);
	
	public void saveHomeTaskResult (HomeTaskResult homeTaskResult);
	
	public void saveHomeTask (HomeTask homeTask);
	
	public List <HomeTaskResult> getHomeTaskResultsByClientId(Long id);
	
	public Rating getRatingOfClientById(Long clientId);
	
	public void saveRating(Rating rating);

	public List <Rating> getRatingList();
	
	public void deleteHomeTask(HomeTask homeTask);

	public void deleteHomeTaskResult(HomeTaskResult homeTaskResult);
	
	public List<HomeTask> searchByTextPart(String textPart);
	

}
