#include <SoftwareSerial.h>
SoftwareSerial mySerial(10,11);
void setup() 
{
  Serial.begin(9600);
  mySerial.begin(9600);
}
void loop() {
  delay(100);
  if(mySerial.available()>0)
  {
    delay(10);
    Serial.println(mySerial.read());
  }
}
