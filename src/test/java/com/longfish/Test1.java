package com.longfish;

import com.longfish.client.RemoteClient;

public class Test1 {

    public static void main(String[] args) {
        new RemoteClient(true, "localhost", 12345, 0, "aaa", "1-1");
    }
}
