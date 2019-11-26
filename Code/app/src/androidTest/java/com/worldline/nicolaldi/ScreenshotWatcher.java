package com.worldline.nicolaldi;

import android.graphics.Bitmap;

import androidx.test.InstrumentationRegistry;
import androidx.test.runner.screenshot.ScreenCapture;
import androidx.test.runner.screenshot.Screenshot;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.io.IOException;
import java.util.Locale;

/**
 * @author Nicola Verbeeck
 */
public class ScreenshotWatcher extends TestWatcher {

    @Override
    protected void succeeded(Description description) {
        Locale locale = InstrumentationRegistry.getTargetContext()
                .getResources()
                .getConfiguration()
                .getLocales()
                .get(0);
        captureScreenshot(description.getMethodName() + "_" + locale.toLanguageTag());
    }


    private void captureScreenshot(String name) {
        ScreenCapture capture = Screenshot.capture();
        capture.setFormat(Bitmap.CompressFormat.PNG);
        capture.setName(name);
        try {
            capture.process();
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
    }


    @Override
    protected void failed(Throwable e, Description description) {
        captureScreenshot(description.getMethodName() + "_fail");
    }
}