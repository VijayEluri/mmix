package hp.radius;


import com.hp.usage.array.Array;
import com.hp.usage.array.BooleanArray;
import com.hp.usage.array.BooleanMutableArray;
import com.hp.usage.array.ByteArray;
import com.hp.usage.array.ByteMutableArray;
import com.hp.usage.array.CharArray;
import com.hp.usage.array.CharMutableArray;
import com.hp.usage.array.DoubleArray;
import com.hp.usage.array.DoubleMutableArray;
import com.hp.usage.array.FloatArray;
import com.hp.usage.array.FloatMutableArray;
import com.hp.usage.array.IntegerArray;
import com.hp.usage.array.IntegerMutableArray;
import com.hp.usage.array.LongArray;
import com.hp.usage.array.LongMutableArray;
import com.hp.usage.array.ShortArray;
import com.hp.usage.array.ShortMutableArray;
import com.hp.usage.array.StringArray;
import com.hp.usage.array.StringMutableArray;
import com.hp.usage.nme.ArrayAllocator;
import com.hp.usage.nme.ArrayType;
import com.hp.usage.nme.AttributeDescriptor;
import com.hp.usage.nme.AttributeNameHelper;
import com.hp.usage.nme.AttributeNotSetException;
import com.hp.usage.nme.AttributeRef;
import com.hp.usage.nme.NME;
import com.hp.usage.nme.NMEArray;
import com.hp.usage.nme.NMEException;
import com.hp.usage.nme.NMEManager;
import com.hp.usage.nme.NMEMutableArray;
import com.hp.usage.nme.NMEType;
import com.hp.usage.nme.PrimitiveType;
import com.hp.usage.nme.Type;
import com.hp.usage.snme.SNMEImplementation;

/**
 * 
 * the assist class to do SNME operation
 * 
 * @author li wei
 * 
 */
public class SNMEHelper {

    public static NME createEmptyNME(String ns, String typeName)
            throws NMEException {
        NMEType nmetype = NMEManager.getNMESchema().getNMEType(ns, typeName);
        return createEmptyNME(nmetype);
    }

    public static NME createEmptyNME(String s) throws NMEException {
        NMEType nmetype = NMEManager.getNMESchema().getNMEType(s);
        return createEmptyNME(nmetype);
    }

    private static NME createEmptyNME(NMEType nmetype) throws NMEException {
        if (NMEManager.getNMEFactory() == null)
            NMEManager.registerNMEImpl(new SNMEImplementation());
        NME nme = NMEManager.getNMEFactory().newNME(nmetype);
        // _log.info("created Empty NME,type=" + nmetype);
        return nme;
    }

    public static void setAttribute(NME nme, String s, NME nme1)
            throws NMEException {
        AttributeRef attributeref = nme.getAttributeRef(s);
        prepareSuperNME(nme, attributeref);
        nme.setStruct(attributeref, nme1);
    }

    private static void prepareSuperNME(NME nme, AttributeRef attributeref)
            throws NMEException {
        AttributeNameHelper attributenamehelper = AttributeNameHelper
                .parseAttributeReference(attributeref.getName());
        int i = attributenamehelper.getDepth();
        NME nme1 = nme;
        for (int j = 0; j < i; j++) {
            String s = attributenamehelper.getName(j);
            int k = attributenamehelper.getIndex(j);
            AttributeRef attributeref1 = nme1.getAttributeRef(s);
            NMEType nmetype = nme1.getType();
            boolean flag = nme1.isAttributeSet(attributeref1);
            AttributeDescriptor attributedescriptor = nmetype.getAttribute(s);
            Type type = attributedescriptor.getType();
            if (type.isStructType()) {
                if (!flag) {
                    NME nme2 = createEmptyNME((NMEType) type);
                    nme1.setStruct(attributeref1, nme2);
                }
                nme1 = nme1.getStruct(attributeref1);
                continue;
            }
            if (!type.isArrayType())
                continue;
            if (((ArrayType) type).getElementType().isStructType()) {
                if (k == -1)
                    continue;
                Type type1 = ((ArrayType) type).getElementType();
                if (!flag) {
                    int i1 = Math.max(64, k + 1);
                    NMEMutableArray nmemutablearray1 = NMEManager
                            .getArrayAllocator().newNMEArray(i1);
                    prepareNMEArray(nmemutablearray1, k, (NMEType) type1);
                    nme1.setArray(attributeref1, nmemutablearray1);
                } else {
                    NMEMutableArray nmemutablearray = (NMEMutableArray) nme1
                            .getArray(attributeref1);
                    if (nmemutablearray.length() <= k)
                        prepareNMEArray(nmemutablearray, k, (NMEType) type1);
                }
                nme1 = ((NMEArray) nme1.getArray(attributeref1)).get(k);
                continue;
            }
            if (k == -1)
                continue;
            if (!flag) {
                int l = Math.max(64, k + 1);
                Array array1 = createArray(type.getID(), l);
                preparePrimitiveArray(array1, k);
                nme1.setArray(attributeref1, array1);
                continue;
            }
            Array array = nme1.getArray(attributeref1);
            if (array.length() <= k)
                preparePrimitiveArray(array, k);
        }

    }

