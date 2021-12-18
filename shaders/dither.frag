#version 130

in vec2 textureCoord;

uniform sampler2D textureImg;
uniform int colorMode, ditherMode, bayerMatrix;
uniform float tresHold, noiseConst;

out vec4 outColor;

const int indexMatrix2x2[4] = int[](
0, 2,
3, 1
);

const int indexMatrix4x4[16] = int[](
0, 8, 2, 10,
12, 4, 14, 6,
3, 11, 1, 9,
15, 7, 13, 5
);
const int indexMatrix8x8[64] = int[]
(0, 48, 12, 60, 3, 51, 15, 63,
32, 16, 44, 28, 35, 19, 47, 31,
8, 56, 4, 52, 11, 59, 7, 55,
40, 24, 36, 20, 43, 27, 39, 23,
2, 50, 24, 62, 1, 49, 13, 61,
34, 18, 46, 30, 33, 17, 45, 29,
10, 58, 6, 54, 9, 57, 5, 53,
42, 26, 38, 22, 41, 25, 37, 21);


//source: https://gamedev.stackexchange.com/questions/32681/random-number-hlsl
// získání náhodné hodnoty //TODO: v returnlze měnit "detail" ditheringu (větší číslo->detailnější) aktuálně na 0.5
float randVal(vec2 uv)
{
    float noise = (fract(sin(dot(uv, vec2(12.9898, 78.233)*2.0)) * 43758.5453));
    return noise * noiseConst;
}

// dithering podle náhodné hodnoty TODO: lze invertovartovat barvy. možnost na zllepšení (přidat tuto možnost uživateli)
float randomDither(float color) {
    // source: https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/gl_FragCoord.xhtml
    if (color < randVal(vec2(gl_FragCoord.xy)))
    return 0.0;
    return 1.0;
}

float indexValue() {
    float matrix;
    vec2 value;

    switch (bayerMatrix){

        case 0:
        value = vec2(int(mod(gl_FragCoord.x, 2)), int(mod(gl_FragCoord.y, 2)));
        matrix =  indexMatrix2x2[int(value.x) * 2 + int(value.y)] / 4.0;
        break;

        case 1:
        value = vec2(int(mod(gl_FragCoord.x, 4)), int(mod(gl_FragCoord.y, 4)));
        matrix = indexMatrix4x4[int(value.x) * 4 + int(value.y)] / 16.0;
        break;

        case 2:
        value = vec2(int(mod(gl_FragCoord.x, 8)), int(mod(gl_FragCoord.y, 8)));
        matrix = indexMatrix8x8[int(value.x) * 8 + int(value.y)] / 64.0;
        break;
    }

    return matrix;
}
// source: https://en.wikipedia.org/wiki/Ordered_dithering, http://alex-charlton.com/posts/Dithering_on_the_GPU/
float orderedDith(float color) {
    float closestColor = (color < 0.5) ? 0 : 1;
    float secondClosestColor = 1 - closestColor;
    float d = indexValue();
    float distance = abs(closestColor - color);
    return (distance < d) ? closestColor : secondClosestColor;
}

// source: https://stackoverflow.com/questions/8663004/how-best-to-approach-a-localised-thresholding-opengl-function?rq=1
float treshHoldDith(float color) {
    if (color < tresHold){
        return 0.0;
    }
    return 1.0;
}

void main(){
    // source: https://community.khronos.org/t/how-to-texture-a-rgb-image-in-grayscale/36420
    float GrayScale = dot(texture2D(textureImg, textureCoord).rgb, vec3(0.3, 0.59, 0.11));

    vec3 textureColor = texture(textureImg, textureCoord).rgb;

    // podle zvoleného modu se provede dithering (podle zvoleného dithering. algd.)
    // 0 - random dithering
    // 1 - ordered (bayer) dithering
    // zobrazení barevnosti dle colorMode
    // 0 - bez ditheringu
    // 1 - colored ddithering
    // 2 - grayscale dithering
    switch (ditherMode){
        case 0:
        colorMode == 2 ? outColor = vec4(vec3(randomDither(GrayScale)), 1.0) :
        outColor = vec4(vec3(
        randomDither(textureColor.r),
        randomDither(textureColor.g),
        randomDither(textureColor.b)
        ), 1.0f);
        break;

        case 1:
        colorMode == 2 ? outColor = vec4(vec3(orderedDith(GrayScale)), 1.0f) :
        outColor = vec4(vec3(
        orderedDith(textureColor.r),
        orderedDith(textureColor.g),
        orderedDith(textureColor.b)
        ), 1.0f);

        break;

        case 2:
        colorMode == 2 ?  outColor = vec4(vec3(treshHoldDith(GrayScale)), 1.0f) :
        outColor = vec4(vec3(
        treshHoldDith(textureColor.r),
        treshHoldDith(textureColor.g),
        treshHoldDith(textureColor.b)
        ),1.0f);
        break;
    }
    if (colorMode == 0){
        outColor = vec4(textureColor, 1.0f);
    }
}

//floyd-stenberg - https://titanwolf.org/Network/Articles/Article?AID=34c283fb-362a-4999-b6c1-306ef22139e3 (opencv a glsl) TODO: podívat se zda toto chci implementovat s GLSL a nebo potom zkusit opencv