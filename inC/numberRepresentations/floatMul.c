#include <stdbool.h>
#include <stdlib.h>
#include <stdio.h>
#include <assert.h>
#include <math.h>
// https://www.tutorialspoint.com/c_standard_library/limits_h.htm
#include <limits.h>
// https://www.cplusplus.com/reference/cfloat/
#include <float.h>

#define FLOAT_SZ sizeof(float)*CHAR_BIT
#define EXP_SZ (FLOAT_SZ-FLT_MANT_DIG)
#define FRAC_SZ (FLT_MANT_DIG-1)

float binToFloat(int source[]){
     int S;
    if (source[0]==0){
        S = 1;
    }
    else {
        S = -1;
    }
    // E
int exp=0;
int multiplier = 1;
for (int i = 8; i >=1; i-- )
{
    exp += (multiplier * source[i]);
    multiplier *= 2;
}
int E = exp-127;
    // M
 float divisor = 2; 
 float m = 1; 
 if (E == -127){
    m=0;
 }
 for (int i = 9; i <32; i++ )
{
    m+= (((float)source[i])/divisor);
    divisor*=2;
} 
float number = ldexp ( m, E );
     number*=S;
return number;
}




int main(int argc, char *argv[]) {

    // float value = *(float *) &binary; // you are not allowed to do this.
    // unsigned int binary = *(unsigned int*) &value; // you are not allowed to do this.

    FILE* fp = fopen(argv[1], "r");
    if (!fp) {
        perror("fopen failed");
        return EXIT_FAILURE;
    }

    // SETUP
char buff;
      int* multiplier = (int*)malloc(32*sizeof(int)); 
    for (int i=0; i<32; i++) { 
    fscanf(fp, "%c", &buff);
    if (buff == '1'){
        multiplier[i] = 1;
    }
    else 
    {
        multiplier[i] = 0;
    }

    }
    fscanf(fp, "%c", &buff);

int* multiplcand = (int*)malloc(32*sizeof(int)); 
for (int i = 0; i < 32; i++) { 
    fscanf(fp, "%c", &buff);
    if (buff == '1'){
        multiplcand[i] = 1;
    } else {
        multiplcand[i] = 0;
    }
}

    float mult1 = binToFloat(multiplier);
    float mult2 = binToFloat(multiplcand);
    float value = mult1*mult2;
    free(multiplier);
    free (multiplcand);


    float absValue = fabs(value);
   // decide if normalized or denormalized
    bool isNormalized = true;
    if (absValue<ldexp(1,-126)){
        isNormalized = false;
    }
    // decide the sign
    int sign = 0;
    if (value < 0 || (1.0/value)==-INFINITY){
        sign = 1;
    }

    // make the binary array
        int binar[32];
        // set the sign field
        binar[0]=sign;
        printf("%d", binar[0]);
    // decide the exp
        //if denormalized
        int expNumber = 0;
        //if normalized
        if (isNormalized){
                expNumber = (int)((log2(absValue))+127);
        }
        int expNumber2 = expNumber - 127;
        // set the exp field
        int expTracker = 1;
            for (int x = 8; x>0;x--){
                int y = ldexp(1,x-1);
                if (expNumber>=y){
                    binar[expTracker]= 1;
                    expNumber-=y;
                }
                else {
                    binar[expTracker]=0;
                }
                
                expTracker++;
            }
            // print out exp field
            for (int x = 1; x<9; x++){
                printf("%d", binar[x]);
            }
        // set the frac field
        float largestPowerNum;
        if (!isNormalized){
            largestPowerNum = ldexp ( 1, -127 );
        }
        else if (isNormalized){
            largestPowerNum = ldexp(1,expNumber2);
            absValue -= largestPowerNum;
            largestPowerNum/=2;
        }

        for (int x = 9; x<32; x++){
            if (absValue >= largestPowerNum){
                binar[x] = 1;
                absValue -= largestPowerNum;
            }
            else {
                binar[x] = 0;
            }
            largestPowerNum/=2;
            printf("%d",binar[x]);
            }
        }
    // first, read the binary number representation of multiplier
    /* ... */

    // notice that you are reading two different lines; caution with reading
    /* ... */

    // first, read the binary number representation of multiplcand
    /* ... */

    // float product = *(float *) &multiplier * *(float *) &multiplicand; // you are not allowed to print from this.
    // unsigned int ref_bits = *(unsigned int *) &product; // you are not allowed to print from this. But you can use it to validate your solution.

    // // SIGN
    // /* ... */
    // printf("%d_",sign);
    // assert (sign == (1&ref_bits>>(EXP_SZ+FRAC_SZ)));

    // EXP
    // get the exp field of the multiplier and multiplicand
    /* ... */
    // add the two exp together
    /* ... */
    // subtract bias
    /* ... */

    // FRAC
    // get the frac field of the multiplier and multiplicand
    /* ... */
    // assuming that the input numbers are normalized floating point numbers, add back the implied leading 1 in the mantissa
    /* ... */
    // multiply the mantissas
    /* ... */

    // overflow and normalize
    /* ... */

    // rounding
    /* ... */

    // move decimal point
    /* ... */

    // PRINTING
    // // print exp
    // for ( int bit_index=EXP_SZ-1; 0<=bit_index; bit_index-- ) {
    //     bool trial_bit = 1&exp>>bit_index;
    //     printf("%d",trial_bit);
    //     assert (trial_bit == (1&ref_bits>>(bit_index+FRAC_SZ)));
    // }
    // printf("_");

    // // print frac
    // for ( int bit_index=FRAC_SZ-1; 0<=bit_index; bit_index-- ) {
    //     bool trial_bit = 1&frac>>bit_index;
    //     printf("%d",trial_bit);
    //     assert (trial_bit == (1&ref_bits>>(bit_index)));
    // }

    // return(EXIT_SUCCESS);

// }
