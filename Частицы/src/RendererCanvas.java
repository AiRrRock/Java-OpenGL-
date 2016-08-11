import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;
import com.sun.javafx.geom.Vec3f;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import static com.jogamp.opengl.GL.*;
import static com.jogamp.opengl.GL2.*;

public class RendererCanvas extends GLCanvas implements GLEventListener, KeyListener {
    private GLU glu;
    private GLUT glut;
    private float sphere_size = 500.0f;
    private boolean light_flag=true, flag0=false,flag1=false,flag2=false;
    private float[] light_position = {1.0f, 1.0f, 1.0f, 0.0f};
    private float[] light_ambient = { 0.f, 0.0f, 0.0f, 1.0f };
    private float[] light_diffuse = { 1.0f, 1.0f, 1.0f, 5.0f };
    private float[] light_specular = { 1.0f, 1.0f, 1.0f, 1.0f };
    private boolean isSimpleCollision=true;
    private int PARTICLES =2000;
    private particle[] partS =new particle[PARTICLES];
    private ArrayList<Vec3f> sphereCoords;
    private float mat_dif[]={.8f,.8f,1f,1f};
    private float mat_amb[] = {1.f, 1.f, 0.7f,1f};
    private float mat_spec[] = {0.0f, 0.0f, 0.0f,0f};
    private float shininess = 0.0f ;
    private float xangle,yangle,zangle;
    private float eyeX=1.0f,eyeY=1.0f,eyeZ=1000.0f,centerX=0.0f,centerY=0.0f, centerZ=0.0f, upX=0.0f, upY=1.0f, upZ=0.0f;
    private int Way = 0;
    private Vec3f EmmiterCoords=new Vec3f(750,700,0);
    public RendererCanvas() {

        this.addGLEventListener(this);
        this.addKeyListener(this);
    }

    private void changeSphear(){
        sphere_size = sphere_size < 700 ? sphere_size+100: 100;
    }

    private boolean changeFlag(boolean flag){
        return flag ? false : true;
    }
    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        GL2 gl4 = drawable.getGL().getGL2();
        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        gl.glPushMatrix();

        gl.glMaterialfv (GL_FRONT_AND_BACK,GL_AMBIENT, FloatBuffer.wrap(mat_amb));
        gl.glMaterialfv (GL_FRONT_AND_BACK,GL_DIFFUSE, FloatBuffer.wrap(mat_dif));
        gl.glMaterialfv (GL_FRONT_AND_BACK,GL_SPECULAR, FloatBuffer.wrap(mat_spec));
        gl.glMaterialf  (GL_FRONT,GL_SHININESS, shininess);

        gl.glRotatef (xangle, .5f, 0.f, 0.f);
        gl.glRotatef (yangle, 0.f, .5f, 0.f);
        gl.glRotatef (zangle, 0.f, 0.f, .5f );


        gl.glTranslatef(0, 0, 0f);

        gl.glColor3f(1f,1f,1f);
        glut.glutSolidSphere(sphere_size,400, 400);
        gl.glPopMatrix();
        gl.glPushMatrix();
        gl.glRotatef (xangle, .5f, 0.f, 0.f);
        gl.glRotatef (yangle, 0.f, .5f, 0.f);
        gl.glRotatef (zangle, 0.f, 0.f, .5f );

