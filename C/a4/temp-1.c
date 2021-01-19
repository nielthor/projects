/********************************************************
 * temp.c
 *
 * SER486 Assignment 4
 * Spring 2020
 * Written By:  Thor Nielsen
 * Modified By:
 *
 * this file implements functions associated with SER486
 * homework assignment 4.  The procedures implemented
 * to setup, start and read the internal temperature
 *
 * functions are:
 *    temp_init(void) - a function to call temp.c functions
 *          that initializes registers needed to read
 *          the arduino temperature.
 *
 *      temp_start(void) - a function used to start the
 *          successive approximation ADC. Set ADSC::1b
 *
 *      temp_is_data_ready(void) - a function used to check
 *          if the ADC has completed its conversion.  Returns
 *          a 1 when complete (ADSC returns to 0).
 *
 *      signed int temp_get(void) - a function returns a calculated
 *          value that represents the temperature of the arduino
 *          in Celsius
 */

 /* define registers */
 #define ADMUX (*((volatile char*)0x7C))
 #define ADCSRA (*((volatile char*)0x7A))
 #define ADCL (*((volatile char*)0x78))
 #define ADCH (*((volatile char*)0x79))

/**********************************
 * temp_init(void)
 *
 * This code initializes ADMUX and ADCSRA registers.
 *
 * arguments:
 *   none
 *
 * returns:
 *   nothing
 *
 * changes:
 *   nothing
 *
 * NOTE: this sets ADMUX to a reference voltage of 1.1v
 *       and single ended input to temperature sensor
 *       ADCSRA enable ADC and sets divisor to 64.
 */
void temp_init(void){
    ADMUX = 0xC8;
    ADCSRA = 0x86;
}
/**********************************
 * temp_is_data_ready(void)
 *
 * This code is used to check if the successive
 * approximation ADC is ready
 *
 * arguments:
 *   none
 *
 * returns:
 *   0x01 when complete
 *
 * changes:
 *   nothing
 *
 * NOTE: this looks at ADCSRC::ADSC when complete
 *       ADSC will return to 0x00
 */
unsigned int temp_is_data_ready(void){
    while((ADCSRA & 0x40) > 0);
    return 0x01;
}
/**********************************
 * temp_start(void)
 *
 * This code is used t0 start the ADC conversion
 *
 * arguments:
 *   none
 *
 * returns:
 *   nothing
 *
 * changes:
 *   ADCSRC::ADSC 1b
 *
 * NOTE: this sets ADSC to 1b to start the ADC
 */
void temp_start(void){
    ADCSRA |= 0x40;
}
/**********************************
 * temp_get(void)
 *
 * This code gets the ADC conversion
 *
 * arguments:
 *   none
 *
 * returns:
 *   signed int of calculated temperature in Celsius
 *
 * changes:
 *   nothing
 *
 * NOTE: this will read ADCL then ADCH and put them in
 *       calculation to produce temp
 */
signed int temp_get(void){
    unsigned int tmpL = ADCL; /* Unsigned int so tmpl does not role over */
    int tmpH = ADCH; /* only using bits 1&2  to to make 10bit value*/
    unsigned int temp;
    temp = (((0x03FF & ((tmpH<<8)|tmpL))* 101)/100)-273;
    return temp;
}

