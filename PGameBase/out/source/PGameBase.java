import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.*; 
import java.util.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class PGameBase extends PApplet {

/* 
  This is the main entrypoint "class" for the game or for Processing in our case.
  Everything is initialised here and spread out from this class/entrypoint*
*/

//This is our main handle to the game engine.
//Properties and methods from the engine is accessed through this property.
public static Engine gameEngineHandle;

//This is our main handle to the renderer.
//Use this to render shapes, primitives and sprites.
public static RenderFactory renderer;
private final int rendererResetColour = color(50, 59, 79);

//Processing's main entrypoint method.
//Similar to public static void Main(string[] args) OR int main()
public void setup()
{
    //for full screen: set x and y to displayWidth and displayHeight
    

    //Set our handle to the renderer
    renderer = new RenderFactory();

    //Set our handle to the game engine and initialise relevant settings.
    gameEngineHandle = new Engine();
    gameEngineHandle.initialise();
}

//Cached tick in milliseconds since the last call to update.
public static int applicationLastTick = 0;

//Constant tick rate in milliseconds to execute update and draw calls
//It is set at 16ms since in order to achieve 60 FPS, update and draw calls needs to be executed 60 times per second
//which is every 16ms -> 1000/60 (1/60)
private final int mainClockTickRate = 16;

//Main update call for the game.
//Everything related to the game is MAINLY updated here.
public void update()
{
    gameEngineHandle.update(gameEngineHandle.gameTime);
}

//Main draw call for the game.
//Everything related to rendering graphics is MAINLY updated here.
public void draw()
{
    int elapsedExecutionDeltaTime = millis();
    gameEngineHandle.gameTime.totalGameTime = elapsedExecutionDeltaTime;

    if(elapsedExecutionDeltaTime > applicationLastTick + mainClockTickRate)
    {
        update(); //run our update method first

        clear(); //clears all PGraphics resources - such as line, rect, circle etc.
        background(rendererResetColour); //resets the back buffer / canvas to our desidered colour
        gameEngineHandle.render(); //run our render method to draw game graphics

        applicationLastTick = elapsedExecutionDeltaTime;
        gameEngineHandle.gameTime.elapsedGameTime = applicationLastTick;
    }
    else 
    {
        delay(max(0, mainClockTickRate  - (elapsedExecutionDeltaTime - applicationLastTick)));
    }
}

//Main input handling for the game.
//Everything related to inputs is MAINLY updated here.
public void keyPressed() 
{
    gameEngineHandle.keyPressed();   
}


public class Engine implements EventSubscriber
{
    public GameTimeStruct gameTime;
    public Events eventHandler;

    public void initialise()
    {
        Tools.Logger.log("Initialising the engine...");

        gameTime = new GameTimeStruct();
        eventHandler = new Events();

        gameEngineHandle.eventHandler.addSubscriber(gameEngineHandle);
        ObjectManager.populateObjects();

        Tools.Logger.log("Sucessfully initialised the engine.");
    }

    //Called when the game should update. 
    //It is called multiple times per second to update the game's state
    public void update(GameTimeStruct gTime)
    {
        for(int gObjIndex = 0; gObjIndex < ObjectManager.objectCollection.size(); gObjIndex++)
        {
            Objects.GameObject gObj = ObjectManager.objectCollection.get(gObjIndex);
            gObj.update(gTime);
        }
    }

    //Called when the game should draw a frame.
    //It is also called multiple times per second to update backbuffer of the graphics
    public void render()
    {
        for(int gObjIndex = 0; gObjIndex < ObjectManager.objectCollection.size(); gObjIndex++)
        {
            Objects.GameObject gObj = ObjectManager.objectCollection.get(gObjIndex);
            gObj.render();
        }
    }

    //Called when the game detects input registeration.
    public void keyPressed()
    {
        if(key == ESC)
           exit();

        if(key == CODED)
        {
            switch (keyCode) 
            {
                case RIGHT:
                  ObjectManager.localPlayer.Walk(true);
                  break;
                case LEFT:
                  ObjectManager.localPlayer.Walk(false);
                  break;
                default:
                   //Do something for other keys that aren't checked with cases
                break;	
            }
        }
    }

    @Override
    public void onAddObject(Objects.GameObject gObject, int currGameTime)
    {
        Tools.Logger.log("onAddObject was raised for the game object: " + gObject.objectName + " at: " + currGameTime);
    }

    @Override
    public void onDeleteObject(Objects.GameObject gObject, int currGameTime)
    {
        Tools.Logger.log("onDeleteObject was raised for the game object: " + gObject.objectName + " at: " + currGameTime);
    }

    public class GameTimeStruct
    {
        //Total elapsed game time in milliseconds since the start of the application
        public int totalGameTime;

        //Time in milliseconds since the last call to update(GameTime)
        public int elapsedGameTime;
    }
}
//This is where we declare our event prototypes.
public interface EventSubscriber
{
     public void onAddObject(Objects.GameObject gObj, int currGameTime);
     public void onDeleteObject(Objects.GameObject gObj, int currGameTime);
}

//This is where we handle, subscribe and raise our events.
//Take it as like a youtube channel sub and post notification
public class Events
{
    private List<EventSubscriber> subscribers = new ArrayList<EventSubscriber>();

    public void addSubscriber(EventSubscriber sub)
    {
        subscribers.add(sub);
    }

    public void invokeOnAddObject(Objects.GameObject gObj, int currGameTime)
    {
        //foreach(EventSubscriber sub in subscribers)
        for(EventSubscriber sub : subscribers)
            sub.onAddObject(gObj, currGameTime);
    }

    public void invokeOnDeleteObject(Objects.GameObject gObj, int currGameTime)
    {
         //foreach(EventSubscriber sub in subscribers)
        for(EventSubscriber sub : subscribers)
            sub.onDeleteObject(gObj, currGameTime);
    }
}
//Our extension class for any extra structs, classes and etc thats needs custom implementation
public static class Extensions
{
    //Constructs a new vector with given x and y coordinates relative to the display.
    //Often known as a Point.
    public static class Vector2
    {
        public Vector2(int xCoord, int yCoord)
        {
            this.x = xCoord;
            this.y = yCoord;
        }

        public int x;
        public int y;

        public String toString() { return new String(x + ", " + y); }
    }

    //Constructs a new rectangle with given x and y coordinates or Vector2 and with relative size.
    public static class Rectangle
    {
        public Rectangle(int rectX, int rectY, int rectWidth, int rectHeight)
        {
            this.x = rectX;
            this.y = rectY;
            this.rWidth = rectWidth;
            this.rHeight = rectHeight;
        }

        public Rectangle(Extensions.Vector2 rectPos, int rectWidth, int rectHeight)
        {
            this._positionVector = rectPos;
            this.rWidth = rectWidth;
            this.rHeight = rectHeight;
        }

        public int x, y, rWidth, rHeight;
        
        private Extensions.Vector2 _positionVector;
        public Extensions.Vector2 getVector2() 
        {
            if(_positionVector != null)
               return _positionVector;
            else
               return new Extensions.Vector2(x, y);
        }

        public int getLeft() { return x; }
        public int getRight() { return x + rWidth; }
        public int getTop() { return y; }
        public int getBottom() { return y + rHeight; }

        public String toString() { return new String("X: " + x + " Y: " + y + " Width: " + rWidth + " Height: " + rHeight); }
    }
}


public static class ObjectManager
{
    //Our main handle to the localPlayer
    public static Objects.LocalPlayer localPlayer;
    //This is where all the game object resides in.
    public static ArrayList<Objects.GameObject> objectCollection;

    public static boolean IsPopulated = objectCollection != null && objectCollection.size() >= 1 && localPlayer != null;

    //Populates the object collection / assigns objects.
    //Currently it just assigns local player and adds it to the collection.
    public static void populateObjects()
    {
        if(!IsPopulated)
        {
            Tools.Logger.log("Populating ObjectManager");

            localPlayer = new Objects.LocalPlayer(new Extensions.Vector2(300, 300));
            localPlayer.objectName = "Local";

            objectCollection = new ArrayList<Objects.GameObject>();
            addObject(localPlayer);

            Tools.Logger.log("Successfully populated ObjectManager: " + objectCollection.size());
        }
    }

    //Adds a game object to the main collection.
    public static boolean addObject(Objects.GameObject gObjectToAdd)
    {
        //lock (objectCollection)
        //this "synchronized" keyword locks the object collection in palce, 
        //so it's not modified when being accessed by this method. 
        //After the execution is completed inside the block, it is then released for other awaiting threads to access it.
        synchronized (objectCollection)
        {
            try
            {
                objectCollection.add(gObjectToAdd);
                gameEngineHandle.eventHandler.invokeOnAddObject(gObjectToAdd, gameEngineHandle.gameTime.totalGameTime);

                return true;
            }
            catch (Exception e) 
            { 
                Tools.Logger.log("Failed to add object: " + e); 
                return false; 
            }
        }
    }

    //Deletes a game object to the main collection.
    public static boolean deleteObject(Objects.GameObject gObjectToDelete)
    {
        //lock (objectCollection)
        //this "synchronized" keyword locks the object collection in palce, 
        //so it's not modified when being accessed by this method. 
        //After the execution is completed inside the block, it is then released for other awaiting threads to access it.
        synchronized (objectCollection)
        {
            try
            {
                if(objectCollection.contains(gObjectToDelete))
                {
                    objectCollection.remove(gObjectToDelete);
                    gameEngineHandle.eventHandler.invokeOnDeleteObject(gObjectToDelete, gameEngineHandle.gameTime.totalGameTime);

                    return true;
                }
                else { return false; }
            }
            catch (Exception e) 
            { 
                Tools.Logger.log("Failed to delete object: " + e); 
                return false; 
            }
        }
    }
}
public static class Objects
{
    public static class GameObject
    {
        public GameObject(Extensions.Vector2 wPosition)
        {
            this.worldPosition = wPosition;
            //charSprite = new SpriteClient();
        }

        //public SpriteClient charSprite;
        public boolean charFlipState = false;
        public ActionState currentActionState = ActionState.Idle;
        public String objectName = "Unknown_Obj";
        public int health = 100;
        public int mana = 100;
        public int movementSpeed = 0;
        public Extensions.Vector2 worldPosition = new Extensions.Vector2(0, 0);
        public int boundingRadius = 80;
         
        public Extensions.Rectangle getBoundingBox()
        {
            return new Extensions.Rectangle(worldPosition.x - (boundingRadius / 2), worldPosition.y - boundingRadius,
                                            boundingRadius, boundingRadius);
        };

        public void drawBoundingBox() { renderer.drawRectangle(getBoundingBox(), renderer.generateColor(144, 238, 144)); }

        public void update(Engine.GameTimeStruct gameTime) { }

        public void render()
        {
            drawBoundingBox(); 
        }

        public enum ActionState
        {
            Idle,
            Walk
        } 
    }

    public static class LocalPlayer extends GameObject
    {
        public LocalPlayer(Extensions.Vector2 wPosition)
        {
            super(wPosition);
            this.movementSpeed = 5;

            //AnimationSprite.idleAnimationSprite = new Sprite(renderer.ldrLoadImage("Idle.png"), 0.2f, true);
        }

        /*public static class AnimationSprite
        {
            public static Sprite idleAnimationSprite;
        }*/

        @Override
        public void update(Engine.GameTimeStruct gTime)
        {
            //this.charSprite.animateSprite(AnimationSprite.idleAnimationSprite);

            super.update(gTime);
        }

        @Override
        public void render()
        {
            /*if(AnimationSprite.idleAnimationSprite != null)
               this.charSprite.drawSprite(this.worldPosition);*/

            super.render();
        }

        public void Walk(boolean isWalkRight)
        {
            if(isWalkRight)
               this.worldPosition.x += this.movementSpeed;
            else
               this.worldPosition.x -= this.movementSpeed;

            checkBoundsAndCalibratePosition();

            this.currentActionState = ActionState.Walk;
        }

        public void checkBoundsAndCalibratePosition()
        {
            if(this.getBoundingBox().getLeft() <= 0)
               this.worldPosition.x = getBoundingBox().rWidth / 2;
            else if(this.getBoundingBox().getRight() > renderer.getPreferredBackBufferWidth())
               this.worldPosition.x = renderer.getPreferredBackBufferWidth() - (this.getBoundingBox().rWidth / 2) - 1;
        }
    }

    /*
    public static class SpriteClient
    {
        private Sprite _sprite;
        public Sprite getSprite() { return _sprite; }

        private int _currentFrameIndex;
        public int getCurrentFrameIndex() { return _currentFrameIndex; }

        private float elapsedTimeOfFrameShown;

        public Extensions.Vector2 getOrigin()
        {
            return new Extensions.Vector2(_sprite.getFrameWidth() / 2, _sprite.getFrameHeight());
        }

        public void animateSprite(Sprite passedSprite)
        {
            if(_sprite == passedSprite)
               return;

            _sprite = passedSprite;
            _currentFrameIndex = 0;
            elapsedTimeOfFrameShown = 0.0f;
        }

        public void drawSprite(Extensions.Vector2 drawPos)
        {
            if(_sprite == null)
               return;
            
            elapsedTimeOfFrameShown += (float)(gameEngineHandle.gameTime.elapsedGameTime * 1000);

            while (elapsedTimeOfFrameShown > _sprite.getFrameTime()) 
            {
                elapsedTimeOfFrameShown -= _sprite.getFrameTime();

                if(_sprite.getIsLoopingState())
                   _currentFrameIndex = (_currentFrameIndex + 1) % _sprite.getNumberOfFrames();
                else
                   _currentFrameIndex = min(_currentFrameIndex + 1, _sprite.getNumberOfFrames() - 1);
            }

            renderer.ldrDrawImage(_sprite.getFrames().get(getCurrentFrameIndex()), drawPos.x, drawPos.y);
        }
    }

    public static class Sprite
    {
        public Sprite(PImage mod, float fTime, boolean isLoop)
        {
            this._model = mod;
            this._frameTime = fTime;
            this._isLooping = isLoop;
            this._frames = new ArrayList<PImage>();

            for(int i = 0; i < getNumberOfFrames(); i++)
            {
                 _frames.add(_model.get(i * getFrameWidth(), 0, getFrameWidth(), getFrameHeight()));
            }
        }

        private ArrayList<PImage> _frames;
        public ArrayList<PImage> getFrames() { return _frames; } 

        private PImage _model;
        //All frames in the animation arranged horizontally.
        public PImage getModel() { return _model; }
        
        //Duration of time to show each frame.
        private float _frameTime;
        public float getFrameTime() { return _frameTime; }

        //When the end of the animation is reached, should it
        //continue playing from the beginning?
        private boolean _isLooping;
        public boolean getIsLoopingState() { return _isLooping; }

        // Gets the width of a frame in the animation.
        public int getFrameWidth() {return _model.height; } //Assume square frames.

        //Gets the height of a frame in the animation.
        public int getFrameHeight() { return _model.height; } //Assume square frames also.
        
        // Gets the number of frames in the animation.
        public int getNumberOfFrames() { return _model.width / getFrameHeight(); } //Assume square frames also 3.
    }*/
}
public class RenderFactory
{   
    public void drawLine(Extensions.Vector2 startVec2, Extensions.Vector2 endVec2)
    {
        line(startVec2.x, startVec2.y, endVec2.x, endVec2.y);
    }

    public void drawRectangle(Extensions.Vector2 vec2Loc, int rWidth, int rHeight, int cl)
    {
        //Left
        drawLine(vec2Loc, new Extensions.Vector2(vec2Loc.x, vec2Loc.y + rHeight));
        stroke(cl);

        //Right
        drawLine(new Extensions.Vector2(vec2Loc.x + rWidth, vec2Loc.y), 
                 new Extensions.Vector2(vec2Loc.x + rWidth, vec2Loc.y + rHeight));
        stroke(cl);
        
        //Top
        drawLine(vec2Loc, new Extensions.Vector2(vec2Loc.x + rWidth, vec2Loc.y));
        stroke(cl);

        //Bottom
        drawLine(new Extensions.Vector2(vec2Loc.x, vec2Loc.y + rHeight), 
                 new Extensions.Vector2(vec2Loc.x + rWidth, vec2Loc.y + rHeight));
        stroke(cl);
    }

    public void drawRectangle(Extensions.Rectangle rectToDraw, int cl)
    {
        //Left
        drawLine(rectToDraw.getVector2(), new Extensions.Vector2(rectToDraw.x, rectToDraw.y + rectToDraw.rHeight));
        stroke(cl);

        //Right
        drawLine(new Extensions.Vector2(rectToDraw.x + rectToDraw.rWidth, rectToDraw.y), 
                 new Extensions.Vector2(rectToDraw.x + rectToDraw.rWidth, rectToDraw.y + rectToDraw.rHeight));
        stroke(cl);
        
        //Top
        drawLine(rectToDraw.getVector2(), new Extensions.Vector2(rectToDraw.x + rectToDraw.rWidth, rectToDraw.y));
        stroke(cl);

        //Bottom
        drawLine(new Extensions.Vector2(rectToDraw.x, rectToDraw.y + rectToDraw.rHeight), 
                 new Extensions.Vector2(rectToDraw.x + rectToDraw.rWidth, rectToDraw.y + rectToDraw.rHeight));
        stroke(cl);
    }

    public int getPreferredBackBufferWidth() { return width; }
    public int getPreferredBackBufferHeight() { return height; }
    
    public int generateColor(int r, int g, int b) { return color(r, g, b); } 

    public void pushMatrixWrapper() { pushMatrix(); }
    public void popMatrixWrapper() { popMatrix(); }
    public void scaleWrapper(float a, float b, float c) { scale(a, b, c); }
    public void beginShapeWrapper() { beginShape(); }
    public void endShapeWrapper() { endShape(); }
    public void vertexWrapper(float x, float y, float u, float v) { vertex(x, y, u, v); }
    public void textureWrapper(PImage img) { texture(img); }

    public PImage ldrLoadImage(String fileName) { return loadImage(fileName); }
    public void ldrDrawImage(PImage imgToDraw, float x, float y, float w, float h) { image(imgToDraw, x, y, w, h); }
    public void ldrDrawImage(PImage imgToDraw, float x, float y) { image(imgToDraw, x, y); }
}
public static class Tools
{
    public static class Logger
    {
        public static void log(Object dataToLog)
        {
            println(dataToLog.toString());
        }
    }
}
  public void settings() {  size(1134, 550); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "PGameBase" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