    private static void prepareNMEArray(NMEMutableArray nmemutablearray, int i,
            NMEType nmetype) throws NMEException {
        nmemutablearray.ensureCapacity(i + 1);
        if (i >= nmemutablearray.length()) {
            int j = nmemutablearray.length();
            for (int k = j; k <= i; k++)
                nmemutablearray.add(createEmptyNME(nmetype));

        }
    }

    private static Array createArray(int i, int j) {
        ArrayAllocator arrayallocator = NMEManager.getArrayAllocator();
        switch (i) {
        case ATTR_BOOLEAN_ARRAY:
            return arrayallocator.newBooleanArray(j);

        case ATTR_BYTE_ARRAY:
            return arrayallocator.newByteArray(j);

        case ATTR_CHAR_ARRAY:
            return arrayallocator.newCharArray(j);

        case ATTR_DOUBLE_ARRAY:
            return arrayallocator.newDoubleArray(j);

        case ATTR_FLOAT_ARRAY:
            return arrayallocator.newFloatArray(j);

        case ATTR_INT_ARRAY:
            return arrayallocator.newIntegerArray(j);

        case ATTR_LONG_ARRAY:
            return arrayallocator.newLongArray(j);

        case ATTR_SHORT_ARRAY:
            return arrayallocator.newShortArray(j);

        case ATTR_STRING_ARRAY:
            return arrayallocator.newStringArray(j);

        case ATTR_SUBNME_ARRAY:
            return arrayallocator.newNMEArray(j);
        }
        throw new IllegalArgumentException("Unknown array type: " + i);
    }

    private static void preparePrimitiveArray(Array array, int i) {
        array.ensureCapacity(i + 1);
        if (i >= array.length()) {
            int j = array.length();
            for (int k = j; k <= i; k++) {
                if (array instanceof BooleanMutableArray) {
                    ((BooleanMutableArray) array).add(false);
                    continue;
                }
                if (array instanceof ByteMutableArray) {
                    ((ByteMutableArray) array).add((byte) 0);
                    continue;
                }
                if (array instanceof ShortMutableArray) {
                    ((ShortMutableArray) array).add((short) 0);
                    continue;
                }
                if (array instanceof CharMutableArray) {
                    ((CharMutableArray) array).add('\0');
                    continue;
                }
                if (array instanceof IntegerMutableArray) {
                    ((IntegerMutableArray) array).add(0);
                    continue;
                }
                if (array instanceof LongMutableArray) {
                    ((LongMutableArray) array).add(0L);
                    continue;
                }
                if (array instanceof FloatMutableArray) {
                    ((FloatMutableArray) array).add(0.0F);
                    continue;
                }
                if (array instanceof DoubleMutableArray) {
                    ((DoubleMutableArray) array).add(0.0D);
                    continue;
                }
                if (array instanceof StringMutableArray)
                    ((StringMutableArray) array).add("");
                else
                    throw new IllegalArgumentException("Unknown array type: "
                            + array.getClass().getName());
            }

        }
    }

