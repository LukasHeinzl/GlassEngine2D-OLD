package com.wolke7.ge2d;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.lang.Thread.UncaughtExceptionHandler;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import com.wolke7.ge2d.entities.Player;
import com.wolke7.ge2d.input.InputHandler;
import com.wolke7.ge2d.level.Level;
import com.wolke7.ge2d.rendering.Font;
import com.wolke7.ge2d.rendering.Renderer;
import com.wolke7.ge2d.rendering.Sprites;

public class Main extends Canvas implements Runnable, UncaughtExceptionHandler {

    private static final long serialVersionUID = 1L;
    private static final int WIDTH = 500;
    private static final int HEIGHT = 300;
    private static final int SCALE = 3;
    private static JFrame frame = new JFrame();
    private static BufferedImage img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    private static int[] pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
    private static Player player;
    private static Sprites defaultSprites;
    private static boolean normal = true;
    private static String[] errorMessage = new String[0];
    private static GameState currentState = GameState.INIT;
    private boolean running = false;
    private boolean showDebug = false;
    private boolean fullScreen = false;
    private Dimension lastSize = null;
    private Point lastLocation = null;

    public static void main(String[] args) {
        Main game = new Main();
        Thread.setDefaultUncaughtExceptionHandler(game);
        frame.setTitle("GlassEngine2D");
        frame.setIconImage(new ImageIcon(Main.class.getResource("/default_icon.png")).getImage());

        frame.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        frame.setSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(game, BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);

        Main.defaultSprites = new Sprites("/default_sprites.png", true);
        Font.init(Main.defaultSprites);
        InputHandler.init(game);
        Renderer.init(Main.WIDTH, Main.HEIGHT, Sprites.DUMMYSPRITES);
        game.start();

        if (!Configuration.loadConfig()) {
            showErrorScreen("configuration error:", "\tbase-configuration not found!");
        } else {
            game.init();
        }
    }

    private void init() {
        if (Configuration.exists("icon")) {
            ImageIcon img = new ImageIcon(Configuration.getFilePath("icon"));
            frame.setIconImage(img.getImage());
        }

        if (Configuration.exists("title")) {
            String title = Configuration.getEntry("title");
            frame.setTitle(title.isEmpty() ? "GlassEngine2D" : title);
        }

        Renderer.init(Main.WIDTH, Main.HEIGHT, new Sprites(Configuration.getFilePath("file", "sprites"), false));
        //Level.loadLevelFromFile(Configuration.getEntry("background", "mainmenu"), false);

        Level.loadLevelFromFile(Configuration.getFilePath("default", "level"), false);

        int width = Configuration.getInt("max-width", "player");
        int height = Configuration.getInt("max-height",
                "player");
        int health = Configuration.getInt("health", "player");
        int maxHealth =
                Configuration.getInt("max-health", "player");
        int texX = Configuration.getInt("sprites-x", "player");
        int
                texY = Configuration.getInt("sprites-y", "player");
        int waterX = Configuration.getInt("water-x", "player");
        int waterY = Configuration.getInt("water-y", "player");
        int heartX = Configuration.getInt("heart-x",
                "player");
        int heartY = Configuration.getInt("heart-y", "player");
        boolean cpui = false;
        int invMax = 0;

        if (Configuration.exists("can-pickup-items", "player")) {
            cpui = Configuration.getBoolean("can-pickup-items",
                    "player");
        }

        if (Configuration.exists("inventory-slots", "player")) {
            invMax = Configuration.getInt("inventory-slots",
                    "player");
        }

        Main.player = new Player(10, 50, width, height, health, maxHealth, texX, texY, waterX + waterY * 64, heartX +
                heartY * 64, cpui, invMax);

        Level.addEntity(Main.player);


        Main.currentState = GameState.INLEVEL;
    }

    public synchronized void start() {
        running = true;
        new Thread(this, "GAME:main").start();
    }

