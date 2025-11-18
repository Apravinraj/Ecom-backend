package com.project.ecom_proj.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.ecom_proj.model.Product;
import com.project.ecom_proj.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
@RestController
@CrossOrigin
@RequestMapping("/api")
public class ProductController {
    @Autowired
    private ProductService service;

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts(){

        return new ResponseEntity<>(service.getAllProducts(), HttpStatus.OK);
    }

@GetMapping("/product/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable int id){
        Product product = service.getProductById(id);

        if(product != null)
            return new ResponseEntity<>(product,HttpStatus.OK);
        else
            return new ResponseEntity<>(product,HttpStatus.NOT_FOUND);

}




    @PostMapping("/product")
    public ResponseEntity<?> addProduct(@RequestPart Product product, @RequestPart MultipartFile imageFile) {
        try {
            System.out.println(product);
            Product product1 = service.addProduct(product, imageFile);
            return new ResponseEntity<>(product1,HttpStatus.CREATED);
        }
        catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


//    @PostMapping(value = "/product", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<String> testMultipart(
//            @RequestPart(value = "product", required = false) String productJson,
//            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {
//
//        System.out.println("Product JSON: " + productJson);
//        if (imageFile != null) {
//            System.out.println("Image received: " + imageFile.getOriginalFilename());
//        } else {
//            System.out.println("No image received");
//        }
//
//        return ResponseEntity.ok("Received multipart data!");
//    }


    @GetMapping("/product/{productId}/image")
public  ResponseEntity<byte[]> getImageByProductId(@PathVariable int productId){
        Product product = service.getProductById(productId);
        byte[] imageFile = product.getImageData();

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(product.getImageType()))
                .body(imageFile);

}

@PutMapping("/product/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable int id,@RequestPart Product product,@RequestPart MultipartFile imageFile){
    Product product1 = null;
    try {
        product1 = service.updateProduct(id,product,imageFile);
    } catch (IOException e) {
        return new ResponseEntity<>("Failed to Update",HttpStatus.BAD_REQUEST);
    }

    if(product1 != null){
            return new ResponseEntity<>("Updated",HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("Failed to Update",HttpStatus.BAD_REQUEST);

        }
}
@DeleteMapping("/product/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable int id){
        Product product = service.getProductById(id);
        if(product != null){
            service.deleteProduct(id);
            return new ResponseEntity<>("Deleted" , HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("Product not found" , HttpStatus.NOT_FOUND);

        }
}
@GetMapping("/products/search")
public ResponseEntity<List<Product>> searchProduct(@RequestParam String keyword){
        List<Product> products = service.searchProducts(keyword);
        return new ResponseEntity<>(products,HttpStatus.OK);

}
}
