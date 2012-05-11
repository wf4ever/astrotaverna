package org.purl.wf4ever.astrotaverna.utils;

import net.sf.taverna.t2.workflowmodel.processor.activity.ActivityConfigurationException;

public class MyUtils {

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
	
	
	public static String checkAndRepairUCDlist(String ucdlist){
		ucdlist = ucdlist.replaceAll(".", "_");
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
				+ "      <FIELD ID=\"U\" datatype=\"float\" ucd=\"PHOT_SDSS_U FIT_PARAM\" /> "
				+ "      <FIELD ID=\"G\" datatype=\"float\" ucd=\"PHOT_SDSS_G FIT_PARAM\" /> "
				+ "      <FIELD ID=\"R\" datatype=\"float\" ucd=\"PHOT_SDSS_R FIT_PARAM\" /> "
				+ "      <FIELD ID=\"I\" datatype=\"float\" ucd=\"PHOT_SDSS_I FIT_PARAM\" /> "
				+ "      <FIELD ID=\"Z\" datatype=\"float\" ucd=\"PHOT_SDSS_Z FIT_PARAM\" /> "
				+ "      <FIELD ID=\"ERR_U\" datatype=\"float\" ucd=\"PHOT_SDSS_U ERROR\" /> "
				+ "      <FIELD ID=\"ERR_G\" datatype=\"float\" ucd=\"PHOT_SDSS_G ERROR\" /> "
				+ "      <FIELD ID=\"ERR_R\" datatype=\"float\" ucd=\"PHOT_SDSS_R ERROR\" /> "
				+ "      <FIELD ID=\"ERR_I\" datatype=\"float\" ucd=\"PHOT_SDSS_I ERROR\" /> "
				+ "      <FIELD ID=\"ERR_Z\" datatype=\"float\" ucd=\"PHOT_SDSS_Z ERROR\" /> "
				+ "      <FIELD ID=\"PSFMAG_U\" datatype=\"float\" ucd=\"PHOT_SDSS_U\" /> "
				+ "      <FIELD ID=\"PSFMAGERR_U\" datatype=\"float\" ucd=\"PHOT_SDSS_U ERROR\" /> "
				+ "      <FIELD ID=\"PSFMAG_G\" datatype=\"float\" ucd=\"PHOT_SDSS_G\" /> "
				+ "      <FIELD ID=\"PSFMAGERR_G\" datatype=\"float\" ucd=\"PHOT_SDSS_G ERROR\" /> "
				+ "      <FIELD ID=\"PSFMAG_R\" datatype=\"float\" ucd=\"PHOT_SDSS_R\" /> "
				+ "      <FIELD ID=\"PSFMAGERR_R\" datatype=\"float\" ucd=\"PHOT_SDSS_R ERROR\" /> "
				+ "      <FIELD ID=\"PSFMAG_I\" datatype=\"float\" ucd=\"PHOT_SDSS_I\" /> "
				+ "      <FIELD ID=\"PSFMAGERR_I\" datatype=\"float\" ucd=\"PHOT_SDSS_I ERROR\" /> "
				+ "      <FIELD ID=\"PSFMAG_Z\" datatype=\"float\" ucd=\"PHOT_SDSS_Z\" /> "
				+ "      <FIELD ID=\"PSFMAGERR_Z\" datatype=\"float\" ucd=\"PHOT_SDSS_Z ERROR\" /> "
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
}
