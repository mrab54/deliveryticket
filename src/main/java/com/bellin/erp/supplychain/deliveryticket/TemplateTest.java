package com.bellin.erp.supplychain.deliveryticket;
import java.io.StringWriter;
import java.util.HashMap;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.tools.generic.DisplayTool;


public class TemplateTest {

    public static void main( String[] args) throws Exception {
        TemplateTest app = new TemplateTest();
        app.runit();

    }

    public void runit() {

        //helloWorld();
        deliveryTicket();
    }

    public void deliveryTicket() {
        VelocityEngine ve = new VelocityEngine();
        ve.init();
        Template t = ve.getTemplate("deliveryticket.vm");
        VelocityContext context = new VelocityContext();
        //context.put("name", "World");
        //Person p = new Person("Bob   ", 10);
        Person p = new Person("   Bob", 10);
        context.put("person", p);
        context.put("display", new DisplayTool());
        StringWriter writer = new StringWriter();
        t.merge(context, writer);
        System.out.println(writer.toString());


//        public String cell(Object obj,
//                String suffix)
//
//        Truncates or pads the string value of the specified object as necessary to ensure that the returned string's length equals the default cell size. If truncation is necessary, the specified suffix will replace the end of the string value to indicate that.
//
//        Parameters:
//        obj - the value to be put in the 'cell'
//        suffix - the suffix to put at the end of any values that need truncating to indicate that they've been truncated

        // look into this
        //File file = BuildHelper.getFile(packagePath, "IJpaStub", "");
        //BuildHelper.writeContentToFile(writer.toString(), file);

    }

    public void helloWorld(){
        VelocityEngine ve = new VelocityEngine();
        ve.init();

        Template t = ve.getTemplate("helloworld.vm");
        VelocityContext context = new VelocityContext();
        context.put("name", "World");
        StringWriter writer = new StringWriter();
        t.merge(context, writer);
        System.out.println(writer.toString());


    }

}
