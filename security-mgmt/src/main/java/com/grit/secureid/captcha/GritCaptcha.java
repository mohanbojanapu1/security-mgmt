package com.grit.secureid.captcha;

import static nl.captcha.Captcha.NAME;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.captcha.Captcha;
import nl.captcha.text.renderer.DefaultWordRenderer;

/**
 * @author Mohan 
 *
 */
public class GritCaptcha{
	    
	    public void writeImage(HttpServletRequest req, HttpServletResponse resp, List COLORS, List FONTS, int _width, int _height)
	            throws ServletException, IOException {
	    	//DefaultTextProducer wordRenderer=new DefaultTextProducer();
	    	DefaultWordRenderer wordRenderer = new DefaultWordRenderer(COLORS, FONTS);
	        Captcha captcha = new Captcha.Builder(_width, _height).addText(wordRenderer)
	                .gimp()
	                .build();
	        GritCaptchaUtil.writeCaptchaHttpRes(resp, captcha.getImage());
	        req.getSession().setAttribute(NAME, captcha);
	    }
	    
	    public boolean validateCaptcha(HttpServletRequest request, String captcha){
	    	boolean flag = true;
	    	if (null == request.getSession().getAttribute(Captcha.NAME) 
					|| !((Captcha) request.getSession().getAttribute(Captcha.NAME)).isCorrect(captcha)) {
				request.getSession().setAttribute("message", "Invalid Captcha");
				System.out.println("Invalid captcha");
				flag = false;
			}
	    	
	    	return flag;
	    }
}
