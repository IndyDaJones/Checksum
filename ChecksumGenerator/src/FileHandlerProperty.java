import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class FileHandlerProperty {
	Properties properties = new Properties();
	public FileHandlerProperty(){
		BufferedInputStream stream;
		try {
		//Prod
		stream = new BufferedInputStream(new FileInputStream("config/file.property"));
					properties.load(stream);
	} catch (FileNotFoundException e) {
		System.out.println(e.getMessage());
	} catch (IOException e) {
		System.out.println(e.getMessage());
	}
}
public String getFileProperty(String key){
	return properties.getProperty(key);
}
}
