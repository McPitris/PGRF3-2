# PGRF3-2
## Zpracování obrazu
### Random Dithering, Ordered dithering, Tresholder dithering

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
