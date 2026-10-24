# Time Slipper

A top-down horror-action game written in Java (Swing/AWT). You play as **Cable**, an officer who can stop time by standing still, and fight through two levels of enemies to reach the boss, **MechaFlyingGoblinRobot**, in a darkened final level.

> **Content warning:** the game has darkness, a loud jumpscare, and a heartbeat sound. Turn your volume down a little before you start. Press **H** in game to switch the horror effects on or off.
<img width="1422" height="800" alt="image" src="https://github.com/user-attachments/assets/7d2f5284-5a17-4da2-bee2-b07183e5598f" />
<img width="1536" height="857" alt="image" src="https://github.com/user-attachments/assets/9001aa6c-6151-4959-8fe6-baafc5c8380d" />
<img width="161" height="166" alt="image" src="https://github.com/user-attachments/assets/ed746ea9-b838-4349-980d-4140539f4f31" />
<img width="161" height="166" alt="image" src="https://github.com/user-attachments/assets/8db6fdca-b8c5-4711-b798-b4ee5c3de485" />

## Background

Time Slipper started as my own original game. For **Hacktoberfest Hack Day Toronto x Hack the 6ix (October 2026)**, I used Claude Code to add a horror layer on top of it. It was one of the ideas I came up with for the hackathon, but it wasn't the idea my team chose for the final submission. We built a different project, [Claude Code Kills You](https://github.com/BenIIVVI/claudecodekillsyou), which I'm also listed as a team member on. I'm keeping Time Slipper here as a separate project.

## Gameplay

- Talk to the wizard to learn your mission, then fight through Level 1 and Level 2.
- Your power: **standing still stops time**, which lets you dodge everything.
- Defeat the boss in the final level, where your powers don't work.
- The horror layer adds darkness with a flickering light around the player, a red vignette when enemies get close, a heartbeat that speeds up, whispers, and a rare jumpscare.

## Controls

| Key | Action |
| --- | --- |
| W / A / S / D | Move |
| Space | Attack / shoot |
| H | Toggle horror effects |

## Requirements

- Java 17 or newer (JDK, to compile)

## How to run

The game loads images and fonts using relative paths, so **run it from the project's root folder** (the one that contains `pictures/` and `src/`).

```bash
git clone https://github.com/deepeshcodes/time-slipper.git
cd time-slipper

# compile
mkdir bin
javac -d bin src/*.java

# run
java -cp bin TimeSlipperMain
```

Or open the folder in Eclipse (it's an Eclipse project) and run `TimeSlipperMain`.

## Project structure

```
src/
  TimeSlipperMain.java   main game loop, levels, dialogue
  Player.java            player data
  PlayerAnimator.java    player animation
  Enemy.java             enemy data
  Boss.java              boss class
  Horror.java            horror layer (darkness, sounds, whispers, jumpscare)
pictures/                sprites, backgrounds, screens
SuperLegendBoy.ttf       fonts
emulogic.ttf
```

## Tech

Java, Swing/AWT, `javax.sound.sampled` (the horror sounds are generated in code, so no audio files are needed).

## Credits

- Original game (levels, gameplay, art, and dialogue): Deepesh Arun
- Horror layer (`Horror.java`: darkness, sounds, whispers, jumpscare) for Hacktoberfest Hack Day Toronto x Hack the 6ix (Oct 2026)
- Fonts: SuperLegendBoy and Emulogic 
