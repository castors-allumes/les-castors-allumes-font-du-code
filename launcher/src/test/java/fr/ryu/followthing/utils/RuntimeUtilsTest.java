package fr.ryu.followthing.utils;

import org.junit.Test;

import static org.junit.Assert.fail;

public class RuntimeUtilsTest {

    @Test(expected = IllegalArgumentException.class)
    public void testIsNotNullKo() throws Exception {
        RuntimeUtils.isNotNull(null, IllegalArgumentException.class, null);
    }
    
    @Test
    public void testIsNotNullOk() throws Exception {
        try {
            RuntimeUtils.isNotNull("", IllegalArgumentException.class, null);
        }catch (RuntimeException e){
            fail("No exception must be raised : object is not null");
        }
    }

    @Test(expected = RuntimeException.class)
    public void testIsNotNullDefaultKo() throws Exception {
        RuntimeUtils.isNotNull(null);
    }

    @Test
    public void testIsNotNullDefaultOk() throws Exception {
        try {
            RuntimeUtils.isNotNull("");
        }catch (RuntimeException e){
            fail("No exception must be raised : object is not null");
        }
    }

    @Test
    public void testIsNotEmptyDefaultOk() throws Exception {
        try {
            RuntimeUtils.isNotEmpty("NotEmptyString");
        }catch (RuntimeException e){
            fail("No exception must be raised : string is not null");
        }
    }

    @Test(expected = RuntimeException.class)
    public void testIsNotEmptyDfaultKo() throws Exception {
            RuntimeUtils.isNotEmpty("");
    }

    @Test(expected = RuntimeException.class)
    public void testIsNotEmptyDefaultKoNull() throws Exception {
        RuntimeUtils.isNotEmpty(null);
    }

    @Test
    public void testIsNotEmptyOk() throws Exception {
        try {
            RuntimeUtils.isNotEmpty("NotEmptyString",IllegalArgumentException.class,null);
        }catch (RuntimeException e){
            fail("No exception must be raised : string is not null");
        }
    }

    @Test(expected = RuntimeException.class)
    public void testIsNotEmptyKo() throws Exception {
        RuntimeUtils.isNotEmpty("",IllegalArgumentException.class,null);
    }

    @Test(expected = RuntimeException.class)
    public void testIsNotEmptyKoNull() throws Exception {
        RuntimeUtils.isNotEmpty(null,IllegalArgumentException.class,null);
    }
}