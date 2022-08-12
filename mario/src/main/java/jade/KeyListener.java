package jade;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyListener {
    private static KeyListener instance;
    private boolean keyPress[] = new boolean[350];

    private KeyListener(){

    }

    public static KeyListener get(){
        if(KeyListener.instance == null){
            KeyListener.instance = new KeyListener();
        }

        return KeyListener.instance;
    }

    public static void keyCallback(long window,int key,int scanCode,int action,int mods){
        if (action == GLFW_PRESS){
            get().keyPress[key] = true;
        }else if (action == GLFW_RELEASE){
            get().keyPress[key] = false;
        }
    }

    public static boolean isKeyPressed(int keyCode){
            return get().keyPress[keyCode];
    }

}
