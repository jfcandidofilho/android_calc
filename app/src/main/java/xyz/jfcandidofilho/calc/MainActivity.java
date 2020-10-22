package xyz.jfcandidofilho.calc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * App's Main Activity.
 * @author J.F. Candido Filho
 * @version 1.0.0
 * @since VersionCode 1
 */
public class MainActivity extends AppCompatActivity {

    /**
     * TAG is an argument necessary for Log.d() to resolve the method being called. Useful in
     * debugging.
     * @since VersionCode 1
     */
    private static final String TAG = MainActivity.class.getSimpleName();

    /**
     * Holds the result of any operation performed.
     * @since VersionCode 1
     */
    public String result;

    /**
     * Holds the value that is the second or the single operand of an operation.
     * @since VersionCode 1
     */
    public String value;

    /**
     * Holds the operation to be executed.
     * @since VersionCode 1
     */
    public String op;

    /**
     * Holds the value converted to Double for mathematical purposes.
     * @since VersionCode 1
     */
    public Double value_holder;

    /**
     * Holds the result converted to Double for mathematical purposes.
     * @since VersionCode 1
     */
    public Double result_holder;

    /**
     * Flag that tells the code that the number to be operated is still being typed by the user.
     * @since VersionCode 1
     */
    public Boolean still;

    /**
     * Flag that tells that a comma was pressed, issuing now decimal numbers.
     * @since VersionCode 1
     */
    public Boolean comma;

    /**
     * Flag that tells that the right decimal zeroes should be removed as they were not typed by the
     * user.
     * @since VersionCode 1
     */
    public Boolean comma_zero;

    /**
     * Flag that tells the number to be typed could be negative as an operation was just typed.
     * Except if a comma was typed, then it isn't activated and the number will never be negative.
     * @since VersionCode 1
     */
    public Boolean might_negate;

    /**
     * Flag that tells the number being typed is negative.
     * @since VersionCode 1
     */
    public Boolean negative;

    /**
     * Flag that tells the number to be typed will have it's square root calculated.
     * @since VersionCode 1
     */
    public Boolean square_root;

    /**
     * Constructor for the main Activity.
     * @param savedInstanceState The state of the Application saved in a Bundle object.
     * @since VersionCode 1
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Resets everything at startup
        this.clear( null );

    }

    /**
     * Clear every memory location used. Resets every visual output to the user.
     * @param view The view that invoked it
     * @since VersionCode 1
     */
    public void clear( View view ){

        // DEBUG
        Log.d( TAG, "S:VALUE: " + this.value );
        Log.d( TAG, "S:OP: " + this.op );
        Log.d( TAG, "S:RESULT: " + this.value );

        // Resets everything to starting values
        this.result = "0";
        this.value = "0";
        this.op = "";

        // Reset the "still building number" flag
        this.still = true;

        // Resets the flags about decimal numbers
        this.comma = false;
        this.comma_zero = false;

        // Resets the flags about negative numbers
        this.might_negate = false;
        this.negative = false;

        // Resets the square root flag
        this.square_root = false;

        // Sets the screens of the calculator
        ((TextView) findViewById( R.id.calc_screen )).setText( "0" );
        ((TextView) findViewById( R.id.op_screen )).setText( " " );

        // DEBUG
        Log.d( TAG, "E:VALUE: " + this.value );
        Log.d( TAG, "E:OP: " + this.op );
        Log.d( TAG, "E:RESULT: " + this.value );

    }

    /**
     * Removes decimal indicator if the decimal are just one or more zeroes. It maintains the
     * decimals if they are necessary (non zeroes).
     * @param number A number converted to String to be checked
     * @return The number without decimals or zeroes to the right of the decimal part.
     * @since VersionCode 1
     */
    public String remove_unnecessary_decimal( String number ){

        // Removes unnecessary zeroes to the right of decimals if necessary
        number = remove_right_decimal_zeroes( number );

        // If there is a string number that contains a dot at the rightest position
        if( number.length() > 0 && number.charAt( number.length() - 1 ) == '.' )

            // Removes the dot
            number = number.substring( 0, number.length() - 1 );

        // Returns the result, be it the original or (de-zeroed and/or de-dotted)
        return number;

    }

