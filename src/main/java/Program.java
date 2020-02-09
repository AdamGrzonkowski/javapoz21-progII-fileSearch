import Files.*;
import Models.FileDto;

import java.util.Map;
import java.util.Scanner;

public class Program {
    public static void main(String args[]){
        try{
            Scanner in = new Scanner(System.in);
            System.out.println("Type in path:");
            String path = in.nextLine();

            System.out.println("Type in max allowed size:");
            long maxSize = in.nextLong();

            System.out.println("Type in days to go back:");
            int daysToGoBack = in.nextInt();

            IFileService service = new FileService(daysToGoBack, maxSize);

            System.out.println("Scanning for big files...");
            Map<String, FileDto> result = service.GetBigFiles(path);

            result.forEach((filePath, fileDto) -> {
                System.out.println(filePath + "\t|\t" + fileDto.getSize() + " MB" + "\t|\t" + fileDto.getLastModifiedDt());
            });
        }catch(Exception ex){
            System.out.println(ex);
        }
    }
}