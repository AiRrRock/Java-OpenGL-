import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.jogamp.opengl.GL.*;
import static com.jogamp.opengl.GL2.*;
import static com.jogamp.opengl.glu.GLU.*;

public class RendererCanvas extends GLCanvas implements GLEventListener, KeyListener {
    private static final String CYLINDER_TEXTURE_FILE = "red_x.bmp";
    private static final String CONE_TEXTURE_FILE = "red_x.bmp";

    private static final int CYLINDER_LIST_ID = 1;
    private static final int CONE_LIST_ID = 2;

    private GLU glu;
    private GLUT glut;

    private float m_cylinderConeRotY = 0f;
    private float m_cylinderConeRotX = 90f;
    private float sphere_size = 20.7f;
    private float m_cubeX =40f;
    public RendererCanvas() {
        this.addGLEventListener(this);
        this.addKeyListener(this);
    }

    private void prepareCylinderList(GL2 gl, float radius, float height, int listId) {
        Path texturePath = FileSystems.getDefault().getPath(RendererSetup.TEXTURES_PATH, CYLINDER_TEXTURE_FILE);

        try (InputStream is = Files.newInputStream(texturePath)){
            Texture cylinderTexture = TextureIO.newTexture(is, true, "");

            GLUquadric cylinder = glu.gluNewQuadric();
            glu.gluQuadricDrawStyle(cylinder, GLU_FILL);
            glu.gluQuadricTexture(cylinder, true);
            glu.gluQuadricNormals(cylinder, GLU_SMOOTH);


            gl.glNewList(listId, GL_COMPILE);
            cylinderTexture.enable(gl);
            cylinderTexture.bind(gl);
            glu.gluCylinder(cylinder, radius, radius, height, 100, 100);



            // top of cylinder
            gl.glPushMatrix();
            gl.glRotatef(180f, 1f, 0f, 0f);
            gl.glRotatef(180f, 0f, 0f, 1f);
            glu.gluDisk(cylinder, 0, radius, 25, 25);
            gl.glPopMatrix();

            // bottom of cylinder
            gl.glPushMatrix();
            gl.glTranslatef(0f, 0f, height);
            glu.gluDisk(cylinder, 0, radius, 25, 25);
            gl.glPopMatrix();

            cylinderTexture.disable(gl);
            gl.glEndList();

            glu.gluDeleteQuadric(cylinder);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GLException e) {
            e.printStackTrace();
        }
    }

    private void prepareConeList(GL2 gl, float radius, float height, int listId) {
        Path texturePath = FileSystems.getDefault().getPath(RendererSetup.TEXTURES_PATH, CONE_TEXTURE_FILE);

        try (InputStream is = Files.newInputStream(texturePath)) {
            Texture coneTexture = TextureIO.newTexture(is, true, "");

            GLUquadric cone = glu.gluNewQuadric();
            glu.gluQuadricDrawStyle(cone, GLU_FILL);
            glu.gluQuadricTexture(cone, true);
            glu.gluQuadricNormals(cone, GLU_SMOOTH);

            gl.glNewList(listId, GL_COMPILE);
            coneTexture.enable(gl);
            coneTexture.bind(gl);
            glu.gluCylinder(cone, 0f, radius, height, 25, 25);

            // bottom of cone
            gl.glPushMatrix();
            gl.glTranslatef(0f, 0f, height);
            glu.gluDisk(cone, 0, radius, 10, 10);
            gl.glPopMatrix();

            coneTexture.disable(gl);
            gl.glEndList();

            glu.gluDeleteQuadric(cone);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (GLException e) {
            e.printStackTrace();
        }
    }


    // GLEventListener implement

    /**
     * Called back by the animator to perform rendering.
     */
    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();  // get the OpenGL 2 graphics context
        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear color and depth buffers

        // draw cube & sphere
        gl.glPushMatrix();
        gl.glTranslatef(40f, 0f, 0f);

        gl.glColor3f(0f, 1f, 1f);
        glut.glutWireSphere(sphere_size, 15, 15); // R = (sqrt(3) * cube side) / 2 (диагональ куба / 2)

        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(m_cubeX, 0f, 0f);

        gl.glColor3f(1f, 0f, 0f);
        glut.glutWireCube(25f);//размер куба


        gl.glPopMatrix();




        // draw cylinder & cone
        gl.glPushMatrix();
        gl.glTranslatef(-50f, 30f, 0f);
        gl.glRotatef(m_cylinderConeRotY, 0f, 100f, 0f);
        gl.glRotatef(m_cylinderConeRotX, 5f, 0f, 0f);


        gl.glColor3f(1f, 1f, 1f);
        gl.glCallList(CYLINDER_LIST_ID);

        gl.glTranslatef(0f, 0f, 35f);
        gl.glCallList(CONE_LIST_ID);

        gl.glPopMatrix();



    }

    /**
     * Called back immediately after the OpenGL context is initialized. Can be used
     * to perform one-time initialization. Run only once.
     */
    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        glu = new GLU();
        glut = new GLUT();

        gl.glGenLists(2);
        prepareCylinderList(gl, 3f, 35f, CYLINDER_LIST_ID);
        prepareConeList(gl, 14f, 40f, CONE_LIST_ID);

        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        gl.glClearDepth(1.0f);

        gl.glEnable(GL_CULL_FACE);
        gl.glEnable(GL_DEPTH_TEST);
        gl.glShadeModel(GL_SMOOTH);
    }

    /**
     * Call-back handler for window re-size event. Also called when the drawable is
     * first set to visible.
     */
    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();  // get the OpenGL 2 graphics context

        if (height == 0) height = 1;   // prevent divide by zero
        float aspect = (float)width / height;

        // Set the view port (display area) to cover the entire window
        gl.glViewport(0, 0, width, height);

        // Setup perspective projection, with aspect ratio matches viewport
        gl.glMatrixMode(GL_PROJECTION);  // choose projection matrix
        gl.glLoadIdentity();             // reset projection matrix
        glu.gluPerspective(60.0, aspect, 1, 260.0); // fovy, aspect, zNear, zFar

        // Enable the model-view transform
        gl.glMatrixMode(GL_MODELVIEW);
        gl.glLoadIdentity(); // reset
        glu.gluLookAt(
                0.0f, 0.0f, 150.0f, // camera
                0.0f, 0.0f, 0.0f, // scene center
                0.0f, 1.0f, 0.0f // y
        );
    }


    /**
     * Called back before the OpenGL context is destroyed. Release resource such as buffers.
     */
    @Override
    public void dispose(GLAutoDrawable drawable) {

    }

    // KeyListener implements

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case 65: // A
                m_cubeX -=50f;
                break;

            case 68: // D
                m_cubeX +=50f;
                break;

            case 87: // W
                sphere_size *= 0.75f;

                break;

            case 83: // S
                sphere_size /= 0.75f;
                break;
        }

        //System.out.print(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}