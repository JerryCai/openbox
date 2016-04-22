package com.googlecode.openbox.common;

import java.io.*;

/**
 * Created by jerrycai on 16-4-22.
 */
public class InputStreamConsumer implements Runnable {
    private InputStream input;
    private OutputStream output;

    public InputStreamConsumer(InputStream input, OutputStream output) {
        this.input = input;
        this.output = output;
    }

    @Override
    public void run() {
        char[] buffer = new char[2048];
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(output));
        BufferedReader br = new BufferedReader(new InputStreamReader(input));

        try {
            while (input.available() > 0) {
                int len = br.read(buffer);
                if (len > 0) {
                    bw.write(buffer, 0, len);
                    bw.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
