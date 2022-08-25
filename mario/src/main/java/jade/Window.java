package jade;

import observers.EventSystem;
import observers.Observer;
import observers.events.Event;
import observers.events.EventType;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import renderer.*;
import scenes.LevelEditorSceneInitializer;
import scenes.Scene;
import scenes.SceneInitializer;
import util.AssetPool;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window implements Observer {
    private int width,height;
    private String title;
    private static Window window = null;
    private static Scene currentScene;
    private long glfwWindow;
    private ImGuiLayer imGuiLayer;
    private FrameBuffer frameBuffer;
    private PickingTexture pickingTexture;
    private boolean isRuntimePlay = false;

    private Window(){
        this.width  =1920;
        this.height = 1080;
        this.title = "Engine";
        EventSystem.addObserver(this);
    }

    public static void changeScene(SceneInitializer sceneInitializer){
        if(currentScene != null){
            currentScene.destroy();
        }

        getImGuiLayer().getPropertiesWindow().setActiveGameObject(null);
        currentScene = new Scene(sceneInitializer);
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

        this.frameBuffer = new FrameBuffer(1920,1080);
        this.pickingTexture = new PickingTexture(1920,1080);
        glViewport(0,0,1920,1080);

        this.imGuiLayer = new ImGuiLayer(glfwWindow,pickingTexture);
        this.imGuiLayer.initImGui();

        window.changeScene(new LevelEditorSceneInitializer());

    }



    public void loop(){

        float beginTime = (float) glfwGetTime();
        float endTime;
        float dt = -1.0f;

        Shader defaultShader = AssetPool.getShader("assets/shaders/default.glsl");
        Shader pickingShader = AssetPool.getShader("assets/shaders/pickingShader.glsl");

        while (!glfwWindowShouldClose(glfwWindow)){
            //poll events
            glfwPollEvents();

            //Render pass 1 : Render to picking texture
            glDisable(GL_BLEND);
            pickingTexture.enableWriting();

            glViewport(0,0,1920,1080);
            glClearColor(0.0f,0.0f,0.0f,0.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            Renderer.bindShader(pickingShader);
            currentScene.render();

            pickingTexture.disableWriting();
            glEnable(GL_BLEND);

            //Render pass 2 : Render actual game

            DebugDraw.beginFrame();

            this.frameBuffer.bind();

            glClearColor(1.0f,1.0f,1.0f,1.0f);
            glClear(GL_COLOR_BUFFER_BIT);



            if(dt >= 0) {
                DebugDraw.draw();
                Renderer.bindShader(defaultShader);
                if (isRuntimePlay) {
                    currentScene.update(dt);
                }else {
                    currentScene.editorUpdate(dt);
                }
                currentScene.render();
                glfwSetWindowTitle(glfwWindow, title + " FPS : " + Math.round(1.0f / dt));
            }

            this.frameBuffer.unBind();

            this.imGuiLayer.update(dt,currentScene);

            glfwSwapBuffers(glfwWindow);

            MouseListener.endFrame();

            endTime = (float) glfwGetTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }

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


    public static FrameBuffer getFrameBuffer() {
        return get().frameBuffer;
    }

    public static float getTargetAspectRatio(){
        return 10.0f/9.0f;
    }

    public static ImGuiLayer getImGuiLayer(){
        return get().imGuiLayer;
    }

    @Override
    public void onNotify(GameObject object, Event event) {
        switch (event.type){
            case GameEngineStartPlay:
                this.isRuntimePlay = true;
                currentScene.save();
                Window.changeScene(new LevelEditorSceneInitializer());
                break;
            case GameEngineStopPlay:
                this.isRuntimePlay = false;
                Window.changeScene(new LevelEditorSceneInitializer());
                break;
            case LoadLevel:
                Window.changeScene(new LevelEditorSceneInitializer());
                break;
            case SaveLevel:
                currentScene.save();
                break;
        }

    }
}
