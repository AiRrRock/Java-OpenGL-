import com.sun.javafx.geom.Vec3f;
import jogamp.nativewindow.windows.RGBQUAD;
import org.w3c.dom.css.RGBColor;

/**
 * Created by Александр on 04.04.2016.
 */
public class particle {
    // Координаты частицы
    public float xpos;//Координата по X
    public float ypos;// Координата по Y
    public float zpos;//Координата по Z
    // Время жизни частицы
    public float life;
    // Скорость угасания
    public float fade;
    // Скорость частицы по осям
    public float xspeed;// Скорость смещения по X
    public float zspeed;// Скорость смещения по Y
    public float yspeed;// Скорость смещения по Z
    // Размер частицы
    public float size;
    // Уменьшение частицы
    public float sizeDec;
    //Цвет частицы по RGB схеме
    public float red;   // Яркость красного
    public float green; // Яркость зеленого
    public float blue;  // Яркость синего
    //Расстояние от частицы до центра сферы
    public float toSphear_center;
    // Состояние активности частицы
    public boolean active = true;
    public Vec3f OldVector;
    //Конструктор частицы
    particle(float xpos,float ypos, float zpos,            /*Положение частицы                               */
             float life, float fade,                      /*Жизнь и скорость угасания                       */
             float xspeed, float yspeed,float zspeed,    /*Скорость перемещения по осям                    */
             float size, float sizeDec,                 /* Размер и величина на которую уменьшать размер  */
             float red, float green, float blue)       /*Цвет                                            */
    {

        this.xpos=xpos;
        this.ypos=ypos;
        this.zpos=zpos;

        this.xspeed=xspeed;
        this.yspeed=yspeed;
        this.zspeed=zspeed;

        this.size=size;
        this.sizeDec=sizeDec;

        this.fade=fade;
        this.life=life;

        this.red=red;
        this.green=green;
        this.blue=blue;

        this.toSphear_center =calculate_toSphearCenter(xpos,ypos,zpos);
    }
    // Набор методов для получения значений полей частицы
    // Координата по X
    public float getXpos(){
        return this.xpos;
    }
    // Координата по Y
    public float getYpos(){
        return this.ypos;
    }
    // Координата по Z
    public float getZpos(){
        return this.zpos;
    }
    // Скорость по X
    public float getXspeed(){
        return this.xspeed;
    }
    // Скорость по Y
    public float getYspeed(){
        return this.yspeed;
    }
    // Скорость по Z
    public float getZspeed(){
        return this.zspeed;
    }
    //Цвет частицы
    // Яркость красного
    public float getRed(){
        return this.red;
    }
    // Яркость зеленого
    public float getGreen(){
        return this.green;
    }
    // Яркость синего
    public float getBlue(){
        return this.blue;
    }
    // Размер частицы
    public float getSize(){
        return this.size;
    }
    // Величина на которую уменьшать размер частицы
    public float getSizeDec(){
        return this.sizeDec;
    }
    // Время жизни частицы
    public float getLife(){
        return this.life;
    }
    // Скорость угосания частицы
    public float getFade(){
        return this.fade;
    }
    // Расстояние до центра сферы
    public float getToSphear_center(){
        return this.toSphear_center;
    }
    // Набор методов для установки новых значений частицы
    //
    //
    public void setXpos(float xpos){
        this.xpos=xpos;
    }
    //
    public void setYpos(float ypos){
         this.ypos=ypos;
    }
    //
    public void setZpos(float zpos){
        this.zpos=zpos;
    }
    //
    public void setXspeed(float xspeed){
        this.xspeed=xspeed;
    }
    //
    public void setYspeed(float yspeed){
        this.yspeed=yspeed;
    }
    //
    public void setZspeed(float zspeed){
        this.zspeed=zspeed;
    }
    //
    //
    public void setRed(float red){
        this.red=red;
    }
    //
    public void setGreen(float green){
        this.green=green;
    }
    //
    public void setBlue(float blue){
        this.blue=blue;
    }
    //
    public void setSize(float size){
        this.size =size;
    }
    //
    public void setSizeDec(float sizeDec){
        this.sizeDec=sizeDec;
    }

    //
    public void setToSphear_center(Vec3f partPosition){
        this.toSphear_center= calculate_toSphearCenter(partPosition.x,partPosition.y,partPosition.z);
    }
    public void setToSphear_center(float xpos, float ypos, float zpos){
        this.toSphear_center= calculate_toSphearCenter(xpos,ypos,zpos);
    }
    //
    public void setLife(float life){
        this.life =life;
    }
    //
    public void setFade(float fade){
        this.fade=fade;
    }

    public void setActive(boolean isActive){
        this.active=isActive;
    }
    //
    public void Move(){
        this.xpos +=this.xspeed;
        this.ypos +=this.yspeed;
        this.zpos +=this.zspeed;
        this.life -=this.fade;
        this.toSphear_center =calculate_toSphearCenter(this.xpos,this.ypos,this.zpos);
    }
    //
    public boolean isDead(){
      return this.life<=0.0f ? true: false;
    }
    //
    public boolean isTooSmall(){
        return this.size<=0.0f ? true : false;
    }

    public boolean isTouchingSphear(float sphearRadius){
        return this.toSphear_center<=sphearRadius+0.5f ? true : false;
    }

    //Метод для расчета координат до центра сферы
    private static float calculate_toSphearCenter(float xpos, float ypos, float zpos){
        return (float)Math.sqrt(Math.pow(xpos,2.0)+Math.pow(ypos,2.0)+Math.pow(zpos,2.0));
    }
}
