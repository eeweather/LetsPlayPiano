/*
  Simple program to take input from Bluetooth and do a variety of light shows based off it.
*/

//Libs
#include "BluetoothSerial.h"
#include <Adafruit_NeoPixel.h>

//Prototypes
void colorWipe(uint32_t color, int wait);
void rainbow(int wait);
void theaterChaseRainbow(int wait);

//Useful parameters / defines
#define LED_PIN 15
#define NUMPIXELS 7
#define SPEED     1000

//Debug message that all the examples has but has never been needed.
#if !defined(CONFIG_BT_ENABLED) || !defined(CONFIG_BLUEDROID_ENABLED)
#error Bluetooth is not enabled! Please run `make menuconfig` to and enable it
#endif

//Neopixel object
Adafruit_NeoPixel np = Adafruit_NeoPixel(NUMPIXELS, LED_PIN, NEO_GRBW);

//Global Vars:
char btData;

BluetoothSerial SerialBT;


void setup() {
  //Neopixel Init:
  np.begin();

  //Bluetooth Init:
  Serial.begin(115200);
  SerialBT.begin("ESP32test"); //Bluetooth device name
  Serial.println("The device started, now you can pair it with bluetooth!");

}


char loopedShow;
int isLooping = 0;
void loop() {


  //This function is for writing from serial to BT -> not necessary for this project
  /*
    if (Serial.available()) {
    SerialBT.write(Serial.read());
    }
  */


  //Get any new data, write to serial monitor too while we're at it.
  if (SerialBT.available()) {
    btData = SerialBT.read();
    Serial.print("Got from BT:");
    Serial.write(btData);
    Serial.println("");
  }

  switch (btData)
  {
    case 'a' : srgb(SPEED);
               isLooping = 0;
      break;
    case 'b' : firefly(SPEED);
               isLooping = 0;
      break;
    case 'c' : ocean(SPEED);
               isLooping = 0;
      break;
    case 'd' : dandelion(SPEED);
               isLooping = 0;
      break;
    case 'e' : emergency(SPEED);
               isLooping = 0;
      break;
    case 'f' : cattoy(SPEED);
               isLooping = 0;
      break;
    case 'g' : dragon(SPEED);
               isLooping = 0;
      break;
    case 'h' : encounter(SPEED);
               isLooping = 0;
      break;
    case 'w' : fireworks(SPEED);
               loopedShow = 'n';
               isLooping = 1;
      break;
    case 'x' : isLooping = 0;
      break;
    case 'y' : theaterChaseRainbow(SPEED);
              isLooping = 1;
      break;
    case 'z' : isLooping = 1;
               loopedShow = random(0,9) + 97;
      break;
    default :
      break;
  }

  //If the bt doesn't send z, we'll wipe out the pixels after a char.
  if (isLooping == 0)
  {
  btData = 'n';
  np.fill(np.Color(0,0,0,0),0,NUMPIXELS);
  np.show();
  }
  else
  {
        switch (loopedShow)
    {
      case 'a' : srgb(SPEED);
        break;
      case 'b' : firefly(SPEED);
        break;
      case 'c' : ocean(SPEED);
        break;
      case 'd' : dandelion(SPEED);
        break;
      case 'e' : emergency(SPEED);
        break;
      case 'f' : cattoy(SPEED);
        break;
      case 'g' : dragon(SPEED);
        break;
      case 'h' : encounter(SPEED);
        break;
      case 'w' : fireworks(SPEED);
        break;
      case 'y': theaterChaseRainbow(SPEED);
        break;
      default :
        break;
    }
  }
  





  delay(20);

}





// Rainbow pinwheel around the np
void theaterChaseRainbow(int wait) {

  int interval = wait / 10;
  for (int frm = 0; frm < 10; frm ++)
  {
    int firstPixelHue = 0;    
    for (int a = 0; a < 4; a++) { 
      for (int b = 0; b < 3; b++) { 
        np.clear();         
        for (int c = b; c < np.numPixels(); c += 3) {
          int      hue   = firstPixelHue + c * 65536L / np.numPixels();
          uint32_t color = np.gamma32(np.ColorHSV(hue)); 
          np.setPixelColor(c, color);
        }
        np.show();                
        delay(interval);                 
        firstPixelHue += 65536 / 90; 
      }
    }

  }
}

