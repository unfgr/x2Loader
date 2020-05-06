/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package x2loader;
import java.io.Console;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.text.DefaultCaret;
import jssc.*;
/**
 *
 * @author User
 */
public class Comms {
private static Comms instance;
public String comPortName;
private int initializedPort = 0;
public SerialPort commPort;
private static int timeout = 10;  //this is timeout per char....
private static int staticTimeout = 5000; //timeout value always added
private MainGui mg;
private boolean enabledFeedBack = false;
    
    private Comms() {
    }

public boolean comportInit(){
if(commPort!=null && !commPort.isOpened()){    
  try{
    commPort.openPort();
    return true; 
    }
  catch(Exception E){
    System.out.println(E); 
    return false;
    } 
}
else
    return false;
}
    
public void setComPort(String comPortName){
if(this.commPort==null){
    this.commPort = new SerialPort(comPortName);   
    try{
        commPort.openPort();
        commPort.setParams(SerialPort.BAUDRATE_115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
        commPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
        initializedPort = 1;
        commPort.addEventListener(new PortBridgefunction(), SerialPort.MASK_RXCHAR);
        }
    catch(Exception E){
        System.out.println(E);
        }        
System.out.println("NEW PORT: " + commPort.getPortName());
}
else{
    try{
        if(commPort.isOpened()){
        System.out.println("Port is already Open");
        commPort.purgePort(SerialPort.PURGE_RXCLEAR | SerialPort.PURGE_TXCLEAR);
        commPort.closePort();
        }
        
        commPort = new SerialPort(comPortName);
        commPort.openPort();
        commPort.setParams(SerialPort.BAUDRATE_115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
        commPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
        commPort.addEventListener(new PortBridgefunction(), SerialPort.MASK_RXCHAR);
        this.initializedPort = 1;
        }
     catch(Exception E){
        System.out.println(E);    
        }
    System.out.println("UPDATED PORT: " + commPort.getPortName());
    }
if(!this.commPort.getPortName().equals(comPortName)){
System.out.println("Previous Name: " + commPort.getPortName() + " New Name: " + comPortName);
this.comPortName = comPortName;    
}

}    


//get a String from Serial Port (ASCII)
public String getString(int dataSize){
String replyString;    
try{
    replyString = commPort.readString(dataSize,staticTimeout+timeout*dataSize);
   }
catch(Exception E){
   replyString = "!ERRORERRORERROR!";
   System.out.println(E);
   }
return replyString;
}


//Send a String to Serial Port (ASCII)
public boolean sendString(String sendData){
try{
    if(sendData.length()>0)
   return commPort.writeString(sendData);
    else 
   return true;
   }
catch(Exception E){
   System.out.println(E);
   return false;
   }
}

public byte[] getData(int dataSize){
try{
    return commPort.readBytes(dataSize,staticTimeout+timeout*dataSize);
    }
catch(Exception E){
   System.out.println(E);
   return null;
   }      
}

public boolean sendData(byte[] dataBuffer){

try{
    return commPort.writeBytes(dataBuffer);
    }
catch(Exception E){
    System.out.println(E);
    return false;
    }
}

public String[] getPortsList(){
return SerialPortList.getPortNames();  
}

public void closePort(){
try{
   commPort.closePort();   
   }
catch(Exception E){};
}
    
public static synchronized Comms getInstance()
{
if (instance == null)
    instance = new Comms();
    return instance;
    }    

public void setMgRef(MainGui mg){
    this.mg = mg;
    }

public boolean getComPortStatus(){
if(commPort!=null)
    return commPort.isOpened();
else
    return false;
}

public void enableConsoleFeedBack(boolean enableFlag){
    if(commPort!=null && commPort.isOpened()){    
        enabledFeedBack = enableFlag;
    }
}

public void purgeRX(){
    try{
        while(commPort.getInputBufferBytesCount()>0){
            commPort.readBytes();
            }    
        commPort.purgePort(SerialPort.PURGE_RXCLEAR);
       }
    catch(Exception E){
    System.out.println(E);    
    }
}

private class PortBridgefunction implements SerialPortEventListener {
    @Override
    public void serialEvent(SerialPortEvent event) {
        if(event.isRXCHAR() && event.getEventValue() > 0 && enabledFeedBack) {
            try {
                
                String receivedData = commPort.readString(event.getEventValue());
                System.out.println(receivedData);
                mg.consoleTextArea.append(receivedData);
                if(mg.autoScroll){
                mg.consoleTextArea.setCaretPosition(mg.consoleTextArea.getDocument().getLength());
                }
            }
            catch (SerialPortException ex) {
                System.out.println("Error in receiving string from COM-port: " + ex);
            }
        }
    }
}

}

