package jade;

import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

public class MouseListener {
    private static MouseListener instance;

    private double scrollX,scrollY;
    private double xPos,yPos,lastY,lastX;
    private boolean mouseButtonPress[] = new boolean[9];
    private boolean isDragging;

    private MouseListener(){
        this.scrollX = 0.0;
        this.scrollY = 0.0;
        this.xPos = 0.0;
        this.yPos = 0.0;
        this.lastX = 0.0;
        this.lastY = 0.0;
    }

    public static MouseListener get(){
        if (MouseListener.instance == null){
            instance = new MouseListener();
        }

        return MouseListener.instance;
    }

    public static void mousePosCallback(long window,double xPos,double yPos){
        get().lastX = get().xPos;
        get().lastY = get().yPos;
        get().xPos =xPos;
        get().yPos = yPos;
        get().isDragging = get().mouseButtonPress[0] || get().mouseButtonPress[1] || get().mouseButtonPress[2];
    }

    public static void mouseButtonCallback(long window,int button,int action,int mods){

        if (action == GLFW_PRESS) {
            if (button < get().mouseButtonPress.length) {
                get().mouseButtonPress[button] = true;
            }
        }else {
            if (button < get().mouseButtonPress.length) {
                get().mouseButtonPress[button] = false;
                get().isDragging = false;
            }
        }
    }

    public static void mouseScrollCallback(long window,double xOffset,double yOffset){
        get().scrollX = xOffset;
        get().scrollY = yOffset;
    }

    public static void endFrame(){
        get().scrollX = 0;
        get().scrollY = 0;
        get().lastX = get().xPos;
        get().lastY = get().yPos;
    }

    public static float getX(){
        return (float) get().xPos;
    }

    public static float getY(){
        return (float) get().yPos;
    }

    public static float getOrthoX(){
        float currentX = getX();
        currentX = (currentX / (float) Window.getWidth()) * 2.0f - 1.0f;
        Vector4f tmp = new Vector4f(currentX,0,0,1);
        tmp.mul(Window.getScene().camera().getInverseProjection().mul(Window.getScene().camera().getInverseView()));
        currentX= tmp.x;

        return currentX;
    }

    public static float getOrthoY(){
        float currentY = Window.getHeight() -  getY();
        currentY = (currentY / (float) Window.getHeight()) * 2.0f - 1.0f;
        Vector4f tmp = new Vector4f(0,currentY,0,1);
        tmp.mul(Window.getScene().camera().getInverseProjection().mul(Window.getScene().camera().getInverseView()));
        currentY= tmp.y;

        return currentY;
    }

    public static float getDX(){
        return (float) (get().lastX - get().xPos);
    }

    public static float getDY(){
        return (float) (get().lastY - get().yPos);
    }

    public static float getScrollX(){
        return (float) get().scrollX;
    }

    public static float getScrollY(){
        return (float) get().scrollY;
    }

    public static boolean isDragging(){
        return get().isDragging;
    }

    public static boolean mouseButtonDown(int button){
        if (button < get().mouseButtonPress.length) {
            return get().mouseButtonPress[button];
        }else {
            return false;
        }
    }
}
