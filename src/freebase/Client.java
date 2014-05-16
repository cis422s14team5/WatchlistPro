package freebase;

/**
 * Copyright (c) 1995, 2013, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

// TODO Handle null pointer errors with searching

public class Client {

    private static final int FILM = 0;
    private static final int TV = 1;
    private static final int LOADING = 2;

    private int state;

    private ArrayList<String> outputList;
    private Socket socket;

    public Client() {
        String hostName = "localhost";
        int portNumber = 1981;
        try {
            socket = new Socket(hostName, portNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Thread send(String command) throws IOException, InterruptedException {
        switch (command) {
            case "film":
                state = FILM;
                break;
            case "tv":
                state = TV;
                break;
            case "load":
                state = LOADING;
                break;
        }
        return new Thread(new ClientThread(this, state, socket, command), "thread");
    }

    public ArrayList<String> getOutputList() {
        return outputList;
    }

    public void setOutputList(ArrayList<String> outputList) {
        this.outputList = outputList;
    }
}
