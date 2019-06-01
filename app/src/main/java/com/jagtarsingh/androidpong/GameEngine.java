package com.jagtarsingh.androidpong;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.constraint.solver.widgets.Rectangle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;

public class GameEngine extends SurfaceView implements Runnable {

    // -----------------------------------
    // ## ANDROID DEBUG VARIABLES
    // -----------------------------------

    // Android debug variables
    final static String TAG="PONG-GAME";

    // -----------------------------------
    // ## SCREEN & DRAWING SETUP VARIABLES
    // -----------------------------------

    // screen size
    int screenHeight;
    int screenWidth;

    // game state
    boolean gameIsRunning;

    // threading
    Thread gameThread;


    // drawing variables
    SurfaceHolder holder;
    Canvas canvas;
    Paint paintbrush;


    // -----------------------------------
    // ## GAME SPECIFIC VARIABLES
    // -----------------------------------

    // ----------------------------
    // ## SPRITES
    // ----------------------------
    Point ballPosition; // point represents the (x,y) position of an item (ball)
    Point racketPosition; // racket (x,y) position
    final int BALL_WIDTH = 45;
    final int BALL_SPEED = 50;
    final int RACKET_WIDTH = 100;
    final int RACKET_HEIGHT = 25;
    final int DISTANCE_FROM_WALL = 300;
    final int RACKET_SPEED = 50;
    // ----------------------------
    // ## GAME STATS - number of lives, score, etc
    // ----------------------------
    int score = 0; //counting score

    public GameEngine(Context context, int w, int h) {
        super(context);


        this.holder = this.getHolder();
        this.paintbrush = new Paint();

        this.screenWidth = w;
        this.screenHeight = h;


        this.printScreenInfo();





        // @TODO: Add your sprites to this section
        // This is optional. Use it to:
        //  - setup or configure your sprites
        //  - set the initial position of your sprites

        //setting the initial position of ball to center of screen
        ballPosition = new Point();  //initializing the ballPosition point
        ballPosition.x = screenWidth/2;
        ballPosition.y = screenHeight/2;

        //setting the initial position of racket to bottom center
        racketPosition = new Point();
        racketPosition.x = (screenWidth/2 - RACKET_WIDTH);
        racketPosition.y = (screenHeight - (DISTANCE_FROM_WALL + RACKET_HEIGHT));


        // @TODO: Any other game setup stuff goes here


    }

    // ------------------------------
    // HELPER FUNCTIONS
    // ------------------------------

    // This funciton prints the screen height & width to the screen.
    private void printScreenInfo() {

        Log.d(TAG, "Screen (w, h) = " + this.screenWidth + "," + this.screenHeight);
    }


    // ------------------------------
    // GAME STATE FUNCTIONS (run, stop, start)
    // ------------------------------
    @Override
    public void run() {
        while (gameIsRunning == true) {
            this.updatePositions();
            this.redrawSprites();
            this.setFPS();
        }
    }


    public void pauseGame() {
        gameIsRunning = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            // Error
        }
    }

    public void startGame() {
        gameIsRunning = true;
        gameThread = new Thread(this);
        gameThread.start();
    }


    // ------------------------------
    // GAME ENGINE FUNCTIONS
    // - update, draw, setFPS
    // ------------------------------
        //setting a variable to keep track of ball movement
        boolean ballMovingDown = true;
    // 1. Tell Android the (x,y) positions of your sprites
    public void updatePositions() {
        // @TODO: Update the position of the sprites

            //code to make the ball move top top or bottom depending on boolean variable
            if(ballMovingDown == true)
            {
                ballPosition.y += BALL_SPEED; // moving ball 50px every frame
            }
            else {
                ballPosition.y -= BALL_SPEED;
            }


        // @TODO: Collision detection code

        //code to detect when ball reaches the screen bottom or top to stop ball from going down further
            if(ballPosition.y > screenHeight){ //ball reaches bottom of screen
                ballMovingDown = false;  //change the boolean
                score +=1; //increase score when ball touches bottom
            }
            else if(ballPosition.y < 0) //ball reaches top of screen
            {
                ballMovingDown = true; //change boolean
                score += 1; //increase score when ball touches top
            }

    }

    // 2. Tell Android to DRAW the sprites at their positions
    public void redrawSprites() {
        if (this.holder.getSurface().isValid()) {
            this.canvas = this.holder.lockCanvas();

            //----------------
            // Put all your drawing code in this section

            // configure the drawing tools
            this.canvas.drawColor(Color.argb(255,0,0,255));
            paintbrush.setColor(Color.WHITE);


            //@TODO: Draw the sprites (rectangle, circle, etc)

            //Drawing a ball as rectangle. We need 4 coordinates.
            int left = ballPosition.x;
            int top = ballPosition.y;
            int right = ballPosition.x + BALL_WIDTH;  // ball is 45 px in width
            int bottom = ballPosition.y + BALL_WIDTH; // ball is 45 px in height

            canvas.drawRect(left,top,right,bottom,paintbrush);

            //Drawing a racket as rectangle. We need 4 coordinates.
            int racleft = racketPosition.x;
            int ractop = racketPosition.y;
            int racright = racleft + RACKET_WIDTH*2;
            int racbottom = ractop + RACKET_HEIGHT*2;

            canvas.drawRect(racleft,ractop,racright,racbottom,paintbrush);

            //@TODO: Draw game statistics (lives, score, etc)
            paintbrush.setTextSize(60);
            canvas.drawText("Score: " + this.score,20,100,paintbrush);

            //----------------
            this.holder.unlockCanvasAndPost(canvas);
        }
    }

    // Sets the frame rate of the game
    public void setFPS() {
        try {
            gameThread.sleep(50);
        }
        catch (Exception e) {

        }
    }

    // ------------------------------
    // USER INPUT FUNCTIONS
    // ------------------------------

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int userAction = event.getActionMasked();
        //@TODO: What should happen when person touches the screen?
        if (userAction == MotionEvent.ACTION_DOWN) {
            // user pushed down on screen
            //code to to move racket left and right when user tap left or right of screen
            if(event.getX() < screenWidth/2 && (racketPosition.x  > 0)) //also checking if racket position reaches corner
            {
                racketPosition.x -= RACKET_SPEED;
            }else if (event.getX() > screenWidth/2 && (racketPosition.x+RACKET_WIDTH*2) < screenWidth)
            {
                racketPosition.x += RACKET_SPEED;
            }
        }
        else if (userAction == MotionEvent.ACTION_UP) {
            // user lifted their finger
        }
        return true;
    }
}