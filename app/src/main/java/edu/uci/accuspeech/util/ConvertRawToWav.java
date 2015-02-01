package edu.uci.accuspeech.util;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ConvertRawToWav {

    /**
     * Code to write WAV header information taken from
     * http://stackoverflow.com/questions/9179536/writing-pcm-recorded-data-into-a-wav-file-java-android
     *
     * Code has been changed for this project from above link.
     * This method takes a raw file and then creates a wav file.
     *
     */
    static public void properWAV(String rawFileName, String wavFileName, int mySampleRate){
        try {
            File rawFile = new File(rawFileName);
            long mySubChunk1Size = 16;
            int myBitsPerSample= 16;
            int myFormat = 1;
            long myChannels = 1;
            long myByteRate = mySampleRate * myChannels * myBitsPerSample/8;
            int myBlockAlign = (int) (myChannels * myBitsPerSample/8);

            long myDataSize = rawFile.length();
            long myChunk2Size =  myDataSize * myChannels * myBitsPerSample/8;
            long myChunkSize = 36 + myChunk2Size;

            OutputStream os;
            os = new FileOutputStream(new File(wavFileName));
            BufferedOutputStream bos = new BufferedOutputStream(os);
            DataOutputStream outFile = new DataOutputStream(bos);

            outFile.writeBytes("RIFF");                                 // 00 - RIFF
            outFile.write(intToByteArray((int)myChunkSize), 0, 4);      // 04 - how big is the rest of this file?
            outFile.writeBytes("WAVE");                                 // 08 - WAVE
            outFile.writeBytes("fmt ");                                 // 12 - fmt
            outFile.write(intToByteArray((int)mySubChunk1Size), 0, 4);  // 16 - size of this chunk
            outFile.write(shortToByteArray((short)myFormat), 0, 2);     // 20 - what is the audio format? 1 for PCM = Pulse Code Modulation
            outFile.write(shortToByteArray((short)myChannels), 0, 2);   // 22 - mono or stereo? 1 or 2?  (or 5 or ???)
            outFile.write(intToByteArray((int)mySampleRate), 0, 4);     // 24 - samples per second (numbers per second)
            outFile.write(intToByteArray((int)myByteRate), 0, 4);       // 28 - bytes per second
            outFile.write(shortToByteArray((short)myBlockAlign), 0, 2); // 32 - # of bytes in one sample, for all channels
            outFile.write(shortToByteArray((short)myBitsPerSample), 0, 2);  // 34 - how many bits in a sample(number)?  usually 16 or 24
            outFile.writeBytes("data");                                 // 36 - data
            outFile.write(intToByteArray((int)myDataSize), 0, 4);       // 40 - how big is this data chunk

            InputStream inputStream = new FileInputStream(rawFile);
            int numRead = 0;
            byte[] buffer = new byte[1024];
            while ( (numRead = inputStream.read(buffer) ) >= 0) {
                outFile.write(buffer, 0, numRead); // 44 - the actual data itself - just a long string of numbers
            }
            inputStream.close();

            outFile.flush();
            outFile.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private static byte[] intToByteArray(int i)
    {
        byte[] b = new byte[4];
        b[0] = (byte) (i & 0x00FF);
        b[1] = (byte) ((i >> 8) & 0x000000FF);
        b[2] = (byte) ((i >> 16) & 0x000000FF);
        b[3] = (byte) ((i >> 24) & 0x000000FF);
        return b;
    }

    // convert a short to a byte array
    public static byte[] shortToByteArray(short data)
    {
        /*
         * NB have also tried:
         * return new byte[]{(byte)(data & 0xff),(byte)((data >> 8) & 0xff)};
         *
         */

        return new byte[]{(byte)(data & 0xff),(byte)((data >>> 8) & 0xff)};
    }
}
