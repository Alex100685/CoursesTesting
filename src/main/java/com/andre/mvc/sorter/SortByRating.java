package com.andre.mvc.sorter;

import java.util.Comparator;

import com.andre.mvc.database.hometask.entity.Rating;

/**
 * Created by Velychko A. on 05.05.2015.
 */
public class SortByRating implements Comparator <Rating> {

	@Override
	public int compare(Rating o1, Rating o2) {
		int s1 = o1.getScore();
		int s2 = o2.getScore();
		
		if(s1>s2){
			return -1;
		}
		if(s1<s2){
			return 1;
		}
		else{
			return 0;
		}
	}
	

}
