package jade;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector4f;

import java.util.Arrays;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

public class MouseListener {
    private static MouseListener instance;

    private double scrollX,scrollY;
    private double xPos, yPos, lastY, lastX, worldX, worldY, lastWorldX, lastWorldY;

    private boolean mouseButtonPress[] = new boolean[9];
    private boolean isDragging;
    private int mouseButtonDown = 0;

    private Vector2f gameViewportPos = new Vector2f();
    private Vector2f gameViewportSize = new Vector2f();

    private MouseListener(){
        this.scrollX = 0.0;
        this.scrollY = 0.0;
        this.xPos = 0.0;
        this.yPos = 0.0;
        this.lastX = 0.0;
        this.lastY = 0.0;
    }


    public static void clear(){
        get().scrollX = 0.0f;
        get().scrollY = 0.0f;
        get().xPos = 0.0;
        get().yPos = 0.0;
        get().lastX = 0.0;
        get().lastY = 0.0;
        get().mouseButtonDown = 0;
        get().isDragging = false;
        Arrays.fill(get().mouseButtonPress,false);
    }

    public static MouseListener get(){
        if (MouseListener.instance == null){
            instance = new MouseListener();
        }

        return MouseListener.instance;
    }

    public static void mousePosCallback(long window,double xPos,double yPos){
        if(!Window.getImGuiLayer().getGameViewWindow().getWantCaptureMouse()){
            clear();
        }

        if(get().mouseButtonDown > 0){
            get().isDragging= true;
        }

        get().xPos =xPos;
        get().yPos = yPos;
    }

    public static void mouseButtonCallback(long window,int button,int action,int mods){

        if (action == GLFW_PRESS) {
            get().mouseButtonDown ++;
            if (button < get().mouseButtonPress.length) {
                get().mouseButtonPress[button] = true;
            }
        }else {
            get().mouseButtonDown --;
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
    }

    public static float getX(){
        return (float) get().xPos;
    }

    public static float getY(){
        return (float) get().yPos;
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

    public static Vector2f screenToWorld(Vector2f screenCoords){
        Vector2f normalizeScreenCords = new Vector2f(
                screenCoords.x / Window.getWidth(),
                screenCoords.y / Window.getHeight()
        );

        normalizeScreenCords.mul(2.0f).sub(new Vector2f(1.0f));
        Camera camera = Window.getScene().camera();
        Vector4f tmp = new Vector4f(
               normalizeScreenCords.x,normalizeScreenCords.y,0,1
        );

        Matrix4f inverseView = new Matrix4f(camera.getInverseView());
        Matrix4f inverseProjection = new Matrix4f(camera.getInverseProjection());

        tmp.mul(inverseView.mul(inverseProjection));

        return new Vector2f(tmp.x,tmp.y);
    }

    public static Vector2f worldToScreen(Vector2f worldCoords){
        Camera camera = Window.getScene().camera();
        Vector4f ndcSpacePos = new Vector4f(
                worldCoords.x,worldCoords.y,0,1
        );

        Matrix4f view = new Matrix4f(camera.getViewMatrix());
        Matrix4f projection = new Matrix4f(camera.getProjectionMatrix());

        ndcSpacePos.mul(projection.mul(view));


        Vector2f windowSpace = new Vector2f(ndcSpacePos.x,ndcSpacePos.y).mul(1.0f/ndcSpacePos.w);
        windowSpace.add(new Vector2f(1.0f,1.0f).mul(0.5f));
        windowSpace.mul(new Vector2f(Window.getWidth(),Window.getHeight()));

        return windowSpace;
    }

    public static float getScreenX(){
        return getScreen().x;
    }

    public static float getScreenY(){
        return getScreen().y;
    }

    public static Vector2f getScreen(){
        float currentX = getX() - get().gameViewportPos.x;
        currentX = (currentX / get().gameViewportSize.x)  * 1920.0f;

        float currentY = getY() - get().gameViewportPos.y;
        currentY = 1080.0f -((currentY / get().gameViewportSize.y) * 1080.0f);

       return new Vector2f(currentX,currentY);
    }

    public static float getWorldX(){
        return getWorld().x;
    }

    public static float getWorldY(){
        return getWorld().y;
    }

    public static Vector2f getWorld(){
        float currentX = getX() - get().gameViewportPos.x;
        currentX = (currentX / get().gameViewportSize.x) * 2.0f - 1.0f;
        float currentY = getY() - get().gameViewportPos.y;
        currentY = -((currentY / get().gameViewportSize.y) * 2.0f - 1.0f);

        Vector4f tmp = new Vector4f(currentX,currentY,0,1);

        Camera camera = Window.getScene().camera();

        Matrix4f inverseView = new Matrix4f(camera.getInverseView());
        Matrix4f inverseProjection = new Matrix4f(camera.getInverseProjection());

        tmp.mul(inverseView.mul(inverseProjection));

        return new Vector2f(tmp.x,tmp.y);
    }

    public static void setGameViewportPos(Vector2f gameViewportPos) {
        get().gameViewportPos.set(gameViewportPos);
    }

    public static void setGameViewportSize(Vector2f gameViewportSize) {
        get().gameViewportSize.set(gameViewportSize);
    }
}
