#include "../graphutils.h"
bool cycleFound = false;
// stack implementation
 struct element {
    // char open; // not needed
    graphNode_t graphNode;
    struct element* next;
};




// Append the new element to the start of the stack
// This is one potential way to write this function signature
void push (
    struct element** stack,
    // char open, // not needed
   graphNode_t graphNode
) {
    struct element* newElement = malloc ( sizeof ( struct element ) );
    // (*newElement).number = number;
    newElement->graphNode = graphNode;
    newElement->next = *stack;
    *stack = newElement;
    return;
}




// Remove element from the top of the stack
graphNode_t pop ( struct element** stack ) {




if ( (*stack) != NULL ) {
        struct element* temp = *stack;
        graphNode_t graphNode = temp->graphNode;
        *stack = temp->next;
        free ( temp );
        return graphNode;
    }
   
    else {
        return'\0';
    }
}

// A program to find a cycle in a directed graph

// You may use DFS or BFS as needed
/* ... */
// void dfs(int graphNodeCount, int source){
// int x = source;
// int* visitedVertices = (int*)malloc(graphNodeCount*sizeOf(int*));
// for (int i = 0;i<graphNodeCount;i++){
//     visitedVertices = 0;
// }
// dfs(graphNodeCount,source);

// }

void printCycle(struct element** stack, graphNode_t startingPointer) {
    if (*stack == NULL)
        return;

    graphNode_t print = pop(stack);
    if (print == startingPointer) {
        cycleFound = true;
        printf("%ld ", print);
        return;
    }

    printCycle(stack, startingPointer);
    printf("%ld ", print);
}
 bool isCyclicUtil(int i, bool visited[], bool recStack[], AdjacencyListNode* adjacencyList, int 
graphNodeCount, struct element** stack, int* startingPointer)
    {
 
        // Mark the current node as visited and
        // part of recursion stack
        if (recStack[i]){
            *startingPointer = i;
            return true;
        }
 
        if (visited[i])
            return false;

        if(cycleFound)
            return true;
 
        visited[i] = true;
        recStack[i] = true;
        AdjacencyListNode* current = &adjacencyList[i]; 
        push(stack,current->graphNode);
        while (current !=NULL) {
            current = current->next;
            if (current!=NULL && isCyclicUtil(current->graphNode, visited, recStack, adjacencyList, graphNodeCount, stack, startingPointer )){           
                if (!cycleFound){
                cycleFound = true;
                 printCycle(stack, *startingPointer);
                return true;
                }
            }
        }
        recStack[i] = false;
        pop(stack);
        return false;
    }

     bool isCyclic(AdjacencyListNode* adjacencyList, int graphNodeCount)
    {
        if (cycleFound){
            return true;
        }
        // Mark all the vertices as not visited and
        // not part of recursion stack
        bool* visited = (bool*)malloc(graphNodeCount*(sizeof(bool)));
        bool* recStack = (bool*)malloc(graphNodeCount*(sizeof(bool))); 
        int* startingPointer = (int*)malloc(1*sizeof(int));
        *startingPointer = 0;
        for (int i = 0; i<graphNodeCount; i++){
            visited[i]=false;
            recStack[i]= false;

        }
        struct element* stack = NULL;
        // Call the recursive helper function to
        // detect cycle in different DFS trees
        for (int i = 0; i < graphNodeCount; i++){
            if (isCyclicUtil(i,visited, recStack, adjacencyList, graphNodeCount, &stack, startingPointer)){
                free (visited);
                free (recStack);
                free (startingPointer);
                for (int x = 0; x<graphNodeCount; x++){
                pop(&stack);
            }
            free(stack);
                return true;
                // printCycle(stack)
            }
        }
             free (visited);
            free (recStack);
            for (int x = 0; x<graphNodeCount; x++){
                pop(&stack);
            }
            free(stack);
            free (startingPointer);
        return false;
    }

int main ( int argc, char* argv[] ) {

    // READ INPUT FILE TO CREATE GRAPH ADJACENCY LIST
    AdjacencyListNode* adjacencyList;
    int graphNodeCount = adjMatrixToList(argv[1],&adjacencyList);
    bool Cyclic = false;
    Cyclic = isCyclic(adjacencyList, graphNodeCount);
    for (unsigned source=0; source<graphNodeCount; source++) {
        /* ... */
    }
    if (!Cyclic) { printf("DAG\n"); }

    freeAdjList ( graphNodeCount, adjacencyList );
    return EXIT_SUCCESS;
}
