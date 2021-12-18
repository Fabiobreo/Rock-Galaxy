package rockGalaxy;
/**
 * This class implements the gameplay
 * @author Ing. Fabio Brea
 * @version 1.1
 */
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.Random;

public class RockGalaxy extends Game
{
	private static final long serialVersionUID = -6490726179412583356L;

	static int FRAMERATE = 60;
	static int SCREENWIDTH = 800;
	static int SCREENHEIGHT = 600;

	final int ASTEROIDS = 8;
	final int BULLET_SPEED = 4;
	final double ACCELERATION = 0.05;
	final double SHIPROTATION = 5.0;

	/* Sprite status */
	final int STATE_NORMAL = 0;
	final int STATE_COLLIDED = 1;
	final int STATE_EXPLODING = 2;

	/* Sprite types */
	final int SPRITE_SHIP = 1;
	final int SPRITE_ASTEROID_BIG = 10;
	final int SPRITE_ASTEROID_MEDIUM = 11;
	final int SPRITE_ASTEROID_SMALL = 12;
	final int SPRITE_ASTEROID_TINY = 13;
	final int SPRITE_BULLET = 100;
	final int SPRITE_EXPLOSION = 200;
	final int SPRITE_POWERUP_SHIELD = 300;
	final int SPRITE_POWERUP_HEALTH = 301;
	final int SPRITE_POWERUP_250 = 302;
	final int SPRITE_POWERUP_500 = 303;
	final int SPRITE_POWERUP_1000 = 304;
	final int SPRITE_POWERUP_GUN = 305;

	/* Game states */
	final int GAME_MENU = 0;
	final int GAME_RUNNING = 1;
	final int GAME_OVER = 2;
	final int GAME_WON = 3;

	/* Visible collision detection variables (Also known as cheats) */
	private boolean showBounds = false;
	private boolean collisionTesting = true;

	/* Image in the game */
	private ImageEntity background;
	private ImageEntity bulletImage;
	private ImageEntity[] bigAsteroids = new ImageEntity[5];
	private ImageEntity[] medAsteroids = new ImageEntity[2];
	private ImageEntity[] smlAsteroids = new ImageEntity[3];
	private ImageEntity[] tnyAsteroids = new ImageEntity[4];
	private ImageEntity[] explosions = new ImageEntity[2];
	private ImageEntity[] shipImage = new ImageEntity[3];
	private ImageEntity[] barImage = new ImageEntity[2];
	private ImageEntity barFrame;
	private ImageEntity powerupShield;
	private ImageEntity powerupHealth;
	private ImageEntity powerup250;
	private ImageEntity powerup500;
	private ImageEntity powerup1000;
	private ImageEntity powerupGun;

	/* Game statistics */
	private final int MAX_HEALTH = 20;
	private final int MAX_SHIELD = 20;
	
	private int health = MAX_HEALTH;
	private int shield = MAX_SHIELD;
	private int score = 0;
	private int highscore = 0;
	private int firepower = 1;
	private int gameState = GAME_MENU;

	/* Handled buttons */
	protected boolean keyLeft;
	protected boolean keyRight;
	protected boolean keyUp;
	protected boolean keyFire;
	protected boolean keyB;
	protected boolean keyC;
	protected boolean keyShield;

	/* Sound effects */
	private MidiSequence music = new MidiSequence();
	private SoundClip shoot = new SoundClip();
	private SoundClip explosion = new SoundClip();

	/* Resources folders */
	private String sounds_folder = "/sounds/";
	private String images_folder = "/images/";

	private Random rand = new Random();
	private long collisionTimer = 0;

	/**
	 * Constructor
	 */
	public RockGalaxy()
	{
		super(FRAMERATE, SCREENWIDTH, SCREENHEIGHT);
	}

