package org.purl.wf4ever.astrotaverna.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import java.util.Vector;



/**
 * 
 * @author Julian Garrido
 * @since    19 May 2011
 */
public class MyUtils {

	public static String readFileAsString(String filePath) throws java.io.IOException{
	    byte[] buffer = new byte[(int) new File(filePath).length()];
	    BufferedInputStream f = null;
	    try {
	        f = new BufferedInputStream(new FileInputStream(filePath));
	        f.read(buffer);
	    } finally {
	        if (f != null) try { f.close(); } catch (IOException ignored) { }
	    }
	    return new String(buffer);
	}
	
	public static File writeStringAsTmpFile(String content) throws java.io.IOException{
	    
	    File file = File.createTempFile("astro", null);
	    FileWriter writer = new FileWriter(file);
	    writer.write(content);
	    writer.close();
	    
	    return file;
	}
	
	public static void writeStringToAFile(String filepath, String content) throws IOException{
		BufferedWriter out = new BufferedWriter(new FileWriter(filepath));
		out.write(content);
		out.close();
	}
	
	public static String convertStreamToString(InputStream is)
            throws IOException {
        //
        // To convert the InputStream to String we use the
        // Reader.read(char[] buffer) method. We iterate until the
        // Reader return -1 which means there's no more data to
        // read. We use the StringWriter class to produce the string.
        //
        if (is != null) {
            Writer writer = new StringWriter();
 
            char[] buffer = new char[1024];
            try {
                Reader reader = new BufferedReader(
                        new InputStreamReader(is, "UTF-8"));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } finally {
                is.close();
            }
            return writer.toString();
        } else {       
            return "";
        }
    }
	
	public static boolean isValidInputFormat(String format){
		if(format== null) 
			return false;
		else if(!(format.compareTo("fits")==0 
				|| format.compareTo("colfits")==0
				|| format.compareTo("votable")==0 
				|| format.compareTo("ascii")==0
				|| format.compareTo("csv")==0
				|| format.compareTo("tst")==0
				|| format.compareTo("ipac")==0))
			return false;
		else
			return true;
		  
	}
	
	public static boolean isValidOutputFormat(String format){
		if(format== null) 
			return false;
		else if(!(format.compareTo("fits")==0 
				|| format.compareTo("colfits")==0
				|| format.compareTo("votable")==0 
				|| format.compareTo("ascii")==0
				|| format.compareTo("csv")==0
				|| format.compareTo("tst")==0
				|| format.compareTo("ipac")==0))
			return false;
		else
			return true;
		  
	}
	
	public static String toSpacedString(String [] array){
		String result="";
		
		for(String cad : array){
			result= result + cad+" ";
		}
		
		return result.trim();
	}
	
	public static String toSpacedString(List<String> array){
		String result="";
		
		for(int i = 0; i < array.size(); i++){
			String cad = array.get(i);
			result= result + cad+" ";
		}
		
		return result.trim();
	}
	
	public static String toCommaSeparatedString(String [] array){
		String result="";
		
		
		for(int i=0; i<array.length; i++){
			String cad = array[i];
			result= result + cad;
			if((i+1) < array.length)
				result = result + ", ";
		}
		
		return result.trim();
	}
	
	public static String toCommaSeparatedString(Vector<String> array){
		String result="";
		
		for(int i=0; i<array.size(); i++){
			String cad = array.elementAt(i);
			result= result + cad;
			if((i+1) < array.size())
				result = result + ", ";
		}
		
		return result.trim();
	}
	
	/*
	 * it returns a string with a valid parameter for stilts.
	 * e.g. if the input is: physic_mag_kg;error_avg coord_ra
	 * then the output is ucd$physic_mag_kg_error_avg ucd$coord_ra
	 */
	public static String checkAndRepairUCDlist(String ucdlist){
		
		ucdlist = ucdlist.replaceAll("\\.", "_");
		ucdlist = ucdlist.replaceAll(";", "_");
		
		String [] splitfilter = ucdlist.split(" ");
		String result = "";
		
		for(String var: splitfilter){
			if(!var.trim().startsWith("ucd$")){
				if(result.length()==0)
					result = "ucd$" + var.trim();
				else
					result = result + " ucd$" + var.trim();
			}else
				if(result.length()==0)
					result = var.trim();
				else
					result = result + " " + var.trim();
		}
		return result;
	}

