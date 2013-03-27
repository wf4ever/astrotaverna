package org.purl.wf4ever.astrotaverna.tjoin;

import uk.ac.starlink.ttools.Stilts;

public class TestMatch2Invokation {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//java -jar stiltsAPI-2.4.jar tmatch2 in1=/var/folders/Lt/LtAS+ARCFQuez0kT9c6YWE+++TQ/-Tmp-/astro2814993806624810582.tmp in2=/var/folders/Lt/LtAS+ARCFQuez0kT9c6YWE+++TQ/-Tmp-/astro3814046398394210397.tmp out=/var/folders/Lt/LtAS+ARCFQuez0kT9c6YWE+++TQ/-Tmp-/astro6179587660213325197.tmp ofmt=votable matcher=sky values2='ra dec' values1='ra dec' params='2' join=1and2 find=best fixcols=dups
		
		String [] param = new String [8];
		param[0] = "tmatch2"; 
		param[1] = "in1=/var/folders/Lt/LtAS+ARCFQuez0kT9c6YWE+++TQ/-Tmp-/astro2814993806624810582.tmp";
		param[2] = "in2=/var/folders/Lt/LtAS+ARCFQuez0kT9c6YWE+++TQ/-Tmp-/astro3814046398394210397.tmp"; 
		param[3] = "values2=ra dec"; 
		param[4] = "values1=ra dec";
		param[5] = "out=/var/folders/Lt/LtAS+ARCFQuez0kT9c6YWE+++TQ/-Tmp-/astro310010473517533455.tmp";
		param[5] = "ofmt=votable";
		param[6] = "matcher=sky";
		param[7] = "params='2'";
		//param[8] = "join=1and2";
		//param[8] = "find=best";
		//param[10] = "fixcols=dups";

		Stilts.main(param);

	}

}
