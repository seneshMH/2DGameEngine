package jade;

import org.lwjgl.BufferUtils;
import renderer.Shader;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class LevelEditorScene extends Scene{
    
    private Shader defaultShader;

    private float[] vertexArray = {
        //position          //color
        0.5f,-0.5f,0.0f,     1.0f,0.0f,0.0f,1.0f,
       -0.5f,0.5f,0.0f,      0.0f,1.0f,0.0f,1.0f,
        0.5f,0.5f,0.0f,      0.0f,0.0f,1.0f,1.0f,
       -0.5f,-0.5f,0.0f,     1.0f,1.0f,0.0f,1.0f
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
        defaultShader = new Shader("assets/shaders/default.glsl");
        defaultShader.compile();

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
        int floatSizeBytes = 4;
        int vertexSizeBytes = (positionSize + colorSize) * floatSizeBytes;

        glVertexAttribPointer(0,positionSize,GL_FLOAT,false,vertexSizeBytes,0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1,colorSize,GL_FLOAT,false,vertexSizeBytes,positionSize * floatSizeBytes);
        glEnableVertexAttribArray(1);
    }

    @Override
    public void update(float dt) {
        //System.out.println(" " + (1.0f / dt) + "FPS");

        //Bind Shader program
        defaultShader.use();

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
