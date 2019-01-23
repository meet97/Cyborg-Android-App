#define VERTICAL_PIN1 8
#define VERTICAL_PIN2 9
#define ENABLE_VERTICAL 3
#define HORIZONTAL_PIN1 10
#define HORIZONTAL_PIN2 11
#define ENABLE_HORIZONTAL 4
#define ENABLE_ARM 7
#define ARM_PIN1 12
#define ARM_PIN2 13



void setup() {
  Serial.begin(9600);
  Serial3.begin(9600);
  pinMode(VERTICAL_PIN1, OUTPUT);
  pinMode(VERTICAL_PIN2, OUTPUT);
  pinMode(ENABLE_VERTICAL, OUTPUT);
  pinMode(HORIZONTAL_PIN1, OUTPUT);
  pinMode(HORIZONTAL_PIN2, OUTPUT);
  pinMode(ENABLE_HORIZONTAL, OUTPUT);
  pinMode(ENABLE_ARM,OUTPUT);
  pinMode(ARM_PIN1,OUTPUT);
  pinMode(ARM_PIN2,OUTPUT);
}


void vertical_down() {
  digitalWrite(ENABLE_VERTICAL, HIGH);
  digitalWrite(VERTICAL_PIN1, HIGH);
  digitalWrite(VERTICAL_PIN2, LOW);
  delay(800);
  digitalWrite(VERTICAL_PIN2, HIGH);
  digitalWrite(ENABLE_VERTICAL, LOW);
}

void vertical_up() {
  digitalWrite(ENABLE_VERTICAL, HIGH);
  digitalWrite(VERTICAL_PIN1, LOW);
  digitalWrite(VERTICAL_PIN2, HIGH);
  delay(800);
  digitalWrite(VERTICAL_PIN1, HIGH);
  digitalWrite(ENABLE_VERTICAL, LOW);
}


void horizontal_backward() {
  digitalWrite(ENABLE_HORIZONTAL, HIGH);
  digitalWrite(HORIZONTAL_PIN1, HIGH);
  digitalWrite(HORIZONTAL_PIN2, LOW);
  delay(800);
  digitalWrite(HORIZONTAL_PIN2, HIGH);
  digitalWrite(ENABLE_HORIZONTAL, LOW);
}

void horizontal_forward() {
  digitalWrite(ENABLE_HORIZONTAL, HIGH);
  digitalWrite(HORIZONTAL_PIN1, LOW);
  digitalWrite(HORIZONTAL_PIN2, HIGH);
  delay(800);
  digitalWrite(HORIZONTAL_PIN1, HIGH);
  digitalWrite(ENABLE_HORIZONTAL, LOW);
}

void arm_close(){
  digitalWrite(ENABLE_ARM,1);
  digitalWrite(ARM_PIN1,1);
  digitalWrite(ARM_PIN2,0);
  delay(1000);
  digitalWrite(ARM_PIN2,1);
  digitalWrite(ENABLE_ARM,0);
}


void arm_open(){
  digitalWrite(ENABLE_ARM,1);
  digitalWrite(ARM_PIN2,1);
  digitalWrite(ARM_PIN1,0);
  delay(1000);
  digitalWrite(ARM_PIN2,1);
  digitalWrite(ENABLE_ARM,0);
}


void loop() {
  if (Serial3.available()) {
    char inp = Serial3.read();
    Serial.println(inp);
    switch(inp) {
    case 'u':
      vertical_up();
      break;
    case 'd':
      vertical_down();
      break;
    case 'f':
      horizontal_forward();
      break;
    case 'b':
      horizontal_backward();
      break;
      case 'o':
      arm_open();
      break;
      case 'c':
      arm_close();
      break;
    }
  }
}
