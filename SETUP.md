Setup
==============

##Dependencies
* JAwesomeBase
* JAwesomeEngine
  * JAwesomeBase
  * disruptor.jar (lwjgl)
  * jinput.jar (JInput)
  * lwjgl.jar (lwjgl)
    * lwjgl native
* JAwesomePhysics
  * JAwesomeBase

##Instructions
The following instructions show you how to setup JAwesomeEngine in [Eclipse](https://eclipse.org/).

###Method 1: pull recent build via git
For this explanation I'll use [EGit](http://eclipse.org/egit/) but you can use any other way to access the repository or download it manually.  
Once you have the prequesites (Eclipse with EGit) set up correctly you have to open the Git-Perspective (Window -> Open Perspective -> Other... -> Git) and then click the "Clone a git repository and open it to view"-button. In the following dialog you enter in the URI-Field "https://github.com/tdc22/JAwesomeEngine.git" and press "Next". Then the branch master should be ticked and you click "Next" again. In the next dialog you enter a clone location for "Directory" and select "Import all existing project after clone finishes".  
Now you have to setup the project dependencies correctly. For this you go back to the Java-Perspective. In that you right-click JAwesomeEngine (Properties -> Java Build Path -> Projects) and add JAwesomeBase and JAwesomePhysics. Then you switch to "Libraries" (in Java Build Path) and add (via "Add External JARs...") disruptor.jar and lwjgl.jar (both in: [clone location]/libs/lwjgl/jar/) and jinput.jar ([clone location]/libs/JInput/jinput.jar). Finally you have to set the native path for lwjgl.jar (arrow next to jar -> Native library location -> Edit...) to ([clone location]/libs/lwjgl/native/[your OS]/[your architecture]/) and for jinput.jar to ([clone location]/libs/JInput/).  
For JAwesomePhysics you have to add the project "JAwesomeBase" in the project dialog again. (Properties -> Java Build Path -> Projects)  
  
Note: It can always be that there are certain syntax errors in the engine because I'm not always updating old or unnecessary classes but if you still can't run basic tests (like the display-test) make sure that every src-folder in the Java-Perspective is marked as source folder and that every project is referenced to a JRE-library.
  
After this you have to add for every project using JAwesomeEngine the projects JAwesomeBase, JAwesomeEngine and JAwesomePhysics in the same dialog as before and you are ready to start!  
You can check if everything is running by adding the projects to the AwesomeTests dependencies and try running e.g. the "DisplayTest" in the package "display". (via "Start.java")  
Whenever you want to update the engine to the most recent version all you have to do is to right click JAwesomeEngine (or one of the other projects) and go to Team -> Pull.

###Method 2: download latest .jar files
--- not available yet ---
