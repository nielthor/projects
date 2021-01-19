/********************************************************
 * tempfsm.c
 *
 * SER486 Project 3
 * Spring 2020
 * Written By:  Thor Nielsen
 *
 * This class implements a finite state machine that provides
 * hysteresis for the device
 * temperature readings
 *
 * functions are:
 *    init() – perform any class-specific initialization, including
 *      setting the state’s initial value
 *
 *    reset() – reset the finite state machine’s state to NORM1.
 *
 *    update() – updates the state of the fsm based on the provided current
 *      temperature and temperature limit arguments. Upon state transitions,
 *      LED blink behavior will be updated (fast blink for critical, slow
 *      blink for warning). Alarms must also be sent according to the
 *      state diagram using the instructor-
 *      provided alarm class functions.
 *          • tcurrent = the device’s current temperature
 *          • tcrit_h = the critical high temperature limit for the device
 *          • twarn_h = the warning high temperature limit for the device
 *          • tcrit_l = the critical low temperature limit for the device
 *          • twarn_l = the warning low temperature limit for the device
 */

 /* required file */
 #include "led.h"
 #include "alarm.h"
 #include "log.h"
 /* define my fsm states */
 #define NORM1 0
 #define NORM2 1
 #define NORM3 3
 #define WARN_HI_1 4
 #define WARN_HI_2 5
 #define CRITICAL_HI 6
 #define WARN_LO_1 7
 #define WARN_LO_2  8
 #define CRITICAL_LO 9
 /* LED states */
 #define LED_CRIT "."
 #define LED_WARN "-"
 #define LED_NORM " "
 /* Alarm state messages*/
#define EVENT_HI_ALARM  0x05
#define EVENT_HI_WARN   0x06
#define EVENT_LO_ALARM  0x07
#define EVENT_LO_WARN   0x08

 unsigned char temp_state;

 /**********************************
 * init() – perform any class-specific initialization, including
 *      setting the state’s initial value
 *
 * arguments:
 *   none
 *
 * returns:
 *   nothing
 *
 * changes:
 *   initializes state
 *
 * NOTE:
 *   none
 */
void tempfsm_init(){
    temp_state = NORM1;
    led_set_blink(LED_NORM);
}

/**********************************
 * reset() – reset the finite state machine’s state to NORM1
 *
 * arguments:
 *   none
 *
 * returns:
 *   nothing
 *
 * changes:
 *   resets fms to NORM1
 *
 * NOTE:
 *   none
 */
void tempfsm_reset(){
    temp_state = NORM1;
}

/**********************************
 * update() – updates the state of the fsm based on the provided current
 *      temperature and temperature limit arguments. Upon state transitions,
 *      LED blink behavior will be updated (fast blink for critical, slow
 *      blink for warning).
 *
 * arguments:
 *   int - current
 *   int - hicrit
 *   int - hiwarn
 *   int - loCrit
 *   int - lowarn
 *
 * returns:
 *   nothing
 *
 * changes:
 *   resets fms to NORM1
 *
 * NOTE:
 *   provided alarm class functions.
 *          • tcurrent = the device’s current temperature
 *          • tcrit_h = the critical high temperature limit for the device
 *          • twarn_h = the warning high temperature limit for the device
 *          • tcrit_l = the critical low temperature limit for the device
 *          • twarn_l = the warning low temperature limit for the device
 */
void tempfsm_update(int current, int hicrit, int hiwarn, int locrit, int lowarn){

    switch(temp_state){

    /* entry normal case */
    case NORM1:
        if(current >= hiwarn){
            /* send alarm event hi warn */
            alarm_send(EVENT_HI_WARN);
            /* add log entry */
            log_add_record(EVENT_HI_WARN);
            led_set_blink(LED_WARN);
            temp_state = WARN_HI_1;
        }
        else if(current <= lowarn){
            /* send alarm event lo warn */
            alarm_send(EVENT_LO_WARN);
            /* add log entry */
            log_add_record(EVENT_LO_WARN);
            led_set_blink(LED_WARN);
            temp_state = WARN_LO_1;
        }
        break;
    /* normal after warning low */
    case NORM2:
        if(current >= hiwarn){
            /* alarm send event hi warn */
            alarm_send(EVENT_HI_WARN);
            /* add log entry */
            log_add_record(EVENT_HI_WARN);
            led_set_blink(LED_WARN);
            temp_state = WARN_HI_1;
        }
        else if(current <= lowarn){
            led_set_blink(LED_WARN);
            temp_state = WARN_LO_1;
        }
        break;
    /* normal after warning high */
    case NORM3:
        if(current <= lowarn){
            /* alarm send event low warn */
            alarm_send(EVENT_LO_WARN);
            /* add log entry */
            log_add_record(EVENT_LO_WARN);
            led_set_blink(LED_WARN);
            temp_state = WARN_LO_1;
        }
        else if(current >= hiwarn){
            led_set_blink(LED_WARN);
            temp_state = WARN_HI_1;
        }
        break;

    /* in high temp warning*/
    case WARN_HI_1:
        if(current >= hicrit){
            /* alarm send event hi alarm */
            alarm_send(EVENT_HI_ALARM);
            /* add log entry */
            log_add_record(EVENT_HI_ALARM);
            led_set_blink(LED_CRIT);
            temp_state = CRITICAL_HI;
        }
        else if(current < hiwarn){
            /* no alarm needed */
            led_set_blink(LED_NORM);
            temp_state = NORM3;
        }
        else{
            led_set_blink(LED_WARN);
        }
        break;

    /* in high temp warning*/
    case WARN_HI_2:
        if(current >= hicrit){
            led_set_blink(LED_CRIT);
            temp_state = CRITICAL_HI;
        }
        else if(current < hiwarn){
            /* no alarm needed */
            led_set_blink(LED_NORM);
            temp_state = NORM3;
        }
        else{
            led_set_blink(LED_WARN);
        }
        break;

    /* in a critical high alarm state */
    case CRITICAL_HI:
        if(current < hicrit){
            led_set_blink(LED_WARN);
            temp_state = WARN_HI_2;
        }
        else{
            led_set_blink(LED_CRIT);
        }
        break;

    /* in low warning temp state */
    case WARN_LO_1:
        if(current <= locrit){
            /* alarm send event critical hi */
            alarm_send(EVENT_LO_ALARM);
            /* add log entry */
            log_add_record(EVENT_LO_ALARM);
            led_set_blink(LED_CRIT);
            temp_state = CRITICAL_LO;
        }
        else if(current > lowarn){
            /* no alarm needed */
            led_set_blink(LED_NORM);
            temp_state = NORM2;
        }
        else{
            led_set_blink(LED_WARN);
        }
        break;

    /* in low warning temp state */
    case WARN_LO_2:
        if(current <= locrit){
            led_set_blink(LED_CRIT);
            temp_state = CRITICAL_LO;
        }
        else if(current > lowarn){
            /* no alarm needed */
            led_set_blink(LED_NORM);
            temp_state = NORM2;
        }
        else{
            led_set_blink(LED_WARN);
        }
        break;

    /* in a critical low alarm state */
    case CRITICAL_LO:
        if(current > locrit){
            led_set_blink(LED_WARN);
            temp_state = WARN_LO_2;
        }
        else{
            led_set_blink(LED_CRIT);
        }
        break;
    }

}
