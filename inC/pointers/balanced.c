#include <stdlib.h>
#include <stdbool.h>
#include <stdio.h>

// Struct to hold the open and close braces and the pointer to the next element.
struct element {
    // char open; // not needed
    char close;
    struct element* next;
};

// Append the new element to the start of the stack
// This is one potential way to write this function signature
void push (
    struct element** stack,
    // char open, // not needed
    char close
) {
    struct element* newElement = malloc ( sizeof ( struct element ) );
    // (*newElement).number = number;
    newElement->close = close;
    newElement->next = *stack;
    *stack = newElement;
    return;
}

// Remove element from the top of the stack
char pop ( struct element** stack ) {

if ( (*stack) != NULL ) {
        struct element* temp = *stack;
        char close = temp->close;
        *stack = temp->next;
        free ( temp );
        return close;
    } 
    
    else {
        return'\0';
    }
}

int main(int argc, char* argv[]){

    FILE* fp = fopen(argv[1], "r");
    if (!fp) {
        perror("fopen failed");
        return EXIT_FAILURE;
    }

    struct element* root = NULL;
    bool balanced = false;
    char buff;
    int stop = 0;
    int in = 0;
    while ( fscanf(fp, "%c", &buff)==1 && stop==0) {

        switch(buff) {
            case '<' :
                push(&root,buff);
                in++;
                break;
            case '(' :
                push(&root,buff);
                in++;
                break;
            case '[' :
                 push(&root,buff);
                 in++;
                 break;
            case '{' :
                push(&root,buff);
                in++;
                break;
            case '>' :
             if (pop(&root)=='<'){
                in--;
                break;
            }
            else {
                stop = 1;
                in=1;
            }
            case ')' :
            if (pop(&root)=='('){
                in--;
                break;
            }
            else {
                in = 1;
                stop = 1;
                break;
            }
            case ']' :
            if (pop(&root)=='['){
                in--;
                break;
            }
            else {
                in = 1;
                stop = 1;
                break;
            }
            case '}' :
            if (pop(&root)=='{'){
                in--;
                break;
             }
             else {
                in = 1;
                stop = 1;
                break;
            }
            default :
                printf("Invalid character\n" );
                break;
        }
    }

if (in==0){
    balanced = true;
}
    printf ( balanced ? "yes" : "no" );
    fclose(fp);
    return 0;
}
