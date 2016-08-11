import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by PolarHIGH on 16.02.2016.
 */
public class RendererSetup extends JFrame {
    private static String TITLE = "Куросовая работа Боричев А.С. 23504/21";
    private static final int CANVAS_WIDTH = 1366;
    private static final int CANVAS_HEIGHT = 700;
    private static final int FPS = 100;

    public RendererSetup() {
        GLCanvas canvas = new RendererCanvas();
        canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));

        final FPSAnimator animator = new FPSAnimator(canvas, FPS, true);

        this.getContentPane().add(canvas);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                new Thread() {
                    @Override
                    public void run() {
                        if(animator.isStarted())
                            animator.stop();
                        System.exit(0);
                    }
                }.start();
            }
        });
        this.setTitle(TITLE);
        this.pack();
        this.setVisible(true);
        animator.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new RendererSetup();
            }
        });
    }
}
