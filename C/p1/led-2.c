/********************************************************
 * led.c
 *
 * SER486 Project 1
 * Spring 2020
 * Written By:  Thor Nielsen
 *
 * this file implements functions associated with SER486
 * Project 1.  The procedures implemented
 * provide led blink message to be set and a
 * FSM to switch between led states.
 *
 * functions are:
 *    led_set_blink(char *msg)  - a function initialize FSM and set the blink
 *                        message that the FSM will iterate through.
 *
 *    led_update() - a FSM used to change the status of the
 *                  state of the led.  The state will be determined
 *                  by the blink message.
 *
 */

#include "delay.h"
#include "led.h"
char* blink_msg = "";
unsigned int blink_pos;
unsigned char blink_state;

/**********************************
 * led_set_blink(char *msg)
 *
 * This code initializes the message for the FSM,
 * its State, message index, delay, and led status
 *
 * arguments:
 *   *msg - the message of the FSM
 *
 * returns:
 *   nothing
 *
 * changes:
 *   nothing
 *
 * NOTE: this reinitialize all states
 */
void led_set_blink(char *msg){
    blink_msg = msg;
    blink_pos = 0;
    blink_state = '1'; //Sets blink state to one for entry
    delay_set(0,0);
    led_off();
}
/**********************************
 * led_update()
 *
 * This is a FSM the will toggle an led on
 * and off.  based on delay it will set or reset
 * the led
 *
 * arguments:
 *   none
 *
 * returns:
 *   nothing
 *
 * changes:
 *   the state of the led
 *
 * NOTE: this implements delay and will turn
 *      on and off the led based message delay
 *      value
 */
void led_update(){
    if(!delay_isdone(0)) return;
    switch(blink_state){
        /* Case 1 will set the hold the led on until delay has
            been reached. It will then choose a 100ms delay if
            the current value is not ' '.  It will increment
            position as needed.*/
    case '1':
        blink_state = '2';
        if(blink_msg[blink_pos] != ' '){
            delay_set(0,100);
            led_off();
        }
        if(blink_msg[blink_pos+1] != 0){
            blink_pos++;
        }
        else{
            blink_pos = 0;
        }
        break;
        /* Case 2 will hold the led off until delay condition has been
            reached.  Once reached it will update the state and value of
            led based on blink msg character at current position.*/
    case '2':
        blink_state = '1';
        if(blink_msg[blink_pos] == ' '){
            delay_set(0,1000);
            led_off();
        }
        else if(blink_msg[blink_pos] == '-'){
            delay_set(0,750);
            led_on();
        }
        else if(blink_msg[blink_pos] == '.'){
            delay_set(0,250);
            led_on();
        }

    }
}
