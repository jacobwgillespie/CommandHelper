/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.laytonsmith.aliasengine.Constructs;

import java.io.File;

/**
 *
 * @author layton
 */
public class CFunction extends Construct {

    public CFunction(String name, int line_num, File file) {
        super(name, ConstructType.FUNCTION, line_num, file);
    }

    public String toString() {
        return this.value;
    }
}
