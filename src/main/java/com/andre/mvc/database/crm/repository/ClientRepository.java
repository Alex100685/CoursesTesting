package com.andre.mvc.database.crm.repository;

import com.andre.mvc.database.crm.entity.Client;
import com.andre.mvc.database.crm.entity.Group;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by Khemrayev A.K. on 21.04.2015.
 */

public interface ClientRepository extends JpaRepository<Client, Long> {
    public List<Client> findByNameLikeAndSurnameLikeAndPhoneLikeAndEmailLikeAndTagsLike(String namePattern, String surnamePattern, String phonePattern, String emailPattern, String tagsPattern);

//    public Client findByEmail(String email);

    public Client findByPhone(String phone);

    public Client findByName(String name);

    public Client findByUsername(String username);
    
    /*
     * Edited By Velichko A. start
     */
    @Query ("SELECT c FROM clients c WHERE c.group = :group")
    public List <Client> findClientsByGroup(@Param ("group") Group group);
    /*
     * end
     */
    
}
