package develop;

public interface Sizes {

	int canvasSizeX = 900;
	int canvasSizeY = 600;
	int rect1X = 0;// position on x coordinates for rect1
	int startX = canvasSizeY / 2 - 50; // position on y coordinates for rect1
										// and
										// rect2
	int rect2X = canvasSizeX - 20; // position on x coordinates for rect2
	int rectXsize = 20; // width of the rackets
	int rectYsize = 100;// height of the rackets
	int mouseInput = 1; // the number of pixels the rackets are moving in a
						// single move
	double increaseSpeed = 0.8; // speed increase after every hit
	double maxBallSpeed = 10; // the maximum number of pixels that the ball can
								// use for a movement

	int edge = 100;
}
