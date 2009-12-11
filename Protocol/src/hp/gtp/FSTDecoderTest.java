package hp.gtp;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.util.Iterator;

import junit.framework.TestCase;

import com.hp.siu.utils.Config;
import com.hp.usage.codec.DefaultListener;
import com.hp.usage.fdl.DocumentReader;
import com.hp.usage.fdl.xfdgen;
import com.hp.usage.fst.codec.FSTDecodeListener;
import com.hp.usage.fst.codec.FSTEncodeListener;
import com.hp.usage.fst.nme.SNMEMappingProvider;
import com.hp.usage.nme.NME;
import com.hp.usage.nme.NMEManager;
import com.hp.usage.nme.NMESchema;
import com.hp.usage.nme.NMEType;
import com.hp.usage.nme.NamespaceNotFoundException;
import com.hp.usage.nme.schemaloader.NMESchemaLoader;
import com.hp.usage.snme.SNMEImplementation;
import com.hp.usage.tlv.codec.TLVConfigSource;
import com.hp.usage.tlv.codec.TLVDecoder;
import com.hp.usage.tlv.codec.TLVEncoder;

public class FSTDecoderTest extends TestCase{
	
    public static String debugInfo() {
        StringBuilder builder = new StringBuilder();
        for (Iterator iterator = NMEManager.getNMESchema().getNamespaces(); iterator
                .hasNext();) {
            String nsname = (String) iterator.next();
            builder.append(nsname);
            try {
                for (Iterator iterator2 = NMEManager.getNMESchema()
                        .getNMETypes(nsname); iterator2.hasNext();) {
                    NMEType type = (NMEType) iterator2.next();
                    builder.append(type);
                }
            } catch (NamespaceNotFoundException e) {
                e.printStackTrace();
            }
        }
        return builder.toString();
    }

    public void testGTPp() throws Exception{
    	String[] args = new String[]{"GTPp.xfd"};
    	main(args);
    }
    public void testQM() throws Exception{
    	String[] args = new String[]{"QM.xfd"};
    	main(args);
    }
    
    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        TLVConfigSource configSource = new TLVConfigSource();
        String xfdLocation;
        if(args.length==0||args[0]==null){
        	xfdLocation = "GTPp.xfd";
        }else{
        	xfdLocation = args[0];
        }
        xfdLocation = "QM.xfd";
        InputStream cis = FSTDecoderTest.class.getClassLoader().getResourceAsStream(
                xfdLocation);
        InputStreamReader reader = new InputStreamReader(cis);
        DocumentReader xfdDoc = new DocumentReader(reader);
        
        Config snmeSchemaConfig = xfdgen.generateSNMESchema(xfdDoc);
        NMEManager.registerNMEImpl(new SNMEImplementation());
        NMESchema schema = NMEManager.getNMESchema();
        NMESchemaLoader loader = new NMESchemaLoader();
         
        loader.loadNMESchema(snmeSchemaConfig, schema);
        //System.out.println(schema);
        
        //System.err.println(debugInfo());
        configSource.configure(xfdDoc);
        reader.close();

        
        TLVDecoder decoder = new TLVDecoder(new SNMEMappingProvider(),
                configSource);
        FSTDecodeListener decodeListener = new FSTDecodeListener();
        decoder.setListener(decodeListener);
        decoder.setErrorListener(new DefaultListener());
        TLVEncoder encoder = new TLVEncoder(new SNMEMappingProvider(),
                configSource);
        FSTEncodeListener encodeListener = new FSTEncodeListener();
        encoder.setListener(encodeListener);
        encoder.setErrorListener(new DefaultListener());

        //encode
        NMEType nmetype = NMEManager.getNMESchema().getNMEType("GTPp:GTPpType");
        
        NME nme = NMEManager.getNMEFactory().newNME(nmetype);
        
        nme.setInt(nme.getAttributeRef("protocol_Type"), 1);
        
//        nme.set(nme.getAttributeRef("authenticator"), "arg1".getBytes());
        
        System.out.println(nme);
        
        encodeListener.setNME(nme);
        ByteBuffer encodeBuffer = ByteBuffer.allocate(1024);
        encoder.encode(null, encodeBuffer);
        encodeListener.setNME(null);
        encoder.close(encodeBuffer);
        
        
        //decode
        encodeBuffer.flip();
        System.out.println(encodeBuffer);
        
        
        
        while (decoder.decode(encodeBuffer)) {
             nme = decodeListener.getNME();
             System.out.println(nme);
        }
        
//        long beginTime = System.currentTimeMillis();
//        int count = 1;
//        for (int i = 0; i < count; i++) {
//            FileInputStream is = new FileInputStream("ccr.dump");
//            ByteBuffer buffer = ByteBuffer.allocate(1024);
//            is.getChannel().read(buffer);
//            is.close();
//            buffer.flip();
//            while (decoder.decode(buffer)) {
//                NME nme = decodeListener.getNME();
//                System.out.println(nme);
//                encodeListener.setNME(nme);
//                ByteBuffer encodeBuffer = ByteBuffer.allocate(1024);
//                encoder.encode(null, encodeBuffer);
//                encodeListener.setNME(null);
//                encoder.close(encodeBuffer);
//                FileOutputStream os = new FileOutputStream("test2.dump");
//                os.getChannel().write(encodeBuffer);
//            }
//        }
//        long endTime = System.currentTimeMillis();
//        long totalTime = endTime - beginTime;
//        System.out.println("total time: " + totalTime);
//        System.out.println("rate: " + count * 1000 / totalTime);
    }

}
