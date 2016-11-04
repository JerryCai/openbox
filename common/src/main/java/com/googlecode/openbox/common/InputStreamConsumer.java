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
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(output));
        BufferedReader br = new BufferedReader(new InputStreamReader(input));

        try {
            String line = null;
            while (null != (line = br.readLine())) {
                bw.write(line);
                bw.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(null != input) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
