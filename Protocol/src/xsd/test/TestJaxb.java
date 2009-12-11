package xsd.test;

 import java.io.File;
import java.io.FileOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;

import xsd.text.ObjectFactory;
import xsd.text.Person;
   
 public class TestJaxb {  
   
     public static void main(String[] args) {  
         try {  
             JAXBContext context = JAXBContext.newInstance(ObjectFactory.class);  
             ObjectFactory of = new ObjectFactory();  
             Person person = of.createPerson();  
             person.setAge(25);  
             person.setName("jerry");  
             person.setIsSingle(Boolean.TRUE);  
             JAXBElement<Person> e = of.createPersons(person);  
   
             // 用于输出元素  
             Marshaller marshaller = context.createMarshaller();  
             marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);  
             FileOutputStream stream;  
             stream = new FileOutputStream(new File(  
                     "C:/workspace/Eclipse/Protocol/src/xsd/text/person.xml"));  
             marshaller.marshal(e, stream);  
             System.out.println("Generate file successfull");  
   
         } catch (Exception e) {  
             e.printStackTrace();  
         }  
     }  
   
 }  
