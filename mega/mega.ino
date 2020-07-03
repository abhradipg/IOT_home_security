#include <Adafruit_Fingerprint.h>
#include <LiquidCrystal.h>

#define mySerial Serial1   //for fingerprint
const int rs = 52, en = 53, d4 = 50, d5 = 51, d6 = 48, d7 = 49;
const int lock = 44;
const int button =45;
uint8_t id;
LiquidCrystal lcd(rs, en, d4, d5, d6, d7);
Adafruit_Fingerprint finger = Adafruit_Fingerprint(&mySerial);
String no;
String otp;
void setup()       //setup
{
  pinMode(lock, OUTPUT);
  digitalWrite(lock,HIGH);
  pinMode(button,INPUT);
  Serial.begin(9600);
  finger.begin(57600);
  Serial2.begin(9600); // uno
  Serial3.begin(9600);   //gsm
  lcd.begin(16, 2);
  if (finger.verifyPassword()) {
    lcd.setCursor(0,0);
    lcd.print("Found sensor!");
    lcd.setCursor(0,0);
    lcd.print("                ");
  } else {
    lcd.setCursor(0,0);
    lcd.print("Not find sensor");
    lcd.setCursor(0,0);
    lcd.print("                ");
    while (1) { delay(1); }
  }
}

uint8_t readnumber(void) {
  uint8_t num = 0;
  
  while (num == 0) {
    while (!Serial.available());
    num = Serial.parseInt();
  }
  return num;
}

void loop()          //loopfunction
{
   char input;
   if(Serial.available()>0)
   {
       input=Serial.read();
       if(input=='e')     //for enrolling
       {
            Serial.println("ok"); //signal received
            id = readnumber();
            lcd.setCursor(0,0);
            lcd.print("Enrolling ID");
            lcd.setCursor(0,1);
            lcd.print(id);
            delay(500);
            while (!getFingerprintEnroll() );
            delay(500);
            lcd.setCursor(0,0);
            lcd.print("                ");
            Serial.println("ok");
            delay(100);
            no=Serial.readString();
            if(no.equals("d"))
            goto label4;
            SendMessagee();
            label4:
            ;
       }
       else if(input=='d')   //for deletion
       {
            Serial.println("ok"); //signal received
            id = readnumber();
            lcd.setCursor(0,0);
            lcd.print("Deleting ID");
            lcd.setCursor(0,1);
            lcd.print(id);
            delay(500);
            deleteFingerprint(id);
            delay(500);
            lcd.setCursor(0,0);
            lcd.print("                ");
            lcd.setCursor(0,1);
            lcd.print("                ");
            Serial.println("ok");
            delay(100);
            no=Serial.readString();
            if(no.equals("d"))
            goto label3;
            SendMessaged();
            label3:
            ;
       }
       else if(input=='u')
       {
        digitalWrite(lock,LOW);
        lcd.setCursor(0,0);
        lcd.print("Welcome");
        Serial.println("ok");
        delay(100);
        no=Serial.readString();
        if(no.equals("d"))
        goto label1;
        SendMessage();
        label1:
        delay(5000);
        lcd.setCursor(0,0);
        lcd.print("              ");
        digitalWrite(lock,HIGH);
       }
       else if(input=='s')
       {
        Serial.println("ok");
        delay(100);
        no=Serial.readString();
        if(no.equals("d"))
        goto label2;
        SendMessage();
        label2:
        ;
       }
       else if(input=='o')
        {
          Serial.println("ok");
          delay(100);
          no=Serial.readString();
          Serial.println("ok");
          delay(100);
          otp=Serial.readString();
          SendMessagea();
        }
        else if(input=='l')
        {
          Serial.println("ok");
          delay(100);
          no=Serial.readString();
          Serial.println("ok");
          delay(100);
          otp=Serial.readString();
          SendMessagel();
        }
   }
   if(digitalRead(button)==HIGH)
   {
       while(getFingerprintID()==100)
       delay(50);
   }
}

