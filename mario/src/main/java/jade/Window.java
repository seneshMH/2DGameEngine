package jade;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import util.Time;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private int width,height;
    private String title;

    private static Window window = null;


    private static Scene currentScene;

    private long glfwWindow;

    private Window(){
        this.width  =1920;
        this.height = 1080;
        this.title = "Mario";
    }

    public static void changeScene(int newScene){
        switch (newScene){
            case 0:
            {
               currentScene = new LevelEditorScene();
               currentScene.init();
               currentScene.start();
                break;
            }
            case 1:
            {
                currentScene = new LevelScene();
                currentScene.init();
                currentScene.start();
                break;
            }
            default:
            {
                assert false :"Unknown scene " + newScene + " !";
                break;
            }
        }
    }

    public static Window get(){
        if(Window.window == null){
            Window.window = new Window();
        }

        return Window.window;
    }

    public static Scene getScene(){
        return get().currentScene;
    }

    public void run(){
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        loop();

        //Free the memory
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        //Terminate GLFW and the free the callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void init(){

        //Error call back
        //System.err.println("Error");
        GLFWErrorCallback.createPrint(System.err).set();

        //Init GLFW
        if(!glfwInit()){
            throw new IllegalStateException("Unable to init GLFW");

        }

        //Config GLFW
        glfwDefaultWindowHints();

        glfwWindowHint(GLFW_VISIBLE,GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE,GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED,GLFW_TRUE);

        //create window
        glfwWindow = glfwCreateWindow(this.width,this.height,this.title,NULL,NULL);

        if(glfwWindow == NULL){
            throw new IllegalStateException("Unable to create GLFW window");
        }

        glfwSetCursorPosCallback(glfwWindow,MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow,MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow,MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow,KeyListener::keyCallback);


        //Make the OPENGL context current
        glfwMakeContextCurrent(glfwWindow);
        //Enable v-sync
        glfwSwapInterval(1);

        //Make window visible
        glfwShowWindow(glfwWindow);

        GL.createCapabilities();

        window.changeScene(0);

    }

    public void loop(){

        float beginTime = (float) glfwGetTime();
        float endTime;
        float dt = -1.0f;

        while (!glfwWindowShouldClose(glfwWindow)){
            //poll events
            glfwPollEvents();

            glClearColor(1.0f,1.0f,1.0f,1.0f);
            glClear(GL_COLOR_BUFFER_BIT);

            if(dt >= 0)
                currentScene.update(dt);
                glfwSetWindowTitle(glfwWindow,title + " FPS : " + Math.round(1.0f / dt));

            glfwSwapBuffers(glfwWindow);

            endTime = (float) glfwGetTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }
    }
}