	/**
	 * Loads game resources at startup
	 */
	public void gameStartup()
	{
		/* Load musics */
		music.load(sounds_folder + "music.mid");
		shoot.load(sounds_folder + "shoot.au");
		explosion.load(sounds_folder + "explode.au");

		/* Load ui */
		barFrame = new ImageEntity(this);
		barFrame.load(images_folder + "barframe.png");
		barImage[0] = new ImageEntity(this);
		barImage[0].load(images_folder + "bar_health.png");
		barImage[1] = new ImageEntity(this);
		barImage[1].load(images_folder + "bar_shield.png");

		/* Load powerups */
		powerupShield = new ImageEntity(this);
		powerupShield.load(images_folder + "powerup_shield2.png");
		powerupHealth = new ImageEntity(this);
		powerupHealth.load(images_folder + "powerup_cola.png");
		powerup250 = new ImageEntity(this);
		powerup250.load(images_folder + "powerup_250.png");
		powerup500 = new ImageEntity(this);
		powerup500.load(images_folder + "powerup_500.png");
		powerup1000 = new ImageEntity(this);
		powerup1000.load(images_folder + "powerup_1000.png");
		powerupGun = new ImageEntity(this);
		powerupGun.load(images_folder + "powerup_gun.png");

		/* Load background */
		background = new ImageEntity(this);
		background.load(images_folder + "space.png");

		/* Load ship */
		shipImage[0] = new ImageEntity(this);
		shipImage[0].load(images_folder + "spaceship.png");
		shipImage[1] = new ImageEntity(this);
		shipImage[1].load(images_folder + "ship_thrust.png");
		shipImage[2] = new ImageEntity(this);
		shipImage[2].load(images_folder + "ship_shield.png");

		AnimatedSprite ship = new AnimatedSprite(this, graphics());
		ship.setSpriteType(SPRITE_SHIP);
		ship.setImage(shipImage[0].getImage());
		ship.setFrameWidth(ship.getImageWidth());
		ship.setFrameHeight(ship.getImageHeight());
		ship.setPosition(new Point2D(SCREENWIDTH/2, SCREENHEIGHT/2));
		ship.setAlive(true);
		ship.setState(STATE_EXPLODING); //Invulnerable at start
		collisionTimer = System.currentTimeMillis();
		getSprites().add(ship);

		/* Load bullets */
		bulletImage = new ImageEntity(this);
		bulletImage.load(images_folder + "plasmashot.png");

		/* Load explosions */
		explosions[0] = new ImageEntity(this);
		explosions[0].load(images_folder + "explosion.png");
		explosions[1] = new ImageEntity(this);
		explosions[1].load(images_folder + "explosion2.png");

		/* Load asteroids */
		for (int n = 0; n < 5; n++)
		{
			bigAsteroids[n] = new ImageEntity(this);
			String fn = images_folder + "asteroid" + (n + 1) + ".png";
			bigAsteroids[n].load(fn);
		}

		for (int n = 0; n < 2; n++)
		{
			medAsteroids[n] = new ImageEntity(this);
			String fn = images_folder + "medium" + (n + 1) + ".png";
			medAsteroids[n].load(fn);
		}

		for (int n = 0; n < 3; n++)
		{
			smlAsteroids[n] = new ImageEntity(this);
			String fn = images_folder + "small" + (n + 1) + ".png";
			smlAsteroids[n].load(fn);
		}

		for (int n = 0; n < 4; n++)
		{
			tnyAsteroids[n] = new ImageEntity(this);
			String fn = images_folder + "tiny" + (n + 1) + ".png";
			tnyAsteroids[n].load(fn);
		}

		pauseGame();
	}

	/**
	 * Put the game into the initial state
	 */
	private void resetGame()
	{
		music.setLooping(true);
		music.play();
		
		resetShip();
		collisionTimer = System.currentTimeMillis();
		
		for (int n = 0; n < ASTEROIDS; n++)
		{
			createAsteroid();
		}

		health = MAX_HEALTH;
		shield = MAX_SHIELD;
		score = 0;
		firepower = 2;
	}
	
	/**
	 * Restore ship initial position, orientation and velocity
	 */
	private void resetShip()
	{
		AnimatedSprite ship = (AnimatedSprite) getSprites().get(0);
		getSprites().clear();

		ship.setPosition(new Point2D(SCREENWIDTH / 2, SCREENHEIGHT / 2));
		ship.setFaceAngle(0);
		ship.setAlive(true);
		ship.setState(STATE_EXPLODING);
		ship.setVelocity(new Point2D(0, 0));
		getSprites().add(ship);
	}

	/**
	 * Update every tick of time
	 */
	public void gameTimedUpdate()
	{
		checkInput();
		
		if (!isGamePaused() && getSprites().size() == 1)
		{
			resetShip();
			gameState = GAME_WON;
		}
	}
	
	/**
	 * Draw a string centered in the middle of a rectangle.
	 * @param graphic The Graphics instance.
	 * @param text The String to draw.
	 * @param rect The Rectangle to center the text in.
	 */
	private void drawCenteredString(Graphics2D graphic, String text, Rectangle rect) {
	    FontMetrics metrics = graphic.getFontMetrics();
	    int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
	    int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
	    graphic.drawString(text, x, y);
	}

