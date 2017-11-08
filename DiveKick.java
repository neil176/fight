
// Final Project
// CS201
// DiveKick Applet
// Neil Steiner
// Jana Parsons

import java.util.*;
import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

public class DiveKick extends Applet implements KeyListener {

	// instance variables
	FightCanvas fc;

	//audio files
	AudioClip headshot;
	AudioClip ultrakill;
	AudioClip doublekill;
	AudioClip monsterkill;
	AudioClip godlike;
	AudioClip humiliation;
	AudioClip unstoppable;
	AudioClip rampage;
	AudioClip firstblood;

	//labels
	Label player1ControlsDive;
	Label player2ControlsDive;
	Label player1ControlsKick;
	Label player2ControlsKick;

	Label clock;
	Label player1Score;
	Label player2Score;
	Label status;

	// local color constants
	static final Color black = Color.black;
	static final Color white = Color.white;
	static final Color red = Color.red;
	static final Color green = Color.green;
	static final Color blue = Color.blue;
	static final Color yellow = Color.yellow;
	static final Color dred = new Color(160, 0, 100);
	static final Color dgreen = new Color(0, 120, 90);
	static final Color dblue = Color.blue.darker();

	// initialize the applet------------------------------------------------
	public void init() {

		fc = new FightCanvas(this);
		fc.addKeyListener(this);
		setFont(new Font("Calibri", Font.BOLD, 24));
		setLayout(new BorderLayout());
		add("North", topPanel()); // add at top
		add("South", bottomPanel()); // add at bottom
		add("Center", fc); // add in remaining area (fills space)


		// load sounds
		headshot = getAudioClip(getCodeBase(), "headshot.au");
		ultrakill = getAudioClip(getCodeBase(), "ultrakill.au");
		doublekill = getAudioClip(getCodeBase(), "doublekill.au");
		monsterkill = getAudioClip(getCodeBase(), "monsterkill.au");
		godlike = getAudioClip(getCodeBase(), "godlike.au");
		humiliation = getAudioClip(getCodeBase(), "humiliation.au");
		unstoppable = getAudioClip(getCodeBase(), "unstoppable.au");
		rampage = getAudioClip(getCodeBase(), "rampage.au");
		firstblood = getAudioClip(getCodeBase(), "firstblood.au");

	}

	// helper method to cvreate the top panel
	public Panel topPanel() {
		Panel topPanel = new Panel();
		topPanel.setLayout(new GridLayout(2, 3));

		// title
		Label titleLabel = new Label("DIVEKICK!!!!!!", Label.CENTER);
		titleLabel.setBackground(dblue);
		titleLabel.setForeground(white);

		// filler
		Label blank = new Label("");
		blank.setBackground(dblue);
		blank.setForeground(dred);

		Label blank2 = new Label("");
		blank2.setBackground(dblue);
		blank2.setForeground(dred);

		// player scores
		player1Score = new Label("0", Label.CENTER);
		player1Score.setBackground(dblue);
		player1Score.setForeground(white);

		player2Score = new Label("0", Label.CENTER);
		player2Score.setBackground(dblue);
		player2Score.setForeground(white);

		// game status
		status = new Label(" ", Label.CENTER);
		status.setForeground(red);
		status.setBackground(dblue);

		// add labels to panel
		topPanel.add(player1Score);
		topPanel.add(titleLabel); // will eventually become clock
		topPanel.add(player2Score);

		topPanel.add(blank);
		topPanel.add(status);
		topPanel.add(blank2);

		return topPanel;

	}

	// helper function to create bottom panel with directions
	public Panel bottomPanel() {
		Panel bottomPanel = new Panel();
		bottomPanel.setLayout(new GridLayout(2, 2));

		// create labels
		player1ControlsDive = new Label("Dive: Z");
		player2ControlsDive = new Label("Dive: Left Arrow", Label.RIGHT);
		player1ControlsKick = new Label("Kick: C");
		player2ControlsKick = new Label("Kick: Right Arrow", Label.RIGHT);

		// colors
		player1ControlsDive.setBackground(dblue);
		player1ControlsDive.setForeground(white);
		player2ControlsDive.setBackground(dblue);
		player2ControlsDive.setForeground(white);
		player1ControlsKick.setBackground(dblue);
		player1ControlsKick.setForeground(white);
		player2ControlsKick.setBackground(dblue);
		player2ControlsKick.setForeground(white);

		// add labels to panel in grid order
		bottomPanel.add(player1ControlsDive);
		bottomPanel.add(player2ControlsDive);
		bottomPanel.add(player1ControlsKick);
		bottomPanel.add(player2ControlsKick);

		return bottomPanel;

	}

	public void start() {
		fc.start();
	}

	public void stop() {
		fc.stop();
	}

