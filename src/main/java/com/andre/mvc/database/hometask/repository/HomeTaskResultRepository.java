package com.andre.mvc.database.hometask.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.andre.mvc.database.crm.entity.Client;
import com.andre.mvc.database.crm.entity.Group;
import com.andre.mvc.database.hometask.entity.HomeTask;
import com.andre.mvc.database.hometask.entity.HomeTaskResult;

/**
 * Created by Velychko A. on 05.05.2015.
 */
public interface HomeTaskResultRepository extends JpaRepository<HomeTaskResult, Long> {
	
	 	@Query("SELECT h FROM HomeTaskResult h WHERE h.clientId = (:clientId) AND h.homeTask = (:homeTask)")
	   public HomeTaskResult findByClientIdAndHomeTask(@Param ("clientId")Long clientId, @Param ("homeTask")HomeTask homeTask);
	 	
	 	public List <HomeTaskResult> findByClientIdLike(Long id);
	 	
	 	
	 	
}
