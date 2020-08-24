/**
 * 2009-12-5
 */
package com.leconssoft.common.baseUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


/**
 * <p>Title: NetIOUtils.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: Eshore</p>
 * <p><a href="NetIOUtils.java.html"><i>View Source</i></a></p>
 * @author Yusm
 * @version 1.0
 */
public class NetIOUtils {
	
	public static boolean isNetworkAvailable(Context context) {
	    ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    if (connectivity == null) {     
	      return false; 
	    } else {   
	        NetworkInfo info = connectivity.getActiveNetworkInfo();
	        if (info != null) {         
	        	if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
	        }  
	    }    
	    return false; 
	}	
	

}
