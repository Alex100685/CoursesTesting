package com.andre.mvc.controller;

import com.andre.mvc.controller.response.JsonResponse;
import com.andre.mvc.database.crm.entity.*;
import com.andre.mvc.database.crm.repository.GroupRepository;
import com.andre.mvc.database.crm.repository.PlaceRepository;
import com.andre.mvc.database.forum.entity.Member;
import com.andre.mvc.manager.CrmManagerImpl;
import com.andre.mvc.manager.ForumManagerImpl;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.xml.bind.DatatypeConverter;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {

//    @Autowired
//    private CoachRepository coachRepository;
//    @Autowired
//    private CourseRepository courseRepository;
//    @Autowired
//    private PlaceRepository placeRepository;
//    @Autowired
//    private GroupRepository groupRepository;

    @Autowired
    private CrmManagerImpl crmManager;

    @Autowired
    private ForumManagerImpl forumManager;

    @Autowired
    private ShaPasswordEncoder passwordEncoder;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String welcome() {
        return "login";
    }

    @RequestMapping(value = "/loginError", method = RequestMethod.POST)
    public ModelAndView loginErr() {
        return new ModelAndView("login", "errMsg", "Illegal phone or password!");
    }

    @RequestMapping(value = "/admin/cabinet")
    public String cabinet() {
        return "cabinet";
    }

    // registration
    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ModelAndView listClients(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String phone,
            @RequestParam String password,
            @RequestParam String confirm_password
    ) {

        if(!password.equals(confirm_password)) {
            return new ModelAndView("login", "errMsg", "Password and confirm password values not equal!");
        }

        if(crmManager.loadClientByPhone(phone) != null) {
            return new ModelAndView("login", "errMsg", "Account with entered phone already exist. Please, enter another phone or login!");
        }

        // generate the "salt" value for password encoding
        byte[] saltBytes = KeyGenerators.secureRandom(2).generateKey();
        String salt = DatatypeConverter.printHexBinary(saltBytes);

        // creating password for smf database
        String passwordForForum = passwordEncoder.encodePassword(username.toLowerCase() + password, null);
        password = passwordEncoder.encodePassword(password, salt);

        Client client = new Client();

        if(username.isEmpty() || username == null) {
            username = phone;
        }

        client.setUsername(username);
        client.setEmail(email);
        client.setPhone(phone);
        client.setPassword(password);
        client.setSalt(salt);

        crmManager.saveClient(client);

        Member member = new Member();
        member.setName(username);
        member.setEmail(email);
        member.setPassword(passwordForForum);
        member.setSalt(salt);
        member.setGroup(0);

        forumManager.save(member);

        return new ModelAndView("login");
    }

    @RequestMapping(value = "/admin/clients/main")
    public String clientsMainPage() {
        return "admin/clients";
    }

    @RequestMapping(value = "/admin/coaches/main")
    public String coachesMainPage() {
        return "admin/coaches";
    }

    @RequestMapping(value = "/admin/courses/main")
    public String coursesMainPage() {
        return "admin/courses";
    }

    @RequestMapping(value = "/admin/groups/main")
    public String groupsMainPage() {
        return "admin/groups";
    }


    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addClient(@ModelAttribute("client") Client client, BindingResult result) {

        crmManager.saveClient(client);

        return "redirect:/register";
    }

    // Clients

    @RequestMapping(value = "/admin/clients/allClients", method = RequestMethod.GET, headers="Accept=application/json")
    public
    @ResponseBody
    List<Client> listClientsJson() {
        return crmManager.listAllClients();
    }

    @RequestMapping(value = "/admin/clients/clientsSearch", method = RequestMethod.GET, headers="Accept=application/json")
    @ResponseBody
    public List<Client> listClientsJsonSearch(
            @RequestParam String groupSelect,
            @RequestParam String namePattern,
            @RequestParam String surnamePattern,
            @RequestParam String phonePattern,
            @RequestParam String emailPattern,
            @RequestParam String tagsPattern

    ) {
        return crmManager.listClientByConditions("%" + namePattern + "%", "%" + surnamePattern + "%",
                "%" + phonePattern + "%", "%" + emailPattern + "%", "%" + tagsPattern + "%");
    }

    @RequestMapping(value = "/admin/clients/addNewClient", method = RequestMethod.POST, headers="Accept=application/json")
    @ResponseBody
    public JsonResponse saveClient(@RequestBody Client client) throws JSONException{
        if(!client.getPhone().isEmpty()) {
            Group group = crmManager.loadGroupByName(client.getGroup().getName());

            client.setGroup(group);

            crmManager.saveClient(client);
            return new JsonResponse("Ok");
        } else {
            return new JsonResponse("You should enter phone. Client not saved");
        }
    }

    @RequestMapping(value = "/admin/clients/updateClient", method = RequestMethod.POST, headers="Accept=application/json")
    @ResponseBody
    public JsonResponse updateClient(@RequestBody Client client) throws JSONException{
        if(!client.getPhone().isEmpty()) {
            crmManager.saveClient(client);
            return new JsonResponse("Ok");
        } else {
            return new JsonResponse("You should enter phone. Client not saved");
        }
    }

    @RequestMapping(value = "/admin/clients/deleteClient")
    @ResponseBody
    public JsonResponse deleteClient(@RequestParam Long id) throws JSONException{
        if(crmManager.loadClientById(id) != null) {
            crmManager.deleteClient(id);
            return new JsonResponse("Client was deleted!");
        } else {
            return new JsonResponse("Client not exist! Impossible delete from db!");
        }
    }

    // Coaches
    @RequestMapping(value = "/admin/coaches/allCoaches", method = RequestMethod.GET, headers="Accept=application/json")
    @ResponseBody
    @Transactional
    public List<Coach> listCoachesJson() throws JSONException {
        List<Coach> list = crmManager.listAllCoaches();
        return list;
    }

    @RequestMapping(value = "/admin/coaches/addNewCoach", method = RequestMethod.POST, headers="Accept=application/json")
    @ResponseBody
    public JsonResponse saveCoach(@RequestBody Coach coach) throws JSONException{
        if(!coach.getPhone().isEmpty()) {
            crmManager.saveCoach(coach);
            return new JsonResponse("Ok");
        } else {
            return new JsonResponse("You should enter phone. Coach not saved");
        }
    }

    @RequestMapping(value = "/admin/coaches/updateCoach", method = RequestMethod.POST, headers="Accept=application/json")
    @ResponseBody
    public JsonResponse updateCoach(@RequestBody Coach coach) throws JSONException{
        if(!coach.getPhone().isEmpty()) {
            crmManager.saveCoach(coach);
            return new JsonResponse("Ok");
        } else {
            return new JsonResponse("You should enter phone. Coach not saved");
        }
    }

    @RequestMapping(value = "/admin/coaches/deleteCoach")
    @ResponseBody
    public JsonResponse deleteCoach(@RequestParam Long id) {
        if(crmManager.loadCoachById(id) != null) {
            crmManager.deleteCoach(id);
            return new JsonResponse("Coach was deleted!");
        } else {
            return new JsonResponse("Coach not exist! Impossible delete from db!");
        }
    }

    @RequestMapping(value = "/admin/coaches/coachSearch", method = RequestMethod.GET, headers="Accept=application/json")
    @ResponseBody
    public List<Coach> searchesCoachesJson( @RequestParam String namePattern,
                                            @RequestParam String surnamePattern,
                                            @RequestParam String phonePattern,
                                            @RequestParam String emailPattern
    ) {
        List<Coach> list = crmManager.listCoachesByConditions("%" + namePattern + "%",
                "%" + surnamePattern + "%", "%" + phonePattern + "%", "%" + emailPattern + "%");
        return list;
    }

    // Courses
    @RequestMapping(value = "/admin/courses/allCourses", method = RequestMethod.GET, headers="Accept=application/json")
    @ResponseBody
    public List<Course> listCousesJson() throws JSONException {
        List<Course> list = crmManager.listAllCourses();
        return list;
    }

    @RequestMapping(value = "/admin/courses/addNewCourse", method = RequestMethod.POST, headers="Accept=application/json")
    @ResponseBody
    public JsonResponse saveCourse(@RequestBody Course course) throws JSONException{
        if(!course.getName().isEmpty()) {
            crmManager.saveCourse(course);

            return new JsonResponse("Ok");
        } else {
            return new JsonResponse("You should enter course name. Course not saved");
        }
    }

    @RequestMapping(value = "/admin/courses/updateCourse", method = RequestMethod.POST, headers="Accept=application/json")
    @ResponseBody
    public JsonResponse updateCourse(@RequestBody Course course) throws JSONException{
        if(!course.getName().isEmpty()) {
            Course tempCourse = crmManager.loadCourseById(course.getId());

            course.setCreationTime(tempCourse.getCreationTime());

            crmManager.saveCourse(course);
            return new JsonResponse("Ok");
        } else {
            return new JsonResponse("You should enter course name. Course not saved");
        }
    }

    @RequestMapping(value = "/admin/courses/deleteCourse")
    @ResponseBody
    public JsonResponse deleteCourse(@RequestParam Long id) {
        if(crmManager.loadCourseById(id) != null) {
            crmManager.deleteCourse(id);
            return new JsonResponse("Course was deleted!");
        } else {
            return new JsonResponse("Course not exist! Impossible to delete from db!");
        }
    }

    @RequestMapping(value = "/admin/courses/courseSearch", method = RequestMethod.GET, headers="Accept=application/json")
    @ResponseBody
    public List<Course> searchesCoursesJson( @RequestParam String namePattern ) {
        List<Course> list = crmManager.listCourseByConditions("%" + namePattern + "%");
        return list;
    }

    // Groups
    @RequestMapping(value = "/admin/groups/getListCoaches", method = RequestMethod.GET, headers="Accept=application/json")
    @ResponseBody
    public List<String> listCoachesNamesJson() {
        List<String> list = new ArrayList<String>();

        List<Coach> coaches = crmManager.listAllCoaches();

        for(Coach coach : coaches) {
            list.add(coach.getName());
        }

        return list;
    }

    @RequestMapping(value = "/admin/groups/getListGroupNames", method = RequestMethod.GET, headers="Accept=application/json")
    @ResponseBody
    public List<String> listGroupNamesJson() {
        List<String> list = new ArrayList<String>();

        List<Group> groups = crmManager.listAllGroups();

        for(Group group : groups) {
            list.add(group.getName());
        }

        return list;
    }

    @RequestMapping(value = "/admin/groups/getListPlaces", method = RequestMethod.GET, headers="Accept=application/json")
    @ResponseBody
    public List<String> listPlacesNamesJson() {
        List<String> list = new ArrayList<String>();

        List<Place> places = crmManager.listAllPlaces();

        for(Place place : places) {
            list.add(place.getName());
        }

        return list;
    }

    @RequestMapping(value = "/admin/groups/getListCourses", method = RequestMethod.GET, headers="Accept=application/json")
    @ResponseBody
    public List<String> listCoursesNamesJson() {
        List<String> list = new ArrayList<String>();

        List<Course> courses = crmManager.listAllCourses();

        for(Course course : courses) {
            list.add(course.getName());
        }

        return list;
    }

    @RequestMapping(value = "/admin/groups/addNewGroup", method = RequestMethod.POST, headers="Accept=application/json")
    @ResponseBody
    public JsonResponse saveGroup(@RequestBody Group group) throws JSONException{
        if(!group.getName().isEmpty()) {
            Coach coach = crmManager.loadCoachByName(group.getCoach().getName());
            Place place = crmManager.loadPlaceByName(group.getPlace().getName());
            Course course = crmManager.loadCourseByName(group.getCourse().getName());

            group.setCoach(coach);
            group.setPlace(place);
            group.setCourse(course);

            crmManager.saveGroup(group);
            return new JsonResponse("Ok");
        } else {
            return new JsonResponse("You should enter group name. Group not saved");
        }
    }

    @RequestMapping(value = "/admin/groups/allGroups", method = RequestMethod.GET, headers="Accept=application/json")
    @ResponseBody
    public List<Group> listGroupsJson() throws JSONException {
        List<Group> list = crmManager.listAllGroups();
        return list;
    }

    @RequestMapping(value = "/admin/groups/groupSearch", method = RequestMethod.GET, headers="Accept=application/json")
    @ResponseBody
    public List<Group> searchGroupsJson( @RequestParam String namePattern ) {
        List<Group> list = crmManager.listGroupByConditions("%" + namePattern + "%");
        return list;
    }

    @RequestMapping(value = "/admin/groups/deleteGroup")
    @ResponseBody
    public JsonResponse deleteGroup(@RequestParam Long id) {
        if(crmManager.loadGroupById(id) != null) {
            crmManager.deleteGroup(id);
            return new JsonResponse("Group was deleted!");
        } else {
            return new JsonResponse("Group not exist! Impossible to delete from db!");
        }
    }

    @RequestMapping(value = "/admin/groups/updateGroup", method = RequestMethod.POST, headers="Accept=application/json")
    @ResponseBody
    public JsonResponse updateGroup(@RequestBody Group group) throws JSONException{
        if(!group.getName().isEmpty()) {
            Group tempGroup = crmManager.loadGroupById(group.getId());

            group.setCreationTime(tempGroup.getCreationTime());

            crmManager.saveGroup(group);
            return new JsonResponse("Ok");
        } else {
            return new JsonResponse("You should enter course name. Course not saved");
        }
    }
}