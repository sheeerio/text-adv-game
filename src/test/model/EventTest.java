package model;

import model.exceptions.LogException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Event class
 */
public class EventTest {
    private Event e;
    private Event b;
    private Date d;
    private LogException le;
    private LogException leb;

    //NOTE: these tests might fail if time at which line (2) below is executed
    //is different from time that line (1) is executed.  Lines (1) and (2) must
    //run in same millisecond for this test to make sense and pass.

    @BeforeEach
    public void runBefore() {
        le = new LogException();
        leb = new LogException("bruh");
        b = new Event("Sensor is closed");
        e = new Event("Sensor open at door");   // (1)
        d = Calendar.getInstance().getTime();   // (2)
    }

    @Test
    public void testEvent() {
        assertEquals("Sensor open at door", e.getDescription());
        assertEquals(d, e.getDate());
    }

    @Test
    public void testToString() {
        assertEquals(d.toString() + "\n" + "Sensor open at door", e.toString());
    }

    @Test
    public void equalsTest() {
        assertFalse(e.equals(b));
        assertTrue(e.equals(e));
        assertFalse(e.equals(null));
        assertFalse(e.equals(d));
    }

    @Test
    public void hashcodeTest() {
        assertNotEquals(e.hashCode(), b.hashCode());
        assertEquals(e.hashCode(), e.hashCode());
    }
}
