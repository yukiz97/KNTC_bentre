package ngn.kntc.modules;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

public class KNTCBaseFunction {
	public static String encodeFileToBase64Binary(String fileName) throws IOException {
		String fileBase64 = "";
	    File file = new File(fileName);
	    byte[] encoded = Files.readAllBytes(file.toPath());
	    fileBase64 = Base64.getEncoder().encodeToString(encoded);

	    return fileBase64;
	}
}
