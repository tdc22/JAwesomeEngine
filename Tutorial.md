Tutorial
==============

[0]: Index
[1]: Introduction

##[0]
[1](#[1])

##[1]
In this tutorial you'll learn the basics of the JAwesomeEngine and at the end of it you'll have a very basic and simple game. If you havn't set the engine up yet read [Setup](../SETUP.md) first.

##Part 1: Display and basic rendering
In this part of the tutorial you're going to create a basic display window with just a white rendered box and a camera you can move around freely. But before you start set up a new project according to [Setup](../SETUP.md).  
Now we start by creating two classes: "Start" and "Tutorial". Start will just contain the following code:  
```java
public class Start {
	public static void main(String[] args) {
		Tutorial tutorial = new Tutorial();
		tutorial.start();
	}
}
```
(Yes, you can also put this into the Tutorial-class but I like to seperate that.)  
  
Now that we have the Start-class we can create the Tutorial-class which will contain the actual code. We start by including the basic structure of the JAwesomeEngine:
```java
public class Tutorial extends StandardGame {

	@Override
	public void init() {
		
	}
  
	@Override
	public void update(int delta) {
		
	}

	@Override
	public void render() {
		
	}

	@Override
	public void render2d() {
		
	}

}
```
So let me explain it: we're starting in the first line by extending the class StandardGame which is the base class which you can use for any game or application using JAwesomeEngine. Tutorial inherits from StandardGame and therefore has to implement the abstract methods: update, render, render2d and init.  
The method init() is called once at the start of the program. It can be used to create objects, load data or initialize the scene. The method update(int delta) gets called once per rendered frame. The parameter gives you the passed time since the last frame in milliseconds. You can use that method to update the game logic, move objects, etc.. The last two methods "render" and "render2d" are called once per frame too but after the update-method. They should just be used to render the objects in the scene. You should also be careful that render2d() just gets called if you have added 2d-Objects (Object2d) or activated it manually but more about this later.  
If you try to run the current code (right-click on "Start.java" -> Run As -> Java Application) you'll probably get an error because we havn't created a display yet. We'll change this now by adding some lines to the init() method:
```java
	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(),
				new VideoSettings());
	}
```
The method initDisplay creates a display using the settings given by the parameters. If you run the code now, you should see a black and empty window, if not check the [Setup](../SETUP.md)-page again.  
Next, we'll add a simple box that will represent the player later. For that we start by declaring the player in the Tutorial-class:
```java
	Box player;
```
(don't forget to choose the right import location, in this case: shape.Box)  
But we also have to initialize it and add it to the scene. For that we have to edit the init()-method:
```java
	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(),
				new VideoSettings());
		
		player = new Box(0,0,0,1,1.7f,1);
		this.addObject(player);
	}
```
The parameters of the box are x, y, z, width, height, depth.  
Now we run the code and see.... nothing? What has gone wrong? Well, two things are still wrong:  
1. The box doesn't get rendered. For that we edit the render()-method. We could simply add player.render(); and it would be fixed but we would have to do this for every object we add to the scene and we don't want to do this, so instead we use:
```java
	@Override
	public void render() {
		renderScene();
	}
```
Now, every object that gets added via addObject(RenderedObject obj) will be rendered and drawn to the screen.  
2. Still we can't see anything because the Camera is in the wrong location. To fix this we edit the init()-method again and add:
```java
	cam.translateTo(0f, 0f, 5);
	cam.rotateTo(0, 0);
```
  
In addition we also want to move the camera around freely by using the mouse and arrow- or wasd-keys. For that you can simply use the default fly-cam by adding
```java
	display.bindMouse();
	cam.setFlyCam(true);
```
to init() and edit update(int delta):
```java
	@Override
	public void update(int delta) {
		cam.update(delta);
	}
```
  
So the entire code should look like that:
```java
import shape.Box;
import game.StandardGame;
import gui.DisplayMode;
import gui.GLDisplay;
import gui.PixelFormat;
import gui.VideoSettings;

public class Tutorial extends StandardGame {
	Box player;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(),
				new VideoSettings());
		display.bindMouse();
		cam.setFlyCam(true);
		cam.translateTo(0f, 0f, 5);
		cam.rotateTo(0, 0);
		
		player = new Box(0,0,0,1,1.7f,1);
		this.addObject(player);
	}
	
	@Override
	public void update(int delta) {
		cam.update(delta);
	}

	@Override
	public void render() {
		renderScene();
	}

	@Override
	public void render2d() {
		
	}

}
```
Now we actually see the white box in the center of the screen and you're able to move the camera around using the mouse and the arrow- or wasd-keys. That's it for the first part.

##Part 2: Input

##Part 3: Simple physics

##Part 4: Shaders

##Part 5: 2d and GUI