	/**
	 * Draw on screen
	 */
	public void gameRefreshScreen() {
		Graphics2D g2d = graphics();
		g2d.drawImage(background.getImage(), 0, 0, SCREENWIDTH - 1, SCREENHEIGHT - 1, this);

		if (gameState == GAME_MENU) {
			/* TITLE */
			g2d.setFont(new Font("Verdana", Font.BOLD, 96));
			g2d.setColor(Color.WHITE);
			drawCenteredString(g2d, "ROCK GALAXY", new Rectangle(2, 0, SCREENWIDTH, SCREENHEIGHT / 2));
			g2d.setColor(new Color(200,30,30));
			drawCenteredString(g2d, "ROCK GALAXY", new Rectangle(0, 0, SCREENWIDTH, SCREENHEIGHT / 2));
			
			/* COMMANDS */
			g2d.setFont(new Font("Times New Roman", Font.ITALIC | Font.BOLD, 20));
			int x = 0;
			int x_right = SCREENWIDTH / 2 + 20;
			int x_left = 0;
			int y = SCREENHEIGHT * 6 / 11 - g2d.getFontMetrics().getAscent();
			g2d.setColor(Color.GREEN);
			drawCenteredString(g2d, "KEYS:", new Rectangle(x, y, SCREENWIDTH, 0));
			
			g2d.setColor(Color.YELLOW);
			y += g2d.getFontMetrics().getAscent();
			x_left = SCREENWIDTH / 2 - 20 - g2d.getFontMetrics().stringWidth("ROTATION");
			g2d.drawString("ROTATION", x_left, y + g2d.getFontMetrics().getHeight() / 2);
			g2d.drawString("RIGHT/LEFT ARROWS",  x_right, y + g2d.getFontMetrics().getHeight() / 2);
			drawCenteredString(g2d, "-", new Rectangle(x, y, SCREENWIDTH, 0));
			
			y += g2d.getFontMetrics().getAscent();
			x_left = SCREENWIDTH / 2 - 20 - g2d.getFontMetrics().stringWidth("ACCELERATION");
			g2d.drawString("ACCELERATION", x_left, y + g2d.getFontMetrics().getHeight() / 2);
			g2d.drawString("UP ARROW",  x_right, y + g2d.getFontMetrics().getHeight() / 2);
			drawCenteredString(g2d, "-", new Rectangle(x, y, SCREENWIDTH, 0));
			
			y += g2d.getFontMetrics().getAscent();
			x_left = SCREENWIDTH / 2 - 20 - g2d.getFontMetrics().stringWidth("SHIELD");
			g2d.drawString("SHIELD", x_left, y + g2d.getFontMetrics().getHeight() / 2);
			g2d.drawString("SHIFT (NO POINTS)",  x_right, y + g2d.getFontMetrics().getHeight() / 2);
			drawCenteredString(g2d, "-", new Rectangle(x, y, SCREENWIDTH, 0));
			
			y += g2d.getFontMetrics().getAscent();
			x_left = SCREENWIDTH / 2 - 20 - g2d.getFontMetrics().stringWidth("FIRE");
			g2d.drawString("FIRE", x_left, y + g2d.getFontMetrics().getHeight() / 2);
			g2d.drawString("CTRL",  x_right, y + g2d.getFontMetrics().getHeight() / 2);
			drawCenteredString(g2d, "-", new Rectangle(x, y, SCREENWIDTH, 0));
			
			y = SCREENHEIGHT * 4 / 5;
			g2d.setColor(Color.WHITE);
			drawCenteredString(g2d, "PICK UP POWERUPS TO IMPROVE YOUR FIRE RATE!", new Rectangle(0, y, SCREENWIDTH, 0));

			/* PRESS ENTER */
			y = SCREENHEIGHT * 9 / 10;
			g2d.setFont(new Font("Ariel", Font.BOLD, 36));
			g2d.setColor(Color.WHITE);
			drawCenteredString(g2d, "PRESS ENTER TO START", new Rectangle(2, y, SCREENWIDTH, 0));
			g2d.setColor(Color.GREEN);
			drawCenteredString(g2d, "PRESS ENTER TO START", new Rectangle(0, y, SCREENWIDTH, 0));
			
		}
		else if (gameState == GAME_RUNNING)
		{
			int border_distance = 30;
			
			/* Draw Health Bar */
			g2d.drawImage(barFrame.getImage(), SCREENWIDTH - barFrame.getWidth() - border_distance - 2, 18, this);
			for (int n = 0; n < health; n++) {
				int dx = SCREENWIDTH - barFrame.getWidth() - border_distance + n * barImage[0].getWidth();
				g2d.drawImage(barImage[0].getImage(), dx, 20, this);
			}
			
			/* Draw Shield Bar */
			g2d.drawImage(barFrame.getImage(), SCREENWIDTH - barFrame.getWidth() - border_distance - 2, 33, this);
			for (int n = 0; n < shield; n++) {
				int dx = SCREENWIDTH - barFrame.getWidth() - border_distance + n * barImage[1].getWidth();
				g2d.drawImage(barImage[1].getImage(), dx, 35, this);
			}

			for (int n = 0; n < firepower; n++) {
				int dx = SCREENWIDTH - 190 - border_distance + n * 13;
				g2d.drawImage(powerupGun.getImage(), dx, 17, this);
			}

			/* Draw score */
			g2d.setFont(new Font("Verdana", Font.BOLD, 24));
			g2d.setColor(Color.WHITE);
			g2d.drawString("" + score, 20, 40);
			g2d.setColor(Color.RED);
			drawCenteredString(g2d, "" + highscore, new Rectangle(0, 40, SCREENWIDTH, 0));
		}
		else if (gameState == GAME_OVER)
		{
			g2d.setFont(new Font("Verdana", Font.BOLD, 36));
			g2d.setColor(new Color(200, 30, 30));
			g2d.drawString("GAME OVER", 270, 200);

			g2d.setFont(new Font("Arial", Font.CENTER_BASELINE, 24));
			g2d.setColor(Color.ORANGE);
			g2d.drawString("Premi INVIO per ricominciare", 240, 500);
		}
		else if (gameState == GAME_WON)
		{
			g2d.setFont(new Font("Verdana", Font.BOLD, 36));
			g2d.setColor(new Color(200, 30, 30));
			g2d.drawString("GAME WON", 270, 200);

			g2d.setFont(new Font("Arial", Font.CENTER_BASELINE, 24));
			g2d.setColor(Color.ORANGE);
			g2d.drawString("Premi INVIO per ricominciare", 260, 500);
		}
	}

