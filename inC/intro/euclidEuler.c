#include <stdbool.h>
#include <stdlib.h>
#include <stdio.h>
#include <math.h>

bool is_prime(long n){

  if (n<2)
    return false;

  for (long i=2; i<=sqrt(n); i++)
    if ((n%i)==0)
      return false;

  return true;
}

int main(int argc, char* argv[]) {

  // Open the filename given as the first command line argument for reading
  FILE* fp = fopen(argv[1], "r");
  if (!fp) {
    perror("fopen failed");
    return EXIT_FAILURE;
  }

  char buf[256];

  char* string = fgets(buf, 256, fp);
  long x = atol(string);
  long mp = 0;
    for (long p = x; p>1; p/=2){
      if (p%2 != 0){
        if (is_prime(p)==true){
          mp = p;
        }
        break;
      }
        
    }
  if ( mp != 0 ) {
    // Printing in C.
    // %d is the format specifier for integer numbers.
    // \n is the newline character
    printf( "%ld \n", mp );
  } else {
    printf("-1\n");
  }
}