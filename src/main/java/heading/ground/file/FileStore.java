package heading.ground.file;

import heading.ground.entity.ImageFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
public class FileStore {

    @Value("${file.dir}")
    private String fileDir;

    public String getFullPath(String fileName){
        return fileDir + fileName;
    }

    public ImageFile storeFile(MultipartFile multipartFile) throws IOException {
        if(multipartFile.isEmpty()){
            return null;
        }
        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);

        multipartFile.transferTo(new File(getFullPath(storeFileName)));

        return new ImageFile(originalFilename,storeFileName);
    }

    public void deleteImage(String imageName){
        File file = new File(fileDir + imageName);
        file.delete();
    }


    private String createStoreFileName(String originalName){
        String s = UUID.randomUUID().toString();
        String ext = extractEXT(originalName);

        return s + "." + ext;
    }

    private String extractEXT(String originalName) {
        int pos = originalName.lastIndexOf(".");
        return originalName.substring(pos+1);
    }

}
