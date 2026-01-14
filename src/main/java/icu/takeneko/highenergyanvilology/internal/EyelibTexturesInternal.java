package icu.takeneko.highenergyanvilology.internal;

import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL32;

public class EyelibTexturesInternal {

    private static int programId = 0;

    // language=glsl
    public static final String VERTEX_SHADER =
        """
            #version 320 core
            
            uniform mat4 ProjMat;
            
            in vec2 Position;
            
            out vec2 fragPosition;
            
            void main() {
                vec4 pos = vec4(Position.xy, 0.0, 0.0) * ProjMat;
                gl_Position = pos;
                fragPosition = pos.xy;
            }
            
            """;

    // language=glsl
    public static final String FRAGMENT_SHADER =
        """
                 #version 320 core
                 uniform sampler2D Textures[16];
                 uniform int TextureCount;
                 uniform ivec2 ImageSize;
            
                 in vec2 fragPosition;
            
                 out vec4 fragColor;
            
                 void main() {
                     ivec2 pixelCoords = ivec2(gl_GlobalInvocationID.xy);
                     if (pixelCoords.x >= ImageSize.x || pixelCoords.y >= ImageSize.y) {
                         return;
                     }
                     vec2 uv = vec2(pixelCoords) / vec2(ImageSize);
                     vec4 finalColor = vec4(0.0, 0.0, 0.0, 0.0);
                     for (int i = 0; i < TextureCount; ++i) {
                         vec4 layerColor = texture(Textures[i], uv);
                         finalColor = mix(finalColor, layerColor, layerColor.a);
                         finalColor.a = layerColor.a + finalColor.a * (1.0 - layerColor.a);
                     }
                     fragColor = finalColor;
                     //imageStore(u_OutputImage, pixelCoords, finalColor);
                 }
            \s""";

    public static void setupShaders() {
        int vshId = GL32.glCreateShader(GL32.GL_VERTEX_SHADER);
        int fshId = GL32.glCreateShader(GL32.GL_FRAGMENT_SHADER);

        GL32.glShaderSource(vshId, VERTEX_SHADER);
        GL32.glShaderSource(fshId, FRAGMENT_SHADER);
        GL32.glCompileShader(vshId);
        validateShaderStatus(vshId);
        GL32.glCompileShader(fshId);
        validateShaderStatus(fshId);

        int program = GL32.glCreateProgram();
        GL32.glCompileShader(vshId);
        GL32.glAttachShader(program, vshId);
        GL32.glAttachShader(program, fshId);
        GL32.glLinkProgram(program);

        GL32.glDeleteShader(vshId);
        GL32.glDeleteShader(fshId);

        if (GL20.glGetProgrami(program, GL20.GL_LINK_STATUS) == 0) {
            String log = GL20.glGetProgramInfoLog(program, 512);
            throw new IllegalStateException("Failed to link program: " + log);
        }

        GL32.glUseProgram(program);

        GL32.glUseProgram(0);

        programId = 0;
    }

    public static void validateShaderStatus(int shader) {
        if (GL20.glGetShaderi(shader, GL20.GL_COMPILE_STATUS) == 0) {
            String log = GL20.glGetShaderInfoLog(shader, 512);
            throw new IllegalStateException("Failed to compile shader: " + log);
        }
    }
}