    /**
     * Removes right zeroes in the decimal portion of a number. The first non-zero or comma is the
     * limit and finishes the process.
     * @param number A number converted to String to be checked
     * @return The number without right zeroes in the decimal part
     * @since VersionCode 1
     */
    public String remove_right_decimal_zeroes( String number ){

        // If there is a string number that contains a dot
        if( number.length() > 0 && number.contains( "." ) ){

            // Loop through if the rightest digit in string number is a zero
            while(true) { if( number.charAt( number.length() - 1 ) == '0' ){

                // Remove the found zero
                number = number.substring( 0, number.length() - 1 );

            // Get out of the loop
            } else break; }

        // Returns the result, be it the original or de-zeroed
        } return number;

    }

    /**
     * Sets the number acting as operand, while verifying flags and changing flags.
     * @param view The view that invoked it
     * @since VersionCode 1
     */
    public void setNumber( View view ) {

        // DEBUG
        Log.d( TAG, "S:VALUE: " + this.value );
        Log.d( TAG, "S:OP: " + this.op );
        Log.d( TAG, "S:RESULT: " + this.value );

        // Sets the number as negative if negative flag is set
        String num = (this.negative ? "-" : "") + ((Button) view).getText().toString();

        // Verifies if the number is still being typed
        if( still ){

            // Verifies if the number is decimal
            if( comma ) {

                // Verifies if there is no need to add the dot in the number
                if( this.value.contains( "." ) ) {

                    // Verifies if the number has unnecessary right zeroes in its decimal part.
                    if( comma_zero ) {

                        // Removes the unnecessary zeroes if the flag for first run is set
                        this.value = remove_right_decimal_zeroes( this.value );

                        // Unset the flag
                        comma_zero = false;

                    }

                    // Concatenate the new number as part of the decimal (most right number)
                    this.value = this.value.concat( num );

                // Add the dot into the number that was a pure integer
                } else {

                    // Adds the dot and the new number to the rightest side of the decimal part
                    this.value = this.value.concat( "." + num );

                    // Unset the flag that allows removal of right zeroes not typed by the user
                    comma_zero = false;

                }

            // The number isn't a decimal
            } else {

                // The number is added as the unit of the integer while the rest shift left by 1
                this.value_holder = ( Double.parseDouble( value ) * 10 ) + Double.parseDouble( num );

                // Stores the string form of the number
                this.value = this.value_holder.toString();

            }

        // The number is the first (maybe the only one) digit of an operand
        } else {

            // Stores the operand being formed
            this.value = num;

            // Sets the flag that tells it is being constructed still
            this.still = true;

        }

        // The number shouldn't receive negative symbols anymore if a number is typed ...
        this.negative = false;

        // ... Unless the negation symbol is typed again
        this.might_negate = false;

        // Shows the decimal part if a comma was typed despite decimals being just zeroes - as they
        // are the user input
        if( comma ) ((TextView) findViewById( R.id.calc_screen )).setText( this.value );

        // Otherwise, shows only non-zero decimals at their rightest
        else ((TextView) findViewById( R.id.calc_screen )).setText( remove_unnecessary_decimal( this.value ) );

        // DEBUG
        Log.d( TAG, "E:VALUE: " + this.value );
        Log.d( TAG, "E:OP: " + this.op );
        Log.d( TAG, "E:RESULT: " + this.value );

    }

