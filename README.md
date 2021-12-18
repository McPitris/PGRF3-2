# PGRF3-2
## Zpracování obrazu
### Random Dithering, Ordered dithering, Tresholder dithering

------------------------------------------------------

#### Random dithering
- možnost přepínání mezi obrázky
- přepínání mezi originálním obrázkem, barevným ditheringem a grayscale ditheringem
- možnost upravení hodnoty ditheringu (0% - 100%) ("jemnost") - KEY_DOWN, KEY_UP

#### Ordered dithering
- možnost přepínání mezi obrázky
- přepínání mezi originálním obrázkem, barevným ditheringem a grayscale ditheringem
- možnost vybráhní Bayer matice (2x2, 4x4, 8x8) - KEY_DOWN, KEY_UP

#### Tresholder dithering
- možnost přepínání mezi obrázky
- přepínání mezi originálním obrázkem, barevným ditheringem a grayscale ditheringem
- možnost nastavení tresholder hodnoty (0% - 100%) - KEY_DOWN, KEY_UP

### Ovládání
* KEY_RIGHT - další obrázek
* KEY_LEFT - předchozí obrázek
* KEY_UP - zvýšení hodnoty nastavení v ditheringu (dle módu ditheringu)
* KEY_DOWN - snížení hodnoty nastavení v ditheringu (dle módu ditheringu)
* KEY_X - přepínání mezi módů ditheringu (random, ordered, tresholder)
* KEY_C - přepínání barevného zobrazení dotheringu (originální obrázek, barevný dithering, grayscale dithering)
* KEY_U - upload/zobrazení obrázku
  * pokud je obrázek ze složky res/textures, bude pouze zobrazen
  * pokud je obrázek z jiné složky bude zkopírován do složky res/textures a po restartování aplokace načten
  * *automatické restartování aplikace není implementováno*
* KEY_H - skrytí/zobrazení popisů v obraze

--------------------------------
### Spuštění projektu
- stáhnutí projektu z gitu
- otevření projektu v IntelliJ IDEA
- vybrat složku **res** a **shaders** a označit jako _Resourses Root_
  - _Right click -> Mark Direstory as -> Resources Root_
- přidání **jwgl** do struktury projektu
  - File -> Project Structure -> Global libraries -> + _(New global library)_ -> Java -> _(vybrat složku s jwgl - rozbalená složka)_
  - File -> Project Structure -> Modules -> Dependencies -> + _(Add)_ -> Library ... -> _(vybrat jwgl)_ Add selected -> Apply
- pokud v projektu něco svítí "červeně" je potřeba restartovat IDEU, aby se soubory znovu naindexovaly

### Důležité odkazy
- JWGL: https://mega.nz/file/QtcmQDwD#HkRUL6-vN1uJv2UcWOZkbFX9hz_BlE6BBkjmfnZ_1wc
  - nutno stáhnout a dle návodu připojit
- Transforms:  https://mega.nz/file/kpF2QbKD#6JpvOO4ufHsvnM8PGjF344Ow47vad6jRKoxUtvcIZjo
  - projekt obsahuje Transforms
- JWGL Utils: https://mega.nz/file/sxNmXZ7T#SuBGlYdaK0UoV-N8Kv2w52Rwj3hsT8QA2Q_dWVvo-_Y
  - projekt obsahuje JWGL Utils
- Jak sputit projekt (official): https://mega.nz/file/QhMATJ5I#6orZJonFbY0NqBR3MwipWFgOOjImMRrJo9mrtQkltEA
  - soubor přiložen v projektu