uint8_t getFingerprintEnroll()            //for enrollment
{
    lcd.setCursor(0,0);
    lcd.print("                ");
    lcd.setCursor(0,1);
    lcd.print("                ");
    int p = -1;
    while (p != FINGERPRINT_OK) 
    {
          p = finger.getImage();
          switch (p)
          {
                 case FINGERPRINT_OK:
                 lcd.setCursor(0,0);
                 lcd.print("                ");
                 lcd.setCursor(0,0);
                 lcd.print("Image taken");
                 break;
                 case FINGERPRINT_NOFINGER:
                 lcd.setCursor(0,0);
                 lcd.print("                ");
                 lcd.setCursor(0,0);
                 lcd.print("Place Finger");
                 break;
                 case FINGERPRINT_PACKETRECIEVEERR:
                 lcd.setCursor(0,0);
                 lcd.print("                ");
                 lcd.setCursor(0,0);
                 lcd.print("Comm. error");
                 break;
                 case FINGERPRINT_IMAGEFAIL:
                 lcd.setCursor(0,0);
                 lcd.print("                ");
                 lcd.setCursor(0,0);
                 lcd.print("Imaging error");
                 break;
                 default:
                 lcd.setCursor(0,0);
                 lcd.print("                ");
                 lcd.setCursor(0,0);
                 lcd.print("Unknown error");
                 break;
          }
    }

  // OK success!

    p = finger.image2Tz(1);
    switch (p) 
    {
          case FINGERPRINT_OK:
          lcd.setCursor(0,0);
          lcd.print("                ");
          lcd.setCursor(0,0);
          lcd.print("Image convert");
          break;
          case FINGERPRINT_IMAGEMESS:
          lcd.setCursor(0,0);
          lcd.print("                ");
          lcd.setCursor(0,0);
          lcd.print("Image messy");
          return p;
          case FINGERPRINT_PACKETRECIEVEERR:
          lcd.setCursor(0,0);
          lcd.print("                ");
          lcd.setCursor(0,0);
          lcd.print("Comm error");
          return p;
          case FINGERPRINT_FEATUREFAIL:
          lcd.setCursor(0,0);
          lcd.print("                ");
          lcd.setCursor(0,0);
          lcd.println("Invaild");
          return p;
          case FINGERPRINT_INVALIDIMAGE:
          lcd.setCursor(0,0);
          lcd.print("                ");
          lcd.setCursor(0,0);
          lcd.print("Invalid");
          return p;
          default:
          lcd.setCursor(0,0);
          lcd.print("                ");
          lcd.setCursor(0,0);
          lcd.print("Unknown error");
          return p;
    }
  
    lcd.setCursor(0,0);
    lcd.print("                ");
    lcd.setCursor(0,0);
    lcd.print("Remove finger");
    delay(2000);
    p = 0;
    while (p != FINGERPRINT_NOFINGER) 
    {
         p = finger.getImage();
    }
    lcd.setCursor(0,0);
    lcd.print("                ");
    lcd.setCursor(0,0);
    lcd.print("ID ");
    lcd.setCursor(5,0);
    lcd.print(id);
    p = -1;
    lcd.setCursor(0,0);
    lcd.print("                ");
    lcd.setCursor(0,0);
    lcd.print("Place finger");
    while (p != FINGERPRINT_OK) 
    {
         p = finger.getImage();
         switch (p) 
         {
                 case FINGERPRINT_OK:
                 lcd.setCursor(0,0);
                 lcd.print("                ");
                 lcd.setCursor(0,0);
                 lcd.print("Image taken");
                 break;
                 case FINGERPRINT_NOFINGER:
                 lcd.setCursor(0,0);
                 lcd.print("                ");
                 lcd.setCursor(0,0);
                 lcd.print("Place Finger");
                 break;
                 case FINGERPRINT_PACKETRECIEVEERR:
                 lcd.setCursor(0,0);
                 lcd.print("                ");
                 lcd.setCursor(0,0);
                 lcd.print("Comm. error");
                 break;
                 case FINGERPRINT_IMAGEFAIL:
                 lcd.setCursor(0,0);
                 lcd.print("                ");
                 lcd.setCursor(0,0);
                 lcd.print("Imaging error");
                 break;
                 default:
                 lcd.setCursor(0,0);
                 lcd.print("                ");
                 lcd.setCursor(0,0);
                 lcd.print("Unknown error");
                 break;
          }
    }

  // OK success!

    p = finger.image2Tz(2);
    switch (p) 
    {
    case FINGERPRINT_OK:
          lcd.setCursor(0,0);
          lcd.print("                ");
          lcd.setCursor(0,0);
          lcd.print("Image convert");
          break;
          case FINGERPRINT_IMAGEMESS:
          lcd.setCursor(0,0);
          lcd.print("                ");
          lcd.setCursor(0,0);
          lcd.print("Image messy");
          return p;
          case FINGERPRINT_PACKETRECIEVEERR:
          lcd.setCursor(0,0);
          lcd.print("                ");
          lcd.setCursor(0,0);
          lcd.print("Comm error");
          return p;
          case FINGERPRINT_FEATUREFAIL:
          lcd.setCursor(0,0);
          lcd.print("                ");
          lcd.setCursor(0,0);
          lcd.println("Invaild");
          return p;
          case FINGERPRINT_INVALIDIMAGE:
          lcd.setCursor(0,0);
          lcd.print("                ");
          lcd.setCursor(0,0);
          lcd.print("Invalid");
          return p;
          default:
          lcd.setCursor(0,0);
          lcd.print("                ");
          lcd.setCursor(0,0);
          lcd.print("Unknown error");
          return p;
     }
  
    // OK converted!
    lcd.setCursor(0,0);
    lcd.print("                ");
    lcd.setCursor(0,0);
    lcd.print("Creating model");
    lcd.setCursor(0,1);  
    lcd.print(id);
    lcd.setCursor(0,0);
    lcd.print("                ");
    lcd.setCursor(0,1);
    lcd.print("                ");
    p = finger.createModel();
    if (p == FINGERPRINT_OK) 
    {
        lcd.setCursor(0,0);
        lcd.print("                ");
        lcd.setCursor(0,0);
        lcd.print("Prints matched");
    } 
    else if (p == FINGERPRINT_PACKETRECIEVEERR) 
    {
        lcd.setCursor(0,0);
        lcd.print("                ");
        lcd.setCursor(0,0);
        Serial.println("Comm. error");
        return p;
    } 
    else if (p == FINGERPRINT_ENROLLMISMATCH) 
    {
        lcd.setCursor(0,0);
        lcd.print("                ");
        lcd.setCursor(0,0);
        lcd.print("not matched");
        return p;
    } 
    else 
    {
        lcd.setCursor(0,0);
        lcd.print("                ");
        lcd.setCursor(0,0);
        lcd.print("Unknown error");
        return p;
    }   
  
     lcd.setCursor(0,0);
     lcd.print("                ");
     lcd.setCursor(0,0);
     lcd.print("ID "); 
     lcd.setCursor(5,0);
     lcd.print(id);
     p = finger.storeModel(id);
     if (p == FINGERPRINT_OK) 
     {
           lcd.setCursor(0,0);
           lcd.print("                ");
           lcd.setCursor(0,0);
           lcd.print("Stored!");
           return 1;
     } 
           else if (p == FINGERPRINT_PACKETRECIEVEERR)
     {
          lcd.setCursor(0,0);
          lcd.print("                ");
          lcd.setCursor(0,0);
          lcd.print("Comm. error");
          return p;
     } 
     else if (p == FINGERPRINT_BADLOCATION)
     {
          lcd.setCursor(0,0);
          lcd.print("                ");
          lcd.setCursor(0,0);
          lcd.print("loc Error");
          return p;
     } 
     else if (p == FINGERPRINT_FLASHERR) 
     {
          lcd.setCursor(0,0);
          lcd.print("                ");
          lcd.setCursor(0,0);
          lcd.print("Error flash");
          return p;
     } 
     else 
     {
          lcd.setCursor(0,0);
          lcd.print("                ");
          lcd.setCursor(0,0);
          lcd.print("Unknown error");
          return p;
  }   
}

