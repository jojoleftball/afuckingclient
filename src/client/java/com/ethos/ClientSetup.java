package com.ethos;

import com.ethos.client.hacks.HackManager;
import com.ethos.client.ui.InputHandler;

public class ClientSetup {
    public static void init() {
        // initialize hack modules and input handlers
        HackManager.init();
        InputHandler.register();
    }
}