//Gradually pulses a greeny-yellow glow.
void dandelion(int wait)
{
  uint32_t col;
  uint32_t start_col = np.Color(10, 40, 0, 2);
  int bright;
  int interval = wait / 20;
  np.fill(start_col, 0, NUMPIXELS);
  np.show();
  for (int frm = 0; frm < 10; frm ++)
  {

    col = np.Color(10, 40, 0, frm * 20);
    np.fill(col, 0, NUMPIXELS);
    np.show();

    delay(interval);
  }
  for (int frm = 10; frm > 0; frm --)
  {
    col = np.Color(10, 40, 0, frm * 20);
    np.fill(col, 0, NUMPIXELS);
    np.show();
    delay(interval);
  }
}


//Emergency = Blue and Red pulsing lights (police lights)
void emergency(int wait)
{
  uint32_t red_base = np.Color(255, 0, 0, 40);
  uint32_t blue_base = np.Color(0, 0, 255, 10);
  int interval = wait / 20;
  uint32_t red_show, blue_show;


  for (int frm = 0; frm < 3; frm ++)
  {
    for (int pixel = 0; pixel < 7; pixel ++)
    {
      int test = pixel + random(-2, 2);
      if ((test % 2) == 1)
      {
        red_show = np.Color( 235 + random(-20, 20), 0, 0, 25 + random(-10, 10));
        np.setPixelColor(pixel, red_base);
        np.show();
      }
      else
      {
        blue_show = np.Color( 0,  235 + random(-20, 20), 0, 15 + random(-10, 10));

        np.setPixelColor(pixel, blue_base);
        np.show();
      }
      delay(interval);
    }
  }
}


//rainbreak, cloudy blue sides into sun
void rainbreak(int wait)
{
  uint32_t ocean_start = np.Color(5, 0, 95, 0);

  int interval = wait / 40;

  np.fill(ocean_start, 0, NUMPIXELS);
  np.show();

  for (int frm = 0; frm < 21; frm ++)
  {

    if (frm < 7)
    {
      np.setPixelColor(0, np.Color(0, frm * 2, 95 + frm * 6 - 10, frm * 2));
      np.setPixelColor(1, np.Color(0, frm, 95 + frm * 4, frm));
      np.setPixelColor(2, np.Color(0, frm, 95 + frm * 3, frm));
    }

    if (frm > 7)
    {
      np.setPixelColor(0, np.Color(0, 10 + frm * 2, 135 + frm * 6 - 10, frm * 2));
      np.setPixelColor(1, np.Color(0, 10 + frm, 135 + frm * 5, frm * 2));
      np.setPixelColor(2, np.Color(0, 10 + frm, 135 + frm * 4, frm * 2));

      np.setPixelColor(3, np.Color(0, frm * 2, 95 + frm * 6 - 10, frm * 2));
      np.setPixelColor(4, np.Color(0, frm, 95 + frm * 4, frm));
      np.setPixelColor(5, np.Color(0, frm, 95 + frm * 3, frm));

    }

    if (frm > 14)
    {
      np.setPixelColor(0, np.Color(0, 10 + frm * 2, 154 + frm * 5, frm * 2));
      np.setPixelColor(1, np.Color(0, 20 + frm, 150 + frm * 5, frm * 2));
      np.setPixelColor(2, np.Color(0, 20 + frm, 155 + frm * 4, frm * 2));

      np.setPixelColor(3, np.Color(0, frm * 2, 135 + frm * 5 - 10, frm * 2));
      np.setPixelColor(4, np.Color(0, frm, 135 + frm * 4, frm));
      np.setPixelColor(5, np.Color(0, frm, 135 + frm * 3, frm));

      np.setPixelColor(6, np.Color(0, frm * 2, 95 + frm * 6 - 10, frm * 2));

    }
    np.show();
    delay(interval);
  }
  for (int frm = 21; frm > 40; frm ++)
  {

    if (frm < 7)
    {
      np.setPixelColor(6, np.Color(0, 35 - frm, 255 - 5 * frm, 45 - 2 * frm));
      np.setPixelColor(5, np.Color(0, 35 - frm, 255 - 4 * frm, 40 - frm));
      np.setPixelColor(4, np.Color(0, 35 - frm, 255 - 3 * frm, 40 - frm));
    }

    if (frm > 7)
    {
      np.setPixelColor(6, np.Color(0, 35 - frm, 230 - 5 * frm, 45 - 2 * frm));
      np.setPixelColor(5, np.Color(0, 35 - frm, 230 - 4 * frm, 40 - frm));
      np.setPixelColor(4, np.Color(0, 35 - frm, 230 - 3 * frm, 40 - frm));

      np.setPixelColor(3, np.Color(0, 35 - frm, 255 - 5 * frm, 45 - 2 * frm));
      np.setPixelColor(2, np.Color(0, 35 - frm, 255 - 4 * frm, 40 - frm));
      np.setPixelColor(1, np.Color(0, 35 - frm, 255 - 3 * frm, 40 - frm));

    }

    if (frm > 14)
    {
      np.setPixelColor(6, np.Color(0, 35 - frm, 200 - 5 * frm, 45 - 2 * frm));
      np.setPixelColor(5, np.Color(0, 35 - frm, 200 - 4 * frm, 40 - frm));
      np.setPixelColor(4, np.Color(0, 35 - frm, 200 - 3 * frm, 40 - frm));

      np.setPixelColor(3, np.Color(0, 35 - frm, 235 - 5 * frm, 45 - 2 * frm));
      np.setPixelColor(2, np.Color(0, 35 - frm, 235 - 4 * frm, 40 - frm));
      np.setPixelColor(1, np.Color(0, 35 - frm, 235 - 3 * frm, 40 - frm));

      np.setPixelColor(0, np.Color(0, 34 - frm, 255 - 3 * frm, 30 - frm));

    }
    np.show();
    delay(interval);
  }
}

