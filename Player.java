// Final Project
// CS201
// DiveKick Applet - Player class
// Neil Steiner
// Jana Parsons


import java.awt.*;
import java.awt.geom.AffineTransform;


public class Player {

    //instance variables
	// integer modifier for all speed related things
	int gameSpeed;

	int scoreCount;
	
	//player parts
	Rectangle head;
	Rectangle torso;
	Rectangle straightLeg;
	Rectangle straightFoot;
	Rectangle kickLeg;
	Rectangle kickFoot;
	
	//player part dimension constants
	int HEAD_HEIGHT = 20;
	int HEAD_WIDTH = 20;
	int TORSO_HEIGHT = 50;
	int TORSO_WIDTH = 40;	
	int LEG_HEIGHT = 50;
	int LEG_WIDTH = 10;
	int FOOT_HEIGHT = 10;
	int FOOT_WIDTH = 20;

	
	// set to -1 if facing left, 1 if facing right---------------------------------
	int left;

	// keep track to prevent repeated kick cheat
	boolean isKicking;

	
	int height;
	int width;

	int xPosition;
	int yPosition;

	int xVelocity;
	int yVelocity;

    int killStreak;
    int headStreak;

    
	FightCanvas parent;

    //constructor
	public Player(int x, int direction, FightCanvas fc) {
		
		parent = fc;
		
		int fcHeight = parent.getSize().height;
		int fcWidth = parent.getSize().width;
		int appWidth = parent.parent.getSize().width;
		
		xPosition = ((appWidth - width) * x)/5; 			
		yPosition = 300;
		
		width = TORSO_WIDTH - LEG_WIDTH + FOOT_WIDTH;
		height = FOOT_HEIGHT + LEG_HEIGHT + TORSO_HEIGHT + HEAD_HEIGHT;
		

				
		//BUILD CHARACTER
		head = new Rectangle(xPosition + (TORSO_WIDTH-HEAD_WIDTH)/2, 
			yPosition - FOOT_HEIGHT - LEG_HEIGHT - TORSO_HEIGHT - HEAD_HEIGHT, 
			HEAD_WIDTH, HEAD_HEIGHT);
		torso = new Rectangle(xPosition, 
			yPosition - FOOT_HEIGHT - LEG_HEIGHT - TORSO_HEIGHT, 
			TORSO_WIDTH, TORSO_HEIGHT);
		straightLeg = new Rectangle(xPosition, 
			yPosition - FOOT_HEIGHT - LEG_HEIGHT, 
			LEG_WIDTH , LEG_HEIGHT);
		straightFoot = new Rectangle(xPosition, 
			yPosition - FOOT_HEIGHT, 
			FOOT_WIDTH , FOOT_HEIGHT);
		kickLeg = new Rectangle(xPosition + TORSO_WIDTH - LEG_WIDTH, 
			yPosition - FOOT_HEIGHT - LEG_HEIGHT, 
			LEG_WIDTH, LEG_HEIGHT);
		kickFoot = new Rectangle(xPosition + TORSO_WIDTH - LEG_WIDTH, 
			yPosition - FOOT_HEIGHT, 
			FOOT_WIDTH, FOOT_HEIGHT);


		// player characteristics

		xVelocity = 0;
		yVelocity = 0;

		gameSpeed = 5;
		left = direction;
		isKicking = false;

        killStreak = 0;
        scoreCount = 0;
        headStreak = 0;
	}
	

    //resets player positions for a new round
    public void newRound(int x, int direction){
    	// place character at either 1/4 or 3/4 of canvas x dimension
		// and standing on ground
		xPosition = ((parent.parent.getSize().width-width) * x)/5; 			
		yPosition = 300;

		xVelocity = 0;
		yVelocity = 0;

		gameSpeed = 5;
		left = direction;
		isKicking = false;
    }
    
    //handles jumps
	public void jumpUp() {

		this.yVelocity -= gameSpeed * 7; //update velocity

    }


    //handles jump backs
    public void jumpBack() {

    	//need to be based on left variable
    	//because xVelocity will be different for each player. 
    	this.xVelocity -= left * gameSpeed * 1; 
    	this.yVelocity -= gameSpeed * 5;

    }


    // kick downward
    public void kick() {
    	this.xVelocity -= left * gameSpeed * -3;
    	this.yVelocity += gameSpeed * 5;
    	isKicking = true;

    	
    }


    // make sure characters are facing the right way
    public void rotate() {

    	left = -left;
    	kickFoot.translate(left*FOOT_WIDTH/2, 0);
    	straightFoot.translate(left*FOOT_WIDTH/2, 0);

    }


