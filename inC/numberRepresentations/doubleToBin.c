#include <stdbool.h>
#include <stdlib.h>
#include <time.h>
#include <stdio.h>
#include <assert.h>
#include <math.h>

#define EXP_SZ 11
#define FRAC_SZ 52



int main(int argc, char *argv[]) {

    // SETUP

    FILE* fp = fopen(argv[1], "r");
    if (!fp) {
        perror("fopen failed");
        return 0;
    }
    // first, read the number
    double value;
    fscanf(fp, "%lf", &value);
    double absValue = fabs(value);
   // decide if normalized or denormalized
    bool isNormalized = true;
    if (absValue<ldexp(1,-1022)){
        isNormalized = false;
    }
    // decide the sign
    int sign = 0;
    if (value < 0 || (1.0/value)==-INFINITY){
        sign = 1;
    }

    // make the binary array
        int binar[EXP_SZ+FRAC_SZ+1];
        // set the sign field
        binar[0]=sign;
        printf("%d_", binar[0]);
    // decide the exp
        //if denormalized
        int expNumber = 0;
        //if normalized
        if (isNormalized){
                expNumber = (int)((log2(absValue))+1023);
        }
        int expNumber2 = expNumber - 1023;
        // set the exp field
        int expTracker = 1;
            for (int x = 11; x>0;x--){
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
            for (int x = 1; x<=11; x++){
                printf("%d", binar[x]);
            }
            printf("_");
        // set the frac field
        double largestPowerNum;
        if (!isNormalized){
            largestPowerNum = ldexp ( 1, -1023 );
        }
        else if (isNormalized){
            largestPowerNum = ldexp(1,expNumber2);
            absValue -= largestPowerNum;
            largestPowerNum/=2;
        }

        for (int x = 12; x<64; x++){
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
        




//     // the reference solution ('the easy way')
//     // you are not allowed to print from this casted 'ref_bits' variable below
//     // but, it is helpful for validating your solution
//     unsigned long int ref_bits = *(unsigned long int*) &value;

//     // THE SIGN BIT
//     bool sign = value<0.0;
//     printf("%d_",sign);
//     assert (sign == (1&ref_bits>>(EXP_SZ+FRAC_SZ))); // validate your result against the reference

//     // THE EXP FIELD
//     // find the exponent E such that the fraction will be a canonical fraction:
//     // 1.0 <= fraction < 2.0
//     double fraction = sign ? -value : value;

//     signed short trial_exp=(1<<(EXP_SZ-1))-1; // start by assuming largest possible exp (before infinity)
//     // do trial division until the proper exponent E is found
//     /* ... */

//     unsigned short bias = (1<<(EXP_SZ-1))-1;
//     signed short exp = trial_exp + bias;

//     for ( int exp_index=EXP_SZ-1; 0<=exp_index; exp_index-- ) {
//         bool exp_bit = 1&exp>>exp_index;
//         printf("%d",exp_bit);
//         assert (exp_bit == (1&ref_bits>>(exp_index+FRAC_SZ))); // validate your result against the reference
//     }
//     printf("_");
//     // you get partial credit by getting the exp field correct
//     // even if you print zeros for the frac field next
//     // you should be able to pass test cases 0, 1, 2, 3

//     // THE FRAC FIELD
//     // prepare the canonical fraction such that:
//     // 1.0 <= fraction < 2.0
//     /* ... */

//     bool frac_array[FRAC_SZ+1]; // one extra LSB bit for rounding
//     for ( int frac_index=FRAC_SZ; 0<=frac_index; frac_index-- ) {
//         frac_array[frac_index] = false; // frac set to zero to enable partial credit
//         /* ... */
//     }

//     // rounding
//     /* ... */

//     for ( int frac_index=FRAC_SZ-1; 0<=frac_index; frac_index-- ) {
//         bool frac_bit = frac_array[frac_index+1]; // skipping the extra LSB bit for rounding
//         printf("%d", frac_bit);
//         // assert (frac_bit == (1&ref_bits>>frac_index)); // validate your result against the reference
//     }

// }
