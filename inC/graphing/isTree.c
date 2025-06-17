#include "../graphutils.h" // header for functions to load and free adjacencyList

// A program to determine whether an undirected graph is a tree

// A recursive function that returns true if no cycles found
bool isTreeDFS (
    size_t graphNodeCount,
    AdjacencyListNode* adjacencyList,
    bool* visited,
    graphNode_t parent,
    graphNode_t current
) {

    // First see if current node has already been visited, indicating a cycle found
    /* ... */

    // Current node was not already visited, so now mark it as visited
    /* ... */

    // Now iterate through each of the neighboring graph nodes
    AdjacencyListNode* neighbor = adjacencyList[current].next;
    while (neighbor) {
        if (neighbor->graphNode!=parent) {
            // If the neighbor nodes is not the parent node (the node from which we arrived at current), call DFS
            /* ... */
        }
        neighbor = neighbor->next;
    }

    // All DFS searches from current node found no cycles, so graph is a tree from this node
    return true;
}

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

// next count the number of edges
int col = 0;
int numVertices = 0;
for (int r = 0; r<numEdges; r++){
    for (int c = col; c<numEdges; c++){
        if (array[r][c]==1){
            numVertices++;
        }
    }
    col++;
}

    // READ INPUT FILE TO CREATE GRAPH ADJACENCY LIST
   // AdjacencyListNode* adjacencyList = NULL;
   //int graphNodeCount = adjMatrixToList(argv[1],&adjacencyList);
    /* ... */
   // printf("%d\n%d", graphNodeCount,adjacencyList);
    // Array of boolean variables indicating whether graph node has been visited
  // bool* visited = calloc ( graphNodeCount, sizeof(bool) );
    /* ... */

    /* ... */

   // printf(isTree ? "yes" : "no");
   if(numVertices>=numEdges){
    printf("no");
   }
   else {
    printf("yes");
   }
//freeAdjList(graphNodeCount, adjacencyList);
//free(visited);

    return EXIT_SUCCESS;
}
