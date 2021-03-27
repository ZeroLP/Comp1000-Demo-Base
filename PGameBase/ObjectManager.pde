import java.util.*;

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