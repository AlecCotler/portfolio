#include <stdbool.h>
#include <stdlib.h>
#include <stdio.h>
// https://www.tutorialspoint.com/c_standard_library/limits_h.htm
#include <limits.h>

int main(int argc, char *argv[]) {

    FILE* fp = fopen(argv[1], "r");
    if (!fp) {
        perror("fopen failed");
        return EXIT_FAILURE;
    }
    
    // SETUP

    // first, read the minuend (number to be subtracted from)
    char buff;
    bool minuend[CHAR_BIT]; // suggested that you store bits as array of bools; minuend[0] is the LSB
    for (int i=CHAR_BIT-1; 0<=i; i--) { // read MSB first as that is what comes first in the file
    fscanf(fp, "%c", &buff);
    if (buff == '1'){
        minuend[i] = 1;
    }
    else 
    {
        minuend[i] = 0;
    }


    }

    // notice that you are reading two different lines; caution with reading
    /* ... */
    // second, read the subtrahend (number to subtract)
    char subtrahend[CHAR_BIT]; // suggested that you store bits as array of bools; subtrahend[0] is the LSB
    for (int i=CHAR_BIT-1; 0<=i; i--) { // read MSB first as that is what comes first in the file
    fscanf(fp, " %c", &buff);
    if (buff == '1'){
        subtrahend[i] = 1;
    }
    else 
    {
        subtrahend[i] = 0;
    }


    }


    for (int i=CHAR_BIT-1; 0<=i; i--) { // read MSB first as that is what comes first in the file
    subtrahend[i]=!subtrahend[i];


    }

    // WE WILL DO SUBTRACTION BY NEGATING THE SUBTRAHEND AND ADD THAT TO THE MINUEND

    // Negate the subtrahend
    // flip all bits
    /* ... */

    // add one
    bool carry = true; // to implement the 'add one' logic, we do binary addition logic with carry set to true at the beginning
    for (int i=0; i<CHAR_BIT && carry; i++) { // iterate from LSB to MSB
    int sum = subtrahend[i] + carry;
    subtrahend[i] = sum % 2;
    carry = sum / 2;
    }

    // Add the minuend and the negated subtrahend
    bool difference[CHAR_BIT];
    /* ... */

    // print the difference bit string
    bool carry2 = false;
    for (int i=0; i<=CHAR_BIT-1; i++){
    int sum = minuend[i] + subtrahend[i] + carry2;
    difference[i] = sum % 2;
    carry2 = sum / 2;
        }

    for (int i=CHAR_BIT-1; 0<=i; i--) {
     printf("%d", difference[i]);
    }

    return EXIT_SUCCESS;

}