	// handle key presses---------------------------------------------------
	public void keyPressed(KeyEvent e) {

		int keyCode = e.getKeyCode();

		int canvasHeight = fc.getSize().height;

		// player 1
		// movements-------------------------------------------------------

		// jump up
		// PRE: player must be on the ground
		if (keyCode == KeyEvent.VK_Z && 
			fc.player1.yPosition == canvasHeight) {
			fc.player1.jumpUp();
		}

		// jump back
		// PRE: player must be on the ground
		else if (keyCode == KeyEvent.VK_C && 
			fc.player1.yPosition == canvasHeight) {
			fc.player1.jumpBack();
		}

		// kick
		// PRE: player must be in air
		else if (keyCode == KeyEvent.VK_C && 
			fc.player1.yPosition != canvasHeight) {
			fc.player1.kick();
		}

		// player 2
		// movements----------------------------------------------------------

		// jump up
		// PRE: player must be on the ground
		if (keyCode == KeyEvent.VK_LEFT && 
			fc.player2.yPosition == canvasHeight) {
			fc.player2.jumpUp();
		}

		// jump back
		// PRE: player must be on the ground
		else if (keyCode == KeyEvent.VK_RIGHT && 
			fc.player2.yPosition == canvasHeight) {
			fc.player2.jumpBack();
		}

		// kick
		// PRE: player must be in air
		else if (keyCode == KeyEvent.VK_RIGHT &&
			fc.player2.yPosition != canvasHeight) {
			fc.player2.kick();
		}
	}

	//keylistener required methods
	public void keyTyped(KeyEvent e) {
	}

	public void keyReleased(KeyEvent e) {
	}

}

// canvas where all the action takes place
class FightCanvas extends Canvas implements Runnable {

	// instance variables
	Image offscreen;
	Dimension offscreensize;
	Graphics g2;

	DiveKick parent;

	Thread t;

	Player player1;
	Player player2;

	// constructor
	public FightCanvas(DiveKick dk) {
		parent = dk;

		//please note that on the browser the players do not initialize to the right positions
		//and it is not an issue in any subsequent rounds after the first
		//running the game in the applet corrects for this.

		player1 = new Player(1, 1, this);
		player2 = new Player(4, -1, this);

	}

