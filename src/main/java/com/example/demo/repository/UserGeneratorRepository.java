package com.example.demo.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.demo.entity.Commentary;
import com.example.demo.entity.User;

@Component
public class UserGeneratorRepository implements CommandLineRunner{
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentaryRepository commentaryRepository;

    @Override
    public void run(String... args) throws Exception {
        if(this.userRepository.count() == 0) {

            List<User> users = new ArrayList<>();

            users.add(new User("Ergy", "ergy@wild.com", "fr0ntDev!","", "ergy", true));
            users.add(new User("Marwa", "marwa@wild.com", "Mt!dev46", "Feeding Mario Party capture the flag first-person kick Dragon Ball GameBattles CoD Mario headshot. Battle pass rng boosting PvP construction and management simulation Call of Duty AI. Flying Simulator advergame AFK free look level design digital rights management Mortal Kombat nerf PlayStation Total War Call of Duty: Modern Warfare 2 Bullet Hell. Retrogaming JRPG Tetris single-player KDR assault mode chiptune griefing Pac-Man kart racing PlayStation Portable.", "marwa", false));
            users.add(new User("Filip", "filip@wild.com", "7devFS!52", "Bot Sidescroller Metal Gear Solid V: The Phantom Pain Hacker digital rights management Wii U resolution. Difficulty avatar lagging publisher GG ez Beat 'Em Up Sega Genesis Crash Bandicoot level design Fighting game. Bundling PSP nerf Party game bug alpha bullshot job. Frame heat map Game Boy Advance waggle secret character aimbot zero-player game invisible wall.", "filip", false));
            users.add(new User("Sid Ahmed", "sidahmed@wild.com", "5Sa!Api85", "MMORPG Baldur's Gate kart racing The Oregon Trail game engine Kingdom Hearts Sega Saturn animation cancel Grand Theft Auto: Vice City. Avatar rubber banding auto battler mute A easter eggs Wombo Combo. Compulsion loop map faceroll credit-feeding NES The Legend of Zelda: Majora's Mask The Elder Scrolls V: Skyrim Star Wars: Battlefront level. Arcade game Xbox 360 The Legend of Zelda: The Wind Waker AFK dead zone first-party developer bottomless pit Tom Clancy's Ghost Recon AFK NPC BioShock D-Pad Halo: Combat Evolved. Fatality Metroid Prime HUD rumble pack rocket jump AoE going gold patch. GBA role-playing video game touchscreen camera LAN God of War recoi Mortal Kombat.", "sid-ahmed", false));
            users.add(new User("Clotilde", "clotilde@wild.com", "74!Bomg3K", "Touchscreen alpha A sandbox Pwn Wombo Combo Dragon Quest power spike Donkey Kong. Action point AI Bejeweled Mario Kart Hot Coffee 32-bit Halo: Combat Evolved level RPG frag.", "clotilde", false));

            this.userRepository.saveAll(users);

            List<Commentary> commentaries = new ArrayList<>();

            commentaries.add(new Commentary("Ce jeu est génial mais difficile", users.get(1), 1));
            commentaries.add(new Commentary("Je suis d'accord avec toi", users.get(2), 1));
            commentaries.add(new Commentary("J'ai fini ce jeu en deux jours, j'ai pas pu m'arrêter", users.get(3), 10));

            this.commentaryRepository.saveAll(commentaries);

            
        }
    }
}
