//Basic Game Application
// Basic Object, Image, Movement
// Threaded

//*******************************************************************************
//Import Section
//Add Java libraries needed for the game
//import java.awt.Canvas;

//Graphics Libraries
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;

//*******************************************************************************

public class BasicGameApp implements Runnable, KeyListener {

    //Variable Definition Section
    //Declare the variables used in the program
    //You can set their initial values too

    //Sets the width and height of the program window
    final int WIDTH = 1000;
    final int HEIGHT = 700;

    //Declare the variables needed for the graphics
    public JFrame frame;
    public Canvas canvas;
    public JPanel panel;
    public BufferStrategy bufferStrategy;

    Chef chef;
    Oven oven;
    Image ovenImage;
    Image chefImage;
    Cake cake;
    Image cakeImage;
    Cake[] cakes;
    Image wood = Toolkit.getDefaultToolkit().getImage("gingham.png");
    Image baked = Toolkit.getDefaultToolkit().getImage("cake.png");
    public boolean firstchefCakeCrash;
    public boolean firstcakeOvenCrash;
    public boolean pressingKey;
    public boolean timesUp;
    public boolean cakeGrabbed;
    public boolean cakeBaked;
    int activeCakeNumber = 0;

    // Main method definition
    // This is the code that runs first and automatically
    public static void main(String[] args) {
        BasicGameApp ex = new BasicGameApp();   //creates a new instance of the game
        new Thread(ex).start();                 //creates a threads & starts up the code in the run( ) method
    }


    // This section is the setup portion of the program
    // Initialize your variables and construct your program objects here.
    public BasicGameApp() { // BasicGameApp constructor

        setUpGraphics();
        firstcakeOvenCrash = true;
        firstchefCakeCrash = true;
        pressingKey = false;
        timesUp = false;
        cakeGrabbed = false;
        chef = new Chef("chef", 0, 0, 0.75);
        chefImage = Toolkit.getDefaultToolkit().getImage("chef.png");
        oven = new Oven("oven", 300, 400, 0.75);
        ovenImage = Toolkit.getDefaultToolkit().getImage("oven.png");
        cake = new Cake("cake", 100, 200, 0.25);
        cakeImage = Toolkit.getDefaultToolkit().getImage("bowl.png");
        cakes = new Cake [10];
        for (int x = 0; x < cakes.length ; x++){
            cakes[x] = new Cake("Cake " + x, (int)(Math.random()*WIDTH), (int)(Math.random()*HEIGHT), 0.25);
            cakes[x].isAlive = false;
            cakes[x].isBaked = false;
        }
        activeCakeNumber = 0;
        cakes[0].isAlive = true;
        run();

    } // end BasicGameApp constructor


//*******************************************************************************
//User Method Section
// put your code to do things here.

    // main thread
    // this is the code that plays the game after you set things up
    public void run() {
        //for the moment we will loop things forever.
        while (true) {
            moveThings(); //move all the game object
            render();  // paint the graphics
            pause(30); // sleep for 10 ms
        }
    }

    public void moveThings() {
        oven.bounce();

        if (pressingKey) {
            chef.move();
        }

        Cake activeCake = cakes[activeCakeNumber];

        // If batter is in hand, force it to be on the chef before collisions
        if (cakeGrabbed == true && activeCake.isAlive == true && activeCake.isBaked == false) {
            activeCake.xpos = chef.xpos;
            activeCake.ypos = chef.ypos;
            activeCake.dx = chef.dx;
            activeCake.dy = chef.dy;
            activeCake.rect = new Rectangle(activeCake.xpos, activeCake.ypos, activeCake.width, activeCake.height);
        }
        // Otherwise batter moves freely
        else if (activeCake.isAlive == true && activeCake.isBaked == false) {
            activeCake.wrap();
        }

        //checking collisions
        chefCakesCrash();
        cakeOvenCrash();
    }

    public void cakeOvenCrash() {
        Cake activeCake = cakes[activeCakeNumber];

        if (activeCake.isAlive && oven.rect.intersects(activeCake.rect) && cakeGrabbed == true) {

            // bounce oven
            oven.dx = -oven.dx;
            oven.dy = -oven.dy;

            // bake cake in place (where the batter currently is)
            activeCake.dx = 0;
            activeCake.dy = 0;
            activeCake.isBaked = true;

            // hand becomes empty
            cakeGrabbed = false;

            // activeCake stays alive so it can sit there as a baked cake
            activeCake.rect = new Rectangle(activeCake.xpos, activeCake.ypos, activeCake.width, activeCake.height);

            // spawn new batter somewhere else
            spawnNextCake();
        }
    }

