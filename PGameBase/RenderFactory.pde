public class RenderFactory
{   
    public void drawLine(Extensions.Vector2 startVec2, Extensions.Vector2 endVec2)
    {
        line(startVec2.x, startVec2.y, endVec2.x, endVec2.y);
    }

    public void drawRectangle(Extensions.Vector2 vec2Loc, int rWidth, int rHeight, color cl)
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

    public void drawRectangle(Extensions.Rectangle rectToDraw, color cl)
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
    
    public color generateColor(int r, int g, int b) { return color(r, g, b); } 

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