    public void stop() {
        if (running) {
            running = false;
        }
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double nsPerTick = 1000000000D / 60D;

        long lastTimer = System.currentTimeMillis();
        double delta = 0;

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / nsPerTick;
            lastTime = now;
            boolean shouldRender = true;

            while (delta >= 1) {
                tick();
                delta -= 1;
                shouldRender = true;
            }

            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if (shouldRender) {
                render();
            }

            if (System.currentTimeMillis() - lastTimer > 1000) {
                lastTimer += 1000;
            }
        }
    }

    public void tick() {
        if (Main.currentState == GameState.INIT) {
            return;
        }

        if (Main.currentState == GameState.INLEVEL) {
            Level.tick();

            if (InputHandler.isPressed(KeyEvent.VK_F3) && !showDebug) {
                InputHandler.setPressed(KeyEvent.VK_F3, false);
                showDebug = true;
            } else if (InputHandler.isPressed(KeyEvent.VK_F3) && showDebug) {
                InputHandler.setPressed(KeyEvent.VK_F3, false);
                showDebug = false;
            }

            if (InputHandler.isPressed(KeyEvent.VK_F11) && !fullScreen) {
                InputHandler.setPressed(KeyEvent.VK_F11, false);

                fullScreen = true;
                lastSize = frame.getSize();
                lastLocation = frame.getLocation();
                frame.dispose();

                frame.setUndecorated(true);
                frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());

                frame.setLocation(0, 0);
                frame.setVisible(true);
            } else if (InputHandler.isPressed(KeyEvent.VK_F11) && fullScreen) {
                InputHandler.setPressed(KeyEvent.VK_F11, false);

                fullScreen = false;
                frame.dispose();

                frame.setUndecorated(false);
                frame.setSize(lastSize);

                frame.setLocation(lastLocation);
                frame.setVisible(true);
            }
        } else if (Main.currentState == GameState.INMENU) {
            Level.getEntities().clear();
            Level.loadLevelFromFile(Configuration.getEntry("background", "mainmenu"), false);
        }
    }

    public void render() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }

        if (Main.currentState == GameState.INIT && Main.normal) {
            return;
        }

        if (Main.normal) {
            if (Main.currentState == GameState.INLEVEL) {
                int x = player.getLocation().getX();
                int y = player.getLocation().getY();
                int xOffset = player.getLocation().getxOffset();
                int yOffset = player.getLocation().getyOffset();

                Level.renderTiles(xOffset, yOffset);
                Level.renderEntities();

                if (showDebug) {
                    Font.render("x: " + x + " / y: " + y, 0, HEIGHT - 30, 1);
                    Font.render("xO: " + xOffset + " / yO: " + yOffset, 0, HEIGHT - 20, 1);
                    Font.render("rxo: " + Renderer.getXOffset() + " / ryo: " + Renderer.getYOffset(), 0, HEIGHT - 10, 1);
                }
            } else if (Main.currentState == GameState.INMENU) {
                Level.renderTiles(0, 0);
            }
        } else {
            Level.renderTiles(0, 0);
            int offset = 0;

            for (String line : Main.errorMessage) {
                Font.render(line, 20, HEIGHT - 75 + offset, 1);
                offset += 9;
            }
        }

        Renderer.renderPixels(pixels);

        Graphics g = bs.getDrawGraphics();
        g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
        g.dispose();
        bs.show();
    }

    public static Sprites getDefaultSprites() {
        return Main.defaultSprites;
    }

    public static void showErrorScreen(String... errorMessage) {
        Main.normal = false;
        Main.errorMessage = errorMessage;
        Level.getEntities().clear();
        Level.loadLevelFromFile("/sadface.png", true);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        System.out.println("Error");
        if (e instanceof ConfErr) {
            Main.showErrorScreen("configuration error:", "\t" + e.getMessage());
            return;
        }

        Main.showErrorScreen("Oops, an error occurred while running the game:", "", "\tin: " + e.getStackTrace()[0].getClassName(),
                "\tcause: " + e.getMessage());
    }

}
