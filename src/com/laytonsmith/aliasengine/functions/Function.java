/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.laytonsmith.aliasengine.functions;

import com.laytonsmith.aliasengine.Constructs.Construct;
import com.laytonsmith.aliasengine.CancelCommandException;
import com.laytonsmith.aliasengine.ConfigRuntimeException;
import org.bukkit.entity.Player;

/**
 *
 * @author layton
 */
public interface Function {
    /**
     * The name of this function, exactly as should be used in a script. Note that the name of
     * the function must match the regex:
     * <pre>
     * [a-zA-Z_][a-zA-Z_0-9]*
     * </pre>
     * In other words, it must start with a letter or underscore, and may have any number of
     * letters, numbers, or underscores after it.
     * @return 
     */
    public String getName();
    /**
     * The number of arguments this function can accept. Some functions may be able to accept multiple numbers
     * of arguments, so this function returns an array. If you return Integer.MAX_VALUE as one of the
     * items in the array, then any number of arguments may be sent.
     * @return 
     */
    public Integer[] numArgs();
    /**
     * This function is invoked when the alias is run. The line number is provided so that if there is an error,
     * the function can provide a more specific error message for the user. The function can throw a CancelCommandException
     * which indicates that the command was purposefully canceled. If the command was canceled due to a fatal error
     * in the syntax of the user input or some similar situation, it is better to throw a ConfigRuntimeException instead.
     * Throwing either Exception will prevent the command from completing, however functions that had been run earlier will
     * may have already completed successfully, so there is no guarantee of atomicity. All parameters sent to the
     * function have already been resolved into an atomic value though, so functions do not have to worry about
     * resolving parameters. There is an explicit check made before calling exec to ensure that Construct ... args
     * will only be one of the following:
     * CBoolean, CDouble, CInt, CNull, CString, CVoid, or IVariable. If you care, you'll need to do further checks
     * on the datatype to verify what the type actually is.
     * @param line_num The line that this function call is being run from
     * @param args An array of evaluated Constructs
     * @return
     * @throws CancelCommandException 
     */
    public Construct exec(int line_num, Player p, Construct ... args) throws CancelCommandException, ConfigRuntimeException;
    /**
     * If a user asks for information about a particular function, this method is called to obtain the functions
     * usage.
     * @return A string with the documentation, or null, which will give a standard message to the user telling them there
     * is no documentation for this function yet.
     */
    public String docs();
    /**
     * Whether or not a function needs to be checked against the permissions file, if there are possible security concerns
     * with a user compiling, or running this function. If this function returns true, the permissions file will be checked for
     * commandhelper.func.compile.&lt;function name&gt; upon compilation, and commandhelper.func.use.&lt;function name&gt; upon
     * usage in game. Note that the config script is never barred from compiling any function.
     * @return 
     */
    public boolean isRestricted();
    /**
     * Most functions don't care that a construct is a variable, they simply care about the value stored in the variable.
     * If the function is concerned with the variable listing however, then it has direct access to the variable list for
     * this command.
     * @param varList 
     */
    public void varList(IVariableList varList);
    /**
     * If a function doesn't want to have to deal with a variable as a variable, but instead wants to recieve it as
     * an atomic, resolved value, the function can return true from this function. This will signal the interpreter
     * to go ahead and resolve the variable into one of the atomic Constructs. If it returns false, it is possible
     * the exec function will receive an IVariable Construct.
     * @return 
     */
    public boolean preResolveVariables();
}