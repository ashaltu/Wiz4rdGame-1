import java.awt.*;
import java.awt.image.BufferStrategy;

public class Game extends Canvas implements Runnable{
	
	public static final long serialVersionUID = 1L;
	
    private boolean isRunning = false;
    private Thread thread;
    private Handler handler;

    public Game() {
        new Window(1000, 563, "Wiz4rd Game", this);

        start();

        handler = new Handler();
        this.addKeyListener(new KeyInput(handler));
        
        handler.addObject(new Wizard(100, 100, ID.Player, handler));
    }

    //start thread
    private void start() {
        isRunning = true;
        thread = new Thread(this);  //"this" as in the run method of the class
        thread.start();
    }

    //stop thread
    private void stop() {
        isRunning = false;
        try {
            thread.join();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public void run() {
        this.requestFocus();
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        while(isRunning) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while(delta >= 1) {
                tick();
                //updates++;
                delta--;
            }
            render();
            frames++;

            if(System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                frames = 0;
                //updates = 0;
            }
        }
        stop();
    }

    //updates everything in game - 60 times a second
    public void tick() {
        handler.tick();
    }

    //render everything in game - couple thousand times a second
    public void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null) {
            this.createBufferStrategy(3);   //preloading frames before they are shown
            return;
        }

        Graphics g = bs.getDrawGraphics();
        //////////////////////////////////

        g.setColor(Color.red);
        g.fillRect(0, 0, 1000, 563);

        handler.render(g);

        //////////////////////////////////
        g.dispose();
        bs.show();
    }

    public static void main(String[] args) {
        new Game();
    }

}