//Something fiery, reddish orangish burns
void dragon(int wait)
{
  int ember1, ember2, ember3;
  int interval = wait / 40;
  //Produce three smouldering embers
  uint32_t base_red = np.Color(235, 0, 0, 15);
  np.fill(base_red, 0, NUMPIXELS);
  np.show();

  for (int roll = 0; roll < 3; roll ++)
  {
    ember1 = random(0, 7);
    ember2 = random(0, 7);
    ember3 = random(0, 7);
    while (ember1 == ember2)
    {
      ember2 = random(0, 7);
    }
    while (ember1 == ember3 || ember2 == ember3)
    {
      ember3 = random(0, 7);
    }
  }

  for (int frm = 0; frm < 20; frm ++)
  {
    np.setPixelColor(ember1, np.Color(215 - frm, 10 + frm * 3, 0, frm));
    np.setPixelColor(ember2, np.Color(230 - frm, 10 + frm * 2, 0, frm));
    np.setPixelColor(ember3, np.Color(190 - frm, 10 + frm * 1, frm, 10));
    np.show();
    delay(interval);
  }

  for (int frm = 20; frm > 0; frm --)
  {
    np.setPixelColor(ember1, np.Color(215 - frm, 61 - frm * 3, 0, frm));
    np.setPixelColor(ember2, np.Color(230 - frm, 41 - frm * 2, 0, frm));
    np.setPixelColor(ember3, np.Color(190 - frm, 21 - frm * 1, frm, 10));
    np.show();
    delay(interval);
  }
}

//Alieny, purples and fluorescent green?
void encounter(int wait)
{
  int interval = wait / 40;
  np.fill(np.Color(75, 0, 120, 0), 0, NUMPIXELS);
  np.show();


  for (int frm = 0; frm < 10; frm ++)
  {
    np.setPixelColor(0, np.Color(95, 50 + frm * 20, 120 - frm * 10));
    np.show();
    delay(interval);
  }
  int invader1, invader2;
  invader1 = random(1, 7);
  invader2 = random(1, 7);
  while (invader1 == invader2)
  {
    invader2 = random(1, 7);
  }
  for (int frm = 0; frm < 10; frm ++)
  {
    np.setPixelColor(0, np.Color(95, 255, 0));
    np.setPixelColor(invader1, np.Color(95, 50 + frm * 20, 120 - frm * 10));
    np.setPixelColor(invader2, np.Color(95, 50 + frm * 20, 120 - frm * 10));

    np.show();
    delay(interval);
  }
  int invader3, invader4, invader5;
  invader3 = random(1, 6);
  invader4 = random(1, 6);
  invader5 = random(1, 6);
  while (invader3 == invader1 || invader3 == invader2)
  {
    invader3 = random(1, 7);
  }
  while (invader4 == invader1 || invader4 == invader2 || invader4 == invader3)
  {
    invader4 = random(1, 7);
  }
  while (invader5 == invader1 || invader5 == invader2 || invader5 == invader4 || invader5 == invader3)
  {
    invader5 = random(1, 7);
  }

  for (int frm = 0; frm < 20; frm++)
  {
    np.setPixelColor(0, np.Color(95, 255, 0));
    np.setPixelColor(invader1, np.Color(95, 255, 0));
    np.setPixelColor(invader2, np.Color(95, 255, 0));

    np.setPixelColor(invader3, np.Color(95, 50 + frm * 10, 120 - frm * 5));
    np.setPixelColor(invader4, np.Color(95, 50 + frm * 10, 120 - frm * 5));
    np.setPixelColor(invader5, np.Color(95, 50 + frm * 10, 120 - frm * 5));

    np.show();
    delay(interval);
  }
}

