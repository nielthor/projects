/********************************************************
 * main.c
 *
 * SER486 Assignment 4
 * Spring 2020
 * Written By:  Thor Nielsen
 * Modified By:
 *
 * this file implements functions associated with SER486
 * homework assignment 4.  The procedures implemented
 * provide temperature console output support for
 * future SER 486 assignments.
 *
 * functions are:
 *    main(void) - a function to call temp.c functions
 *          that initializes and reads temperature from
 *          the arduino board.
 */

 /* add required library */
#include "simpledelay.h"
#include "simpleled.h"
#include "uart.h"
#include "temp.h"
/* used for while delay */
unsigned int tempDataReady = 0;
void uart_writedec32(long str);
void uart_writestr(char* str);

/**********************************
 * main(void)
 *
 * Outputs:There are no outputs only calls to output functions
 *
 * arguments:void
 *
 * returns:nothing
 *
 * changes:none
 *
 * NOTE: main will write the temperature to uart every
 *      second and run indefinitely. The temp is writes
 *      using the provided uart_writedec32 function
 */
int main(void)
{
    /* initialize registers */
    led_init();
    uart_init();
    temp_init();
    /* write name */
    uart_writestr("SER486 HW4 - Thor Nielsen\n\r");

    /* Loop used to write temp every second */
    while(1){
        temp_start();
        /* waits for temp to be ready */
        while(tempDataReady == 0){
            tempDataReady = temp_is_data_ready();
        }
        /* reset temp ready */
        tempDataReady = 0;
        /* print temp */
        uart_writedec32((long)temp_get());
        uart_writestr("\n\r"); /* new line feed for next call to write uart */
        delay(1000); //delay 1sec
    }

}
