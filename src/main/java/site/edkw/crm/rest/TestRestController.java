package site.edkw.crm.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/test")
public class TestRestController {

    @GetMapping
    public ResponseEntity<HashMap<Object, Object>> test(){
        HashMap<Object, Object> obj = new HashMap<>();
        obj.put("Hello", "World!");
        return ResponseEntity.ok(obj);
    }
}