uint8_t getFingerprintID()          //for searching
{
  uint8_t p = finger.getImage();
  switch (p) 
  {
      case FINGERPRINT_OK:
      lcd.setCursor(0,0);
      lcd.print("                ");
      lcd.setCursor(0,0);
      lcd.print("Image taken");
      break;
      case FINGERPRINT_NOFINGER:  
      return 100;
      case FINGERPRINT_PACKETRECIEVEERR:
      lcd.setCursor(0,0);
      lcd.print("                ");
      lcd.setCursor(0,0);
      lcd.print("Comm. error");
      return p;
      case FINGERPRINT_IMAGEFAIL:
      Serial.println("Imaging error");
      lcd.setCursor(0,0);
      lcd.print("                ");
      lcd.setCursor(0,0);
      lcd.print("Imaging error");
      return p;
      default:
      lcd.setCursor(0,0);
      lcd.print("Unknown error");
      lcd.setCursor(0,0);
      lcd.print("                ");
      return p;
  }

  // OK success!

  p = finger.image2Tz();
  switch (p)
  {
          case FINGERPRINT_OK:
          lcd.setCursor(0,0);
          lcd.print("                ");
          lcd.setCursor(0,0);
          lcd.print("Image convert");
          break;
          case FINGERPRINT_IMAGEMESS:
          lcd.setCursor(0,0);
          lcd.print("                ");
          lcd.setCursor(0,0);
          lcd.print("Image messy");
          return p;
          case FINGERPRINT_PACKETRECIEVEERR:
          lcd.setCursor(0,0);
          lcd.print("                ");
          lcd.setCursor(0,0);
          lcd.print("Comm error");
          return p;
          case FINGERPRINT_FEATUREFAIL:
          lcd.setCursor(0,0);
          lcd.print("                ");
          lcd.setCursor(0,0);
          lcd.println("Invaild");
          return p;
          case FINGERPRINT_INVALIDIMAGE:
          lcd.setCursor(0,0);
          lcd.print("                ");
          lcd.setCursor(0,0);
          lcd.print("Invalid");
          return p;
          default:
          lcd.setCursor(0,0);
          lcd.print("                ");
          lcd.setCursor(0,0);
          lcd.print("Unknown error");
          return p;
  }
  
  // OK converted!
  p = finger.fingerFastSearch();
  if (p == FINGERPRINT_OK) 
  {
    lcd.setCursor(0,0);
    lcd.print("                ");
    lcd.setCursor(0,0);
    lcd.print("Match found");
  }
  else if (p == FINGERPRINT_PACKETRECIEVEERR) 
  {
    lcd.setCursor(0,0);
    lcd.print("                ");
    lcd.setCursor(0,0);
    lcd.print("Comm. error");
    return p;
  } 
  else if (p == FINGERPRINT_NOTFOUND)            // No match found
  {
    lcd.setCursor(0,0);
    lcd.print("Not Registered");
    lcd.setCursor(0,0);
    Serial2.write(0);
    delay(500);
    lcd.print("                ");
    return p;
  } 
  else 
  {
    lcd.setCursor(0,0);
    lcd.print("                ");
    lcd.setCursor(0,0);
    lcd.print("Unknown error");
    return p;
  }   
  
  // found a match!
  Serial2.write(finger.fingerID);
  lcd.setCursor(0,0);
  lcd.print("                ");
  lcd.setCursor(0,0);
  lcd.print("Welcome");
  digitalWrite(lock,LOW);
  delay(5000);
  lcd.setCursor(0,0);
  lcd.print("                ");
  digitalWrite(lock,HIGH);
  return finger.fingerID;
}