        gl.glColor3f(1f,1f,1f);
        gl.glShadeModel(GL_SMOOTH);
        gl.glClearColor(0.0f,0.0f,0.0f,0.0f);
        gl.glClearDepth(1.0f);
        gl.glDisable(GL_DEPTH_TEST);
        gl.glEnable(GL_BLEND);
        gl.glBlendFunc(GL_SRC_ALPHA,GL_ONE);
        gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT,GL_NICEST);
        gl.glHint(GL_POINT_SMOOTH_HINT,GL_NICEST);


        for (int loop=0;loop<PARTICLES;loop++)
        {

            if (partS[loop].active)
            {

                if(!light_flag)gl.glColor4f(partS[loop].getRed(), partS[loop].getGreen(), partS[loop].getBlue(),partS[loop].getLife());
                partS[loop].setSize(partS[loop].getSize()-partS[loop].getSizeDec()) ;

                    gl.glBegin(GL_TRIANGLE_STRIP);
                    gl.glTexCoord2d(1, 1);
                    gl.glVertex3f(partS[loop].getXpos() + partS[loop].getSize(),
                            partS[loop].getYpos() + partS[loop].getSize(),
                            partS[loop].getZpos());
                    gl.glTexCoord2d(0, 1);
                    gl.glVertex3f(partS[loop].getXpos() - partS[loop].getSize(),
                            partS[loop].getYpos() + partS[loop].getSize(),
                            partS[loop].getZpos());
                    gl.glTexCoord2d(1, 0);
                    gl.glVertex3f(partS[loop].getXpos() + partS[loop].getSize(),
                            partS[loop].getYpos() - partS[loop].getSize(),
                            partS[loop].getZpos());
                    gl.glTexCoord2d(0, 0);
                    gl.glVertex3f(partS[loop].getXpos() - partS[loop].getSize(),
                            partS[loop].getYpos() - partS[loop].getSize(),
                            partS[loop].getZpos());
                    gl.glEnd();

                    partS[loop].Move();

                if (partS[loop].isDead())
                {
                    partS[loop].setLife(1.0f);
                    partS[loop].setFade((float) Math.random()/100);
                    partS[loop].setXpos(EmmiterCoords.x);
                    partS[loop].setYpos(EmmiterCoords.y);
                    partS[loop].setZpos(EmmiterCoords.z);
                    partS[loop].setRed((float) Math.random());
                    partS[loop].setGreen((float) Math.random());
                    partS[loop].setBlue((float) Math.random());
                    switch (Way) {
                        case 0:    partS[loop].setXspeed(((float) Math.random() - (float) Math.random() - (float) Math.random() + (float) Math.random()) * 10);
                                   partS[loop].setYspeed(((float) Math.random() - (float) Math.random() - (float) Math.random() + (float) Math.random()) * 10);
                                   partS[loop].setZspeed(((float) Math.random() - (float) Math.random() - (float) Math.random() + (float) Math.random()) * 10);
                                   break;
                        case 1:    partS[loop].setXspeed(((float) Math.random() - (float) Math.random() - (float) Math.random() + (float) Math.random()) * 10);
                                   partS[loop].setYspeed(((float) Math.random() + (float) Math.random()) * 10);
                                   partS[loop].setZspeed(((float) Math.random() - (float) Math.random() - (float) Math.random() + (float) Math.random()) * 10);
                                   break;
                        case 2:    partS[loop].setXspeed(((float) Math.random() + (float) Math.random()) * 10);
                                   partS[loop].setYspeed(((float) Math.random() - (float) Math.random() - (float) Math.random() + (float) Math.random()) * 10);
                                   partS[loop].setZspeed(((float) Math.random() - (float) Math.random() - (float) Math.random() + (float) Math.random()) * 10);
                                   break;
                        case 3:    partS[loop].setXspeed(((float) Math.random() - (float) Math.random() - (float) Math.random() + (float) Math.random()) * 10);
                                   partS[loop].setYspeed(((float) - Math.random() - (float) Math.random()) * 10);
                                   partS[loop].setZspeed(((float) Math.random() - (float) Math.random() - (float) Math.random() + (float) Math.random()) * 10);
                                   break;
                        case 4:    partS[loop].setXspeed(((float) - Math.random() - (float) Math.random()) * 10);
                                   partS[loop].setYspeed(((float) Math.random() - (float) Math.random() - (float) Math.random() + (float) Math.random()) * 10);
                                   partS[loop].setZspeed(((float) Math.random() - (float) Math.random() - (float) Math.random() + (float) Math.random()) * 10);
                                   break;
                    }
                    partS[loop].setSize(10f);
                    partS[loop].setSizeDec((float) Math.random() / 100);

                }
                if (partS[loop].isTooSmall()){
                    partS[loop].setLife(1.0f);
                    partS[loop].setFade((float) Math.random()/100);
                    partS[loop].setXpos(EmmiterCoords.x);
                    partS[loop].setYpos(EmmiterCoords.y);
                    partS[loop].setZpos(EmmiterCoords.z);
                    partS[loop].setSize(10f);
                    partS[loop].setSizeDec((float) Math.random()/100);

                    switch (Way) {
                        case 0:    partS[loop].setXspeed(((float) Math.random() - (float) Math.random() - (float) Math.random() + (float) Math.random()) * 10);
                            partS[loop].setYspeed(((float) Math.random() - (float) Math.random() - (float) Math.random() + (float) Math.random()) * 10);
                            partS[loop].setZspeed(((float) Math.random() - (float) Math.random() - (float) Math.random() + (float) Math.random()) * 10);
                            break;
                        case 1:    partS[loop].setXspeed(((float) Math.random() - (float) Math.random() - (float) Math.random() + (float) Math.random()) * 10);
                            partS[loop].setYspeed(((float) Math.random() + (float) Math.random()) * 10);
                            partS[loop].setZspeed(((float) Math.random() - (float) Math.random() - (float) Math.random() + (float) Math.random()) * 10);
                            break;
                        case 2:    partS[loop].setXspeed(((float) Math.random() + (float) Math.random()) * 10);
                            partS[loop].setYspeed(((float) Math.random() - (float) Math.random() - (float) Math.random() + (float) Math.random()) * 10);
                            partS[loop].setZspeed(((float) Math.random() - (float) Math.random() - (float) Math.random() + (float) Math.random()) * 10);
                            break;
                        case 3:    partS[loop].setXspeed(((float) Math.random() - (float) Math.random() - (float) Math.random() + (float) Math.random()) * 10);
                            partS[loop].setYspeed(((float) - Math.random() - (float) Math.random()) * 10);
                            partS[loop].setZspeed(((float) Math.random() - (float) Math.random() - (float) Math.random() + (float) Math.random()) * 10);
                            break;
                        case 4:    partS[loop].setXspeed(((float) - Math.random() - (float) Math.random()) * 10);
                            partS[loop].setYspeed(((float) Math.random() - (float) Math.random() - (float) Math.random() + (float) Math.random()) * 10);
                            partS[loop].setZspeed(((float) Math.random() - (float) Math.random() - (float) Math.random() + (float) Math.random()) * 10);
                            break;
                    }
                }
                if (partS[loop].isTouchingSphear(sphere_size)){
                    if (isSimpleCollision){
                        partS[loop].setXspeed(-partS[loop].getXspeed());
                        partS[loop].setYspeed(-partS[loop].getYspeed());
                        partS[loop].setZspeed(-partS[loop].getZspeed());

                          }else{
                          Vec3f normal = new Vec3f(partS[loop].getXpos(),partS[loop].getYpos(),partS[loop].getZpos());
                          Vec3f speed =  new Vec3f(partS[loop].getXspeed(),
                                  partS[loop].getYspeed(),
                                  partS[loop].getZspeed());
                          normal.normalize(); speed.normalize();float multiply;
                          multiply =  normal.dot(speed);
                          multiply = -2.0f*multiply;
                          normal.mul(multiply);
                          speed.add(normal);
                          speed.normalize();
                          partS[loop].setXspeed(speed.x*10);
                          partS[loop].setYspeed(speed.y*10);
                          partS[loop].setZspeed(speed.z*10);
                          }
                }
            }

        gl.glPopMatrix();
   /* устанавливаем параметры источника света */
        gl.glLightfv (GL_LIGHT0, GL_AMBIENT, FloatBuffer.wrap(light_ambient));
        gl.glLightfv (GL_LIGHT0, GL_DIFFUSE, FloatBuffer.wrap(light_diffuse));
        gl.glLightfv (GL_LIGHT0, GL_SPECULAR, FloatBuffer.wrap(light_specular));
        gl.glLightfv (GL_LIGHT0, GL_POSITION, FloatBuffer.wrap(light_position));

   /* включаем освещение и источник света */
        if (light_flag) { gl.glEnable (GL_LIGHTING);} else {gl.glDisable(GL_LIGHTING);}
        gl.glEnable (GL_LIGHT0);
        gl.glEnable(GL_DEPTH_TEST);
        gl.glPushMatrix();
        gl.glPopMatrix();

    }}

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        glu = new GLU();
        glut = new GLUT();

        gl.glGenLists(2);

        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        gl.glClearDepth(1.0f);

        gl.glEnable(GL_CULL_FACE);
        gl.glEnable(GL_DEPTH_TEST);
        gl.glShadeModel(GL_SMOOTH);
        gl.glPushMatrix();
        for (int i=0; i< PARTICLES; i++){


            partS[i] = new particle(EmmiterCoords.x,EmmiterCoords.y,EmmiterCoords.z,1.0f,0.02f,0,0,0,10,0,1,1,1);
            partS[i].setXpos(EmmiterCoords.x);
            partS[i].setYpos(EmmiterCoords.y);
            partS[i].setZpos(EmmiterCoords.z);
            partS[i].setRed((float)Math.random());
            partS[i].setGreen((float)Math.random());
            partS[i].setBlue((float)Math.random());
            partS[i].setXspeed(((float)Math.random()-(float)Math.random()-(float)Math.random()+(float)Math.random())*10);
            partS[i].setYspeed(((float)Math.random()-(float)Math.random()-(float)Math.random()+(float)Math.random())*10);
            partS[i].setZspeed(((float)Math.random()-(float)Math.random()-(float)Math.random()+(float)Math.random())*10);
            partS[i].setSizeDec((float) Math.random());
        }


        gl.glPopMatrix();

    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();

        if (height == 0) height = 1;
        float aspect = (float)width / height;

        gl.glViewport(0, 0, width, height);

        gl.glMatrixMode(GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(100.0, aspect, 10, 1000.0);


        gl.glMatrixMode(GL_MODELVIEW);
        gl.glLoadIdentity();
        glu.gluLookAt(
                eyeX,eyeY,eyeZ,
                centerX,centerY, centerZ,
                upX, upY, upZ

        );
    }


    @Override
    public void dispose(GLAutoDrawable drawable) {

    }


    @Override
    public void keyTyped(KeyEvent e) {


    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            //Свойства света
            //Смена настроек света
            case 82: //R
                light_ambient[0] = light_ambient[0]>=1.0f ? 0 : light_ambient[0]+0.1f ;
                break;
            case 71: //G
                light_ambient[1] = light_ambient[1]>=1.0f ? 0 : light_ambient[1]+0.1f ;
                break;
            case 66: // B
                light_ambient[2] = light_ambient[2]>=1.0f ? 0 : light_ambient[2]+0.1f ;
                break;

            //Свет по X
            case 65://A
                light_position[0] += 0.1f;
                break;
            case 90: // Z
                light_position[0] -= 0.1f;
                break;

            //Свет по Y
            case 83: //S
                light_position[1] += 0.1f;
                break;
            case 88: // X
                light_position[1] -= 0.1f;
                break;
            //Свет по Z
            case 68: //D
                light_position[2] += 0.1f;
                break;
            case 67://C
                light_position[2] -= 0.1f;
                break;

            //Изменение размера сферы
            case 84: //T
                changeSphear();
                break;

            //Включение отключение света
            case 76: //L
                light_flag = light_flag ? false :true;
                break;

            case 86:// V
                isSimpleCollision = isSimpleCollision ? false: true;
                break;

            case 39: //Right_arrow
                Way =2;
                break;
            case 37: // Left_arrow
                Way =4;
                break;
            case 38: // Up_arrow
                Way=1;
                break;
            case 40: // Down_arrow
                Way=3;
                 break;
            case 35://END
                Way = 0;
            case 27://ESC
                System.exit(0);
                break;
            case 18:
        //        anim
        }
        System.out.println(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}