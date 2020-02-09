package Files;

import Models.FileDto;

import java.util.Map;

public interface IFileService {
    /**
     * Returns Map of big files a given directory (and its subdirectories).
     * @param path Root directory, for which we want to traverse through sub-directories.
     * @return Map with files' paths and their sizes.
     */
    Map<String, FileDto> GetBigFiles(String path);
}
