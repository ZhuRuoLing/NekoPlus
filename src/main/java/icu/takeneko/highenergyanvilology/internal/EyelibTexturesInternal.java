package icu.takeneko.highenergyanvilology.internal;

public class EyelibTexturesInternal {

    // language=glsl
    public static final String VERTEX_SHADER =
    """
    #version 320 core
    
    uniform mat4 ProjMat;
    
    in vec2 Position;
    
    void main() {
        gl_Position = vec4(Position.xy, 0.0, 0.0) * ProjMat;
    }
    
    """;

    // language=glsl
    public static final String FRAGMENT_SHADER =
        """
            #version 320 core
            uniform sampler2D u_Textures[16];
            uniform int u_TextureCount;

            out vec4 fragColor;

            void main() {
                ivec2 pixelCoords = ivec2(gl_GlobalInvocationID.xy);
                ivec2 size = imageSize(u_OutputImage);
                if (pixelCoords.x >= size.x || pixelCoords.y >= size.y) {
                    return;
                }
                vec2 uv = vec2(pixelCoords) / vec2(size);
                vec4 finalColor = vec4(0.0, 0.0, 0.0, 0.0);
                for (int i = 0; i < u_TextureCount; ++i) {
                    vec4 layerColor = texture(u_Textures[i], uv);
                    finalColor = mix(finalColor, layerColor, layerColor.a);
                    finalColor.a = layerColor.a + finalColor.a * (1.0 - layerColor.a);
                }
                fragColor = finalColor;
                //imageStore(u_OutputImage, pixelCoords, finalColor);
            }
       \s""";
}
