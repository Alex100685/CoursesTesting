package com.andre.mvc.manager;

import com.andre.mvc.database.crm.entity.*;
import com.andre.mvc.database.crm.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.Override;import java.lang.String;import java.util.List;

/**
 * Created by Khemrayev A.K. on 12.05.2015.
 */
@Component("clientService")
public class CrmManagerImpl implements CrmManager {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private CoachRepository coachRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @Override
    public List<Client> listClientByConditions(String name, String surname, String phone, String email, String tags) {
        return clientRepository.findByNameLikeAndSurnameLikeAndPhoneLikeAndEmailLikeAndTagsLike(name, surname, phone, email, tags);
    }

    @Override
    public Client loadClientByPhone(String phone) {
        return clientRepository.findByPhone(phone);
    }

    @Override
    public void saveClient(Client client) {
        clientRepository.saveAndFlush(client);
    }

    @Override
    public List<Client> listAllClients() {
        return clientRepository.findAll();
    }

    @Override
    public Client loadClientById(Long id) {
        return clientRepository.findOne(id);
    }

    @Override
    public void deleteClient(Long id) {
        clientRepository.delete(id);
    }

    @Override
    public Client loadClientByName(String name) {
        return clientRepository.findByName(name);
    }

    @Override
    public Client loadClientByUsername(String username) {
        return clientRepository.findByUsername(username);
    }

    //coaches

    @Override
    public List<Coach> listCoachesByConditions(String name, String surname, String phone, String email) {
        return coachRepository.findByNameLikeAndSurnameLikeAndPhoneLikeAndEmailLike(name, surname, phone, email);
    }

    @Override
    public Coach loadCoachByName(String name) {
        return coachRepository.findByName(name);
    }

    @Override
    public void saveCoach(Coach coach) {
        coachRepository.save(coach);
    }

    @Override
    public List<Coach> listAllCoaches() {
        return coachRepository.findAll();
    }

    @Override
    public Coach loadCoachById(Long id) {
        return coachRepository.findOne(id);
    }

    @Override
    public void deleteCoach(Long id) {
        coachRepository.delete(id);
    }

    //courses

    @Override
    public List<Course> listAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    public List<Course> listCourseByConditions(String name) {
        return courseRepository.findByNameLike(name);
    }

    @Override
    public Course loadCourseByName(String name) {
        return courseRepository.findByName(name);
    }

    @Override
    public void saveCourse(Course course) {
        courseRepository.save(course);
    }

    @Override
    public void deleteCourse(Long id) {
        courseRepository.delete(id);
    }

    @Override
    public Course loadCourseById(Long id) {
        return courseRepository.findOne(id);
    }

    //groups

    @Override
    public List<Group> listAllGroups() {
        return groupRepository.findAll();
    }

    @Override
    public List<Group> listGroupByConditions(String name) {
        return groupRepository.findByNameLike(name);
    }

    @Override
    public Group loadGroupByName(String name) {
        return groupRepository.findByName(name);
    }

    @Override
    public void saveGroup(Group group) {
        groupRepository.save(group);
    }

    @Override
    public void deleteGroup(Long id) {
        groupRepository.delete(id);
    }

    @Override
    public Group loadGroupById(Long id) {
        return groupRepository.findOne(id);
    }

    // places

    @Override
    public List<Place> listAllPlaces() {
        return placeRepository.findAll();
    }

    @Override
    public Place loadPlaceByName(String name) {
        return placeRepository.findByName(name);
    }

    // getters and setters

    public ClientRepository getClientRepository() {
        return clientRepository;
    }

    public void setClientRepository(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public CoachRepository getCoachRepository() {
        return coachRepository;
    }

    public void setCoachRepository(CoachRepository coachRepository) {
        this.coachRepository = coachRepository;
    }

    public CourseRepository getCourseRepository() {
        return courseRepository;
    }

    public void setCourseRepository(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public GroupRepository getGroupRepository() {
        return groupRepository;
    }

    public void setGroupRepository(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public PlaceRepository getPlaceRepository() {
        return placeRepository;
    }

    public void setPlaceRepository(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    /*
     * Edited By Velichko A. start
     */
	@Override
	public List<Client> getClientsByGroup(Group group) {
		return clientRepository.findClientsByGroup(group);
	}
	/*
     * end
     */
}