	// handles all movement and gameplay
	public void run() {

		Thread currentThread = Thread.currentThread();
		while (currentThread == t) {
			Dimension d = getSize();

			// handle player movement

			if (player1.yPosition <= d.height) {
				player1.movePlayer();
			}

			if (player2.yPosition <= d.height) {
				player2.movePlayer();
			}

			// create floor
			if (player1.yPosition > d.height) {
				player1.floor();
			}

			if (player2.yPosition > d.height) {
				player2.floor();
			}

			// create left wall
			if (player1.xPosition < 0) {
				player1.leftWall();
			}

			if (player2.xPosition < 0) {
				player2.leftWall();
			}

			// create right wall
			if (player1.xPosition + player1.width > d.width) {
				player1.rightWall();
			}

			if (player2.xPosition + player2.width > d.width) {
				player2.rightWall();
			}

			// check for position
			// swap/rotate---------------------------------------------------

			// player 1 needs to be facing left
			if (player1.yPosition == d.height && 
				player1.xPosition > player2.xPosition + (player2.width / 2)) {
				if (player1.left != -1) {
					player1.rotate();
				}
			}
			// player 1 needs to be facing right
			if (player1.yPosition == d.height && 
				player1.xPosition < player2.xPosition + (player2.width / 2)) {
				if (player1.left != 1) {
					player1.rotate();
				}
			}

			// player 2 needs to be facing left
			if (player2.yPosition == d.height && 
				player2.xPosition > player1.xPosition + (player1.width / 2)) {
				if (player2.left != -1) {
					player2.rotate();
				}
			}

			// player 2 needs to be facing right
			if (player2.yPosition == d.height && 
				player2.xPosition < player1.xPosition + (player1.width / 2)) {
				if (player2.left != 1) {
					player2.rotate();
				}
			}

			// check for
			// collision-------------------------------------------------------

			// COLLISION FOR PLAYER 1
			if (player1.isKicking) {
				// player 1 must have made a collision AND player 2 must have
				// not made a collision
				if (player1.collision(player2) && !player2.collision(player1)) {

					// player 1 wins round, update all variables
					player2.killStreak = 0; // reset player2 kill streak back to 0
					// change player 1 score label
					parent.player1Score.setText(Integer.toString(player1.scoreCount)); 
					// update status
					parent.status.setText("Player 1 wins round " + (player1.scoreCount + player2.scoreCount)); 

					// sounds
					if (player1.headStreak >= 1) {
						parent.headshot.play();
						if (player1.killStreak > 1) {
							try {
								Thread.sleep(1500);
							} catch (InterruptedException e) {};
						}	
					}

					if (player2.scoreCount + player1.scoreCount == 1) {
						parent.firstblood.play();
					}
					if (player1.killStreak == 2) {
						parent.doublekill.play();
					}
					if (player1.killStreak == 3) {
						parent.ultrakill.play();
					}
					if (player1.killStreak == 4) {
						parent.rampage.play();
					}
					if (player1.killStreak == 5) {
						parent.unstoppable.play();
					}
					if (player1.killStreak == 6) {
						parent.godlike.play();
					}
					if (player1.killStreak == 7) {
						parent.humiliation.play();
					}

					try {
						Thread.sleep(2500);
					} catch (InterruptedException e) {};

					parent.status.setText(" "); // update status

					player1.newRound(1, 1);// reset both player positions
					player2.newRound(4, -1);

					// both players make a collision
					//going to need to update this becayuse I check for that in the player class collision check. 
				} else if (player1.collision(player2) && player2.collision(player1)) {
					// if both players kick and collide at the same time its a
					// tie, no score update

					parent.status.setText("It's a draw"); // update status

					try {
						Thread.sleep(2500);
					} catch (InterruptedException e) {};

					parent.status.setText(" "); // update status

					player1.newRound(1, 1);// reset both player positions
					player2.newRound(4, -1);

				}
			}

			// player2 collision detection
			if (player2.isKicking) {
				// player 2 must make a collision and player 1 must NOT make a
				// collision
				if (player2.collision(player1) && !player1.collision(player2) ) {

					player1.killStreak = 0; // set player1 kill streak back to 0
					parent.player2Score.setText(Integer.toString(player2.scoreCount)); // change player 2 score label

					parent.status.setText("Player 2 wins round " + (player1.scoreCount + player2.scoreCount)); // update status



					// sounds
					if (player2.headStreak >= 1) {
						parent.headshot.play();
						if (player2.killStreak > 1) {
							try {
								Thread.sleep(1500);
							} catch (InterruptedException e) {};
						}
					}

					if (player2.scoreCount + player1.scoreCount == 1) {
						parent.firstblood.play();
					}

					if (player2.killStreak == 2) {
						parent.doublekill.play();
					}
					if (player2.killStreak == 3) {
						parent.ultrakill.play();
					}
					if (player2.killStreak == 4) {
						parent.rampage.play();
					}
					if (player2.killStreak == 5) {
						parent.unstoppable.play();
					}
					if (player2.killStreak == 6) {
						parent.godlike.play();
					}
					if (player2.killStreak == 7) {
						parent.humiliation.play();
					}

					try {
						Thread.sleep(2500);
					} catch (InterruptedException e) {};

					parent.status.setText(" "); // update status

					// reset both player positions and clock
					player1.newRound(1, 1);// reset both player positions
					player2.newRound(4, -1);

				}
			}

			


			// player 1 wins, reset game
			if (player1.scoreCount == 7) {
				parent.status.setText("PLAYER 1 WINS!");

				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {};

				// reset all variables and create new round
				player1.newRound(1, 1); // reset both player positions
				player2.newRound(4, -1);
				player1.scoreCount = 0;
				player2.scoreCount = 0;
				player1.killStreak = 0;
				player2.killStreak = 0;
				parent.player1Score.setText("0");
				parent.player2Score.setText("0");
				parent.status.setText(" ");
			}

			// player 2 wins, reset game
			if (player2.scoreCount == 7) {
				parent.status.setText("PLAYER 2 WINS!");

				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {};

				// reset all variables and create new round
				player1.newRound(1, 1); // reset both player positions
				player2.newRound(4, -1);
				player1.scoreCount = 0;
				player2.scoreCount = 0;
				player1.killStreak = 0;
				player2.killStreak = 0;
				parent.player1Score.setText("0");
				parent.player2Score.setText("0");
				parent.status.setText(" ");
			}

			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {};

			repaint();
		}
	}

	public void start() {
		t = new Thread(this);
		t.start();
	}

	public void stop() {
		t = null;
	}

	

	public synchronized void update(Graphics g) {
		Dimension d = getSize();

		// initially (or when size changes) create new image
		if ((offscreen == null) || (d.width != offscreensize.width) || (d.height != offscreensize.height)) {
			offscreen = createImage(d.width, d.height);
			offscreensize = d;
			g2 = offscreen.getGraphics();
		}

		// erase old contents:
		g2.setColor(getBackground());
		g2.fillRect(0, 0, d.width, d.height);

		// now, draw as usual, but use g2 instead of g
		g2.setColor(Color.RED);
		player1.draw();
		g2.setColor(Color.BLUE);
		player2.draw();
		
		// finally, draw the image on top of the old one
		g.drawImage(offscreen, 0, 0, null);

	}

	// this is called when window is uncovered or resized
	public synchronized void paint(Graphics g) {
		update(g);
	}

	

}