	void gameShutdown() {
		music.stop();
		shoot.stop();
		explosion.stop();
	}

	public void spriteUpdate(AnimatedSprite sprite) {
		switch(sprite.getSpriteType()) {
		case SPRITE_SHIP:
			warp(sprite);
			break;

		case SPRITE_BULLET:
			warp(sprite);
			break;

		case SPRITE_EXPLOSION:
			if (sprite.getCurrentFrame() == sprite.totalFrames()-1) {
				sprite.setAlive(false);
			}
			break;

		case SPRITE_ASTEROID_BIG:
		case SPRITE_ASTEROID_MEDIUM:
		case SPRITE_ASTEROID_SMALL:
		case SPRITE_ASTEROID_TINY:
			warp(sprite);
			break;

		case SPRITE_POWERUP_SHIELD:
		case SPRITE_POWERUP_HEALTH:
		case SPRITE_POWERUP_250:
		case SPRITE_POWERUP_500:
		case SPRITE_POWERUP_1000:
		case SPRITE_POWERUP_GUN:
			warp(sprite);
			double rot = sprite.getRotationRate();
			if (sprite.getFaceAngle() > 350) {
				sprite.setRotationRate(rot * -1);
				sprite.setFaceAngle(350);
			}
			else if (sprite.getFaceAngle() < 10) {
				sprite.setRotationRate(rot * -1);
				sprite.setFaceAngle(10);
			}
			break;
		}
	}

	public void spriteDraw(AnimatedSprite sprite) {
		if (showBounds) {
			if (sprite.hasCollided())
				sprite.drawBounds(Color.RED);
			else
				sprite.drawBounds(Color.BLUE);
		}
	}

	public void spriteDying(AnimatedSprite sprite)
	{
	}

