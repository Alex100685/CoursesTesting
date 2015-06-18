package com.andre.mvc.manager;

import com.andre.mvc.database.crm.entity.*;
import org.springframework.transaction.annotation.Transactional;

import java.lang.String;import java.util.List;

/**
 * Created by Khemrayev A.K. on 12.05.2015.
 */
public interface CrmManager {

    // clients
    public List<Client> listClientByConditions(String name, String surname, String phone, String email, String tags);

    public Client loadClientByPhone(String phone);

    public void saveClient(Client client);

    public List<Client> listAllClients();

    public Client loadClientById(Long id);

    public void deleteClient(Long id);

    public Client loadClientByName(String name);

    public Client loadClientByUsername(String username);

    //coaches
    public List<Coach> listCoachesByConditions(String name, String surname, String phone, String email);

    public Coach loadCoachByName(String name);

    public void saveCoach(Coach coach);

    public List<Coach> listAllCoaches();

    public Coach loadCoachById(Long id);

    public void deleteCoach(Long id);

    //courses
    public List<Course> listAllCourses();

    public List<Course> listCourseByConditions(String name);

    public Course loadCourseByName(String name);

    public void saveCourse(Course course);

    public void deleteCourse(Long id);

    public Course loadCourseById(Long id);

    //groups

    public List<Group> listAllGroups();

    public List<Group> listGroupByConditions(String name);

    public Group loadGroupByName(String name);

    public void saveGroup(Group group);

    public void deleteGroup(Long id);

    public Group loadGroupById(Long id);

    //places

    public List<Place> listAllPlaces();

    public Place loadPlaceByName(String name);
    
    /*
     * Edited By Velichko A. start:
     */

    public List <Client> getClientsByGroup(Group group);

    /*
     * end
     */

}
