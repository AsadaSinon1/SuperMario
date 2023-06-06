# SuperMario

This is the final project of the "Java Programming" course at Peking University. Our team name is "So What Should We Call Ourselves?"

# Project Roles

In this section, we introduce the project roles and contributions of our team members.

**_Jiakai Mao_** (AsadaSinon1) \
Merge and modify part of other contributors' code. \
Responsible for Design well-balanced levels. \
Responsible for realizing the game background and level internal interface.

**_Dongyi Li_** (rcvjqigp-2) \
Handle Mario's movement,health reduction, level switching, and other related functionalities. \
Design and debug mario's motion-related values.

**_Zhiyue Feng_** (FZYsheep) \
Design and implement the start screen, login interface and level selection interface. \
Use a database to manage users' data.

**_Yutong Yang_** (Selina233) \
Adjust Mario's attributes and values. \
Find and create various artistic textures and graphics. \
Perpare PowerPoint presentation and create a demo video. \
Handle enemy movement, death, and related behaviors.

We have achieved a fair distribution of work, and each member has contributed to this project.

# Updates

In this section, we introduce the important updates.

### May 16th

- We successfully formed a team and created this repository on GitHub.

### May 23rd

- We have completed the testing of Mario's wall jump mechanic and plan to incorporate the sprint and long jump mechanics.

### May 27th

- We finished the beautiful Game enter interface and uploaded all the textures of Mario

### May 30th

- We implemented the Enemy class and its subclass Mushroom.
- The Mario class implemented the ability to sprint.
- We completed the initial code integration and decomposed the Mario class.
- We attempted and implemented a simple map design.

### June 1st

- The Plot class has been designated as an inner class of the Map class, and corresponding inner classes with appropriate file structures have been added to facilitate future map design. This improves code maintainability.
- The Plot class implements the JPanel class and the KeyListener interface, while the Backstage class implements the JFrame class and the Runnable and ActionListener interfaces.
- We replaced the while loop in the run function with the start function of a Timer object to prevent blocking the event dispatch thread.

### June 3rd

- We have addressed several bugs to make the gameplay smoother.
- We have separated abilities such as sprinting, wall jumps, and wall grabs, making Mario's abilities controllable.
- We have introduced a password mechanism that requires SQL database support.
  
***It's a significant milestone that the game prototype has taken shape and is playable even without enemies.***
