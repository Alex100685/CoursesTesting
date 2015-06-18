package com.andre.mvc.controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.andre.mvc.database.crm.entity.Client;
import com.andre.mvc.database.crm.entity.Group;
import com.andre.mvc.database.hometask.entity.HomeTask;
import com.andre.mvc.database.hometask.entity.HomeTaskResult;
import com.andre.mvc.database.hometask.entity.Rating;
import com.andre.mvc.manager.CrmManager;
import com.andre.mvc.manager.ForumManagerImpl;
import com.andre.mvc.manager.HomeTaskManager;
import com.andre.mvc.sorter.SortByDifficultyLevel;
import com.andre.mvc.sorter.SortByDifficultyLevelRev;
import com.andre.mvc.sorter.SortById;
import com.andre.mvc.sorter.SortByIdRev;
import com.andre.mvc.sorter.SortByRating;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by Velychko A. on 05.05.2015.
 */

@Controller
//@RequestMapping (value = "/homeWork/**")
public class HomeWorkController {
	
	public static class TestResult {
        public String phone;
        public int taskNumber;
        public String output;
    }
	
	
	@Autowired
	private HomeTaskManager homeTaskManager;
	
	@Autowired
	private CrmManager crmManager;
	
	@Autowired
	private ForumManagerImpl forumManager;
	
