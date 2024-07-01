package hust.server.domain.products.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadQrcodeRequest {
    private File file;
    private String upload_preset;
    private String api_key;
}
