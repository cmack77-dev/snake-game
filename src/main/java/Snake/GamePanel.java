package Snake;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;

public class GamePanel extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Sound
	static String AUDIO_FILE_PATH;
//	boolean SOUND;

	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 600;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
	static final int DELAY = 125;
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];
	int bodyParts = 6;
	int applesEaten;
	int appleX;
	int appleY;
	char direction = 'R';
	boolean running = false;
	Timer timer;
	Random random;

	GamePanel() {
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
		this.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.DARK_GRAY));
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	}

	public void startGame() {
		newApple();
		running = true;
		timer = new Timer(DELAY, this);

		timer.start();
	}

	public void checkIfStart(Graphics g) {

	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (SnakeGame.firstRun) {

			g.setColor(Color.red);
			g.setFont(new Font("Ink Free", Font.BOLD, 60));
			FontMetrics metrics2 = getFontMetrics(g.getFont());
			g.drawString("Welcome to Snake", (SCREEN_WIDTH - metrics2.stringWidth("Welcome to Snake")) / 2,
					SCREEN_HEIGHT / 2);

			g.setColor(Color.blue);
			g.setFont(new Font("Arial", Font.BOLD, 35));
			FontMetrics metrics3 = getFontMetrics(g.getFont());
			g.drawString("Press Enter to play!", (SCREEN_WIDTH - metrics3.stringWidth("Press Enter to play!")) / 2,
					450);
		} else {
			draw(g);
		}

	}

	public void draw(Graphics g) {
		if (running) {
//		for (int i=0;i<SCREEN_HEIGHT/UNIT_SIZE;i++) {
//			g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);	//Just to show
//			g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);	//Just to show
//		}
			g.setColor(Color.red);
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

			for (int i = 0; i < bodyParts; i++) {
				if (i == 0) {
					g.setColor(Color.green);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
//				g.drawImage(null, i, i, i, i, i, i, i, i, getFocusCycleRootAncestor())
				} else {
					// g.setColor(new Color(45,180,0));
					g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));

					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
			}

			// Display score
			g.setColor(Color.red);
			g.setFont(new Font("Ink Free", Font.BOLD, 40));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2,
					g.getFont().getSize());

		} else {
			gameOver(g);
		}
	}

	public void newApple() {
		appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
		appleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;

	}

	public void move() {
		for (int i = bodyParts; i > 0; i--) {
			x[i] = x[i - 1];
			y[i] = y[i - 1];
		}

		switch (direction) {
		case 'U':
			y[0] = y[0] - UNIT_SIZE;
			break;
		case 'D':
			y[0] = y[0] + UNIT_SIZE;
			break;
		case 'L':
			x[0] = x[0] - UNIT_SIZE;
			break;
		case 'R':
			x[0] = x[0] + UNIT_SIZE;
			break;
		}

	}

	public void checkApple() {
		if ((x[0] == appleX) && (y[0] == appleY)) {
			// SOUND = false;
			playSound("src/main/resources/yiha.wav");
			bodyParts++;
			applesEaten++;
			newApple();
		}

	}

	public void checkCollisions() {
		// Check if head touches with body
		for (int i = bodyParts; i > 0; i--) {
			if ((x[0] == x[i]) && (y[0] == y[i])) {
				running = false;
			}

			if (x[0] > SCREEN_WIDTH) {
				running = false;
			}

			if (x[0] < 0) {
				running = false;
			}

			if (y[0] < 0) {
				running = false;
			}

			if (y[0] > SCREEN_HEIGHT) {
				running = false;
			}
		}
		if (!running) {
			timer.stop();
		}
	}

	public void playSound(String audio) {
		AUDIO_FILE_PATH = audio;
		File file = new File(AUDIO_FILE_PATH);
		try {

			AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
			Clip clip = AudioSystem.getClip();
			clip.open(audioStream);

			clip.start();

		} catch (Exception e) {
			e.printStackTrace(System.out);
		}

	}

	public void gameOver(Graphics g) {
		playSound("src/main/resources/gameover.wav");
		// Display score
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD, 40));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: " + applesEaten)) / 2,
				g.getFont().getSize());

		// Game over text
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD, 75));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);
		g.setColor(Color.blue);
		g.setFont(new Font("Arial", Font.BOLD, 35));
		FontMetrics metrics3 = getFontMetrics(g.getFont());
		g.drawString("Press Enter to play again",
				(SCREEN_WIDTH - metrics3.stringWidth("Press Enter to play again")) / 2, 450);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (running) {
			move();
			checkApple();
			checkCollisions();
		}

		repaint();
	}

	public void restart() {
		SnakeGame.frame.dispose();
		SnakeGame.firstRun = false;
		SnakeGame.frame = new GameFrame();
	}

	public class MyKeyAdapter extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if (direction != 'R') {
					direction = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT:
				if (direction != 'L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_UP:
				if (direction != 'D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_DOWN:
				if (direction != 'U') {
					direction = 'D';
				}
				break;

			case KeyEvent.VK_ENTER:
				restart();
				break;

			}

		}
	}

}
