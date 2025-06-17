#include "../graphutils.h" // header for functions to load and free adjacencyList

// A program to print the edge list of a graph given the adjacency matrix
int main ( int argc, char* argv[] ) {

    // FIRST, READ THE ADJACENCY MATRIX FILE    
FILE* fp = fopen(argv[1], "r");
    if (!fp) {
        perror("fopen failed");
        exit(EXIT_FAILURE);
    }
char buf[256];
if (!fscanf(fp, "%s\n", buf)) {
        perror("reading the number of jobs failed");
        exit(EXIT_FAILURE);
    }
    int numEdges = atoi(buf);

// make array
int array [numEdges][numEdges];
int row = 0;
while (!feof(fp)){
    if (ferror(fp)){
        printf("error reading file.\n");
        return 1;
    }
    for (int i = 0; i<numEdges;i++){
        if (fscanf(fp,"%d", &array[row][i]) == EOF){
            break;
        }
    }
    row ++;
        if (row == numEdges){
            break;
        }
}
fclose(fp);
int col = 0;
for (int r = 0; r<numEdges; r++){
    for (int c = col; c<numEdges; c++){
        if (array[r][c]==1){
        printf("%d %d\n", r, c);
        }
    }
    col++;
}


    // NEXT, TRAVERSE THE ADJACENCY LIST AND PRINT EACH EDGE, REPRESENTED AS A PAIR OF NODES
    // Example of traversing the adjacency list is in the freeAdjList() function in graphutils.h
    /* ... */

    // NOW, BE SURE TO FREE THE ADJACENCY LIST
    /* ... */
    return EXIT_SUCCESS;
}
