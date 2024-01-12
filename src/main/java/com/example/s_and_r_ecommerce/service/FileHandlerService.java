package com.example.s_and_r_ecommerce.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileHandlerService {
    /*To handle the images, it is necessary to create a new service class
    in which we specify where to save/find to delete those images */
    private String folder="src/main/resources/static/images//";
    public String saveImage(MultipartFile file) throws IOException {
        if(!file.isEmpty()){
            byte[] bytes = file.getBytes();
            Path path = Paths.get(folder+file.getOriginalFilename());
            Files.write(path,bytes);
            return file.getOriginalFilename();
        }
        return "src/main/resources/static/images/default.png";
    }
    public void delete(String name){
        String directory="src/main/resources/static/images//";
        File file = new File(directory+name);
        file.delete();
    }
}
