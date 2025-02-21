package com.example.demo.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.demo.entity.Commentary;
import com.example.demo.entity.User;
import com.example.demo.entity.Role;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.CommentaryRepository;
import com.example.demo.repository.RoleRepository;

@Component
public class UserGeneratorRepository implements CommandLineRunner {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentaryRepository commentaryRepository;

    @Autowired
    private RoleRepository roleRepository;  

    @Override
    public void run(String... args) throws Exception {
        if (this.userRepository.count() == 0) {

            Role adminRole = roleRepository.findByName("Admin");
            if (adminRole == null) {
                adminRole = new Role("Admin");
                roleRepository.save(adminRole);
            }

            Role userRole = roleRepository.findByName("User");
            if (userRole == null) {
                userRole = new Role("User");
                roleRepository.save(userRole);
            }

           
            List<User> users = new ArrayList<>();

            User user1 = new User("Ergy", "ergy@wild.com", "fr0ntDev!", "", "ergy");
            user1.getRoles().add(adminRole); 
            users.add(user1);

            User user2 = new User("Marwa", "marwa@wild.com", "Mt!dev46", "Feeding Mario Party...", "marwa");
            user2.getRoles().add(userRole);  
            users.add(user2);

            User user3 = new User("Filip", "filip@wild.com", "7devFS!52", "Bot Sidescroller...", "filip");
            user3.getRoles().add(userRole);  
            users.add(user3);

            User user4 = new User("Sid Ahmed", "sidahmed@wild.com", "5Sa!Api85", "MMORPG Baldur's Gate...", "sid-ahmed");
            user4.getRoles().add(userRole);  
            users.add(user4);

            User user5 = new User("Clotilde", "clotilde@wild.com", "74!Bomg3K", "Touchscreen alpha...", "clotilde");
            user5.getRoles().add(userRole); 
            users.add(user5);

            this.userRepository.saveAll(users);


            List<Commentary> commentaries = new ArrayList<>();
            commentaries.add(new Commentary("Ce jeu est génial mais difficile", users.get(1), 1));
            commentaries.add(new Commentary("Je suis d'accord avec toi", users.get(2), 1));
            commentaries.add(new Commentary("J'ai fini ce jeu en deux jours, j'ai pas pu m'arrêter", users.get(3), 10));

            this.commentaryRepository.saveAll(commentaries);
        }
    }
}
