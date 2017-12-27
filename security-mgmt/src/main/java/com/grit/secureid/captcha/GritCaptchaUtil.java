package com.grit.secureid.captcha;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

public final class GritCaptchaUtil {

    public static void writeCaptchaHttpRes(HttpServletResponse response, BufferedImage bi) {
        response.setHeader("Cache-Control", "private,no-cache,no-store");
        response.setContentType("image/png");	// PNGs allow for transparency. JPGs do not.
        try {
            writeImage(response.getOutputStream(), bi);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeImage(OutputStream os, BufferedImage bi) {
    	try {
    		ImageIO.write(bi, "png", os);
    		os.close();
    	}
    	catch(IOException e){
    		IOUtils.closeQuietly(os);
    	}
    	catch (Exception e) {
    		e.printStackTrace();
		}
    }
}