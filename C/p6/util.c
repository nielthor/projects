/********************************************************
 * util.c
 *
 * SER486 Project 3
 * Spring 2020
 * Written By:  Thor Nielsen
 *
 * The util class is a helper function that checks if the state of temp inputs are valid.
 *
 * functions are:
 *    update_tcrit_hi() – Update the configuration tcrit_hi limit with the specified value.
 *      This function is called by the packet command parser.
 *
 *    update_twarn_hi() – Update the configuration twarn_hi limit with the specified value.
 *      This function is called by the packet command parser.
 *
 *    update_twarn_lo() – Update the configuration twarn_lo limit with the specified value.
 *      This function is called by the packet command parser.
 *
 *    update_tcrit_lo() – Update the configuration tcrit_lo limit with the specified value.
 *      This function is called by the packet command parser.
 */

 /* required to set temp configuration */
 #include "config.h"


 /**********************************
 * update_tcrit_hi(int value) - Update the configuration tcrit_hi limit with the specified value.
 *      This function is called by the packet command parser.
 *
 * arguments:
 *   int value - to be checked
 *
 * returns:
 *    0 if valid else 1
 *
 * changes:
 *   config.hi_alarm
 *
 * NOTE:
 *   Valid inputs for tcrit_hi must be greater than twarn_hi
 *   but less than or equal to 0x3FF
 */
int update_tcrit_hi(int value){
    if(value > config.hi_warn && value <= 0x3FF){
        /* load good value */
        config.hi_alarm = value;
        return 0;
    }
    return 1;
}

 /**********************************
 * update_twarn_hi(int value) - Update the configuration twarn_hi limit with the specified value.
 *      This function is called by the packet command parser.
 *
 * arguments:
 *   int value - warning hi
 *
 * returns:
 *   0 if valid else 1
 *
 * changes:
 *   config.hi_warn
 *
 * NOTE:
 *   Valid inputs for twarn_hi must be greater than twarn_lo
 *   and less than tcrit_h
 */
int update_twarn_hi(int value){
    if(value > config.lo_warn && value < config.hi_alarm){
        /* load good value */
        config.hi_warn = value;
        return 0;
    }
    return 1;
}

 /**********************************
 * update_twarn_lo(int value) - Update the configuration twarn_lo limit with the specified value.
 *      This function is called by the packet command parser.
 *
 * arguments:
 *   int value - warning lo
 *
 * returns:
 *   0 if valid else 1
 *
 * changes:
 *   config.lo_warn
 *
 * NOTE:
 *   Valid inputs for twarn_lo must be greater than tcrit_lo
 *   and less than twarn_hi
 */
int update_twarn_lo(int value){
    if(value > config.lo_alarm && value < config.hi_warn){
        /* load good value */
        config.lo_warn=value;
        return 0;
    }
    return 1;
}

/**********************************
 * update_tcirt_lo(int value) - Update the configuration tcrit_lo limit with the specified value.
 *      This function is called by the packet command parser.
 *
 * arguments:
 *   int value crit_lo
 *
 * returns:
 *   0 if valid else 1
 *
 * changes:
 *   config.lo_alarm
 *
 * NOTE:
 *   Valid inputs for tcrit_lo must be less than twarn_lo.
 *   Returns 0 if there is no error, otherwise, returns 1
 */
int update_tcrit_lo(int value){
    if(value < config.lo_warn){
        /* load good value */
        config.lo_alarm = value;
        return 0;
    }
    return 1;
}