	public void spriteCollision(AnimatedSprite spr1, AnimatedSprite spr2)
	{
		if (!collisionTesting)
		{
			return;
		}

		switch(spr1.getSpriteType()) {
		case SPRITE_BULLET:
			//il proiettile ha colpito l'asteoride?
			if (isAsteroid(spr2.getSpriteType())) {
				bumpScore(5);
				spr1.setAlive(false);
				spr2.setAlive(false);
				breakAsteroid(spr2);
			}
			break;
		case SPRITE_SHIP:
			//L'asteroide è crashato sull'astronave?
			if (isAsteroid(spr2.getSpriteType())) {
				if (spr1.getState() == STATE_NORMAL) {
					if (keyShield)
					{
						shield -= 1;
					}
					else
					{ 
						collisionTimer = System.currentTimeMillis();
						spr1.setVelocity(new Point2D(0, 0));
						double x = spr1.getPosition().X() - 10;
						double y = spr1.getPosition().Y() - 10;
						startBigExplosion(new Point2D(x, y));
						spr1.setState(STATE_EXPLODING);
						//reduci salute della navicella
						health -= 2;
						if (health < 0) {
							gameState = GAME_OVER;
						}
						//riduce la potenza di fuoco
						firepower--;
						if (firepower < 1) firepower = 1;

					}
					spr2.setAlive(false);
					breakAsteroid(spr2);
				}
				//rendi l'astronave invincibile temporaneamente
				else if (spr1.getState() == STATE_EXPLODING) {
					if (collisionTimer + 3000 <
							System.currentTimeMillis()) {
						spr1.setState(STATE_NORMAL);
					}
				}
			}
			break;
		case SPRITE_POWERUP_SHIELD:
			if (spr2.getSpriteType()==SPRITE_SHIP) {
				shield += 5;
				if (shield > 20) shield = 20;
				spr1.setAlive(false);
			}
			break;

		case SPRITE_POWERUP_HEALTH:
			if (spr2.getSpriteType()==SPRITE_SHIP) {
				health += 5;
				if (health > 20) health = 20;
				spr1.setAlive(false);
			}
			break;

		case SPRITE_POWERUP_250:
			if (spr2.getSpriteType()==SPRITE_SHIP) {
				bumpScore(250);
				spr1.setAlive(false);
			}
			break;

		case SPRITE_POWERUP_500:
			if (spr2.getSpriteType()==SPRITE_SHIP) {
				bumpScore(500);
				spr1.setAlive(false);
			}
			break;

		case SPRITE_POWERUP_1000:
			if (spr2.getSpriteType()==SPRITE_SHIP) {
				bumpScore(1000);
				spr1.setAlive(false);
			}
			break;

		case SPRITE_POWERUP_GUN:
			if (spr2.getSpriteType()==SPRITE_SHIP) {
				firepower++;
				if (firepower > 5) firepower = 5;
				spr1.setAlive(false);
			}
			break;

		}

	}

	/*****************************************************
	 * Pressione dei pulsanti
	 *****************************************************/
	public void gameKeyDown(int keyCode) {
		switch(keyCode) {
		case KeyEvent.VK_LEFT:
			keyLeft = true;
			break;
		case KeyEvent.VK_RIGHT:
			keyRight = true;
			break;
		case KeyEvent.VK_UP:
			keyUp = true;
			break;
		case KeyEvent.VK_CONTROL:
			keyFire = true;
			break;
		case KeyEvent.VK_B:
			showBounds = !showBounds;
			break;
		case KeyEvent.VK_C:
			collisionTesting = !collisionTesting;
			break;
		case KeyEvent.VK_SHIFT:
			if ((!keyUp) && (shield > 0))
				keyShield = true;
			else
				keyShield = false;
			break;

		case KeyEvent.VK_ENTER:
			if (gameState == GAME_MENU) {
				resetGame();
				resumeGame();
				gameState = GAME_RUNNING;
			}
			else if (gameState == GAME_OVER || gameState == GAME_WON) {
				resetGame();
				resumeGame();
				gameState = GAME_RUNNING;
			}
			break;

		case KeyEvent.VK_ESCAPE:
			if (gameState == GAME_RUNNING) {
				pauseGame();
				resetShip();
				resumeGame();
				gameState = GAME_OVER;
			}
			break;
		}
	}

	public void gameKeyUp(int keyCode) {
		switch(keyCode) {
		case KeyEvent.VK_LEFT:
			keyLeft = false;
			break;
		case KeyEvent.VK_RIGHT:
			keyRight = false;
			break;
		case KeyEvent.VK_UP:
			keyUp = false;
			break;
		case KeyEvent.VK_CONTROL:
			keyFire = false;
			fireBullet();
			break;
		case KeyEvent.VK_SHIFT:
			keyShield = false;
			break;
		}
	}

	/* Disable mouse control */
	public void gameMouseDown()
	{

	}

	public void gameMouseUp()
	{

	}

	public void gameMouseMove()
	{

	}