    public static void setGeneralAttribute(NME nme, String s, Object o)
            throws NMEException {
        if (o instanceof Array) {
            AttributeRef attributeref = nme.getAttributeRef(s);
            prepareSuperNME(nme, attributeref);
            nme.setArray(attributeref, (Array) o);
        } else if (o instanceof Boolean) {
            setAttribute(nme, s, (Boolean) o);
        } else if (o instanceof Boolean[]) {
            setAttribute(nme, s, (boolean[]) o);
        } else if (o instanceof boolean[]) {
            setAttribute(nme, s, (boolean[]) o);
        } else if (o instanceof Byte) {
            setAttribute(nme, s, (Byte) o);
        } else if (o instanceof Byte[]) {
            setAttribute(nme, s, (byte[]) o);
        } else if (o instanceof byte[]) {
            setAttribute(nme, s, (byte[]) o);
        } else if (o instanceof Character) {
            setAttribute(nme, s, (Character) o);
        } else if (o instanceof Character[]) {
            setAttribute(nme, s, (char[]) o);
        } else if (o instanceof char[]) {
            setAttribute(nme, s, (char[]) o);
        } else if (o instanceof Double) {
            setAttribute(nme, s, (Double) o);
        } else if (o instanceof Double[]) {
            setAttribute(nme, s, (double[]) o);
        } else if (o instanceof double[]) {
            setAttribute(nme, s, (double[]) o);
        } else if (o instanceof Float) {
            setAttribute(nme, s, (Float) o);
        } else if (o instanceof Float[]) {
            setAttribute(nme, s, (float[]) o);
        } else if (o instanceof float[]) {
            setAttribute(nme, s, (float[]) o);
        } else if (o instanceof Integer) {
            setAttribute(nme, s, (Integer) o);
        } else if (o instanceof Integer[]) {
            setAttribute(nme, s, (int[]) o);
        } else if (o instanceof int[]) {
            setAttribute(nme, s, (int[]) o);
        } else if (o instanceof Long) {
            setAttribute(nme, s, (Long) o);
        } else if (o instanceof Long[]) {
            setAttribute(nme, s, (long[]) o);
        } else if (o instanceof long[]) {
            setAttribute(nme, s, (long[]) o);
        } else if (o instanceof Short) {
            setAttribute(nme, s, (Short) o);
        } else if (o instanceof Short[]) {
            setAttribute(nme, s, (short[]) o);
        } else if (o instanceof short[]) {
            setAttribute(nme, s, (short[]) o);
        } else if (o instanceof String) {
            setAttribute(nme, s, (String) o);
        } else if (o instanceof String[]) {
            setAttribute(nme, s, (String[]) o);
        } else if (o instanceof NME) {
            setAttribute(nme, s, (NME) o);
        } else if (o instanceof NMEArray) {
            setAttribute(nme, s, (NMEArray) o);
        } else {
            throw new RuntimeException("Attribute :" + o + " not supported!");
        }

    }

    public static void setAttribute(NME nme, String s, boolean flag)
            throws NMEException {

        AttributeRef attributeref = nme.getAttributeRef(s);
        prepareSuperNME(nme, attributeref);
        nme.setBoolean(attributeref, flag);
    }

    public static void setAttribute(NME nme, String s, boolean aflag[])
            throws NMEException {
        AttributeRef attributeref = nme.getAttributeRef(s);
        prepareSuperNME(nme, attributeref);
        ArrayAllocator arrayallocator = NMEManager.getArrayAllocator();
        BooleanMutableArray booleanmutablearray = arrayallocator
                .newBooleanArray(aflag.length);
        for (int i = 0; i < aflag.length; i++)
            booleanmutablearray.add(aflag[i]);

        nme.setArray(attributeref, booleanmutablearray);
    }

    public static void setAttribute(NME nme, String s, byte byte0)
            throws NMEException {
        AttributeRef attributeref = nme.getAttributeRef(s);
        prepareSuperNME(nme, attributeref);
        nme.setByte(attributeref, byte0);
    }

    public static void setAttribute(NME nme, String s, byte abyte0[])
            throws NMEException {
        ArrayAllocator arrayallocator = NMEManager.getArrayAllocator();
        AttributeRef attributeref = nme.getAttributeRef(s);
        prepareSuperNME(nme, attributeref);
        ByteMutableArray bytemutablearray = arrayallocator
                .newByteArray(abyte0.length);
        for (int i = 0; i < abyte0.length; i++)
            bytemutablearray.add(abyte0[i]);

        nme.setArray(attributeref, bytemutablearray);
    }

    public static void setAttribute(NME nme, String s, char c)
            throws NMEException {
        AttributeRef attributeref = nme.getAttributeRef(s);
        prepareSuperNME(nme, attributeref);
        nme.setChar(attributeref, c);
    }

