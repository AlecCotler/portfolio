#include "../graphutils.h" // header for functions to load and free adjacencyList

// A program to find the minimum spanning tree of a weighted undirected graph using Prim's algorithm

int main ( int argc, char* argv[] ) {

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
double array [numEdges][numEdges];
int rows = 0;
while (!feof(fp)){
    if (ferror(fp)){
        printf("error reading file.\n");
        return 1;
    }
    for (int i = 0; i<numEdges;i++){
        if (fscanf(fp,"%le", &array[rows][i]) == EOF){
            break;
        }
    }
    rows ++;
        if (rows == numEdges){
            break;
        }
}
fclose(fp);
bool hasBeenVisited[numEdges];
int visited[numEdges];
for (int r = 0; r<numEdges;r++){
    hasBeenVisited[r] = false;
     visited[r]=-1;
}
hasBeenVisited[0] = true;
visited[0]=0;
double min = 2147483647; 
int r = 0;
int rowCount = 1;
int col = 0;
int x = 0;
    while (x<rowCount){
        r = 0;
    for (int row = 0; row<rowCount; row++){
        for(int c = 0; c<numEdges; c++){
        if (array[visited[row]][c]!= 0.0 && hasBeenVisited[c]==false && array[visited[row]][c]<min){
            min = array[visited[row]][c];
            col = c;
            r = row;
        }
        }
    }

    visited [rowCount] = col;
        hasBeenVisited[col]=true;
        array[visited[r]][col]=0;
        array[col][visited[r]]=0;
        min = 2147483647;
        printf("%d %d\n", visited[r], col);
        rowCount ++;
        if (rowCount==numEdges){
            x = rowCount;
        }
    }

   
    // READ INPUT FILE TO CREATE GRAPH ADJACENCY LIST
    //AdjacencyListNode* adjacencyList;
    /* ... */

    // An array that keeps track of who is the parent node of each graph node we visit
    // In Prim's algorithm, this parents array keeps track of what is the edge that connects a node to the MST.
  /*  graphNode_t* parents = calloc( graphNodeCount, sizeof(graphNode_t) );
    for (size_t i=0; i<graphNodeCount; i++) {
        parents[i] = -1; // -1 indicates that a nodes is not yet visited; i.e., node not yet connected to MST.
    }

    graphNode_t root = rand()%graphNodeCount;
    parents[root] = root;
*/
    // Prim's algorithm:
    // A greedy algorithm that builds the minimum spanning tree.
    // For a graph with N nodes, the minimum spanning tree will have N-1 edges spanning all nodes.
    // Prim's algorithm starts with all nodes unconnected.
    // At each iteration of Prim's algorithm, the minimum weight node that connects an unconnected node to the connected set of nodes is added to the MST.
   /* for (unsigned iter=0; iter<graphNodeCount-1; iter++) {

        double minWeight = DBL_MAX; // If we find an edge with weight less than this minWeight, and edge connects a new node to MST, then mark this as the minimum weight to beat.
        graphNode_t minSource = -1;
        graphNode_t minDest = -1;

        for (graphNode_t source=0; source<graphNodeCount; source++) {
            if (parents[source]!=-1) { // if already visited
            }
        }
        parents[minDest]=minSource; // we found the minimum weight
    }
    */

    // Using the fully populated parents array, print the edges in the MST.
    /* ... */

    /*free (parents);
    freeAdjList ( graphNodeCount, adjacencyList );
*/
    return EXIT_SUCCESS;
}