//Smoothly transition all the pixels along the RGB scale.
void srgb(int wait)
{
  int interval = wait / 30;

  for (int frm = 0; frm < 254; frm = frm + 30)
  {
    np.fill(np.Color(255 - frm, frm, 0, 0), 0, NUMPIXELS);
    np.show();
    delay(interval);
  }
  for (int frm = 0; frm < 254; frm = frm + 30)
  {
    np.fill(np.Color(0, 255 - frm, frm, 0), 0, NUMPIXELS);
    np.show();
    delay(interval);
  }
  for (int frm = 0; frm < 254; frm = frm + 30)
  {
    np.fill(np.Color(frm, 0, 255 - frm, 0), 0, NUMPIXELS);
    np.show();
    delay(interval);
  }
}


//Two little fireflies on a lazy night
void firefly(int wait)
{
  int interval = wait / 40;

  int ff_pix1, ff_pix2;

  for (int locations = 0; locations < 2; locations ++)
  {
    ff_pix1 = random(0, 7);
    ff_pix2 = random(0, 7);
    while (ff_pix1 == ff_pix2)
    {
      ff_pix2 = random(0, 7);
    }


    for (int glow = 0; glow < 2; glow ++)
    {

      np.fill(np.Color(0, 0, 80, 0), 0, NUMPIXELS);
      np.show();

      for (int frm = 0; frm < 10; frm ++)
      {
        if (glow == 1)
        {
          np.setPixelColor(ff_pix1, np.Color(255, 175 + frm * 4, 0, 0));
          np.setPixelColor(ff_pix2, np.Color(255, 255 - frm * 4, 0, 0));
        }
        else
        {
          np.setPixelColor(ff_pix2, np.Color(255, 175 + frm * 4, 0, 0));
          np.setPixelColor(ff_pix1, np.Color(255, 255 - frm * 4, 0, 0));
        }
        np.show();
        delay(interval);

      }
    }
  }
}