    public void chefCakesCrash() {
        Cake activeCake = cakes[activeCakeNumber];

        if (activeCake.isAlive && chef.rect.intersects(activeCake.rect)) {
            cakeGrabbed = true;
        }

        if (cakeGrabbed == true && cakeBaked == false) {
            activeCake.xpos = chef.xpos;
            activeCake.ypos = chef.ypos;
            activeCake.dx = chef.dx;
            activeCake.dy = chef.dy;
            activeCake.rect = new Rectangle(activeCake.xpos, activeCake.ypos, activeCake.width, activeCake.height);
        }
    }

    public void spawnNextCake() {
        cakeBaked = false;

        activeCakeNumber++;
        if (activeCakeNumber >= cakes.length) {
            activeCakeNumber = 0;
        }

        Cake next = cakes[activeCakeNumber];
        next.xpos = (int)(Math.random() * WIDTH);
        next.ypos = (int)(Math.random() * HEIGHT);
        next.dx = (int)(Math.random() * 11) - 5;
        next.dy = (int)(Math.random() * 11) - 5;

        next.isBaked = false;
        next.isAlive = true;

        next.rect = new Rectangle(next.xpos, next.ypos, next.width, next.height);

        cakeGrabbed = false;
    }



    //Paints things on the screen using bufferStrategy
    private void render() {
        Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();

        g.clearRect(0, 0, WIDTH, HEIGHT);
        g.drawImage(wood, 0, 0, WIDTH, HEIGHT, null);

        g.drawImage(ovenImage, oven.xpos, oven.ypos, oven.width, oven.height, null);

        // draw all baked cakes
        for (int i = 0; i < cakes.length; i++) {
            if (cakes[i].isBaked == true) {
                g.drawImage(baked, cakes[i].xpos, cakes[i].ypos, cakes[i].width, cakes[i].height, null);
            }
        }

        // draw ONLY the active batter
        Cake activeCake = cakes[activeCakeNumber];
        if (activeCake.isAlive == true && activeCake.isBaked == false) {
            g.drawImage(cakeImage, activeCake.xpos, activeCake.ypos, activeCake.width, activeCake.height, null);
        }

        g.drawImage(chefImage, chef.xpos, chef.ypos, chef.width, chef.height, null);

        g.dispose();
        bufferStrategy.show();
    }

    //Pauses or sleeps the computer for the amount specified in milliseconds
    public void pause(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
        }
    }

    //Graphics setup method
    private void setUpGraphics() {
        frame = new JFrame("Application Template");   //Create the program window or frame.  Names it.

        panel = (JPanel) frame.getContentPane();  //sets up a JPanel which is what goes in the frame
        panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));  //sizes the JPanel
        panel.setLayout(null);   //set the layout

        // creates a canvas which is a blank rectangular area of the screen onto which the application can draw
        // and trap input events (Mouse and Keyboard events)
        canvas = new Canvas();
        canvas.setBounds(0, 0, WIDTH, HEIGHT);
        canvas.setIgnoreRepaint(true);

        panel.add(canvas);  // adds the canvas to the panel.

        // frame operations
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //makes the frame close and exit nicely
        frame.pack();  //adjusts the frame and its contents so the sizes are at their default or larger
        frame.setResizable(false);   //makes it so the frame cannot be resized
        frame.setVisible(true);      //IMPORTANT!!!  if the frame is not set to visible it will not appear on the screen!

        // sets up things so the screen displays images nicely.
        canvas.createBufferStrategy(2);
        bufferStrategy = canvas.getBufferStrategy();
//        canvas.addKeyListener(this);
        canvas.requestFocus();
        canvas.addKeyListener(this);
        System.out.println("DONE graphic setup");
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println(e.getKeyCode());
        pressingKey = true;
        if (e.getKeyCode() == 87) {
            chef.dy = -10;
            chef.dx = 0;
        }
        if (e.getKeyCode() == 65) {
            chef.dx = -10;
            chef.dy = 0;

        }
        if (e.getKeyCode() == 83) {
            chef.dy = 10;
            chef.dx = 0;
        }
        if (e.getKeyCode() == 68) {
            chef.dx = 10;
            chef.dy = 0;

        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressingKey= false;
        if (e.getKeyCode() == 87) {
            chef.dy = 0;
            chef.dx = 0;
        }
        if (e.getKeyCode() == 65) {
            chef.dx = 0;
            chef.dy = 0;

        }
        if (e.getKeyCode() == 83) {
            chef.dy = 0;
            chef.dx = 0;
        }
        if (e.getKeyCode() == 68) {
            chef.dx = 0;
            chef.dy = 0;

        }
    }
}