	/*****************************************************
	 * distruzione asteroidi in asteroidi più piccoli
	 *****************************************************/
	private void breakAsteroid(AnimatedSprite sprite)
	{
		switch(sprite.getSpriteType()) {
		case SPRITE_ASTEROID_BIG:
			//asteroidi medi
			spawnAsteroid(sprite);
			spawnAsteroid(sprite);
			spawnAsteroid(sprite);
			//disegna esplosione grande
			startBigExplosion(sprite.getPosition());
			break;
		case SPRITE_ASTEROID_MEDIUM:
			//asteroidi piccoli
			spawnAsteroid(sprite);
			spawnAsteroid(sprite);
			spawnAsteroid(sprite);
			//disegna esplosione piccola
			startSmallExplosion(sprite.getPosition());
			break;
		case SPRITE_ASTEROID_SMALL:
			//asteroidi minuscoli
			spawnAsteroid(sprite);
			spawnAsteroid(sprite);
			spawnAsteroid(sprite);
			//disegna esplosione piccola
			startSmallExplosion(sprite.getPosition());
			break;
		case SPRITE_ASTEROID_TINY:
			//power up a random
			spawnPowerup(sprite);
			//disegna esplosione piccola
			startSmallExplosion(sprite.getPosition());
			break;
		}
	}

	private void spawnAsteroid(AnimatedSprite sprite) 
	{
		AnimatedSprite ast = new AnimatedSprite(this, graphics());
		ast.setAlive(true);

		int w = sprite.getBounds().width;
		int h = sprite.getBounds().height;
		double x = sprite.getPosition().X() + w/2 + rand.nextInt(20)-40;
		double y = sprite.getPosition().Y() + h/2 + rand.nextInt(20)-40;
		ast.setPosition(new Point2D(x,y));

		ast.setFaceAngle(rand.nextInt(360));
		ast.setMoveAngle(rand.nextInt(360));
		ast.setRotationRate(rand.nextDouble());

		double ang = ast.getMoveAngle() - 90;
		double velx = calculateAngleMoveX(ang);
		double vely = calculateAngleMoveY(ang);
		ast.setVelocity(new Point2D(velx, vely));

		switch(sprite.getSpriteType()) {
		case SPRITE_ASTEROID_BIG:
			ast.setSpriteType(SPRITE_ASTEROID_MEDIUM);

			int i = rand.nextInt(2);
			ast.setImage(medAsteroids[i].getImage());
			ast.setFrameWidth(medAsteroids[i].getWidth());
			ast.setFrameHeight(medAsteroids[i].getHeight());

			break;
		case SPRITE_ASTEROID_MEDIUM:
			ast.setSpriteType(SPRITE_ASTEROID_SMALL);

			i = rand.nextInt(3);
			ast.setImage(smlAsteroids[i].getImage());
			ast.setFrameWidth(smlAsteroids[i].getWidth());
			ast.setFrameHeight(smlAsteroids[i].getHeight());
			break;

		case SPRITE_ASTEROID_SMALL:
			ast.setSpriteType(SPRITE_ASTEROID_TINY);

			i = rand.nextInt(4);
			ast.setImage(tnyAsteroids[i].getImage());
			ast.setFrameWidth(tnyAsteroids[i].getWidth());
			ast.setFrameHeight(tnyAsteroids[i].getHeight());
			break;
		}

		getSprites().add(ast);
	}

	/*****************************************************
	 * Crea un powerup random
	 *****************************************************/
	private void spawnPowerup(AnimatedSprite sprite)
	{
		//solo qualche asteroide minuscolo da powerups
		int n = rand.nextInt(100);
		if (n > 24) return;

		AnimatedSprite spr = new AnimatedSprite(this, graphics());
		spr.setRotationRate(8);
		spr.setPosition(sprite.getPosition());
		double velx = rand.nextDouble();
		double vely = rand.nextDouble();
		spr.setVelocity(new Point2D(velx, vely));
		spr.setLifespan(1500);
		spr.setAlive(true);

		switch(rand.nextInt(6)) {
		case 0:
			spr.setImage(powerupShield.getImage());
			spr.setSpriteType(SPRITE_POWERUP_SHIELD);
			getSprites().add(spr);
			break;

		case 1:
			spr.setImage(powerupHealth.getImage());
			spr.setSpriteType(SPRITE_POWERUP_HEALTH);
			getSprites().add(spr);
			break;

		case 2:
			spr.setImage(powerup250.getImage());
			spr.setSpriteType(SPRITE_POWERUP_250);
			getSprites().add(spr);
			break;

		case 3:
			spr.setImage(powerup500.getImage());
			spr.setSpriteType(SPRITE_POWERUP_500);
			getSprites().add(spr);
			break;

		case 4:
			spr.setImage(powerup1000.getImage());
			spr.setSpriteType(SPRITE_POWERUP_1000);
			getSprites().add(spr);
			break;

		case 5:
			spr.setImage(powerupGun.getImage());
			spr.setSpriteType(SPRITE_POWERUP_GUN);
			getSprites().add(spr);
			break;

		}
	}