	@Autowired
	private ShaPasswordEncoder passwordEncoder;
	
	
	@RequestMapping (value = "/checkAssignment", method = RequestMethod.POST)
	public void checkAssignment (
			HttpServletRequest request,
			HttpServletResponse response
			)
			{
		int size;
		InputStream is = null;
		DataInputStream dis = null;
		byte [] buffer = null;
		try{
			is = request.getInputStream();
			dis = new DataInputStream(is);
			size = dis.readInt();
			if(is.available()>0){
				buffer = new byte [size];
				while(is.available()>0){
				is.read(buffer);
				}		
			}	
		}
		catch(IOException e){
			e.printStackTrace();	
		}
		finally{
			try {
				dis.close();
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Gson gson = new GsonBuilder().create();
		String gsonTestResult = new String (buffer);
		TestResult tr = gson.fromJson(gsonTestResult, TestResult.class);
		tr.output = tr.output.substring(0, tr.output.length()-2);
		String serverResponse = null;
		HomeTask ht = homeTaskManager.getHomeTaskById(tr.taskNumber);
		if(ht == null){
			serverResponse = "Задания под таким номером не существует";
		}
		else{
			Client c = crmManager.loadClientByPhone(tr.phone);
			if(c == null){
				serverResponse = "Студента с таким номером телефона не существует! Введите корректный номер телефона";
			}
			else{
				HomeTaskResult htr = homeTaskManager.getHometaskByClientIdAndHomeTask(c.getId(), ht);
				if(htr == null){
				 htr = new HomeTaskResult();
				 htr.setClientId(c.getId());
				 htr.setHomeTask(ht);
				 htr.setAttemptQuantity(0);
				 htr.setExecuted(false);
				 homeTaskManager.saveHomeTaskResult(htr);
				}
				if(htr.getIsExecuted() == true){
					 serverResponse = "Вы уже выполнили задание номер "+ht.getId()+" c "+htr.getAttemptQuantity()+"-й попытки";
				}
				if(tr.output.equals(ht.getTaskCorrectResult()) && htr.getIsExecuted() == false){
					htr.setAttemptQuantity(htr.getAttemptQuantity()+1);
					htr.setExecuted(true);
					serverResponse = "Поздравляем! Это правильный ответ! Вы выполнили задание номер "+ht.getId()+" с "+htr.getAttemptQuantity()+"-й попытки";
					homeTaskManager.saveHomeTaskResult(htr);
					List <HomeTaskResult> resultList = homeTaskManager.getHomeTaskResultsByClientId(c.getId());
					int newScore = 0;
					for(HomeTaskResult h: resultList){
						if(h.getIsExecuted()){
							newScore += h.getHomeTask().getDifficultyLevel();
						}
					}
					Rating r = homeTaskManager.getRatingOfClientById(c.getId());
					if(r==null){
						r = new Rating();
						r.setClientId(c.getId());
						r.setClientName(c.getName());
					}
					r.setScore(newScore);
					homeTaskManager.saveRating(r);
					
				}
				if(!tr.output.equals(ht.getTaskCorrectResult()) && htr.getIsExecuted() == false){
					htr.setAttemptQuantity(htr.getAttemptQuantity()+1);
					serverResponse = "Неправильный ответ! Это была "+htr.getAttemptQuantity()+"-я попытка";
					homeTaskManager.saveHomeTaskResult(htr);
				}
			}
		}
		try {
			byte [] byteResp = serverResponse.getBytes();
			OutputStream os = response.getOutputStream();
			DataOutputStream dos = new DataOutputStream(os);
			dos.writeInt(byteResp.length);
			os.write(byteResp);
			os.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			try {
				dis.close();
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		
		
			}
	
			}
	
	@RequestMapping("/admin/createNewAssignment")
	public ModelAndView createNewAssignment() {
		return new ModelAndView("/admin/createHomeTask");
	}
	
	@RequestMapping("/admin/saveNewAssignment")
	public ModelAndView saveNewAssignment
	(
	@RequestParam(value="taskText") String taskText,
	@RequestParam(value="taskResult") String taskResult,	
	@RequestParam(value="difficultyLevel") int difficultyLevel
			) {
		HomeTask ht = new HomeTask();
		ht.setTaskText(taskText);
		ht.setTaskCorrectResult(taskResult);
		ht.setDifficultyLevel(difficultyLevel);	
		homeTaskManager.saveHomeTask(ht);
		return new ModelAndView("/admin/homeTaskList","homeTasks" ,homeTaskManager.getHometaskList());
	}
	
	@RequestMapping("/admin/toHomeTasks")
	public ModelAndView toHomeTasks
	() {
		List <HomeTask> hList = homeTaskManager.getHometaskList();
		return new ModelAndView("/admin/homeTaskList","homeTasks" , hList);
	}
	
	@RequestMapping("/admin/edit")
	public ModelAndView editHomeTask
	(
			HttpServletRequest request,
			HttpServletResponse response
			) {
		String homeTaskId = request.getParameter("id");
		int htId = Integer.parseInt(homeTaskId);
		HomeTask ht = homeTaskManager.getHomeTaskById(htId);
		return new ModelAndView("/admin/editHomeTask","homeTask" , ht);
	}
	
	@RequestMapping("/admin/saveEditedTask")
	public ModelAndView saveEditedTask
	(
			@RequestParam(value="taskId") int taskId,
			@RequestParam(value="taskText") String taskText,
			@RequestParam(value="taskResult") String taskResult,	
			@RequestParam(value="difficultyLevel") int difficultyLevel
			) {
		HomeTask ht = homeTaskManager.getHomeTaskById(taskId);
		ht.setTaskText(taskText);
		ht.setTaskCorrectResult(taskResult);
		ht.setDifficultyLevel(difficultyLevel);
		homeTaskManager.saveHomeTask(ht);
		List <HomeTask> hList = homeTaskManager.getHometaskList();
		return new ModelAndView("/admin/homeTaskList","homeTasks" , hList);
	}
	
	@RequestMapping("/client/toRating")
	public ModelAndView toRating(){
		List <Group> gList = crmManager.listAllGroups();
		for(int i=0; i<gList.size(); i++){
			if("Admins".equals(gList.get(i).getName())){
				gList.remove(gList.get(i));
			}
		}
		return new ModelAndView("/client/groupList","Groups", gList);
	}
	
	
	
	@RequestMapping("/client/groupRating")
	public ModelAndView groupRating(
			@RequestParam(value="selectedGroup") String groupName
			){
		Group group = crmManager.loadGroupByName(groupName);
		//List <Client> cList = group.getClients();
		List <Client> cList = crmManager.getClientsByGroup(group);
		List <Rating> rList = new ArrayList<Rating>();
		for(Client c: cList){
			Rating r = homeTaskManager.getRatingOfClientById(c.getId());
			if(r==null){
				r = new Rating();
				r.setScore(0);
				r.setClientId(c.getId());
				r.setClientName(c.getName());
			}
			rList.add(r);
		}
		Rating [] rArray = new Rating [rList.size()];
		for(int i=0; i<rList.size(); i++){
			rArray[i] = rList.get(i);
		}
		Arrays.sort(rArray, new SortByRating());
		return new ModelAndView("/client/groupRates","Ratings", rArray);
	}
	
	@RequestMapping("/client/executedTasks")
	public ModelAndView executedTasks
			(
					HttpServletRequest request,
					HttpServletResponse response
					){
		String clientIdString = request.getParameter("id");
		Long clientId = Long.parseLong(clientIdString);
		Client client = crmManager.loadClientById(clientId);
		String clientName = client.getName();
		List <HomeTaskResult> htrList = homeTaskManager.getHomeTaskResultsByClientId(clientId);
		ModelAndView modelAndView = new ModelAndView("/client/executedTasks");
		modelAndView.addObject("clientName", clientName);
        modelAndView.addObject("homeTaskResults", htrList);
		return modelAndView;
	}
	
	@RequestMapping("/admin/deleteTask")
	public ModelAndView deleteTasks(
			@RequestParam(value="id []") String [] id
			){
		for(int i=0; i<id.length; i++){
			int htid = Integer.parseInt(id[i]);
			HomeTask ht = homeTaskManager.getHomeTaskById(htid);
			if(ht.getHomeTaskResults().size()>0){
				List <HomeTask> htPass = new ArrayList<HomeTask>();
				for(int j=0; j<id.length; j++){
					int htid1 = Integer.parseInt(id[j]);
					HomeTask ht1 = homeTaskManager.getHomeTaskById(htid1);
					htPass.add(ht1);
				}
				return new ModelAndView("/admin/confirmDelete", "homeTasks", htPass);
			}
		}
		for(int i=0; i<id.length; i++){
			int htid = Integer.parseInt(id[i]);
			HomeTask ht = homeTaskManager.getHomeTaskById(htid);
			homeTaskManager.deleteHomeTask(ht);	
		}
		return new ModelAndView("/admin/homeTaskList","homeTasks" ,homeTaskManager.getHometaskList());
	}
	
	@RequestMapping("/admin/deleteTasksAnyway")
	public ModelAndView deleteAnyway(
			HttpServletRequest request,
			HttpServletResponse response
			){
		String [] id= request.getParameterValues("id");
		for(int i=0;i<id.length; i++){
			int htid = Integer.parseInt(id[i]);
			HomeTask ht = homeTaskManager.getHomeTaskById(htid);
			List <HomeTaskResult> htr = ht.getHomeTaskResults();
			for(int j=0; j<htr.size(); j++){
				homeTaskManager.deleteHomeTaskResult(htr.get(j));
			}
			homeTaskManager.deleteHomeTask(ht);
		}
		return new ModelAndView("/admin/homeTaskList","homeTasks" ,homeTaskManager.getHometaskList());
	}
	
	@RequestMapping("/admin/searchById")
	public ModelAndView searchById(
			@RequestParam(value="pattern") String iDpattern
			) {
		List <HomeTask> hList = homeTaskManager.getHometaskList();
		List <HomeTask> findedhList = new ArrayList<HomeTask>();
		for(HomeTask h: hList){
			if(String.valueOf(h.getId()).contains(iDpattern)){
				findedhList.add(h);
			}
		}
		return new ModelAndView("/admin/homeTaskList", "homeTasks", findedhList);
	}
	
	@RequestMapping("/admin/searchByText")
	public ModelAndView searchByText(
			@RequestParam(value="pattern") String textPattern
			) {
		List <HomeTask> hList = homeTaskManager.searchByTextPart("%"+textPattern+"%");
		return new ModelAndView("/admin/homeTaskList", "homeTasks", hList);
	}
	
	@RequestMapping("/admin/sortById")
	public ModelAndView sortById(){
		List <HomeTask> hList = homeTaskManager.getHometaskList();
		HomeTask [] hArray = new HomeTask [hList.size()];
		for(int i=0;i<hList.size(); i++){
			hArray [i] = hList.get(i);
		}
		Arrays.sort(hArray, new SortById());
		return new ModelAndView("/admin/homeTaskList", "homeTasks", hArray);	
	}
	
	@RequestMapping("/admin/sortByIdRev")
	public ModelAndView sortByIdRev(){
		List <HomeTask> hList = homeTaskManager.getHometaskList();
		HomeTask [] hArray = new HomeTask [hList.size()];
		for(int i=0;i<hList.size(); i++){
			hArray [i] = hList.get(i);
		}
		Arrays.sort(hArray, new SortByIdRev());
		return new ModelAndView("/admin/homeTaskList", "homeTasks", hArray);	
	}
	
	@RequestMapping("/admin/sortByDifficultyLevel")
	public ModelAndView sortByDifficultyLevel(){
		List <HomeTask> hList = homeTaskManager.getHometaskList();
		HomeTask [] hArray = new HomeTask [hList.size()];
		for(int i=0;i<hList.size(); i++){
			hArray [i] = hList.get(i);
		}
		Arrays.sort(hArray, new SortByDifficultyLevel());
		return new ModelAndView("/admin/homeTaskList", "homeTasks", hArray);	
	}
	
	@RequestMapping("/admin/sortByDifficultyLevelRev")
	public ModelAndView sortByDifficultyLevelRev(){
		List <HomeTask> hList = homeTaskManager.getHometaskList();
		HomeTask [] hArray = new HomeTask [hList.size()];
		for(int i=0;i<hList.size(); i++){
			hArray [i] = hList.get(i);
		}
		Arrays.sort(hArray, new SortByDifficultyLevelRev());
		return new ModelAndView("/admin/homeTaskList", "homeTasks", hArray);	
	}
	
	@RequestMapping("/admin/executedBy")
	public ModelAndView executedBy(
			HttpServletRequest request,
			HttpServletResponse response
			){
		String strId = request.getParameter("id");
		HomeTask ht = homeTaskManager.getHomeTaskById(Integer.parseInt(strId));
		List <HomeTaskResult> htrList = ht.getHomeTaskResults();
		List <Client> cList = new ArrayList<Client>();
		for(HomeTaskResult h: htrList){
			if(h.getIsExecuted()==true){
			Long cId = h.getClientId();
			Client c = crmManager.loadClientById(cId);
			cList.add(c);
			}
		}
		return new ModelAndView("/admin/executedBy", "clients", cList);
	}
	
	

}
