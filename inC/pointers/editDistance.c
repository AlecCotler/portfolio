#include <stdlib.h>
#include <stdio.h>
#include <string.h>

int min ( int x, int y, int z ) {
    int a = x;
    if (a>y){
        a = y;
    }
    if (a>z){
        a = z;
    }
    return a;
}


int main(int argc, char* argv[])
{

    FILE* inputFile = fopen(argv[1], "r");
    if (!inputFile) {
        perror("fopen failed");
        return EXIT_FAILURE;
    }

    char source[32];
    char target[32];

    fscanf (inputFile, "%s\n%s", source, target);
    // find length of source and target
    // int source_length;
    // int target_length;
    // for (int i = 0; source[i]!= '\0';i++){
    //     source_length = i;
    // }
    // for (int i = 0; target[i]!= '\0';i++){
    //     target_length = i;
    // }
    // source_length++;
    // target_length++;

    int source_length = strlen(source);
    int target_length = strlen(target);
    
    // make 2d array
    int** array_2d = malloc (sizeof(int*)*(source_length+1));
    for (int i = 0; i< source_length+1; i++){
        array_2d[i] = malloc (sizeof(int)*(target_length+1));
    }

    // populate first row and column of 2d array
    for (int i = 0; i< source_length+1; i++){
        array_2d[i][0] = i;
    }

     for (int i = 0; i< target_length+1; i++){

        array_2d[0][i] = i;

    }


// populate the array
    for (int r = 1; r<source_length+1; r++){
        for (int c = 1; c<target_length+1; c++){
            if (source[r-1]==target[c-1]){
                array_2d[r][c]=array_2d[r-1][c-1];
            }
            else{
                array_2d[r][c]=min(array_2d[r][c-1],array_2d[r-1][c-1],array_2d[r-1][c])+1;
            }
        }
    }
    for (int r = 0; r<source_length+1; r++){
        for (int c = 0; c<target_length+1; c++){
        }
    }
    printf("%d\n", array_2d[source_length][target_length]);
    // free the array
     for (int i = 0; i< source_length +1; i++){
        free(array_2d[i]);
    }
    free (array_2d);

    return EXIT_SUCCESS;

}