	public void createAsteroid()
	{
		AnimatedSprite ast = new AnimatedSprite(this, graphics());
		ast.setAlive(true);
		ast.setSpriteType(SPRITE_ASTEROID_BIG);

		int i = rand.nextInt(5);
		ast.setImage(bigAsteroids[i].getImage());
		ast.setFrameWidth(bigAsteroids[i].getWidth());
		ast.setFrameHeight(bigAsteroids[i].getHeight());

		int x = rand.nextInt(SCREENWIDTH - 128);
		int y = rand.nextInt(SCREENHEIGHT - 128);
		ast.setPosition(new Point2D(x, y));

		ast.setFaceAngle(rand.nextInt(360));
		ast.setMoveAngle(rand.nextInt(360));
		ast.setRotationRate(rand.nextDouble());

		double ang = ast.getMoveAngle() - 90;
		double velx = calculateAngleMoveX(ang);
		double vely = calculateAngleMoveY(ang);
		ast.setVelocity(new Point2D(velx, vely));

		getSprites().add(ast);
	}

	private boolean isAsteroid(int spriteType)
	{
		switch(spriteType)
		{
		case SPRITE_ASTEROID_BIG:
		case SPRITE_ASTEROID_MEDIUM:
		case SPRITE_ASTEROID_SMALL:
		case SPRITE_ASTEROID_TINY:
			return true;
		default:
			return false;
		}
	}

	public void checkInput()
	{
		if (gameState != GAME_RUNNING) return;

		AnimatedSprite ship = (AnimatedSprite)getSprites().get(0);
		if (keyLeft)
		{
			ship.setFaceAngle(ship.getFaceAngle() - SHIPROTATION);
			if (ship.getFaceAngle() < 0)
				ship.setFaceAngle(360 - SHIPROTATION);

		} else if (keyRight)
		{
			ship.setFaceAngle(ship.getFaceAngle() + SHIPROTATION);
			if (ship.getFaceAngle() > 360)
				ship.setFaceAngle(SHIPROTATION);
		}
		if (keyUp)
		{
			ship.setImage(shipImage[1].getImage());
			applyThrust();
		}
		else if (keyShield)
		{
			ship.setImage(shipImage[2].getImage());
		}
		else
		{
			ship.setImage(shipImage[0].getImage());
		}
	}

	public void applyThrust()
	{
		AnimatedSprite ship = (AnimatedSprite)getSprites().get(0);

		ship.setMoveAngle(ship.getFaceAngle() - 90);

		double velx = ship.getVelocity().X();
		velx += calculateAngleMoveX(ship.getMoveAngle()) * ACCELERATION;
		if (velx < -5) velx = -5;
		else if (velx > 5) velx = 5;
		double vely = ship.getVelocity().Y();
		vely += calculateAngleMoveY(ship.getMoveAngle()) * ACCELERATION;
		if (vely < -5) vely = -5;
		else if (vely > 5) vely = 5;
		ship.setVelocity(new Point2D(velx, vely));

	}

	/*****************************************************
	 * SBAM!
	 *****************************************************/
	public void fireBullet()
	{
		AnimatedSprite[] bullets = new AnimatedSprite[6];

		switch(firepower)
		{
		case 1:
			bullets[0] = stockBullet();
			getSprites().add(bullets[0]);
			break;

		case 2:
			bullets[0] = stockBullet();
			adjustDirection(bullets[0], -4);
			getSprites().add(bullets[0]);

			bullets[1] = stockBullet();
			adjustDirection(bullets[1], 4);
			getSprites().add(bullets[1]);

			break;

		case 3:
			bullets[0] = stockBullet();
			adjustDirection(bullets[0], -4);
			getSprites().add(bullets[0]);

			bullets[1] = stockBullet();
			getSprites().add(bullets[1]);

			bullets[2] = stockBullet();
			adjustDirection(bullets[2], 4);
			getSprites().add(bullets[2]);

			break;

		case 4:
			bullets[0] = stockBullet();
			adjustDirection(bullets[0], -5);
			getSprites().add(bullets[0]);

			bullets[1] = stockBullet();
			adjustDirection(bullets[1], 5);
			getSprites().add(bullets[1]);

			bullets[2] = stockBullet();
			adjustDirection(bullets[2], -10);
			getSprites().add(bullets[2]);

			bullets[3] = stockBullet();
			adjustDirection(bullets[3], 10);
			getSprites().add(bullets[3]);

			break;

		case 5:
			bullets[0] = stockBullet();
			adjustDirection(bullets[0], -6);
			getSprites().add(bullets[0]);

			bullets[1] = stockBullet();
			adjustDirection(bullets[1], 6);
			getSprites().add(bullets[1]);

			bullets[2] = stockBullet();
			adjustDirection(bullets[2], -15);
			getSprites().add(bullets[2]);

			bullets[3] = stockBullet();
			adjustDirection(bullets[3], 15);
			getSprites().add(bullets[3]);

			bullets[4] = stockBullet();
			adjustDirection(bullets[4], -60);
			getSprites().add(bullets[4]);

			bullets[5] = stockBullet();
			adjustDirection(bullets[5], 60);
			getSprites().add(bullets[5]);
			break;
		}
		shoot.play();

	}