	public static String getExampleVOtable(){
		String example="";
		example = "<?xml version=\"1.0\" encoding=\"utf-8\"?> "
				+ "<VOTABLE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://vizier.u-strasbg.fr/xml/VOTable-1.1.xsd\"> "
				+ "  <DESCRIPTION>ConeSearch results from the Sloan Digital Sky Survey</DESCRIPTION> "
				+ "  <INFO name=\"rowcount, table 0\" value=\"2\" /> "
				+ "  <RESOURCE> "
				+ "    <TABLE> "
				+ "      <PARAM unit=\"degrees\" datatype=\"float\" name=\"inputRA\" value=\"195.163333333333\" /> "
				+ "      <PARAM unit=\"degrees\" datatype=\"float\" name=\"inputDEC\" value=\"2.50077777777778\" /> "
				+ "      <PARAM unit=\"degrees\" datatype=\"float\" name=\"inputSR\" value=\"0.001\" /> "
				+ "      <FIELD ID=\"OBJID\" datatype=\"long\" ucd=\"ID_MAIN\" /> "
				+ "      <FIELD ID=\"RA\" datatype=\"double\" ucd=\"POS_EQ_RA_MAIN\" /> "
				+ "      <FIELD ID=\"DEC\" datatype=\"double\" ucd=\"POS_EQ_DEC_MAIN\" /> "
				+ "      <FIELD ID=\"TYPE\" datatype=\"char\" ucd=\"CLASS_OBJECT\" /> "
				+ "      <FIELD ID=\"U\" datatype=\"float\" ucd=\"PHOT_SDSS_U;FIT_PARAM\" /> "
				+ "      <FIELD ID=\"G\" datatype=\"float\" ucd=\"PHOT_SDSS_G;FIT_PARAM\" /> "
				+ "      <FIELD ID=\"R\" datatype=\"float\" ucd=\"PHOT_SDSS_R;FIT_PARAM\" /> "
				+ "      <FIELD ID=\"I\" datatype=\"float\" ucd=\"PHOT_SDSS_I;FIT_PARAM\" /> "
				+ "      <FIELD ID=\"Z\" datatype=\"float\" ucd=\"PHOT_SDSS_Z;FIT_PARAM\" /> "
				+ "      <FIELD ID=\"ERR_U\" datatype=\"float\" ucd=\"PHOT_SDSS_U;ERROR\" /> "
				+ "      <FIELD ID=\"ERR_G\" datatype=\"float\" ucd=\"PHOT_SDSS_G;ERROR\" /> "
				+ "      <FIELD ID=\"ERR_R\" datatype=\"float\" ucd=\"PHOT_SDSS_R;ERROR\" /> "
				+ "      <FIELD ID=\"ERR_I\" datatype=\"float\" ucd=\"PHOT_SDSS_I;ERROR\" /> "
				+ "      <FIELD ID=\"ERR_Z\" datatype=\"float\" ucd=\"PHOT_SDSS_Z;ERROR\" /> "
				+ "      <FIELD ID=\"PSFMAG_U\" datatype=\"float\" ucd=\"PHOT_SDSS_U\" /> "
				+ "      <FIELD ID=\"PSFMAGERR_U\" datatype=\"float\" ucd=\"PHOT_SDSS_U;ERROR\" /> "
				+ "      <FIELD ID=\"PSFMAG_G\" datatype=\"float\" ucd=\"PHOT_SDSS_G\" /> "
				+ "      <FIELD ID=\"PSFMAGERR_G\" datatype=\"float\" ucd=\"PHOT_SDSS_G;ERROR\" /> "
				+ "      <FIELD ID=\"PSFMAG_R\" datatype=\"float\" ucd=\"PHOT_SDSS_R\" /> "
				+ "      <FIELD ID=\"PSFMAGERR_R\" datatype=\"float\" ucd=\"PHOT_SDSS_R;ERROR\" /> "
				+ "      <FIELD ID=\"PSFMAG_I\" datatype=\"float\" ucd=\"PHOT_SDSS_I\" /> "
				+ "      <FIELD ID=\"PSFMAGERR_I\" datatype=\"float\" ucd=\"PHOT_SDSS_I;ERROR\" /> "
				+ "      <FIELD ID=\"PSFMAG_Z\" datatype=\"float\" ucd=\"PHOT_SDSS_Z\" /> "
				+ "      <FIELD ID=\"PSFMAGERR_Z\" datatype=\"float\" ucd=\"PHOT_SDSS_Z;ERROR\" /> "
				+ "      <DATA> "
				+ "        <TABLEDATA> "
				+ "          <TR> "
				+ "            <TD>587726032792059953</TD> "
				+ "            <TD>195.163538721478</TD> "
				+ "            <TD>2.50072618814849</TD> "
				+ "            <TD>STAR</TD> "
				+ "            <TD>17.52193</TD> "
				+ "            <TD>17.47281</TD> "
				+ "            <TD>17.50826</TD> "
				+ "            <TD>17.99788</TD> "
				+ "            <TD>17.8128</TD> "
				+ "            <TD>0.0127011</TD> "
				+ "            <TD>0.006139796</TD> "
				+ "            <TD>0.007103184</TD> "
				+ "            <TD>0.01207803</TD> "
				+ "            <TD>0.03062468</TD> "
				+ "            <TD>17.59192</TD> "
				+ "            <TD>0.04599576</TD> "
				+ "            <TD>17.55609</TD> "
				+ "            <TD>0.1091162</TD> "
				+ "            <TD>17.60397</TD> "
				+ "            <TD>0.1366048</TD> "
				+ "            <TD>18.09505</TD> "
				+ "            <TD>0.2507383</TD> "
				+ "            <TD>17.93584</TD> "
				+ "            <TD>0.1380201</TD> "
				+ "          </TR> "
				+ "          <TR> "
				+ "            <TD>587726032792059952</TD> "
				+ "            <TD>195.162958157847</TD> "
				+ "            <TD>2.50144182633295</TD> "
				+ "            <TD>GALAXY</TD> "
				+ "            <TD>13.7583</TD> "
				+ "            <TD>12.79937</TD> "
				+ "            <TD>12.27589</TD> "
				+ "            <TD>11.99715</TD> "
				+ "            <TD>11.71331</TD> "
				+ "            <TD>0.004007341</TD> "
				+ "           <TD>0.001826325</TD> "
				+ "           <TD>0.001757175</TD> "
				+ "            <TD>0.001762709</TD> "
				+ "            <TD>0.002704954</TD> "
				+ "            <TD>17.67873</TD> "
				+ "            <TD>0.0193816</TD> "
				+ "            <TD>16.67595</TD> "
				+ "            <TD>0.03404235</TD> "
				+ "            <TD>16.40215</TD> "
				+ "            <TD>0.06255744</TD> "
				+ "            <TD>16.2727</TD> "
				+ "            <TD>0.02829609</TD> "
				+ "            <TD>16.17118</TD> "
				+ "            <TD>0.07506536</TD> "
				+ "          </TR> "
				+ "        </TABLEDATA> "
				+ "      </DATA> "
				+ "    </TABLE> "
				+ "  </RESOURCE> "
				+ "</VOTABLE> ";
		return example;
	}
	
	
	public static String getExampleVOTable2(){
		String example ="";
		example = "<?xml version=\"1.0\"?>"
				+ "<!DOCTYPE VOTABLE SYSTEM \"http://us-vo.org/xml/VOTable.dtd\">"
				+ "<VOTABLE version=\"1.0\">"
				+ "  <DEFINITIONS>"
				+ "    <COOSYS system=\"eq_FK5\" equinox=\"2000\" />"
				+ "  </DEFINITIONS>"
				+ "    "
				+ "  <RESOURCE ID=\"T9001\">"
				+ "     <DESCRIPTION>"
				+ "       HEASARC Browse data service"
				+ "       Please send inquiries to mailto:request@athena.gsfc.nasa.gov"
				+ "    </DESCRIPTION>"
				+ "    <PARAM ID=\"default_search_radius\" ucd=\"OBS_ANG-SIZE\" value=\"0.0166666666666667\" ></PARAM>"
				+ "    <TABLE>"
				+ "      <DESCRIPTION> Faint Images of the Radio Sky at Twenty cm (FIRST) </DESCRIPTION>"
				+ "      "
				+ "      <FIELD name=\"unique_id\" datatype=\"int\"  >"
				+ "        <DESCRIPTION> Integer key </DESCRIPTION>"
				+ "      </FIELD>"
				+ "      "
				+ "      <FIELD name=\"name\" datatype=\"char\" arraysize=\"*\"  ucd=\"ID_MAIN\">"
				+ "        <DESCRIPTION> FIRST Source Designation </DESCRIPTION>"
				+ "      </FIELD>"
				+ "      "
				+ "      <FIELD name=\"ra\" datatype=\"double\" unit=\"degree\" ucd=\"POS_EQ_RA_MAIN\">"
				+ "        <DESCRIPTION> Right Ascension </DESCRIPTION>"
				+ "      </FIELD>"
				+ "      "
				+ "      <FIELD name=\"dec\" datatype=\"double\" unit=\"degree\" ucd=\"POS_EQ_DEC_MAIN\">"
				+ "        <DESCRIPTION> Declination </DESCRIPTION>"
				+ "      </FIELD>"
				+ "      "
				+ "      <FIELD name=\"flux_20_cm\" datatype=\"double\" unit=\"mJy\" ucd=\"phot.flux.density;em.radio.750-1500MHz\">"
				+ "        <DESCRIPTION> Peak Flux Density at 1.4GHz (mJy) </DESCRIPTION>"
				+ "      </FIELD>"
				+ "      "
				+ "      <FIELD name=\"flux_20_cm_error\" datatype=\"double\" unit=\"mJy\" ucd=\"stat.error;phot.flux.density;em.radio.750-1500MHz\">"
				+ "        <DESCRIPTION> Local Noise Estimate of Source (mJy) </DESCRIPTION>"
				+ "      </FIELD>"
				+ "      "
				+ "      <FIELD name=\"int_flux_20_cm\" datatype=\"double\" unit=\"mJy\" ucd=\"phot.flux.density;em.radio.750-1500MHz\">"
				+ "        <DESCRIPTION> Integrated Flux Density at 1.4GHz (mJy) </DESCRIPTION>"
				+ "      </FIELD>"
				+ "      "
				+ "      <FIELD name=\"sidelobe_prob\" datatype=\"double\"  ucd=\"stat.probability\">"
				+ "        <DESCRIPTION> Probability That Source Is a Sidelobe </DESCRIPTION>"
				+ "      </FIELD>"
				+ "      "
				+ "      <FIELD name=\"twomass_first_offset\" datatype=\"double\" unit=\"arcsec\" ucd=\"pos.angDistance;em.IR;em.radio.750-1500MHz\">"
				+ "        <DESCRIPTION> Offset of Nearest 2MASS Source </DESCRIPTION>"
				+ "      </FIELD>"
				+ "      "
				+ "      <FIELD name=\"twomass_kmag\" datatype=\"double\" unit=\"mag\" ucd=\"phot.mag;em.IR.K\">"
				+ "        <DESCRIPTION> K Magnitude of Nearest 2MASS Source </DESCRIPTION>"
				+ "      </FIELD>"
				+ "      "
				+ "      <FIELD name=\"Search_Offset\" datatype=\"double\" unit=\"'\" >"
				+ "        <DESCRIPTION> Offset of target/observation from query center </DESCRIPTION>"
				+ "      </FIELD>"
				+ "	"
				+ "	"
				+ "      <DATA>"
				+ "        <TABLEDATA>"
				+ "<TR><TD>946464</TD><TD>FIRST J233859.7-112355</TD><TD>354.749046</TD><TD>-11.398828</TD><TD>    1.68</TD><TD>  0.140</TD><TD>     1.75</TD><TD>0.016</TD><TD></TD><TD></TD><TD>0.000</TD></TR>"
				+ "<TR><TD>946352</TD><TD>FIRST J233916.9-111928</TD><TD>354.820408</TD><TD>-11.324472</TD><TD>    1.62</TD><TD>  0.138</TD><TD>     1.48</TD><TD>0.047</TD><TD> 0.12</TD><TD>14.99</TD><TD>6.126</TD></TR>"
				+ "<TR><TD>946331</TD><TD>FIRST J233846.4-111841</TD><TD>354.693467</TD><TD>-11.311506</TD><TD>   11.41</TD><TD>  0.137</TD><TD>    15.56</TD><TD>0.014</TD><TD></TD><TD></TD><TD>6.176</TD></TR>"
				+ "        </TABLEDATA>"
				+ "      </DATA>"
				+ "    </TABLE>"
				+ "  </RESOURCE>"
				+ "</VOTABLE>";
		
		return example;
	}
	
	
	
	
}
