/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

/**
 *
 * @author zepingluo
 */

import java.io.*;
import java.nio.channels.FileChannel;

public class Copy
{
	public static void main(String[] args) throws IOException
	{
		File file = new File("/Users/zepingluo/downloads/cat.jpg");
		File target = new File("/Users/zepingluo/documents/catcopied.jpg");
               // System.out.println(file.getPath());
              // Copy copy = new Copy();
                //copy.copyFile(file,target,false);
               // Copy.copyFile(file, target);
               
               Copy copy= new Copy();
               String str="123";
               copy.str(str);
               System.out.println(str);
               String x= "123";
               str(x);
               System.out.println(x);               
        }
        public static void str(String str)
        {
           str="234";
        }
        
        
     
    public static void copyFile(File sourceFile, File destFile)
            throws IOException {
        if (!sourceFile.exists()) {
            System.out.println("!sourceFile.exists");
            return;
        }
        if (!destFile.exists()) {
            destFile.createNewFile();
            System.out.println("!destFile.exists()");
        }
        FileChannel source = null;
        FileChannel destination = null;
        source = new FileInputStream(sourceFile).getChannel();
        destination = new FileOutputStream(destFile).getChannel();
        if (destination != null && source != null) {
            destination.transferFrom(source, 0, source.size());
            System.out.println("transfered");
        }
        if (source != null) {
            source.close();
        }
        if (destination != null) {
            destination.close();
        }

    }


        
        
        
        
        //****
        public void copyFile(File aSourceFile, File aTargetFile, boolean aAppend) {
    log("Copying files with streams.");
    ensureTargetDirectoryExists(aTargetFile.getParentFile());
    InputStream inStream = null;
    OutputStream outStream = null;
    try{
      try {
        byte[] bucket = new byte[32*1024];
        inStream = new BufferedInputStream(new FileInputStream(aSourceFile));
        outStream = new BufferedOutputStream(new FileOutputStream(aTargetFile, aAppend));
        int bytesRead = 0;
        while(bytesRead != -1){
          bytesRead = inStream.read(bucket); //-1, 0, or more
          if(bytesRead > 0){
            outStream.write(bucket, 0, bytesRead);
          }
        }
      }
      finally {
        if (inStream != null) inStream.close();
        if (outStream != null) outStream.close();
      }
    }
    catch (FileNotFoundException ex){
      log("File not found: " + ex);
    }
    catch (IOException ex){
      log(ex);
    }
  }
  private static void log(Object aThing){
    System.out.println(String.valueOf(aThing));
  }
  private void ensureTargetDirectoryExists(File aTargetDir){
    if(!aTargetDir.exists()){
      aTargetDir.mkdirs();
    }
  }
  
}

