package com.mirth.connect.simplesender;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class TCPSender {

	private Logger logger = Logger.getLogger(this.getClass().getName());

	/**
	 * 
	 * @param hl7
	 * @param outputIP indirizzo ip su cui collegarsi
	 * @param outputPort porta di ascolto del server
	 * @return server answer
	 * @throws Exception
	 */
    public List<String> send(String hl7, String outputIP, String outputPort) throws Exception {
    	
    	List<String> answer = null;
        Socket s = null;
        try {
            s = new Socket(outputIP, Integer.parseInt(outputPort));
            DataOutputStream out = new DataOutputStream(s.getOutputStream());
            out.write(LLPUtil.HL7Encode(hl7).getBytes());
            
            logger.info("Message sent...\n" + hl7);
            
            answer = getAnswer(s);
            
        } finally {
            if (s != null) {
                s.close();
            }
        }
        return answer;
    }
    
    /**
     * risposta del server
     * @param s Socket
     */
	private List<String> getAnswer(Socket s) {
		
		ArrayList<String> retValue = new ArrayList<String>();
		
		DataInputStream in = null;
		char[] chars = new char[9999];
		try {
			in = new DataInputStream(s.getInputStream());
			int i = 0;
			char c = (char) in.read();
			while (c >= 0 && c != LLPUtil.END_MESSAGE) {
				
				if (c != LLPUtil.LAST_CHARACTER && c != LLPUtil.END_MESSAGE) {
					if (c != LLPUtil.START_MESSAGE && c != LLPUtil.END_MESSAGE) {
						chars[i] = c;
						i++;
					}
				} else {
					String oneRow = new String(chars, 0, i);
//					System.out.println(oneRow);
					retValue.add(oneRow);
					chars = new char[9999];
					i = 0;
				}
				c = (char) in.read();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}

		return retValue;
	}
}
