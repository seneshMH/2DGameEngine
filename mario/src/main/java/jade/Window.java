package jade;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import renderer.DebugDraw;
import renderer.FrameBuffer;
import scenes.LevelEditorScene;
import scenes.LevelScene;
import scenes.Scene;

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

    private ImGuiLayer imGuiLayer;

    private FrameBuffer frameBuffer;

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
                break;
            }
            case 1:
            {
                currentScene = new LevelScene();
                break;
            }
            default:
            {
                assert false :"Unknown scene " + newScene + " !";
                break;
            }
        }

        currentScene.load();
        currentScene.init();
        currentScene.start();
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

        glfwSetWindowSizeCallback(glfwWindow,(w,newWidth,newHeight) -> {
            Window.setWidth(newWidth);
            Window.setHeight(newHeight);
        });


        //Make the OPENGL context current
        glfwMakeContextCurrent(glfwWindow);
        //Enable v-sync
        glfwSwapInterval(1);

        //Make window visible
        glfwShowWindow(glfwWindow);

        GL.createCapabilities();
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE,GL_ONE_MINUS_SRC_ALPHA);

        this.imGuiLayer = new ImGuiLayer(glfwWindow);
        this.imGuiLayer.initImGui();

        this.frameBuffer = new FrameBuffer(1920,1080);

        window.changeScene(0);

    }

    public void loop(){

        float beginTime = (float) glfwGetTime();
        float endTime;
        float dt = -1.0f;


        while (!glfwWindowShouldClose(glfwWindow)){
            //poll events
            glfwPollEvents();

            DebugDraw.beginFrame();

            glClearColor(1.0f,1.0f,1.0f,1.0f);
            glClear(GL_COLOR_BUFFER_BIT);

            this.frameBuffer.bind();

            if(dt >= 0) {
                DebugDraw.draw();
                currentScene.update(dt);
                glfwSetWindowTitle(glfwWindow, title + " FPS : " + Math.round(1.0f / dt));
            }

            this.frameBuffer.unBind();

            this.imGuiLayer.update(dt,currentScene);

            glfwSwapBuffers(glfwWindow);

            endTime = (float) glfwGetTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }
        currentScene.saveExit();
    }

    public static int getWidth(){
        return get().width;
    }

    public static int getHeight(){
        return get().height;
    }

    public static void setWidth(int newWidth){
        get().width =newWidth;
    }

    public static void setHeight(int newHeight) {
        get().height = newHeight;
    }


}
