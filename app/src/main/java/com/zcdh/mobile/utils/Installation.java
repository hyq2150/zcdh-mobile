package com.zcdh.mobile.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.UUID;

import android.content.Context;
import android.util.Log;

/**
 * 设备标识码
 * @author YJN
 *
 */
public class Installation {  
    private static String sID = null;  
    private static final String INSTALLATION = "INSTALLATION";  
    private static final String TAG = Installation.class.getSimpleName();
    public synchronized static String id(Context context) {  
    	File installation = new File(context.getFilesDir(), INSTALLATION);  
    	 Log.i(TAG, installation.getPath());
        if (sID == null) {    
           
            try {  
                if (!installation.exists())  
                    writeInstallationFile(installation);  
                sID = readInstallationFile(installation);  
            } catch (Exception e) {  
                throw new RuntimeException(e);  
            }  
        }  
        return sID;  
    }  
    private static String readInstallationFile(File installation) throws IOException {  
        RandomAccessFile f = new RandomAccessFile(installation, "r");  
        byte[] bytes = new byte[(int) f.length()];  
        f.readFully(bytes);  
        f.close();  
        return new String(bytes);  
    }  
    private static void writeInstallationFile(File installation) throws IOException {  
        FileOutputStream out = new FileOutputStream(installation);  
        String id = UUID.randomUUID().toString();  
        out.write(id.getBytes());  
        out.close();  
    }  
}  