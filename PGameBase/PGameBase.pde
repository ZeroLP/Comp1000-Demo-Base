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
private final color rendererResetColour = color(50, 59, 79);

//Processing's main entrypoint method.
//Similar to public static void Main(string[] args) OR int main()
public void setup()
{
    //for full screen: set x and y to displayWidth and displayHeight
    size(1134, 550);

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