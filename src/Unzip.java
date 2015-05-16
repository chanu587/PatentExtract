import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Unzip {

private static final String FILESEPARATOR = File.separator;

public static void storeZipStream(InputStream inputStream, 
String dir)
throws IOException {

ZipInputStream zis = new ZipInputStream(inputStream);
ZipEntry entry = null;
int countEntry = 0;
if (!dir.endsWith(FILESEPARATOR))
dir += FILESEPARATOR;

// check inputStream is ZIP or not
if ((entry = zis.getNextEntry()) != null) {
do {
String entryName = entry.getName();
// Directory Entry should end with FileSeparator
if (!entry.isDirectory()) {
// Directory will be created while creating file with in it.
String fileName = dir + entryName;
createFile(zis, fileName);
countEntry++;
}
} while ((entry = zis.getNextEntry()) != null);
System.out.println("No of files Extracted : " + countEntry);

} else {
throw new IOException("Given file is not a Compressed one");
}
}

public static void createFile(InputStream is, 
String absoluteFileName)
throws IOException {
File f = new File(absoluteFileName);

if (!f.getParentFile().exists())
f.getParentFile().mkdirs();
OutputStream out = new FileOutputStream(absoluteFileName);
byte[] buf = new byte[1024];
int len = 0;
while ((len = is.read(buf)) > 0) {
out.write(buf, 0, len);
}
// Close the streams
out.close();
}

public static void main(String args[]) throws Exception {

if (args.length < 1) {
System.out.println("Syntax : Unzip zipfile [extractlocation]");
return;
}

FileInputStream zis = new FileInputStream(new File(args[0]));
String dir = System.getProperty("java.io.tmpdir");
if (args.length == 2) {
dir = args[1].replace('\\', '/');
}
dir="/home/chan/NLPData";

System.out.println("Extracted to "+dir);
storeZipStream(zis, dir);
}
}