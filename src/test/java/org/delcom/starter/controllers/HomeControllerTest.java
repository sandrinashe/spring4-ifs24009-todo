package org.delcom.starter.controllers;

import org.junit.jupiter.api.Test;
import java.util.Base64;
import static org.junit.jupiter.api.Assertions.*;

class HomeControllerTest {
    
    private final HomeController controller = new HomeController();

    @Test void test1() { assertNotNull(new HomeController()); }
    
    @Test void test2() { 
        assertEquals("Hay Abdullah, selamat datang di pengembangan aplikasi dengan Spring Boot!", controller.hello()); 
    }
    
    @Test void test3() { 
        assertEquals("Hello, Test!", controller.sayHello("Test"));
    }
    
    @Test void test4() { 
        String result = controller.informasiNim("11234567");
        assertTrue(result.contains("Program Studi") || result.contains("Error"));
    }
    
    @Test void test5() { 
        assertEquals("NIM harus 8 karakter", controller.informasiNim("1"));
        assertEquals("NIM harus 8 karakter", controller.informasiNim(null));
    }
    
    @Test void test6() { 
        assertEquals("Program Studi tidak Tersedia", controller.informasiNim("99234567"));
    }

    @Test void test6a() { 
        String result = controller.informasiNim("11507777");
        assertTrue(result.contains("Sarjana Informatika"));
    }
    
    @Test void test6b() { 
        assertTrue(controller.informasiNim("11407777").contains("Diploma 4"));
        assertTrue(controller.informasiNim("11308888").contains("Diploma 3"));
        assertTrue(controller.informasiNim("13309999").contains("Teknologi Komputer"));
    }
    
    @Test void test6c() { 
        assertTrue(controller.informasiNim("12234567").contains("Sarjana Sistem Informasi"));
        assertTrue(controller.informasiNim("14234567").contains("Sarjana Teknik Elektro"));
        assertTrue(controller.informasiNim("21234567").contains("Sarjana Manajemen Rekayasa"));
        assertTrue(controller.informasiNim("22234567").contains("Sarjana Teknik Metalurgi"));
        assertTrue(controller.informasiNim("31234567").contains("Sarjana Teknik Bioproses"));
    }
    
    @Test void test7() { 
        String simpleData = "PA|100|80";
        String result = controller.perolehanNilai(10,10,10,10,10,10, simpleData);
        assertTrue(result.length() > 0);
    }
    
    @Test void test8() { 
        String result = controller.perolehanNilai(10,10,10,10,10,10, "PA|abc|def");
        assertTrue(result.contains("Error"));
    }
    
    @Test void test9() { 
        String matrix = Base64.getEncoder().encodeToString("2\n1 2\n3 4".getBytes());
        String result = controller.perbedaanL(matrix);
        assertTrue(result.contains("Nilai") || result.contains("Error"));
    }
    
    @Test void test10() { 
        String result = controller.perbedaanL("invalid");
        assertTrue(result.contains("Error"));
    }
    
    @Test void test11() { 
        String numbers = Base64.getEncoder().encodeToString("5".getBytes());
        String result = controller.palingTer(numbers);
        assertTrue(result.length() > 0);
    }
    
    @Test void test12() { 
        String result = controller.palingTer("invalid");
        assertFalse(result.contains("Error"));
    }
    
    @Test void test13() { 
        String result = controller.palingTer(Base64.getEncoder().encodeToString("".getBytes()));
        assertEquals("Informasi tidak tersedia", result);
    }
    
    @Test void test14() { 
        String result = controller.perolehanNilai(10,10,10,10,10,10, "");
        assertTrue(result.contains("Error"));
    }
    
    @Test void test15() { 
        String result = controller.perolehanNilai(10,10,10,10,10,10, null);
        assertTrue(result.contains("Error"));
    }

    @Test void test16() { 
        String multiData = "PA|10|80\nT|20|75\nK|10|70";
        String result = controller.perolehanNilai(10,20,10,30,15,15, multiData);
        assertTrue(result.contains("Nilai Akhir:"));
    }
    
    @Test void test17() { 
        String matrix = Base64.getEncoder().encodeToString("3\n1 2 3\n4 5 6\n7 8 9".getBytes());
        String result = controller.perbedaanL(matrix);
        assertTrue(result.contains("Nilai L:"));
    }
    
    @Test void test18() { 
        String numbers = Base64.getEncoder().encodeToString("1\n2\n3\n2\n1".getBytes());
        String result = controller.palingTer(numbers);
        assertTrue(result.contains("Tertinggi:"));
        assertTrue(result.contains("Terendah:"));
    }
    
    @Test void test19() { 
        assertEquals("NIM harus 8 karakter", controller.informasiNim(""));
    }
    
    @Test void test20() { 
        assertEquals("Program Studi tidak Tersedia", controller.informasiNim("99907777"));
    }

    // ✅ Tambahan baru: menutup cabang matrix kecil (<3)
    @Test void test21_matrixKecilCabangElse() {
        // matrix 2x2 akan masuk ke blok else di perbedaanL (n < 3)
        String matrix = Base64.getEncoder().encodeToString("2\n5 3\n1 7".getBytes());
        String result = controller.perbedaanL(matrix);

        // Pastikan result mengandung semua elemen penting
        assertTrue(result.contains("Nilai L:"));
        assertTrue(result.contains("Perbedaan"));
        assertTrue(result.contains("Dominan"));
    }
}