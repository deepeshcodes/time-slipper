# time-slipper
Java action game with a time-stop mechanic: standing still freezes time. Originally built by me, then given a horror layer (darkness, heartbeat, whispers, jumpscare) with Claude Code for a hackathon.
# Time Slipper

A top-down horror-action game written in Java (Swing/AWT). You play as **Cable**, an officer who can stop time by standing still, and fight through two levels of enemies to reach the boss, **MechaFlyingGoblinRobot**, in a darkened final level.

> **Content warning:** the game has darkness, a loud jumpscare, and a heartbeat sound. Turn your volume down a little before you start. Press **H** in game to switch the horror effects on or off.

## Background

Time Slipper was one of the game ideas I came up with for **Hacktoberfest Hack Day Toronto x Hack the 6ix (October 2026)**. It was not the idea my team chose for the final submission. We built a different project, [Claude Code Kills You](https://github.com/BenIIVVI/claudecodekillsyou), which I'm also listed as a team member on. I'm keeping Time Slipper here as a separate project.

## Gameplay

- Talk to the wizard to learn your mission, then fight through Level 1 and Level 2.
- Your power: **standing still stops time**, which lets you dodge everything.
- Defeat the boss in the final level, where your powers don't work.
- A horror layer sits on top of the game: darkness with a flickering light around the player, a red vignette when enemies get close, a heartbeat that speeds up, whispers, and a rare jumpscare.

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
git clone https://github.com/<your-username>/<repo-name>.git
cd <repo-name>

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

- Created by Deepesh Arun.
- AI tools: *[add this honestly. For example: "The horror layer (Horror.java) was written with help from Claude Code." Edit to match what actually happened.]*
- Fonts: SuperLegendBoy and Emulogic. Check each font's license before redistributing.
