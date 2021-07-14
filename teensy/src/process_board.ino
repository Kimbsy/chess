#define R0 12
#define R1 9
#define R2 14
#define R3 8
#define R4 3
#define R5 15
#define R6 5
#define R7 0

#define C0 4
#define C1 10
#define C2 11
#define C3 1
#define C4 13  // this is the built-in led, might wanna skip it
#define C5 2
#define C6 6
#define C7 7

const byte rows[] = {
    R0, R1, R2, R3, R4, R5, R6, R7
};

const byte cols[] = {
    C0, C1, C2, C3, C4, C5, C6, C7
};

byte board[] = {0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0};

int imageId = 0;

void setup() {
    Serial.begin(9600);

    for (int i = 0; i < 8; i++) {
        pinMode(rows[i], OUTPUT);
        pinMode(cols[i], OUTPUT);
        digitalWrite(rows[i], LOW);
        digitalWrite(cols[i], LOW);
    }
}

void loop() {
    draw(board);

    if (Serial.available() > 64) {
        for (int i = 0; i < 8; i++) {
            int value = 0;
            for (int j = 0; j < 8; j++) {
                value *= 2;
                if (Serial.read() != ' ') value++;
            }
            board[i] = (byte) value;
        }
        Serial.read();
    }
}

void draw(byte image[]) {
    for (byte i = 0; i < 8; i++) {
        // select a row
        digitalWrite(rows[i], LOW);
        for (byte j = 0; j < 8; j++) {
            // write the column data
            digitalWrite(cols[j], (image[i] >> j) & 0x01);
        }
        delay(1);
        // deselect the row
        digitalWrite(rows[i], HIGH);
    }
}