//Gentle ocean blues and cyans swirl
void ocean(int wait)
{
  uint32_t ocean_start = np.Color(5, 0, 95, 0);

  int interval = wait / 40;

  np.fill(ocean_start, 0, NUMPIXELS);
  np.show();

  for (int frm = 0; frm < 21; frm ++)
  {

    if (frm < 7)
    {
      np.setPixelColor(0, np.Color(0, frm * 2, 95 + frm * 6 - 10, frm * 2));
      np.setPixelColor(1, np.Color(0, frm, 95 + frm * 4, frm));
      np.setPixelColor(2, np.Color(0, frm, 95 + frm * 3, frm));
    }

    if (frm > 7)
    {
      np.setPixelColor(0, np.Color(0, 10 + frm * 2, 135 + frm * 6 - 10, frm * 2));
      np.setPixelColor(1, np.Color(0, 10 + frm, 135 + frm * 5, frm * 2));
      np.setPixelColor(2, np.Color(0, 10 + frm, 135 + frm * 4, frm * 2));

      np.setPixelColor(3, np.Color(0, frm * 2, 95 + frm * 6 - 10, frm * 2));
      np.setPixelColor(4, np.Color(0, frm, 95 + frm * 4, frm));
      np.setPixelColor(5, np.Color(0, frm, 95 + frm * 3, frm));

    }

    if (frm > 14)
    {
      np.setPixelColor(0, np.Color(0, 10 + frm * 2, 154 + frm * 5, frm * 2));
      np.setPixelColor(1, np.Color(0, 20 + frm, 150 + frm * 5, frm * 2));
      np.setPixelColor(2, np.Color(0, 20 + frm, 155 + frm * 4, frm * 2));

      np.setPixelColor(3, np.Color(0, frm * 2, 135 + frm * 5 - 10, frm * 2));
      np.setPixelColor(4, np.Color(0, frm, 135 + frm * 4, frm));
      np.setPixelColor(5, np.Color(0, frm, 135 + frm * 3, frm));

      np.setPixelColor(6, np.Color(0, frm * 2, 95 + frm * 6 - 10, frm * 2));

    }
    np.show();
    delay(interval);
  }

  for (int frm = 21; frm > 0; frm --)
  {

    if (frm < 7)
    {
      np.setPixelColor(6, np.Color(0, 35 - frm, 255 - 5 * frm, 45 - 2 * frm));
      np.setPixelColor(5, np.Color(0, 35 - frm, 255 - 4 * frm, 40 - frm));
      np.setPixelColor(4, np.Color(0, 35 - frm, 255 - 3 * frm, 40 - frm));
    }

    if (frm > 7)
    {
      np.setPixelColor(6, np.Color(0, 35 - frm, 230 - 5 * frm, 45 - 2 * frm));
      np.setPixelColor(5, np.Color(0, 35 - frm, 230 - 4 * frm, 40 - frm));
      np.setPixelColor(4, np.Color(0, 35 - frm, 230 - 3 * frm, 40 - frm));

      np.setPixelColor(3, np.Color(0, 35 - frm, 255 - 5 * frm, 45 - 2 * frm));
      np.setPixelColor(2, np.Color(0, 35 - frm, 255 - 4 * frm, 40 - frm));
      np.setPixelColor(1, np.Color(0, 35 - frm, 255 - 3 * frm, 40 - frm));

    }

    if (frm > 14)
    {
      np.setPixelColor(6, np.Color(0, 35 - frm, 200 - 5 * frm, 45 - 2 * frm));
      np.setPixelColor(5, np.Color(0, 35 - frm, 200 - 4 * frm, 40 - frm));
      np.setPixelColor(4, np.Color(0, 35 - frm, 200 - 3 * frm, 40 - frm));

      np.setPixelColor(3, np.Color(0, 35 - frm, 235 - 5 * frm, 45 - 2 * frm));
      np.setPixelColor(2, np.Color(0, 35 - frm, 235 - 4 * frm, 40 - frm));
      np.setPixelColor(1, np.Color(0, 35 - frm, 235 - 3 * frm, 40 - frm));

      np.setPixelColor(0, np.Color(0, 34 - frm, 255 - 3 * frm, 30 - frm));

    }
    np.show();
    delay(interval);
  }
}

//Yellow background with a pink ball rolling around the outside
void cattoy(int wait)
{
  
  int interval = wait / 100;


  for (int frm = 0; frm < 12; frm ++)
  {

    np.fill(np.Color(255,255,0,0), 0, NUMPIXELS);

    np.setPixelColor(frm % 6 + 1, np.Color(255, 0, 127, 0));
    np.show();
    delay(10 + frm * interval);
  }
}


void fireworks(int wait)
{
  int interval = 2*wait / 4;

  int fw_roll = random(0, 6);
  int fw_start = random(1, 7);
  uint32_t fw_col, fw_fade;

  switch (fw_roll)
  {
    case 0: fw_col = np.Color(255, 0, 0, 0);
            fw_fade = np.Color(130, 0, 30, 0);
      break;
    case 1: fw_col = np.Color(0, 255, 0, 0);
            fw_fade = np.Color(0, 130, 30, 0);
      break;
    case 2: fw_col = np.Color(0, 0, 255, 0);
            fw_fade = np.Color(20,0, 140, 10);
      break;
    case 3: fw_col = np.Color(255, 0, 255, 0);
            fw_fade = np.Color(130, 0, 140, 0);
      break;
    case 4: fw_col = np.Color(0, 255, 255, 0);
            fw_fade = np.Color(0, 130, 140, 0);
      break;
    case 5: fw_col = np.Color(255, 255, 0, 0);
            fw_fade = np.Color(130, 130, 0, 0);
      break;
  }

  np.fill(np.Color(0, 0, 80, 0), 0, NUMPIXELS);
  np.setPixelColor(fw_start, fw_col);
  np.show();
  delay(interval*2);

  np.fill(np.Color(0, 0, 80, 0), 0, NUMPIXELS);
  np.setPixelColor(0, fw_col);
  np.show();
  delay(interval*2);

  np.fill(fw_col, 0, NUMPIXELS);
  np.show();
  delay(interval);

  np.setPixelColor(0, fw_fade);
  np.show();
  delay(interval);

  np.fill(fw_fade, 0, NUMPIXELS);
  np.setPixelColor(0, np.Color(0, 0, 80, 20));
  np.show();
  delay(interval);
}
