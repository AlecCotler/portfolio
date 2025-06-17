#include <stdbool.h>
#include <stdlib.h>
#include <stdio.h>
#include <math.h>

#define EXP_SZ 8
#define FRAC_SZ 23

int main(int argc, char *argv[]) {

    FILE* fp = fopen(argv[1], "r");
    if (!fp) {
        perror("fopen failed");
        return EXIT_FAILURE;
    }

    // SETUP

    // first, read the binary number representation of float point number
    char buff;
   // unsigned int binary = 0;
    int binar[EXP_SZ+FRAC_SZ+1];
    for (int i=0; i<EXP_SZ+FRAC_SZ+1; i++) { // read MSB first as that is what comes first in the file
    fscanf(fp, "%c", &buff);
    if (buff=='1'){
        binar[i]=1;
    }
    else {
        binar[i]=0;
    }
    }

    // float number = *(float *)&binary; // you are not allowed to do this.
    int S;
    if (binar[0]==0){
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
    exp += (multiplier * binar[i]);
    multiplier *= 2;
}
int E = exp-127;
    // M
 double divisor = 2; 
 double m = 1; 
 for (int i = 9; i <EXP_SZ+FRAC_SZ+1; i++ )
{
    m+= (((double)binar[i])/divisor);
    divisor*=2;
} 

    // https://www.tutorialspoint.com/c_standard_library/c_function_ldexp.htm
     double number = ldexp ( m, E );
     number*=S;
    // number = sign ? -number : number;
     printf("%f\n", number);

    return EXIT_SUCCESS;

}
