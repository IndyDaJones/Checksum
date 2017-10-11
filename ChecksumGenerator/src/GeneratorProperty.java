import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class GeneratorProperty {
	Properties properties = new Properties();
	public GeneratorProperty(){
		BufferedInputStream stream;
		try {
			//Prod
			stream = new BufferedInputStream(new FileInputStream("config/service.property"));
						properties.load(stream);
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	public String getServiceProperty(String key){
		return properties.getProperty(key);
	}
}
