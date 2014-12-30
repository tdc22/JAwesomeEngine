Tutorial
==============

##Index
[Introduction](#introduction)  
[Part 1: Display and basic rendering](#part-1-display-and-basic-rendering)  
[Part 2: Input](#part-2-input)  
[Part 3: Simple physics](#part-3-simple-physics)  
[Part 4: Shaders](#part-4-shaders)  
[Part 5: 2d and GUI](#part-5-2d-and-gui)

##Introduction
In this tutorial you'll learn the basics of the JAwesomeEngine and at the end of it you'll have a very basic and simple game. If you havn't set the engine up yet read [Setup](/SETUP.md) first.

##Part 1: Display and basic rendering
In this part of the tutorial you're going to create a basic display window with just a white rendered box and a camera you can move around freely. But before you start set up a new project according to [Setup](/SETUP.md).  
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
The method initDisplay creates a display using the settings given by the parameters. If you run the code now, you should see a black and empty window, if not check the [Setup](/SETUP.md)-page again.  
Next, we'll add a simple box that will represent the player later. For that we start by initializing and adding it to the scene. For that we have to edit the init()-method:
```java
	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(),
				new VideoSettings());
		
		player = new Box(0,0,0,1,1.7f,1);
		this.addObject(player);
	}
```
(don't forget to choose the right import location, in this case: shape.Box)  
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

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(),
				new VideoSettings());
		display.bindMouse();
		cam.setFlyCam(true);
		cam.translateTo(0f, 0f, 5);
		cam.rotateTo(0, 0);
		
		Box player = new Box(0,0,0,1,1.7f,1);
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
Now we actually see the white box in the center of the screen and you're able to move the camera around using the mouse and the arrow- or wasd-keys. You can then exit by using the Escape-key. That's it for the first part.

##Part 2: Input
In this part we'll set up the input system but leave it without functionality for now. The class StandardGame, which we extended in the first part, contains the variable inputs which we use to check the inputs.  
In the end we want to have a first-person like movement and controls similar to the camera but we want to move the player object at the same time. Because of that we have to disable the flying camera first so we remove the line
```java
		cam.setFlyCam(true);
```
The camera was nice for demonstration but now we want to program our own movement.  
Now we initialize the input events:
```java
		forward = new InputEvent("Forward", new Input(
				Input.KEYBOARD_EVENT, "W", KeyInput.KEY_DOWN), new Input(
						Input.KEYBOARD_EVENT, "Up", KeyInput.KEY_DOWN));
		backward = new InputEvent("Backward", new Input(
				Input.KEYBOARD_EVENT, "S", KeyInput.KEY_DOWN), new Input(
						Input.KEYBOARD_EVENT, "Down", KeyInput.KEY_DOWN));
		left = new InputEvent("Left", new Input(
				Input.KEYBOARD_EVENT, "A", KeyInput.KEY_DOWN), new Input(
						Input.KEYBOARD_EVENT, "Left", KeyInput.KEY_DOWN));
		right = new InputEvent("Right", new Input(
				Input.KEYBOARD_EVENT, "D", KeyInput.KEY_DOWN), new Input(
						Input.KEYBOARD_EVENT, "Right", KeyInput.KEY_DOWN));
		inputs.addEvent(forward);
		inputs.addEvent(backward);
		inputs.addEvent(left);
		inputs.addEvent(right);
```
What we do here is we add an InputEvent for forward, backward, left and right movement and for each event we add a trigger for the wasd- and one for the arrow keys. Besides Keyboard events, there are also mouse and gamepad events. Moreover instead of KEY\_DOWN you can use KEY\_PRESSED and KEY\_RELEASED to trigger an event. Another thing is that you can add as many triggers (Input) as you want within the constructor of InputEvent. At the end we have to pass every event to the inputs-Object to update the status of each InputEvent.  
To test this we edit the update(int delta) method to:
```java
		if(inputs.isMouseMoved()) {
			System.out.println("mouse moved (" + inputs.getMouseDX() + "; " + inputs.getMouseDY() + ")");
		}
		if(forward.isActive()) {
			System.out.println("forward");
		}
		if(backward.isActive()) {
			System.out.println("backward");
		}
		if(left.isActive()) {
			System.out.println("left");
		}
		if(right.isActive()) {
			System.out.println("right");
		}
		
		cam.update(delta);
```
Now you can run the program again and test the events which will result in a console output. The entire code should now like like this:
```java
import input.Input;
import input.InputEvent;
import input.KeyInput;
import shape.Box;
import game.StandardGame;
import gui.DisplayMode;
import gui.GLDisplay;
import gui.PixelFormat;
import gui.VideoSettings;

public class Tutorial extends StandardGame {
	InputEvent forward, backward, left, right;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(),
				new VideoSettings());
		display.bindMouse();
		cam.translateTo(0f, 0f, 5);
		cam.rotateTo(0, 0);
		
		Box player = new Box(0,0,0,1,1.7f,1);
		this.addObject(player);
		
		forward = new InputEvent("Forward", new Input(
				Input.KEYBOARD_EVENT, "W", KeyInput.KEY_DOWN), new Input(
						Input.KEYBOARD_EVENT, "Up", KeyInput.KEY_DOWN));
		backward = new InputEvent("Backward", new Input(
				Input.KEYBOARD_EVENT, "S", KeyInput.KEY_DOWN), new Input(
						Input.KEYBOARD_EVENT, "Down", KeyInput.KEY_DOWN));
		left = new InputEvent("Left", new Input(
				Input.KEYBOARD_EVENT, "A", KeyInput.KEY_DOWN), new Input(
						Input.KEYBOARD_EVENT, "Left", KeyInput.KEY_DOWN));
		right = new InputEvent("Right", new Input(
				Input.KEYBOARD_EVENT, "D", KeyInput.KEY_DOWN), new Input(
						Input.KEYBOARD_EVENT, "Right", KeyInput.KEY_DOWN));
		inputs.addEvent(forward);
		inputs.addEvent(backward);
		inputs.addEvent(left);
		inputs.addEvent(right);
	}
	
	@Override
	public void update(int delta) {
		if(inputs.isMouseMoved()) {
			System.out.println("mouse moved (" + inputs.getMouseDX() + "; " + inputs.getMouseDY() + ")");
		}
		if(forward.isActive()) {
			System.out.println("forward");
		}
		if(backward.isActive()) {
			System.out.println("backward");
		}
		if(left.isActive()) {
			System.out.println("left");
		}
		if(right.isActive()) {
			System.out.println("right");
		}
		
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

##Part 3: Simple physics
This part is about adding physics and movement to the current game. For this we need to use the physics class PhysicsSpace which handles all the collision objects and manages the collisions. So we start by adding the decleration:
```java
	PhysicsSpace space;
```
And initialize it with:
```java
		space = new PhysicsSpace(new VerletIntegration(), new SAP(), new GJK(
				new EPA()), new LinearImpulseResolution(),
				new ProjectionCorrection(0.02f, 0.0f),
				new PersistentManifoldManager());
```
The meaning of most of these parameters is not so trivial to understand so just take it for now. Still an important one is LinearImpulseResolution which restricts the collision resolution to a linear one which is sufficient for now but if you want to change that later you can use ImpulseResolution instead.  
But we still have to add gravity by using
```java
		space.setGlobalForce(new Vector3f(0, -5, 0));
```
within the init() method which applys a global force pointing downward. By changing the magnitude of the given vector you can increase or decrease the intensity of the gravity.  
Now we can simply add our player object to the PhysicsSpace by adding the following lines:
```java
		playerbody = PhysicsShapeCreator.create(player);
		playerbody.setMass(1f);
		space.addRigidBody(player, playerbody);
```
The first line takes a game-object and generates a collision-object from it. The second one sets the mass of that object. The default value is 0 which means that it's a static object, any other value means that it's a moveable object that physically interacts with the world. You can choose any value but you should keep the values for mass in a certain range. The last one - of course - adds the object to the physics space which handles the collision and everything connected to that.  
Like stated before we ignore the rotation but if you want to have physics with the rotational part you had to add playerbody.setInertia(new Quaternionf());.  
Next we need to add the decleration:
```java
	RigidBody3 playerbody;
```
Furthermore we have to make sure that the physics gets updated every loop. To achieve that we add to the update-method:
```java
		space.update(delta);
```
If you run the game now you should see our white playerbox falling down out of sight. If not something probably went wrong and you should check your code again.  
In the last part we added input events which, so far, just output a line if the defined keys get pressed. We'll change that now and add interaction to the player. For that we need to apply a force into the direction we want to move the player in. For that we start by creating an accumulation vector:
```java
	Vector3f move = new Vector3f();
```
Then we simply add a vector pointing into the direction we want to move in:
```java
		if(forward.isActive()) {
			move = VecMath.addition(move, new Vector3f(0, 0, 1));
		}
		if(backward.isActive()) {
			move = VecMath.addition(move, new Vector3f(0, 0, -1));
		}
		if(left.isActive()) {
			move = VecMath.addition(move, new Vector3f(1, 0, 0));
		}
		if(right.isActive()) {
			move = VecMath.addition(move, new Vector3f(-1, 0, 0));
		}
```
Then we normalize the vector and apply a force to the player object:
```java
		if(move.length() > 0 ) {
			move.normalize();
			move.scale(playerspeed);
			playerbody.applyCentralForce(move);
		}
```
Of course we then set the playerspeed:
```java
	float playerspeed = 10;
```
To then stop the player from falling infinitely we add a ground within the init-method:
```java
		Box ground = new Box(0, -5, 0, 10, 1, 10);
		RigidBody3 rb = PhysicsShapeCreator.create(ground);
		space.addRigidBody(ground, rb);
		addObject(ground);
```
Because we want the ground to be static we set the mass this time to 0.  
If we start it now we see that everything seems to work so far but we don't see too much yet, because everything is still white (which we'll fix in the next part), and that the movement of the player object is a bit slippery, it feels like the player is walking on ice and sliding around way too much. To fix that we have to change the metod to move the player object. For that we change the line:
```java
			playerbody.applyCentralForce(move);
```
to
```java
		playerbody.setLinearVelocity(new Vector3f(move.x, playerbody.getLinearVelocity().y, move.z));
```
*and take it out of the if-check.*  
Now the movement feels like a more "usual" FPS-movement. But we still have to make it actually first-person.

##Part 4: Shaders

##Part 5: 2d and GUI