    public static void setAttribute(NME nme, String s, char ac[])
            throws NMEException {
        ArrayAllocator arrayallocator = NMEManager.getArrayAllocator();
        AttributeRef attributeref = nme.getAttributeRef(s);
        prepareSuperNME(nme, attributeref);
        CharMutableArray charmutablearray = arrayallocator
                .newCharArray(ac.length);
        charmutablearray.transfer(ac, 0, 0, ac.length);
        nme.setArray(attributeref, charmutablearray);
    }

    public static void setAttribute(NME nme, String s, short word0)
            throws NMEException {
        AttributeRef attributeref = nme.getAttributeRef(s);
        prepareSuperNME(nme, attributeref);
        nme.setShort(attributeref, word0);
    }

    public static void setAttribute(NME nme, String s, short aword0[])
            throws NMEException {
        ArrayAllocator arrayallocator = NMEManager.getArrayAllocator();
        AttributeRef attributeref = nme.getAttributeRef(s);
        prepareSuperNME(nme, attributeref);
        ShortMutableArray shortmutablearray = arrayallocator
                .newShortArray(aword0.length);
        for (int i = 0; i < aword0.length; i++)
            shortmutablearray.add(aword0[i]);

        nme.setArray(attributeref, shortmutablearray);
    }

    public static void setAttribute(NME nme, String s, int i)
            throws NMEException {
        AttributeRef attributeref = nme.getAttributeRef(s);
        prepareSuperNME(nme, attributeref);
        nme.setInt(attributeref, i);
    }

    public static void setAttribute(NME nme, String s, int ai[])
            throws NMEException {
        ArrayAllocator arrayallocator = NMEManager.getArrayAllocator();
        AttributeRef attributeref = nme.getAttributeRef(s);
        prepareSuperNME(nme, attributeref);
        IntegerMutableArray integermutablearray = arrayallocator
                .newIntegerArray(ai.length);
        for (int i = 0; i < ai.length; i++)
            integermutablearray.add(ai[i]);

        nme.setArray(attributeref, integermutablearray);
    }

    public static void setAttribute(NME nme, String s, long l)
            throws NMEException {
        AttributeRef attributeref = nme.getAttributeRef(s);
        prepareSuperNME(nme, attributeref);
        nme.setLong(attributeref, l);
    }

    public static void setAttribute(NME nme, String s, long al[])
            throws NMEException {
        ArrayAllocator arrayallocator = NMEManager.getArrayAllocator();
        AttributeRef attributeref = nme.getAttributeRef(s);
        prepareSuperNME(nme, attributeref);
        LongMutableArray longmutablearray = arrayallocator
                .newLongArray(al.length);
        for (int i = 0; i < al.length; i++)
            longmutablearray.add(al[i]);

        nme.setArray(attributeref, longmutablearray);
    }

    public static void setAttribute(NME nme, String s, String s1)
            throws NMEException {
        AttributeRef attributeref = nme.getAttributeRef(s);
        prepareSuperNME(nme, attributeref);
        nme.setString(attributeref, s1);
    }

    public static void setAttribute(NME nme, String s, String as[])
            throws NMEException {
        AttributeRef attributeref = nme.getAttributeRef(s);
        prepareSuperNME(nme, attributeref);
        ArrayAllocator arrayallocator = NMEManager.getArrayAllocator();
        StringMutableArray stringmutablearray = arrayallocator
                .newStringArray(as.length);
        for (int i = 0; i < as.length; i++)
            stringmutablearray.add(as[i]);

        nme.setArray(attributeref, stringmutablearray);
    }

    public static void setAttribute(NME nme, String s, float f)
            throws NMEException {
        AttributeRef attributeref = nme.getAttributeRef(s);
        prepareSuperNME(nme, attributeref);
        nme.setFloat(attributeref, f);
    }

    public static void setAttribute(NME nme, String s, float af[])
            throws NMEException {
        ArrayAllocator arrayallocator = NMEManager.getArrayAllocator();
        AttributeRef attributeref = nme.getAttributeRef(s);
        prepareSuperNME(nme, attributeref);
        FloatMutableArray floatmutablearray = arrayallocator
                .newFloatArray(af.length);
        for (int i = 0; i < af.length; i++)
            floatmutablearray.add(af[i]);

        nme.setArray(attributeref, floatmutablearray);
    }

    public static void setAttribute(NME nme, String s, double d)
            throws NMEException {
        AttributeRef attributeref = nme.getAttributeRef(s);
        prepareSuperNME(nme, attributeref);
        nme.setDouble(attributeref, d);
    }

