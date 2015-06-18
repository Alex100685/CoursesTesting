package com.andre.mvc.database.hometask.repository;

import java.util.List;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.andre.mvc.database.hometask.entity.HomeTask;
import com.andre.mvc.database.hometask.entity.HomeTaskResult;

/**
 * Created by Velychko A. on 05.05.2015.
 */

public interface HomeTaskRepository extends JpaRepository<HomeTask, Long> {

    public List<HomeTask> findAll();
    public HomeTask findById(int id);

    @Query("SELECT h FROM HomeTask h WHERE h.taskText LIKE :textPart")
	   public List <HomeTask> findByText(@Param ("textPart") String textPart);
   
}
