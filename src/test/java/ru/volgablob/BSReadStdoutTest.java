package ru.volgablob;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bsh.EvalError;
import bsh.Interpreter;

public class BSReadStdoutTest 
{
	private static final Logger log = LoggerFactory.getLogger(BSReadStdoutTest.class);
	@Test
    public void testEval() throws EvalError
    {
        log.info("Running beanshell..." );
        Interpreter i = new Interpreter();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        i.setOut(ps);
        i.eval("exec(\"grep MemFree /proc/meminfo\")");
        String out = baos.toString();
        log.info("Output = "+out);
    }
}
