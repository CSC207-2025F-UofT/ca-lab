package entity;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GalleryTest {
    @Test
    void testGalleryWithPetList() {
        Pet p1 = new Pet("1", "Bella", "Dog", "Husky", "Young", "Female", "Medium", "555-1111");
        Pet p2 = new Pet("2", "Milo", "Cat", "Siamese", "Adult", "Male", "Small", "555-2222");

        List<Pet> pets = Arrays.asList(p1, p2);
        Gallery gallery = new Gallery(pets);

        assertFalse(gallery.isEmpty());
        assertEquals(2, gallery.size());
        assertEquals(pets, gallery.getPets());

        assertThrows(UnsupportedOperationException.class, () -> {
            gallery.getPets().remove(0);
        });
    }

}
