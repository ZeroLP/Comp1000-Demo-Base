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