	private void adjustDirection(AnimatedSprite sprite, double angle)
	{
		angle = sprite.getFaceAngle() + angle;
		if (angle < 0) angle += 360;
		else if (angle > 360) angle -= 360;
		sprite.setFaceAngle(angle);
		sprite.setMoveAngle(sprite.getFaceAngle()-90);
		angle = sprite.getMoveAngle();
		double svx = calculateAngleMoveX(angle) * BULLET_SPEED;
		double svy = calculateAngleMoveY(angle) * BULLET_SPEED;
		sprite.setVelocity(new Point2D(svx, svy));
	}

	private AnimatedSprite stockBullet()
	{
		AnimatedSprite ship = (AnimatedSprite)getSprites().get(0);

		AnimatedSprite bul = new AnimatedSprite(this, graphics());
		bul.setAlive(true);
		bul.setImage(bulletImage.getImage());
		bul.setFrameWidth(bulletImage.getWidth());
		bul.setFrameHeight(bulletImage.getHeight());
		bul.setSpriteType(SPRITE_BULLET);
		bul.setLifespan(90);
		bul.setFaceAngle(ship.getFaceAngle());
		bul.setMoveAngle(ship.getFaceAngle() - 90);
		double angle = bul.getMoveAngle();
		double svx = calculateAngleMoveX(angle) * BULLET_SPEED;
		double svy = calculateAngleMoveY(angle) * BULLET_SPEED;
		bul.setVelocity(new Point2D(svx, svy));
		double x = ship.getCenter().X() - bul.getImageWidth()/2;
		double y = ship.getCenter().Y() - bul.getImageHeight()/2;
		bul.setPosition(new Point2D(x,y));

		return bul;
	}

	public void startBigExplosion(Point2D point)
	{
		AnimatedSprite expl = new AnimatedSprite(this,graphics());
		expl.setSpriteType(SPRITE_EXPLOSION);
		expl.setAlive(true);
		expl.setAnimImage(explosions[0].getImage());
		expl.setTotalFrames(16);
		expl.setNumberOfAnimations(4);
		expl.setFrameWidth(96);
		expl.setFrameHeight(96);
		expl.setFrameDelay(2);
		expl.setPosition(point);

		getSprites().add(expl);

		explosion.play();
	}

	public void startSmallExplosion(Point2D point)
	{
		AnimatedSprite expl = new AnimatedSprite(this, graphics());
		expl.setSpriteType(SPRITE_EXPLOSION);
		expl.setAlive(true);
		expl.setAnimImage(explosions[1].getImage());
		expl.setTotalFrames(8);
		expl.setNumberOfAnimations(4);
		expl.setFrameWidth(40);
		expl.setFrameHeight(40);
		expl.setFrameDelay(2);
		expl.setPosition(point);

		getSprites().add(expl);

		explosion.play();

	}

	public void warp(AnimatedSprite spr)
	{
		int width = spr.getFrameWidth()-1;
		int height = spr.getFrameHeight()-1;
		if (spr.getPosition().X() < 0 - width)
			spr.getPosition().setX(SCREENWIDTH);
		else if (spr.getPosition().X() > SCREENWIDTH)
			spr.getPosition().setX(0 - width);
		if (spr.getPosition().Y() < 0 - height)
			spr.getPosition().setY(SCREENHEIGHT);
		else if (spr.getPosition().Y() > SCREENHEIGHT)
			spr.getPosition().setY(0 - height);
	}

	public void bumpScore(int howmuch) {
		score += howmuch;
		if (score > highscore)
			highscore = score;
	}
}
