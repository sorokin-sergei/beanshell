package ru.volgablob.rest;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import bsh.Interpreter;

@RestController
@RequestMapping(value="/beanshell")
class BeanshellController{
	private final Logger log = LoggerFactory.getLogger(BeanshellController.class);

	// run external script
	@CrossOrigin(origins = "*", allowedHeaders = "*") 				// set up for restricted access
	@RequestMapping(value="/script",method = RequestMethod.POST) 	// POST is secured
	public String runExternalScript(@RequestParam(name = "file") String file,  
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		log.info("Running external script, file = "+file);
		Interpreter i = new Interpreter();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        i.setOut(ps);
		i.source(file);
        String out = baos.toString();
		log.info("Result = "+out);
		return out;
	}
	
	// run an OS command
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@RequestMapping(value="/command",method = RequestMethod.POST) 
	public String runExternalCommand(@RequestParam(name = "command") String command,  
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		log.info("Running external command = "+command);
		Interpreter i = new Interpreter();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        i.setOut(ps);
		i.eval("exec(\""+command+"\")");
        String out = baos.toString();
		log.info("Result = "+out);
		return out;

	}
}
