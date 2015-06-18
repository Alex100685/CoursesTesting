package com.andre.mvc.sorter;

import java.util.Comparator;

import com.andre.mvc.database.hometask.entity.HomeTask;
import com.andre.mvc.database.hometask.entity.Rating;

/**
 * Created by Velychko A. on 05.05.2015.
 */
public class SortByDifficultyLevelRev implements Comparator <HomeTask> {

	@Override
	public int compare(HomeTask o1, HomeTask o2) {
		int s1 = o1.getDifficultyLevel();
		int s2 = o2.getDifficultyLevel();
		
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
