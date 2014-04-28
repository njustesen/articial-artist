package Sound;

import java.io.ByteArrayOutputStream;

import javax.sound.sampled.*;

public class MicTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		AudioFormat format = new AudioFormat(8000.0f, 8, 1, true, true);

	    TargetDataLine line = null;
	    DataLine.Info info = new DataLine.Info(TargetDataLine.class,
	            format); 
	    if (!AudioSystem.isLineSupported(info)) {

	    }

	    try {
	        line = (TargetDataLine) AudioSystem.getLine(info);
	        line.open(format);
	    } catch (LineUnavailableException ex) {
	        // Handle the error ...
	    }

	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    int numBytesRead;
	    byte[] data = new byte[line.getBufferSize() / 5];


	    line.start();

	    while (true) {

	        numBytesRead = line.read(data, 0, data.length);
	        // Save this chunk of data.
	        out.write(data, 0, numBytesRead);
	        for(int i=0; i<numBytesRead; i+=1) {
	            System.out.println(Byte.toString(data[i]));

	        }
	        System.out.println();
	    }
	}

}