    // apply acceleration to velocity to player position
    public void movePlayer() {
    	// apply gravity (positive is down)
        yVelocity += 1;
        

        // apply velocity to position
        xPosition += xVelocity;
        yPosition += yVelocity;

        head.setLocation(xPosition + (TORSO_WIDTH-HEAD_WIDTH)/2, 
        	yPosition - FOOT_HEIGHT - LEG_HEIGHT - TORSO_HEIGHT - HEAD_HEIGHT);
        torso.setLocation(xPosition, 
        	yPosition - FOOT_HEIGHT - LEG_HEIGHT - TORSO_HEIGHT);
        straightLeg.setLocation(xPosition, 
        	yPosition - FOOT_HEIGHT - LEG_HEIGHT);
		kickLeg.setLocation(xPosition + TORSO_WIDTH - LEG_WIDTH, 
			yPosition - FOOT_HEIGHT - LEG_HEIGHT);
		if (left == 1) {
			kickFoot.setLocation(xPosition + TORSO_WIDTH - LEG_WIDTH, 
				yPosition - FOOT_HEIGHT);
        	straightFoot.setLocation(xPosition, 
        		yPosition - FOOT_HEIGHT);
		}
		else {
			kickFoot.setLocation(xPosition + TORSO_WIDTH - FOOT_WIDTH, 
				yPosition - FOOT_HEIGHT);
        	straightFoot.setLocation(xPosition + LEG_WIDTH - FOOT_WIDTH, 
        		yPosition - FOOT_HEIGHT);
		}
    }

    public void draw() {

		//BUILD CHARACTER
    	//draw head
		parent.g2.fillRect(xPosition + (TORSO_WIDTH-HEAD_WIDTH)/2, 
			yPosition - FOOT_HEIGHT - LEG_HEIGHT - TORSO_HEIGHT - HEAD_HEIGHT, 
			HEAD_WIDTH, HEAD_HEIGHT);

		//draw torso
		parent.g2.fillRect(xPosition, 
			yPosition - FOOT_HEIGHT - LEG_HEIGHT - TORSO_HEIGHT, 
			TORSO_WIDTH, TORSO_HEIGHT);

		//draw sraightleg
		parent.g2.fillRect(xPosition, 
			yPosition - FOOT_HEIGHT - LEG_HEIGHT, 
			LEG_WIDTH , LEG_HEIGHT);
		//draw kickleg
		parent.g2.fillRect(xPosition + TORSO_WIDTH - LEG_WIDTH, 
			yPosition - FOOT_HEIGHT - LEG_HEIGHT, 
			LEG_WIDTH, LEG_HEIGHT);

		if (left == 1) {
			//draw kickfoot
			parent.g2.fillRect(xPosition + TORSO_WIDTH - LEG_WIDTH, 
				yPosition - FOOT_HEIGHT, 
				FOOT_WIDTH, FOOT_HEIGHT);

			//draw straightfoot
			parent.g2.fillRect(xPosition, 
				yPosition - FOOT_HEIGHT, 
				FOOT_WIDTH , FOOT_HEIGHT);
		}
		else {
			//draw kickfoot
			parent.g2.fillRect(xPosition + TORSO_WIDTH - FOOT_WIDTH, 
				yPosition - FOOT_HEIGHT, 
				FOOT_WIDTH, FOOT_HEIGHT);
			
			//draw straightfoot
			parent.g2.fillRect(xPosition + LEG_WIDTH - FOOT_WIDTH, 
				yPosition - FOOT_HEIGHT, 
				FOOT_WIDTH , FOOT_HEIGHT);
		}
		
    }

    //make floor
    public void floor() {
    	isKicking = false;
		yVelocity=0;
        yPosition = parent.getSize().height;
        xVelocity = 0;
        

    }

    //make left wall
    public void leftWall() {
    	xPosition = 0;
		xVelocity = 0;
    }

    //make right wall
    public void rightWall() {
    	xPosition = parent.getSize().width - width;
		xVelocity = 0;

    }
    


        //right collision detection
    public boolean collision(Player opponent){
    	//if bottom corner of player 1 is greater than player 2 left x position
    	//less than player 2 right x position
    	//greater than player 2 y position
    	//less than player 2 bottom y position

		//if player kickfoot or straight foot intersects wqith any part of opponents body, its a collision
		if (isKicking && (kickFoot.intersects(opponent.torso) || 
			kickFoot.intersects(opponent.straightLeg) ||
			kickFoot.intersects(opponent.straightFoot) ||//unless its the opponents foot and the opponent is already kicking. 
			kickFoot.intersects(opponent.kickLeg) ||
			kickFoot.intersects(opponent.kickFoot) ||

			straightFoot.intersects(opponent.torso) || 
			straightFoot.intersects(opponent.straightLeg) ||
			straightFoot.intersects(opponent.straightFoot)||
			straightFoot.intersects(opponent.kickLeg) ||
			straightFoot.intersects(opponent.kickFoot)))  {

			killStreak += 1;
			headStreak = 0;
            scoreCount += 1;
			return true;
		
	
		} else if (kickFoot.intersects(opponent.head) | straightFoot.intersects(opponent.head)){ //if player hits the opponents head also increase headstreak
			killStreak += 1;
			headStreak += 1;
            scoreCount += 1;
			return true;
		} else {
			return false;
		}
	}
	
}
