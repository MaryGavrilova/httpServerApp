package ru.netology;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

import static ru.netology.Server.VALID_PATHS;

public class RequestHandler implements Runnable {
    public static final int NUMBER_OF_REQUEST_LINE_PARTS = 3;
    protected Socket socket;

    public RequestHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        while (!socket.isClosed()) {
            try (final BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 final BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream())) {
                final String requestLine = in.readLine();
                final String[] parts = requestLine.split(" ");
                if (parts.length != NUMBER_OF_REQUEST_LINE_PARTS) {
                    socket.close();
                    break;
                }
                final String path = parts[1];
                if (!VALID_PATHS.contains(path)) {
                    out.write((
                            "HTTP/1.1 404 Not Found\r\n" +
                                    "Content-Length: 0\r\n" +
                                    "Connection: close\r\n" +
                                    "\r\n"
                    ).getBytes());
                    out.flush();
                    socket.close();
                    break;
                }
                final Path filePath = Path.of(".", "public", path);
                final String mimeType = Files.probeContentType(filePath);

                // special case for classic
                if (path.equals("/classic.html")) {
                    final String template = Files.readString(filePath);
                    final byte[] content = template.replace(
                            "{time}",
                            LocalDateTime.now().toString()
                    ).getBytes();
                    out.write((
                            "HTTP/1.1 200 OK\r\n" +
                                    "Content-Type: " + mimeType + "\r\n" +
                                    "Content-Length: " + content.length + "\r\n" +
                                    "Connection: close\r\n" +
                                    "\r\n"
                    ).getBytes());
                    out.write(content);
                    out.flush();
                    socket.close();
                    break;
                }

                final long length = Files.size(filePath);
                out.write((
                        "HTTP/1.1 200 OK\r\n" +
                                "Content-Type: " + mimeType + "\r\n" +
                                "Content-Length: " + length + "\r\n" +
                                "Connection: close\r\n" +
                                "\r\n"
                ).getBytes());
                Files.copy(filePath, out);
                out.flush();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