    public static void setAttribute(NME nme, String s, double ad[])
            throws NMEException {
        ArrayAllocator arrayallocator = NMEManager.getArrayAllocator();
        AttributeRef attributeref = nme.getAttributeRef(s);
        prepareSuperNME(nme, attributeref);
        DoubleMutableArray doublemutablearray = arrayallocator
                .newDoubleArray(ad.length);
        for (int i = 0; i < ad.length; i++)
            doublemutablearray.add(ad[i]);

        nme.setArray(attributeref, doublemutablearray);
    }

    public static NMEArray createSubNMEArray(NME nme, String s, int i)
            throws NMEException {
        AttributeRef attributeref = nme.getAttributeRef(s);
        Type type = attributeref.getType();
        if (!type.isArrayType())
            throw new IllegalArgumentException("SNME attr " + s
                    + " is NOT a Array Type!");
        type = ((ArrayType) type).getElementType();
        if (!type.isStructType())
            throw new IllegalArgumentException("SNME array attr " + s
                    + "'s element is NOT a NME Struct Type!");
        prepareSuperNME(nme, attributeref);
        ArrayAllocator arrayallocator = NMEManager.getArrayAllocator();
        NMEMutableArray nmemutablearray = arrayallocator.newNMEArray(i);
        for (int j = 0; j < i; j++)
            nmemutablearray.add(createEmptyNME((NMEType) type));

        setAttribute(nme, s, nmemutablearray);
        return nmemutablearray;
    }

    public static void setAttribute(NME nme, String s, NMEArray nmearray)
            throws NMEException {
        AttributeRef attributeref = nme.getAttributeRef(s);
        prepareSuperNME(nme, attributeref);
        nme.setArray(attributeref, nmearray);
    }

    public static Object getAttribute(NME nme, String s) throws NMEException {
        AttributeRef attributeref = nme.getAttributeRef(s);
        return getAttribute(nme, attributeref);
    }

    public static Object getAttribute(NME nme, AttributeRef attributeref)
            throws NMEException {
        if (!nme.isAttributeSet(attributeref))
            return null;
        Type type = attributeref.getType();
        if (type.isStringType()) {
            return nme.getString(attributeref);
        } else if (type.isStructType()) {
            return nme.getStruct(attributeref);
        } else if (type.isArrayType()) {
            return nme.getArray(attributeref);
        } else if (type.isPrimitiveType()) {
            if (type.getID() == PrimitiveType.BYTE_ID) {
                return nme.getByte(attributeref);
            } else if (type.getID() == PrimitiveType.SHORT_ID) {
                return nme.getShort(attributeref);
            } else if (type.getID() == PrimitiveType.INT_ID) {
                return nme.getInt(attributeref);
            } else if (type.getID() == PrimitiveType.LONG_ID) {
                return nme.getLong(attributeref);
            } else if (type.getID() == PrimitiveType.CHAR_ID) {
                return nme.getChar(attributeref);
            } else if (type.getID() == PrimitiveType.BOOLEAN_ID) {
                return nme.getBoolean(attributeref);
            } else if (type.getID() == PrimitiveType.FLOAT_ID) {
                return nme.getFloat(attributeref);
            } else if (type.getID() == PrimitiveType.DOUBLE_ID) {
                return nme.getDouble(attributeref);
            }
        }
        return null;
    }

    public static boolean getAttributeAsBoolean(NME nme, String s, boolean flag)
            throws NMEException {
        try {
            AttributeRef attributeref = nme.getAttributeRef(s);
            if (nme.isAttributeSet(attributeref))
                return nme.getBoolean(attributeref);
        } catch (AttributeNotSetException attributenotsetexception) {
            return flag;
        }
        return flag;
    }

    public static boolean[] getAttributeAsBooleanArray(NME nme, String s)
            throws NMEException {
        AttributeRef attributeref = nme.getAttributeRef(s);
        if (!nme.isAttributeSet(attributeref))
            return null;
        int i = nme.getArraySize(attributeref);
        if (i <= 0)
            return null;
        BooleanArray booleanarray = (BooleanArray) nme.getArray(attributeref);
        boolean aflag[] = new boolean[i];
        for (int j = 0; j < i; j++)
            aflag[j] = booleanarray.get(j);

        return aflag;
    }

