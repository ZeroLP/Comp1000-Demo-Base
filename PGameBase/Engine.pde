import java.util.*;

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