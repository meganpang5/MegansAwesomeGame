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
//    Cake cake1;
//    Image cakeImage1;
//    Cake ncracker2;
//    Image ncrackerImage2;
    Image wood = Toolkit.getDefaultToolkit().getImage("gingham.png");
    Image baked = Toolkit.getDefaultToolkit().getImage("cake.png");
    public boolean firstchefCakeCrash;
    public boolean firstcakeOvenCrash;
//    public boolean firstchefCake1Crash;
    public boolean pressingKey;
    public boolean timesUp;
    public boolean cakeGrabbed;
//    public boolean cake1Grabbed;
    public boolean cakeBaked;

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
        cakes = new Cake [6];
        for (int x = 0; x < cakes.length ; x++){
            cakes[x] = new Cake("Cake " + x, (int)(Math.random()*WIDTH),(int)(Math.random()*HEIGHT), 0.25);
        }
//        cake1 = new Cake("cake1", 900, 100, 0.25);
//        cakeImage1 = Toolkit.getDefaultToolkit().getImage("bowl.png");
//        ncracker2 = new Cake("ncracker3", 500, 200, 0.25);
//        ncrackerImage2 = Toolkit.getDefaultToolkit().getImage("nutcracker.png");
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
//            if (astro1.isAlive == false){
//                astro1.width = astro1.width + 10;
//                astro1.height = astro1.height + 10;
//            }
            render();  // paint the graphics
            pause(30); // sleep for 10 ms
        }
    }

    public void moveThings() {
//        bunny.bounce();
        oven.bounce();
        if (cakeGrabbed == false && cake.isAlive == true){
            cake.wrap();
        }
        else if (cakeGrabbed == true && cake.isAlive == true){
            cake.xpos = chef.xpos;
            cake.ypos = chef.ypos;
            cake.rect = new Rectangle(cake.xpos, cake.ypos, cake.width, cake.height);
        }
        else if (cake.isAlive == false){
            cakeGrabbed = false;
            cake.dx = 0;
            cake.dy = 0;
        }
        for (int x = 0; x < cakes.length ; x++){
            cakes[x].wrap();
        }
//        if (cake1Grabbed == false && cake1.isAlive == true){
//            cake1.wrap();
//        }
//        else if (cake1Grabbed == true && cake1.isAlive == true){
//            cake1.xpos = chef.xpos;
//            cake1.ypos = chef.ypos;
//            cake1.rect = new Rectangle(cake1.xpos, cake1.ypos, cake1.width, cake1.height);
//        }
//        else if (cake1.isAlive == false){
//            cake1Grabbed = false;
//            cake1.dx = 0;
//            cake1.dy = 0;
//        }

        if (pressingKey) {
            chef.move();
        }
//        if(timesUp = true){
//            bunny.dx = -bunny.dx;
//            bunny.dy = -bunny.dy;
//        }


        cakeOvenCrash();
        chefCakeCrash();

    }

    public void cakeOvenCrash() {
        if (oven.rect.intersects(cake.rect) && firstcakeOvenCrash == true) {
            firstcakeOvenCrash = false;

//            double rand1 = Math.random();
//            double rand2 = Math.random();
//            if (rand1 + oven.successRate > rand2 + cake.successRate) {
                oven.dx = -oven.dx;
                oven.dy = -oven.dy;
                cake.dx = 0;
                cake.dy = 0;


//                ncracker.height+=50;
//                ncracker.width+=50;
//                chef.health = chef.health - 5;
                cake.isAlive = false;
//                timesUp = true;



//            else{
//                ncracker.dx = 0;
//                ncracker.dy = 0;
//                astro.dx = -astro.dx;
//                astro.dy = -astro.dy;
             //when they hit one of them stops and the other one expands


        }

        if (!oven.rect.intersects(cake.rect)) {
            firstcakeOvenCrash = true;

        }
    }

    public void chefCakeCrash() {
        if (chef.rect.intersects(cake.rect) && firstchefCakeCrash == true) {
            firstchefCakeCrash = false;

            double rand1 = Math.random();
            double rand2 = Math.random();
            if (rand1 + chef.successRate > rand2 + cake.successRate) {
                chef.dx = -chef.dy;
                chef.dy = -chef.dy;


//                oven.height += 50;
//                oven.width += 50;
//                chef.height += 50;
//                chef.width += 50;
//                chef.isAlive = false;
//            }
                cakeGrabbed = true;
//            else{w
//                ncracker.dx = 0;
//                ncracker.dy = 0;
//                astro.dx = -astro.dx;
//                astro.dy = -astro.dy;
            } //when they hit one of them stops and the other one expands

        }
        if(cakeGrabbed == true && cakeBaked == false){
            cake.xpos = chef.xpos;
            cake.ypos = chef.ypos;
            cake.dx = chef.dx;
            cake.dy = chef.dy;
        }

        if (!chef.rect.intersects(cake.rect) && cakeBaked == true) {
            firstchefCakeCrash = true;
//            cake.isAlive = false;

        }
    }



    //Paints things on the screen using bufferStrategy
    private void render() {
        Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();

        g.clearRect(0, 0, WIDTH, HEIGHT);
        //g.drawImage(astroImage,0,0,WIDTH, HEIGHT, null);
//        g.setColor(new Color(255, 240, 240));
//        g.fillRect(0,0,WIDTH, HEIGHT);
        g.drawImage(wood, 0, 0, WIDTH, HEIGHT, null);
//health bar:
//        g.setColor(new Color(217, 119, 119));
//        g.fillRect(800, 20, chef.health, 15);

        //draw the image
        g.drawImage(ovenImage, oven.xpos, oven.ypos, oven.width, oven.height, null);
        if (cake.isAlive == false) {
            g.drawImage(baked, cake.xpos, cake.ypos, cake.width, cake.height, null);
        } else {
            g.drawImage(cakeImage, cake.xpos, cake.ypos, cake.width, cake.height, null);

        }
        for (int x = 0; x < cakes.length ; x++){
            g.drawImage(cakeImage, cakes[x].xpos, cakes[x].ypos, cakes[x].width, cakes[x].height, null);
        }
//        if (cake1.isAlive == false) {
//            g.drawImage(baked, cake1.xpos, cake1.ypos, cake1.width, cake1.height, null);
//        } else {
//            g.drawImage(cakeImage1, cake1.xpos, cake1.ypos, cake1.width, cake1.height, null);
//
//        }
//        if(timesUp == true){
//            g.drawImage(bunnyImage, bunny.xpos, bunny.ypos, bunny.width, bunny.height, null);
//        }


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
//            bunny.ypos = bunny.ypos - bunny.dy;
//            bunny.rect = new Rectangle(bunny.xpos, bunny.ypos, bunny.width, bunny.height);
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
//            bunny.ypos = bunny.ypos - bunny.dy;
//            bunny.rect = new Rectangle(bunny.xpos, bunny.ypos, bunny.width, bunny.height);
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
//}
