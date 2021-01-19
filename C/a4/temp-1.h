/********************************************************
 *temp.h
 *
 * SER486 Assignment 4
 * Spring 2020
 * Written By:  Thor Nielsen
 * Modified By:
 *
 * header file for function declarations
 * declarations:
 *      void temp_init(void)
 *      unsigned int temp_is_data_ready(void)
 *      void temp_start(void)
 *      signed int temp_get(void)
 */
#ifndef _TEMP_H
#define _TEMP_H


/* initialize temp - this function must be called once before
   the serial port can be used.
*/
void temp_init(void);
/* temp ready to read- this function will return a 1 if the temp is ready to
* read and 0 if it is not.
*/
unsigned int temp_is_data_ready(void);
/* temp start- this function will start the process of reading registers.
*/
void temp_start(void);
/* temp read- this function will return a signed integer of the
* on board temp.
*/
signed int temp_get(void);


#endif  /* _TEMP_H */

