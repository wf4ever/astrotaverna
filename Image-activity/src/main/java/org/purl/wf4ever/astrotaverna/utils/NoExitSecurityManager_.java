package org.purl.wf4ever.astrotaverna.utils;

import java.security.Permission;

/**
 * 
 * @author Julian Garrido
 * @since    19 May 2011
 */
public class NoExitSecurityManager_ extends SecurityManager {
	private int status;
	
	@Override
    public void checkPermission(Permission perm)
    {
      /* Allow everything else. */
    }

    @Override
    public void checkExit(int status)
    {
      /* Don't allow exit with any status code 1 . */
        this.status = status;
      //status == 0: no error
      //status != 0: error 
	    if(status == 1)
	      throw new SecurityException();
	    else
	      throw new SecurityException();
    }
    
    public int getStatus(){
    	return status;
    }
}
