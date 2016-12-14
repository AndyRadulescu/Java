package application;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.effect.Reflection;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * 
 * @author Andy Radulesscu
 * 
 *         This class makes all the graphic content for a table tennis game
 * 
 */
public class TableTennis extends Application implements Sizes, Runnable {
	// directions
	private enum Direction {
		LEFT, RIGHT;
	}

	private Timeline timeline = new Timeline(); // Timeline object creation
	private Direction direction = Direction.RIGHT;

	private double height = 0;
	private double width = 1;
	private double ballSpeed = 1;
	private double time = 0.005;

	private int score = 0;

	private Rectangle rect1 = new Rectangle(rectXsize, rectYsize);
	private Rectangle rect2 = new Rectangle(rectXsize, rectYsize);
	private Circle ball = new Circle(10);
	private Label label;

	private Parent createContent() {
		Pane layout = new Pane();// layout creation
		layout.setPrefSize(canvasSizeX, canvasSizeY);

		// this whole section makes a shadow for the rackets
		// to look more realistic
		InnerShadow scoreShadow = new InnerShadow();
		scoreShadow.setOffsetX(3.0f);
		scoreShadow.setOffsetY(3.0f);
		scoreShadow.setColor(Color.color(0.6f, 0.6f, 0.6f));
		DropShadow ds = new DropShadow();
		ds.setOffsetY(3.0f);
		ds.setColor(Color.color(0, 0, 0));

		// 2 rectangle creation ,going to be the rackets
		rect1.setTranslateX(rect1X);
		rect1.setTranslateY(startX);
		rect1.setArcHeight(edge);// making the edges smooth
		rect1.setArcWidth(edge);
		rect1.setEffect(ds);
		rect2.setTranslateX(rect2X);
		rect2.setTranslateY(startX);
		rect2.setArcHeight(edge);// making the edges smooth
		rect2.setArcWidth(edge);
		rect2.setEffect(ds);

		// this label object is going to be the score panel
		label = new Label();
		label.setEffect(scoreShadow);
		label.setTranslateX(100);
		label.setTranslateY(0);
		label.setText("Score:" + String.valueOf(score));
		label.setFont(Font.font("Arial", 100));
		label.setTextAlignment(TextAlignment.CENTER);
		label.setTextFill(Color.LIGHTGREY);

		// setting the game timer and ball movement
		KeyFrame frame = new KeyFrame(Duration.seconds(time), event -> {
			switch (direction) {
			case RIGHT:
				ball.setTranslateX(ball.getTranslateX() + width);
				ball.setTranslateY(ball.getTranslateY() + height);
				break;
			case LEFT:
				ball.setTranslateX(ball.getTranslateX() - width);
				ball.setTranslateY(ball.getTranslateY() + height);
				break;
			default:
				break;
			}
			die();// method to stop the animation
			bounce(rect1, rect2);// method to bounce the ball when on rackets
									// or the top and the bottom lines
		});

		timeline.getKeyFrames().add(frame);// setting up the timeline
		timeline.setCycleCount(Timeline.INDEFINITE);
		layout.getChildren().addAll(label, ball, rect1, rect2);

		return layout;
	}

	// the method die
	private void die() {
		if (ball.getTranslateX() >= (canvasSizeX - 10) || ball.getTranslateX() <= 10) {
			timeline.stop();
			height = 0;
			ballSpeed = 1;
			score = 0;
			label.setText("Score:" + String.valueOf(score));
			width = 1;
			height = 0;
			startGame();
		}
	}

