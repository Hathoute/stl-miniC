package fr.n7.stl.block;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;

@RunWith(Parameterized.class)
public class TestCompiler {

    private static String outFile = "./output.tam";
    private static String testBaseDir = "./tests/tests_ok/";
    private File file;

    public TestCompiler(File file) {
        this.file = file;
    }

    @Parameterized.Parameters(name="{0}")
    public static Collection<Object[]> data() {
        File dir = new File(testBaseDir);
        File[] testFiles = dir.listFiles();
        testFiles = testFiles == null ? new File[0] : testFiles;

        Collection<Object[]> data = new ArrayList<>();
        for(File file : testFiles) {
            data.add(new Object[]{file});
        }

        return data;
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void test() throws Exception {
        System.out.println("Testing " + file.getName());
        ignoreOutput();
        Driver.main(new String[] {file.getPath()});
        restoreOutput();

        String expected = grabOutput(file.getPath());
        String output = executeCode();
        assertEquals("Test output mismatch for " + file, expected, output.substring(0, output.length() - 1));
    }

    private PrintStream out;
    private void ignoreOutput() {
        out = System.out;
        System.setOut(new PrintStream(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                //
            }
        }));
    }

    private void restoreOutput() {
        System.setOut(out);
    }

    private String grabOutput(String source) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(source));
        String line = br.readLine();
        br.close();
        return line.substring(2);
    }

    private String executeCode() throws IOException {
        String cmd = "java -jar ./tools/runtam.jar " + outFile;
        String[] args = new String[] {"java", "-jar", "./tools/runtam.jar", outFile};
        ProcessBuilder pb = new ProcessBuilder(args);
        pb.redirectErrorStream(true);
        Process proc = pb.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        StringBuilder builder = new StringBuilder();
        String line = "";
        while((line = reader.readLine()) != null) {
            builder.append(line).append("\n");
        }

        reader.close();
        return builder.toString();
    }
}
