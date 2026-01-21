package THRProject.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class FakeSocketSafe extends Socket {

    private final InputStream in;
    private final OutputStream out;

    public FakeSocketSafe() throws IOException {
        // OUT finto (server → client)
        this.out = new ByteArrayOutputStream();

        // IN con header ObjectStream valido
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.flush(); // <-- HEADER SCRITTO

        this.in = new ByteArrayInputStream(bos.toByteArray());
    }

    @Override
    public InputStream getInputStream() {
        return in;
    }

    @Override
    public OutputStream getOutputStream() {
        return out;
    }
}
