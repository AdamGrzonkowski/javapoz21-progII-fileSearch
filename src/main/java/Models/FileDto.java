package Models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FileDto {
    private LocalDateTime lastModifiedDt;
    private long size;
}
