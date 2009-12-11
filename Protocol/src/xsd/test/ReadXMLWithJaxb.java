package xsd.test;

 import java.io.FileInputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;

import xsd.text.ObjectFactory;
import xsd.text.Person;
import xsd.text.Rank;
   
 public class ReadXMLWithJaxb {  
   
     @SuppressWarnings("unchecked")  
     public static void main(String[] args) {  
         try {  
             JAXBContext context;  
             context = JAXBContext.newInstance(ObjectFactory.class);  
             Unmarshaller marshaller = context.createUnmarshaller();  
             JAXBElement element = (JAXBElement) marshaller.unmarshal(new FileInputStream(  
                     "C:/workspace/Eclipse/Protocol/src/xsd/text/person_2.xml"));  
             // 读取出根结点  
             Person person = (Person) element.getValue();  
   
             // 获取name值  
             String name = person.getName();  
   
             System.out.println(name);  
   
             // 获取另一子结点  
             Rank rank = person.getRank();  //Null Pointer if not defined in xml.
               
             String accountId = rank.getAccountId();  
             double money = rank.getMoney();  
               
             System.out.println(accountId);  
             System.out.println(money);  
         } catch (Exception e) {  
             e.printStackTrace();  
         }  
   
     }  
 }  
