package com.andre.mvc.manager;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.andre.mvc.controller.HomeWorkController.TestResult;
import com.andre.mvc.database.crm.entity.Client;
import com.andre.mvc.database.crm.entity.Group;
import com.andre.mvc.database.hometask.entity.HomeTask;
import com.andre.mvc.database.hometask.entity.HomeTaskResult;
import com.andre.mvc.database.hometask.entity.Rating;
import com.andre.mvc.database.hometask.repository.HomeTaskRepository;
import com.andre.mvc.database.hometask.repository.HomeTaskResultRepository;
import com.andre.mvc.database.hometask.repository.RatingRepository;

/**
 * Created by Velychko A. on 05.05.2015.
 */
@Component("homeTaskManager")
public class HometaskManagerImpl implements HomeTaskManager {
	
	@Autowired
	HomeTaskRepository htrepository;
	
	@Autowired
	HomeTaskResultRepository htResultrepository;
	
	@Autowired
	RatingRepository ratingRepository;
	
	CrmManagerImpl crmManagerImpl;
	

	@Override
	public List<HomeTask> getHometaskList() {
		return htrepository.findAll();
	}
	
	@Override
	public HomeTask getHomeTaskById(int id){
		return htrepository.findById(id);
	}

	@Override
	public HomeTaskResult getHometaskByClientIdAndHomeTask(Long clientId, HomeTask homeTask){
		return htResultrepository.findByClientIdAndHomeTask(clientId, homeTask);
	}

	@Override
	public void saveHomeTaskResult(HomeTaskResult homeTaskResult) {
		 htResultrepository.saveAndFlush(homeTaskResult);	
	}

	@Override
	public void saveHomeTask(HomeTask homeTask) {
		htrepository.saveAndFlush(homeTask);
		
	}

	@Override
	public List<HomeTaskResult> getHomeTaskResultsByClientId(Long Id) {
		return htResultrepository.findByClientIdLike(Id);
	}

	@Override
	public Rating getRatingOfClientById(Long id) {
		return ratingRepository.findByClientId(id);
	}
	
	public void saveRating(Rating rating){
		ratingRepository.saveAndFlush(rating);
	}

	@Override
	public List<Rating> getRatingList() {
		return ratingRepository.findAll();
	}

	public void deleteHomeTask(HomeTask homeTask){
		htrepository.delete(homeTask);
	}
	
	public void deleteHomeTaskResult(HomeTaskResult homeTaskResult) {
		htResultrepository.delete(homeTaskResult);
	}

	@Override
	public List<HomeTask> searchByTextPart(String textPart) {
		return htrepository.findByText(textPart);
	}

	

}
