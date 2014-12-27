Tutorial
==============

##Index

##Introduction
In this tutorial you'll learn the basics of the JAwesomeEngine and at the end of it you'll have a very basic and simple game. If you havn't set the engine up yet read [Setup](https://github.com/tdc22/JAwesomeEngine/blob/master/SETUP.md) first.

##Part 1: Display and basic rendering
In this part of the tutorial you're going to create a basic display window with just a white rendered cube and a camera you can move around freely. But before you start set up a new project according to [Setup](https://github.com/tdc22/JAwesomeEngine/blob/master/SETUP.md).  
Now we start by creating two classes: "Start" and "Tutorial". Start will just contain the following code:  
```java
public class Start {
	public static void main(String[] args) {
		Tutorial tutorial = new Tutorial();
		tutorial.start();
	}
}
```
(Yes, you can also put this into the Tutorial-class but I like to seperate that)  
  
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
The method initDisplay creates a display using the settings given by the parameters. If you run the code now, you should see a black and empty window, if not check the [Setup](https://github.com/tdc22/JAwesomeEngine/blob/master/SETUP.md)-page again.

##Part 2: Input

##Part 3: Simple physics

##Part 4: Shaders

##Part 5: 2d and GUI
