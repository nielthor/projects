/********************************************************
 * delay.c
 *
 * SER486 Project 1
 * Spring 2020
 * Written By:  Thor Nielsen
 *
 * this file implements functions associated with SER486
 * Project 1.  The procedures implemented
 * sets up a 8 bit timer/counter in the atmega328p
 * it is triggered using interrupt isr 14
 *
 * functions are:
 *    init() - a function initialize registers required for
 *                        isr14 to work
 *
 *    delay_get(unsigned num) - a routine to get the most resent count provided
 *                   by isr14 determided by input selection
 *    delay_set(unsigned int num, unsigned int msec)- a routine to set new delay
 *
 *    delay_isdone(unsigned int num) - checks to see if delay has completed
 *
 */

 /* define registers */
#define TCCROA (*((volatile char*)0x44))
#define TCCR0B (*((volatile char*)0x45))
#define TIMSK0 (*((volatile char*)0x6E))
#define OCR0A (*((volatile char*)0x47))
#define SREG (*((volatile char*)0x5F))

 unsigned int count[2];
 unsigned int limit[2];
 static unsigned char tempReg;
 static int initialized = 0;
/**********************************
 * init()
 *
 * This code initializes the registers
 * for timer0 to work
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
 * NOTE: this uses a prescaler of 256 to get a
 *          1ms delay.  OCR0A is set to 249 to achieve this.
 */
void init(){

    TCCROA|= 0xA2;
    OCR0A = 0xF9;   // set compare to interrupt to 249 ticks
    TCCR0B |= 0x03; //set prescaler to 64
    TIMSK0 |= 0x02;  //set interrupt on compare A

    initialized = 1;


}
void __vector_14(void) __attribute__ ((signal,used, externally_visible));
void __vector_14(void){
    if (count[0] < limit[0]){
        count[0]++;             //delay 0 count increment
    }
    if (count[1] < limit[1]){
        count[1]++;             //delay 1 count increment
    }
}
/**********************************
 * delay_get(unsigned num)
 *
 * This code returns counts of current delay
 *
 * arguments:
 *   none
 *
 * returns:
 *   unsigned of value selected
 *
 * changes:
 *   nothing
 *
 */
unsigned int delay_get(unsigned num){

    unsigned int tempCount = 0;
    tempReg = SREG;             //save state of sreg
    __builtin_avr_cli();        //disable interrupts
    if (num == 0){
        tempCount = count[0];
    }
    else if (num == 1){
        tempCount = count[1];
    }
    SREG = tempReg;             //restore state of sreg
    return tempCount;           //return count based selected delay
}


/* set the counter limit and reset the count for the specified instance */
void delay_set(unsigned int num, unsigned int msec){

    if(initialized != 1){
        init();             //called only on first time delay is set
    }

    tempReg = SREG;
    __builtin_avr_cli();    //disable interrupts

    if(num == 0){           //Sets delay 0
        limit[0] = msec;
        count[0] = 0;
    }
    else if(num == 1){      //Sets delay 1
        limit[1] = msec;
        count[1] = 0;
    }
    SREG = tempReg;         //Restores interrupt reg
}

/* return 1 if the specified instance of the counter has reached its
* limit, otherwise, return 0 */
unsigned int delay_isdone(unsigned int num){

    if(num == 0){                   //checks if delay 0 is done returns 1 if count = limit
        if (count[0] == limit[0]){
            return 1;
        }
    }
    else if(num == 1){              //checks if delay 1 is done returns 1 if count = limit
        if (count[1] == limit[1]){
            return 1;
        }
    }
    return 0; //counts not done
}
