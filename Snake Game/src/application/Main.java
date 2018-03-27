package application;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Random;

public class Main extends Application implements Help {

    private Timeline timeline = new Timeline();

    private boolean running = false;
    private boolean move = false;
    private ObservableList<Node> snake;
    private Direction direction = Direction.RIGHT;

    public static void main(String[] args) {
        launch(args);
    }

    public Parent createContent() {
        Pane layout = new Pane();
        layout.setBorder(new Border(
                new BorderStroke(Help.bodyColor, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        layout.getStyleClass().add("pane");
        layout.setPrefSize(Help.paneH, Help.paneW);

        Random rnd = new Random();

        // creating snake body
        Group snakeBody = new Group();
        snake = snakeBody.getChildren();

        // creating food
        Circle food = new Circle(Help.circleSize);
        food.setFill(Help.foodColor);
        food.setTranslateX(rnd.nextInt(26) * Help.move);
        food.setTranslateY(rnd.nextInt(26) * Help.move);

        KeyFrame frame = new KeyFrame(Duration.seconds(0.2), event -> {
            if (!running) {
                return;
            }
            boolean delete = snake.size() > 1;
            Node tail = delete ? snake.remove(snake.size() - 1) : snake.get(0);
            double tailX = snake.get(0).getTranslateX();
            double tailY = snake.get(0).getTranslateY();
            // moving a circle
            tail = snakeMove(tail);
            move = true;

            if (delete) {
                snake.add(0, tail);
            }
            // colision with margins
            die(tail);
            // colision with food
            eat(tail, food, rnd, tailX, tailY);
            color();

        });
        // timer setup
        timeline.getKeyFrames().add(frame);
        timeline.setCycleCount(Timeline.INDEFINITE);
        layout.getChildren().addAll(food, snakeBody);
        return layout;

    }

    public void color() {
        if (((Circle) snake.get(0)).getFill() != Help.headColor) {
            ((Circle) snake.get(0)).setFill(Help.headColor);
        }
        for (int i = 1; i < snake.size(); i++) {
            if (((Circle) snake.get(i)).getFill() != Help.headColor) {
                ((Circle) snake.get(i)).setFill(Help.bodyColor);
            }
        }
    }

    public void eat(Node tail, Circle food, Random rnd, double tailX, double tailY) {
        if (tail.getTranslateX() == food.getTranslateX() && tail.getTranslateY() == food.getTranslateY()) {
            food.setTranslateX(rnd.nextInt(26) * Help.move);
            food.setTranslateY(rnd.nextInt(26) * Help.move);

            Circle body = new Circle(Help.circleSize);
            body.setFill(Help.headColor);
            body.setTranslateX(tailX);
            body.setTranslateY(tailY);

            snake.add(body);
        }
    }

    public void die(Node tail) {
        // colision with snake
        if (tail.getTranslateX() == 0 || tail.getTranslateX() == Help.KillW || tail.getTranslateY() == 0
                || tail.getTranslateY() == KillW) {
            timeline.stop();
        }
        // colision with margins
        for (Node body : snake) {
            if (body != tail && tail.getTranslateX() == body.getTranslateX()
                    && tail.getTranslateY() == body.getTranslateY()) {
                timeline.stop();
                break;
            }
        }
    }

    public Node snakeMove(Node tail) {

        switch (direction) {
            case UP:
                tail.setTranslateX(snake.get(0).getTranslateX());
                tail.setTranslateY(snake.get(0).getTranslateY() - Help.move);
                break;
            case DOWN:
                tail.setTranslateX(snake.get(0).getTranslateX());
                tail.setTranslateY(snake.get(0).getTranslateY() + Help.move);
                break;
            case LEFT:
                tail.setTranslateX(snake.get(0).getTranslateX() - Help.move);
                tail.setTranslateY(snake.get(0).getTranslateY());
                break;
            case RIGHT:
                tail.setTranslateX(snake.get(0).getTranslateX() + Help.move);
                tail.setTranslateY(snake.get(0).getTranslateY());
                break;
        }
        return tail;
    }

    public void startGame() {
        direction = Direction.RIGHT;
        Circle head = new Circle(Help.circleSize);
        head.setFill(Help.headColor);
        head.setTranslateX(Help.startPointX);
        head.setTranslateY(Help.startPointY);
        snake.add(head);
        timeline.play();
        running = true;
    }

    public void start(Stage primaryStage) {
        Scene scene = new Scene(createContent());
        // key press setup
        scene.setOnKeyPressed(e -> {
            if (!move)
                return;
            switch (e.getCode()) {
                case W:
                    if (direction != Direction.DOWN)
                        direction = Direction.UP;
                    break;
                case S:
                    if (direction != Direction.UP)
                        direction = Direction.DOWN;
                    break;
                case A:
                    if (direction != Direction.RIGHT)
                        direction = Direction.LEFT;
                    break;
                case D:
                    if (direction != Direction.LEFT)
                        direction = Direction.RIGHT;
                    break;
                default:
                    break;

            }
            move = false;
        });
        // set the graphics content
        scene.setFill(Color.BISQUE);
        // primaryStage.setFullScreen(true);
        primaryStage.setScene(scene);
        primaryStage.show();
        // start the game
        startGame();

    }

    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

}