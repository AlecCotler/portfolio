#include <stddef.h>
#include <stdlib.h>
#include <stdio.h>
#include <assert.h>
#include <complex.h>

/* Markers used to bound trace regions of interest */
volatile char MARKER_START, MARKER_END;
 
void mult(complex*A, complex*B, complex**C, int n, int bsize, int pad)
 {
 int i, j, k, kk, jj;
 complex sum;
 int en = bsize * (n/bsize);
 for (kk = pad; kk < en; kk += bsize) {
    for (jj = pad; jj < en; jj += bsize) {
        for (i = pad; i < n; i++) {
            for (j = jj; (j < jj + bsize)&& (j<n); j++) {
                 sum = (*C)[i*n+j];
                for (k = kk; (k < kk + bsize) && (k<n); k++) {
                    sum += A[i*n+k]*B[k*n+j];
                }
                (*C)[i*n+j] = sum;
            }
        }
    }
 }
 }
int isPrime(int n) {
    if (n <= 1) {
        return 0; 
    }
    
    for (int i = 2; i < n; i++) {
        if (n % i == 0) {
            return 0; 
        }
    }
    
    return 1; 
}
int block(int n){
int x = n;
if (n>3 && isPrime(n)){
    x++;
}
int y = 1;
for (int i = x/2; i>1; i--){
    if (x%i == 0){
        y = i;
    }
}
return y;
}
int main(int argc, char* argv[])
{

    /* Record marker addresses */
    FILE* marker_fp = fopen(".marker","w");
    assert(marker_fp);
    fprintf(marker_fp, "%llx\n%llx",
        (unsigned long long int) &MARKER_START,
        (unsigned long long int) &MARKER_END );
    fclose(marker_fp);

    if (argc!=3) {
        printf("Usage: ./matMul <matrix_a_file> <matrix_b_file>\n");
        exit(EXIT_FAILURE);
    }

    FILE* matrix_a_fp = fopen(argv[1], "r");
    if (!matrix_a_fp) {
        perror("fopen failed");
        return EXIT_FAILURE;
    }
    size_t n;
    int ret = fscanf(matrix_a_fp, "%ld\n", &n);
    assert (ret==1);
    int start = n;
    int pad = 0;
    if (n>3 && isPrime(n)){
        start ++;
        pad ++;
    }
    int blocksize = block(n);
    complex* a = calloc( start*start, sizeof(complex) );
    for ( size_t i=pad; i<start; i++ ) {
        for ( size_t k=pad; k<start; k++ ) {
            double real, imag;
            ret = fscanf(matrix_a_fp, "(%lf%lfj) ", &real, &imag);
            assert (ret==2);
            // printf("real=%lf\n",real);
            // printf("imag=%lf\n",imag);
            a[i*start+k] = CMPLX(real, imag);
        }
        ret = fscanf(matrix_a_fp, "\n");
    }
    fclose(matrix_a_fp);
    FILE* matrix_b_fp = fopen(argv[2], "r");
    if (!matrix_b_fp) {
        perror("fopen failed");
        return EXIT_FAILURE;
    }
    size_t m;
    ret = fscanf(matrix_b_fp, "%ld\n", &m);
    assert (ret==1);
    assert( n==m );
    complex* b = calloc( start*start, sizeof(complex) );
    for ( size_t k=pad; k<start; k++ ) {
        for ( size_t j=pad; j<start; j++ ) {
            double real, imag;
            ret = fscanf(matrix_b_fp, "(%lf%lfj) ", &real, &imag);
            assert (ret==2);
            // printf("real=%lf\n",real);
            // printf("imag=%lf\n",imag);
            b[k*start+j] = CMPLX(real, imag);
        }
        ret = fscanf(matrix_b_fp, "\n");
    }
    fclose(matrix_b_fp);

    complex* c = calloc( start*start, sizeof(complex) );
    MARKER_START = 211;
    mult(a,b,&c,start,blocksize, pad);
    MARKER_END = 211;

    for ( size_t i=pad; i<start; i++ ) {
        for ( size_t j=pad; j<start; j++ ) {
            if (cimag(c[i*start+j])<0) {
                printf( "(%.12lf%.12lfj) ", creal(c[i*start+j]), cimag(c[i*start+j]) );
            } else {
                printf( "(%.12lf+%.12lfj) ", creal(c[i*start+j]), cimag(c[i*start+j]) );
            }
        }
        printf( "\n" );
    }

    free(c);
    free(b);
    free(a);
    exit(EXIT_SUCCESS);

}