    public static byte getAttributeAsByte(NME nme, String s, byte byte0)
            throws NMEException {
        try {
            AttributeRef attributeref = nme.getAttributeRef(s);
            if (nme.isAttributeSet(attributeref))
                return nme.getByte(attributeref);
        } catch (AttributeNotSetException attributenotsetexception) {
            return byte0;
        }
        return byte0;
    }

    public static byte[] getAttributeAsByteArray(NME nme, String s)
            throws NMEException {
        AttributeRef attributeref = nme.getAttributeRef(s);
        if (!nme.isAttributeSet(attributeref))
            return null;
        int i = nme.getArraySize(attributeref);
        if (i <= 0)
            return null;
        ByteArray bytearray = (ByteArray) nme.getArray(attributeref);
        byte abyte0[] = new byte[i];
        for (int j = 0; j < i; j++)
            abyte0[j] = bytearray.get(j);

        return abyte0;
    }

    public static char getAttributeAsChar(NME nme, String s, char char0)
            throws NMEException {
        try {
            AttributeRef attributeref = nme.getAttributeRef(s);
            if (nme.isAttributeSet(attributeref))
                return nme.getChar(attributeref);
        } catch (AttributeNotSetException attributenotsetexception) {
            return char0;
        }
        return char0;
    }

    public static char[] getAttributeAsCharArray(NME nme, String s)
            throws NMEException {
        AttributeRef attributeref = nme.getAttributeRef(s);
        if (!nme.isAttributeSet(attributeref))
            return null;
        int i = nme.getArraySize(attributeref);
        if (i <= 0)
            return null;
        CharArray chararray = (CharArray) nme.getArray(attributeref);
        char achar0[] = new char[i];
        for (int j = 0; j < i; j++)
            achar0[j] = chararray.get(j);

        return achar0;
    }

    public static short getAttributeAsShort(NME nme, String s, short word0)
            throws NMEException {
        try {
            AttributeRef attributeref = nme.getAttributeRef(s);
            if (nme.isAttributeSet(attributeref))
                return nme.getShort(attributeref);
        } catch (AttributeNotSetException attributenotsetexception) {
            return word0;
        }
        return word0;
    }

    public static short[] getAttributeAsShortArray(NME nme, String s)
            throws NMEException {
        AttributeRef attributeref = nme.getAttributeRef(s);
        if (!nme.isAttributeSet(attributeref))
            return null;
        int i = nme.getArraySize(attributeref);
        if (i <= 0)
            return null;
        ShortArray shortarray = (ShortArray) nme.getArray(attributeref);
        short aword0[] = new short[i];
        for (int j = 0; j < i; j++)
            aword0[j] = shortarray.get(j);

        return aword0;
    }

    public static int getAttributeAsInt(NME nme, String s, int i)
            throws NMEException {
        try {
            AttributeRef attributeref = nme.getAttributeRef(s);
            if (nme.isAttributeSet(attributeref))
                return nme.getInt(attributeref);
        } catch (AttributeNotSetException attributenotsetexception) {
            return i;
        }
        return i;
    }

    public static int[] getAttributeAsIntArray(NME nme, String s)
            throws NMEException {
        AttributeRef attributeref = nme.getAttributeRef(s);
        if (!nme.isAttributeSet(attributeref))
            return null;
        int i = nme.getArraySize(attributeref);
        if (i <= 0)
            return null;
        IntegerArray integerarray = (IntegerArray) nme.getArray(attributeref);
        int ai[] = new int[i];
        for (int j = 0; j < i; j++)
            ai[j] = integerarray.get(j);

        return ai;
    }

    public static long getAttributeAsLong(NME nme, String s, long l)
            throws NMEException {
        try {
            AttributeRef attributeref = nme.getAttributeRef(s);
            if (nme.isAttributeSet(attributeref))
                return nme.getLong(attributeref);
        } catch (AttributeNotSetException attributenotsetexception) {
            return l;
        }
        return l;
    }

    public static long[] getAttributeAsLongArray(NME nme, String s)
            throws NMEException {
        AttributeRef attributeref = nme.getAttributeRef(s);
        if (!nme.isAttributeSet(attributeref))
            return null;
        int i = nme.getArraySize(attributeref);
        if (i <= 0)
            return null;
        LongArray longarray = (LongArray) nme.getArray(attributeref);
        long al[] = new long[i];
        for (int j = 0; j < i; j++)
            al[j] = longarray.get(j);

        return al;
    }