	// bounce method
	public void bounce(Rectangle rect1, Rectangle rect2) {

		double value1 = (double) (ball.getTranslateY() - rect1.getTranslateY());
		double value2 = (double) (ball.getTranslateY() - rect2.getTranslateY());
		// testing if the ball is between the top and bottom part of the right
		// racket
		// to make the ball squish effect i made the ball to go 5 pixels
		// underneath the rackets
		if ((ball.getTranslateX() >= canvasSizeX - 25) && (ball.getTranslateY() >= rect2.getTranslateY() - 10
				&& ball.getTranslateY() <= rect2.getTranslateY() + 110)) {
			direction = Direction.LEFT;
			calculate(value2);
			if (ballSpeed + incressSpeed < maxBallSpeed)
				ballSpeed += incressSpeed;
			score += 100;
			label.setText("Score:" + String.valueOf(score));

		}
		// testing if the ball is between the top and bottom part of the left
		// racket
		if ((ball.getTranslateX() <= 25) && (ball.getTranslateY() >= rect1.getTranslateY() - 10
				&& ball.getTranslateY() <= rect1.getTranslateY() + 110)) {
			direction = Direction.RIGHT;
			calculate(value1);
			if (ballSpeed + incressSpeed < maxBallSpeed)
				ballSpeed += incressSpeed;
			score += 100;
			label.setText("Score:" + String.valueOf(score));
		}

		// bounce on the top and bottom walls
		if (ball.getTranslateY() < 10 || ball.getTranslateY() > canvasSizeY)
			height = -height;

	}

	// calculates how much the ball should go up or down
	// the ball is should move in any direction with 1 pixel
	// this is where its calculated the x and y for the ball that x^2+x^2=1
	private void calculate(double value) {
		double y = (50 - value) / 100 * 2;
		if (y >= 1)
			y = 0.98;
		if (y <= -1)
			y = -0.98;
		double x = Math.sqrt(ballSpeed - y * y);
		height = -y;
		width = x;

	}

	private Blend effect() {
		// two effects are made g\here, an external shadow and a reflection
		Blend blend = new Blend();
		blend.setMode(BlendMode.MULTIPLY);
		Reflection r = new Reflection();
		r.setFraction(1f);
		DropShadow ds = new DropShadow();
		ds.setOffsetY(3.0f);
		ds.setColor(Color.color(0.f, 0.f, 0.f));
		blend.setTopInput(r);
		blend.setBottomInput(ds);
		return blend;
	}

	private void startGame() {
		// this is where the ball is instantiated ,it also takes an image inside
		// of the circle object ,to look more realistic
		Image image = new Image("/application/sphere4.png");
		direction = Direction.RIGHT;
		ball.setTranslateX(canvasSizeX / 2);
		ball.setTranslateY(canvasSizeY / 2);
		ball.setFill(new ImagePattern(image));
		ball.setEffect(effect());
		timeline.play();
	}

	private int mousePointer = 0;

	private void movement(Scene scene) {

		// moving the rackets
		// the mouse will act for the right racket by pressing LMB and dragging
		// up or down
		scene.setOnMousePressed(e -> {
			mousePointer = (int) e.getSceneY();
		});
		scene.setOnMouseDragged(e -> {
			if (e.isPrimaryButtonDown()) {
				if (e.getSceneY() < mousePointer) {
					if (rect2.getTranslateY() != 0) {
						rect2.setTranslateX(rect2.getTranslateX());
						rect2.setTranslateY(rect2.getTranslateY() - mouseInput);

					}
				} else if (rect2.getTranslateY() != canvasSizeY - rectYsize) {
					rect2.setTranslateX(rect2.getTranslateX());
					rect2.setTranslateY(rect2.getTranslateY() + mouseInput);

				}
				mousePointer = (int) e.getSceneY();
			}
		});
		// the keys W and S will move the left racket
		scene.setOnKeyPressed(e -> {

			switch (e.getCode()) {
			case W:
				if (rect1.getTranslateY() != 0) {
					rect1.setTranslateX(rect1.getTranslateX());
					rect1.setTranslateY(rect1.getTranslateY() - 10);
				}
				break;
			case S:
				if (rect1.getTranslateY() != canvasSizeY - rectYsize) {
					rect1.setTranslateX(rect1.getTranslateX());
					rect1.setTranslateY(rect1.getTranslateY() + 10);
				}
				break;
			default:
				break;
			}
		});
	}

	@Override
	public void start(Stage primaryStage) {

		Scene scene = new Scene(createContent());
		movement(scene);// setting up racket movement
		primaryStage.setScene(scene);
		primaryStage.show();
		startGame();
	}

	@Override
	public void run() {
		doWork();
	}

	private synchronized void doWork() {
		launch();
	}
}
