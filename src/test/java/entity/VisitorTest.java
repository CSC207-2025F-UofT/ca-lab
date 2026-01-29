package entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VisitorTest {

    @Test
    public void testFewerInfoVisitor() {
        Visitor v = new Visitor("visitor", "visitor12345");

        assertEquals("visitor", v.getName());
        assertEquals("visitor12345", v.getPassword());
        assertEquals("visitor", v.getUserType());
        assertEquals("", v.getFirstName());
        assertEquals("", v.getLastName());
        assertEquals("", v.getAddress());
        assertEquals("", v.getHomeEvi());
        assertEquals("", v.getOccupation());
        assertEquals("", v.getAge());
        assertEquals("", v.getEmail());
        assertEquals("", v.getTel());

    }

    @Test
    public void testCompleteInfoVisitor() {
        Visitor v = new Visitor("visitor", "visitor12345");
        v.setFirstName("R");
        v.setLastName("L");
        v.setAddress("Fifth Avenue");
        v.setHomeEvi("good");
        v.setOccupation("student");
        v.setAge("20");
        v.setEmail("visitor.@gmail.com");
        v.setTel("123456789");

        assertEquals("visitor", v.getName());
        assertEquals("visitor12345", v.getPassword());
        assertEquals("visitor", v.getUserType());
        assertEquals("R", v.getFirstName());
        assertEquals("L", v.getLastName());
        assertEquals("Fifth Avenue", v.getAddress());
        assertEquals("good", v.getHomeEvi());
        assertEquals("student", v.getOccupation());
        assertEquals("20", v.getAge());
        assertEquals("visitor.@gmail.com", v.getEmail());
        assertEquals("123456789", v.getTel());

    }
}
