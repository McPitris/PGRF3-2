#version 130

in vec2 textureCoord;

uniform sampler2D mosaic;
uniform int colorMode;

out vec4 outColor;

//source: https://gamedev.stackexchange.com/questions/32681/random-number-hlsl
// získání náhodné hodnoty //TODO: v returnlze měnit "detail" ditheringu (větší číslo->detailnější) aktuálně na 0.5
float randVal(vec2 uv)
{
    float noise = (fract(sin(dot(uv, vec2(12.9898, 78.233)*2.0)) * 43758.5453));
    return noise * 0.5;
}

// dithering podle náhodné hodnoty TODO: lze invertovartovat barvy. možnost na zllepšení (přidat tuto možnost uživateli)
float randomDither(float color) {
    // source: https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/gl_FragCoord.xhtml
    if (color < randVal(vec2(gl_FragCoord.xy)))
    return 0.0;
    return 1.0;
}

void main(){
    // source: https://community.khronos.org/t/how-to-texture-a-rgb-image-in-grayscale/36420
    float GrayScale = dot(texture2D(mosaic, textureCoord).rgb, vec3(0.3, 0.59, 0.11));

    vec3 textureColor = texture(mosaic, textureCoord).rgb;

    // podle zvoleného modu se zobrazí colored, grayscale nebo žádný dithering
    // 0 - bez ditheringu
    // 1 - barevný dithering
    // 2 - grayscale dithering
    switch (colorMode){
        case 0:
        outColor = vec4(textureColor, 1.0);
        break;
        case 1:
        outColor = vec4(vec3(
        randomDither(textureColor.r),
        randomDither(textureColor.g),
        randomDither(textureColor.b)
        ), 1.0);
        break;
        case 2:
        outColor = vec4(vec3(randomDither(GrayScale)), 1.0);
        break;
    }
    //

}