    public static float getAttributeAsFloat(NME nme, String s, float f)
            throws NMEException {
        try {
            AttributeRef attributeref = nme.getAttributeRef(s);
            if (nme.isAttributeSet(attributeref))
                return nme.getFloat(attributeref);
        } catch (AttributeNotSetException attributenotsetexception) {
            return f;
        }
        return f;
    }

    public static float[] getAttributeAsFloatArray(NME nme, String s)
            throws NMEException {
        AttributeRef attributeref = nme.getAttributeRef(s);
        if (!nme.isAttributeSet(attributeref))
            return null;
        int i = nme.getArraySize(attributeref);
        if (i <= 0)
            return null;
        FloatArray floatarray = (FloatArray) nme.getArray(attributeref);
        float af[] = new float[i];
        for (int j = 0; j < i; j++)
            af[j] = floatarray.get(j);

        return af;
    }

    public static double getAttributeAsDouble(NME nme, String s, double d)
            throws NMEException {
        try {
            AttributeRef attributeref = nme.getAttributeRef(s);
            if (nme.isAttributeSet(attributeref))
                return nme.getDouble(attributeref);
        } catch (AttributeNotSetException attributenotsetexception) {
            return d;
        }
        return d;
    }

    public static double[] getAttributeAsDoubleArray(NME nme, String s)
            throws NMEException {
        AttributeRef attributeref = nme.getAttributeRef(s);
        if (!nme.isAttributeSet(attributeref))
            return null;
        int i = nme.getArraySize(attributeref);
        if (i <= 0)
            return null;
        DoubleArray doublearray = (DoubleArray) nme.getArray(attributeref);
        double ad[] = new double[i];
        for (int j = 0; j < i; j++)
            ad[j] = doublearray.get(j);

        return ad;
    }

    public static String getAttributeAsString(NME nme, String s, String s1)
            throws NMEException {
        try {
            AttributeRef attributeref = nme.getAttributeRef(s);
            if (nme.isAttributeSet(attributeref))
                return nme.getString(attributeref);
        } catch (AttributeNotSetException attributenotsetexception) {
            return s1;
        }
        return s1;
    }

    public static String[] getAttributeAsStringArray(NME nme, String s)
            throws NMEException {
        AttributeRef attributeref = nme.getAttributeRef(s);
        if (!nme.isAttributeSet(attributeref))
            return null;
        int i = nme.getArraySize(attributeref);
        if (i <= 0)
            return null;
        StringArray stringarray = (StringArray) nme.getArray(attributeref);
        String as[] = new String[i];
        for (int j = 0; j < i; j++)
            as[j] = stringarray.get(j);

        return as;
    }

    public static NME getAttributeAsNME(NME nme, String s) throws NMEException {
        AttributeRef attributeref = nme.getAttributeRef(s);
        if (!nme.isAttributeSet(attributeref))
            return null;
        else
            return nme.getStruct(attributeref);
    }

    public static NMEArray getAttributeAsNMEArray(NME nme, String s)
            throws NMEException {
        AttributeRef attributeref = nme.getAttributeRef(s);
        if (!nme.isAttributeSet(attributeref))
            return null;
        int i = nme.getArraySize(attributeref);
        if (i <= 0) {
            return null;
        } else {
            NMEArray nmearray = (NMEArray) nme.getArray(attributeref);
            return nmearray;
        }
    }

    static final int ATTR_BOOLEAN = 6;
    static final int ATTR_BOOLEAN_ARRAY = 16777222;
    static final int ATTR_BYTE = 1;
    static final int ATTR_BYTE_ARRAY = 16777217;
    static final int ATTR_SHORT = 2;
    static final int ATTR_SHORT_ARRAY = 16777218;
    static final int ATTR_INT = 3;
    static final int ATTR_INT_ARRAY = 16777219;
    static final int ATTR_CHAR = 5;
    static final int ATTR_CHAR_ARRAY = 16777221;
    static final int ATTR_LONG = 4;
    static final int ATTR_LONG_ARRAY = 16777220;
    static final int ATTR_FLOAT = 9;
    static final int ATTR_FLOAT_ARRAY = 16777225;
    static final int ATTR_DOUBLE = 10;
    static final int ATTR_DOUBLE_ARRAY = 16777226;
    static final int ATTR_STRING = 33554432;
    static final int ATTR_STRING_ARRAY = 50331648;
    static final int ATTR_SUBNME = 67108864;
    static final int ATTR_SUBNME_ARRAY = 83886080;

}