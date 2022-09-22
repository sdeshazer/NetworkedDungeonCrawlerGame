Dungeon Quest: A 2 Player Dungeon crawler game
By:  Samantha Deshazer, Brandon Huang, and Alex Yeang

How to Play: (also refer to how to play section in game)
The goal of each level is to defeat all the enemies on the map without your health dropping to 0. Once the last enemy
is defeated, they drop a key which is used to unlock a door at the end of the level, completing the level. If all
player's health drops to 0, gameover occurs. There are several powerups which are statically placed on the map and have
a small chance to drop when an enemy is defeated. Powerups are picked up by walking over them and include, healing,
double damage, invincibility, and an extra life. See the how to play section of the game to see examples.

HOW TO RUN 2 PLAYER:
***IMPORTANT*** 2P AND JOIN FROM THE MENU WILL NOT WORK UNLESS THESE STEPS ARE FOLLOWED
1. FIRST you MUST manually place IP address of the device you are running the server on. It is located on Line 23 of the
MenuState object and the line reads:
  private String serverAddress = "<PUT_IP_HERE>";
2. Launch the Server Class
3. Launch the first instance of DungeonGame on one device and choose 2P and a character or JOIN.
  - 2P is the smart client
  - Join is the Dummy client
4. Launch the second instance of DungeonGame on a second device and choose either 2P or join, whichever you didn't
use on the first instance.
5. Once both are connected, the play button should appear on P1's screen, which when they press will launch 2P mode.
6. Have fun!

CONTROLS:
  In Menu:
    Mouse: Choose menu selection
    LClick: Select option
  In Game:
    wasd: to move the player
    Mouse: Aim
    L Click: Attack
    CHEATS IN GAME:
      M: Activate cheat mode
      Q: Skip level

ORIGINAL LOW BAR GOALS:
- 2 Player Networked Multiplayer [Complete]
- Traversable tile-based Map [Complete]
- Multiple Classes: One Archer and One knight [Complete]
- Enemies and characters have different behaviors and abilities depending on type. [Complete]
- Health tracking and damage system. [Complete]
- Win Conditions, once all enemies are defeated and the key is picked up, the player with the key can unlock the door
to the next level. [Complete]
- Multiple levels with scaling difficulty. [Complete]
- Constrained movement within the map. [Complete]
- Two enemy types, one melee and one ranged [Complete]

OTHER GOALS ACCOMPLISHED:
- Multiple powerup's were added to the game including:
  - Full health restore (Health potion)
  - Temporary invincibility (Shield)
  - Temporary double damage (Gold powder)
  - Extra Life/Revive (Watermelon)
- A large scrolling map in each level with a "camera" that follows the player and if the player dies, follows their
partner if applicable.
- Improved menu/splash screen
- Improved HUD system for displaying health, class and current powerups.
- More interesting enemy behavior:
  - Enemies only "activate" in a certain range of any player.
  - Enemies will prioritize which ever player is closer to them.
- Different assets for different levels

Art Credits:
Dungeon RPG Tileset by Rekkimaru (Tile assets): https://rekkimaru.itch.io/dungeon-rpg-tileset
16x16 RPG Assets by ssugmi (Powerups): https://ssugmi.itch.io/16x16-rpg-assets
Evolving Bows by Momentary Unicorn (Bow and Arrows): https://momentaryunicorn.itch.io/momentary-unicorns-evolving-bows-32x32
Admurin's Armory by Admurin (Swords): https://admurin.itch.io/pixel-armory
16x16 Enchanted Forest Characters by SuperDark (Player and enemy models): https://superdark.itch.io/enchanted-forest-characters

All other Art assets made/edited by Brandon or Sam

Thanks for playing!

This work is licensed under http://creativecommons.org/licenses/by/3.0/
