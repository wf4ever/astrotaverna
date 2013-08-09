package org.purl.wf4ever.astrotaverna.voutils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

import org.apache.log4j.Logger;

import uk.ac.starlink.table.JoinStarTable;
import uk.ac.starlink.table.RowListStarTable;
import uk.ac.starlink.table.StarTable;
import uk.ac.starlink.table.StarTableFactory;
import uk.ac.starlink.table.StarTableOutput;
import uk.ac.starlink.table.StarTableWriter;
import uk.ac.starlink.table.TableFormatException;

public class AddCommonRowToVOTableController {
	private StarTable mainVOTable;
	private StarTable rowVOTable;
	
	private StarTable result;
	
	private static Logger logger = Logger.getLogger(AddCommonRowToVOTableController.class);
	
	public AddCommonRowToVOTableController (File rowVOTableFile, File mainVOTableFile, boolean leftPosition) throws TableFormatException, IOException{
		long colrowcount, rowrowcount, rownumber_main;
		RowListStarTable commonRowTable;
		JoinStarTable joinTable;
		
		
		mainVOTable = loadVOTable(mainVOTableFile);
		rowVOTable = loadVOTable(rowVOTableFile);
		
		if(mainVOTable !=null && rowVOTable !=null){
			colrowcount = rowVOTable.getRowCount();
			rowrowcount = rowVOTable.getRowCount();
			rownumber_main = mainVOTable.getRowCount();
			result = mainVOTable;
			if(colrowcount > 0 && rowrowcount >0){
				try {
					//get the row
					Object[] row = rowVOTable.getRow(0);
					
					//build the aux table
					commonRowTable = new RowListStarTable(rowVOTable);
					for(long i = 0; i<rownumber_main; i++){
						commonRowTable.addRow(row);
					}
					
					//join both tables
					if(commonRowTable.getRowCount() == mainVOTable.getRowCount()){
						StarTable [] tables = new StarTable[2];
						if(leftPosition){
							tables[1] = mainVOTable;
							tables[0] = commonRowTable;
						}else{
							tables[0] = mainVOTable;
							tables[1] = commonRowTable;
						}
						joinTable = new JoinStarTable(tables);
						if(joinTable !=null)
							result = joinTable;
						
					}
					
				} catch (IOException e) {
					logger.warn("The table didn't have elements.\n " + e.getMessage(), e);
				}
				
			}
		}
		
	}
	
	public AddCommonRowToVOTableController (URI rowVOTableURI, URI mainVOTableURI, boolean leftPosition) throws TableFormatException, IOException{
		long colrowcount, rowrowcount, rownumber_main;
		RowListStarTable commonRowTable;
		JoinStarTable joinTable;
		
		
		mainVOTable = loadVOTable(mainVOTableURI);
		rowVOTable = loadVOTable(rowVOTableURI);
		
		if(mainVOTable !=null && rowVOTable !=null){
			colrowcount = rowVOTable.getRowCount();
			rowrowcount = rowVOTable.getRowCount();
			rownumber_main = mainVOTable.getRowCount();
			result = mainVOTable;
			if(colrowcount > 0 && rowrowcount >0){
				try {
					//get the row
					Object[] row = rowVOTable.getRow(0);
					
					//build the aux table
					commonRowTable = new RowListStarTable(rowVOTable);
					for(long i = 0; i<rownumber_main; i++){
						commonRowTable.addRow(row);
					}
					
					//join both tables
					if(commonRowTable.getRowCount() == mainVOTable.getRowCount()){
						StarTable [] tables = new StarTable[2];
						if(leftPosition){
							tables[1] = mainVOTable;
							tables[0] = commonRowTable;
						}else{
							tables[0] = mainVOTable;
							tables[1] = commonRowTable;
						}
						
						joinTable = new JoinStarTable(tables);
						if(joinTable !=null)
							result = joinTable;
						
					}
					
				} catch (IOException e) {
					logger.warn("The table didn't have elements.\n " + e.getMessage(), e);
				}
			}
		}
		
	}
	
	public StarTable getJoinTable(){
		return result;
	}
	
	public void writeJoinTable(File file) throws TableFormatException, IOException{
		StarTableOutput output = new StarTableOutput();
		output.writeStarTable(result, file.toString(), "votable" );
	}
	
	public String writeJoinTable() throws IOException{
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		writeTableAsVOTable(result, outputStream);
		String string_result = outputStream.toString("UTF-8");
		return string_result;
	}
		
	private void writeTableAsVOTable( StarTable table, OutputStream out ) throws IOException {
        StarTableOutput sto = new StarTableOutput();
        StarTableWriter outputHandler = sto.getHandler( "votable" );
        sto.writeStarTable( table, out, outputHandler );
    }
	
	/*
	private StarTable loadVOTable(String path) throws TableFormatException, IOException{
		return new StarTableFactory().makeStarTable(path, "votable" );
	}
	
	public StarTable loadVOTable( File source ) throws IOException {
	    return new StarTableFactory().makeStarTable( source.toString(), "votable" );
	}
	*/
	
	public StarTable loadVOTable( File source ) throws IOException {
	    return new StarTableFactory().makeStarTable( source.toString(), "votable" );
	}
	
	public StarTable loadVOTable( URI source ) throws IOException {
	    return new StarTableFactory().makeStarTable( source.toString(), "votable" );
	}
}
