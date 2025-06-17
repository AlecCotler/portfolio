#include <stdlib.h>
#include <stdio.h>
#include <stdbool.h>

// Selection sort based approach to sorting jobs

int main(int argc, char* argv[]) {

    // First peek at how many jobs and timeslots there are
    FILE* fp = fopen(argv[1], "r");
    if (!fp) {
        perror("fopen failed");
        exit(EXIT_FAILURE);
    }

    char buf[256];

    // Read the number of jobs to be scheduled
    if (!fscanf(fp, "%s\n", buf)) {
        perror("reading the number of jobs failed");
        exit(EXIT_FAILURE);
    }
    int jobcount = atoi(buf);

    // Next read the maximum number of timeslots
    if (!fscanf(fp, "%s\n", buf)) {
        perror("reading the number of timeslots failed");
        exit(EXIT_FAILURE);
    }
    int timeslots = atoi(buf);

    // We've read the number of timeslots so close the file and start over
    fclose(fp);
  
  // instantiate arrays
  int array_size = jobcount;
  char* job_array = malloc(array_size*sizeof(char));
    unsigned int* start_array = malloc(array_size*sizeof(unsigned int));
  unsigned int* end_array = malloc(array_size*sizeof(unsigned int));
  int index = 0;

    for (unsigned int slot=0; slot<timeslots; slot++) {

        FILE* fp = fopen(argv[1], "r");
        if (!fp) {
            perror("fopen failed");
            exit(EXIT_FAILURE);
        }

        char buf[256];

        // First read the number of jobs to be scheduled
        if (!fscanf(fp, "%s\n", buf)) {
            perror("reading the number of jobs failed");
            exit(EXIT_FAILURE);
        }

        // Next read the maximum number of timeslots
        if (!fscanf(fp, "%s\n", buf)) {
            perror("reading the number of timeslots failed");
            exit(EXIT_FAILURE);
        }
        // Now read the rest of the file
        for ( int line=0; line<jobcount; line++ ) {

            if (!fgets(buf, 256, fp)) {
                perror("reading a line for a job failed");
                exit(EXIT_FAILURE);
            }
            char job;
            unsigned int start;
            unsigned int end;

            if (!sscanf(buf, "%c %d %d", &job, &start, &end)) {
                perror("parsing a line for a job failed");
                exit(EXIT_FAILURE);
            }
           // order jobs by end time and put into arrays
            if ( end==slot ) {
                job_array[index]=job;
                start_array[index]=start;
                end_array[index]=end;
                index+=1;
            }

        }
        fclose(fp);

    }
    // go through arrays and print out the correct jobs
    unsigned int current_endtime = end_array[0];
    unsigned int previous_endtime = 0;
    char earliest_starttime = 0;
    unsigned int index_of_earliest_starttime = 0;
    unsigned int earliest = start_array[0];
    bool valid = true;
    for (int i = 0; i<index; i++){
        if (start_array[i]!= 0){
            if (end_array[i]==current_endtime){
                if (start_array[i]<= earliest && start_array[i]>previous_endtime){
                    earliest_starttime = job_array[i];
                    current_endtime = end_array[i];
                    index_of_earliest_starttime = i;
                    earliest = start_array[i];
                    valid = true;
                }

            }
            if (end_array[i]!= current_endtime){
                if (valid&& earliest_starttime!= (char)0){
                    printf("%c\n", earliest_starttime);
                    previous_endtime = end_array[index_of_earliest_starttime];
                    valid = false;
                }
                if (start_array[i]>previous_endtime){
                    earliest_starttime = job_array[i];
                    current_endtime = end_array[i];
                    index_of_earliest_starttime = i;
                    earliest = start_array[i];
                    valid = true;
                }
            }
            }
        }
        if (valid&& earliest_starttime!=(char)0){
            printf("%c\n", earliest_starttime);
        }            
        
        free(job_array);
        free(start_array);
        free(end_array);
    exit(EXIT_SUCCESS);
}
