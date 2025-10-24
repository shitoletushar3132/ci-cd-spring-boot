package site.tushar.data_generator.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import site.tushar.data_generator.utils.ApiResponse;

@RestController
@RequestMapping("/api/v1")
public class TestController {

    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> getHealth() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("Server is working...", "Healthy ✅"));
    }
}
