package com.andre.mvc.database.hometask.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.andre.mvc.database.hometask.entity.Rating;

/**
 * Created by Velychko A. on 05.05.2015.
 */
public interface RatingRepository extends JpaRepository<Rating, Long> {
	
	public Rating findByClientId(Long id);
	
	public List <Rating> findAll();

}
