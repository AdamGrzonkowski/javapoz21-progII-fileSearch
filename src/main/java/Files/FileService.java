package Files;

import Models.FileDto;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.TreeMap;

@Getter
@Setter
public class FileService implements IFileService {

    Map<String, FileDto> filesToShow = new TreeMap<>();
    private int daysToGoBack;
    private long minAllowedFileSize;

    public FileService(int daysToGoBack, long maxAllowedFileSize){
        this.setDaysToGoBack(daysToGoBack);
        this.setMinAllowedFileSize(maxAllowedFileSize);
    }

    /**
     * {@inheritDoc}
     */
    public Map<String, FileDto> GetBigFiles(String path) {
        File fileOrDirectory = new File(path);
        CheckFiles(fileOrDirectory);

        return filesToShow;
    }

    /**
     * Traverses through directory tree and returns info about files.
     * @param fileOrDirectory Root directory, for which we want to traverse through sub-directories.
     * @return Map with files' paths and their sizes.
     */
    private Map<String, FileDto> CheckFiles(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            for (File file : fileOrDirectory.listFiles()) {
                if (file.isFile()) {
                    ProcessFile(file);
                } else if (file.isDirectory()) {
                    CheckFiles(file); // recursive invocation
                }
            }
        }
        else {
            ProcessFile(fileOrDirectory);
        }
        return null;
    }

    /**
     * Processes file (checks whether it should be added to results and if so, gets its size and lastModifiedDate).
     * @param file
     */
    private void ProcessFile(File file) {
        long fileSize = GetFileSize(file.length());

        if (fileSize > this.getMinAllowedFileSize()){
            LocalDateTime lastModifiedDt = ParseDateTime(file.lastModified());

            if (LastModifiedDateIsInRange(lastModifiedDt)){
                FileDto dto = new FileDto();
                dto.setLastModifiedDt(lastModifiedDt);
                dto.setSize(fileSize);

                filesToShow.put(file.getAbsolutePath(), dto); // there can be no two files with same name in a given path, so we can safely use absolutePath as key in a Map or Dictionary
            }
        }
    }

    /**
     * Checks whether file was last modified in a given datetime range.
     * @param date
     * @return
     */
    public boolean LastModifiedDateIsInRange(LocalDateTime date) {
        LocalDateTime daysToGoBack = LocalDateTime.now().plusDays(this.getDaysToGoBack());
        return date.isBefore(daysToGoBack);
    }

    /**
     * Converts size from bytes to megabytes.
     * @param size
     * @return
     */
    private long GetFileSize(long size) {
        return size / 1024 / 1024;
    }

    /**
     * Parses date/time from long to LocalDateTime.
     * @param fileModificationTime
     * @return
     */
    private LocalDateTime ParseDateTime(long fileModificationTime){
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(fileModificationTime), ZoneId.systemDefault());
    }
}
