/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.purl.wf4ever.astrotaverna.voutils;

import static org.junit.Assert.assertTrue;

/**
 *
 * @author christian
 */
public class TestUtils {
    
    public static final void compareStringLengthsIgnoreWhiteSpace(String a, String b, int tolerance){
        a = a.replace(System.getProperty("line.separator"), "").replace("\n", "").replace("\r", "").replace("\t", "").replace(" ", "");
        b = b.replace(System.getProperty("line.separator"), "").replace("\n", "").replace("\r", "").replace("\t", "").replace(" ", "");
		
        assertTrue("Wrong output : ", (a.length()>b.length()-tolerance) && (a.length()<b.length()+tolerance));
    }
}
