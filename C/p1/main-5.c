/********************************************************
 * main.c
 *
 * SER486 Project 1
 * Spring 2020
 * Written By:  Thor Nielsen
 * Modified By:
 *
 * this file implements functions associated with SER486
 * Project 1.  The procedures implemented provides
 * an introduction to timers/counters and will assist
 * in future projects.
 *
 * functions are:
 *    main(void) - calls delay, timer1, rtc, and led
 *    to update time one uart and status of led on atmega328p.
 */

#include "uart.h"
#include "delay.h"
#include "led.h"
#include "timer1.h"
#include "rtc.h"
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
 * NOTE: main will write the time to uart every
 *      half second using timer1. It will check if
 *      the led needs to update its mores code during
 *      each iteration of the while loop. The led update
 *      has a delay method to know when an led update is needed.
 */
int main(void)
{
    uart_init();
    led_init();
    rtc_init();
    timer1_init();

    __builtin_avr_sei();    //Set interrupts
    rtc_set_by_datestr("01/01/2019 00:00:00");
    led_set_blink("--- -.- ");
    uart_writestr("SER486 Project 1 - Thor Nielsen\n\r");

    /* get baseline performance */
    signed long c1=0;
    delay_set(1,10000); while (!delay_isdone(1)) { c1++; }

    /* measure performance with led_update */
    signed long c2=0;
    delay_set(1,10000); while (!delay_isdone(1)) { led_update(); c2++; }

    /* display the results */
    uart_writedec32(c1); uart_writestr(" ");
    uart_writedec32(c2); uart_writestr("\r\n");

    /* Start of loop */
    delay_set(1, 500);
    while(1){
        if(delay_isdone(1)){ //When done rtc will update time on uart
            uart_writestr(rtc_get_date_string());
            uart_writestr("\r");
            delay_set(1, 500);
        }
        led_update(); //checks and updates led state
    }
}
