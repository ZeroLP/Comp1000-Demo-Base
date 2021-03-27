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