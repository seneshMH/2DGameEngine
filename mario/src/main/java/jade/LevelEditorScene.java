package jade;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import renderer.Shader;
import renderer.Texture;
import util.Time;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class LevelEditorScene extends Scene{
    
    private Shader defaultShader;
    private Texture testTexture;

    private float[] vertexArray = {
        //position          //color                   //UV
        100f,0.0f,0.0f,     1.0f,0.0f,0.0f,1.0f,     1,1,
        0.0f,100f,0.0f,      0.0f,1.0f,0.0f,1.0f,     0,0,
        100f,100f,0.0f,      0.0f,0.0f,1.0f,1.0f,     1,0,
        0f,0f,0.0f,          1.0f,1.0f,0.0f,1.0f,     0,1
    };

    private int[] elementArray = {
        2,1,0,
        0,1,3
    };

    private int vaoID,vboID,eboID;

    public LevelEditorScene(){

    }

    @Override
    public void init(){
        this.camera = new Camera(new Vector2f());

        defaultShader = new Shader("assets/shaders/default.glsl");
        defaultShader.compile();
        this.testTexture = new Texture("assets/images/testImage.png");

        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        //Create a float buffer object
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        //create VBO upload the vertex buffer
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER,vboID);
        glBufferData(GL_ARRAY_BUFFER,vertexBuffer,GL_STATIC_DRAW);

        //create indices and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER,elementBuffer,GL_STATIC_DRAW);

        //Add the vertex attribute pointers
        int positionSize = 3;
        int colorSize = 4;
        int uvSize = 2;
        int vertexSizeBytes = (positionSize + colorSize + uvSize) * Float.BYTES;

        glVertexAttribPointer(0,positionSize,GL_FLOAT,false,vertexSizeBytes,0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1,colorSize,GL_FLOAT,false,vertexSizeBytes,positionSize * Float.BYTES);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2,uvSize,GL_FLOAT,false,vertexSizeBytes,(positionSize + colorSize) * Float.BYTES);
        glEnableVertexAttribArray(2);
    }

    @Override
    public void update(float dt) {
        //System.out.println(" " + (1.0f / dt) + "FPS");

        //Bind Shader program
        defaultShader.use();

        defaultShader.uploadTexture("TEX_SAMPLER",0);
        glActiveTexture(0);
        testTexture.bind();

        defaultShader.uploadMat4f("uProjection",camera.getProjectionMatrix());
        defaultShader.uploadMat4f("uView",camera.getViewMatrix());

        defaultShader.uploadFlot("uTime", Time.getTime());

        //bind vao
        glBindVertexArray(vaoID);

        //Enable the vertex attrib pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES,elementArray.length,GL_UNSIGNED_INT,0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);

        defaultShader.detach();
    }


}
