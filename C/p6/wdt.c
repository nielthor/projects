/********************************************************
 * wdt.c
 *
 * SER486 Project 3
 * Spring 2020
 * Written By:  Thor Nielsen
 *
 * The wdt class implements a hardware device design pattern to encapsulate the
 * ATMEGA 328P watchdog timer. Upon initialization, the timer is set for a 2 second
 * timeout period that first generates an interrupt. A second timeout will generate a
 * system reset. Other member functions of this class support resetting the timer, and
 * utilizing the timer to force a system reset. More information on this watchdog timer
 * device can be found in the ATMEGA 328P datasheet.
 *
 * functions are:
 *    init() – This function initializes the watchdog timer for a timeout period of 2 seconds.
 *      Upon timeout, the watchdog will first generate an interrupt, followed by a system
 *      reset. Once init is called, wdt.reset() must be called at least once every two seconds
 *      in order to avoid a watchdog timeout.
 *
 *    reset() – resets the watchdog timer to prevent a timeout
 *
 *    force_restart()– When invoked, this function disables the watchdog interrupt and
 *      waits for the watchdog to timeout (generating a system reset).
 *
 *    void __vector_6() – This function is enabled when the watchdog is initialized. When
 *      invoked, the function turns on the LED, adds a EVENT_WDT to the system event log
 *      and attempts to write back any modified log and configuration information before the
 *      second stage of the watchdog timer forces a system reset.
 *
 */


#include "config.h"
#include "led.h"
#include "log.h"

#define WDTCSR (*((volatile char*)0x60))
#define SREG (*((volatile char*)0x5F))
#define EVENT_WDT       0x02

 /**********************************
 * init() – This function initializes the watchdog timer for a timeout period of 2 seconds.
 *      Upon timeout, the watchdog will first generate an interrupt, followed by a system
 *      reset.
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
 *   Time sensitive needs pragma
 *   Once init is called, wdt.reset() must be called at least once every two seconds
 *      in order to avoid a watchdog timeout.
 */
#pragma GCC push_options
#pragma GCC optimize ("Os")
void wdt_init(){
    /* disable interrupts */
    unsigned char tempReg = SREG;
    __builtin_avr_cli();
    /* watch dog reset */
    __builtin_avr_wdr();
    /* write 1 to wde and wde */
    WDTCSR |= 0x18;
    /* turn off wdt */
    WDTCSR = 0x4F;
    /* enable interrupts */
    SREG = tempReg;

}
#pragma GCC pop_options
 /**********************************
 * reset() – resets the watchdog timer to prevent a timeout
 *
 * arguments:
 *   none
 *
 * returns:
 *   nothing
 *
 * changes:
 *   resets watch dog timer
 *
 * NOTE:
 *   built in function
 */
void wdt_reset(){
    __builtin_avr_wdr();
}


 /**********************************
 * force_restart()– When invoked, this function disables the watchdog interrupt and
 *      waits for the watchdog to timeout
 *
 * arguments:
 *   none
 *
 * returns:
 *   nothing
 *
 * changes:
 *   resets system
 *
 * NOTE:
 *   force a system restart (reset only, no interrupt) by disabling the watchdog
 *   interrupt and waiting for a watchdog timeout.
 */
void wdt_force_restart(){
    while(1){}; //hold until system reset occurs
}


 /**********************************
 * void __vector_6() – This function is enabled when the watchdog is initialized.
 *
 * arguments:
 *   none
 *
 * returns:
 *   nothing
 *
 * changes:
 *   When invoked, the function turns on the LED, adds a EVENT_WDT to the system event log
 *   and attempts to write back any modified log and configuration information before the
 *   second stage of the watchdog timer forces a system reset.
 *
 * NOTE:
 *   force a system restart (reset only, no interrupt) by disabling the watchdog
 *   interrupt and waiting for a watchdog timeout.
 */
void __vector_6(void) __attribute__ ((signal,used, externally_visible));
void __vector_6(void){
    //add wtc timeout log entry and turn on led
    log_add_record(EVENT_WDT);
    led_on();
    //write modiied log entreis to eeprom no isr
    for(int i = 0; i < 16; i++){
        log_update_noisr();
    }
    //write config data to eeprom
    config_update_noisr();

    //wait for system reset
    while(1){};
}
