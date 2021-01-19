/********************************************************
 * timer1.c
 *
 * SER486 Project 1
 * Spring 2020
 * Written By:  Thor Nielsen
 *
 * this file implements functions associated with SER486
 * Project 1.  The procedures implemented
 * sets up a 16 bit timer/counter in the atmega328p
 * it is triggered using interrupt isr 11
 *
 * functions are:
 *    timer1_init()  - a function initialize registers required for
 *                        isr11 to work
 *
 *    timer1_get() - a routine to get the most resent count provided
 *                   by isr11
 *    timer1_clear() - a routine to clear the current count
 *
 *    __vector_11(void) - a interrupt procedure to increment the count
 *
 */

#define TCCR1A (*((volatile char*)0x80))
#define TCCR1B (*((volatile char*)0x81))
#define TCCR1C (*((volatile char*)0x82))
#define TIMSK1 (*((volatile char*)0x6F))
#define OCR1AL (*((volatile char*)0x88))
#define OCR1AH (*((volatile char*)0x89))
#define SREG (*((volatile char*)0x5F))

unsigned long timer1Count;
static unsigned long count;
static unsigned char tempReg;
/**********************************
 * timer1_init()
 *
 * This code initializes the registers
 * for timer1 to work
 *
 * arguments:
 *   none
 *
 * returns:
 *   nothing
 *
 * changes:
 *   the required registers to work as a ctc
 *
 * NOTE: this uses a prescaler of 1024 to get a
 *          1s delay.  OCR1A is set to 15,624 to achieve this.
 */
void timer1_init(){
    TCCR1A |= 0x80;
    TCCR1B |= 0x08;
    OCR1AL = 0x08;
    OCR1AH = 0x3D;
    TCCR1C |= 0xC0;
    TCCR1B |= 0x05;
    TIMSK1 |= 0x02;
}

/**********************************
 * timer1_get()
 *
 * This code gets the current count
 *
 * arguments:
 *   none
 *
 * returns:
 *   unsigned long of current count
 *
 * changes:
 *   nothing
 *
 */
unsigned long timer1_get(){
    tempReg = SREG;         //save state of sreg
    __builtin_avr_cli();
    timer1Count = count;    //get temp value of count for rtc
    SREG = tempReg;         // restore sreg
    return timer1Count;     //return count to rtc
}

/**********************************
 * imer1_clear()
 *
 * This code clears current count
 *
 * arguments:
 *   none
 *
 * returns:
 *   nothing
 *
 * changes:
 *   nothing
 */
void timer1_clear(){
    tempReg = SREG;
    __builtin_avr_cli();
    count = 0;              //clear count
    SREG = tempReg;
}

void __vector_11(void) __attribute__ ((signal,used, externally_visible));
void __vector_11(void){
    count++;
}
