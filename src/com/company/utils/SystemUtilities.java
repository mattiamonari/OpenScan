package com.company.utils;

import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

public class SystemUtilities {

    public static void copyToClipboardText(String textToCopy) {

        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();

        content.putString(textToCopy);
        clipboard.setContent(content);

    }
}
