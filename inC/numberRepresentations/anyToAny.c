#include <stdlib.h>
#include <stdio.h>
#include <math.h>
#include <string.h>
#include <stdint.h>

// Author: Pedro Torres
void reverseString(char* string){
  int stringLength = strlen(string);
  for (int i = 0; i<stringLength/2;i++){
    char temp = string[i];
      string[i] = string[stringLength-i-1];
      string[stringLength-i-1] = temp;
  }
}

size_t anyToInteger(char* source, int base, int digitCount) {
    // Implement logic to convert the source string to integer representation
    // Remember to return the integer value

   int64_t repr = 0;
   int64_t power = 1;
    for (int i = digitCount-1; i>=0;i--){
      
      if (source[i]>='0'&&source[i]<='9'){
      repr+=(source[i]-'0')*power;
      }
      else {
        repr+=(source[i]-'A'+10)*power;
      }
      power = power*base;
    }
    return repr;
}

void integerToAny(char* result, size_t repr, int base) {
    // Implement the logic to convert the represented value to any base
    // Store this value as a string in the 'result' array

    int a = 0;
    while (repr>0){
      if((repr%base)>=10){
        result[a++] = (repr%base)-10+'A';
      }
      else {
        result[a++] = (repr%base)+'0';
      }
      repr/=base;
    }
    result[a] = '\0';
    reverseString(result);
    return;
}

int main(int argc, char* argv[]) {
    FILE* fp = fopen(argv[1], "r");
    if (!fp) {
        perror("fopen failed");
        return EXIT_FAILURE;
    }

    int digitCount;
    int sourceBase;
    int destBase;

    if (!fscanf(fp, "%d\n", &digitCount)) {
      perror("reading the input digit count failed");
      exit(EXIT_FAILURE);
    }

    if (!fscanf(fp, "%d\n", &sourceBase)) {
      perror("reading the source base failed");
      exit(EXIT_FAILURE);
    }

    if (!fscanf(fp, "%d\n", &destBase)) {
      perror("reading the destination base failed");
      exit(EXIT_FAILURE);
    }

    char* sourceNum = calloc(sizeof(char), digitCount+1);

    if (!fscanf(fp, "%s\n", sourceNum)) {
      perror("reading the source number");
      exit(EXIT_FAILURE);
    }

    size_t repr;

    repr = anyToInteger(sourceNum, sourceBase, digitCount);

    // Size 65 accounts for the largest digit result (64 bit binary) + 1 null operator
    char* result = (char*)calloc(sizeof(char), 65);

 integerToAny(result, repr, destBase);
 int size = strlen(result);
 for (int b = 0; b<size; b++){
  printf("%c", result[b]);
 }

    // Print string
    /* ... */

    free(sourceNum);
    free(result);

    return EXIT_SUCCESS;
}
