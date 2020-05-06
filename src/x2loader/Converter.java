/*
 * Conversion Functions are delegated Here...
 * 
 * 
 */
package x2loader;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import nl.lxtreme.binutils.hex.*;

/**
 *
 * @author User
 */
public class Converter {

    public Converter() {
    }
 

public byte[] convertNIOSHEX(String everything, int binSize, int address){
    System.out.println("HEX FILE IS" + everything);
    IntelHexReader hp1 = new IntelHexReader(new StringReader(everything));
    int dataByte = 0x00;
    byte[] fullStream = new byte[binSize]; 
    do{    
       try{dataByte = hp1.readByte();}
          catch(Exception E){System.out.println(E);}
       System.out.println("Data: " + String.format("0x%08X", dataByte) + " Address: " + String.format("0x%08X", address));
       if ( dataByte == -1 )
           {
           break;
           }
      fullStream[address]=(byte)dataByte;
      address++;
      }while ( dataByte != -1 );
    byte[] bigEndineStream = new byte[binSize];
    //fix 4-byte endian for Altera 
    System.out.println("Fixing Altera Awkward Endianess.....");
    for(int i = 0;i<binSize;i+=4){
        bigEndineStream[i] = fullStream[i+3];
        bigEndineStream[i+1] = fullStream[i+2]; 
        bigEndineStream[i+2] = fullStream[i+1]; 
        bigEndineStream[i+3] = fullStream[i]; 
        }
    System.out.println("Done.....");
    return bigEndineStream;
    }    
}