uint8_t deleteFingerprint(uint8_t id) {
  uint8_t p = -1;  
  p = finger.deleteModel(id);   
}

 void SendMessage()
{
  Serial3.println("AT+CMGF=1");    //Sets the GSM Module in Text Mode
  delay(1000);  // Delay of 1000 milli seconds or 1 second
  Serial3.println("AT+CMGS=\""+no+"\"\r"); // Replace x with mobile number
  delay(1000);
  Serial3.println("Someone Accessed the Lock");// The SMS text you want to send
  delay(100);
  Serial3.println((char)26);// ASCII code of CTRL+Z
  delay(1000);
}
void SendMessagee()
{
  Serial3.println("AT+CMGF=1");    //Sets the GSM Module in Text Mode
  delay(1000);  // Delay of 1000 milli seconds or 1 second
  Serial3.println("AT+CMGS=\""+no+"\"\r"); // Replace x with mobile number
  delay(1000);
  Serial3.println("New User Enrolled");// The SMS text you want to send
  delay(100);
  Serial3.println((char)26);// ASCII code of CTRL+Z
  delay(1000);
}
void SendMessaged()
{
  Serial3.println("AT+CMGF=1");    //Sets the GSM Module in Text Mode
  delay(1000);  // Delay of 1000 milli seconds or 1 second
  Serial3.println("AT+CMGS=\""+no+"\"\r"); // Replace x with mobile number
  delay(1000);
  Serial3.println("A User is Removed");// The SMS text you want to send
  delay(100);
  Serial3.println((char)26);// ASCII code of CTRL+Z
  delay(1000);
}
void SendMessagea()
{
  Serial3.println("AT+CMGF=1");    //Sets the GSM Module in Text Mode
  delay(1000);  // Delay of 1000 milli seconds or 1 second
  Serial3.println("AT+CMGS=\""+no+"\"\r"); // Replace x with mobile number
  delay(1000);
  Serial3.println("OTP to change Registered no. is "+otp);// The SMS text you want to send
  delay(100);
  Serial3.println((char)26);// ASCII code of CTRL+Z
  delay(1000);
}
void SendMessagel()
{
  Serial3.println("AT+CMGF=1");    //Sets the GSM Module in Text Mode
  delay(1000);  // Delay of 1000 milli seconds or 1 second
  Serial3.println("AT+CMGS=\""+no+"\"\r"); // Replace x with mobile number
  delay(1000);
  Serial3.println("OTP to Unlock the lock is "+otp);// The SMS text you want to send
  delay(100);
  Serial3.println((char)26);// ASCII code of CTRL+Z
  delay(1000);
}