    /**
     * Sets the operation while verifying and changing flags.
     * @param view The view that invoked it
     * @since VersionCode 1
     */
    public void setOperation( View view ){

        // DEBUG
        Log.d( TAG, "S:VALUE: " + this.value );
        Log.d( TAG, "S:OP: " + this.op );
        Log.d( TAG, "S:RESULT: " + this.value );

        // Gets the operation set by the user
        String op = ((Button) view).getText().toString();

        // Shows the operation set by the user
        ((TextView) findViewById( R.id.op_screen )).setText( op );

        // In case the operation is a comma (dot)
        if( op.equals(",") ) {

            // The number is still being formed
            this.still = true;

            // The comma flag means the number is a decimal
            this.comma = true;

            // The comma_zero flag means there might be unnecessary decimal zeroes by default
            this.comma_zero = true;

        // In case operation is setting a negative number
        } else if( op.equals("-") && this.might_negate ) {

            // The negative flag is raised to negate the next operand
            this.negative = true;

        // In case the operation is the square root
        } else if( op.equals("√") ){

            // Raise its flag to take the square root of the next operand
            this.square_root = true;

        // In other cases
        } else {

            // Calculate the current result so far despite the next operand (cascading operations)
            equal( null );

            // Set the current operation in its screen
            this.op = op;

            // The next operand can be negative if the very next action raises an operation, meaning
            // the user selected an operation and than negated the operand before being formed
            this.might_negate = true;

            // The previous operand finished its formation (operation selected)
            this.still = false;

            // There is no comma since there is no operand being formed.
            this.comma = false;
            this.comma_zero = false;

        }

        // DEBUG
        Log.d( TAG, "E:VALUE: " + this.value );
        Log.d( TAG, "E:OP: " + this.op );
        Log.d( TAG, "E:RESULT: " + this.value );

    }

    /**
     * Gets the results while verifying and changing flags.
     * @param view The view that invoked it
     * @since VersionCode 1
     */
    public void equal( View view ){

        // DEBUG
        Log.d( TAG, "S:VALUE: " + this.value );
        Log.d( TAG, "S:OP: " + this.op );
        Log.d( TAG, "S:RESULT: " + this.value );

        // Sets the mathematical holders of the result and the operand at hand
        this.value_holder = Double.parseDouble( this.value );
        this.result_holder = Double.parseDouble( this.result );

        // If the view was invoked directly by the "equal operation", sets the operation screen
        if( view != null )
            ((TextView) findViewById( R.id.op_screen )).setText( ((Button) view).getText() );

        // If the operation to be performed is the square root
        if( this.square_root ){

            // Do the math and store the string value
            this.value_holder = Math.sqrt( this.value_holder );
            this.value = this.value_holder.toString();

            // Lower the square root flag as the operation ended
            this.square_root = false;

        }

        // If the operand at hand is a number
        if( ! this.value_holder.isNaN() ){

            // Perform the operation at bay
            switch( this.op ){

                // Performs the multiplication
                case "×": this.result_holder *= this.value_holder; break;

                // Performs the division if the value at hand (the divisor or factor) isn't zero
                case "÷": if( this.value_holder != 0.0 ) this.result_holder /= this.value_holder; break;

                // Performs the addition
                case "+": this.result_holder += this.value_holder; break;

                // Performs the subtraction
                case "-": this.result_holder -= this.value_holder; break;

                // Any other case is just the previous result
                default: this.result_holder = this.value_holder; break;

            }

            // Stores the string number of the result
            this.result = this.result_holder.toString();

            // Store the string value of the operand at hand
            this.value = this.value_holder.toString();

        }

        // The operand at hand will be the result in cascading operations
        this.value = this.result;

        // Clears the operation after calculating it
        this.op = "";

        // The operation was performed so there is no operand at hand
        this.still = false;

        // There is no need for a comma if there is no operand being formed
        this.comma = false;

        // Shows the result into the calculator screen
        ((TextView) findViewById( R.id.calc_screen )).setText( remove_unnecessary_decimal( this.result ) );

        // DEBUG
        Log.d( TAG, "E:VALUE: " + this.value );
        Log.d( TAG, "E:OP: " + this.op );
        Log.d( TAG, "E:RESULT: " + this.value );

    }

}