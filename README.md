# Space invaders demo project

This is a very basic space invaders demo for use in one of my classes to show some basic Swing/AWT code.

This is not a good example of how to make a game, it is just there to give some hints as to what one could do to create a very basic game.

## Building

Build using maven with `./mvnw package`.

## Running

Run the app with `./mvnw exec:java`.

## Architecture Overview

This project demonstrates a basic game architecture using Java Swing/AWT. Here's what you need to
know to understand and modify the code safely:

### Class Structure

The game consists of four main classes:

1. **App.java** - The entry point
   - Creates the main JFrame window
   - Initializes the GameSurface (game panel)
   - Sets up the window properties and event listeners
   - This is where the application starts

2. **GameSurface.java** - The core game logic and rendering
   - Extends JPanel and implements KeyListener
   - Manages all game state (aliens, spaceship, game over flag)
   - Handles two main responsibilities:
     - **Rendering** (`paintComponent()` and `drawSurface()`): Draws everything on screen
     - **Game logic** (`update()`): Updates positions, detects collisions, spawns aliens
   - Processes keyboard input for ship movement
   - Creates and starts the FrameUpdater thread

3. **FrameUpdater.java** - The game loop timing mechanism
   - A dedicated Thread that drives the game loop
   - Calls `update()` and `repaint()` on GameSurface at a target frame rate (60 FPS)
   - Uses precise timing with nanoseconds to maintain consistent speed
   - Runs independently of the rendering to keep the game smooth

4. **Alien.java** - A simple data class
   - Represents a single alien enemy
   - Contains position/size (bounds) and creation time
   - Note: This is intentionally simple and could be improved with better encapsulation

### How the Game Loop Works

1. **Initialization**: App creates GameSurface, which starts FrameUpdater
2. **Loop cycle** (60 times per second):
   - FrameUpdater calls `GameSurface.update(time)` with elapsed time in milliseconds
   - `update()` moves aliens based on elapsed time, checks collisions, spawns new aliens
   - FrameUpdater triggers `repaint()` which eventually calls `paintComponent()`
   - `paintComponent()` redraws everything at their current positions
3. **Input**: Key releases are captured and move the spaceship immediately

### Key Design Concepts

- **Time-based movement**: Aliens move based on elapsed time (not frame count), making movement smooth and consistent regardless of frame rate
- **Separation of concerns**: Update logic is separate from rendering logic

### Important Notes for Safe Modifications

- Always update game positions in `update()`, not in `paintComponent()` - painting should only read state, not modify it
- Remember that movement should be time-based: multiply speed by elapsed time
- When adding/removing from lists during iteration, collect items to remove in a separate list (see how aliens are removed)
- The game loop runs on a background thread, so be aware of potential threading issues when accessing shared data
