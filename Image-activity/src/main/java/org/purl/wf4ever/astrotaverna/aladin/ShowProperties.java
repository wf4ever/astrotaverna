package org.purl.wf4ever.astrotaverna.aladin;
 
public class ShowProperties {

    public static void main(String[] args) {
        String [] properties = {
            "java.ext.dirs",
            "java.home",
            "path.separator",
            "file.separator",
            "java.library.path",
            "os.arch",
            "sun.boot.class.path"
        };
        for (int i = 0; i <  properties.length; i++) {
            String key = properties[i];
            System.out.println(key + ": " + System.getProperty(key));
        }
    }
} 


