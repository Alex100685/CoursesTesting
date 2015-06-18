package com.andre.mvc.controller;

import com.andre.mvc.controller.response.JsonResponse;
import com.andre.mvc.database.crm.entity.Client;
import com.andre.mvc.database.forum.entity.Member;
import com.andre.mvc.manager.CrmManager;
import com.andre.mvc.manager.ForumManagerImpl;
import com.andre.mvc.security.CustomUserDetailsUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.xml.bind.DatatypeConverter;


/**
 * Created by Khemrayev A.K. on 21.05.2015.
 */

@Controller
@RequestMapping(value = "/client/**")
public class ClientController {

    @Autowired
    private CrmManager crmManager;
    @Autowired
    private ForumManagerImpl forumManager;
    @Autowired
    private ShaPasswordEncoder passwordEncoder;

    @RequestMapping(value = "/editProfile")
    public ModelAndView editProfilePage() {
        ModelAndView modelAndView = new ModelAndView("/client/edit-profile");

        CustomUserDetailsUser user = (CustomUserDetailsUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Client client = crmManager.loadClientByPhone(user.getPhone());

        modelAndView.addObject("username", client.getName());
        modelAndView.addObject("phone", client.getPhone());
        return modelAndView;
    }

    @RequestMapping(value = "cabinet")
    public ModelAndView cabinet() {
        return new ModelAndView("/client/cabinet");
    }

    @RequestMapping(value = "/editProfile/changeName", method = RequestMethod.POST, headers = "Accept=application/json")
    @ResponseBody
    public JsonResponse editName(@RequestParam String username,
                                 @RequestParam String firstName,
                                 @RequestParam String lastName) {

        CustomUserDetailsUser user = (CustomUserDetailsUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String phone = user.getPhone();
        String currentUsername = user.getUsername();
        String newUsername = username;

        Member member = forumManager.loadByName(newUsername);

        // this username isn't used
        if (member == null) {
            // we should search client by phone, because it's required field in registration
            // so it's unique and in's guaranteed not null
            Client client = crmManager.loadClientByPhone(phone);
            System.out.println(client);
            client.setUsername(newUsername);
            client.setName(firstName);
            client.setSurname(lastName);
            crmManager.saveClient(client);

//            //reinit. Old object is useless now
//            member = forumManager.loadByName(currentUsername);
//            //it means, that client wasn't registered in forum
//            if(member == null) {
//                if(client.getEmail() != null) {
//                    //registration
//                    member = new Member();
//                    member.setName(client.getUsername());
//                    member.setEmail(client.getEmail());
//
//                    String password = passwordEncoder.encodePassword(client.getUsername().toLowerCase()
//                        + client.getUsername(), null);
//                    member.setPassword(password);
//                    member.setGroup(0);
//
//                    forumManager.saveClient(member);
//                }
//            } else {
//                member.setName(newUsername);
//                forumManager.saveClient(member);
//            }

            //maybe dangerous!!!!
            user.setUsername(newUsername);

            return new JsonResponse("Changes accepted", JsonResponse.SUCCESS);
        } else {
            return new JsonResponse("This username is already in use or just you can't change it now",
                    JsonResponse.ERROR);
        }
    }

    @RequestMapping(value = "/editProfile/changePhones", method = RequestMethod.POST, headers = "Accept=application/json")
    @ResponseBody
    public JsonResponse editPhone(@RequestParam String phone1,
                                  @RequestParam String phone2,
                                  @RequestParam String phone3) {

        //bad validation
        if(phone1.isEmpty()) {
            return new JsonResponse("Primary phone can't be empty", JsonResponse.ERROR);
        }

        CustomUserDetailsUser user = (CustomUserDetailsUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Client client = crmManager.loadClientByPhone(user.getPhone());

        client.setPhone(phone1);
        client.setPhone2(phone2);
        client.setPhone3(phone3);

        crmManager.saveClient(client);
        user.setPhone(client.getPhone());
        return new JsonResponse("Changes accepted", JsonResponse.SUCCESS);
    }

    @RequestMapping(value = "/editProfile/changeSocials")
    @ResponseBody
    public JsonResponse editSocials(@RequestParam String socials) {
        CustomUserDetailsUser user = (CustomUserDetailsUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Client client = crmManager.loadClientByPhone(user.getPhone());
        client.setSocialLinks(socials);
        crmManager.saveClient(client);

        return new JsonResponse("Changes accepted", JsonResponse.SUCCESS);
    }

    @RequestMapping(value = "/editProfile/changeEmail")
    @ResponseBody
    public JsonResponse editEmail(@RequestParam String email) {
        CustomUserDetailsUser user = (CustomUserDetailsUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Client client = crmManager.loadClientByPhone(user.getPhone());
        client.setEmail(email);
        crmManager.saveClient(client);

        return new JsonResponse("Changes accepted", JsonResponse.SUCCESS);
    }

    @RequestMapping(value = "/editProfile/changePassword")
    @ResponseBody
    public JsonResponse changePassword(@RequestParam String password,
                                       @RequestParam String newPassword,
                                       @RequestParam String confirmedPassword) {
        if(!newPassword.equals(confirmedPassword)) {
            return new JsonResponse("New password and confirmed new password values are not equal!",
                    JsonResponse.ERROR);
        }

        CustomUserDetailsUser user = (CustomUserDetailsUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Client client = crmManager.loadClientByPhone(user.getPhone());

        String currentEncodedPass = user.getPassword();
        String currentSalt = user.getSalt();

        String encodedPasswordForCheck = passwordEncoder.encodePassword(password, currentSalt);

        if(!currentEncodedPass.equals(encodedPasswordForCheck)) {
            return new JsonResponse("Illegal old password!", JsonResponse.ERROR);
        }

        // generate the "salt" value for password encoding
        byte[] saltBytes = KeyGenerators.secureRandom(2).generateKey();
        String newSalt = DatatypeConverter.printHexBinary(saltBytes);

        String newEncodedPassword = passwordEncoder.encodePassword(newPassword, newSalt);

        client.setPassword(newEncodedPassword);
        client.setSalt(newSalt);

        crmManager.saveClient(client);

        user.setSalt(newSalt);
        user.setPassword(newEncodedPassword);

        return new JsonResponse("Changes accepted", JsonResponse.SUCCESS);
    }
}
